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
    protected static final HashSet<String> ISO_STRING_NAMES = new HashSet<String>();
    protected JsonContext isoEltRootCtx;
    
    static
    {
        initSpecialNames();
    }
    
    
    public SMLJsonStreamReader(InputStream is, Charset charset)
    {
        super(is, charset);
    }
    
    
    public SMLJsonStreamReader(JsonReader reader)
    {
        super(reader);
    }
    
    
    protected static void initSpecialNames()
    {
        // XML attributes
        addSpecialNames(XML_ATT_NAMES,
            "srsName", "srsDimension", "indeterminatePosition",
            "ref", "codeList", "codeListValue", "nilReason");
        
        // XML inline values
        addSpecialNames(INLINE_VALUES_NAMES,
            "axis", "setValue", "setStatus", "setMode", "CharacterString", "URL");
        
        // ISO data types
        addSpecialNames(ISO_STRING_NAMES,
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
    
    
    @Override
    protected void pushContext(String eltName)
    {
        super.pushContext(eltName);
        
        // mark begin of root ISO object
        if (eltName != null && isoEltRootCtx == null &&
           (eltName.startsWith("CI_") || eltName.startsWith("MD_")))
            isoEltRootCtx = currentContext;
    }
    
    
    @Override
    protected void popContext()
    {
        // mark end of ISO object
        if (currentContext == isoEltRootCtx)
            isoEltRootCtx = null;
        
        super.popContext();
    }
    
    
    @Override
    public int next() throws JsonStreamException
    {
        if (isoEltRootCtx != null && ISO_STRING_NAMES.contains(currentContext.eltName) && currentContext.consumeText)
            return nextTag();
        else
            return super.next();
    }
    
    
    @Override
    public int nextTag() throws JsonStreamException
    {
        if (isoEltRootCtx != null && ISO_STRING_NAMES.contains(currentContext.eltName) && currentContext.consumeText)
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
        if (isoEltRootCtx != null && "name".equals(name))
                return false;
        
        return super.isXmlAttribute(name);
    }
    
}
