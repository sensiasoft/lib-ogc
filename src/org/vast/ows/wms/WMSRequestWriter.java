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

import org.vast.ows.OWSQuery;
import org.vast.ows.OWSRequestWriter;
import org.w3c.dom.*;
import org.vast.io.xml.DOMWriter;


/**
 * <p><b>Title:</b><br/>
 * WMS POST/GET Request Builder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a GET or POST WMS request based
 * on values contained in a WMSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public class WMSRequestWriter extends OWSRequestWriter
{
    final static int GROUP1 = 1;
    final static int GROUP2 = 2;    
	int versionGroup = GROUP1;
	

	public WMSRequestWriter()
	{	
	}
	
	
	public void setVersionGroup(String version) throws WMSException
	{
        if ((version.equalsIgnoreCase("1.0"))||
            (version.equalsIgnoreCase("1.0.0")) ||
            (version.equalsIgnoreCase("1.0.6")))
        {
            versionGroup = GROUP1;
        }
        else if ((version.equalsIgnoreCase("1.1.0"))||
                 (version.equalsIgnoreCase("1.1.1")) ||
                 (version.equalsIgnoreCase("1.0.8")))
        {
            versionGroup = GROUP2;
        }
        else
            throw new WMSException("WMS version " + version + " not supported");
    }

	
	@Override
	public String buildGetRequest(OWSQuery owsQuery) throws WMSException
	{
		WMSQuery query = (WMSQuery)owsQuery;
		setVersionGroup(query.getVersion());
		
		StringBuffer urlBuff = new StringBuffer(query.getGetServer());
		
		if (versionGroup == GROUP1)
		{
			urlBuff.append("wmtver=" + query.getVersion());
			urlBuff.append("&request=map");
		}
		else if (versionGroup == GROUP2)
		{
			urlBuff.append("service=WMS");
			urlBuff.append("&version=" + query.getVersion());
			urlBuff.append("&request=GetMap");			
		}
		
		urlBuff.append("&layers=" + query.getLayers().get(0));
        
		urlBuff.append("&styles=");
		if (!query.getStyles().isEmpty())
			urlBuff.append(query.getStyles().get(0));
        
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
	public Element buildRequestXML(OWSQuery owsQuery, DOMWriter domWriter) throws WMSException
	{
		WMSQuery query = (WMSQuery)owsQuery;
		domWriter.addNS("http://www.opengis.net/ows", "ows");
		domWriter.addNS("http://www.opengis.net/ogc", "ogc");
		domWriter.addNS("http://www.opengis.net/gml", "gml");
		domWriter.addNS("http://www.opengis.net/sld", "sld");
		
		// root element
		Element rootElt = domWriter.createElement("ows:GetMap");
		domWriter.setAttributeValue(rootElt, "version", query.getVersion());
		domWriter.setAttributeValue(rootElt, "service", query.getService());
		
		// layers and styles
		for (int i=0; i<query.getLayers().size(); i++)
		{
			Element layerdElt = domWriter.addElement(rootElt, "sld:StyledLayerDescriptor/+sld:NamedLayer");
			domWriter.setElementValue(layerdElt, "sld:Name", query.getLayers().get(i));
			if (!query.getStyles().isEmpty())
				domWriter.setElementValue(layerdElt, "sld:NamedStyle/sld:Name", query.getStyles().get(i));
		}
		
		// bounding box
		domWriter.setElementValue(rootElt, "ows:BoundingBox/gml:coordinates", getBboxList(query));
		domWriter.setAttributeValue(rootElt, "ows:BoundingBox/@srsName", query.getSrs());
		
		// output parameters
		domWriter.setElementValue(rootElt, "ows:Output/ows:Format", query.getFormat());
		domWriter.setElementValue(rootElt, "ows:Output/ows:Transparent", query.isTransparent() ? "TRUE" : "FALSE");
		domWriter.setElementValue(rootElt, "ows:Output/ows:Size/ows:Width", Integer.toString(query.getWidth()));
		domWriter.setElementValue(rootElt, "ows:Output/ows:Size/ows:Height", Integer.toString(query.getHeight()));
		domWriter.setElementValue(rootElt, "ows:Exceptions", query.getExceptionType());
		
		return rootElt;
	}
	
	
	/**
	 * Create comma separated list of bbox coordinates
	 * @param query
	 * @return
	 */
	protected String getBboxList(WMSQuery query)
	{
		StringBuffer buff = new StringBuffer();
		
		buff.append(query.getBbox().getMinX());
		buff.append("," + query.getBbox().getMinY());
		buff.append(" " + query.getBbox().getMaxX());
		buff.append("," + query.getBbox().getMaxY());
		
		return buff.toString();
	}
	
	
	/**
	 * Create comma separated layer list for GET request
	 * @param query
	 * @return
	 */
	protected String getLayerList(WMSQuery query)
	{
		StringBuffer buff = new StringBuffer();
		
		int layerCount = query.getLayers().size();
		for (int i=0; i<layerCount; i++)
		{
			buff.append(query.getLayers().get(i));
			
			if (i != layerCount-1)
				buff.append(',');				
		}
		
		return buff.toString();
	}
	
	
	/**
	 * Create comma separated style list for GET request
	 * @param query
	 * @return
	 */
	protected String createStyleList(WMSQuery query)
	{
		StringBuffer buff = new StringBuffer();
		
		int styleCount = query.getStyles().size();
		for (int i=0; i<styleCount; i++)
		{
			buff.append(query.getStyles().get(i));
			
			if (i != styleCount-1)
				buff.append(',');				
		}
		
		return buff.toString();
	}
}