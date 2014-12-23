package net.opengis.fes.v20.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.TemporalCapabilities;
import net.opengis.fes.v20.TemporalOperator;


public class TemporalCapabilitiesImpl implements TemporalCapabilities
{
    static final long serialVersionUID = 1L;
    protected List<QName> temporalOperands = new ArrayList<QName>();
    protected List<TemporalOperator> temporalOperators = new ArrayList<TemporalOperator>();
    
    
    public TemporalCapabilitiesImpl()
    {
    }
    
    
    @Override
    public List<QName> getTemporalOperands()
    {
        return temporalOperands;
    }
    
    
    @Override
    public List<TemporalOperator> getTemporalOperators()
    {
        return temporalOperators;
    }
}
