package net.opengis.fes.v20;

import java.util.List;
import net.opengis.ows.v11.Domain;


/**
 * POJO class for XML type ConformanceType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface Conformance
{
    
    
    /**
     * Gets the list of constraint properties
     * @return property list
     */
    public List<Domain> getConstraintList();
    
    
    /**
     * Returns number of constraint properties
     * @return number of properties
     */
    public int getNumConstraints();
    
    
    /**
     * Adds a new constraint property
     * @param constraint property value
     */
    public void addConstraint(Domain constraint);
}
