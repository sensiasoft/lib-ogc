package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.LowerBoundary;


/**
 * POJO class for XML type LowerBoundaryType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class LowerBoundaryImpl implements LowerBoundary
{
    static final long serialVersionUID = 1L;
    protected Expression expression;
    
    
    public LowerBoundaryImpl()
    {
    }
    
    
    /**
     * Gets the expression property
     */
    @Override
    public Expression getExpression()
    {
        return expression;
    }
    
    
    /**
     * Sets the expression property
     */
    @Override
    public void setExpression(Expression expression)
    {
        this.expression = expression;
    }
}
