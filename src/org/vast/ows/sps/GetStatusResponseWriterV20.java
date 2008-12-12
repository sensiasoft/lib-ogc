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

import org.vast.cdm.common.CDMException;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSException;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * GetStatus Response Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to generate an XML GetStatus response based
 * on values contained in a GetStatusResponse object for SPS v2.0
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin
 * @date Mar 04, 2008
 * @version 1.0
 */
public class GetStatusResponseWriterV20 extends AbstractResponseWriter<GetStatusResponse>
{
	protected SPSCommonWriterV20 commonWriter = new SPSCommonWriterV20();
	
	
	public Element buildXMLResponse(DOMHelper dom, GetStatusResponse response, String version) throws OWSException
	{
		try
		{
			dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI("SPS", version));
			
			// root element
			Element rootElt = dom.createElement("sps:" + response.getMessageType());
			
			// progress report
			Element reportElt = commonWriter.writeStatusReport(dom, response.getReport());
			rootElt.appendChild(reportElt);
			
			return rootElt;
		}
		catch (CDMException e)
		{
			throw new SPSException(e);
		}
	}
	
}