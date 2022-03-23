/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML.json;

import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Set;
import org.vast.json.JsonStreamException;
import org.vast.ogc.gml.GMLStaxBindings;
import org.vast.sensorML.SMLStaxBindings;
import org.vast.swe.json.SWEJsonStreamWriter;
import com.google.common.collect.ImmutableSet;
import com.google.gson.stream.JsonWriter;


public class SMLJsonStreamWriter extends SWEJsonStreamWriter
{
    static Set<String> ISO_BASIC_TYPES = ImmutableSet.of(
        "Boolean", "CharacterString", "Date", "Decimal", "Real", "Integer"
    );
    
    static Set<String> ISO_CODELIST_TYPES = ImmutableSet.of(
        "CI_RoleCode"
    );
        
    public SMLJsonStreamWriter(OutputStream os, Charset charset)
    {
        super(os, charset);
    }
    
    
    public SMLJsonStreamWriter(JsonWriter writer)
    {
        super(writer);
    }
    
    
    @Override
    protected void initSpecialNames()
    {
        super.initSpecialNames();
        
        addSpecialNames(arrays, SMLStaxBindings.NS_URI,
            "keywords", "identification", "classification",
            "validTime", "securityConstraints", "legalConstraints",
            "characteristics", "capabilities", "contacts",
            "documentation", "history", "modes", "mode", "position",
            "localReferenceFrame", "localTimeFrame", "timePosition",
            "keyword", "input", "output", "parameter", "identifier",
            "classifier", "characteristic", "capability",
            "contact", "document", "event", "feature", "property",
            "connection", "component", "axis", "algorithm",
            "setValue", "setArrayValues", "setConstraint",
            "setMode", "setStatus");

        addSpecialNames(arrays, GMLStaxBindings.NS_URI, "name");
    }
    
    
    @Override
    protected String getPluralName(String localName)
    {
        if ("capability".equals(localName))
            return "capabilities";
        else
            return super.getPluralName(localName);
    }
    
    
    @Override
    public void writeAttribute(String localName, String value) throws JsonStreamException
    {
        if ("codeSpace".equals(localName)) // to skip UID codespace
            return;
        else if (ISO_BASIC_TYPES.contains(currentContext.eltName)) // skip all basic types attributes
            return;
        else
            super.writeAttribute(localName, value);
    }
    
    
    @Override
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws JsonStreamException
    {
        // keep only ISO basic types value
        if (ISO_BASIC_TYPES.contains(localName))
        {
            pushContext(localName);
            currentContext.skipParent = true;
        }
        else
            super.writeStartElement(prefix, localName, namespaceURI);
    }
    
    
    @Override
    public void writeEndElement() throws JsonStreamException
    {
        // keep only ISO basic types value
        if (ISO_BASIC_TYPES.contains(currentContext.eltName))
        {
            super.writeEndElement();
            currentContext.skipParent = true;
        }
        else
            super.writeEndElement();
    }
}
