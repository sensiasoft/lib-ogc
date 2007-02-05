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

package org.vast.ows.wms;

import org.w3c.dom.*;
import org.vast.ows.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * WMS POST/GET Request Builder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a GET or POST WMS request based
 * on values contained in a WMSQuery object for versions
 * 1.0, 1.0.0 and 1.0.6
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public class WMSRequestWriterV10 extends WMSRequestWriter
{

    public WMSRequestWriterV10()
    {        
    }
    
	
	@Override
	public String buildURLQuery(WMSQuery query) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(query.getGetServer());
		
		urlBuff.append("wmtver=" + query.getVersion());
		urlBuff.append("&request=map");
		
		urlBuff.append("&layers=" + createLayerList(query));
        
		urlBuff.append("&styles=" + createStyleList(query));
        
		urlBuff.append("&srs=" + query.getSrs());
        
		urlBuff.append("&bbox=");
        this.writeBboxArgument(urlBuff, query.getBbox());
        
		urlBuff.append("&width=" + query.getWidth());
        
		urlBuff.append("&height=" + query.getHeight());
        
		urlBuff.append("&format=" + query.getFormat());
        
		urlBuff.append("&transparent=" + (query.isTransparent() ? "TRUE" : "FALSE"));
        
		urlBuff.append("&exceptions=" + query.getExceptionType());
				
		String url = urlBuff.toString();
		url.replaceAll(" ","%20");
		return url;
	}	
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, WMSQuery query) throws OWSException
	{
		throw new WMSException("XML request not supported in WMS 1.0");
	}
}