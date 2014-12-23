package net.opengis.fes.v20;



/**
 * POJO class for XML type MeasureType(@http://www.opengis.net/fes/2.0).
 *
 */
public interface Measure
{
        
    /**
     * Gets the uom property
     */
    public String getUom();
    
    
    /**
     * Sets the uom property
     */
    public void setUom(String uom);
    
    
    /**
     * Gets the inline value
     */
    public double getValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(double value);
}
