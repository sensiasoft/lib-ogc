package net.opengis.fes.v20;



/**
 * POJO class for XML type ComparisonOperatorNameType(@http://www.opengis.net/fes/2.0).
 *
 */
public enum ComparisonOperatorName
{
    PROPERTY_IS_EQUAL_TO("PropertyIsEqualTo"),
    PROPERTY_IS_NOT_EQUAL_TO("PropertyIsNotEqualTo"),
    PROPERTY_IS_LESS_THAN("PropertyIsLessThan"),
    PROPERTY_IS_GREATER_THAN("PropertyIsGreaterThan"),
    PROPERTY_IS_LESS_THAN_OR_EQUAL_TO("PropertyIsLessThanOrEqualTo"),
    PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO("PropertyIsGreaterThanOrEqualTo"),
    PROPERTY_IS_LIKE("PropertyIsLike"),
    PROPERTY_IS_NULL("PropertyIsNull"),
    PROPERTY_IS_NIL("PropertyIsNil"),
    PROPERTY_IS_BETWEEN("PropertyIsBetween");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private ComparisonOperatorName(String s)
    {
        this.text = s;
    }
    
    
    
    /**
     * To convert an enum constant to its String representation
     */
    public String toString()
    {
        return text;
    }
    
    
    
    /**
     * To get the enum constant corresponding to the given String representation
     */
    public static ComparisonOperatorName fromString(String s)
    {
        if (s.equals("PropertyIsEqualTo"))
            return PROPERTY_IS_EQUAL_TO;
        else if (s.equals("PropertyIsNotEqualTo"))
            return PROPERTY_IS_NOT_EQUAL_TO;
        else if (s.equals("PropertyIsLessThan"))
            return PROPERTY_IS_LESS_THAN;
        else if (s.equals("PropertyIsGreaterThan"))
            return PROPERTY_IS_GREATER_THAN;
        else if (s.equals("PropertyIsLessThanOrEqualTo"))
            return PROPERTY_IS_LESS_THAN_OR_EQUAL_TO;
        else if (s.equals("PropertyIsGreaterThanOrEqualTo"))
            return PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO;
        else if (s.equals("PropertyIsLike"))
            return PROPERTY_IS_LIKE;
        else if (s.equals("PropertyIsNull"))
            return PROPERTY_IS_NULL;
        else if (s.equals("PropertyIsNil"))
            return PROPERTY_IS_NIL;
        else if (s.equals("PropertyIsBetween"))
            return PROPERTY_IS_BETWEEN;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum ComparisonOperatorName");
    }
}
