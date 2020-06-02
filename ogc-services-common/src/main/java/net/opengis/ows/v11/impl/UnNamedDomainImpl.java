package net.opengis.ows.v11.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.opengis.ows.v11.DomainMetadata;
import net.opengis.ows.v11.Metadata;
import net.opengis.ows.v11.Range;
import net.opengis.ows.v11.UnNamedDomain;


/**
 * POJO class for XML type UnNamedDomainType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public class UnNamedDomainImpl implements UnNamedDomain
{
    private static final long serialVersionUID = 6484393159497882372L;
    protected ArrayList<Object> allowedValues = new ArrayList<Object>();
    protected boolean anyValue;
    protected boolean noValues = true;
    protected Serializable valuesReference;
    protected String defaultValue;
    protected DomainMetadata meaning;
    protected DomainMetadata dataType;
    protected DomainMetadata uom;
    protected DomainMetadata referenceSystem;
    protected ArrayList<Metadata> metadataList = new ArrayList<Metadata>();
    
    
    public UnNamedDomainImpl()
    {
    }
    
    
    /**
     * Gets the allowedValues property
     */
    @Override
    public List<Object> getAllowedValues()
    {
        return allowedValues;
    }
    
    
    /**
     * Sets the allowedValues property
     */
    @Override
    public void addAllowedValue(String allowedValue)
    {
        allowedValues.add(allowedValue);
    }
    
    
    /**
     * Sets the allowedValues property
     */
    @Override
    public void addAllowedValue(Range allowedValue)
    {
        allowedValues.add(allowedValue);
    }

    
    
    /**
     * Checks if anyValue is set
     */
    @Override
    public boolean isSetAnyValue()
    {
        return anyValue;
    }
    
    
    /**
     * Sets the anyValue property
     */
    @Override
    public void setAnyValue(boolean anyValue)
    {
        this.anyValue = anyValue;
    }
    
    
    /**
     * Checks if noValues is set
     */
    @Override
    public boolean isSetNoValues()
    {
        return noValues;
    }
    
    
    /**
     * Sets the noValues property
     */
    @Override
    public void setNoValues(boolean noValues)
    {
        this.noValues = noValues;
    }
    
    
    /**
     * Gets the valuesReference property
     */
    @Override
    public Object getValuesReference()
    {
        return valuesReference;
    }
    
    
    /**
     * Checks if valuesReference is set
     */
    @Override
    public boolean isSetValuesReference()
    {
        return (valuesReference != null);
    }
    
    
    /**
     * Sets the valuesReference property
     */
    @Override
    public void setValuesReference(Serializable valuesReference)
    {
        this.valuesReference = valuesReference;
    }
    
    
    /**
     * Gets the defaultValue property
     */
    @Override
    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    
    /**
     * Checks if defaultValue is set
     */
    @Override
    public boolean isSetDefaultValue()
    {
        return (defaultValue != null);
    }
    
    
    /**
     * Sets the defaultValue property
     */
    @Override
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
    
    
    /**
     * Gets the meaning property
     */
    @Override
    public DomainMetadata getMeaning()
    {
        return meaning;
    }
    
    
    /**
     * Checks if meaning is set
     */
    @Override
    public boolean isSetMeaning()
    {
        return (meaning != null);
    }
    
    
    /**
     * Sets the meaning property
     */
    @Override
    public void setMeaning(DomainMetadata meaning)
    {
        this.meaning = meaning;
    }
    
    
    /**
     * Gets the dataType property
     */
    @Override
    public DomainMetadata getDataType()
    {
        return dataType;
    }
    
    
    /**
     * Checks if dataType is set
     */
    @Override
    public boolean isSetDataType()
    {
        return (dataType != null);
    }
    
    
    /**
     * Sets the dataType property
     */
    @Override
    public void setDataType(DomainMetadata dataType)
    {
        this.dataType = dataType;
    }
    
    
    /**
     * Gets the uom property
     */
    @Override
    public DomainMetadata getUOM()
    {
        return uom;
    }
    
    
    /**
     * Checks if uom is set
     */
    @Override
    public boolean isSetUOM()
    {
        return (uom != null);
    }
    
    
    /**
     * Sets the uom property
     */
    @Override
    public void setUOM(DomainMetadata uom)
    {
        this.uom = uom;
    }
    
    
    /**
     * Gets the referenceSystem property
     */
    @Override
    public DomainMetadata getReferenceSystem()
    {
        return referenceSystem;
    }
    
    
    /**
     * Checks if referenceSystem is set
     */
    @Override
    public boolean isSetReferenceSystem()
    {
        return (referenceSystem != null);
    }
    
    
    /**
     * Sets the referenceSystem property
     */
    @Override
    public void setReferenceSystem(DomainMetadata referenceSystem)
    {
        this.referenceSystem = referenceSystem;
    }
    
    
    /**
     * Gets the list of metadata properties
     */
    @Override
    public List<Metadata> getMetadataList()
    {
        return metadataList;
    }
    
    
    /**
     * Returns number of metadata properties
     */
    @Override
    public int getNumMetadatas()
    {
        if (metadataList == null)
            return 0;
        return metadataList.size();
    }
    
    
    /**
     * Adds a new metadata property
     */
    @Override
    public void addMetadata(Metadata metadata)
    {
        this.metadataList.add(metadata);
    }
}
