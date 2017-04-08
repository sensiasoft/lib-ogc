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

import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * Provides methods to generate an XML Unregister request based
 * on values contained in a UnregisterRequest object for WNS version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Jan 16, 2008
 * */
public class UnregisterWriterV10 extends AbstractRequestWriter<UnregisterRequest>
{

	@Override
	public String buildURLQuery(UnregisterRequest request) throws OWSException
	{
		throw new WNSException(noKVP + "WNS Unregister version 1.0");
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, UnregisterRequest request) throws OWSException
	{
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WNS));
		
		// root element
		Element rootElt = dom.createElement("Unregister");
		addCommonXML(dom, rootElt, request);
		
		// write userID
		dom.setElementValue(rootElt, "ID", request.getUserId());
		
		return rootElt;
	}
}