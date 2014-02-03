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
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseWriter;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * InsertResultTemplate Response Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to generate an XML InsertResultTemplate response based
 * on values contained in a InsertResultTemplateResponse object for SOS v2.0
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 3, 2014
 * @version 1.0
 */
public class InsertResultTemplateResponseWriterV20 extends SWEResponseWriter<InsertResultTemplateResponse>
{
		
	public Element buildXMLResponse(DOMHelper dom, InsertResultTemplateResponse response, String version) throws OWSException
	{
	    dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(SOSUtils.SOS, version));
        
        // root element
        Element rootElt = dom.createElement("sos:" + response.getMessageType());
        
        // write extensions
        writeExtensions(dom, rootElt, response);
        
        // template id
        dom.setElementValue(rootElt, "acceptedTemplate", response.getAcceptedTemplateId());
        
        return rootElt;
	}
	
}