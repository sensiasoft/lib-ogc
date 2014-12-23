package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.BinaryLogicOp;
import net.opengis.fes.v20.FilterPredicate;


public abstract class BinaryLogicOpImpl extends LogicOpsImpl implements BinaryLogicOp
{
    static final long serialVersionUID = 1L;
    protected FilterPredicate operand1;
    protected FilterPredicate operand2;
    
    
    public BinaryLogicOpImpl()
    {
    }
    
    
    @Override
    public FilterPredicate getOperand1()
    {
        return operand1;
    }
    
    
    @Override
    public void setOperand1(FilterPredicate operand1)
    {
        this.operand1 = operand1;
    }
    
    
    @Override
    public FilterPredicate getOperand2()
    {
        return operand2;
    }
    
    
    @Override
    public void setOperand2(FilterPredicate operand2)
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
        
        BinaryLogicOp other = (BinaryLogicOp)obj;
        
        if (!getOperand1().equals(other.getOperand1()))
            return false;
        
        if (!getOperand2().equals(other.getOperand2()))
            return false;
        
        return true;
    }
}
