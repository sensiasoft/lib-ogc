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
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.util.Bbox;


/**
 * <p><b>Title:</b><br/>
 * WCS Capabilities Reader V11
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads a WCS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WCSLayerCapabilities objects for version 1.1.0 and 1.1.1
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 27 nov. 07
 * @version 1.0
 */
public class WCSCapabilitiesReaderV11 extends OWSCapabilitiesReaderV11
{

	public WCSCapabilitiesReaderV11()
	{
	}
	
	
	@Override
    protected String buildQuery() throws OWSException
    {
        String query = null;
        query = this.server + "SERVICE=WCS&VERSION=1.1.1&REQUEST=GetCapabilities"; 
        return query;
    }
	
	
	@Override
	protected void readServers(DOMHelper dom, Element capabilitiesElt)
	{
		Node map = dom.getAllElements(capabilitiesElt, "http:operation").item(0);
		String url = ((Element) map).getAttribute("location");

		// GET server
		if (url != null)
			serviceCaps.getGetServers().put("GetCoverage", url);
	}


	@Override
	protected void readContents(DOMHelper dom, Element capsElt)
	{
		//  Load List of GridCoverageLayer elements
		NodeList layers = dom.getElements(capsElt, "Contents/CoverageSummary");
		int numLayers = layers.getLength();

		// Go through each CoverageSummary and populate layerCaps
		for (int i = 0; i < numLayers; i++)
		{
			Element layerCapElt = (Element) layers.item(i);
			WCSLayerCapabilities layerCap = new WCSLayerCapabilities();
			layerCap.setParent(serviceCaps);

			String title = dom.getElementValue(layerCapElt, "Title");
			layerCap.setTitle(title);
			
			String desc = dom.getElementValue(layerCapElt, "Abstract");
			layerCap.setDescription(desc);

			readKeywordList(layerCap, dom, layerCapElt);			
			read2DBboxList(layerCap, dom, layerCapElt);
			readCRSList(layerCap, dom, layerCapElt);
			readFormatList(layerCap, dom, layerCapElt);
			
			String id = dom.getElementValue(layerCapElt, "Identifier");
			layerCap.setIdentifier(id);

			serviceCaps.getLayers().add(layerCap);
		}
	}
	

	/**
	 * Reads list of WGS84 BBOX for given layer
	 * @param bboxElement
	 * @param Bbox
	 */
	protected void read2DBboxList(WCSLayerCapabilities layerCap, DOMHelper dom, Element layerElt)
	{
		NodeList bboxElts = dom.getElements(layerElt, "WGS84BoundingBox");
		int numElts = bboxElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element bboxElt = (Element) bboxElts.item(i);
			String[] lowerCoords = dom.getElementValue(bboxElt, "LowerCorner").split(" ");
			String[] upperCoords = dom.getElementValue(bboxElt, "UpperCorner").split(" ");
			String crs = dom.getAttributeValue(bboxElt, "@crs");
			
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
	
	
	/**
	 * Reads all keywords for given layer
	 * @param layerElt
	 * @return
	 */
	protected void readKeywordList(WCSLayerCapabilities layerCap, DOMHelper dom, Element layerElt)
	{
		NodeList keywordElts = dom.getElements(layerElt, "Keywords/Keyword");
		int numElts = keywordElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element keywordElt = (Element) keywordElts.item(i);
			String keyword = dom.getElementValue(keywordElt);
			layerCap.getKeywords().add(keyword);
		}
	}


	/**
	 * Reads all supported format for given layer
	 * @param layerElt
	 * @return
	 */
	protected void readFormatList(WCSLayerCapabilities layerCap, DOMHelper dom, Element layerElt)
	{
		NodeList formatElts = dom.getElements(layerElt, "SupportedFormat");
		int numElts = formatElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element formatElt = (Element) formatElts.item(i);
			String formatName = dom.getElementValue(formatElt);
			layerCap.getFormatList().add(formatName);
		}
	}


	/**
	 * Reads all supported CRS for given layer
	 * @param layerElt
	 * @return
	 */
	protected void readCRSList(WCSLayerCapabilities layerCap, DOMHelper dom, Element layerElt)
	{
		NodeList crsElts = dom.getElements(layerElt, "SupportedCRS");
		int numElts = crsElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element crsElt = (Element) crsElts.item(i);
			String crsName = dom.getElementValue(crsElt);
			layerCap.getCrsList().add(crsName);
		}
	}
}
