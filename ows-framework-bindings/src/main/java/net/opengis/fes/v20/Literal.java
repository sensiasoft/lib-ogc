package net.opengis.fes.v20;

import javax.xml.namespace.QName;


/**
 * POJO class for XML type LiteralType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface Literal extends Expression
{
    
    
    /**
     * Gets the type property
     */
    public QName getType();
    
    
    /**
     * Checks if type is set
     */
    public boolean isSetType();
    
    
    /**
     * Sets the type property
     */
    public void setType(QName type);
    
    
    /**
     * Gets the inline value
     */
    public String getValue();
    
    
    /**
     * Checks if inline value is set
     */
    public boolean isSetValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(String val);
}
