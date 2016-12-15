/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p>
 * Provides methods to parse a KVP SPS ConnectTasking request and create
 * a ConnectTaskingRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Dec 14, 2016
 * */
public class ConnectTaskingReaderV20 extends SWERequestReader<ConnectTaskingRequest>
{
	
	public ConnectTaskingReaderV20()
	{
	}
	
	
	@Override
	public ConnectTaskingRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		ConnectTaskingRequest request = new ConnectTaskingRequest();
		readCommonQueryArguments(queryParameters, request);
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
		
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
			
            // template argument
            if (argName.equalsIgnoreCase("template"))
                request.setTemplateId(argValue);
			
			else
				throw new SPSException(invalidKVP + ": Unknown Argument " + argName);
		}

		checkParameters(request, report);
		return request;
	}
	
	
	@Override
	public ConnectTaskingRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
	    throw new SPSException(noXML + "SPS 2.0 ConnectTasking");
	}
	
	
	/**
     * Checks that DescribeSensor mandatory parameters are present
     * @param request
     * @throws OWSException
     */
	protected void checkParameters(ConnectTaskingRequest request, OWSExceptionReport report) throws OWSException
	{
		// check common params + generate exception
		super.checkParameters(request, report);
		
		// need template ID
        if (request.getTemplateId() == null)
            report.add(new OWSException(OWSException.missing_param_code, "template"));
		
		report.process();
	}
}