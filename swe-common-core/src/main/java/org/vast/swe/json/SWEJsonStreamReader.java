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
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.vast.json.JsonStreamReader;
import org.vast.swe.SWEStaxBindings;
import com.google.gson.stream.JsonReader;


public class SWEJsonStreamReader extends JsonStreamReader
{
    protected static final String NO_PARENT = "";
    protected static final HashSet<String> XML_ATT_NAMES = new HashSet<String>();
    protected static final HashSet<String> INLINE_VALUES_NAMES = new HashSet<String>();
    protected static final Map<String, Map<String, String>> VALUE_ARRAYS = new HashMap<String, Map<String, String>>();
    
    static
    {
        initSpecialNames();
    }
    
    
    public SWEJsonStreamReader(InputStream is, Charset charset)
    {
        super(is, charset);
    }
    
    
    public SWEJsonStreamReader(JsonReader reader)
    {
        super(reader);
    }
    
    
    protected static void initSpecialNames()
    {
        // XML attributes
        addSpecialNames(XML_ATT_NAMES,
                "name", "href", "title", "role", "arcrole", "code", "reason", "id", "definition",
                "referenceFrame", "localFrame", "referenceTime", "axisID", "updatable", "optional",
                "collapseWhiteSpaces", "decimalSeparator", "tokenSeparator", "blockSeparator",
                "byteOrder", "byteEncoding", "byteLength", "significantBits", "bitLength", "dataType", "ref",
                "compression", "encryption", "paddingBytes-after", "paddingBytes-before", "byteLength");
        
        // XML inline values
        addSpecialNames(INLINE_VALUES_NAMES, "nilValue");
        
        // value arrays
        addSpecialNamesWithParent(VALUE_ARRAYS, "CountRange", "value");
        addSpecialNamesWithParent(VALUE_ARRAYS, "QuantityRange", "value");
        addSpecialNamesWithParent(VALUE_ARRAYS, "CategoryRange", "value");
        addSpecialNamesWithParent(VALUE_ARRAYS, "TimeRange", "value");
        addSpecialNamesWithParent(VALUE_ARRAYS, "interval", "interval");
    }
    
    
    protected static void addSpecialNames(HashSet<String> nameList, String... names)
    {
        for (String name: names)
            nameList.add(name);
    }
    
    
    protected static void addSpecialNamesWithParent(Map<String, Map<String, String>> nameMaps, String parentName, String... names)
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
        return ( (name.charAt(0) == ATT_PREFIX) || XML_ATT_NAMES.contains(name) );
    }
    

    @Override
    protected boolean isInlineValue(String name)
    {
        return INLINE_VALUES_NAMES.contains(name);
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
        
        return isSpecialPath(VALUE_ARRAYS, parentName, name);
    }


    @Override
    public String getNamespaceURI()
    {
        return SWEStaxBindings.NS_URI;
    }

}
