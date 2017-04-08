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
import org.vast.ows.OWSCommonWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSReferenceGroup;
import org.vast.ows.swe.SWEResponseWriter;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * Writer to generate an XML DescribeResultAccess response based
 * on values contained in a DescribeResultAccessResponse object for SPS v2.0
 * </p>
 *
 * @author Alex Robin
 * @date Mar 19, 2008
 * */
public class DescribeResultAccessResponseWriterV20 extends SWEResponseWriter<DescribeResultAccessResponse>
{
	protected OWSCommonWriterV11 owsWriter = new OWSCommonWriterV11();
	
	
	public Element buildXMLResponse(DOMHelper dom, DescribeResultAccessResponse response, String version) throws OWSException
	{
		dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(SPSUtils.SPS, version));
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(SPSUtils.OWS, "1.1"));
		
		// root element
		Element rootElt = dom.createElement("sps:" + response.getMessageType());
		
		// write extensions
		writeExtensions(dom, rootElt, response);
		
		// case of no data available
		if (response.getReasonCode() != null)
		{
			Element noDataElt = dom.addElement(rootElt, "sps:availability/sps:unavailable/sps:DataNotAvailable");
			dom.setElementValue(noDataElt, "sps:unavailableCode", response.getReasonCode());			
			if (response.getDescription() != null)
				dom.setElementValue(noDataElt, "sps:message", response.getDescription());
		}
		
		// case of list of OWS reference groups
		else
		{
			Element dataElt = dom.addElement(rootElt, "sps:availability/sps:available/sps:DataAvailable");			
			for (OWSReferenceGroup refGroup: response.getResultGroups())
			{				
				Element refGroupElt = dom.addElement(dataElt, "+sps:dataReference/ows:ReferenceGroup");
				owsWriter.buildRefGroup(dom, refGroupElt, refGroup);
			}
		}
		
		return rootElt;
	}
	
}