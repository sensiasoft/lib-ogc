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

package org.vast.ows.swe;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ows.*;


/**
 * <p>
 * Provides methods to parse a KVP or XML SOS DescribeSensor
 * request and create a DescribeSensorRequest object for version 1.0
 * </p>
 *
 * @author Alex Robin
 * @date Oct 10, 2007
 * */
public class DescribeSensorReaderV20 extends SWERequestReader<DescribeSensorRequest>
{
	protected GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
	
	
	public DescribeSensorReaderV20()
	{
	}
	
	
	@Override
	public DescribeSensorRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		DescribeSensorRequest request = new DescribeSensorRequest();
		readCommonQueryArguments(queryParameters, request);
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
		
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
			
			// time
			if ("validTime".equalsIgnoreCase(argName))
			{
				TimeExtent time = parseTimeArg(argValue);
            	request.setTime(time);
			}
			
			// procedure
			else if ("procedure".equalsIgnoreCase(argName))
			{
				request.setProcedureID(argValue);
			}
			
			// format
			else if ("procedureDescriptionFormat".equalsIgnoreCase(argName))
			{
				request.setFormat(argValue);
			}
			
			else
				throw new OWSException(invalidKVP + ": Unknown Argument " + argName);
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
		String format = dom.getElementValue(requestElt, "procedureDescriptionFormat");
		request.setFormat(format);
		
		// time
		try
		{
			Element timeElt = dom.getElement(requestElt, "validTime/*");
			if (timeElt != null)
			{
				TimeExtent time = gmlUtils.readTimePrimitiveAsTimeExtent(dom, timeElt);
				request.setTime(time);
			}
		}
		catch (XMLReaderException e)
		{
			report.add(new OWSException(OWSException.invalid_param_code, "validTime", e));
		}
		
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
			report.add(new OWSException(OWSException.missing_param_code, "procedure"));
			
	    // need format
		if (request.getFormat() == null)
			report.add(new OWSException(OWSException.missing_param_code, "procedureDescriptionFormat"));
		
		report.process();
	}
}