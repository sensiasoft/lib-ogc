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
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.swe.SWERequestReader;
import org.vast.xml.DOMHelper;


/**
* <p><b>Title:</b><br/>
* SPS DescribeResultAccess Request Reader v2.0
* </p>
*
* <p><b>Description:</b><br/>
* Provides methods to parse a KVP or XML SPS DescribeResultAccess
* request and create a DescribeResultAccess object for version 2.0
* </p>
*
* <p>Copyright (c) 2008</p>
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Mar, 04 2008
* @version 1.0
*/
public class DescribeResultAccessRequestReaderV20 extends SWERequestReader<DescribeResultAccessRequest>
{

	@Override
	public DescribeResultAccessRequest readURLQuery(String queryString) throws OWSException
	{
		DescribeResultAccessRequest request = new DescribeResultAccessRequest();
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

			// sensor ID
			else if (argName.equalsIgnoreCase("sensorID"))
			{
				request.setSensorID(argValue);
			}
			
			// ID
			else if (argName.equalsIgnoreCase("taskID"))
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

		// task or sensor ID
		String ID = dom.getElementValue(requestElt, "identifier");
		request.setTaskID(ID);
		
		// TODO how do we know if it's a sensor identifier??
		
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
		if (request.getSensorID() == null && request.getTaskID() == null)
		{
			report.add(new OWSException(OWSException.missing_param_code, "identifier"));
		}

		report.process();
	}
}