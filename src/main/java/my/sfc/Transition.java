/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.sfc;

import my.expression.Expression;

/**
 *
 * @author Kay Jay O'Nail
 */
public class Transition extends Component
{
    private final Integer id;
    private Component precedingComponent;
    private Component succeedingComponent;
    private Expression condition;
    
    public Transition(Integer _id, Expression expression)
    {
        super(ComponentType.TRANSITION);
        condition = expression;
        id = _id;
    }

    public Transition(int source) {
        super(ComponentType.TRANSITION);
        id = source;
    }

    public void setPrecedingStep(Step step)
    {
        precedingComponent = step;
    }
    
    public void setPrecedingConvergence(Convergence convergence)
    {
        precedingComponent = convergence;
    }
    
    public void setSucceedingStep(Step step)
    {
        succeedingComponent = step;
    }
    
    public void setSucceedingDivergence(Divergence divergence)
    {
        succeedingComponent = divergence;
    }

    @Override
    public String toString() {
        return "Transition{" +
                "id=" + id +
                ", precedingComponent=" + precedingComponent +
                ", succeedingComponent=" + succeedingComponent +
                ", condition=" + condition +
                '}';
    }
}
