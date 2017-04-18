package net.opengis.ows.v11.impl;

import net.opengis.ows.v11.DomainMetadata;


/**
 * POJO class for XML type DomainMetadataType(@http://www.opengis.net/ows/1.1).
 *
 */
public class DomainMetadataImpl implements DomainMetadata
{
    private static final long serialVersionUID = 3240152314511963585L;
    protected String reference;
    protected String value;
    
    
    public DomainMetadataImpl()
    {
    }
    
    
    /**
     * Gets the reference property
     */
    @Override
    public String getReference()
    {
        return reference;
    }
    
    
    /**
     * Checks if reference is set
     */
    @Override
    public boolean isSetReference()
    {
        return (reference != null);
    }
    
    
    /**
     * Sets the reference property
     */
    @Override
    public void setReference(String reference)
    {
        this.reference = reference;
    }
    
    
    /**
     * Gets the inline value
     */
    @Override
    public String getValue()
    {
        return value;
    }
    
    
    /**
     * Sets the inline value
     */
    @Override
    public void setValue(String value)
    {
        this.value = value;
    }
}
