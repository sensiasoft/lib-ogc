/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.*;
import org.w3c.dom.*;
import org.vast.util.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;


/**
 * <p>
 * Reads a SOS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * SOSLayerCapabilities objects for version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Sep 14, 2005
 * */
public class SOSCapabilitiesReaderV10 extends OWSCapabilitiesReaderV11
{
   
    public SOSCapabilitiesReaderV10()
    {
    }
       
    
    @Override
    public SOSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
        SOSServiceCapabilities caps = new SOSServiceCapabilities();
        readOWSCapabilities(dom, capabilitiesElt, caps);
        return caps;
    }
    
    
    @Override
    protected void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws SOSException
    {
        NodeList offeringList = dom.getElements(capsElt, "Contents/ObservationOfferingList/ObservationOffering");
        
        for (int i=0; i<offeringList.getLength(); i++)
        {
            Element offeringElt = (Element)offeringList.item(i);
            String offeringName = dom.getElementValue(offeringElt, "name");
            
            if (offeringName == null)
            	offeringName = "Offering " + i + 1;
            
            String id = dom.getAttributeValue(offeringElt, "id");
            
            SOSOfferingCapabilities layerCaps = new SOSOfferingCapabilities();           
            
            try
            {
	            layerCaps.setTitle(offeringName);
	            layerCaps.setIdentifier(id);
	            getFormatList(dom, offeringElt, layerCaps);
	            getObservableList(dom, offeringElt, layerCaps);
	            getProcedureList(dom, offeringElt, layerCaps);
	            getTimeList(dom, offeringElt, layerCaps);
	            layerCaps.setParent(serviceCaps);
            }
            catch (XMLReaderException e)
            {
            	String message = xmlError + " in offering " + layerCaps.getIdentifier();
                ExceptionSystem.display(new SOSException(message, e));
				continue;
            }
            
            serviceCaps.getLayers().add(layerCaps);
        }
    }
    
    
    /**
     * Create TimeExtent array
     * @param parentElement
     * @return
     */
    protected void getTimeList(DOMHelper dom, Element parentElement, SOSOfferingCapabilities layerCaps) throws XMLReaderException, SOSException
    {
        GMLTimeReader timeReader = new GMLTimeReader();
        Element timeElt = dom.getElement(parentElement, "time/*");  // 1.0 schema is using plain old 'time' here 
        // test and remove eventTime check
        if(timeElt == null)
        	timeElt = dom.getElement(parentElement, "eventTime/*");

        if(timeElt == null)
            throw new SOSException("At least one offering time must be specified");
        
        // case of time aggregate
        // we only parse the first member
        if (dom.existElement(timeElt, "member"))
        {
            NodeList timeElts = dom.getElements(timeElt, "member/*");
            Element timeMemberElt = (Element)timeElts.item(0);
            TimeExtent time = timeReader.readTimePrimitive(dom, timeMemberElt);            
            layerCaps.setPhenomenonTime(time);
        }
        
        // case of single instant/period/grid
        else
        {
            TimeExtent time = timeReader.readTimePrimitive(dom, timeElt);
            layerCaps.setPhenomenonTime(time);
        }
    }
    
    
    /**
     * Creates format list array
     * @param parentElement
     * @return
     */
    protected void getFormatList(DOMHelper dom, Element parentElement, SOSOfferingCapabilities layerCaps) throws SOSException
    {
    	NodeList formatElts = dom.getElements(parentElement, "resultFormat");
    	if(formatElts.getLength()==0) {
    		formatElts = dom.getElements(parentElement, "responseFormat");
    	}
        int listSize = formatElts.getLength();

        ArrayList<String> formatList = new ArrayList<String>(listSize);
        layerCaps.setResponseFormats(formatList);
        
        for(int i = 0; i < listSize; i++)
        {
            Element formatElt = (Element)formatElts.item(i);
            String format = dom.getElementValue(formatElt, "");
            formatList.add(format);
        }
    }


    /**
     * Creates observable list array
     * @param parentElement
     * @return
     */
    protected void getObservableList(DOMHelper dom, Element parentElement, SOSOfferingCapabilities layerCaps) throws SOSException
    {
        NodeList obsElts = dom.getElements(parentElement, "observedProperty");
        int listSize = obsElts.getLength();
        ArrayList<String> obsList = new ArrayList<String>(listSize);
        layerCaps.setObservableProperties(obsList);
        
        for(int i = 0; i < listSize; i++)
        {
            Element propElt = (Element)obsElts.item(i);
            String xlink = dom.getAttributeValue(propElt, "href");
            String id = null;
            
            if (xlink != null)
            {
            	id = xlink;
            }
            else
            {
	            Element obsElement = dom.getFirstChildElement(propElt);
	            id = dom.getAttributeValue(obsElement, "id");            
            }
            
            obsList.add(id);
        }
    }
    
    
    /**
     * Creates procedure list array
     * @param parentElement
     * @return
     */
    protected void getProcedureList(DOMHelper dom, Element parentElement, SOSOfferingCapabilities layerCaps) throws SOSException
    {
        NodeList procElts = dom.getElements(parentElement, "procedure");
        int listSize = procElts.getLength();
        ArrayList<String> procList = new ArrayList<String>(listSize);
        layerCaps.setProcedures(procList);

        for(int i = 0; i < listSize; i++)
        {
            Element propElt = (Element)procElts.item(i);
            String xlink = dom.getAttributeValue(propElt, "href");
            String id = null;
            
            if (xlink != null)
            {
            	id = xlink;
            }
            else
            {
	            Element procElement = dom.getFirstChildElement(propElt);
	            id = dom.getAttributeValue(procElement, "id");            
            }
            
            procList.add(id);
        }
    }
}
