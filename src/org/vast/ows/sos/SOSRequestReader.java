/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.text.ParseException;
import java.util.StringTokenizer;

import org.vast.io.xml.DOMReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ows.*;
import org.vast.ows.util.TimeInfo;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;


/**
 * <p><b>Title:</b><br/>
 * SOS Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST SOS request and
 * create an SOSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 4, 2005
 * @version 1.0
 */
public class SOSRequestReader extends OWSRequestReader
{
	
	public SOSRequestReader()
	{	
	}

	
	@Override
	public SOSQuery readGetRequest(String queryString) throws SOSException
	{
		SOSQuery query = new SOSQuery();
		String invalidQuery = "Invalid SOS GET request";
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
				throw new SOSException(invalidQuery);
			}
			
			// service ID
			if (argName.equalsIgnoreCase("service"))
			{
				query.setService(argValue);
			}
			
			// service version
			else if (argName.equalsIgnoreCase("version"))
			{
				query.setVersion(argValue);
			}

			// request argument
			else if (argName.equalsIgnoreCase("request"))
			{
				query.setRequest(argValue);
			}

			// offering argument
			else if (argName.equalsIgnoreCase("offering"))
			{
				query.setOffering(argValue);
			}

			// format argument
			else if (argName.equalsIgnoreCase("format"))
			{
				query.setFormat(argValue);
			}
			
			// time
			else if (argName.equalsIgnoreCase("time"))
			{
				TimeInfo time = query.getTime();
				String[] timeRange = argValue.split("/");
				
				if (timeRange[0].equalsIgnoreCase("now"))
				{
					time.setStartTime((new DateTime()).getJulianTime());
					time.setStopTime(time.getStartTime());
				}
				else
				{
					try
					{
						time.setStartTime(DateTimeFormat.parseIso(timeRange[0]));
						
						if (timeRange.length == 1)
							time.setStopTime(time.getStartTime());
						else
							time.setStopTime(DateTimeFormat.parseIso(timeRange[1]));
					}
					catch (ParseException e)
					{
						throw new SOSException(invalidQuery + ": Invalid time range " + timeRange[0] + "/" + timeRange[1]);
					}
				}					
			}
			
			// observables
			else if (argName.equalsIgnoreCase("observables"))
			{
				String[] obsList = argValue.split(",");
				query.getObservables().clear();					
				for (int i=0; i<obsList.length; i++)
					query.getObservables().add(obsList[i]);
			}
			
			// sensor Ids
			//else if (argName.equalsIgnoreCase("sensors"))
			//  modded to accept SensorID- that's what it is in the current spec.  TC 6/3/06
			else if (argName.equalsIgnoreCase("sensors") || argName.equalsIgnoreCase("SensorID"))
			{
				String[] sensorList = argValue.split(",");
				query.getProcedures().clear();					
				for (int i=0; i<sensorList.length; i++)
					query.getProcedures().add(sensorList[i]);
			}

