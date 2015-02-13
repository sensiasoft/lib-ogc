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

import net.opengis.swe.v20.DataEncoding;
import org.w3c.dom.Element;
import net.opengis.swe.v20.DataComponent;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.SweEncodedMessageProcessor;
import org.vast.ows.swe.SWERequestReader;
import org.vast.swe.SWEData;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;


/**
* <p>
* Provides methods to parse SPS tasking requests for version 2.0
* </p>
*
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Feb, 29 2008
**/
public abstract class TaskingRequestReaderV20<RequestType extends TaskingRequest> extends SWERequestReader<RequestType> implements SweEncodedMessageProcessor
{
	protected SPSCommonReaderV20 commonReader = new SPSCommonReaderV20();
	protected DateTimeFormat timeFormat = new DateTimeFormat();
	protected DataComponent taskingParamStructure;
	
	
	@Override
	public void setSweCommonStructure(DataComponent taskingParamStructure, DataEncoding resultEncoding)
	{
		this.taskingParamStructure = taskingParamStructure;
	}


	public void readTaskingRequestXML(DOMHelper dom, Element requestElt, RequestType request) throws OWSException
	{
		try
		{
			// do common stuffs like version, request name and service type
			readCommonXML(dom, requestElt, request);

			// sensor ID
			String sensorID = dom.getElementValue(requestElt, "procedure");
			request.setProcedureID(sensorID);
			
			// tasking parameters
			try
			{
				Element taskingParamsElt = dom.getElement(requestElt, "taskingParameters/*");
				if (taskingParamsElt != null && taskingParamStructure != null)
				{
					// parse data into a SWEData object
					SWEData taskingParams = commonReader.readSWEData(dom, taskingParamsElt, taskingParamStructure);
					request.setParameters(taskingParams);
				}
			}
			catch (Exception e)
			{
				String code = SPSException.invalid_param_code;
				String locator = "taskingParameters";
				throw new SPSException(code, locator, invalidXML + ": " + e.getMessage(), e);
			}
			
			// latest response time
			String isoDate = dom.getElementValue(requestElt, "latestResponseTime");
			try
            {
                if (isoDate != null)
                {
                	DateTime latestResponseTime = new DateTime(timeFormat.parseIso(isoDate));
                	request.setLatestResponseTime(latestResponseTime);
                }
            }
            catch (Exception e)
            {
                String code = SPSException.invalid_param_code;
                String locator = "latestResponseTime";
                throw new SPSException(code, locator, invalidValue + locator + ": " + isoDate, e);
            }
		}
		catch (SPSException e)
		{
		    throw e;
		}
		catch (Exception e)
		{
			throw new SPSException(invalidXML + ": " + e.getMessage(), e);
		}
	}


	/**
	 * Checks that GetFeasibility mandatory parameters are present
	 * @param request
	 * @throws OWSException
	 */
	protected void checkParameters(RequestType request, OWSExceptionReport report) throws OWSException
	{
		// check common params + generate exception
		checkParameters(request, report, SPSUtils.SPS);

		// Check that sensorID is present
		if (request.getProcedureID() == null)
			report.add(new SPSException(SPSException.missing_param_code, "procedure"));

		// Check that tasking parameters are present
		if (request.getParameters() == null)
			report.add(new SPSException(SPSException.missing_param_code, "taskingParameters"));
		
		report.process();
	}
}