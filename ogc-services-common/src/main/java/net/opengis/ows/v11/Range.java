package net.opengis.ows.v11;

import java.io.Serializable;

/**
 * POJO class for XML type RangeType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public interface Range extends Serializable
{
    
    
    /**
     * Gets the minimumValue property
     */
    public String getMinimumValue();
    
    
    /**
     * Checks if minimumValue is set
     */
    public boolean isSetMinimumValue();
    
    
    /**
     * Sets the minimumValue property
     */
    public void setMinimumValue(String minimumValue);
    
    
    /**
     * Gets the maximumValue property
     */
    public String getMaximumValue();
    
    
    /**
     * Checks if maximumValue is set
     */
    public boolean isSetMaximumValue();
    
    
    /**
     * Sets the maximumValue property
     */
    public void setMaximumValue(String maximumValue);
    
    
    /**
     * Gets the spacing property
     */
    public String getSpacing();
    
    
    /**
     * Checks if spacing is set
     */
    public boolean isSetSpacing();
    
    
    /**
     * Sets the spacing property
     */
    public void setSpacing(String spacing);
    
    
    /**
     * Gets the rangeClosure property
     */
    public String[] getRangeClosure();
    
    
    /**
     * Checks if rangeClosure is set
     */
    public boolean isSetRangeClosure();
    
    
    /**
     * Sets the rangeClosure property
     */
    public void setRangeClosure(String[] rangeClosure);
}
