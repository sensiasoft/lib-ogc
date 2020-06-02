/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import net.opengis.fes.v20.FilterCapabilities;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.ows.fes.FESUtils;
import org.vast.ows.swe.SWESCapabilitiesWriterV20;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * Writes an SOS server capabilities document from
 * OWSServiceCapabilities and SOSOfferingCapabilities
 * objects for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date 27 nov. 07
 * */
public class SOSCapabilitiesWriterV20 extends SWESCapabilitiesWriterV20
{
    GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
    FESUtils fesUtils = new FESUtils(FESUtils.V2_0);
    
    
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
		writeInsertionCapabilities(dom, capsElt, (SOSServiceCapabilities)caps);
		writeFilterCapabilities(dom, capsElt, (SOSServiceCapabilities)caps);
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
	
	
	protected void writeFilterCapabilities(DOMHelper dom, Element capsElt, SOSServiceCapabilities serviceCaps) throws OWSException
    {
        try
        {
            FilterCapabilities filterCaps = serviceCaps.getFilterCapabilities();
            if (filterCaps != null)
            {
                Element filterCapsElt = fesUtils.writeFilterCapabilities(dom, filterCaps);
                Element capsPropElt = dom.addElement(capsElt, "sos:filterCapabilities");
                capsPropElt.appendChild(filterCapsElt);
            }
        }
        catch (Exception e)
        {
            throw new SOSException("Internal error while writing filter capabilities", e);
        }
    }
	
	
	@Override
	protected void writeContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps, String version) throws OWSException
	{
		Element contentsElt = dom.addElement(capsElt, "sos:contents/sos:Contents");
		
	    // SWES properties common to all offerings
		super.writeCommonContentsProperties(dom, contentsElt, serviceCaps);
	    
	    // SOS offerings
	    for (SOSOfferingCapabilities offeringCaps: ((SOSServiceCapabilities)serviceCaps).getLayers())
	    {
	        // skip disabled offerings
            if (!offeringCaps.isEnabled())
                continue;
            
            try
            {
                Element offeringElt = dom.addElement(contentsElt, "+swes:offering/sos:ObservationOffering");
                
                // SWES offering properties
                super.writeCommonOfferingProperties(dom, offeringElt, serviceCaps, offeringCaps);
                
                // observed area
                if (offeringCaps.getObservedAreas() != null && !offeringCaps.getObservedAreas().isEmpty())
                {
                    Bbox bbox = offeringCaps.getObservedAreas().get(0);
                    Element envElt = gmlUtils.writeBboxAsEnvelope(dom, bbox);
                    dom.addElement(offeringElt, "sos:observedArea").appendChild(envElt);
                }
                
                // phenomenon time
                TimeExtent timePeriod = offeringCaps.getPhenomenonTime();
                if (timePeriod != null)
                {
                    Element envElt = gmlUtils.writeTimeExtentAsTimePeriod(dom, timePeriod);
                    dom.addElement(offeringElt, "sos:phenomenonTime").appendChild(envElt);
                }
                
                // result time
                timePeriod = offeringCaps.getResultTime();
                if (timePeriod != null)
                {
                    Element envElt = gmlUtils.writeTimeExtentAsTimePeriod(dom, timePeriod);
                    dom.addElement(offeringElt, "sos:resultTime").appendChild(envElt);
                }
                
                // response formats (inheritance = replace)
                if (!offeringCaps.getResponseFormats().equals(getCommonListEntries(serviceCaps, "getResponseFormats")))
                {
                    for (String token: offeringCaps.getResponseFormats())
                        dom.setElementValue(offeringElt, "+sos:responseFormat", token);
                }
                
                // obs types (inheritance = replace)
                if (!offeringCaps.getObservationTypes().equals(getCommonListEntries(serviceCaps, "getObservationTypes")))
                {
                    for (String token: offeringCaps.getObservationTypes())
                        dom.setElementValue(offeringElt, "+sos:observationType", token);
                }
                
                // foi types (inheritance = replace)
                if (!offeringCaps.getFoiTypes().equals(getCommonListEntries(serviceCaps, "getFoiTypes")))
                {
                    for (String token: offeringCaps.getFoiTypes())
                        dom.setElementValue(offeringElt, "+sos:featureOfInterestType", token);
                }
            }
            catch (XMLWriterException e)
            {
                throw new SOSException("Error writing offering " + offeringCaps.getIdentifier(), e);
            }
	    }
	    
	    // SOS properties common to all offerings
	    for (String token: getCommonListEntries(serviceCaps, "getResponseFormats"))
            dom.setElementValue(contentsElt, "+sos:responseFormat", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getObservationTypes"))
            dom.setElementValue(contentsElt, "+sos:observationType", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getFoiTypes"))
            dom.setElementValue(contentsElt, "+sos:featureOfInterestType", token);
	}

}
