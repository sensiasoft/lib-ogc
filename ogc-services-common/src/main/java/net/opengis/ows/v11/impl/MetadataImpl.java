package net.opengis.ows.v11.impl;

import net.opengis.ows.v11.Metadata;


/**
 * POJO class for XML type MetadataType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public class MetadataImpl extends net.opengis.OgcPropertyImpl<Object> implements Metadata
{
    static final long serialVersionUID = 1L;
    protected Object abstractMetaData;
    protected String about;
    
    
    public MetadataImpl()
    {
    }
    
    
    /**
     * Gets the abstractMetaData property
     */
    @Override
    public Object getAbstractMetaData()
    {
        return abstractMetaData;
    }
    
    
    /**
     * Checks if abstractMetaData is set
     */
    @Override
    public boolean isSetAbstractMetaData()
    {
        return (abstractMetaData != null);
    }
    
    
    /**
     * Sets the abstractMetaData property
     */
    @Override
    public void setAbstractMetaData(Object abstractMetaData)
    {
        this.abstractMetaData = abstractMetaData;
    }
    
    
    /**
     * Gets the about property
     */
    @Override
    public String getAbout()
    {
        return about;
    }
    
    
    /**
     * Checks if about is set
     */
    @Override
    public boolean isSetAbout()
    {
        return (about != null);
    }
    
    
    /**
     * Sets the about property
     */
    @Override
    public void setAbout(String about)
    {
        this.about = about;
    }
}
