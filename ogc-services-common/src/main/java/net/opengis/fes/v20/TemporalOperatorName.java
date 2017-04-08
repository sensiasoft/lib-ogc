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
 * POJO class for XML type TemporalOperatorNameType(@http://www.opengis.net/fes/2.0).
 *
 */
public enum TemporalOperatorName
{
    AFTER("After"),
    BEFORE("Before"),
    BEGINS("Begins"),
    BEGUN_BY("BegunBy"),
    T_CONTAINS("TContains"),
    DURING("During"),
    T_EQUALS("TEquals"),
    T_OVERLAPS("TOverlaps"),
    MEETS("Meets"),
    OVERLAPPED_BY("OverlappedBy"),
    MET_BY("MetBy"),
    ENDS("Ends"),
    ENDED_BY("EndedBy");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private TemporalOperatorName(String s)
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
    public static TemporalOperatorName fromString(String s)
    {
        if ("After".equals(s))
            return AFTER;
        else if ("Before".equals(s))
            return BEFORE;
        else if ("Begins".equals(s))
            return BEGINS;
        else if ("BegunBy".equals(s))
            return BEGUN_BY;
        else if ("TContains".equals(s))
            return T_CONTAINS;
        else if ("During".equals(s))
            return DURING;
        else if ("TEquals".equals(s))
            return T_EQUALS;
        else if ("TOverlaps".equals(s))
            return T_OVERLAPS;
        else if ("Meets".equals(s))
            return MEETS;
        else if ("OverlappedBy".equals(s))
            return OVERLAPPED_BY;
        else if ("MetBy".equals(s))
            return MET_BY;
        else if ("Ends".equals(s))
            return ENDS;
        else if ("EndedBy".equals(s))
            return ENDED_BY;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum TemporalOperatorName");
    }
}
