package net.opengis.ows.v11;

import java.io.Serializable;

/**
 * POJO class for XML type DomainMetadataType(@http://www.opengis.net/ows/1.1).
 *
 */
public interface DomainMetadata extends Serializable
{
    
    
    /**
     * Gets the reference property
     */
    public String getReference();
    
    
    /**
     * Checks if reference is set
     */
    public boolean isSetReference();
    
    
    /**
     * Sets the reference property
     */
    public void setReference(String reference);
    
    
    /**
     * Gets the inline value
     */
    public String getValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(String value);
}
