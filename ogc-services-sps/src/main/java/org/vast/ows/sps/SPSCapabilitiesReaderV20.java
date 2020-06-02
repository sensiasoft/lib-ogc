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
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import java.text.ParseException;
import java.util.Collection;
import net.opengis.gml.v32.AbstractGeometry;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.swe.SWESCapabilitiesReaderV20;


/**
 * <p>
 * Reads an SPS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * SPSOfferingCapabilities objects for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @since Dec, 12 2014
 */
public class SPSCapabilitiesReaderV20 extends SWESCapabilitiesReaderV20
{
    GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
    
    
	public SPSCapabilitiesReaderV20()
	{
	}	
	
	
	@Override
    public SPSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
	    SPSServiceCapabilities serviceCaps = new SPSServiceCapabilities();
	    readOWSCapabilities(dom, capabilitiesElt, serviceCaps);
	    return serviceCaps;
    }


    @Override
	protected void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws OWSException
	{
        SPSServiceCapabilities spsCaps = (SPSServiceCapabilities)serviceCaps;
        Element contentsElt = dom.getElement(capsElt, "contents/SPSContents");
        
        try
        {
            // common service settings (for all offerings)
            Collection<String> serviceProcFormats = readProcedureFormats(dom, contentsElt, null);
            Collection<String> serviceObsProperties = readObservableProperties(dom, contentsElt, null);
            Collection<String> serviceRelFeatures = readRelatedFeatures(dom, contentsElt, null);
            
            // list of SensorOffering elements
            NodeList offerings = dom.getElements(contentsElt, "offering/SensorOffering");
            int numOfferings = offerings.getLength();
            
            // Go through each element and populate offering caps
            for (int i = 0; i < numOfferings; i++)
            {
            	Element offeringElt = (Element) offerings.item(i);
            	SPSOfferingCapabilities offering = new SPSOfferingCapabilities();
            	offering.setParent(serviceCaps);
            	
            	super.readCommonOfferingProperties(dom, offeringElt, offering,
                        serviceProcFormats, serviceObsProperties, serviceRelFeatures);
            	
            	// observable area
            	Element geomElt = dom.getElement(offeringElt, "observableArea/*/*");
            	if (geomElt != null)
            	{
            	    AbstractGeometry geom = gmlUtils.readGeometry(dom, geomElt);
            	    offering.setObservableArea(geom);
            	}
            	
            	spsCaps.getLayers().add(offering);
            }
            
            // minStatusTime
            String val = dom.getElementValue(contentsElt, "minStatusTime");
            if (val != null)
                spsCaps.setMinStatusTime(timeFormat.parseIsoPeriod(val));
                
            // supported encodings
            NodeList encodings = dom.getElements(contentsElt, "supportedEncoding");
            for (int i = 0; i < encodings.getLength(); i++)
            {
                Element encodingElt = (Element) encodings.item(i);
                spsCaps.getSupportedEncodings().add(dom.getElementValue(encodingElt));
            }
        }
        catch (XMLReaderException | ParseException e)
        {
            throw new SPSException(READ_ERROR_MSG + spsCaps.getMessageType(), e);
        }
	}
	
}
