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

import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * WNS Unregister Response Builder v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate an XML Unregister response based
 * on values contained in a UnregisterResponse object for WNS version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Jan 16, 2008
 * @version 1.0
 */
public class UnregisterResponseWriterV10 extends AbstractResponseWriter<UnregisterResponse>
{

	@Override
	public Element buildXMLResponse(DOMHelper dom, UnregisterResponse response, String version) throws OWSException
	{
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OGCRegistry.WNS));
		
		// root element
		Element respElt = dom.createElement("UnregisterResponse");
		dom.setElementValue(respElt, "status", response.getStatus());
		
		return respElt;
	}
}