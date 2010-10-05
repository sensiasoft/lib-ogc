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

package org.vast.ows.sps;

import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSException;


/**
 * <p><b>Title:</b><br/>
 * SPS Capabilities Reader V2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads an SPS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * SPSOfferingCapabilities objects for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2009</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 5 jan. 09
 * @version 1.0
 */
public class SPSCapabilitiesReaderV20 extends OWSCapabilitiesReaderV11
{

	public SPSCapabilitiesReaderV20()
	{
	}
	
	
	@Override
	protected void readContents(DOMHelper dom, Element capsElt) throws OWSException
	{
		//  Load List of SensorOffering elements
		NodeList offerings = dom.getElements(capsElt, "contents/SPSContents/offering/SensorOffering");
		int numOfferings = offerings.getLength();

		// Go through each element and populate offering caps
		for (int i = 0; i < numOfferings; i++)
		{
			Element offeringElt = (Element) offerings.item(i);
			SPSOfferingCapabilities offering = new SPSOfferingCapabilities();
			offering.setParent(serviceCaps);
			
			// title
			String title = dom.getElementValue(offeringElt, "identifier");
			offering.setTitle(title);
			
			// abstract
			String description = dom.getElementValue(offeringElt, "description");
			offering.setDescription(description);
			
			// sensorID
			String sensorID = dom.getElementValue(offeringElt, "procedure");
			offering.setSensorID(sensorID);
			
			readROI(offering, dom, offeringElt);
			//readTOI(offering, dom, offeringElt);
			//readFOI(offering, dom, offeringElt);

			serviceCaps.getLayers().add(offering);
		}
	}
	

	/**
	 * Reads ROI as point or polygon
	 * @param layerCap
	 * @param dom
	 * @param layerElt
	 */
	protected void readROI(SPSOfferingCapabilities layerCap, DOMHelper dom, Element layerElt)
	{
		// TODO read ROI
	}
	
}
