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

import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p>
 * Provides methods to parse an XML Register response and
 * create a RegisterResponse object for WNS version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Jan 21, 2008
 * */
public class RegisterResponseReaderV10 extends AbstractResponseReader<RegisterResponse>
{
	
	@Override
	public RegisterResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		RegisterResponse response = new RegisterResponse();
		response.setVersion("1.0");
		
		String userId = dom.getElementValue(responseElt, "UserID");
		response.setUserId(userId);
		
		return response;
	}
}