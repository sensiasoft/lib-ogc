/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseWriter;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;


/**
 * <p>
 * Writer to generate an adequate XML status response based on values
 * contained in a GetStatusResponse object for SPS v2.0
 * </p>
 *
 * @author Alex Robin
 * @date Mar 04, 2008
 * */
public class GetStatusResponseWriterV20 extends SWEResponseWriter<GetStatusResponse>
{
	protected SPSCommonWriterV20 commonWriter = new SPSCommonWriterV20();
	
	
	public Element buildXMLResponse(DOMHelper dom, GetStatusResponse response, String version) throws OWSException
	{
		try
		{
			dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(SPSUtils.SPS, version));
			
			// root element
			Element rootElt = dom.createElement("sps:" + response.getMessageType());
			
			// write extensions
			writeExtensions(dom, rootElt, response);
			
			// status reports
			for (StatusReport report: response.getReportList())
			{
				Element reportElt = commonWriter.writeReport(dom, report);
				Element resElt = dom.addElement(rootElt, "+sps:status");
				resElt.appendChild(reportElt);
			}
			
			return rootElt;
		}
		catch (XMLWriterException e)
		{
			throw new SPSException("Error while building status response", e);
		}
	}
	
}