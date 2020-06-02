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

package org.vast.ows.sps;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseWriter;
import org.w3c.dom.*;
import org.vast.util.DateTime;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;


/**
 * <p>
 * Writer to generate an XML GetFeasibility response based
 * on values contained in a GetFeasibilityResponse object for SPS v2.0
 * </p>
 *
 * @author Alex Robin
 * @date Mar 04, 2008
 * */
public class GetFeasibilityResponseWriterV20 extends SWEResponseWriter<GetFeasibilityResponse>
{
	protected SPSCommonWriterV20 commonWriter = new SPSCommonWriterV20();
		
	
	public Element buildXMLResponse(DOMHelper dom, GetFeasibilityResponse response, String version) throws OWSException
	{
		try
		{
			dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(SPSUtils.SPS, version));
			
			// root element
			Element rootElt = dom.createElement("sps:" + response.getMessageType());
			
			// write extensions
			writeExtensions(dom, rootElt, response);
			
			// latest response time
			DateTime latestResp = response.getLatestResponseTime();
			if (latestResp != null)
				dom.setElementValue(rootElt, "sps:latestResponseTime",
				        timeFormat.formatIso(latestResp.getJulianTime(), 0));
			
			// feasibility report
			Element reportElt = commonWriter.writeFeasibilityReport(dom, response.getReport());
			Element resElt = dom.addElement(rootElt, "sps:result");
			resElt.appendChild(reportElt);
			
			// alternatives
			// TODO write alternatives
			
			return rootElt;
		}
		catch (XMLWriterException e)
        {
            throw new SPSException("Error while building feasibilty response", e);
        }
	}
	
}