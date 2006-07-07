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

package org.vast.ows;

import org.w3c.dom.*;
import org.vast.io.xml.*;


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
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public abstract class OWSCapabilitiesReader
{
    final static protected int GROUP1 = 1;
    final static protected int GROUP2 = 2;
    final static protected int GROUP3 = 3;
    final static protected int GROUP4 = 4;

    protected String version;
    protected int versionGroup = GROUP1;
    protected String server;
    protected DOMReader dom;
    protected OWSServiceCapabilities serviceCaps;
    

    public OWSCapabilitiesReader()
    {
    	dom = new DOMReader();
    }
    
    
    protected abstract String buildQuery() throws OWSException;
    protected abstract void setVersionGroup(String version) throws OWSException;
    protected abstract void readContents(Element contentElt) throws OWSException;
    
    
    /**
     * This method will issue a GetCapabilities request and parse
     * the returned capabilities document.
     * @param server
     * @param version
     * @return
     * @throws OWSException
     */
    public OWSServiceCapabilities readCapabilities(String server, String version) throws OWSException
    {
    	// Try to parse the XML response
    	try
		{
    		this.server = OWSQuery.checkServer(server);
    		this.version = version;
    		setVersionGroup(version);
    		String url = buildQuery();
    		dom.setXmlFragment(dom.parseURI(url, false));
		}
		catch (DOMReaderException e)
		{
			throw new OWSException("The Service responded with an invalid XML document", e);
		}
		
		// Check if exception was returned
        OWSExceptionReader.checkException(dom);
        
		return readCapabilities(dom.getBaseElement());
    }
    
    
    public OWSServiceCapabilities readCapabilities(DOMReader dom, Element capabilitiesElt) throws OWSException
    {
        this.dom = dom;
        return readCapabilities(capabilitiesElt);
    }
    
    
    /**
     * Will create and populate an OWSServiceCapabilities object and
     * call the service specific readContents method.
     * @param capabilitiesElt
     * @return
     * @throws OWSException
     */
    public OWSServiceCapabilities readCapabilities(Element capabilitiesElt) throws OWSException
    {
    	dom.getXmlFragment().setBaseElement(capabilitiesElt);
    			
    	// Version
        this.version = dom.getAttributeValue("version");
        setVersionGroup(version);
        
        // Read Service Identification Section
        serviceCaps = new OWSServiceCapabilities();
        Element serviceElt;
        String serviceTitle = null;
        String serviceType = null;
        String desc = null;
        
        // two cases depending on service
        if ((serviceElt = dom.getElement("ServiceIdentification")) != null)
        {
	        serviceTitle = dom.getElementValue(serviceElt, "Title");
	        serviceType = dom.getElementValue(serviceElt, "ServiceType");
	        desc = dom.getElementValue(serviceElt, "Abstract");
        }
        else if ((serviceElt = dom.getElement("Service")) != null)
        {
        	serviceTitle = dom.getElementValue(serviceElt, "Title");
	        serviceType = dom.getElementValue(serviceElt, "Name");
	        desc = dom.getElementValue(serviceElt, "Abstract");
        }
        
        serviceCaps.setName(serviceTitle);
        serviceCaps.setDescription(desc);
        serviceCaps.setService(serviceType);
        serviceCaps.setVersion(this.version);
        
        // Server URLS
        readServers();
        
    	//if (capabilities.getPostServers().isEmpty())
    	//	capabilities.getPostServers().put("GetObservation", this.server);
        
        // Contents section
        Element contentElt = dom.getElement("Contents");
        readContents(contentElt);
        
        return serviceCaps;
    }
    
    
    /**
     * Read server GET and POST urls for each operation
     * @param operationMetadataElt
     * @param capabilities
     */
    protected void readServers()
    {
    	NodeList operationList = dom.getElements("OperationsMetadata/Operation");    	
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
    }
}
