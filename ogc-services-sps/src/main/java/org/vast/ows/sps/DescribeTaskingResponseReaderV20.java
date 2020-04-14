/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import net.opengis.swe.v20.DataComponent;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseReader;
import org.vast.swe.SWEUtils;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Used to read a version 2.0 SPS DescribeTasking Response into
 * a DescribeTaskingResponse object
 * </p>
 *
 * @author Alex Robin
 * @date Feb, 25 2008
 * */
public class DescribeTaskingResponseReaderV20 extends SWEResponseReader<DescribeTaskingResponse>
{
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
	
	
    @Override
    public DescribeTaskingResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
	    DescribeTaskingResponse response = new DescribeTaskingResponse();
	    
	    try
		{
			response.setVersion("2.0");
			Element paramsElt;
			
			// common tasking parameters
			paramsElt = dom.getElement(responseElt, "taskingParameters/*");
			DataComponent taskingParams = sweUtils.readComponent(dom, paramsElt);
			String name = dom.getAttributeValue(responseElt, "taskingParameters/@name");
			taskingParams.setName(name);
			response.setTaskingParameters(taskingParams);
			
			// read extensions
            readXMLExtensions(dom, responseElt, response);
            
			return response;
		}
		catch (Exception e)
		{
			throw new SPSException(READ_ERROR_MSG + response.getMessageType(), e);
		}
	}
}
