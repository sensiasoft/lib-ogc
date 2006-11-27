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

import org.vast.ows.OWSQuery;
import org.vast.ows.OWSRequestWriter;
import org.w3c.dom.*;
import org.vast.io.xml.DOMWriter;


/**
 * <p><b>Title:</b><br/>
 * WCS POST/GET Request Builder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a GET or POST WCS request based
 * on values contained in a WCSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public class WCSRequestWriter extends OWSRequestWriter
{
    final static int GROUP1 = 1;
    final static int GROUP2 = 2;    
	int versionGroup = GROUP1;
	

	public WCSRequestWriter()
	{	
	}
	
	
	public void setVersionGroup(String version) throws WCSException
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
            throw new WCSException("WCS version " + version + " not supported");
    }

	
	@Override
	public String buildGetRequest(OWSQuery owsQuery) throws WCSException
	{
		WCSQuery query = (WCSQuery)owsQuery;
		//setVersionGroup(query.getVersion());
		
		StringBuffer urlBuff = new StringBuffer(query.getGetServer());
		
        urlBuff.append("service=WCS");
        urlBuff.append("&version=" + query.getVersion());
        urlBuff.append("&request=GetCoverage");          
        urlBuff.append("&layers=" + query.getLayer());
        urlBuff.append("&crs=" + query.getSrs());
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
	public Element buildRequestXML(OWSQuery owsQuery, DOMWriter domWriter) throws WCSException
	{
		WCSQuery query = (WCSQuery)owsQuery;
		domWriter.addNS("http://www.opengis.net/ows", "ows");
		domWriter.addNS("http://www.opengis.net/ogc", "ogc");
		domWriter.addNS("http://www.opengis.net/gml", "gml");
		domWriter.addNS("http://www.opengis.net/sld", "sld");
		
		// root element
		Element rootElt = domWriter.createElement("ows:GetCoverage");
		query.getBbox();
		
		return rootElt;
	}
	
	
	/**
	 * Create comma separated list of bbox coordinates
	 * @param query
	 * @return
	 */
	protected String getBboxList(WCSQuery query)
	{
		StringBuffer buff = new StringBuffer();
		
		buff.append(query.getBbox().getMinX());
		buff.append("," + query.getBbox().getMinY());
		buff.append(" " + query.getBbox().getMaxX());
		buff.append("," + query.getBbox().getMaxY());
		
		return buff.toString();
	}
}