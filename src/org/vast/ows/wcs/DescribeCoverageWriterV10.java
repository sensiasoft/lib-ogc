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

package org.vast.ows.wcs;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * DescribeCoverage Request Builder v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML DescribeCoverage request based
 * on values contained in a DescribeCoverage object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Sep 21, 2007
 * @version 1.0
 */
public class DescribeCoverageWriterV10 extends AbstractRequestWriter<DescribeCoverageRequest>
{

	@Override
	public String buildURLQuery(DescribeCoverageRequest request) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(request.getGetServer());
        addCommonArgs(urlBuff, request);
        
        // add all requested coverage names
        if (!request.getCoverages().isEmpty())
        {
			urlBuff.append("&COVERAGE=");
			int listSize = request.getCoverages().size();
        	for (int i=0; i<listSize; i++)
        	{
				urlBuff.append(request.getCoverages().get(i));
				if (i < listSize-1)
					urlBuff.append(",");
        	}
        }        
		
        // replace spaces
		String url = urlBuff.toString();
		url = url.replaceAll(" ","%20");		
		return url;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, DescribeCoverageRequest request) throws OWSException
	{
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OGCRegistry.WCS));
		
		// root element
		Element rootElt = dom.createElement("DescribeCoverage");
		addCommonXML(dom, rootElt, request);
		
		// add all requested coverage names
		for (int i=0; i<request.getCoverages().size(); i++)
			dom.setElementValue(rootElt, "Coverage", request.getCoverages().get(i));
		
		return rootElt;
	}
}