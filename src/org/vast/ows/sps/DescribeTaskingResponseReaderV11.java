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
import org.vast.ows.AbstractResponseReader;
import org.vast.ows.OWSException;
import org.vast.sweCommon.SweComponentReaderV11;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * DescribeFeasibility Response Reader v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DescribeFeasibilityResponseReaderV11 type description
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 25 2008
 * @version 1.0
 */
public class DescribeTaskingResponseReaderV11 extends AbstractResponseReader<DescribeTaskingResponse>
{
	protected SweComponentReaderV11 componentReader = new SweComponentReaderV11();
	
	
	public DescribeTaskingResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		try
		{
			DescribeTaskingResponse response = new DescribeTaskingResponse();
			response.setVersion("1.1");
			Element paramsElt;
			DataComponent sweParams;
			
			// common tasking parameters
			paramsElt = dom.getElement(responseElt, "CommonTaskingParameters");
			if (paramsElt != null)
			{
				sweParams = componentReader.readComponentProperty(dom, paramsElt);
				response.setCommonTaskingParameters(sweParams);
			}
			
			// additional feasibility parameters
			paramsElt = dom.getElement(responseElt, "AdditionalFeasibilitytParameters");
			if (paramsElt != null)
			{
				sweParams = componentReader.readComponentProperty(dom, paramsElt);
				response.setAdditionalFeasibilityParameters(sweParams);
			}
			
			// additional submit parameters
			paramsElt = dom.getElement(responseElt, "AdditionalSubmitParameters");
			if (paramsElt != null)
			{
				sweParams = componentReader.readComponentProperty(dom, paramsElt);
				response.setAdditionalSubmitParameters(sweParams);
			}
			
			// Feasibility Study parameters
			paramsElt = dom.getElement(responseElt, "FeasibilityStudyParameters");
			if (paramsElt != null)
			{
				sweParams = componentReader.readComponentProperty(dom, paramsElt);
				response.setFeasibilityStudyParameters(sweParams);
			}
			
			// Progress Report parameters
			paramsElt = dom.getElement(responseElt, "ProgressReportParameters");
			if (paramsElt != null)
			{
				sweParams = componentReader.readComponentProperty(dom, paramsElt);
				response.setProgressReportParameters(sweParams);
			}
			
			return response;
		}
		catch (Exception e)
		{
			throw new SPSException(e);
		}
	}
	
}
