package net.opengis.fes.v20;



/**
 * POJO class for XML type PropertyIsNullType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface PropertyIsNull extends ComparisonOps
{
    
    
    /**
     * Gets the operand property
     */
    public Expression getOperand();
    
    
    /**
     * Sets the operand property
     */
    public void setOperand(Expression expression);
}
