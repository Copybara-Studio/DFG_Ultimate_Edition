/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.sfc;

import java.util.*;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Step extends Component
{
    private Integer id;
    private String name;

    private final List<Transition> precedingTransitions;
    private final List<Transition> succeedingTransitions;
    
    /**
     * Constructor.
     * @param id
     * @param name - label of the step
     */
    public Step(int id, String name)
    {
        super(ComponentType.STEP);
        precedingTransitions = new ArrayList<>();
        succeedingTransitions = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public void addPrecedingTransition(Transition transition)
    {
        precedingTransitions.add(transition);
    }

    public void addSucceedingTransition(Transition transition) throws Exception
    {
        succeedingTransitions.add(transition);
    }
    
    public String getLabel()
    {
        return name;
    }
    
    public List<Transition> getPrecedingTransitions()
    {
        return Collections.unmodifiableList(precedingTransitions);
    }
    
    public List<Transition> getSucceedingTransitions()
    {
        return Collections.unmodifiableList(succeedingTransitions);
    }
    
    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", label='" + name + '\'' +
                '}';
    }
}
