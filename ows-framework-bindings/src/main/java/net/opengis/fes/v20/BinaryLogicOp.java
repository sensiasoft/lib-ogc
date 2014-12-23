package net.opengis.fes.v20;



/**
 * POJO class for XML type BinaryLogicOpType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface BinaryLogicOp extends LogicOps
{    
    
    /**
     * Gets the operand1 property
     */
    public FilterPredicate getOperand1();
    
    
    /**
     * Sets the operand1 property
     */
    public void setOperand1(FilterPredicate operand1);
    
    
    /**
     * Gets the operand2 property
     */
    public FilterPredicate getOperand2();
    
    
    /**
     * Sets the operand2 property
     */
    public void setOperand2(FilterPredicate operand2);
    
}
