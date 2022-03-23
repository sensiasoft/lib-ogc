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

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import org.vast.json.JsonStreamException;
import org.vast.sensorML.SMLStaxBindings;
import org.vast.swe.json.SWEJsonStreamReader;
import com.google.gson.stream.JsonReader;


public class SMLJsonStreamReader extends SWEJsonStreamReader
{
    protected HashSet<String> isoStringNames;
    
    
    protected static class JsonSmlContext extends JsonContext
    {
        public boolean isIsoObject;
    }
    
    
    public SMLJsonStreamReader(InputStream is, Charset charset)
    {
        super(is, charset);
        currentContext = new JsonSmlContext();
    }
    
    
    public SMLJsonStreamReader(JsonReader reader)
    {
        super(reader);
        currentContext = new JsonSmlContext();
    }
    
    
    @Override
    protected void initSpecialNames()
    {
        super.initSpecialNames();
        
        // XML attributes
        addSpecialNames(xmlAttNames,
            "srsName", "srsDimension", "indeterminatePosition",
            "ref", "codeList", "codeListValue", "nilReason");
        
        // XML inline values
        addSpecialNames(inlineValueNames,
            "axis", "setValue", "setStatus", "setMode", "CharacterString", "URL");
        
        // ISO data types
        isoStringNames = new HashSet<String>();
        addSpecialNames(isoStringNames,
            "individualName", "organisationName", "positionName",
            "hoursOfService", "contactInstructions",
            "voice", "facsimile",
            "deliveryPoint", "city", "administrativeArea", "postalCode", "country", "electronicMailAddress",
            "linkage", "protocol", "applicationProfile", "name", "description", 
            "useLimitation");
    }


    @Override
    public String getNamespaceURI()
    {
        return SMLStaxBindings.NS_URI;
    }
    
    
    protected String getSingularName(String name)
    {
        if (("keywords".equals(name) && !"KeywordList".equals(currentContext.eltName)) ||
            ("capabilities".equals(name) && !"CapabilityList".equals(currentContext.eltName)) ||
            ("characteristics".equals(name) && !"CharacteristicList".equals(currentContext.eltName)) ||
            ("contacts".equals(name) && !"ContactList".equals(currentContext.eltName)) ||
            ("modes".equals(name) && !"ModeChoice".equals(currentContext.eltName)) ||
            "axis".equals(name) || "setStatus".equals(name) ||
            "securityConstraints".equals(name) || "legalConstraints".equals(name))
            return name;
        else if ("capabilities".equals(name))
            return "capability";
        else
            return super.getSingularName(name);
    }
    
    
    protected void pushContext(String eltName)
    {
        JsonContext prevContext = currentContext;
        currentContext = new JsonSmlContext();
        currentContext.parent = prevContext;
        currentContext.eltName = eltName;
        ((JsonSmlContext)currentContext).isIsoObject = ((JsonSmlContext)prevContext).isIsoObject;
    }
    
    
    @Override
    public int next() throws JsonStreamException
    {
        if (((JsonSmlContext)currentContext).isIsoObject && isoStringNames.contains(currentContext.eltName) && currentContext.consumeText)
            return nextTag();
        else
            return super.next();
    }
    
    
    @Override
    public int nextTag() throws JsonStreamException
    {
        if (currentContext.eltName != null)
        {
            String parentName = currentContext.eltName;
            if (parentName.startsWith("CI_") || parentName.startsWith("MD_"))
                ((JsonSmlContext)currentContext).isIsoObject = true;
        }
        
        if (((JsonSmlContext)currentContext).isIsoObject && isoStringNames.contains(currentContext.eltName) && currentContext.consumeText)
        {
            currentContext.consumeText = false;
            pushContext("CharacterString");
            currentContext.text = currentContext.parent.text;
            currentContext.isTextOnly = true;
            currentContext.consumeText = true;
            return eventType = START_ELEMENT;
        }
        else
            return super.nextTag();
    }
    
    
    @Override
    protected boolean isXmlAttribute(String name)
    {
        if ("name".equals(name) && currentContext.eltName != null)
        {
            String parentName = currentContext.eltName;
            if (parentName.startsWith("CI_") || parentName.startsWith("MD_"))
                return false;
        }

        return super.isXmlAttribute(name);
            
    }
    
}
