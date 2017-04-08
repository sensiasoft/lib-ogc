/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.LinkedHashMap;
import java.util.Map;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SOS GetResultTemplate
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a GetResultTemplateRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date Aug 8, 2012
 * */
public class GetResultTemplateWriterV20 extends SWERequestWriter<GetResultTemplateRequest>
{
	    
	public GetResultTemplateWriterV20()
	{	    
	}

	
	@Override
	public Map<String, String> buildURLParameters(GetResultTemplateRequest request) throws OWSException
	{
		Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
		
		// offering
        urlParams.put("offering", request.getOffering());
        
        // observed properties (only one officially supported by SOS 2.0!)
        if (!request.getObservables().isEmpty())
        {
            StringBuilder buf = new StringBuilder();
            
            for (String off: request.getObservables())
            {
                buf.append(off);
                buf.append(',');
            }
            buf.deleteCharAt(buf.length()-1);
            
            urlParams.put("observedProperty", buf.toString());
        }
        
        // format
        if (request.getFormat() != null)
            urlParams.put("responseFormat", request.getFormat());
                		
        return urlParams;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetResultTemplateRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		
		// root element
		Element rootElt = dom.createElement("sos:GetResultTemplate");
		addCommonXML(dom, rootElt, request);
		
		// offering
		dom.setElementValue(rootElt, "+sos:offering", request.getOffering());
		
		// observed properties (only one officially supported by SOS 2.0!)
        for (String obs: request.getObservables())
            dom.setElementValue(rootElt, "+sos:observedProperty", obs);
		        
		return rootElt;
	}
}