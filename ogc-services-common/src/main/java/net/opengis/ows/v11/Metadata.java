package net.opengis.ows.v11;

import java.io.Serializable;
import net.opengis.OgcProperty;

/**
 * POJO class for XML type MetadataType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public interface Metadata extends OgcProperty<Serializable>
{
    
    
    /**
     * Gets the abstractMetaData property
     */
    public Serializable getAbstractMetaData();
    
    
    /**
     * Checks if abstractMetaData is set
     */
    public boolean isSetAbstractMetaData();
    
    
    /**
     * Sets the abstractMetaData property
     */
    public void setAbstractMetaData(Serializable abstractMetaData);
    
    
    /**
     * Gets the about property
     */
    public String getAbout();
    
    
    /**
     * Checks if about is set
     */
    public boolean isSetAbout();
    
    
    /**
     * Sets the about property
     */
    public void setAbout(String about);
}
