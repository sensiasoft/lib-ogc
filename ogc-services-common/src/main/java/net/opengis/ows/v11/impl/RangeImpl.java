package net.opengis.ows.v11.impl;

import net.opengis.ows.v11.Range;


/**
 * POJO class for XML type RangeType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public class RangeImpl implements Range
{
    private static final long serialVersionUID = -4762293372970086016L;
    protected String minimumValue;
    protected String maximumValue;
    protected String spacing;
    protected String[] rangeClosure;
    
    
    public RangeImpl()
    {
    }
    
    
    /**
     * Gets the minimumValue property
     */
    @Override
    public String getMinimumValue()
    {
        return minimumValue;
    }
    
    
    /**
     * Checks if minimumValue is set
     */
    @Override
    public boolean isSetMinimumValue()
    {
        return (minimumValue != null);
    }
    
    
    /**
     * Sets the minimumValue property
     */
    @Override
    public void setMinimumValue(String minimumValue)
    {
        this.minimumValue = minimumValue;
    }
    
    
    /**
     * Gets the maximumValue property
     */
    @Override
    public String getMaximumValue()
    {
        return maximumValue;
    }
    
    
    /**
     * Checks if maximumValue is set
     */
    @Override
    public boolean isSetMaximumValue()
    {
        return (maximumValue != null);
    }
    
    
    /**
     * Sets the maximumValue property
     */
    @Override
    public void setMaximumValue(String maximumValue)
    {
        this.maximumValue = maximumValue;
    }
    
    
    /**
     * Gets the spacing property
     */
    @Override
    public String getSpacing()
    {
        return spacing;
    }
    
    
    /**
     * Checks if spacing is set
     */
    @Override
    public boolean isSetSpacing()
    {
        return (spacing != null);
    }
    
    
    /**
     * Sets the spacing property
     */
    @Override
    public void setSpacing(String spacing)
    {
        this.spacing = spacing;
    }
    
    
    /**
     * Gets the rangeClosure property
     */
    @Override
    public String[] getRangeClosure()
    {
        return rangeClosure;
    }
    
    
    /**
     * Checks if rangeClosure is set
     */
    @Override
    public boolean isSetRangeClosure()
    {
        return (rangeClosure != null);
    }
    
    
    /**
     * Sets the rangeClosure property
     */
    @Override
    public void setRangeClosure(String[] rangeClosure)
    {
        this.rangeClosure = rangeClosure;
    }
}
