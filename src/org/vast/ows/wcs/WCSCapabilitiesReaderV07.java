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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.text.ParseException;
import java.util.*;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ows.AbstractCapabilitiesReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;


/**
 * <p><b>Title:</b><br/>
 * WCS Capabilities Reader v0.7
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads a WCS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WCSLayerCapabilities objects for version 0.7
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin, Tony Cook
 * @date Nov 17, 2005
 * @version 1.0
 * 
 * @TODO  add support for WCS 1.*
 * @TODO  clarify/add support for realTime
 */
public class WCSCapabilitiesReaderV07 extends AbstractCapabilitiesReader
{

    public WCSCapabilitiesReaderV07()
    {
    }
    
    
    @Override
    public OWSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
    	serviceCaps = new OWSServiceCapabilities();
    	
    	// Version
        String version = dom.getAttributeValue(capabilitiesElt, "version");
        serviceCaps.setVersion(version);
        
        // Read Service Identification Section
        Element serviceElt = dom.getElement(capabilitiesElt, "Service");
        String serviceTitle = dom.getElementValue(serviceElt, "Title");
        serviceCaps.getIdentification().setTitle(serviceTitle);        
        String serviceType = dom.getElementValue(serviceElt, "Name");
        serviceCaps.setService(serviceType);        
        String desc = dom.getElementValue(serviceElt, "Abstract");
        serviceCaps.getIdentification().setDescription(desc);
        
        // Server URLS
        readOperationsMetadata(dom, capabilitiesElt);
        
        // Contents section
        readContents(dom, capabilitiesElt);
        
