/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

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
