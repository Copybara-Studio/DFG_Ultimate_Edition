/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.dfg;

import java.util.*;
import my.expression.*;
import my.sfc.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Graph
{
    private final List<Node> nodes;
    private final List<Edge> edges;

    public Graph()
    {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    private void joinNodes(Node tail, Node head, EdgeAttribute attribute)
    {
        Edge edge = new Edge(attribute);
        edge.setTail(tail);
        edge.setHead(head);
        edges.add(edge);
    }

    private void joinNodes(Node tail, Node head)
    {
        joinNodes(tail, head, EdgeAttribute.SIMPLE);
    }

    class ExpressionStructure
    {
        private Node outputNode;

        class Element
        {
            private final Node node;
            private int counter;

            public Element(Node node, int counter)
            {
                this.node = node;
                this.counter = counter;
            }

            public Node useNode()
            {
                --counter;
                return node;
            }

            public boolean isDone()
            {
                return counter == 0;
            }
        }

        public ExpressionStructure(Expression expression)
        {
            outputNode = null;
            Term term;
            EdgeAttribute attribute = EdgeAttribute.SIMPLE;
            Stack<Element> stack = new Stack<>();
            for (int i = 0; i < expression.size(); ++i)
            {
                term = expression.get(i);
                Node node;
                if (term.getType() == TermType.OPERATION)
                {
                    Operation operation = ((OperationTerm) term).getOperation();
                    if (operation == Operation.NEGATION)
                    {
                        attribute = EdgeAttribute.NEGATION;
                        continue;
                    }
                    else
                    {
                        node = new OperationNode(operation == Operation.DISJUNCTION
                                ? NodeOperation.DISJUNCTION : NodeOperation.CONJUNCTION);
                        if (!stack.isEmpty())
                        {
                            joinNodes(node, stack.peek().useNode(), attribute);
                            if (stack.peek().isDone())
                            {
                                stack.pop();
                            }
                            attribute = EdgeAttribute.SIMPLE;
                        }
                        stack.push(new Element(node, 2));
                    }
                }
                else
                {
                    String label = ((VariableTerm) term).getLabel();
                    node = new ReadingNode(label);
                    if (!stack.isEmpty())
                    {
                        joinNodes(node, stack.peek().useNode(), attribute);
                        if (stack.peek().isDone())
                        {
                            stack.pop();
                        }
                        attribute = EdgeAttribute.SIMPLE;
                    }
                }
                nodes.add(node);
                if (outputNode == null)
                {
                    outputNode = node;
                }
            }
        }

        public Node getOutputNode()
        {
            return outputNode;
        }
    }

    class StepStructure
    {
        private final Node settingNode;
        private final Node clearingNode;
        private final Node outputNode;

        public StepStructure(Step step)
        {
            settingNode = new OperationNode(NodeOperation.DISJUNCTION);
            clearingNode = new OperationNode(NodeOperation.CONJUNCTION);
            outputNode = new OperationNode(NodeOperation.DISJUNCTION);
            Node conjunction = new OperationNode(NodeOperation.CONJUNCTION);
            Node getter = new ReadingNode(step.getLabel() + ".token");
            Node setter = new WritingNode(step.getLabel() + ".token");
            nodes.add(settingNode);
            nodes.add(conjunction);
            nodes.add(clearingNode);
            nodes.add(outputNode);
            nodes.add(getter);
            nodes.add(setter);
            joinNodes(settingNode, conjunction, EdgeAttribute.SIMPLE);
            joinNodes(conjunction, outputNode, EdgeAttribute.SIMPLE);
            joinNodes(clearingNode, outputNode, EdgeAttribute.SIMPLE);
            joinNodes(getter, conjunction, EdgeAttribute.NEGATION);
            joinNodes(getter, clearingNode, EdgeAttribute.SIMPLE);
            joinNodes(outputNode, setter, EdgeAttribute.SIMPLE);
        }

        public Node getSettingNode()
        {
            return settingNode;
        }

        public Node getClearingNode()
        {
            return clearingNode;
        }

        public Node getOutputNode()
        {
            return outputNode;
        }
    }

    public void constructGraph(SFC sfc)
    {
        Map<Step, StepStructure> stepStructures = new HashMap<>();
        List<Step> steps = sfc.getSteps();

        /* Create the structures representing steps. */
        for (var step : steps)
        {
            stepStructures.put(step, new StepStructure(step));
        }

        /* Join. */
        for (var step : steps)
        {
            /* Activation condition. */
            List<Transition> transitions = step.getPrecedingTransitions();
            for (var transition : transitions)
            {
                var conjunction = new OperationNode(NodeOperation.CONJUNCTION);

                Expression condition = transition.getCondition();
                var transitionCondition = new ExpressionStructure(condition);
                joinNodes(transitionCondition.getOutputNode(), conjunction);

                List<Step> predecessors = transition.getPredecessors();
                for (var predecessor : predecessors)
                {
                    var node = stepStructures.get(predecessor).getOutputNode();
                    joinNodes(node, conjunction);
                }

                var setter = stepStructures.get(step).getSettingNode();
                joinNodes(conjunction, setter);
            }

            /* Deactivation condition. */
            transitions = step.getSucceedingTransitions();
            Node clearer = stepStructures.get(step).getClearingNode();
            for (var transition : transitions)
            {
                Expression condition = transition.getCondition();
                var transitionCondition = new ExpressionStructure(condition);
                joinNodes(transitionCondition.getOutputNode(), clearer);
            }
        }
    }
}
