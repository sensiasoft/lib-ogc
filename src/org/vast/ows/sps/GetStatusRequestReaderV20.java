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
import java.util.StringTokenizer;
import org.vast.ows.AbstractRequestReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.util.DateTime;
import org.vast.xml.DOMHelper;


/**
* <p><b>Title:</b><br/>
* SPS GetStatus Request Reader v2.0
* </p>
*
* <p><b>Description:</b><br/>
* Provides methods to parse a KVP or XML SPS GetStatus
* request and create a GetStatusRequest object for version 2.0
* </p>
*
* <p>Copyright (c) 2008</p>
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Feb, 258 2008
* @version 1.0
*/
public class GetStatusRequestReaderV20 extends AbstractRequestReader<GetStatusRequest>
{

	@Override
	public GetStatusRequest readURLQuery(String queryString) throws OWSException
	{
		GetStatusRequest request = new GetStatusRequest();
		StringTokenizer st = new StringTokenizer(queryString, "&");

		while (st.hasMoreTokens())
		{
			String argName = null;
			String argValue = null;
			String nextArg = st.nextToken();

			// separate argument name and value
			try
			{
				int sepIndex = nextArg.indexOf('=');
				argName = nextArg.substring(0, sepIndex);
				argValue = nextArg.substring(sepIndex + 1);
			}
			catch (IndexOutOfBoundsException e)
			{
				throw new OWSException(invalidKVP);
			}

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
				throw new SPSException(invalidKVP + ": Unknown Argument " + argName);
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

		// taskID
		String taskID = dom.getElementValue(requestElt, "taskID");
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
		checkParameters(request, report, "SPS");

		// Check that taskID is present
		if (request.getTaskID() == null)
			report.add(new OWSException(OWSException.missing_param_code, "TaskID"));

		report.process();
	}
}