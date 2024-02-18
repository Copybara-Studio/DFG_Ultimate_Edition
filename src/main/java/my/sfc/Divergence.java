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
public class Divergence extends Component
{
    private Integer id;
    private Transition precedingTransition;
    private List<Step> succeedingSteps;
    
    public Divergence(Integer _id)
    {
        super(ComponentType.DIVERGENCE);
        id = _id;
        succeedingSteps = new ArrayList<>();
    }
    
    public void addPrecedingTransition(Transition transition)
    {
        precedingTransition = transition;
        transition.setSucceedingDivergence(this);
    }
    
    public void setSucceedingTransition(Step step)
    {
        succeedingSteps.add(step);
    }

    @Override
    public String toString() {
        return "Divergence{" +
                "id=" + id +
                '}';
    }
}
