package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.DistanceBuffer;
import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.Measure;


public class DistanceBufferImpl extends SpatialOpsImpl implements DistanceBuffer
{
    static final long serialVersionUID = 1L;
    protected Expression operand1;
    protected Expression operand2;
    protected Measure distance;
    
    
    public DistanceBufferImpl()
    {
    }
    
    
    @Override
    public Expression getOperand1()
    {
        return operand1;
    }
    
    
    @Override
    public void setOperand1(Expression operand1)
    {
        this.operand1 = operand1;
    }
    
    
    @Override
    public Expression getOperand2()
    {
        return operand2;
    }
    
    
    @Override
    public void setOperand2(Expression operand2)
    {
        this.operand2 = operand2;
    }
    
    
    @Override
    public Measure getDistance()
    {
        return distance;
    }
    
    
    @Override
    public void setDistance(Measure distance)
    {
        this.distance = distance;
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        if (!this.getClass().equals(obj.getClass()))
            return false;
        
        DistanceBuffer other = (DistanceBuffer)obj;
        
        if (!getOperand1().equals(other.getOperand1()))
            return false;
        
        if (!getOperand2().equals(other.getOperand2()))
            return false;
        
        if (getDistance().getValue() != other.getDistance().getValue())
            return false;
        
        return true;
    }
}
