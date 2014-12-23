package net.opengis.fes.v20.impl;

import java.util.ArrayList;
import java.util.List;
import net.opengis.fes.v20.ComparisonOperator;
import net.opengis.fes.v20.ScalarCapabilities;


public class ScalarCapabilitiesImpl implements ScalarCapabilities
{
    static final long serialVersionUID = 1L;
    protected boolean logicalOperators;
    protected List<ComparisonOperator> comparisonOperators = new ArrayList<ComparisonOperator>();
    
    
    public ScalarCapabilitiesImpl()
    {
    }
    
    
    @Override
    public boolean getLogicalOperators()
    {
        return logicalOperators;
    }
    
    
    @Override
    public void setLogicalOperators(boolean b)
    {
        this.logicalOperators = b;        
    }
    
    
    @Override
    public List<ComparisonOperator> getComparisonOperators()
    {
        return comparisonOperators;
    }
}
