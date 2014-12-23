package net.opengis.fes.v20.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.SpatialOperator;
import net.opengis.fes.v20.SpatialOperatorName;


public class SpatialOperatorImpl implements SpatialOperator
{
    static final long serialVersionUID = 1L;
    protected List<QName> geometryOperands = new ArrayList<QName>();
    protected SpatialOperatorName name;
    
    
    public SpatialOperatorImpl()
    {
    }
    
    
    @Override
    public List<QName> getGeometryOperands()
    {
        return geometryOperands;
    }
    
    
    @Override
    public SpatialOperatorName getName()
    {
        return name;
    }
    
    
    @Override
    public boolean isSetName()
    {
        return (name != null);
    }
    
    
    @Override
    public void setName(SpatialOperatorName name)
    {
        this.name = name;
    }
}
