/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
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
 * <p><b>Title:</b><br/>
 * SOS GetResultTemplate Request Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS GetResultTemplate
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a GetResultTemplateRequest object 
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Aug 8, 2012
 * @version 1.0
 */
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
        
        // observed property
        urlParams.put("observedProperty", request.getObservables().get(0));
                		
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
		
		// observed property
        dom.setElementValue(rootElt, "+sos:observedProperty", request.getObservables().get(0));
		        
		return rootElt;
	}
}