package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.UpperBoundary;


/**
 * POJO class for XML type UpperBoundaryType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class UpperBoundaryImpl implements UpperBoundary
{
    static final long serialVersionUID = 1L;
    protected Expression expression;
    
    
    public UpperBoundaryImpl()
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
