package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.PropertyIsNull;


public class PropertyIsNullImpl extends ComparisonOpsImpl implements PropertyIsNull
{
    static final long serialVersionUID = 1L;
    protected Expression expression;
    
    
    public PropertyIsNullImpl()
    {
    }
    
    
    @Override
    public Expression getOperand()
    {
        return expression;
    }
    
    
    @Override
    public void setOperand(Expression expression)
    {
        this.expression = expression;
    }
}
