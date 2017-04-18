package net.opengis.ows.v11;

import java.io.Serializable;
import java.util.List;


/**
 * POJO class for XML type UnNamedDomainType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public interface UnNamedDomain extends Serializable
{
    
    
    /**
     * Gets the allowedValues property
     */
    public List<Object> getAllowedValues();
    
    
    /**
     * Adds an allowedValue property
     */
    public void addAllowedValue(String allowedValue);
    
    
    /**
     * Adds an allowedValue property
     */
    public void addAllowedValue(Range allowedValue);
    
    
    /**
     * Checks if anyValue is set
     */
    public boolean isSetAnyValue();
    
    
    /**
     * Sets the anyValue property
     */
    public void setAnyValue(boolean anyValue);
    
    
    /**
     * Gets the noValues property
     */
    public boolean isSetNoValues();
    
    
    /**
     * Sets the noValues property
     */
    public void setNoValues(boolean noValues);
    
    
    /**
     * Gets the valuesReference property
     */
    public Object getValuesReference();
    
    
    /**
     * Checks if valuesReference is set
     */
    public boolean isSetValuesReference();
    
    
    /**
     * Sets the valuesReference property
     */
    public void setValuesReference(Serializable valuesReference);
    
    
    /**
     * Gets the defaultValue property
     */
    public String getDefaultValue();
    
    
    /**
     * Checks if defaultValue is set
     */
    public boolean isSetDefaultValue();
    
    
    /**
     * Sets the defaultValue property
     */
    public void setDefaultValue(String defaultValue);
    
    
    /**
     * Gets the meaning property
     */
    public DomainMetadata getMeaning();
    
    
    /**
     * Checks if meaning is set
     */
    public boolean isSetMeaning();
    
    
    /**
     * Sets the meaning property
     */
    public void setMeaning(DomainMetadata meaning);
    
    
    /**
     * Gets the dataType property
     */
    public DomainMetadata getDataType();
    
    
    /**
     * Checks if dataType is set
     */
    public boolean isSetDataType();
    
    
    /**
     * Sets the dataType property
     */
    public void setDataType(DomainMetadata dataType);
    
    
    /**
     * Gets the uom property
     */
    public DomainMetadata getUOM();
    
    
    /**
     * Checks if uom is set
     */
    public boolean isSetUOM();
    
    
    /**
     * Sets the uom property
     */
    public void setUOM(DomainMetadata uom);
    
    
    /**
     * Gets the referenceSystem property
     */
    public DomainMetadata getReferenceSystem();
    
    
    /**
     * Checks if referenceSystem is set
     */
    public boolean isSetReferenceSystem();
    
    
    /**
     * Sets the referenceSystem property
     */
    public void setReferenceSystem(DomainMetadata referenceSystem);
    
    
    /**
     * Gets the list of metadata properties
     */
    public List<Metadata> getMetadataList();
    
    
    /**
     * Returns number of metadata properties
     */
    public int getNumMetadatas();
    
    
    /**
     * Adds a new metadata property
     */
    public void addMetadata(Metadata metadata);
}
