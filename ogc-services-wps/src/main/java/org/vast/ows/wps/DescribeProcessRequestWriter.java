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

package org.vast.ows.wps;

import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML WPS DescribeProcess
 * request based on values contained in a DescribeProcessRequest
 * object
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Dec 2, 2008
 * */
public class DescribeProcessRequestWriter extends AbstractRequestWriter<DescribeProcessRequest>
{

	public DescribeProcessRequestWriter()
	{
	}

	@Override
	public String buildURLQuery(DescribeProcessRequest request) throws OWSException
	{
	    StringBuilder urlBuff = new StringBuilder(request.getGetServer());
        addCommonArgs(urlBuff, request);

		// offering
		urlBuff.append("&requestFormat=" + request.getRequestFormat());
		
        String url = urlBuff.toString();
        url = url.replaceAll(" ","%20");
		return url;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, DescribeProcessRequest request) throws OWSException
	{
		dom.addUserPrefix("wps", OGCRegistry.getNamespaceURI(OWSUtils.WPS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OWSUtils.OGC));
		
		// root element
		Element rootElt = dom.createElement("wps:DescribeProcess");
		addCommonXML(dom, rootElt, request);

		// offering
		dom.setElementValue(rootElt, "wps:requestFormat", request.getRequestFormat());

		return rootElt;
	}
	

	public Element buildXMLQuery(DescribeProcessRequest request) throws OWSException
	{
		DOMHelper dom = new DOMHelper();
		return buildXMLQuery(dom, request);
	}
    
}