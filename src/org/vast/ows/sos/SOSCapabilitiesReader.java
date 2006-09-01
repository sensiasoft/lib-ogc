/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.*;
import org.w3c.dom.*;
import org.vast.util.*;
import org.vast.io.xml.DOMReader;
import org.vast.ows.OWSCapabilitiesReader;
import org.vast.ows.OWSException;
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLTimeReader;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * SOS Capabilities Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads a SOS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * SOSLayerCapabilities objects
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Sep 14, 2005
 * @version 1.0
 */
public class SOSCapabilitiesReader extends OWSCapabilitiesReader
{
   
    public SOSCapabilitiesReader()
    {
    }
    
    
    public SOSCapabilitiesReader(DOMReader dom)
    {
        this.dom = dom;
    }
    
    
    @Override
    protected String buildQuery() throws OWSException
    {
        String url = null;
        
        // build query according to version group
        if (versionGroup == GROUP1)
        {
        	url = this.server + "service=SOS&version=" + version + "&request=GetCapabilities";
        }
        else
            throw new SOSException("SOS version " + version + " not supported");

        System.out.println("SOS Capabilities request: " + url);        
        return url;
    }


    @Override
    protected void setVersionGroup(String version)
    {
        if (version.equalsIgnoreCase("0.0.31"))
        {
            versionGroup = GROUP1;
        }
        else
            System.err.println("SOSCapabilities error: version number " + version + " not recognized");
    }
    
    
    protected void readServers()
    {
    	super.readServers();
    	
    	// put default POST server if none is specified in capabilities doc
		if (serviceCaps.getGetServers().isEmpty() && serviceCaps.getPostServers().isEmpty())
		{
			serviceCaps.getPostServers().put("GetObservation", this.server);
		}
    }

    
    @Override
    protected void readContents(Element contentElt) throws SOSException
    {
        NodeList offeringList = dom.getElements(contentElt, "ObservationOfferingList/ObservationOffering");
        
        for (int i=0; i<offeringList.getLength(); i++)
        {
            Element offeringElt = (Element)offeringList.item(i);
            String offeringName = dom.getElementValue(offeringElt, "name");
            
            if (offeringName == null)
            	offeringName = "Offering " + i + 1;
            
            String id = dom.getAttributeValue(offeringElt, "id");
            
            SOSLayerCapabilities layerCaps = new SOSLayerCapabilities();           
            
            try
            {
	            layerCaps.setName(offeringName);
	            layerCaps.setId(id);
	            getFormatList(offeringElt, layerCaps);
	            getObservableList(offeringElt, layerCaps);
	            getProcedureList(offeringElt, layerCaps);
	            getTimeList(offeringElt, layerCaps);
	            layerCaps.setParent(serviceCaps);
            }
            catch (GMLException e)
            {
            	String message = parsingError + " in offering " + layerCaps.getId();
                ExceptionSystem.display(new SOSException(message, e));
				continue;
            }
            
            serviceCaps.getLayers().add(layerCaps);
        }
    }
    
    
    /**
     * Create TimeInfo array
     * @param parentElement
     * @return
     */
    protected void getTimeList(Element parentElement, SOSLayerCapabilities layerCaps) throws GMLException
    {
    	NodeList timeElts = dom.getElements(parentElement, "eventTime/*");
        int listSize = timeElts.getLength();
        ArrayList<TimeInfo> timeList = new ArrayList<TimeInfo>(listSize);
        layerCaps.setTimeList(timeList);
    	GMLTimeReader timeReader = new GMLTimeReader();
        
        for(int i = 0; i < listSize; i++)
        {
            Element timeElt = (Element)timeElts.item(i);
            TimeInfo time = timeReader.readTimePrimitive(dom, timeElt);         
            timeList.add(time);
        }
    }
    
    
    /**
     * Creates format list array
     * @param parentElement
     * @return
     */
    protected void getFormatList(Element parentElement, SOSLayerCapabilities layerCaps) throws SOSException
    {
    	NodeList formatElts = dom.getElements(parentElement, "resultFormat");
        int listSize = formatElts.getLength();
        ArrayList<String> formatList = new ArrayList<String>(listSize);
        layerCaps.setFormatList(formatList);
        
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
    protected void getObservableList(Element parentElement, SOSLayerCapabilities layerCaps) throws SOSException
    {
        NodeList obsElts = dom.getElements(parentElement, "observedProperty");
        int listSize = obsElts.getLength();
        ArrayList<String> obsList = new ArrayList<String>(listSize);
        layerCaps.setObservableList(obsList);
        
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
    protected void getProcedureList(Element parentElement, SOSLayerCapabilities layerCaps) throws SOSException
    {
        NodeList procElts = dom.getElements(parentElement, "procedure");
        int listSize = procElts.getLength();
        ArrayList<String> procList = new ArrayList<String>(listSize);
        layerCaps.setProcedureList(procList);

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
