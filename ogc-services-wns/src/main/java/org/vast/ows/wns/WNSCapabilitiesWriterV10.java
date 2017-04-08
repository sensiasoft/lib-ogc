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
import org.vast.ows.OWSCapabilitiesWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes a WNS server capabilities document from
 * OWSServiceCapabilities and WNSCapabilities
 * objects for WNS version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Jan 17, 2008
 * */
public class WNSCapabilitiesWriterV10 extends OWSCapabilitiesWriterV11
{

	@Override
	public Element buildXMLResponse(DOMHelper dom, OWSServiceCapabilities caps, String version) throws OWSException
	{
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WNS));
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		Element capsElt = dom.createElement("Capabilities");
		writeRootAttributes(dom, capsElt, caps, version);
		writeServiceIdentification(dom, capsElt, caps);
		writeServiceProvider(dom, capsElt, caps.getServiceProvider());
		writeOperationsMetadata(dom, capsElt, caps);
		writeContents(dom, capsElt, caps, version);
				
		return capsElt;
	}
	
	
	@Override
	protected void writeContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps, String version) throws OWSException
	{
		Element contentsElt = dom.addElement(capsElt, "Contents");
		
		// supported protocol list
		Element suppProtocolElt = dom.addElement(contentsElt, "SupportedCommunicationProtocols");
		WNSCapabilities wnsCaps = (WNSCapabilities)caps.getLayers().get(0);
		
		// write each protocol flag
		String [] allProtocols = {"XMPP", "SMS", "Phone", "Fax", "Email"};
		for (int i=0; i<allProtocols.length; i++)
		{
			boolean supported = false;
			if (wnsCaps.getSupportedProtocols().contains(allProtocols[i]))
				supported = true;
			
			dom.setElementValue(suppProtocolElt, allProtocols[i], Boolean.toString(supported));
		}
		
		dom.setElementValue(contentsElt, "MaxTTLOfMessages", "PT1D");
	}
}
