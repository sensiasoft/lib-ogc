/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ows.*;
import org.vast.ows.sos.SOSException;
import org.vast.ows.swe.DescribeSensorRequest;


/**
 * <p>
 * Provides methods to parse a KVP or XML SOS DescribeSensor
 * request and create a DescribeSensorRequest object for version 1.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 10, 2007
 * */
public class DescribeSensorReaderV10 extends AbstractRequestReader<DescribeSensorRequest>
{
	
	
	public DescribeSensorReaderV10()
	{
	}
	
	
	@Override
	public DescribeSensorRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		DescribeSensorRequest request = new DescribeSensorRequest();
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
        
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
			
			// service ID
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

			// format argument
			else if (argName.equalsIgnoreCase("format"))
			{
				request.setFormat(argValue);
			}
			
			// time
			else if (argName.equalsIgnoreCase("time"))
			{
				TimeExtent time = parseTimeArg(argValue);
            	request.setTime(time);
			}
			
			// procedure
			else if (argName.equalsIgnoreCase("procedure"))
			{
				request.setProcedureID(argValue);
			}
			
			else
				throw new SOSException(invalidKVP + ": Unknown Argument " + argName);
		}

		checkParameters(request, report);
		return request;
	}
	
	
	@Override
	public DescribeSensorRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		DescribeSensorRequest request = new DescribeSensorRequest();        
        
        // do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
        // procedure
		String procedure = dom.getElementValue(requestElt, "procedure");
		request.setProcedureID(procedure);
		
		// format
		String format = dom.getAttributeValue(requestElt, "@outputFormat");
		request.setFormat(format);
		
		this.checkParameters(request, report); 
		return request;
	}
	
	
	/**
     * Checks that DescribeSensor mandatory parameters are present
     * @param request
     * @throws OWSException
     */
	protected void checkParameters(DescribeSensorRequest request, OWSExceptionReport report) throws OWSException
	{
		// check common params + generate exception
		super.checkParameters(request, report);
		
		// need procedure
		if (request.getProcedureID() == null)
			report.add(new OWSException(OWSException.missing_param_code, "PROCEDURE"));
		
		// need format
		if (request.getProcedureID() == null)
			report.add(new OWSException(OWSException.missing_param_code, "FORMAT"));
		
		report.process();
	}
}