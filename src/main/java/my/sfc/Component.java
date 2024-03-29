/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package my.sfc;

/**
 *
 * @author Kay Jay O'Nail
 */
public abstract class Component
{
    private final ComponentType type;
    
    public Component(ComponentType type)
    {
        this.type = type;
    }
    
    public ComponentType getType()
    {
        return type;
    }
}
