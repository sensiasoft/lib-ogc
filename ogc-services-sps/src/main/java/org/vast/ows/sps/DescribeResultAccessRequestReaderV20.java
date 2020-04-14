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
import org.vast.xml.DOMHelper;


/**
* <p>
* Provides methods to parse a KVP or XML SPS DescribeResultAccess
* request and create a DescribeResultAccess object for version 2.0
* </p>
*
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Mar, 04 2008
**/
public class DescribeResultAccessRequestReaderV20 extends SWERequestReader<DescribeResultAccessRequest>
{

	@Override
	public DescribeResultAccessRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		DescribeResultAccessRequest request = new DescribeResultAccessRequest();
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

			// sensor ID
			else if (argName.equalsIgnoreCase("procedure"))
			{
				request.setProcedureID(argValue);
			}
			
			// ID
			else if (argName.equalsIgnoreCase("task"))
			{
				request.setTaskID(argValue);
			}

			else
				throw new OWSException(invalidKVP + ": Unknown Argument " + argName);
		}

		checkParameters(request, new OWSExceptionReport());
		return request;
	}


	@Override
	public DescribeResultAccessRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		DescribeResultAccessRequest request = new DescribeResultAccessRequest();

		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);

		// task ID
		String taskID = dom.getElementValue(requestElt, "target/task");
		request.setTaskID(taskID);
		
		// procedure ID
		String procedureID = dom.getElementValue(requestElt, "target/procedure");
		request.setProcedureID(procedureID);
		
		checkParameters(request, new OWSExceptionReport());
		return request;
	}


	/**
	 * Checks that DescribeResultAccess mandatory parameters are present
	 * @param request
	 * @throws OWSException
	 */
	protected void checkParameters(DescribeResultAccessRequest request, OWSExceptionReport report) throws OWSException
	{
		// check common params + generate exception
		checkParameters(request, report, SPSUtils.SPS);

		// Check that at least one ID is present
		if (request.getProcedureID() == null && request.getTaskID() == null)
		{
			report.add(new OWSException(OWSException.missing_param_code, "task OR procedure"));
		}

		report.process();
	}
}