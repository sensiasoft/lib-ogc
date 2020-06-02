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

import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ows.OWSException;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWESCapabilitiesWriterV20;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Writes a SPS server capabilities document from
 * OWSServiceCapabilities and SPSLayerCapabilities
 * objects for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date 27 nov. 07
 * */
public class SPSCapabilitiesWriterV20 extends SWESCapabilitiesWriterV20
{
    GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
    
    
	@Override
	public Element buildXMLResponse(DOMHelper dom, OWSServiceCapabilities caps, String version) throws OWSException
	{
	    dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(OWSUtils.SPS, version));
        dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));
        dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		Element capsElt = dom.createElement("sps:Capabilities");
		writeRootAttributes(dom, capsElt, caps, version);
		writeServiceIdentification(dom, capsElt, caps);		
		writeServiceProvider(dom, capsElt, caps.getServiceProvider());
		writeOperationsMetadata(dom, capsElt, caps);
		writeContents(dom, capsElt, caps, version);
				
		return capsElt;
	}
	
	
	@Override
	protected void writeContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps, String version) throws OWSException
	{
	    try
        {
            Element contentsElt = dom.addElement(capsElt, "sps:contents/sps:SPSContents");
            SPSServiceCapabilities spsCaps = (SPSServiceCapabilities)serviceCaps;
            
            // SWES properties common to all offerings
            super.writeCommonContentsProperties(dom, contentsElt, serviceCaps);

            // SPS offerings
            for (OWSLayerCapabilities layerCaps: serviceCaps.getLayers())
            {
                // skip disabled offerings
                if (!layerCaps.isEnabled())
                    continue;
                
                Element offeringElt = dom.addElement(contentsElt, "+swes:offering/sps:SensorOffering");
                SPSOfferingCapabilities offering = (SPSOfferingCapabilities)layerCaps;
                
                // SWES offering properties
                super.writeCommonOfferingProperties(dom, offeringElt, serviceCaps, offering);
                
                // observable area
                AbstractGeometry obsArea = offering.getObservableArea();
                if (obsArea != null)
                {
                    Element geomElt = gmlUtils.writeGeometry(dom, obsArea);
                    String path = "sps:observableArea/";
                    if (obsArea instanceof Polygon)
                        path += "sps:byPolygon";
                    else if (obsArea instanceof Point)
                        path += "sps:byPoint";
                    else
                        throw new SPSException("Invalid geometry for observedArea: " + obsArea.getClass().getSimpleName());
                    dom.addElement(offeringElt, path).appendChild(geomElt);
                }
            }
            
            // min status time
            double minStatusTime = spsCaps.getMinStatusTime();
            if (!Double.isNaN(minStatusTime)) 
                dom.setElementValue(contentsElt, "sps:minStatusTime", timeFormat.formatIsoPeriod(minStatusTime, 'D'));
            
            // supported encodings
            for (String encoding: spsCaps.getSupportedEncodings())
                dom.setElementValue(contentsElt, "+sps:supportedEncoding", encoding);
        }
        catch (XMLWriterException e)
        {
            throw new SPSException("Error while writing SPS capabilities", e);
        }
	}
}
