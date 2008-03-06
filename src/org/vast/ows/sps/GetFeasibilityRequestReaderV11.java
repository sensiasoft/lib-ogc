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

import org.w3c.dom.Element;
import org.vast.cdm.common.DataComponent;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.sweCommon.SweData;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;


/**
* <p><b>Title:</b><br/>
* SPS GetFeasibility Request Reader v1.1
* </p>
*
* <p><b>Description:</b><br/>
* Provides methods to parse an XML SPS GetFeasibility
* request and create a GetFeasibilityRequest object for version 1.1
* </p>
*
* <p>Copyright (c) 2008</p>
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Feb, 29 2008
* @version 1.0
*/
public class GetFeasibilityRequestReaderV11 extends ParameterizedRequestReader<GetFeasibilityRequest>
{
	protected SPSCommonReaderV11 commonReader = new SPSCommonReaderV11();
	protected DataComponent taskingParamStructure;
	protected DataComponent additionalParamStructure;
	
	
	public void setParamStructure(DataComponent taskingParamStructure, DataComponent additionalParamStructure)
	{
		this.taskingParamStructure = taskingParamStructure;
		this.additionalParamStructure = additionalParamStructure;
	}
	
	
	@Override
	public GetFeasibilityRequest readURLQuery(String queryString) throws OWSException
	{
		throw new SPSException(noKVP + "SPS 1.1 GetFeasibility");
	}


	@Override
	public GetFeasibilityRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		try
		{
			GetFeasibilityRequest request = new GetFeasibilityRequest();

			// do common stuffs like version, request name and service type
			readCommonXML(dom, requestElt, request);

			// sensor ID
			String sensorID = dom.getElementValue(requestElt, "sensorID");
			request.setSensorID(sensorID);
			
			// tasking parameters
			Element taskingParamsElt = dom.getElement(requestElt, "taskingParameters");
			if (taskingParamsElt != null && taskingParamStructure != null)
			{
				// parse data into a SWEData object
				SweData taskingParams = commonReader.readSWEData(dom, taskingParamsElt, taskingParamStructure);
				request.setTaskingParameters(taskingParams);
			}
			
			// additional parameters
			Element extParamsElt = dom.getElement(requestElt, "additionalParameters");
			if (extParamsElt != null && additionalParamStructure != null)
			{
				// parse data into a SWEData object
				SweData extParams = commonReader.readSWEData(dom, extParamsElt, additionalParamStructure);
				request.setAdditionalParameters(extParams);
			}
			
			// time frame
			String isoDate = dom.getElementValue(requestElt, "timeFrame");
			if (isoDate != null)
			{
				DateTime timeFrame = new DateTime(DateTimeFormat.parseIso(isoDate));
				request.setTimeFrame(timeFrame);
			}
			
			checkParameters(request, new OWSExceptionReport());
			return request;
		}
		catch (Exception e)
		{
			throw new SPSException(invalidXML + ": " + e.getMessage());
		}
	}


	/**
	 * Checks that GetFeasibility mandatory parameters are present
	 * @param request
	 * @throws OWSException
	 */
	protected void checkParameters(GetFeasibilityRequest request, OWSExceptionReport report) throws OWSException
	{
		// check common params + generate exception
		checkParameters(request, report, "SPS");

		// Check that sensorID is present
		if (request.getSensorID() == null)
			report.add(new SPSException(SPSException.missing_param_code, "SensorID"));

		// Check that tasking parameters are present
		if (request.getTaskingParameters() == null)
			report.add(new SPSException(SPSException.missing_param_code, "TaskingParameters"));
		
		report.process();
	}
}