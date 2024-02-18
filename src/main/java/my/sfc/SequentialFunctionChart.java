/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.sfc;

import my.expression.Expression;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// TODO -> more like remark tbh, I would add Variable as temporary class, I am little dum dum RN, maybe we will figure it out

/**
 * Program in form of a Sequential Function Chart.
 * 
 * Definition 3.13., page 85.
 * 
 * @author Kay Jay O'Nail
 */
public class SequentialFunctionChart
{
    /*
     * Wszystkie kroki i inne komponenty są w components.
     * Do initialSteps trzeba tylko 'dorzucić' te kroki (referencje do już
     * istniejących), które mają być początkowo aktywne.
     */
    private Collection<Component> components = new ArrayList<>();
    private Collection<Step> initialSteps = new ArrayList<>();
//    private Collection<Transition> transitions = new ArrayList<>();
    private Collection<String> vars = new ArrayList<>(); // you can treat it as a map -> variables in transitions

    public void readFromXML(String fileName) throws Exception {
        File file = new File(fileName);
        System.out.println("File path : " + file.getAbsolutePath());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName("variable");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            addVarToSFC(nodeList.item(i));
        }
        nodeList = document.getElementsByTagName("step");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            addStepToSFC(nodeList.item(i));
        }
        nodeList = document.getElementsByTagName("transition");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            addTransitionToSFC(nodeList.item(i));
        }
        nodeList = document.getElementsByTagName("simultaneousConvergence");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            addConvergenceToSFC(nodeList.item(i));
        }
        nodeList = document.getElementsByTagName("simultaneousDivergence");
        for (int i = 0; i < nodeList.getLength(); i++)
        {
            addDivergenceToSFC(nodeList.item(i));
        }
    }

    private void addTransitionToSFC(Node node) throws Exception {
        int id = Integer.parseInt(node.getAttributes().getNamedItem("localId").getNodeValue());
        String condition = "";
        int source = -1;

        NodeList transitionChildren = node.getChildNodes();
        for (int j = 0; j < transitionChildren.getLength(); j++) {
            Node transitionChild = transitionChildren.item(j);
            if (transitionChild.getNodeName().equals("connectionPointIn")) {
                NodeList connectionPointInChildren = transitionChild.getChildNodes();
                for (int k = 0; k < connectionPointInChildren.getLength(); k++) {
                    if (connectionPointInChildren.item(k).getNodeName().equals("refLocalId")) {
                        source = Integer.parseInt(connectionPointInChildren.item(k).getAttributes().getNamedItem("refLocalId").getNodeValue());
                    }
                }
            } else if (transitionChild.getNodeName().equals("condition")) {
                NodeList conditionChildren = transitionChild.getChildNodes();
                for (int k = 0; k < conditionChildren.getLength(); k++) {
                    if (conditionChildren.item(k).getNodeName().equals("inline")) {
                        for (int l = 0; l < conditionChildren.item(k).getChildNodes().getLength(); l++) {
                            if (conditionChildren.item(k).getChildNodes().item(l).getNodeName().equals("ST")) {
                                condition = conditionChildren.item(k).getChildNodes().item(l).getTextContent().trim();
                                System.out.println("testtttt: " + condition.trim());
                            }
                        }
                    }
                }
            }
        }
        Expression expCondition = new Expression(condition);
        Transition transitionToAdd = new Transition(id, expCondition);
//        transitionToAdd.setPrecedingStep(source);
        components.add(transitionToAdd);
    }

    private void addStepToSFC(Node node) {
        int id = Integer.parseInt(node.getAttributes().getNamedItem("localId").getNodeValue());
        String name = node.getAttributes().getNamedItem("name").getNodeValue();
        boolean initialStep = Boolean.parseBoolean(node.getAttributes().getNamedItem("initialStep").getNodeValue());

        if (initialStep) {
            initialSteps.add(new Step(id, name));
        }

        components.add(new Step(id, name));
    }

    private void addVarToSFC(Node node)
    {
        vars.add(node.getAttributes().getNamedItem("name").getNodeValue());
    }

    private void addConvergenceToSFC(Node node)
    {
        int id = Integer.parseInt(node.getAttributes().getNamedItem("localId").getNodeValue());
        Collection<Integer> sources = new ArrayList<>();

        NodeList convergenceChildren = node.getChildNodes();
        for (int j = 0; j < convergenceChildren.getLength(); j++)
        {
            Node convergenceChild = convergenceChildren.item(j);
            if (convergenceChild.getNodeName().equals("connectionPointIn"))
            {
                NodeList connectionPointInChildren = convergenceChild.getChildNodes();
                for (int k = 0; k < connectionPointInChildren.getLength(); k++)
                {
                    if (connectionPointInChildren.item(k).getNodeName().equals("connection"))
                    {
                        sources.add(Integer.parseInt(connectionPointInChildren.item(k).getAttributes().getNamedItem("refLocalId").getNodeValue()));
                    }
                }
            }

        }
        Convergence convergenceToAdd = new Convergence(id);
        for (Integer source : sources) {
            convergenceToAdd.addPrecedingStep(new Step(source, "")); // TODO? I dont like it tbh
        }
        components.add(convergenceToAdd);
    }

    private void addDivergenceToSFC(Node node)
    {
        int id = Integer.parseInt(node.getAttributes().getNamedItem("localId").getNodeValue());
        int source = -1;

        NodeList convergenceChildren = node.getChildNodes();
        for (int j = 0; j < convergenceChildren.getLength(); j++)
        {
            Node convergenceChild = convergenceChildren.item(j);
            if (convergenceChild.getNodeName().equals("connectionPointIn"))
            {
                NodeList connectionPointInChildren = convergenceChild.getChildNodes();
                for (int k = 0; k < connectionPointInChildren.getLength(); k++)
                {
                    if (connectionPointInChildren.item(k).getNodeName().equals("connection"))
                    {
                        source = Integer.parseInt(connectionPointInChildren.item(k).getAttributes().getNamedItem("refLocalId").getNodeValue());
                    }
                }
            }

        }
        Divergence divergenceToAdd = new Divergence(id);
        if (source != -1) {
            divergenceToAdd.addPrecedingTransition(new Transition(source));
        }
        components.add(divergenceToAdd);
    }

    public void printSFC()
    {
        for (Component component : components)
        {
            System.out.println(component);
        }

        System.out.println("Variables:");
        for (String var : vars)
        {
            System.out.println(var);
        }
    }

    public Step getStartStep() throws Exception
    {
        if (!initialSteps.isEmpty())
        {
            return initialSteps.iterator().next();
        }
        else
        {
            throw new Exception("SFC.getFirstStep");
        }
    }
    /* Jakieś akcesory do treści tego SFC, żeby go przerobić na graf.
       W praniu wyjdzie. */
}
