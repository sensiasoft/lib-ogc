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
public class WCSRequestWriterV07 extends WCSRequestWriter
{
	
	@Override
	public String buildURLQuery(WCSQuery query) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(query.getGetServer());
		
        urlBuff.append("service=WCS");
        urlBuff.append("&version=" + query.getVersion());
        urlBuff.append("&request=GetCoverage");          
        urlBuff.append("&layers=" + query.getLayer());
        urlBuff.append("&crs=" + query.getSrs());
        
        if (query.getTime() != null)
        {
            urlBuff.append("&time=");
            this.writeTimeArgument(urlBuff, query.getTime());
        }
        
        urlBuff.append("&bbox=");
        this.writeBboxArgument(urlBuff, query.getBbox());
        urlBuff.append("&skipX=" + query.getSkipX());
        urlBuff.append("&skipY=" + query.getSkipY());
        urlBuff.append("&format=" + query.getFormat());
		
		String url = urlBuff.toString();
		url.replaceAll(" ","%20");
		return url;
	}	
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, WCSQuery query) throws OWSException
	{
	    // TODO WCSRequestWriter v0.7 buildRequestXML
        return null;
	}
}