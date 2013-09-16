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

package org.vast.ows.sos;

import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLTimeWriter;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.DescribeSensorRequest;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS DescribeSensor Request Writer v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS DescribeSensor
 * request based on values contained in a DescribeSensorRequest
 * object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 10, 2007
 * @version 1.0
 */
public class DescribeSensorWriterV10 extends AbstractRequestWriter<DescribeSensorRequest>
{
	protected GMLTimeWriter timeWriter = new GMLTimeWriter("3.1");
    
    
	public DescribeSensorWriterV10()
	{
	}

	
	@Override
	public String buildURLQuery(DescribeSensorRequest request) throws OWSException
	{
		StringBuilder urlBuff = new StringBuilder(request.getGetServer());
        addCommonArgs(urlBuff, request);
        
		// procedure
        urlBuff.append("&procedure=" + request.getProcedureID());
        
        if (request.getTime() != null)
        {
        	urlBuff.append("&time=");
        	this.writeTimeArgument(urlBuff, request.getTime());
        }
        
        // format
        urlBuff.append("&format=" + request.getFormat());

        String url = urlBuff.toString();
        url = url.replaceAll(" ","%20");
		return url;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, DescribeSensorRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));			
		
		// root element
		Element rootElt = dom.createElement("sos:GetObservation");
		addCommonXML(dom, rootElt, request);
		
		// procedure
        if (request.getProcedureID() != null)
        	dom.setElementValue(rootElt, "sos:procedure", request.getProcedureID());
		
		// result format
		dom.setAttributeValue(rootElt, "@resultFormat", request.getFormat());
        
		return rootElt;
	}
}