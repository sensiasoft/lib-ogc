package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.ComparisonOperator;
import net.opengis.fes.v20.ComparisonOperatorName;


/**
 * POJO class for XML type ComparisonOperatorType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class ComparisonOperatorImpl implements ComparisonOperator
{
    static final long serialVersionUID = 1L;
    protected ComparisonOperatorName name;
    
    
    public ComparisonOperatorImpl()
    {
    }
    
    
    /**
     * Gets the name property
     */
    @Override
    public ComparisonOperatorName getName()
    {
        return name;
    }
    
    
    /**
     * Sets the name property
     */
    @Override
    public void setName(ComparisonOperatorName name)
    {
        this.name = name;
    }
}
