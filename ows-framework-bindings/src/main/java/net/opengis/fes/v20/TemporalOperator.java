package net.opengis.fes.v20;

import java.util.List;
import javax.xml.namespace.QName;


/**
 * POJO class for XML type TemporalOperatorType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface TemporalOperator
{
    
    /**
     * Gets the list of temporal operands
     */
    public List<QName> getTemporalOperands();
    
    
    /**
     * Gets the name property
     */
    public TemporalOperatorName getName();
    
    
    /**
     * Sets the name property
     */
    public void setName(TemporalOperatorName name);
}