			//else
			//	throw new SOSException(invalidQuery + ": Unknown argument " + argName);
		}

		return query;
	}
	
	
	@Override
	public SOSQuery readRequestXML(DOMReader domReader, Element requestElt) throws SOSException
	{
		String opName = requestElt.getLocalName();
		SOSQuery query;
		
		if (opName.equalsIgnoreCase("GetCapabilities"))
		{
			query = new SOSQuery();
			readGetCapabilitiesXML(domReader, requestElt, query);
		}
		
		else if (opName.equalsIgnoreCase("GetObservation"))
			query = readGetObservationXML(domReader, requestElt);
		
		else if (opName.equalsIgnoreCase("DescribeSensor"))
			query = readDescribeSensorXML(domReader, requestElt);
		
		else throw new SOSException("Operation " + opName + " not supported");
		
		// do common stuffs like version, request name and service type
		readCommonXML(domReader, requestElt, query);
		
		return query;
	}
	
	
	/**
	 * Reads a GetObservation XML request and fill up the SOSQuery accordingly
	 * @param domReader
	 * @param requestElt
	 * @return
	 * @throws SOSException
	 */
	protected SOSQuery readGetObservationXML(DOMReader domReader, Element requestElt) throws SOSException
	{
		SOSQuery query = new SOSQuery();
		
		// read main request parameters
		query.setOffering(domReader.getElementValue(requestElt, "offering"));
		query.setFormat(domReader.getElementValue(requestElt, "resultFormat"));

		// read time values
		readEventTime(domReader, requestElt, query);
		
		// read observables
		NodeList obsList = domReader.getElements(requestElt, "observedProperty");
		for (int i = 0; i < obsList.getLength(); i++)
		{
			String val = domReader.getElementValue((Element)obsList.item(i), "");
			query.getObservables().add(val);
		}
		
		// read procedures
		NodeList procList = domReader.getElements(requestElt, "procedure");
		for (int i = 0; i < procList.getLength(); i++)
		{
			String val = domReader.getElementValue((Element)procList.item(i), "");
			query.getProcedures().add(val);
		}

		return query;
	}
	
	
	/**
	 * Reads a DescribeSensor XML request and fill up the SOSQuery accordingly
	 * @param domReader
	 * @param requestElt
	 * @return
	 * @throws SOSException
	 */
	protected SOSQuery readDescribeSensorXML(DOMReader domReader, Element requestElt) throws SOSException
	{
		SOSQuery query = new SOSQuery();
		
		NodeList sensorList = domReader.getElements("sensorId");
		for (int i = 0; i < sensorList.getLength(); i++)
		{
			String val = domReader.getElementValue((Element)sensorList.item(i), "");
			query.getProcedures().add(val);
		}

		return query;
	}
	
	
	protected void readEventTime(DOMReader domReader, Element requestElt, SOSQuery query) throws SOSException
	{
		try
		{
			Element eventTimeElt = domReader.getElement(requestElt, "eventTime/*");
			TimeInfo time = query.getTime();
			
			// TODO support more time operators
			// right now i am assuming ogc:During for period and ogc:TContains for instant 
			
			if (domReader.existElement(eventTimeElt, "TimeInstant"))
			{
				String timeString = domReader.getElementValue(eventTimeElt, "TimeInstant/timePosition");
				String timeAtt = domReader.getAttributeValue(eventTimeElt, "TimeInstant/timePosition/indeterminatePosition");
				
				if (timeAtt != null)
				{
					if (timeAtt.equals("now"))
						time.setStartTime((new DateTime()).getJulianTime());
				}
				else
					time.setStartTime(DateTimeFormat.parseIso(timeString));
				
				time.setStopTime(time.getStartTime());
			}
			else if (domReader.existElement(eventTimeElt, "TimePeriod"))
			{
				String startTimeString = domReader.getElementValue(eventTimeElt, "TimePeriod/beginPosition");
				String startAtt = domReader.getAttributeValue(eventTimeElt, "TimePeriod/beginPosition/indeterminatePosition");
				String stopTimeString = domReader.getElementValue(eventTimeElt, "TimePeriod/endPosition");
				String stopAtt = domReader.getAttributeValue(eventTimeElt, "TimePeriod/endPosition/indeterminatePosition");
				
				if (startAtt != null)
				{
					if (startAtt.equals("now"))
						time.setStartTime((new DateTime()).getJulianTime());
				}
				else
					time.setStartTime(DateTimeFormat.parseIso(startTimeString));
				
				if (stopAtt != null)
				{
					if (stopAtt.equals("now"))
						time.setStopTime((new DateTime()).getJulianTime());
				}
				else
					time.setStopTime(DateTimeFormat.parseIso(stopTimeString));
			}
		}
		catch (ParseException e)
		{
			throw new SOSException("Error while parsing SOS request time section", e);
		}
	}
}