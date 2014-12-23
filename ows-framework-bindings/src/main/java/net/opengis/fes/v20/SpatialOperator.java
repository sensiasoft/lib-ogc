package net.opengis.fes.v20;

import java.util.List;
import javax.xml.namespace.QName;


/**
 * POJO class for XML type SpatialOperatorType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface SpatialOperator
{
        
    /**
     * Gets the list of geometry operands
     */
    public List<QName> getGeometryOperands();
    
    
    /**
     * Gets the name property
     */
    public SpatialOperatorName getName();
    
    
    /**
     * Checks if name is set
     */
    public boolean isSetName();
    
    
    /**
     * Sets the name property
     */
    public void setName(SpatialOperatorName name);
}
