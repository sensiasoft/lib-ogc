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

import java.util.Enumeration;
import java.util.List;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * Provides methods to generate an XML Register request based
 * on values contained in a RegisterRequest object for WNS version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Jan 16, 2008
 * */
public class RegisterWriterV10 extends AbstractRequestWriter<RegisterRequest>
{

	@Override
	public String buildURLQuery(RegisterRequest request) throws OWSException
	{
		throw new WNSException(noKVP + "WNS version 1.0");
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, RegisterRequest request) throws OWSException
	{
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WNS));
		
		// root element
		Element rootElt = dom.createElement("Register");
		addCommonXML(dom, rootElt, request);
		
		// write user
		Element userElt = writeUser(dom, request.getUser());
		rootElt.appendChild(userElt);
		
		return rootElt;
	}
	
	
	public Element writeUser(DOMHelper dom, WNSUser user)
	{
		// case of single user
		if (user instanceof SingleUser)
		{
			SingleUser singleUser = (SingleUser)user;
			
			Element singleUserElt = dom.createElement("SingleUser");
			dom.setElementValue(singleUserElt, "Name", singleUser.getName());
			
			Element commElt = dom.addElement(singleUserElt, "CommunicationProtocol");
			Enumeration <String> protocols = singleUser.getProtocolTable().keys();
			while (protocols.hasMoreElements())
			{
				String protocol = protocols.nextElement();
				List<String> addressList = singleUser.getProtocolTable().get(protocol);
				for (int i=0; i<addressList.size(); i++)
					dom.setElementValue(commElt, protocol, addressList.get(i));
			}
			
			return singleUserElt;
		}
		
		// case of multi user
		else if (user instanceof MultiUser)
		{
			MultiUser multiUser = (MultiUser)user;
			Element multiUserElt = dom.createElement("MultiUser");
						
			List<String> userIds = multiUser.getUserIds();
			for (int i=0; i<userIds.size(); i++)
				dom.setElementValue(multiUserElt, "UserID", userIds.get(i));
			
			return multiUserElt;
		}
		
		return null;
	}
}