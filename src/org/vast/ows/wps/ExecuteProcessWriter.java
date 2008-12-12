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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * WPS ExecuteProcess Request Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML WPS ExecuteProcess
 * request based on values contained in a ExecuteProcessRequest
 * object
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Gregoire Berthiau
 * @date Dec 2, 2008
 * @version 1.0
 */
public class ExecuteProcessWriter extends AbstractRequestWriter<ExecuteProcessRequest>
{
    
	public ExecuteProcessWriter()
	{
	}
	
	
	@Override
	public String buildURLQuery(ExecuteProcessRequest request) throws OWSException
	{
		return null;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, ExecuteProcessRequest request) throws OWSException
	{
		dom.addUserPrefix("wps", OGCRegistry.getNamespaceURI(OGCRegistry.WPS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OGCRegistry.OGC));
		
		// root element
		Element rootElt = dom.createElement("wps:executeProcess");
		addCommonXML(dom, rootElt, request);
		
		// offering
		dom.setElementValue(rootElt, "wps:offering", request.getOffering());

		// response format
		dom.setElementValue(rootElt, "wps:responseFormat", request.getResponseFormat());
		
		// response format
		dom.setElementValue(rootElt, "wps:responseMode", request.getResponseMode());
        
		return rootElt;
	}
	
	
	public Element addEncoding(DOMHelper dom, ExecuteProcessRequest request) throws OWSException
	{
		dom.addUserPrefix("wps", OGCRegistry.getNamespaceURI(OGCRegistry.WPS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OGCRegistry.OGC));
		
		// root element
		Element rootElt = dom.createElement("wps:executeProcess");
		addCommonXML(dom, rootElt, request);
		
		// offering
		dom.setElementValue(rootElt, "wps:offering", request.getOffering());

		// response format
		dom.setElementValue(rootElt, "wps:responseFormat", request.getResponseFormat());
		
		// response format
		dom.setElementValue(rootElt, "wps:responseMode", request.getResponseMode());
        
		return rootElt;
	}
	
	
	public Element addValues(DOMHelper dom, ExecuteProcessRequest request) throws OWSException
	{
		dom.addUserPrefix("wps", OGCRegistry.getNamespaceURI(OGCRegistry.WPS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OGCRegistry.OGC));
		
		// root element
		Element rootElt = dom.createElement("wps:executeProcess");
		addCommonXML(dom, rootElt, request);
		
		// offering
		dom.setElementValue(rootElt, "wps:offering", request.getOffering());

		// response format
		dom.setElementValue(rootElt, "wps:responseFormat", request.getResponseFormat());
		
		// response format
		dom.setElementValue(rootElt, "wps:responseMode", request.getResponseMode());
        
		return rootElt;
	}
    
}