package net.opengis.fes.v20;

import java.util.List;
import javax.xml.namespace.QName;


/**
 * POJO class for XML type Temporal_CapabilitiesType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface TemporalCapabilities
{    
    
    /**
     * Gets the list of temporal operands
     */
    public List<QName> getTemporalOperands();
    
    
    /**
     * Gets the list of temporal operators
     */
    public List<TemporalOperator> getTemporalOperators();

}
