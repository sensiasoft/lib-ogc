package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.BBOX;
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.Expression;


public class BBOXImpl extends SpatialOpsImpl implements BBOX
{
    static final long serialVersionUID = 1L;
    protected Expression operand1;
    protected Expression operand2;
    
    
    public BBOXImpl()
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
    public boolean equals(Object obj)
    {
        if (!this.getClass().equals(obj.getClass()))
            return false;
        
        if (!getOperand1().equals(((BinarySpatialOp)obj).getOperand1()))
            return false;
        
        if (!getOperand2().equals(((BinarySpatialOp)obj).getOperand2()))
            return false;
        
        return true;
    }
}
