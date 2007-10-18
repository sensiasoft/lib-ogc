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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.ows.*;
import org.vast.ows.wms.WMSException;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * WCS Request Builder v0.7
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a GET or POST WCS request based
 * on values contained in a WCSQuery object for version 0.7
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public class WCSRequestWriterV07 extends AbstractRequestWriter<GetCoverageRequest>
{
	
	@Override
	public String buildURLQuery(GetCoverageRequest request) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(request.getGetServer());
		
        urlBuff.append("service=WCS");
        urlBuff.append("&version=" + request.getVersion());
        String operation = request.getOperation();
        urlBuff.append("&request=" + operation);
        if(!"GetCapabilities".equalsIgnoreCase(operation)) {
	        urlBuff.append("&layers=" + request.getCoverage());
	        urlBuff.append("&crs=" + request.getBbox().getCrs());
	        
	        if (request.getTime() != null)
	        {
	            urlBuff.append("&time=");
	            this.writeTimeArgument(urlBuff, request.getTime());
	        }
	        
	        urlBuff.append("&bbox=");
	        this.writeBboxArgument(urlBuff, request.getBbox());
	        urlBuff.append("&skipX=" + request.getSkipX());
	        urlBuff.append("&skipY=" + request.getSkipY());
	        urlBuff.append("&format=" + request.getFormat());
        }
		String url = urlBuff.toString();
		url.replaceAll(" ","%20");
		return url;
	}	
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetCoverageRequest query) throws OWSException
	{
        throw new WMSException(noXML + "WCS 0.7");
	}
}