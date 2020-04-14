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

import java.util.Collection;
import net.opengis.fes.v20.FilterCapabilities;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.vast.ogc.gml.GMLUtils;
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
 * @author Alex Robin
 * @since Sep 15, 2013
 */
public class SOSCapabilitiesReaderV20 extends SWESCapabilitiesReaderV20
{
    GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
    FESUtils fesUtils = new FESUtils(FESUtils.V2_0);
    
    
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
                readProcedureFormats(dom, insertionCapsElt, insertionCaps.getProcedureFormats());
                readObservationTypes(dom, insertionCapsElt, insertionCaps.getObservationTypes());
                readFoiTypes(dom, insertionCapsElt, insertionCaps.getFoiTypes());
                readSupportedEncodings(dom, insertionCapsElt, insertionCaps.getSupportedEncodings());
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
            Collection<String> serviceProcFormats = readProcedureFormats(dom, contentsElt, null);
            Collection<String> serviceObsProperties = readObservableProperties(dom, contentsElt, null);
            Collection<String> serviceRelFeatures = readRelatedFeatures(dom, contentsElt, null);
            Collection<String> serviceRespFormats = readResponseFormats(dom, contentsElt, null);
            Collection<String> serviceObsTypes = readObservationTypes(dom, contentsElt, null);
            Collection<String> serviceFoiTypes = readFoiTypes(dom, contentsElt, null);	
            
            //  moad List of SensorOffering elements
            NodeList offerings = dom.getElements(contentsElt, "offering/ObservationOffering");
            int numOfferings = offerings.getLength();

            // go through each element and populate offering caps
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
            	    offering.getObservedAreas().add(gmlUtils.readEnvelopeAsBbox(dom, envElt));
            	
            	// phenomenon time
            	Element phenTimeElt = dom.getElement(offeringElt, "phenomenonTime/TimePeriod");
            	if (phenTimeElt != null)
            	    offering.setPhenomenonTime(gmlUtils.readTimePrimitiveAsTimeExtent(dom, phenTimeElt));
            	
            	// result time
            	Element resultTimeElt = dom.getElement(offeringElt, "resultTime/TimePeriod");
            	if (resultTimeElt != null)
                    offering.setResultTime(gmlUtils.readTimePrimitiveAsTimeExtent(dom, resultTimeElt));
                
            	// for the following items, we inherit from service level			
            	// response formats (inheritance = replace)
                readResponseFormats(dom, offeringElt, offering.getResponseFormats());
                if (offering.getResponseFormats().isEmpty())
                    offering.getResponseFormats().addAll(serviceRespFormats);
                
                // observation types (inheritance = replace)
                readObservationTypes(dom, offeringElt, offering.getObservationTypes());
                if (offering.getObservationTypes().isEmpty())
                    offering.getObservationTypes().addAll(serviceObsTypes);
                
                // feature of interest types (inheritance = replace)
                readFoiTypes(dom, offeringElt, offering.getFoiTypes());
                if (offering.getFoiTypes().isEmpty())
                    offering.getFoiTypes().addAll(serviceFoiTypes);
                
            	((SOSServiceCapabilities)serviceCaps).getLayers().add(offering);
            }
        }
        catch (XMLReaderException e)
        {
            throw new SOSException("Error in capabilities document", e);
        }
	}
    
    
    /*
     * Reads content from a list of responseFormat elements
     */
    protected Collection<String> readResponseFormats(DOMHelper dom, Element parentElt, Collection<String> respFormats)
    {
        return readStringList(dom, parentElt, "responseFormat", respFormats);
    }
    
    
    /*
     * Reads content from a list of observationType elements
     */
    protected Collection<String> readObservationTypes(DOMHelper dom, Element parentElt, Collection<String> obsTypes)
    {
        return readStringList(dom, parentElt, "observationType", obsTypes);
    }
    
    
    /*
     * Reads content from a list of featureOfInterestType elements
     */
    protected Collection<String> readFoiTypes(DOMHelper dom, Element parentElt, Collection<String> foiTypes)
    {
        return readStringList(dom, parentElt, "featureOfInterestType", foiTypes);
    }
    
    
    /*
     * Reads content from a list of supportedEncoding elements
     */
    protected Collection<String> readSupportedEncodings(DOMHelper dom, Element parentElt, Collection<String> encodings)
    {
        return readStringList(dom, parentElt, "supportedEncoding", encodings);
    }	
}
