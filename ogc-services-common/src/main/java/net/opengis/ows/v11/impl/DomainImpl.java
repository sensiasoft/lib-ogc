package net.opengis.ows.v11.impl;

import net.opengis.ows.v11.Domain;


/**
 * POJO class for XML type DomainType(@http://www.opengis.net/ows/1.1).
 *
 * This is a complex type.
 */
public class DomainImpl extends UnNamedDomainImpl implements Domain
{
    private static final long serialVersionUID = -6578864006061374321L;
    protected String name = "";
    
    
    public DomainImpl()
    {
    }
    
    
    /**
     * Gets the name property
     */
    @Override
    public String getName()
    {
        return name;
    }
    
    
    /**
     * Sets the name property
     */
    @Override
    public void setName(String name)
    {
        this.name = name;
    }
}
