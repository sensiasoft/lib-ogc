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

import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.ows.AbstractResponseReader;
import org.vast.ows.OWSException;
import org.vast.sweCommon.SweComponentReaderV1;
import org.vast.sweCommon.SweEncodingReaderV1;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * DescribeFeasibility Response Reader V1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DescribeFeasibilityResponseReaderV11 type description
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date 27 déc. 07
 * @version 1.0
 */
public class DescribeGetFeasibilityResponseReaderV11 extends AbstractResponseReader<DescribeGetFeasibilityResponse>
{
	protected SweComponentReaderV1 componentReader = new SweComponentReaderV1();
	protected SweEncodingReaderV1 encodingReader = new SweEncodingReaderV1();
	
	
	public DescribeGetFeasibilityResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		try
		{
			DescribeGetFeasibilityResponse response = new DescribeGetFeasibilityResponse();
			response.setVersion("1.1");
			
			Element inputDataElt = dom.getElement("input/DataBlockDefinition");
			Element inputComponentsElt = dom.getElement(inputDataElt, "components");
			Element inputEncodingElt = dom.getElement(inputDataElt, "encoding");
			DataComponent inputDataComponents = componentReader.readComponentProperty(dom, inputComponentsElt);
			DataEncoding inputDataEncoding = encodingReader.readEncodingProperty(dom, inputEncodingElt);
						
			Element outputDataElt = dom.getElement("output/DataBlockDefinition");
			Element outputComponentsElt = dom.getElement(outputDataElt, "components");
			Element outputEncodingElt = dom.getElement(outputDataElt, "encoding");
			DataComponent outputDataComponents = componentReader.readComponentProperty(dom, outputComponentsElt);
			DataEncoding outputDataEncoding = encodingReader.readEncodingProperty(dom, outputEncodingElt);
			
			response.setInputDataComponents(inputDataComponents);
			response.setInputDataEncoding(inputDataEncoding);
			response.setOutputDataComponents(outputDataComponents);
			response.setOutputDataEncoding(outputDataEncoding);
			
			return response;
		}
		catch (Exception e)
		{
			throw new OWSException(e);
		}
	}
	
}
