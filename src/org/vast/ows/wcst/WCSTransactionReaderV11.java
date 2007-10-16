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

package org.vast.ows.wcst;

import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;
import org.vast.ows.wcs.WCSException;


/**
 * <p><b>Title:</b><br/>
 * WCS Transaction Request Reader v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse an XML Transaction request and
 * create a WCSTransactionRequest object for version 1.1
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 11, 2007
 * @version 1.0
 */
public class WCSTransactionReaderV11 extends AbstractRequestReader<WCSTransactionRequest>
{
	
	@Override
	public WCSTransactionRequest readURLQuery(String queryString) throws OWSException
	{
		throw new WCSException(noKVP + "WCS 1.1 Transaction");
	}
	
	
	@Override
	public WCSTransactionRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		WCSTransactionRequest request = new WCSTransactionRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// get all Coverage Reference Groups
		NodeList covElts = dom.getElements(requestElt, "InputCoverages/Coverage");
		for (int i=0; i<covElts.getLength(); i++)
		{
			CoverageTransaction coverageRef = new CoverageTransaction();
			Element coverageElt = (Element)covElts.item(i);
			
			// title = transaction type (add, update, delete)
			String title = dom.getElementValue(coverageElt, "Title");
			coverageRef.setTitle(title);
			
			// description
			String description = dom.getElementValue(coverageElt, "Abstract");
			coverageRef.setDescription(description);
			
			// identifier
			String identifier = dom.getElementValue(coverageElt, "Identifier");
			coverageRef.setIdentifier(identifier);
			
			// references
			NodeList refElts = dom.getElements(coverageElt, "Reference");
			for (int j=0; j<refElts.getLength(); j++)
			{
				OWSReference ref = new OWSReference();
				Element refElt = (Element)refElts.item(j);
				
				// role
				String role = dom.getAttributeValue(refElt, "@role");
				ref.setRole(role);
				
				// href
				String endpoint = dom.getAttributeValue(refElt, "@href");
				ref.setHref(endpoint);
				
				//TODO parse embedded RequestMessage
			}
			
			request.getInputCoverages().add(coverageRef);
		}
		
		return request;
	}
}