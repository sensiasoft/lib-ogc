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
    Alexandre Robin <robin@nsstc.uah.edu>
    Kevin Carter <kcarter@nsstc.uah.edu>
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sas;

import java.io.IOException;
import java.io.InputStream;

import org.vast.ows.AbstractRequestReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.sos.GetObservationRequest;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;

/**
 * <p><b>Title:</b>
 *  SAS Subscribe Response reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * reads the SAS Subscribe XML response
 * </p>
 *
 * <p>Copyright (c) 2006</p>
 * @author Gregoire Berthiau
 * @date Jul 9, 2008
 * @version 1.0
 */

public class SASSubscribeResponseReader
{
	public SASSubscribeResponseReader(){
		
	}
	
	public SASSubscribeResponse parseSASSubscribeXMLResponse(DOMHelper dom, Element subscribeResponseElt) throws  OWSException {
		
		SASSubscribeResponse response = new SASSubscribeResponse();
		
		response.setSubscriptionOfferingID(dom.getAttributeValue(subscribeResponseElt, "SubscriptionID"));
		response.setExpiration(dom.getAttributeValue(subscribeResponseElt, "expires"));
		
		if(dom.existElement("XMPPResponse/XMPPURI"))
				response.setXMPPURI(dom.getElementValue("XMPPResponse/XMPPURI"));
		
		return response;			
	}
	
}

