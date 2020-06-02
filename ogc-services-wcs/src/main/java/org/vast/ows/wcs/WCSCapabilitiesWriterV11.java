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
package org.vast.ows.wcs;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSCapabilitiesWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes a WCS server capabilities document from
 * OWSServiceCapabilities and WCSLayerCapabilities
 * objects for version 1.1.1
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 27 nov. 07
 * */
public class WCSCapabilitiesWriterV11 extends OWSCapabilitiesWriterV11
{

	@Override
	public Element buildXMLResponse(DOMHelper dom, OWSServiceCapabilities caps, String version) throws OWSException
	{
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WCS, version));
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
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
		
		for (int i=0; i<caps.getLayers().size(); i++)
		{
			WCSLayerCapabilities layerCaps = (WCSLayerCapabilities)caps.getLayers().get(i);
			Element summaryElt = dom.addElement(contentsElt, "+CoverageSummary");
			writeIdentification(dom, summaryElt, layerCaps);
			writeBboxList(dom, summaryElt, layerCaps);
			writeCRSList(dom, summaryElt, layerCaps);
			writeFormatList(dom, summaryElt, layerCaps);
			
			// mandatory identifier
			String id = layerCaps.getIdentifier();
			dom.setElementValue(summaryElt, "Identifier", id);
		}
	}
	
	
	/**
	 * Writes list of WGS84 BBOX for given layer
	 * @param bboxElement
	 * @param Bbox
	 */
	protected void writeBboxList(DOMHelper dom, Element layerElt, WCSLayerCapabilities layerCaps)
	{
		int numElts = layerCaps.getBboxList().size();
		for (int i = 0; i < numElts; i++)
		{
			Bbox bbox = layerCaps.getBboxList().get(i);
			Element bboxElt = dom.addElement(layerElt, "+ows:WGS84BoundingBox");
			dom.setElementValue(bboxElt, "ows:LowerCorner", bbox.getMinX() + " " + bbox.getMinY());
			dom.setElementValue(bboxElt, "ows:UpperCorner", bbox.getMaxX() + " " + bbox.getMaxY());
		}
	}
	
	
	/**
	 * Writes all supported CRS for given layer
	 * @param layerElt
	 * @return
	 */
	protected void writeCRSList(DOMHelper dom, Element layerElt, WCSLayerCapabilities layerCaps)
	{
		int numElts = layerCaps.getCrsList().size();
		for (int i = 0; i < numElts; i++)
		{
			String crs = layerCaps.getCrsList().get(i);
			dom.setElementValue(layerElt, "+SupportedCRS", crs);
		}
	}
	
	
	/**
	 * Writes all supported format for given layer
	 * @param layerElt
	 * @return
	 */
	protected void writeFormatList(DOMHelper dom, Element layerElt, WCSLayerCapabilities layerCaps)
	{
		int numElts = layerCaps.getFormatList().size();
		for (int i = 0; i < numElts; i++)
		{
			String format = layerCaps.getFormatList().get(i);
			dom.setElementValue(layerElt, "+SupportedFormat", format);
		}
	}
}
