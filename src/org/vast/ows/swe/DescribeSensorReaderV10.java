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

package org.vast.ows.swe;

import java.util.StringTokenizer;
import org.vast.util.TimeInfo;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ogc.gml.GMLException;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ows.*;
import org.vast.ows.sos.SOSException;


/**
 * <p><b>Title:</b><br/>
 * SOS DescribeSensor Request Reader v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or XML SOS DescribeSensor
 * request and create a DescribeSensorRequest object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 10, 2007
 * @version 1.0
 */
public class DescribeSensorReaderV10 extends AbstractRequestReader<DescribeSensorRequest>
{
	protected GMLTimeReader timeReader;
	
	
	public DescribeSensorReaderV10()
	{
        timeReader = new GMLTimeReader();
	}
	
	
	@Override
	public DescribeSensorRequest readURLQuery(String queryString) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		DescribeSensorRequest request = new DescribeSensorRequest();
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
				throw new SOSException(invalidKVP);
			}
			
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
				TimeInfo time = parseTimeArg(argValue);
            	request.setTime(time);
			}
			
			// procedure
			else if (argName.equalsIgnoreCase("procedure"))
			{
				request.setProcedure(argValue);
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
		request.setProcedure(procedure);
		
		// time
		try
		{
			Element timeElt = dom.getElement(requestElt, "time/*");
			if (timeElt != null)
			{
				TimeInfo time = timeReader.readTimePrimitive(dom, timeElt);
				request.setTime(time);
			}
		}
		catch (GMLException e)
		{
			report.add(new OWSException(OWSException.invalid_param_code, "TIME"));
		}
		
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
		super.checkParameters(request, report, OWSUtils.SOS);
		
		// need procedure
		if (request.getProcedure() == null)
			report.add(new OWSException(OWSException.missing_param_code, "PROCEDURE"));
		
		// need format
		if (request.getFormat() == null)
			report.add(new OWSException(OWSException.missing_param_code, "RESPONSE_FORMAT"));
		
		report.process();
	}
}