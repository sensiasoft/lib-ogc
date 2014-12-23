package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.PropertyIsNil;


public class PropertyIsNilImpl extends ComparisonOpsImpl implements PropertyIsNil
{
    static final long serialVersionUID = 1L;
    protected Expression expression;
    protected String nilReason;
    
    
    public PropertyIsNilImpl()
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
    
    
    @Override
    public String getNilReason()
    {
        return nilReason;
    }
    
    
    @Override
    public boolean isSetNilReason()
    {
        return (nilReason != null);
    }
    
    
    @Override
    public void setNilReason(String nilReason)
    {
        this.nilReason = nilReason;
    }
}
