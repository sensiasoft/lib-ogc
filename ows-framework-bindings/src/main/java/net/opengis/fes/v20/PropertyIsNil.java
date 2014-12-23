package net.opengis.fes.v20;



/**
 * POJO class for XML type PropertyIsNilType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface PropertyIsNil extends ComparisonOps
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
     * Gets the nilReason property
     */
    public String getNilReason();
    
    
    /**
     * Checks if nilReason is set
     */
    public boolean isSetNilReason();
    
    
    /**
     * Sets the nilReason property
     */
    public void setNilReason(String nilReason);
}
