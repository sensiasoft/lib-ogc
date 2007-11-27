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

package org.vast.ows;

import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;


/**
 * <p><b>Title:</b><br/>
 * OWS Capabilities Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base abstract class for reading an OWS server capabilities document.
 * This class instantiates a OWSServiceCapabilities object.
 * Descendants should add their own specific layer capabilities object 
 * to the list in OWSServiceCapablities.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public abstract class AbstractCapabilitiesReader implements OWSCapabilitiesReader
{
    protected final static String parsingError = "Error while parsing capabilities document";
    protected OWSServiceCapabilities serviceCaps;
    protected String version;
    protected String server;
    protected boolean showQuery;
    

    public AbstractCapabilitiesReader()
    {
    }
    
    
    protected abstract String buildQuery() throws OWSException;
    protected abstract void readContents(DOMHelper dom, Element capsElt) throws OWSException;
    
    
    /**
     * This method will issue a GetCapabilities request and parse
     * the returned capabilities document.
     * @param server
     * @param version
     * @return
     * @throws OWSException
     */
    public OWSServiceCapabilities getCapabilities(String server, String version) throws OWSException
    {
    	// Try to parse the XML response
    	try
		{
    		this.server = OWSRequest.checkServer(server);
    		this.version = version;
    		String url = buildQuery();
            
            if (showQuery)
                System.out.println("Capabilities Request: " + url);
            
            DOMHelper dom = new DOMHelper(url, false);
            
            // Check if exception was returned
            OWSExceptionReader.checkException(dom);
            
            // read capabilities xml from dom
            return readCapabilities(dom, dom.getBaseElement());
		}
		catch (DOMHelperException e)
		{
			throw new OWSException("The Service responded with an invalid XML document", e);
		}
    }
    
    
    /**
     * Read Capabilities from the given DOM document
     * @param dom
     * @param capabilitiesElt
     * @return
     * @throws OWSException
     */
    public OWSServiceCapabilities readCapabilities(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
    	// Version
        this.version = dom.getAttributeValue(capabilitiesElt, "version");
        
        // Read Service Identification Section
        serviceCaps = new OWSServiceCapabilities();
        Element serviceElt;
        String serviceTitle = null;
        String serviceType = null;
        String desc = null;
        
        // two cases depending on service
        if ((serviceElt = dom.getElement(capabilitiesElt, "ServiceIdentification")) != null)
        {
	        serviceTitle = dom.getElementValue(serviceElt, "Title");
	        serviceType = dom.getElementValue(serviceElt, "ServiceType");
	        desc = dom.getElementValue(serviceElt, "Abstract");
        }
        else if ((serviceElt = dom.getElement(capabilitiesElt, "Service")) != null)
        {
        	serviceTitle = dom.getElementValue(serviceElt, "Title");
	        serviceType = dom.getElementValue(serviceElt, "Name");
	        desc = dom.getElementValue(serviceElt, "Abstract");
        }
        
        serviceCaps.setTitle(serviceTitle);
        serviceCaps.setDescription(desc);
        serviceCaps.setService(serviceType);
        serviceCaps.setVersion(this.version);
        
        // Server URLS
        readServers(dom, capabilitiesElt);
        
        // Contents section
        readContents(dom, capabilitiesElt);
        
        return serviceCaps;
    }
    
    
    /**
     * Read server GET and POST urls for each operation
     * @param operationMetadataElt
     * @param capabilities
     */
    protected void readServers(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
    	NodeList operationList = dom.getElements(capabilitiesElt, "OperationsMetadata/Operation");    	
    	for (int i=0; i<operationList.getLength(); i++)
        {
    		Element operationElt = (Element)operationList.item(i);
    		String name = dom.getAttributeValue(operationElt, "name");
    		
    		String getServer = dom.getAttributeValue(operationElt, "DCP/HTTP/Get/href");
    		if (getServer != null)
    			serviceCaps.getGetServers().put(name, getServer);
    		
    		String postServer = dom.getAttributeValue(operationElt, "DCP/HTTP/Post/href");
    		if (postServer != null)
    			serviceCaps.getPostServers().put(name, postServer);
        }
        
        
////       GET server
//        url = dom.getAttributeValue(capsElt, "Capability/Request/Map/DCPType/HTTP/Get/onlineResource");
//        if (url == null)
//            url = dom.getAttributeValue(capsElt, "Capability/Request/GetMap/DCPType/HTTP/Get/OnlineResource/href");
//        if (url != null)
//            serviceCaps.getGetServers().put("GetMap", url);
//        
//        // POST server
//        url = dom.getAttributeValue(capsElt, "Capability/Request/Map/DCPType/HTTP/Post/onlineResource");
//        if (url == null)
//            url = dom.getAttributeValue(capsElt, "Capability/Request/GetMap/DCPType/HTTP/Post/OnlineResource/href");
//        if (url != null)
//            serviceCaps.getPostServers().put("GetMap", url);
//        
////      GET server
//        url = dom.getAttributeValue("Capability/Request/GetFeature/DCPType/HTTP/Get/@onlineResource");
//        if (url == null)
//            url = dom.getAttributeValue("Capability/Request/GetFeatureType/DCPType/HTTP/Get/@onlineResource");
//        if (url != null)
//            serviceCaps.getGetServers().put("GetFeature", url);
//        
//        // POST server
//        url = dom.getAttributeValue("Capability/Request/GetFeature/DCPType/HTTP/Post/@onlineResource");
//        if (url == null)
//            url = dom.getAttributeValue("Capability/Request/GetFeatureType/DCPType/HTTP/Post/@onlineResource");
//        if (url != null)
//            serviceCaps.getPostServers().put("GetFeature", url);
    }


    public void setShowQuery(boolean showQuery)
    {
        this.showQuery = showQuery;
    }
}
