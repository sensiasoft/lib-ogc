package net.opengis.fes.v20;

import java.util.List;
import javax.xml.namespace.QName;



/**
 * POJO class for XML type Spatial_CapabilitiesType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface SpatialCapabilities
{
        
    /**
     * Gets the list of geometry operands
     */
    public List<QName> getGeometryOperands();
     
    
    /**
     * Gets the list of spatial operators
     */
    public List<SpatialOperator> getSpatialOperators();
}
