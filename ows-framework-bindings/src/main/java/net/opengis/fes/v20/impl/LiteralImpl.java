package net.opengis.fes.v20.impl;

import javax.xml.namespace.QName;
import net.opengis.fes.v20.Literal;


/**
 * POJO class for XML type LiteralType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class LiteralImpl implements Literal
{
    static final long serialVersionUID = 1L;
    protected QName type;
    protected String value;
    
    
    public LiteralImpl()
    {
    }
    
    
    /**
     * Gets the type property
     */
    @Override
    public QName getType()
    {
        return type;
    }
    
    
    /**
     * Checks if type is set
     */
    @Override
    public boolean isSetType()
    {
        return (type != null);
    }
    
    
    /**
     * Sets the type property
     */
    @Override
    public void setType(QName type)
    {
        this.type = type;
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
        if (!(obj instanceof Literal))
            return false;
        return getValue().equals(((Literal)obj).getValue());
    }
}
