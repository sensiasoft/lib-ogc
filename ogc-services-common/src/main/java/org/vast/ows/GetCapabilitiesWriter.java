/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import org.w3c.dom.*;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * Provides methods to generate a KVP or XML GetCapabilities request based
 * on values contained in a GetCapabilities object for version 1.0
 * </p>
 *
 * @author Alex Robin
 * @since Sep 21, 2007
 * */
public class GetCapabilitiesWriter extends AbstractRequestWriter<GetCapabilitiesRequest>
{
	
	@Override
	public String buildURLQuery(GetCapabilitiesRequest request) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(request.getGetServer());
		
        urlBuff.append("service=" + request.getService());
        
        if (request.getAcceptedVersions().isEmpty())
        {
            if (request.getVersion() != null)
                urlBuff.append("&version=" + request.getVersion());
        }
        else
        {
            urlBuff.append("&acceptVersions=");
            for (String version: request.getAcceptedVersions())
                urlBuff.append(version).append(',');
            urlBuff.deleteCharAt(urlBuff.length()-1);
        }
            
        urlBuff.append("&request=" + request.getOperation());
		
        if (request.getSection() != null)
        	urlBuff.append("&section=" + request.getSection());
        
        return urlBuff.toString();
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetCapabilitiesRequest request) throws OWSException
	{
		String nsUri = OGCRegistry.getNamespaceURI(request.getService(), request.getVersion());
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, nsUri);
		
		Element rootElt = dom.createElement(request.getOperation());		
		dom.setAttributeValue(rootElt, "service", request.getService());
		
		if (request.getAcceptedVersions().isEmpty())
        {
            if (request.getVersion() != null)
                dom.setAttributeValue(rootElt, "version", request.getVersion());
        }
        else
        {
            for (String version: request.getAcceptedVersions())
                dom.setElementValue(rootElt, "+AcceptVersions", version);
        }
		
		if (request.getSection() != null)
			dom.setElementValue(rootElt, "Sections", request.getSection());
		
		return rootElt;
	}
}