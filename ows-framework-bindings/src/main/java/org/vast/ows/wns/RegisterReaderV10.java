/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code part of the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wns;

import java.util.Map;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p>
 * Provides methods to parse an XML Register request and
 * create a RegisterRequest object for WNS version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Sep 21, 2007
 * */
public class RegisterReaderV10 extends AbstractRequestReader<RegisterRequest>
{
	
	@Override
	public RegisterRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		throw new WNSException(noKVP + "WNS Register version 1.0");
	}
	
	
	@Override
	public RegisterRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport();
		RegisterRequest request = new RegisterRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// read SingleUser OR MultiUser
		Element userElt = dom.getFirstChildElement(requestElt);
		WNSUser user = readUser(dom, userElt);
		request.setUser(user);
				
		this.checkParameters(request, report);		
		return request;
	}
	
	
	public WNSUser readUser(DOMHelper dom, Element userElt) throws OWSException
	{
		// case of single user
		if (userElt.getLocalName().equals("SingleUser"))
		{
			SingleUser user = new SingleUser();
			
			String userName = dom.getElementValue(userElt, "Name");
			user.setName(userName);
			
			NodeList protocolElts = dom.getElements(userElt, "CommunicationProtocol/*");
			for (int i=0; i<protocolElts.getLength(); i++)
			{
				Element protocolElt = (Element)protocolElts.item(i);
				String protocol = protocolElt.getLocalName();
				String address = dom.getElementValue(protocolElt);
				user.addProtocol(protocol, address);				
			}
			
			return user;
		}
		
		// case of multi user
		else if (userElt.getLocalName().equals("MultiUser"))
		{
			MultiUser user = new MultiUser();
			
			NodeList userIdElts = dom.getElements(userElt, "UserID");
			for (int i=0; i<userIdElts.getLength(); i++)
			{
				Element userIdElt = (Element)userIdElts.item(i);
				String userId = dom.getElementValue(userIdElt);
				user.getUserIds().add(userId);
			}
			
			return user;
		}
		else
			throw new WNSException(invalidXML);
	}
	
	
	/**
     * Checks that Register mandatory parameters are present
     * @param request
     * @throws OWSException
     */
	protected void checkParameters(RegisterRequest request, OWSExceptionReport report) throws OWSException
    {
		// check common params
		super.checkParameters(request, report, OWSUtils.WNS);
		
		// need single user or multi user
		WNSUser user = request.getUser();
		if (user == null)
			report.add(new WNSException(WNSException.missing_param_code, "User"));
		
		if (user instanceof SingleUser)
		{
			SingleUser singleUser = (SingleUser)user;
			
			// need name
			String name = singleUser.getName();
			if (name == null)
				report.add(new WNSException(WNSException.missing_param_code, "Name"));
			
			// need at least one communication method!
			if (singleUser.getProtocolTable().isEmpty())
				report.add(new WNSException(WNSException.missing_param_code, "CommunicationProtocol"));
		}
		else if (user instanceof MultiUser)
		{
			MultiUser multiUser = (MultiUser)user;
			
			if (multiUser.getUserIds().isEmpty())
				report.add(new WNSException(WNSException.missing_param_code, "UserID"));
					
			// need at least two users in the group!
			else if (multiUser.getUserIds().size() < 2)
				report.add(new WNSException(WNSException.invalid_param_code, "UserID"));
		}
				
		report.process();
	}
}