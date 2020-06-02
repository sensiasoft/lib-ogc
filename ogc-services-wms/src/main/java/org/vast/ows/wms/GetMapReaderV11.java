/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wms;

import org.w3c.dom.*;
import org.vast.ows.*;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * Provides methods to parse a KVP or XML GetMap request and
 * create a GetMapRequest object for version 1.1
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 10, 2007
 * */
public class GetMapReaderV11 extends GetMapReaderV10
{
	
	
	public GetMapReaderV11()
	{
		this.owsVersion = OWSException.VERSION_11;
	}

	
	@Override
	public GetMapRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		GetMapRequest request = new GetMapRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// layer names and styles
		NodeList layerElts = dom.getElements(requestElt, "StyledLayerDescriptor/NamedLayer");
		for (int i=0; i<layerElts.getLength(); i++)
		{
			Element elt = (Element)layerElts.item(i);
			request.getLayers().add(dom.getElementValue(elt, "Name"));
			String styleName = dom.getElementValue(elt, "NamedStyle/Name");
			if (styleName == null) styleName = "";
			request.getStyles().add(styleName);
		}
		
		// bounding box
		String bboxText = dom.getElementValue(requestElt, "BoundingBox/coordinates");
		request.setBbox(parseBboxArg(bboxText));
		request.setSrs(dom.getAttributeValue(requestElt, "BoundingBox/srsName"));
		
		// output parameters
		request.setFormat(dom.getElementValue(requestElt, "Output/Format"));
		String transparency = dom.getElementValue(requestElt, "Output/Transparent");
		request.setTransparent(transparency.equalsIgnoreCase("true") ? true : false);
		request.setWidth(Integer.parseInt(dom.getElementValue(requestElt, "Output/Size/Width")));
		request.setHeight(Integer.parseInt(dom.getElementValue(requestElt, "Output/Size/Height")));
		request.setExceptionType(dom.getElementValue(requestElt, "Exceptions"));
		
		this.checkParameters(request, new OWSExceptionReport(OWSException.VERSION_11));
		return request;
	}
}