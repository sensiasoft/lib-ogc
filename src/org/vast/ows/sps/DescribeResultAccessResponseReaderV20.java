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

import org.vast.ows.AbstractResponseReader;
import org.vast.ows.OWSCommonReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSReferenceGroup;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b>
 * DescribeResultAccess Response Reader v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DescribeResultAccessResponseReaderV11 type description
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Mar, 19 2008
 * @version 1.0
 */
public class DescribeResultAccessResponseReaderV20 extends AbstractResponseReader<DescribeResultAccessResponse>
{
	protected OWSCommonReaderV11 owsReader = new OWSCommonReaderV11();
	
	
	public DescribeResultAccessResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		DescribeResultAccessResponse response = new DescribeResultAccessResponse();
		response.setVersion("1.1");
		
		// case of no data available
		Element noDataElt = dom.getElement(responseElt, "DataNotAvailable");
		if (noDataElt != null)
		{
			String reasonCode = dom.getElementValue(noDataElt, "reasonCode");
			response.setReasonCode(reasonCode);				
			String description = dom.getElementValue(noDataElt, "description");
			response.setDescription(description);
		}
		
		// case of list of OWS reference groups
		else
		{
			NodeList refGroupElts = dom.getElements(responseElt, "ReferenceGroup");
			for (int i=0; i<refGroupElts.getLength(); i++)
			{
				OWSReferenceGroup refGroup = new OWSReferenceGroup();
				Element refGroupElt = (Element)refGroupElts.item(i);
				owsReader.readReferenceGroup(dom, refGroupElt, refGroup);
				response.getResultGroups().add(refGroup);
			}				
		}
		
		return response;
	}	
}