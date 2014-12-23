package net.opengis.fes.v20;



/**
 * POJO class for XML type MatchActionType(@http://www.opengis.net/fes/2.0).
 *
 */
public enum MatchAction
{
    ALL("All"),
    ANY("Any"),
    ONE("One");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private MatchAction(String s)
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
    public static MatchAction fromString(String s)
    {
        if (s.equals("All"))
            return ALL;
        else if (s.equals("Any"))
            return ANY;
        else if (s.equals("One"))
            return ONE;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum MatchAction");
    }
}
