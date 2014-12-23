package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.ValueReference;


/**
 * POJO class for XML type ValueReferenceType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class ValueReferenceImpl implements ValueReference
{
    static final long serialVersionUID = 1L;
    protected String value;
    
    
    public ValueReferenceImpl()
    {
    }
    
    
    @Override
    public String getValue()
    {
        return value;
    }


    @Override
    public boolean isSetValue()
    {
        return (value != null);
    }


    @Override
    public void setValue(String value)
    {
        this.value = value;        
    }
    
    
    @Override
    public String toString()
    {
        return getValue();
    }


    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ValueReference))
            return false;
        return getValue().equals(((ValueReference)obj).getValue());
    }
}
