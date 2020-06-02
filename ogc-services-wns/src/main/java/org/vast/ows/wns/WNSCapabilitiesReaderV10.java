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

import java.util.List;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;


/**
 * <p>
 * Reads a WNS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WNSCapabilities objects for WNS version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Jan 17, 2008
 * */
public class WNSCapabilitiesReaderV10 extends OWSCapabilitiesReaderV11
{

	public WNSCapabilitiesReaderV10()
	{
	}
	
	
	@Override
	public OWSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
	{
		OWSServiceCapabilities caps = super.readXMLResponse(dom, capabilitiesElt);
		caps.setService(OWSUtils.WNS);
		return caps;
	}
	
	
	@Override
	protected void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws WNSException
	{
		WNSCapabilities wnsCaps = new WNSCapabilities();
		
		//  Load List of CoverageOfferingBrief elements
		NodeList protocolElts = dom.getElements(capsElt, "Contents/SupportedCommunicationProtocols/*");
		int listSize = protocolElts.getLength();

		// Read supported protocols
		for (int i = 0; i < listSize; i++)
		{
			Element protocolElt = (Element)protocolElts.item(i);
			String flag = dom.getElementValue(protocolElt);
			if (flag.equalsIgnoreCase("true"))
				wnsCaps.getSupportedProtocols().add(protocolElt.getLocalName());
		}
		
		((List<OWSLayerCapabilities>)serviceCaps.getLayers()).add(wnsCaps);
	}

}
