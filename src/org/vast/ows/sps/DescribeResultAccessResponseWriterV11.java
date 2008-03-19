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
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSCommonWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSReferenceGroup;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * DescribeResultAccess Response Writer v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to generate an XML DescribeResultAccess response based
 * on values contained in a DescribeResultAccessResponse object for SPS v1.1
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin
 * @date Mar 19, 2008
 * @version 1.0
 */
public class DescribeResultAccessResponseWriterV11 extends AbstractResponseWriter<DescribeResultAccessResponse>
{
	protected OWSCommonWriterV11 owsWriter = new OWSCommonWriterV11();
	
	
	public Element buildXMLResponse(DOMHelper dom, DescribeResultAccessResponse response, String version) throws OWSException
	{
		dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(OGCRegistry.SPS, version));
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OGCRegistry.OWS, "1.1"));
		
		// root element
		Element rootElt = dom.createElement("sps:" + response.getMessageType());
		
		// case of no data available
		if (response.getReasonCode() != null)
		{
			Element noDataElt = dom.addElement(rootElt, "sps:DataNotAvailable");
			dom.setElementValue(noDataElt, "sps:reasonCode", response.getReasonCode());
			
			if (response.getDescription() != null)
				dom.setElementValue(noDataElt, "sps:description", response.getDescription());
		}
		
		// case of list of OWS reference groups
		else
		{
			for (OWSReferenceGroup refGroup: response.getResultGroups())
			{
				Element refGroupElt = dom.addElement(rootElt, "+ows:ReferenceGroup");
				owsWriter.buildRefGroup(dom, refGroupElt, refGroup);
			}
		}
		
		return rootElt;
	}
	
}