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

package org.vast.ows.wps;

import java.util.*;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSServiceCapabilities;


/**
 * <p>
 * Reads a WPS server capabilities document and create and
 * populate the corresponding OWSServiceCapabilities and
 * WPSLayerCapabilities objects
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Nov 29, 2008
 * */
public class WPSCapabilitiesReader extends OWSCapabilitiesReaderV11
{
   
    public WPSCapabilitiesReader()
    {
    }
       
    
    @Override
    protected void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws WPSException
    {
        NodeList offeringList = dom.getElements(capsElt, "Contents/ProcessOfferingList/ProcessOffering");
        
        for (int i=0; i<offeringList.getLength(); i++)
        {
            Element offeringElt = (Element)offeringList.item(i);
            String offeringName = dom.getElementValue(offeringElt, "name");
            Element methodElt = (Element)offeringElt.getElementsByTagName("method");
            String methodURI = dom.getAttributeValue(methodElt, "xlink:href");
            
            if (offeringName == null)
            	offeringName = "Offering " + i + 1;
            
            String id = dom.getAttributeValue(offeringElt, "id");
            
            WPSLayerCapabilities layerCaps = new WPSLayerCapabilities();           
            
            layerCaps.setTitle(offeringName);
	        layerCaps.setIdentifier(id);
	        layerCaps.setMethod(methodURI);
	        getResponseFormatList(dom, offeringElt, layerCaps);
	        getRequestFormatList(dom, offeringElt, layerCaps);
	        
	        layerCaps.setParent(serviceCaps);
            
	        ((List<OWSLayerCapabilities>)serviceCaps.getLayers()).add(layerCaps);
        }
    }
 
    /**
     * Creates format list array
     * @param parentElement
     */
    protected void getResponseFormatList(DOMHelper dom, Element parentElement, WPSLayerCapabilities layerCaps) throws WPSException
    {
    	NodeList responseFormatElts = dom.getElements(parentElement, "responseFormat");
        int listSize = responseFormatElts.getLength();

        ArrayList<String> responseFormatList = new ArrayList<String>(listSize);
        layerCaps.setResponseFormatList(responseFormatList);
        
        for(int i = 0; i < listSize; i++)
        {
            Element responseFormatElt = (Element)responseFormatElts.item(i);
            String responseFormat = dom.getElementValue(responseFormatElt, "");
            responseFormatList.add(responseFormat);
        }
    }
    
    
    /**
     * Creates format list array
     * @param parentElement
     */
    protected void getRequestFormatList(DOMHelper dom, Element parentElement, WPSLayerCapabilities layerCaps) throws WPSException
    {
    	NodeList requestFormatElts = dom.getElements(parentElement, "requestFormat");
        int listSize = requestFormatElts.getLength();

        ArrayList<String> requestFormatList = new ArrayList<String>(listSize);
        layerCaps.setRequestFormatList(requestFormatList);
        
        for(int i = 0; i < listSize; i++)
        {
            Element requestFormatElt = (Element)requestFormatElts.item(i);
            String requestFormat = dom.getElementValue(requestFormatElt, "");
            requestFormatList.add(requestFormat);
        }
    }
    
}
