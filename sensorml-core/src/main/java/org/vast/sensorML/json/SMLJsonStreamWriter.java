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
import org.vast.json.JsonStreamException;
import org.vast.ogc.gml.GMLStaxBindings;
import org.vast.sensorML.SMLStaxBindings;
import org.vast.swe.json.SWEJsonStreamWriter;
import com.google.gson.stream.JsonWriter;


public class SMLJsonStreamWriter extends SWEJsonStreamWriter
{
        
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
        if (localName.equals("codeSpace"))
            return;
        else
            super.writeAttribute(localName, value);
    }
}
