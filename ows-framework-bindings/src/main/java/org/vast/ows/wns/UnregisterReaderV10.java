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
 * Provides methods to parse an XML Unregister request and
 * create a UnregisterRequest object for WNS version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Jan 16, 2008
 * */
public class UnregisterReaderV10 extends AbstractRequestReader<UnregisterRequest>
{
	
	@Override
	public UnregisterRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		throw new WNSException(noKVP + "WNS Unregister version 1.0");
	}
	
	
	@Override
	public UnregisterRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport();
		UnregisterRequest request = new UnregisterRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// read user ID
		String userId = dom.getElementValue(requestElt, "ID");
		request.setUserId(userId);
				
		super.checkParameters(request, report, OWSUtils.WNS);		
		return request;
	}
}