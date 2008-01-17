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

package org.vast.ows.wns;

import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * WNS Register Response Reader v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse an XML Register response and
 * create a RegisterResponse object for WNS version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Sep 21, 2007
 * @version 1.0
 */
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