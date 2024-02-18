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
public class Convergence extends Component
{
    private Integer id;
    private List<Step> preceedingSteps;
    private Transition succeedingTransition;
    
    public Convergence(Integer _id)
    {
        super(ComponentType.CONVERGENCE);
        id = _id;
        preceedingSteps = new ArrayList<>();
    }
    
    public void addPrecedingStep(Step step)
    {
        preceedingSteps.add(step);
    }
    
    public void setSucceedingTransition(Transition transition)
    {
        succeedingTransition = transition;
        transition.setPrecedingConvergence(this);
    }

    @Override
    public String toString() {
        return "Convergence{" +
                "id=" + id +
                '}';
    }
}
