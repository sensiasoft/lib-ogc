/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.sos.SOSUtils;
import org.vast.ows.swe.SWEResponseWriter;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * InsertObservation Response Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to generate an XML InsertObservation response based
 * on values contained in a InsertObservationResponse object for SOS v2.0
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 3, 2014
 * @version 1.0
 */
public class InsertObservationResponseWriterV20 extends SWEResponseWriter<InsertObservationResponse>
{
		
	public Element buildXMLResponse(DOMHelper dom, InsertObservationResponse response, String version) throws OWSException
	{
	    dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(SOSUtils.SOS, version));
        
        // root element
        Element rootElt = dom.createElement("sos:" + response.getMessageType());
        
        // write extensions
        writeExtensions(dom, rootElt, response);
        
        // assigned observation id
        if (response.getObsId() != null)
            dom.setElementValue(rootElt, "+sos:observation", response.getObsId());
        
        return rootElt;
	}
	
}