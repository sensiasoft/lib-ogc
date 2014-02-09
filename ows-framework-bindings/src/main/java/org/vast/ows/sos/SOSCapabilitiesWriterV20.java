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

import java.util.ArrayList;
import java.util.List;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLEnvelopeWriter;
import org.vast.ogc.gml.GMLTimeWriter;
import org.vast.ows.OWSCapabilitiesWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SPS Capabilities Writer V2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes a SPS server capabilities document from
 * OWSServiceCapabilities and SPSLayerCapabilities
 * objects for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2009</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 27 nov. 07
 * @version 1.0
 */
public class SOSCapabilitiesWriterV20 extends OWSCapabilitiesWriterV11
{
    GMLEnvelopeWriter bboxWriter = new GMLEnvelopeWriter("3.2");
    GMLTimeWriter timeWriter = new GMLTimeWriter("3.2");
    
    
	@Override
	public Element buildXMLResponse(DOMHelper dom, OWSServiceCapabilities caps, String version) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, version));
		dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		Element capsElt = dom.createElement("sos:Capabilities");
		writeRootAttributes(dom, capsElt, caps, version);
		writeServiceIdentification(dom, capsElt, caps);
		writeServiceProvider(dom, capsElt, caps.getServiceProvider());
		writeOperationsMetadata(dom, capsElt, caps);
		// TODO write filter capabilities
		writeInsertionCapabilities(dom, capsElt, (SOSServiceCapabilities)caps);
		writeContents(dom, capsElt, caps, version);
				
		return capsElt;
	}
	
	
	protected void writeInsertionCapabilities(DOMHelper dom, Element capsElt, SOSServiceCapabilities serviceCaps) throws OWSException
    {
	    SOSInsertionCapabilities insertionCaps = serviceCaps.getInsertionCapabilities();
	    if (insertionCaps != null)
	    {
	        Element insertionCapsElt = dom.addElement(capsElt, "sos:extension/sos:InsertionCapabilities");
	        
	        // procedure formats
	        for (String token: insertionCaps.getProcedureFormats())
	            dom.setElementValue(insertionCapsElt, "+sos:procedureDescriptionFormat", token);
	        
	        // foi types
            for (String token: insertionCaps.getFoiTypes())
                dom.setElementValue(insertionCapsElt, "+sos:featureOfInterestType", token);
	        
	        // obs types
            for (String token: insertionCaps.getObservationTypes())
                dom.setElementValue(insertionCapsElt, "+sos:observationType", token);
            
            // supported result encodings
            for (String token: insertionCaps.getSupportedEncodings())
                dom.setElementValue(insertionCapsElt, "+sos:supportedEncoding", token);
	    }
    }
	
	
	
	@Override
	protected void writeContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps, String version) throws OWSException
	{
		Element contentsElt = dom.addElement(capsElt, "sos:contents/sos:Contents");
		
	    // SWES properties common to all offerings
		for (String token: getCommonListEntries(serviceCaps, "getProcedureFormats"))
            dom.setElementValue(contentsElt, "+swes:procedureDescriptionFormat", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getObservableProperties"))
            dom.setElementValue(contentsElt, "+swes:observableProperty", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getRelatedFeatures"))
            addRelatedFeature(dom, contentsElt, token);
	    
	    // SOS offerings
	    for (OWSLayerCapabilities layerCaps: serviceCaps.getLayers())
	    {
	        Element offeringElt = dom.addElement(contentsElt, "+swes:offering/sos:ObservationOffering");
	        SOSOfferingCapabilities offeringCaps = (SOSOfferingCapabilities)layerCaps;
	        
	        // description
	        String desc = offeringCaps.getDescription();
            if (desc != null)
                dom.setElementValue(offeringElt, "swes:description", desc);
	        
	        // identifier
	        dom.setElementValue(offeringElt, "swes:identifier", offeringCaps.getIdentifier());
            
            // name
	        String title = offeringCaps.getTitle();
	        if (title != null)
	            dom.setElementValue(offeringElt, "+swes:name", title);
            
	        // procedure
	        dom.setElementValue(offeringElt, "swes:procedure", offeringCaps.getProcedures().get(0));
	        
	        // procedure description formats
	        for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getProcedureFormats"))
                dom.setElementValue(offeringElt, "+swes:procedureDescriptionFormat", token);
	        
	        // observable properties
	        for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getObservableProperties"))
                dom.setElementValue(offeringElt, "+swes:observableProperty", token);
	        
	        // related features
	        for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getRelatedFeatures"))
	            addRelatedFeature(dom, offeringElt, token);
	        
	        // observed area
	        if (offeringCaps.getObservedAreas() != null && !offeringCaps.getObservedAreas().isEmpty())
	        {
	            Bbox bbox = offeringCaps.getObservedAreas().get(0);
	            Element envElt = bboxWriter.writeEnvelope(dom, bbox);
	            dom.addElement(offeringElt, "sos:observedArea").appendChild(envElt);
	        }
	        
	        // phenomenon time
	        if (offeringCaps.getPhenomenonTimes() != null && !offeringCaps.getPhenomenonTimes().isEmpty())
            {
                TimeExtent timePeriod = offeringCaps.getPhenomenonTimes().get(0);
                Element envElt = timeWriter.writeTime(dom, timePeriod);
                dom.addElement(offeringElt, "sos:phenomenonTime").appendChild(envElt);
            }
	        
	        // result time
            if (offeringCaps.getResultTimes() != null && !offeringCaps.getResultTimes().isEmpty())
            {
                TimeExtent timePeriod = offeringCaps.getResultTimes().get(0);
                Element envElt = timeWriter.writeTime(dom, timePeriod);
                dom.addElement(offeringElt, "sos:resultTime").appendChild(envElt);
            }
            
            // response formats
            for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getResponseFormats"))
                dom.setElementValue(offeringElt, "+sos:responseFormat", token);
            
            // obs types
            for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getObservationTypes"))
                dom.setElementValue(offeringElt, "+sos:observationType", token);
            
            // foi types
            for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getFoiTypes"))
                dom.setElementValue(offeringElt, "+sos:featureOfInterestType", token);
	    }
	    
	    // SOS properties common to all offerings
	    for (String token: getCommonListEntries(serviceCaps, "getResponseFormats"))
            dom.setElementValue(contentsElt, "+sos:responseFormat", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getObservationTypes"))
            dom.setElementValue(contentsElt, "+sos:observationType", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getFoiTypes"))
            dom.setElementValue(contentsElt, "+sos:featureOfInterestType", token);
	}
	
	
	protected void addRelatedFeature(DOMHelper dom, Element parentElt, String featureURI)
	{
	    dom.setAttributeValue(parentElt, "+swes:relatedFeature/swes:FeatureRelationship/swes:target/@xlink:href", featureURI);
	}
	
	
	/*
	 * Extracts entries that are common to all offerings in order to build the top level inherited section
	 */
	protected List<String> getCommonListEntries(OWSServiceCapabilities caps, String getListMethodName)
	{
	    ArrayList<String> intersectList = new ArrayList<String>();
	    boolean first = true;
	    
	    for (OWSLayerCapabilities layerCaps: caps.getLayers())
	    {
	        try
            {
                List<String> tokenListFromOffering = (List<String>)layerCaps.getClass().getMethod(getListMethodName).invoke(layerCaps);
                if (first)
                {
                    intersectList.addAll(tokenListFromOffering);
                    first = false;
                }
                else
                    intersectList.retainAll(tokenListFromOffering);                
            }
            catch (Exception e)
            {                
            }
	    }
	    
	    return intersectList;
	}
	
	
	/*
     * Extracts entries that are not common to all offerings
     */
    protected List<String> getNonCommonListEntries(OWSLayerCapabilities layerCaps, OWSServiceCapabilities serviceCaps, String getListMethodName)
    {
        ArrayList<String> nonIntersectList = new ArrayList<String>();        
        List<String> intersectList = getCommonListEntries(serviceCaps, getListMethodName);
        
        try
        {
            List<String> tokenListFromOffering = (List<String>)layerCaps.getClass().getMethod(getListMethodName).invoke(layerCaps);
            nonIntersectList.addAll(tokenListFromOffering);
            nonIntersectList.removeAll(intersectList);            
        }
        catch (Exception e)
        {                
        }
        
        return nonIntersectList;
    }
}
