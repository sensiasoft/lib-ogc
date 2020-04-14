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


/**
 * <p>
 * Base abstract class for reading an OWS server capabilities document.
 * This class instantiates a OWSServiceCapabilities object.
 * Descendants should add their own specific layer capabilities object 
 * to the list in OWSServiceCapablities.
 * </p>
 *
 * @author Alex Robin
 * @since Oct 30, 2005
 * */
public abstract class AbstractCapabilitiesReader extends AbstractResponseReader<OWSServiceCapabilities>
{
    protected final static String xmlError = "Error while parsing capabilities document XML";
    

    public AbstractCapabilitiesReader()
    {
    }
    
    
    protected abstract void readContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities serviceCaps) throws OWSException;
           
        
    /**
     * Read server GET and POST urls for each operation
     * @param operationMetadataElt
     * @param capabilities
     */
    protected void readOperationsMetadata(DOMHelper dom, Element capabilitiesElt, OWSServiceCapabilities serviceCaps) throws OWSException
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
    }
}
