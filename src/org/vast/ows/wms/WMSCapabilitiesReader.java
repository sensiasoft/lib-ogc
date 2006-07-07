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

package org.vast.ows.wms;

import java.util.*;

import org.w3c.dom.*;
import org.vast.ows.OWSCapabilitiesReader;
import org.vast.ows.OWSException;
import org.vast.ows.util.Bbox;


/**
 * <p><b>Title:</b><br/>
 * WMS Capabilities Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads a WMS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WMSLayerCapabilities objects
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public class WMSCapabilitiesReader extends OWSCapabilitiesReader
{
	ArrayList<String> formatList;
	
	
    public WMSCapabilitiesReader()
    {
    }
    
    
    @Override
    protected String buildQuery() throws OWSException
    {
        String url = null;
        
        if (versionGroup == GROUP1)
        {
            url = this.server + "REQUEST=capabilities&WMTVER=" + version;
        }
        else if (versionGroup >= GROUP2)
        {
        	url = this.server + "service=WMS&version=" + version + "&request=GetCapabilities";
        	//url = server + firstChar + "REQUEST=GetCapabilities&VERSION=" + version + "&SERVICE=WMS";
        }
        else
            throw new WMSException("WMS version " + version + " not supported");

        System.out.println("WMS Capabilities request: " + url);        
        return url;
    }


    @Override
    protected void setVersionGroup(String version) throws WMSException
    {
    	if ((version.equalsIgnoreCase("1.0"))||
            (version.equalsIgnoreCase("1.0.0")) ||
            (version.equalsIgnoreCase("1.0.6")))
        {
            versionGroup = GROUP1;
        }
        else if ((version.equalsIgnoreCase("1.1"))||
        		 (version.equalsIgnoreCase("1.1.0"))||
                 (version.equalsIgnoreCase("1.0.8")))
        {
            versionGroup = GROUP2;
        }
        else if ((version.equalsIgnoreCase("1.1.1"))||
        		 (version.equalsIgnoreCase("1.3")))
        {
            versionGroup = GROUP3;
        }
        else
            throw new WMSException("WMS version " + version + " not supported");
    }
    
    
    @Override
    protected void readServers()
    {
    	String url;
    	
    	// GET server
    	url = dom.getAttributeValue("Capability/Request/Map/DCPType/HTTP/Get/onlineResource");
    	if (url == null)
            url = dom.getAttributeValue("Capability/Request/GetMap/DCPType/HTTP/Get/OnlineResource/href");
    	if (url != null)
    		serviceCaps.getGetServers().put("GetMap", url);
    	
        // POST server
        url = dom.getAttributeValue("Capability/Request/Map/DCPType/HTTP/Post/onlineResource");
        if (url == null)
            url = dom.getAttributeValue("Capability/Request/GetMap/DCPType/HTTP/Post/OnlineResource/href");
        if (url != null)
        	serviceCaps.getPostServers().put("GetMap", url);
    }
    
    
    @Override
    protected void readContents(Element contentElt)
    {
    	readFormatList();
    	
    	// read max width and height
    	//int maxWidth = Integer.parseInt(dom.getElementValue("Service/"));
    	
    	Element rootLayer = dom.getElement("Capability/Layer");
    	WMSLayerCapabilities layerCaps = readLayer(rootLayer, null);
    	if (!serviceCaps.getLayers().contains(layerCaps))
    		serviceCaps.getLayers().add(0, layerCaps);
    }
    
    
    /**
     * Retrieve list of formats supported by this server
     * @param layerCaps
     */
    protected void readFormatList()
    {
    	Element getMapElt = dom.getElement("Capability/Request/Map");
        if (getMapElt == null)
        	getMapElt = dom.getElement("Capability/Request/GetMap");
        
        if (this.versionGroup == GROUP1)
        {
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

        else if (this.versionGroup >= GROUP2)
        {
            NodeList formatElts = dom.getElements(getMapElt, "Format");
            int listSize = formatElts.getLength();
            formatList = new ArrayList<String>(listSize);
            
            for (int i = 0; i < listSize; i++)
			{
				Element formatElement = (Element) formatElts.item(i);
				String formatName = dom.getElementValue(formatElement, "");
				formatList.add(formatName);
			}
        }
    }

    
    /**
     * Parse each Layer description, create and populate appropriate object
     * add add it to the capabilities layer list.
     * @param layerElt
     * @param parentCaps
     * @param serviceCaps
     */
    protected WMSLayerCapabilities readLayer(Element layerElt, WMSLayerCapabilities parentCaps)
    {
        WMSLayerCapabilities layerCaps = new WMSLayerCapabilities();
        layerCaps.setParent(serviceCaps);
        
        // read layer id
        String id = dom.getElementValue(layerElt, "Name");
        layerCaps.setId(id);        
        
        // read layer name
        String name = dom.getElementValue(layerElt, "Title");
        layerCaps.setName(name);
        
        // read layer description
        String desc = dom.getElementValue(layerElt, "Abstract");
        layerCaps.setDescription(desc);
        
        // read layer SRS and style lists
        getSRSList(layerElt, layerCaps);
        getStyleList(layerElt, layerCaps);
        getBBOXList(layerElt, layerCaps);
        
        // add format list
        layerCaps.setFormatList(formatList);

        // combine parent and local properties
        mergeWithParent(layerCaps, parentCaps);
        
        // add it to the main service object if it is has a name/id
    	if (layerCaps.getId() != null)
        	serviceCaps.getLayers().add(layerCaps);
        
        // get all child layers        
        NodeList childLayers = dom.getElements(layerElt, "Layer");
        int listSize = childLayers.getLength();        
        if (listSize > 0)
        	layerCaps.setChildLayers(new ArrayList<WMSLayerCapabilities>(listSize));
        
        // call this function recursively for all nested layers
    	for (int i = 0; i < listSize; i++)
        {
            Element childLayer = (Element)childLayers.item(i);
            WMSLayerCapabilities childCaps = readLayer(childLayer, layerCaps);
            layerCaps.getChildLayers().add(childCaps);
        }
    	
    	return layerCaps;
    }
    
    
    /**
     * Retrieve list of available SRS for this layer
     * @param layerElement
     * @param layerCaps
     */
    protected void getSRSList(Element layerElement, WMSLayerCapabilities layerCaps)
    {
    	ArrayList<String> srsList = null;

        if (this.versionGroup <= GROUP2)
        {
            String srsNames = dom.getElementValue(layerElement, "SRS");
            if (srsNames == null) return;

            String [] srsArray = srsNames.split(" ");
            int listSize = srsArray.length;
            srsList = new ArrayList<String>(listSize);
            
            for(int i=0; i<listSize; i++)
            	srsList.add(srsArray[i]);
        }

        else if (this.versionGroup == GROUP3)
        {
            NodeList srsElts = dom.getElements(layerElement, "SRS");
            int listSize = srsElts.getLength();
            srsList = new ArrayList<String>(listSize);
            
            for(int i = 0; i < listSize; i++)
            {
                Element srsElement = (Element)srsElts.item(i);
                String srsName = dom.getElementValue(srsElement, "");
                srsList.add(srsName);
            }
        }

        layerCaps.setSrsList(srsList);
    }

    
    /**
     * Retrieve list of available styles for this layer
     * @param layerElement
     * @param layerCaps
     */
    protected void getStyleList(Element layerElement, WMSLayerCapabilities layerCaps)
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
    protected void getBBOXList(Element layerElement, WMSLayerCapabilities layerCaps)
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
    protected void getExtentList(Element layerElement, WMSLayerCapabilities layerCaps)
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
