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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wms;

import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP GetMap request based
 * on values contained in a GetMapRequest object for version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 10, 2007
 * */
public class GetMapWriterV10 extends AbstractRequestWriter<GetMapRequest>
{

    public GetMapWriterV10()
    {        
    }
    
	
	@Override
	public String buildURLQuery(GetMapRequest request) throws OWSException
	{
	    StringBuilder urlBuff = new StringBuilder(request.getGetServer());
		
		urlBuff.append("wmtver=" + request.getVersion());
		urlBuff.append("&request=map");
		
		urlBuff.append("&layers=" + createLayerList(request));
        
		urlBuff.append("&styles=" + createStyleList(request));
        
		urlBuff.append("&srs=" + request.getSrs());
        
		urlBuff.append("&bbox=");
        this.writeBboxArgument(urlBuff, request.getBbox());
        
		urlBuff.append("&width=" + request.getWidth());
        
		urlBuff.append("&height=" + request.getHeight());
        
		urlBuff.append("&format=" + request.getFormat());
        
		urlBuff.append("&transparent=" + (request.isTransparent() ? "TRUE" : "FALSE"));
        
		urlBuff.append("&exceptions=" + request.getExceptionType());
				
		String url = urlBuff.toString();
		url.replaceAll(" ","%20");
		return url;
	}	
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetMapRequest request) throws OWSException
	{
		throw new WMSException(noXML + "WMS 1.0");
	}
	
	
	
	/**
     * Create comma separated layer list for GET request
     * @param request
     * @return
     */
    protected String createLayerList(GetMapRequest request)
    {
        StringBuffer buff = new StringBuffer();
        
        int layerCount = request.getLayers().size();
        for (int i=0; i<layerCount; i++)
        {
            buff.append(request.getLayers().get(i));
            
            if (i != layerCount-1)
                buff.append(',');               
        }
        
        return buff.toString();
    }
    
    
    /**
     * Create comma separated style list for GET request
     * @param request
     * @return
     */
    protected String createStyleList(GetMapRequest request)
    {
        StringBuffer buff = new StringBuffer();
        
        int styleCount = request.getStyles().size();
        for (int i=0; i<styleCount; i++)
        {
            buff.append(request.getStyles().get(i));
            
            if (i != styleCount-1)
                buff.append(',');               
        }
        
        return buff.toString();
    }
}