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
    @Override
    public String toString()
    {
        return text;
    }
    
    
    
    /**
     * To get the enum constant corresponding to the given String representation
     */
    public static SpatialOperatorName fromString(String s)
    {
        if ("BBOX".equals(s))
            return BBOX;
        else if ("Equals".equals(s))
            return EQUALS;
        else if ("Disjoint".equals(s))
            return DISJOINT;
        else if ("Intersects".equals(s))
            return INTERSECTS;
        else if ("Touches".equals(s))
            return TOUCHES;
        else if ("Crosses".equals(s))
            return CROSSES;
        else if ("Within".equals(s))
            return WITHIN;
        else if ("Contains".equals(s))
            return CONTAINS;
        else if ("Overlaps".equals(s))
            return OVERLAPS;
        else if ("Beyond".equals(s))
            return BEYOND;
        else if ("DWithin".equals(s))
            return D_WITHIN;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum SpatialOperatorName");
    }
}
