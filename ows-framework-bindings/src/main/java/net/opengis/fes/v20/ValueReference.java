package net.opengis.fes.v20;


/**
 * POJO class for XML type ValueReferenceType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface ValueReference extends Expression
{
        
    /**
     * Gets the inline value
     */
    public String getValue();
    
    
    /**
     * Checks if inline value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(String val);
}
