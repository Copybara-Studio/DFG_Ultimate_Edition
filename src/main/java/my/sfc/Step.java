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
    private String label;
    
    /**
     * Collection of preceding components.
     * These can be:<br>
     * <ul>
     *   <li>a single transition, see Fig. 3.34.A., p. 87;</li>
     *   <li>multiple transitions, see Fig. 3.34.B., p. 87;</li>
     *   <li>a divergence, see Fig. 3.34.D., p. 87.</li>
     * </ul>
     */
    private final List<Component> precedingComponents;
    
    /**
     * Collection of succeeding components.
     * These can be:<br>
     * <ul>
     *   <li>a single transition, see Fig. 3.34.A., p. 87;</li>
     *   <li>multiple transitions, see Fig. 3.34.C., p. 87;</li>
     *   <li>a convergence, see Fig. 3.34.D., p. 87.</li>
     * </ul>
     */
    private final List<Component> succeedingComponents;
    
    /**
     * Constructor.
     * @param label
     */
    public Step(int _id, String label)
    {
        super(ComponentType.STEP);
        precedingComponents = new ArrayList<>();
        succeedingComponents = new ArrayList<>();
        id = _id;
        this.label = label;
    }
    
    /**
     * Adds the divergence as a preceding component of this step. A divergence
     * may be the <em>sole</em> preceding component of a step, so if this step
     * has already a preceding component, the method throws.
     * 
     * @param divergence
     * @throws Exception
     */
    public void addPrecedingDivergence(Divergence divergence) throws Exception
    {
        if (precedingComponents.isEmpty())
        {
            precedingComponents.add(divergence);
        }
        else
        {
            throw new Exception("Step.addPrecedingDivergence");
        }
    }
    
    /**
     * Adds the transition as a preceding component of this step. There may be
     * multiple transitions preceding a step, but no component of other type may
     * be mixed with them, thus the method throws if this step has already
     * a preceding component that is not a <code>Transition</code>.
     * 
     * @param transition 
     * @throws Exception 
     */
    public void addPrecedingTransition(Transition transition) throws Exception
    {
        boolean success = true;
        for (var component : precedingComponents)
        {
            if (component.getType() != ComponentType.TRANSITION)
            {
                success = false;
                break;
            }
        }
        if (success)
        {
            precedingComponents.add(transition);
            transition.setSucceedingStep(this);
        }
        else
        {
            throw new Exception("Step.addPrecedingTransition");
        }
    }
    
    /**
     * Adds the convergence as a succeeding component of this step.
     * A convergence may be the <em>sole</em> succeeding component of a step, so
     * if this step has already a succeeding component, the method throws.
     * 
     * @param convergence
     * @throws Exception
     */
    public void addSucceedingConvergence(Convergence convergence) throws Exception
    {
        if (precedingComponents.isEmpty())
        {
            precedingComponents.add(convergence);
            convergence.addPrecedingStep(this);
        }
        else
        {
            throw new Exception("Step.addSucceedingConvergence");
        }
    }
    
    /**
     * Adds the transition as a succeeding component of this step. There may be
     * multiple transitions succeeding a step, but no component of other type
     * may be mixed with them, thus the method throws if this step has already
     * a succeeding component that is not a <code>Transition</code>.
     * 
     * @param transition 
     * @throws Exception 
     */
    public void addSucceedingTransition(Transition transition) throws Exception
    {
        boolean success = true;
        for (var component : succeedingComponents)
        {
            if (component.getType() != ComponentType.TRANSITION)
            {
                success = false;
                break;
            }
        }
        if (success)
        {
            succeedingComponents.add(transition);
            transition.setPrecedingStep(this);
        }
        else
        {
            throw new Exception("Step.addSucceedingTransition");
        }
    }
    
    public String getLabel()
    {
        return label;
    }

    @Override
    public String toString() {
        return "Step{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", precedingComponents=" + precedingComponents +
                ", succeedingComponents=" + succeedingComponents +
                '}';
    }
}
