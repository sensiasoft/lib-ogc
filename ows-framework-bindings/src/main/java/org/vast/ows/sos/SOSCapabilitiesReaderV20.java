/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.List;
import net.opengis.fes.v20.FilterCapabilities;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.vast.ogc.gml.GMLEnvelopeReader;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.fes.FESUtils;
import org.vast.ows.swe.SWESCapabilitiesReaderV20;


/**
 * <p>
 * Reads an SOS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * SOSOfferingCapabilities objects for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2013</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Sep 15, 2013
 */
public class SOSCapabilitiesReaderV20 extends SWESCapabilitiesReaderV20
{
    GMLEnvelopeReader gmlEnvReader = new GMLEnvelopeReader();
    GMLTimeReader gmlTimeReader = new GMLTimeReader();
    FESUtils fesUtils = new FESUtils();
    
    
	public SOSCapabilitiesReaderV20()
	{
	}
	
	
	@Override
    public SOSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
	    try
        {
            SOSServiceCapabilities caps = new SOSServiceCapabilities();
            
            // read common OWS stuff
            readOWSCapabilities(dom, capabilitiesElt, caps);
            
            // TODO read specific SOS stuff
            
            // insertion capabilities
            Element insertionCapsElt = dom.getElement(capabilitiesElt, "extension/InsertionCapabilities");
            if (insertionCapsElt != null)
            {
                SOSInsertionCapabilities insertionCaps = new SOSInsertionCapabilities();
                insertionCaps.setProcedureFormats(readProcedureFormats(dom, insertionCapsElt));
                insertionCaps.setObservationTypes(readObservationTypes(dom, insertionCapsElt));
                insertionCaps.setFoiTypes(readFoiTypes(dom, insertionCapsElt));
                insertionCaps.setSupportedEncodings(readSupportedEncodings(dom, insertionCapsElt));
                caps.setInsertionCapabilities(insertionCaps);
            }
            
            // read filter capabilities
            Element filterCapsElt = dom.getElement(capabilitiesElt, "filterCapabilities/Filter_Capabilities");
            if (filterCapsElt != null)
            {
                FilterCapabilities filterCaps = fesUtils.readFilterCapabilities(filterCapsElt);
                caps.setFilterCapabilities(filterCaps);
            }
            
            return caps;
        }
        catch (Exception e)
        {
            throw new OWSException(xmlError, e);
        }
    }
	
	
	@Override
	protected void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws OWSException
	{
		try
        {
            Element contentsElt = dom.getElement(capsElt, "contents/Contents");
            
            // common service settings (for all offerings)
            List<String> serviceProcFormats = readProcedureFormats(dom, contentsElt);
            List<String> serviceObsProperties = readObservableProperties(dom, contentsElt);
            List<String> serviceRelFeatures = readRelatedFeatures(dom, contentsElt);
            List<String> serviceRespFormats = readResponseFormats(dom, contentsElt);
            List<String> serviceObsTypes = readObservationTypes(dom, contentsElt);
            List<String> serviceFoiTypes = readFoiTypes(dom, contentsElt);	
            
            //  Load List of SensorOffering elements
            NodeList offerings = dom.getElements(contentsElt, "offering/ObservationOffering");
            int numOfferings = offerings.getLength();

            // Go through each element and populate offering caps
            for (int i = 0; i < numOfferings; i++)
            {
            	Element offeringElt = (Element) offerings.item(i);
            	SOSOfferingCapabilities offering = new SOSOfferingCapabilities();
            	offering.setParent(serviceCaps);
            	
            	super.readCommonOfferingProperties(dom, offeringElt, offering,
            	            serviceProcFormats, serviceObsProperties, serviceRelFeatures);
            	
            	// observed area
            	Element envElt = dom.getElement(offeringElt, "observedArea/Envelope");
            	if (envElt != null)
            	    offering.getObservedAreas().add(gmlEnvReader.readEnvelope(dom, envElt));
            	
            	// phenomenon time
            	Element phenTimeElt = dom.getElement(offeringElt, "phenomenonTime/TimePeriod");
            	if (phenTimeElt != null)
            	    offering.setPhenomenonTime(gmlTimeReader.readTimePeriod(dom, phenTimeElt));
            	
            	// result time
            	Element resultTimeElt = dom.getElement(offeringElt, "resultTime/TimePeriod");
            	if (resultTimeElt != null)
                    offering.setResultTime(gmlTimeReader.readTimePeriod(dom, resultTimeElt));
                
            	// for the following items, we combine service level and offering level metadata			
            	// response formats
                offering.getResponseFormats().addAll(serviceRespFormats);
                offering.getResponseFormats().addAll(readResponseFormats(dom, offeringElt));
                
                // observation types
                offering.getObservationTypes().addAll(serviceObsTypes);
                offering.getObservationTypes().addAll(readObservationTypes(dom, offeringElt));
                
                // feature of interest types
                offering.getFoiTypes().addAll(serviceFoiTypes);
                offering.getFoiTypes().addAll(readFoiTypes(dom, offeringElt));
            	
            	serviceCaps.getLayers().add(offering);
            }
        }
        catch (XMLReaderException e)
        {
            throw new SOSException(e.getMessage());
        }
	}
    
    
    /*
     * Reads content from a list of responseFormat elements
     */
    protected List<String> readResponseFormats(DOMHelper dom, Element parentElt)
    {
        return readStringList(dom, parentElt, "responseFormat");
    }
    
    
    /*
     * Reads content from a list of observationType elements
     */
    protected List<String> readObservationTypes(DOMHelper dom, Element parentElt)
    {
        return readStringList(dom, parentElt, "observationType");
    }
    
    
    /*
     * Reads content from a list of featureOfInterestType elements
     */
    protected List<String> readFoiTypes(DOMHelper dom, Element parentElt)
    {
        return readStringList(dom, parentElt, "featureOfInterestType");
    }
    
    
    /*
     * Reads content from a list of supportedEncoding elements
     */
    protected List<String> readSupportedEncodings(DOMHelper dom, Element parentElt)
    {
        return readStringList(dom, parentElt, "supportedEncoding");
    }	
}
