package net.opengis.ows.v11.impl;

import java.io.Serializable;
import net.opengis.OgcPropertyImpl;
import net.opengis.ows.v11.Metadata;


/**
 * POJO class for XML type MetadataType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public class MetadataImpl extends OgcPropertyImpl<Serializable> implements Metadata
{
    private static final long serialVersionUID = -7178639924829147249L;
    protected Serializable abstractMetaData;
    protected String about;
    
    public MetadataImpl()
    {
    }
    
    
    /**
     * Gets the abstractMetaData property
     */
    @Override
    public Serializable getAbstractMetaData()
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
    public void setAbstractMetaData(Serializable abstractMetaData)
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
