/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.vast.json.JsonStreamException;
import org.vast.json.JsonStreamReader;


public class SWEJsonStreamReader extends JsonStreamReader
{
    protected final static String NO_PARENT = "";
    
    protected HashSet<String> xmlAttNames = new HashSet<String>();
    protected HashSet<String> inlineValueNames = new HashSet<String>();
    protected Map<String, Map<String, String>> valueArrays = new HashMap<String, Map<String, String>>();
    
    
    public SWEJsonStreamReader(InputStream is, String encoding) throws JsonStreamException
    {
        super(is, encoding);
        
        // XML attributes
        addSpecialNames(xmlAttNames,
                "name", "href", "role", "arcrole", "code", "id", "definition", "referenceFrame",
                "localFrame", "referenceTime", "axisID", "updatable", "optional", "reason",
                "collapseWhiteSpaces", "decimalSeparator", "tokenSeparator", "blockSeparator",
                "byteOrder", "byteEncoding", "byteLength", "significantBits", "bitLength", "dataType", "ref",
                "compression", "encryption", "paddingBytes-after", "paddingBytes-before", "byteLength");
        
        // XML inline values
        addSpecialNames(inlineValueNames, "nilValue");
        
        // value arrays
        addSpecialNamesWithParent(valueArrays, "CountRange", "value");
        addSpecialNamesWithParent(valueArrays, "QuantityRange", "value");
        addSpecialNamesWithParent(valueArrays, "CategoryRange", "value");
        addSpecialNamesWithParent(valueArrays, "TimeRange", "value");
        addSpecialNamesWithParent(valueArrays, "interval", "interval");
    }
    
    
    protected void addSpecialNames(HashSet<String> nameList, String... names)
    {
        for (String name: names)
            nameList.add(name);
    }
    
    
    protected void addSpecialNamesWithParent(Map<String, Map<String, String>> nameMaps, String parentName, String... names)
    {
        Map<String, String> nameMapForParent = nameMaps.get(parentName);
        if (nameMapForParent == null)
        {
            nameMapForParent = new HashMap<String, String>();
            nameMaps.put(parentName, nameMapForParent);
        }
        
        for (String name: names)
            nameMapForParent.put(name, "");
    }
    

    @Override
    protected boolean isXmlAttribute(String name)
    {
        return ( (name.charAt(0) == ATT_PREFIX) || xmlAttNames.contains(name) );
    }
    

    @Override
    protected boolean isInlineValue(String name)
    {
        return inlineValueNames.contains(name);
    }
    
    
    protected boolean isSpecialName(Map<String, String> nameMap, String name)
    {
        if (nameMap != null)
            return nameMap.containsKey(name);
        else
            return false;
    }
    
    
    protected boolean isSpecialPath(Map<String, Map<String, String>> nameMaps, String parentName, String name)
    {
        Map<String, String> nameMap = nameMaps.get(parentName);
        if (nameMap != null)
            return isSpecialName(nameMap, name);
        
        return isSpecialName(nameMaps.get(NO_PARENT), name);
    }
    
    
    @Override
    protected boolean isValueArray(String name)
    {
        String parentName = NO_PARENT;
        if (currentContext.parent != null && currentContext.parent.eltName != null)
            parentName = currentContext.parent.eltName;
        
        return isSpecialPath(valueArrays, parentName, name);
    }

}
