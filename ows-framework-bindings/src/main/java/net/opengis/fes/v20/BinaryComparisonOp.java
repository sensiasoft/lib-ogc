package net.opengis.fes.v20;


/**
 * POJO class for XML type BinaryComparisonOpType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface BinaryComparisonOp extends ComparisonOps
{
    
    
    /**
     * Gets 1st operand property
     */
    public Expression getOperand1();
    
    
    /**
     * Sets 1st operand property
     */
    public void setOperand1(Expression exp);
    
    
    /**
     * Gets 2nd operand property
     */
    public Expression getOperand2();
    
    
    /**
     * Sets 2nd operand property
     */
    public void setOperand2(Expression exp);
    
    
    /**
     * Gets the matchCase property
     */
    public boolean getMatchCase();
    
    
    /**
     * Checks if matchCase is set
     */
    public boolean isSetMatchCase();
    
    
    /**
     * Sets the matchCase property
     */
    public void setMatchCase(boolean matchCase);
    
    
    /**
     * Unsets the matchCase property
     */
    public void unSetMatchCase();
    
    
    /**
     * Gets the matchAction property
     */
    public MatchAction getMatchAction();
    
    
    /**
     * Checks if matchAction is set
     */
    public boolean isSetMatchAction();
    
    
    /**
     * Sets the matchAction property
     */
    public void setMatchAction(MatchAction matchAction);
}
