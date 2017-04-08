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

package org.vast.ows.swe;

import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSRequest;


/**
 * <p>
 * Base abstract class for SWE services POST/GET request builders
 * </p>
 *
 * @author Alex Robin
 * @date Oct 30, 2005
 * @param <RequestType> Type of OWS request object accepted by this writer
 */
public abstract class SWERequestWriter<RequestType extends OWSRequest> extends AbstractRequestWriter<RequestType>
{	
	
	public SWERequestWriter()
	{	
	}
	
	    
    /**
     * Adds common attributes and elements to XML request
     * @param dom
     * @param requestElt
     * @param request
     */
	@Override
    protected void addCommonXML(DOMHelper dom, Element requestElt, OWSRequest request)
	{
    	super.addCommonXML(dom, requestElt, request);
    	
    	// write extensions
    	Map<QName, Object> extObjs = request.getExtensions();
        SWESUtils.writeXMLExtensions(dom, requestElt, request.getVersion(), extObjs);
	}
}