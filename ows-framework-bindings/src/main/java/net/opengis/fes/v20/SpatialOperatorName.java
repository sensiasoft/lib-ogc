package net.opengis.fes.v20;



/**
 * POJO class for XML type SpatialOperatorNameType(@http://www.opengis.net/fes/2.0).
 *
 */
public enum SpatialOperatorName
{
    BBOX("BBOX"),
    EQUALS("Equals"),
    DISJOINT("Disjoint"),
    INTERSECTS("Intersects"),
    TOUCHES("Touches"),
    CROSSES("Crosses"),
    WITHIN("Within"),
    CONTAINS("Contains"),
    OVERLAPS("Overlaps"),
    BEYOND("Beyond"),
    D_WITHIN("DWithin");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private SpatialOperatorName(String s)
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
    public static SpatialOperatorName fromString(String s)
    {
        if (s.equals("BBOX"))
            return BBOX;
        else if (s.equals("Equals"))
            return EQUALS;
        else if (s.equals("Disjoint"))
            return DISJOINT;
        else if (s.equals("Intersects"))
            return INTERSECTS;
        else if (s.equals("Touches"))
            return TOUCHES;
        else if (s.equals("Crosses"))
            return CROSSES;
        else if (s.equals("Within"))
            return WITHIN;
        else if (s.equals("Contains"))
            return CONTAINS;
        else if (s.equals("Overlaps"))
            return OVERLAPS;
        else if (s.equals("Beyond"))
            return BEYOND;
        else if (s.equals("DWithin"))
            return D_WITHIN;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum SpatialOperatorName");
    }
}
