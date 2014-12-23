package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.LowerBoundary;
import net.opengis.fes.v20.PropertyIsBetween;
import net.opengis.fes.v20.UpperBoundary;


public class PropertyIsBetweenImpl extends ComparisonOpsImpl implements PropertyIsBetween
{
    static final long serialVersionUID = 1L;
    protected Expression expression;
    protected LowerBoundary lowerBoundary;
    protected UpperBoundary upperBoundary;
    
    
    public PropertyIsBetweenImpl()
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
    public LowerBoundary getLowerBoundary()
    {
        return lowerBoundary;
    }
    
    
    @Override
    public void setLowerBoundary(LowerBoundary lowerBoundary)
    {
        this.lowerBoundary = lowerBoundary;
    }
    
    
    @Override
    public UpperBoundary getUpperBoundary()
    {
        return upperBoundary;
    }
    
    
    @Override
    public void setUpperBoundary(UpperBoundary upperBoundary)
    {
        this.upperBoundary = upperBoundary;
    }
}
