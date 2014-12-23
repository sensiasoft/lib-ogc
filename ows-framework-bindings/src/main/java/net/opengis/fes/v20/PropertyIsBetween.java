package net.opengis.fes.v20;



/**
 * POJO class for XML type PropertyIsBetweenType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface PropertyIsBetween extends ComparisonOps
{    
    
    /**
     * Gets the operand property
     */
    public Expression getOperand();
    
    
    /**
     * Sets the operand property
     */
    public void setOperand(Expression expression);
    
    
    /**
     * Gets the lowerBoundary property
     */
    public LowerBoundary getLowerBoundary();
    
    
    /**
     * Sets the lowerBoundary property
     */
    public void setLowerBoundary(LowerBoundary lowerBoundary);
    
    
    /**
     * Gets the upperBoundary property
     */
    public UpperBoundary getUpperBoundary();
    
    
    /**
     * Sets the upperBoundary property
     */
    public void setUpperBoundary(UpperBoundary upperBoundary);
}
