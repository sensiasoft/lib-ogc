package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.Expression;


public abstract class BinarySpatialOpImpl extends SpatialOpsImpl implements BinarySpatialOp
{
    static final long serialVersionUID = 1L;
    protected Expression operand1;
    protected Expression operand2;
    
    
    public BinarySpatialOpImpl()
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
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append(getClass().getSimpleName());
        buf.append(" (");
        buf.append(operand1.toString());
        buf.append(',');
        buf.append(operand2.toString());
        buf.append(')');
        return buf.toString();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!this.getClass().equals(obj.getClass()))
            return false;
        
        BinarySpatialOp other = (BinarySpatialOp)obj;
        
        if (!getOperand1().equals(other.getOperand1()))
            return false;
        
        if (!getOperand2().equals(other.getOperand2()))
            return false;
        
        return true;
    }
}
