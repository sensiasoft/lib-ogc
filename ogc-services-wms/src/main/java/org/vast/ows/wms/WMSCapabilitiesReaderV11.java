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
import org.vast.xml.DOMHelper;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;


/**
 * <p>
 * Reads a WMS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WMSLayerCapabilities objects for version 1.1.1, 1.3
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 30, 2005
 * */
public class WMSCapabilitiesReaderV11 extends WMSCapabilitiesReaderV10
{
	public WMSCapabilitiesReaderV11()
    {
    }
    
    
    @Override
    protected void readOperationsMetadata(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws OWSException
    {
        String url;
        
        // GET server
        url = dom.getAttributeValue(capsElt, "Capability/Request/GetMap/DCPType/HTTP/Get/OnlineResource/@href");
        if (url != null)
            serviceCaps.getGetServers().put("GetMap", url);
        
        // POST server
        url = dom.getAttributeValue(capsElt, "Capability/Request/GetMap/DCPType/HTTP/Post/OnlineResource/@href");
        if (url != null)
            serviceCaps.getPostServers().put("GetMap", url);
    }
    
    
    @Override
    protected void readFormatList(DOMHelper dom, Element capsElt)
    {
    	Element getMapElt = dom.getElement(capsElt, "Capability/Request/GetMap");
        
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
    
    
    @Override
    protected void getSRSList(DOMHelper dom, Element layerElement, WMSLayerCapabilities layerCaps)
    {
    	ArrayList<String> srsList = null;

        NodeList srsElts = dom.getElements(layerElement, "SRS");
        int listSize = srsElts.getLength();
        srsList = new ArrayList<String>(listSize);
        
        for(int i = 0; i < listSize; i++)
        {
            Element srsElement = (Element)srsElts.item(i);
            String srsName = dom.getElementValue(srsElement, "");
            srsList.add(srsName);
        }

        layerCaps.setSrsList(srsList);
    }
}
