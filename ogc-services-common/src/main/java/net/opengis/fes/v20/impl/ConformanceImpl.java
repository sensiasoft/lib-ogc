package net.opengis.fes.v20.impl;

import java.util.List;
import net.opengis.OgcPropertyList;
import net.opengis.fes.v20.Conformance;
import net.opengis.ows.v11.Domain;


/**
 * POJO class for XML type ConformanceType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class ConformanceImpl implements Conformance
{
    protected OgcPropertyList<Domain> constraintList = new OgcPropertyList<Domain>();
    
    
    public ConformanceImpl()
    {
    }
    
    
    @Override
    public List<Domain> getConstraintList()
    {
        return constraintList;
    }
    
    
    @Override
    public int getNumConstraints()
    {
        if (constraintList == null)
            return 0;
        return constraintList.size();
    }
    
    
    @Override
    public void addConstraint(Domain constraint)
    {
        this.constraintList.add(constraint);
    }
}
