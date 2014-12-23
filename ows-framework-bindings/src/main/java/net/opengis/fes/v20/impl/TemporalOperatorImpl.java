package net.opengis.fes.v20.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.TemporalOperator;
import net.opengis.fes.v20.TemporalOperatorName;


public class TemporalOperatorImpl implements TemporalOperator
{
    static final long serialVersionUID = 1L;
    protected List<QName> temporalOperands = new ArrayList<QName>();
    protected TemporalOperatorName name;
    
    
    public TemporalOperatorImpl()
    {
    }
    
    
    @Override
    public List<QName> getTemporalOperands()
    {
        return temporalOperands;
    }
    
    
    @Override
    public TemporalOperatorName getName()
    {
        return name;
    }
    
    
    @Override
    public void setName(TemporalOperatorName name)
    {
        this.name = name;
    }
}
