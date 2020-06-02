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
import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;


/**
 * <p>
 * Writer to generate an WCS-T Transaction Ackowledgment for
 * version 1.1.1 based on a WCSTransactionAck object. 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 22 nov. 07
 * */
public class WCSTransactionAckWriterV11 extends AbstractResponseWriter<WCSTransactionAck>
{
	protected WCSTransactionWriterV11 requestWriter = new WCSTransactionWriterV11();
	
	
	public Element buildXMLResponse(DOMHelper dom, WCSTransactionAck response, String version) throws OWSException
	{
		WCSTransactionRequest request = response.getRequest();
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WCS, request.getVersion()));
		
		// root element
		Element rootElt = dom.createElement("Acknowledgement");
		
		// time stamp
		String timeStamp = timeFormat.formatIso(response.getTimeStamp().getJulianTime(), 0);
		dom.setElementValue(rootElt, "TimeStamp", timeStamp);
		
		// original request
		Element originalRequestElt = dom.addElement(rootElt, "OperationRequest");
		Element requestElt = requestWriter.buildXMLQuery(dom, request);
		originalRequestElt.appendChild(dom.getFirstChildElement(requestElt));
		
		return rootElt;
	}

}