package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Measure;


public class MeasureImpl implements Measure
{
    static final long serialVersionUID = 1L;
    protected String uom;
    protected double value;
    
    
    public MeasureImpl()
    {
    }
    
    
    @Override
    public String getUom()
    {
        return uom;
    }
    
    
    @Override
    public void setUom(String uom)
    {
        this.uom = uom;
    }
    
    
    @Override
    public double getValue()
    {
        return value;
    }
    
    
    @Override
    public void setValue(double value)
    {
        this.value = value;
    }
}
