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
 * POJO class for XML type VersionActionTokens(@http://www.opengis.net/fes/2.0).
 *
 */
public enum VersionActionTokens
{
    FIRST("FIRST"),
    LAST("LAST"),
    PREVIOUS("PREVIOUS"),
    NEXT("NEXT"),
    ALL("ALL");
    
    private final String text;
    
    
    
    /**
     * Private constructor for storing string representation
     */
    private VersionActionTokens(String s)
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
    public static VersionActionTokens fromString(String s)
    {
        if ("FIRST".equals(s))
            return FIRST;
        else if ("LAST".equals(s))
            return LAST;
        else if ("PREVIOUS".equals(s))
            return PREVIOUS;
        else if ("NEXT".equals(s))
            return NEXT;
        else if ("ALL".equals(s))
            return ALL;
        
        throw new IllegalArgumentException("Invalid token " + s + " for enum VersionActionTokens");
    }
}
