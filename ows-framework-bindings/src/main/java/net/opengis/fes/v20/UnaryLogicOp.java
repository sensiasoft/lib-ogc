package net.opengis.fes.v20;



/**
 * POJO class for XML type UnaryLogicOpType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface UnaryLogicOp extends LogicOps
{
    
    /**
     * Gets the operand1 property
     */
    public FilterPredicate getOperand();
    
    
    /**
     * Sets the operand1 property
     */
    public void setOperand(FilterPredicate operand);
    
}
