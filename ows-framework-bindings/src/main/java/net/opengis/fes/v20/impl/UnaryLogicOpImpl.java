package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.FilterPredicate;
import net.opengis.fes.v20.UnaryLogicOp;


public abstract class UnaryLogicOpImpl extends LogicOpsImpl implements UnaryLogicOp
{
    static final long serialVersionUID = 1L;
    protected FilterPredicate operand;
    
    
    @Override
    public FilterPredicate getOperand()
    {
        return operand;
    }
    
    
    @Override
    public void setOperand(FilterPredicate operand)
    {
        this.operand = operand;
    }
}
