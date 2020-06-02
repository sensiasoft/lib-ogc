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

package org.vast.ows.wfs;

import java.util.*;
import org.w3c.dom.*;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;
import org.vast.ows.AbstractCapabilitiesReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSServiceCapabilities;


/**
 * <p>
 * Reads a WFS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WFSLayerCapabilities objects for version 1.1
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 30, 2005
 * */
public class WFSCapabilitiesReaderV11 extends AbstractCapabilitiesReader
{
	
    public WFSCapabilitiesReaderV11()
    {
    }
    
    
    @Override
    public OWSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
        OWSServiceCapabilities serviceCaps = new OWSServiceCapabilities();
    	
    	// Version
        String version = dom.getAttributeValue(capabilitiesElt, "version");
        serviceCaps.setVersion(version);
        
        // Read Service Identification Section
        Element serviceElt = dom.getElement(capabilitiesElt, "ServiceIdentification");
        String serviceTitle = dom.getElementValue(serviceElt, "Title");
        serviceCaps.getIdentification().setTitle(serviceTitle);
        String serviceType = dom.getElementValue(serviceElt, "ServiceType");
        serviceCaps.setService(serviceType);
        String desc = dom.getElementValue(serviceElt, "Abstract");
        serviceCaps.getIdentification().setDescription(desc);        
        
        // Server URLS
        readOperationsMetadata(dom, capabilitiesElt, serviceCaps);
        
        // Contents section
        readContents(dom, capabilitiesElt, serviceCaps);
        
        return serviceCaps;
    }
    
    
    @Override
    protected void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps)
    {
    	ArrayList<String> formatList = getFormatList(dom, capsElt);
    	
    	NodeList featureList = dom.getElements("FeatureTypeList/FeatureType");
        for (int i=0; i<featureList.getLength(); i++)
        {
        	Element featureElt = (Element)featureList.item(i);
        	WFSLayerCapabilities featureCaps = readFeature(dom, featureElt);
        	featureCaps.setParent(serviceCaps);
        	featureCaps.setFormatList(formatList);
        	((List<OWSLayerCapabilities>)serviceCaps.getLayers()).add(featureCaps);
        }
    }
    
    
    /**
     * Retrieve list of formats supported by this server
     * @return
     */
    protected ArrayList<String> getFormatList(DOMHelper dom, Element capsElt)
    {
    	ArrayList<String> formatList = null;
    	
    	Element getMapElt = dom.getElement(capsElt, "Capability/Request/GetFeature");
        if (getMapElt == null)
        	getMapElt = dom.getElement(capsElt, "Capability/Request/GetFeatureType");
        
        Element formatElement = dom.getElement(getMapElt, "ResultFormat");
        NodeList formatElts = dom.getAllChildElements(formatElement);
        int listSize = formatElts.getLength();
        formatList = new ArrayList<String>(listSize);
        
        for(int i = 0; i < listSize; i++)
        {
            formatElement = (Element)formatElts.item(i);
            String formatName = formatElement.getLocalName();
            formatList.add(formatName);
        }
        
        return formatList;
    }

    
    /**
     * Parse each Layer description, create and populate appropriate object
     * @param featureElt
     * @return WFSLayerCapabilities object
     */
    protected WFSLayerCapabilities readFeature(DOMHelper dom, Element featureElt)
    {
        WFSLayerCapabilities layerCaps = new WFSLayerCapabilities();
        
        // read layer name/id
        String name = dom.getElementValue(featureElt, "name");
        layerCaps.setIdentifier(name);        
        
        // read layer title/description
        String title = dom.getElementValue(featureElt, "title");
        if (title != null)
        	layerCaps.setTitle(title);
        else
        	layerCaps.setTitle(name);
        
        // read layer SRS and style lists
        getSRSList(dom, featureElt, layerCaps);
        getBBOXList(dom, featureElt, layerCaps);
 
        return layerCaps;
    }
    
    
    /**
     * Retrieve list of available SRS for this feature
     * @param featureElement
     * @param layerCaps
     */
    protected void getSRSList(DOMHelper dom, Element featureElement, WFSLayerCapabilities layerCaps)
    {
    	ArrayList<String> srsList = new ArrayList<String>();

        NodeList srsElts = dom.getElements(featureElement, "SRS");
        int listSize = srsElts.getLength();
        
        for(int i = 0; i < listSize; i++)
        {
            Element srsElement = (Element)srsElts.item(i);
            String srsNames = dom.getElementValue(srsElement, "");
            
            String [] srsArray = srsNames.split(" ");                        
            for(int j=0; j<srsArray.length; j++)
                srsList.add(srsArray[j]);
        }

        srsList.trimToSize();
        layerCaps.setSrsList(srsList);
    }
    
    
    /**
     * Retrieve list of available BBOX for this feature
     * @param featureElement
     * @param layerCaps
     */
    protected void getBBOXList(DOMHelper dom, Element featureElement, WFSLayerCapabilities layerCaps)
    {
    	NodeList bboxElts = dom.getElements(featureElement, "LatLonBoundingBox");
    	if (bboxElts.getLength() == 0)
    		bboxElts = dom.getElements(featureElement, "LatLongBoundingBox");

        int listSize = bboxElts.getLength();
        ArrayList<Bbox> bboxList = new ArrayList<Bbox>(listSize);
        
        for(int i = 0; i < listSize; i++)
        {
        	Element bboxElement = (Element)bboxElts.item(i);
            double minX = Double.parseDouble(dom.getAttributeValue(bboxElement, "minx"));
            double minY = Double.parseDouble(dom.getAttributeValue(bboxElement, "miny"));
            double maxX = Double.parseDouble(dom.getAttributeValue(bboxElement, "maxx"));
            double maxY = Double.parseDouble(dom.getAttributeValue(bboxElement, "maxy"));
            
            Bbox bbox = new Bbox();
            bbox.setMinX(minX);
            bbox.setMaxX(maxX);
            bbox.setMinY(minY);
            bbox.setMaxY(maxY);
            
            bboxList.add(bbox);
        }
        
        layerCaps.setBboxList(bboxList);
    }
}
