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

package org.vast.ows.wms;

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
 * Reads a WMS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WMSLayerCapabilities objects. for versions 1.0.0, 1.0.6
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 30, 2005
 * */
public class WMSCapabilitiesReaderV10 extends AbstractCapabilitiesReader
{
	ArrayList<String> formatList;
	
	
    public WMSCapabilitiesReaderV10()
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
        Element serviceElt = dom.getElement(capabilitiesElt, "Service");
        String serviceTitle = dom.getElementValue(serviceElt, "Title");
        serviceCaps.getIdentification().setTitle(serviceTitle);        
        String serviceType = dom.getElementValue(serviceElt, "Name");
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
    protected void readOperationsMetadata(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws OWSException
    {
        String url;
        
        // GET server
        url = dom.getAttributeValue(capsElt, "Capability/Request/Map/DCPType/HTTP/Get/onlineResource");
        if (url != null)
            serviceCaps.getGetServers().put("GetMap", url);
        
        // POST server
        url = dom.getAttributeValue(capsElt, "Capability/Request/Map/DCPType/HTTP/Post/onlineResource");
        if (url != null)
            serviceCaps.getPostServers().put("GetMap", url);
    }
    
    
    @Override
    protected void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps)
    {
    	readFormatList(dom, capsElt);
    	
    	// read max width and height
    	//int maxWidth = Integer.parseInt(dom.getElementValue("Service/"));
    	
    	Element rootLayer = dom.getElement(capsElt, "Capability/Layer");
    	WMSLayerCapabilities layerCaps = readLayer(dom, rootLayer, null, serviceCaps);
    	if (!serviceCaps.getLayers().contains(layerCaps))
    	    ((List<OWSLayerCapabilities>)serviceCaps.getLayers()).add(0, layerCaps);
    }
    
    
    /**
     * Retrieve list of formats supported by this server
     * @param layerCaps
     */
    protected void readFormatList(DOMHelper dom, Element capsElt)
    {
    	Element getMapElt = dom.getElement(capsElt, "Capability/Request/Map");
        if (getMapElt == null)
        	getMapElt = dom.getElement(capsElt, "Capability/Request/GetMap");
        
        Element formatElement = dom.getElement(getMapElt, "Format");
        NodeList formatElts = dom.getAllChildElements(formatElement);
        int listSize = formatElts.getLength();
        formatList = new ArrayList<String>(listSize);
        
        for(int i = 0; i < listSize; i++)
        {
            formatElement = (Element)formatElts.item(i);
            String formatName = formatElement.getLocalName();
            formatList.add(formatName);
        }
    }

    
    /**
     * Parse each Layer description, create and populate appropriate object
     * add add it to the capabilities layer list.
     * @param layerElt
     * @param parentCaps
     * @param serviceCaps
     */
    protected WMSLayerCapabilities readLayer(DOMHelper dom, Element layerElt, WMSLayerCapabilities parentCaps, OWSServiceCapabilities serviceCaps)
    {
        WMSLayerCapabilities layerCaps = new WMSLayerCapabilities();
        layerCaps.setParent(serviceCaps);
        
        // read layer id
        String id = dom.getElementValue(layerElt, "Name");
        layerCaps.setIdentifier(id);        
        
        // read layer name
        String name = dom.getElementValue(layerElt, "Title");
        layerCaps.setTitle(name);
        
        // read layer description
        String desc = dom.getElementValue(layerElt, "Abstract");
        layerCaps.setDescription(desc);
        
        // read layer SRS and style lists
        getSRSList(dom, layerElt, layerCaps);
        getStyleList(dom, layerElt, layerCaps);
        getBBOXList(dom, layerElt, layerCaps);
        
        // add format list
        layerCaps.setFormatList(formatList);

        // combine parent and local properties
        mergeWithParent(layerCaps, parentCaps);
        
        // add it to the main service object if it is has a name/id
    	if (layerCaps.getIdentifier() != null)
    	    ((List<OWSLayerCapabilities>)serviceCaps.getLayers()).add(layerCaps);
        
        // get all child layers        
        NodeList childLayers = dom.getElements(layerElt, "Layer");
        int listSize = childLayers.getLength();        
        if (listSize > 0)
        	layerCaps.setChildLayers(new ArrayList<WMSLayerCapabilities>(listSize));
        
        // call this function recursively for all nested layers
    	for (int i = 0; i < listSize; i++)
        {
            Element childLayer = (Element)childLayers.item(i);
            WMSLayerCapabilities childCaps = readLayer(dom, childLayer, layerCaps, serviceCaps);
            layerCaps.getChildLayers().add(childCaps);
        }
    	
    	return layerCaps;
    }
    
    
    /**
     * Retrieve list of available SRS for this layer
     * @param layerElement
     * @param layerCaps
     */
    protected void getSRSList(DOMHelper dom, Element layerElement, WMSLayerCapabilities layerCaps)
    {
    	ArrayList<String> srsList = null;

        String srsNames = dom.getElementValue(layerElement, "SRS");
        if (srsNames == null) return;

        String [] srsArray = srsNames.split(" ");
        int listSize = srsArray.length;
        srsList = new ArrayList<String>(listSize);
        
        for(int i=0; i<listSize; i++)
            srsList.add(srsArray[i]);

        layerCaps.setSrsList(srsList);
    }

    
    /**
     * Retrieve list of available styles for this layer
     * @param layerElement
     * @param layerCaps
     */
    protected void getStyleList(DOMHelper dom, Element layerElement, WMSLayerCapabilities layerCaps)
    {
        NodeList styleElts = dom.getElements(layerElement, "Style");
        int listSize = styleElts.getLength();
        ArrayList<String> styleList = new ArrayList<String>(listSize);
        
        for (int i = 0; i < listSize; i++)
		{
			Element styleElement = (Element) styleElts.item(i);
			String styleName = dom.getElementValue(styleElement, "Name");
			styleList.add(styleName);
		}

        layerCaps.setStyleList(styleList);
    }
    
    
    /**
     * Retrieve list of available BBOX for this layer
     * @param layerElement
     * @param layerCaps
     */
    protected void getBBOXList(DOMHelper dom, Element layerElement, WMSLayerCapabilities layerCaps)
    {
    	NodeList bboxElts = dom.getElements(layerElement, "LatLonBoundingBox");
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
    
    
    /**
     * Retrieve list of available extents for this layer
     * @param layerElement
     * @param layerCaps
     */
    protected void getExtentList(DOMHelper dom, Element layerElement, WMSLayerCapabilities layerCaps)
    {
//        Element extentElement = dom.getElement(layerElement, "Extent");
//        if (extentElement == null) return;
//
//        String extentName = dom.getAttributeValue(extentElement, "name");
//        String extentDefault = dom.getAttributeValue(extentElement, "default");
//        String extentRange = dom.getElementValue(extentElement, "");
//
//        String str = new String("extent = name = " + extentName
//                                + ", default = " + extentDefault
//                                + ", range = " + extentRange);
        
        // TODO add extent support. Is it always Time ???
    }
    
    
    /**
     * Combines inherited parent capabilities with current layer capabilities 
     * @param layerCaps
     * @param parentCaps
     */
    protected void mergeWithParent(WMSLayerCapabilities layerCaps, WMSLayerCapabilities parentCaps)
    {
    	if (parentCaps == null)
    		return;
    	
    	// copy capabilities from parent
    	layerCaps.getStyleList().addAll(parentCaps.getStyleList());
    	layerCaps.getSrsList().addAll(parentCaps.getSrsList());
    	
    	// copy bbox list from parent if this one is empty
    	if (layerCaps.getBboxList().isEmpty())
    		layerCaps.getBboxList().addAll(parentCaps.getBboxList());
    	
    	// copy max height and width
    	if (layerCaps.getMaxHeight() == -1)
    		layerCaps.setMaxHeight(parentCaps.getMaxHeight());
    	if (layerCaps.getMaxWidth() == -1)
    		layerCaps.setMaxWidth(parentCaps.getMaxWidth());
    }
}
