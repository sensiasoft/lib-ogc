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
import org.vast.json.JsonStreamException;
import org.vast.sensorML.SMLStaxBindings;
import org.vast.swe.json.SWEJsonStreamReader;


public class SMLJsonStreamReader extends SWEJsonStreamReader
{
        
    public SMLJsonStreamReader(InputStream is, String encoding) throws JsonStreamException
    {
        super(is, encoding);
        
        // XML attributes
        addSpecialNames(xmlAttNames,
            "srsName", "srsDimension", "indeterminatePosition",
            "ref", "codeList", "codeListValue", "nilReason");
        
        // XML inline values
        addSpecialNames(inlineValueNames,
            "axis", "setValue", "setStatus", "setMode", "CharacterString", "URL");
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
    
}
