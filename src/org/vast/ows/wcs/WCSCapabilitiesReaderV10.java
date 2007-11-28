/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSCapabilitiesReaderV0;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.util.Bbox;


/**
 * <p><b>Title:</b><br/>
 * WCS Capabilities Reader V1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads a WCS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WCSLayerCapabilities objects for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 27 nov. 07
 * @version 1.0
 */
public class WCSCapabilitiesReaderV10 extends OWSCapabilitiesReaderV0
{

	public WCSCapabilitiesReaderV10()
	{
	}
	
	
	public WCSCapabilitiesReaderV10(String version)
	{
		this.version = version;
	}
	
	
	@Override
    protected String buildQuery() throws OWSException
    {
        String query = null;
        query = this.server + "SERVICE=WCS&VERSION=1.0&REQUEST=GetCapabilities";
        return query;
    }
	
	
	@Override
	public OWSServiceCapabilities readCapabilities(DOMHelper dom, Element capabilitiesElt) throws OWSException
	{
		OWSServiceCapabilities caps = super.readCapabilities(dom, capabilitiesElt);
		caps.setService(OGCRegistry.WCS);
		return caps;
	}
	
	
	@Override
	protected void readContents(DOMHelper dom, Element capsElt) throws WCSException
	{
		//  Load List of CoverageOfferingBrief elements
		NodeList layers = dom.getElements(capsElt, "ContentMetadata/CoverageOfferingBrief");
		int numLayers = layers.getLength();

		// Go through each element and populate layerCaps
		for (int i = 0; i < numLayers; i++)
		{
			Element layerCapElt = (Element) layers.item(i);
			WCSLayerCapabilities layerCap = new WCSLayerCapabilities();
			layerCap.setParent(serviceCaps);
			readIdentification(layerCap, dom, layerCapElt);		
			read2DBboxList(layerCap, dom, layerCapElt);
			serviceCaps.getLayers().add(layerCap);
		}
	}
	

	/**
	 * Reads list of WGS84 BBOX for given layer
	 * @param bboxElement
	 * @param Bbox
	 */
	protected void read2DBboxList(WCSLayerCapabilities layerCap, DOMHelper dom, Element layerElt) throws WCSException
	{
		NodeList bboxElts = dom.getElements(layerElt, "lonLatEnvelope");
		int numElts = bboxElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element bboxElt = (Element) bboxElts.item(i);
			NodeList posElts = dom.getElements(bboxElt, "pos");
			if (posElts.getLength() < 2)
				throw new WCSException("Invalid Bbox");
			
			String[] lowerCoords = dom.getElementValue((Element)posElts.item(0)).split(" ");
			String[] upperCoords = dom.getElementValue((Element)posElts.item(1)).split(" ");
			String crs = dom.getAttributeValue(bboxElt, "@srsName");
			
			Bbox bbox = new Bbox();
			double minX = Double.parseDouble(lowerCoords[0]);
			double minY = Double.parseDouble(lowerCoords[1]);
			double maxX = Double.parseDouble(upperCoords[0]);
			double maxY = Double.parseDouble(upperCoords[1]);
			bbox.setMinX(minX);
			bbox.setMinY(minY);
			bbox.setMaxX(maxX);
			bbox.setMaxY(maxY);
			bbox.setCrs(crs);
			
			layerCap.getBboxList().add(bbox);
		}
	}
}
