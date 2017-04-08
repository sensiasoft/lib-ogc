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
    @Override
    public String toString()
    {
        return text;
    }
    
    
    
    /**
     * To get the enum constant corresponding to the given String representation
     */
    public static ComparisonOperatorName fromString(String s)
    {
        if ("PropertyIsEqualTo".equals(s))
            return PROPERTY_IS_EQUAL_TO;
        else if ("PropertyIsNotEqualTo".equals(s))
            return PROPERTY_IS_NOT_EQUAL_TO;
        else if ("PropertyIsLessThan".equals(s))
            return PROPERTY_IS_LESS_THAN;
        else if ("PropertyIsGreaterThan".equals(s))
            return PROPERTY_IS_GREATER_THAN;
        else if ("PropertyIsLessThanOrEqualTo".equals(s))
            return PROPERTY_IS_LESS_THAN_OR_EQUAL_TO;
        else if ("PropertyIsGreaterThanOrEqualTo".equals(s))
            return PROPERTY_IS_GREATER_THAN_OR_EQUAL_TO;
        else if ("PropertyIsLike".equals(s))
            return PROPERTY_IS_LIKE;
        else if ("PropertyIsNull".equals(s))
            return PROPERTY_IS_NULL;
        else if ("PropertyIsNil".equals(s))
            return PROPERTY_IS_NIL;
        else if ("PropertyIsBetween".equals(s))
            return PROPERTY_IS_BETWEEN;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum ComparisonOperatorName");
    }
}