        return serviceCaps;
    }
    
    
    @Override
    protected void readOperationsMetadata(DOMHelper dom, Element capabilitiesElt)
    {
        Node map = dom.getAllElements(capabilitiesElt, "http:operation").item(0);
        String url = ((Element)map).getAttribute("location");

        // GET server
        if (url != null)
            serviceCaps.getGetServers().put("GetCoverage", url);
    }
    

    @Override
    protected void readContents(DOMHelper dom, Element capsElt)
    {
        //  Load List of GridCoverageLayer elements
    	NodeList layers = dom.getElements(capsElt, "ContentMetadata/CoverageLayerList/GridCoverageLayer");
		int numLayers = layers.getLength();
		Element layerCapElt;
		WCSLayerCapabilities layerCap; 
		ArrayList<String> crsList, formatList;
		ArrayList<TimeInfo> timeList;
        
		//  Go through each GridCoverageLayer and populate layerCaps
		for(int i=0; i<numLayers; i++) {
			layerCapElt = (Element)layers.item(i);
			layerCap = new WCSLayerCapabilities();
			layerCap.setParent(serviceCaps);
	        
			String layerId = dom.getElementValue(layerCapElt, "LayerID");
	        layerCap.setIdentifier(layerId);
	        
	        String desc = dom.getElementValue(layerCapElt, "Title");
	        layerCap.setDescription(desc);
	        
	        Element bboxElt = dom.getElement(layerCapElt, "LatLonBoundingBox");
	        Bbox bbox = getBbox(dom, bboxElt);
	        layerCap.getBboxList().add(bbox);
	        
	        crsList = getCRSList(dom, layerCapElt);
	        layerCap.setCrsList(crsList);
	        
	        formatList = getFormatList(dom, layerCapElt);
	        layerCap.setFormatList(formatList);
	        
	        try {
	        	timeList = getTimeList(dom, layerCapElt);
	        	layerCap.setTimeList(timeList);
	        } catch (ParseException e) {
	        	System.err.println("Could not parse timeInfo for WCSLayerCap ID: " + layerId);
	        }
	        
			serviceCaps.getLayers().add(layerCap);
		}
    }
    
    
    /**
     * Create TimeInfo array
     * @param laterElt
     * @return
     */
    protected ArrayList<TimeInfo> getTimeList(DOMHelper dom, Element layerElt)  throws ParseException
    {
    	NodeList timeElts = dom.getElements(layerElt, "DomainSetDescription/GridExtentDescription/TemporalExtent");
        int listSize = timeElts.getLength();
        ArrayList<TimeInfo> timeList = new ArrayList<TimeInfo>(listSize);
    	
		for(int i = 0; i < listSize; i++) {
			Element timeElt = (Element)timeElts.item(i);
			TimeInfo time = new TimeInfo();
			
	    	try
			{
				// case of TimeInterval
				if (dom.existElement(timeElt, "TimeInterval")) {
					String begin = dom.getElementValue(timeElt, "TimeInterval/minT");
					if(begin.indexOf("current")!=-1)  //  skip 'current' entries for now
						continue;
					time.setStartTime(DateTimeFormat.parseIso(begin));
					
					String end = dom.getElementValue(timeElt, "TimeInterval/maxT");
					if (end == null)
						time.setStopTime((new DateTime()).getJulianTime());
					else
						time.setStopTime(DateTimeFormat.parseIso(end));

					String step = dom.getElementValue("TimeInterval/resT");
					if(step != null){
						//  TODO:  add res parsing
						// double res = DateTimeFormat.parseTimeRes(step);
						// time.setStepTime(res);
					}
					timeList.add(time);
				}
				// case of TimeSeries/TimeInstant
				else {
					NodeList timeInstants = dom.getElements(timeElt, "TimeSeries/TimeInstant");
					Element timeInstantElt;
					int numTimes = timeInstants.getLength();
					for(int j=0; j<numTimes; j++){
						timeInstantElt = (Element)timeInstants.item(j);
						String timeStr = timeInstantElt.getTextContent();
						double jtime = DateTimeFormat.parseIso(timeStr);
						time.setStartTime(jtime);
						time.setStopTime(jtime);
						timeList.add(time);
					}
				}
			}
			catch (ParseException e)
			{
				e.printStackTrace();
				throw(e);
			}
		}
		return timeList;
    }
    
    
    /**
     * Retrieve list of available BBOX for this layer
     * @param bboxElement
     * @param Bbox
     */
    protected Bbox getBbox(DOMHelper dom, Element bboxElement)
    {
        double minX = Double.parseDouble(dom.getAttributeValue(bboxElement, "minx"));
        double minY = Double.parseDouble(dom.getAttributeValue(bboxElement, "miny"));
        double maxX = Double.parseDouble(dom.getAttributeValue(bboxElement, "maxx"));
        double maxY = Double.parseDouble(dom.getAttributeValue(bboxElement, "maxy"));
        Bbox bbox = new Bbox();
        bbox.setMinX(minX);
        bbox.setMinY(minY);
        bbox.setMaxX(maxX);
        bbox.setMaxY(maxY);
        
        return bbox;
    }
    
    /**
     * 
     * @param layerElt
     * @return
     */
    protected ArrayList<String> getFormatList(DOMHelper dom, Element layerElt){
    	ArrayList<String> formatList = new ArrayList<String>();
    	
    	NodeList formatElts = dom.getElements(layerElt, "SupportedFormatList/Format/FormatName");
    	int numElts = formatElts.getLength();
    	Element formatElt;
    	for(int i = 0; i < numElts; i++) {
    		formatElt = (Element)formatElts.item(i);
            String formatName = formatElt.getTextContent();
            formatList.add(formatName);
        }
    	
    	return formatList;
    }
    
    /**
     * 
     * @param layerElt
     * @return
     */
    protected ArrayList<String> getCRSList(DOMHelper dom, Element layerElt){
    	ArrayList<String> srsList = new ArrayList<String>();
    	
    	NodeList srsElts = dom.getElements(layerElt, "SupportedSRSList/SupportedSRS/SRS");
    	int numElts = srsElts.getLength();
    	Element srsElt;
    	for(int i = 0; i < numElts; i++) {
    		srsElt = (Element)srsElts.item(i);
            String srsName = srsElt.getTextContent();
            srsList.add(srsName);
        }
    	
    	return srsList;
    }    
}
