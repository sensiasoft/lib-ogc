package net.opengis.fes.v20;

import java.util.List;



/**
 * POJO class for XML type Scalar_CapabilitiesType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface ScalarCapabilities
{
        
    /**
     * Checks of logical operators are supported
     */
    public boolean getLogicalOperators();
    
    
    /**
     * Sets if logical operators are supported
     */
    public void setLogicalOperators(boolean b);
    
    
    /**
     * Gets the list of comparison operators
     */
    public List<ComparisonOperator> getComparisonOperators();
}
