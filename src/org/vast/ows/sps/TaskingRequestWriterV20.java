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
import org.vast.cdm.common.CDMException;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.sweCommon.SWEData;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;


/**
* <p><b>Title:</b><br/>
* SPS Tasking Request Writer v2.0
* </p>
*
* <p><b>Description:</b><br/>
* Provides methods to generate SPS tasking requests for version 2.0
* </p>
*
* <p>Copyright (c) 2008</p>
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Feb, 28 2008
* @version 1.0
*/
public abstract class TaskingRequestWriterV20<RequestType extends TaskingRequest> extends AbstractRequestWriter<RequestType>
{
	protected SPSCommonWriterV20 commonWriter = new SPSCommonWriterV20();
	

	/**
	 * XML Request
	 */
	@Override
	public Element buildXMLQuery(DOMHelper dom, RequestType request) throws OWSException
	{
		try
		{
			dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(SPSUtils.SPS, request.getVersion()));

			// root element
			Element rootElt = dom.createElement("sps:" + request.getOperation());
			addCommonXML(dom, rootElt, request);

			// sensorID
			if (request.getSensorID() != null)
				dom.setElementValue(rootElt, "sps:sensorID", request.getSensorID());
							
			// tasking parameters
			SWEData taskingParams = request.getParameters();
			if (taskingParams != null)
			{
				Element taskingParamsElt = dom.addElement(rootElt, "sps:taskingParameters");
				commonWriter.writeSWEData(dom, taskingParamsElt, taskingParams);
			}
			
			// latest response time
			DateTime latestResponseTime = request.getLatestResponseTime();
			if (latestResponseTime != null)
			{
				String isoTime = DateTimeFormat.formatIso(latestResponseTime.getJulianTime(), 0);
				dom.setElementValue(rootElt, "sps:latestResponseTime", isoTime);
			}

			return rootElt;
		}
		catch (CDMException e)
		{
			throw new SPSException(e);
		}
	}
}