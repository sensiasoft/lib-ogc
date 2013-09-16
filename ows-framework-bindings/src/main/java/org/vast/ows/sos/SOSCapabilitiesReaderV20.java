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

package org.vast.ows.sos;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.vast.ogc.gml.GMLEnvelopeReader;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;


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
public class SOSCapabilitiesReaderV20 extends OWSCapabilitiesReaderV11
{
    GMLEnvelopeReader gmlEnvReader = new GMLEnvelopeReader();
    GMLTimeReader gmlTimeReader = new GMLTimeReader();
    
    
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
            // TODO read insertion capabilities
            // TODO read filter capabilities
            
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
            	
            	// identifier
                String id = dom.getElementValue(offeringElt, "identifier");
                offering.setIdentifier(id);
            	
            	// name
            	String name = dom.getElementValue(offeringElt, "name");
            	offering.setTitle(name);
            	
            	// description
            	String description = dom.getElementValue(offeringElt, "description");
            	offering.setDescription(description);
            	
            	// procedure
            	String procedureID = dom.getElementValue(offeringElt, "procedure");
            	offering.getProcedures().add(procedureID);
            	
            	// observed area
            	Element envElt = dom.getElement(offeringElt, "observedArea/Envelope");
            	offering.getObservedAreas().add(gmlEnvReader.readEnvelope(dom, envElt));
            	
            	// phenomenon time
            	Element phenTimeElt = dom.getElement(offeringElt, "phenomenonTime/TimePeriod");
            	if (phenTimeElt != null)
            	    offering.getPhenomenonTimes().add(gmlTimeReader.readTimePeriod(dom, phenTimeElt));
            	
            	// result time
            	Element resultTimeElt = dom.getElement(offeringElt, "resultTime/TimePeriod");
            	if (resultTimeElt != null)
                    offering.getResultTimes().add(gmlTimeReader.readTimePeriod(dom, resultTimeElt));
                
            	// for the following items, we combine service level and offering level metadata			
            	// procedure description formats
            	offering.getProcedureFormats().addAll(serviceProcFormats);
            	offering.getProcedureFormats().addAll(readProcedureFormats(dom, offeringElt));
            	
            	// observable properties
            	offering.getObservableProperties().addAll(serviceObsProperties);
            	offering.getObservableProperties().addAll(readObservableProperties(dom, offeringElt));
            	
            	// related features
                offering.getRelatedFeatures().addAll(serviceRelFeatures);
                offering.getRelatedFeatures().addAll(readRelatedFeatures(dom, offeringElt));
                
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
	 * Reads content from a list of procedureDescriptionFormat elements
	 */
	protected List<String> readProcedureFormats(DOMHelper dom, Element parentElt)
	{
	    return readStringList(dom, parentElt, "procedureDescriptionFormat");
	}
	
	
	/*
     * Reads content from a list of observableProperty elements
     */
    protected List<String> readObservableProperties(DOMHelper dom, Element parentElt)
    {
        return readStringList(dom, parentElt, "observableProperty");
    }
    
    
    /*
     * Reads content from a list of relatedFeature elements
     */
    protected List<String> readRelatedFeatures(DOMHelper dom, Element parentElt)
    {
        List<String> featureList = new ArrayList<String>();
        
        NodeList featRelationshipElts = dom.getElements(parentElt, "relatedFeature/FeatureRelationship/target");
        for (int i=0; i<featRelationshipElts.getLength(); i++)
        {
            String href = dom.getAttributeValue((Element)featRelationshipElts.item(i), "@href");
            featureList.add(href);
        }
        
        return featureList;
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
     * Reads string values from a list of identical elements
     */
    protected List<String> readStringList(DOMHelper dom, Element parentElt, String eltPath)
    {
        List<String> tokenList = new ArrayList<String>();
        NodeList elts = dom.getElements(parentElt, eltPath);
        for (int i=0; i<elts.getLength(); i++)
        {
            String token = dom.getElementValue((Element)elts.item(i));
            tokenList.add(token);
        }
        
        return tokenList;
    }	
}
