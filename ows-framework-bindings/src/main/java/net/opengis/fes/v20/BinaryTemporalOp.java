package net.opengis.fes.v20;



/**
 * POJO class for XML type BinaryTemporalOpType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface BinaryTemporalOp extends TemporalOps
{    
    
    /**
     * Gets the operand1 property
     */
    public Expression getOperand1();
    
    
    /**
     * Sets the operand1 property
     */
    public void setOperand1(Expression operand1);
    
    
    /**
     * Gets the operand2 property
     */
    public Expression getOperand2();
    
    
    /**
     * Sets the operand2 property
     */
    public void setOperand2(Expression operand2);
}
