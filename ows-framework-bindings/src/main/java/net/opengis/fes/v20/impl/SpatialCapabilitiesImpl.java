package net.opengis.fes.v20.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.SpatialCapabilities;
import net.opengis.fes.v20.SpatialOperator;


/**
 * POJO class for XML type Spatial_CapabilitiesType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class SpatialCapabilitiesImpl implements SpatialCapabilities
{
    static final long serialVersionUID = 1L;
    protected List<QName> geometryOperands = new ArrayList<QName>();
    protected List<SpatialOperator> spatialOperators = new ArrayList<SpatialOperator>();
    
    
    public SpatialCapabilitiesImpl()
    {
    }
    
    
    @Override
    public List<QName> getGeometryOperands()
    {
        return geometryOperands;
    }
    
    
    @Override
    public List<SpatialOperator> getSpatialOperators()
    {
        return spatialOperators;
    }
}
