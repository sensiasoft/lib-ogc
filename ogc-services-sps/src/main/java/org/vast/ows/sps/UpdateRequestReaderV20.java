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

import java.util.Map;
import org.w3c.dom.Element;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.xml.DOMHelper;


/**
* <p>
* Provides methods to parse an XML SPS Update
* request and create an UpdateRequest object for version 2.0
* </p>
*
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Dec, 10 2008
**/
public class UpdateRequestReaderV20 extends TaskingRequestReaderV20<UpdateRequest>
{
		
	@Override
	public UpdateRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		throw new SPSException(noKVP + "SPS 2.0 Update");
	}


	@Override
	public UpdateRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		UpdateRequest request = new UpdateRequest();
		readTaskingRequestXML(dom, requestElt, request);
		
		// task ID
		String taskID = dom.getElementValue(requestElt, "targetTask");
		request.setTaskID(taskID);
		
		checkParameters(request, new OWSExceptionReport());
		
		return request;		
	}
	
	
	@Override
	protected void checkParameters(UpdateRequest request, OWSExceptionReport report) throws OWSException
	{
		super.checkParameters(request, report);

		// Check that taskID is present
		if (request.getTaskID() == null)
			report.add(new SPSException(SPSException.missing_param_code, "targetTask"));
		
		report.process();
	}
}