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
import org.vast.ogc.gml.GMLUtils;
import org.vast.ows.OWSCapabilitiesWriterV0;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes a WCS server capabilities document from
 * OWSServiceCapabilities and WCSLayerCapabilities
 * objects for version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 27 nov. 07
 * */
public class WCSCapabilitiesWriterV10 extends OWSCapabilitiesWriterV0
{

	@Override
	public Element buildXMLResponse(DOMHelper dom, OWSServiceCapabilities caps, String version) throws OWSException
	{
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WCS));
		dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(GMLUtils.GML));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		Element capsElt = dom.createElement("WCS_Capabilities");
		writeRootAttributes(dom, capsElt, caps, version);
		writeService(dom, capsElt, caps);
		writeCapability(dom, capsElt, caps);
		writeContents(dom, capsElt, caps, version);
				
		return capsElt;
	}
	
	
	@Override
	protected void writeContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps, String version) throws OWSException
	{
		Element contentsElt = dom.addElement(capsElt, "ContentMetadata");
		
		for (int i=0; i<caps.getLayers().size(); i++)
		{
			WCSLayerCapabilities layerCaps = (WCSLayerCapabilities)caps.getLayers().get(i);
			Element summaryElt = dom.addElement(contentsElt, "+CoverageOfferingBrief");
			writeIdentification(dom, summaryElt, layerCaps);
			writeBboxList(dom, summaryElt, layerCaps);
			writeKeywords(dom, summaryElt, layerCaps);
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
			Element bboxElt = dom.addElement(layerElt, "lonLatEnvelope");
			dom.setElementValue(bboxElt, "gml:pos", bbox.getMinX() + " " + bbox.getMinY());
			dom.setElementValue(bboxElt, "+gml:pos", bbox.getMaxX() + " " + bbox.getMaxY());
			dom.setAttributeValue(bboxElt, "@srsName", "urn:ogc:def:crs:OGC:1.3:CRS84");
		}
	}
}
