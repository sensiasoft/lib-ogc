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
   Philippe Merigot <philippe.merigot@spotimage.fr>

******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.w3c.dom.Element;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.swe.SWERequestReader;
import org.vast.util.DateTime;
import org.vast.xml.DOMHelper;


/**
* <p>
* Provides methods to parse a KVP or XML SPS GetStatus
* request and create a GetStatusRequest object for version 2.0
* </p>
*
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Feb, 258 2008
**/
public class GetStatusRequestReaderV20 extends SWERequestReader<GetStatusRequest>
{

	@Override
	public GetStatusRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		GetStatusRequest request = new GetStatusRequest();
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
        
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();

			// service type
			if (argName.equalsIgnoreCase("service"))
			{
				request.setService(argValue);
			}

			// service version
			else if (argName.equalsIgnoreCase("version"))
			{
				request.setVersion(argValue);
			}

			// request argument
			else if (argName.equalsIgnoreCase("request"))
			{
				request.setOperation(argValue);
			}

			// task ID
			else if (argName.equalsIgnoreCase("taskID"))
			{
				request.setTaskID(argValue);
			}
			
			// since
			else if (argName.equalsIgnoreCase("since"))
			{
				request.setSince(new DateTime(argValue));
			}

			else
				addKVPExtension(argName, argValue, request);
		}

		checkParameters(request, new OWSExceptionReport());
		return request;
	}


	@Override
	public GetStatusRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		GetStatusRequest request = new GetStatusRequest();

		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);

		// task
		String taskID = dom.getElementValue(requestElt, "task");
		request.setTaskID(taskID);
		
		// since
		String isoTime = dom.getElementValue(requestElt, "since");
		if (isoTime != null)
			request.setSince(new DateTime(isoTime));

		checkParameters(request, new OWSExceptionReport());
		return request;
	}


	/**
	 * Checks that GetStatus mandatory parameters are present
	 * @param request
	 * @throws OWSException
	 */
	protected void checkParameters(GetStatusRequest request, OWSExceptionReport report) throws OWSException
	{
		// check common params + generate exception
		checkParameters(request, report, SPSUtils.SPS);

		// Check that taskID is present
		if (request.getTaskID() == null)
			report.add(new OWSException(OWSException.missing_param_code, "task"));

		report.process();
	}
}