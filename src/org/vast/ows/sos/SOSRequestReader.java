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
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLTimeReader;
import org.vast.ows.util.TimeInfo;
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
    protected final static String invalidGet = "Invalid SOS GET Request";
    protected final static String invalidPost = "Invalid SOS POST Request";
    protected GMLTimeReader timeReader;
    
    
    public SOSRequestReader()
	{
        timeReader = new GMLTimeReader();
	}

	
	@Override
	public SOSQuery readGetRequest(String queryString) throws SOSException
	{
		SOSQuery query = new SOSQuery();
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
				throw new SOSException(invalidGet);
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
				TimeInfo timeInfo = query.getTime();
				String[] timeRange = argValue.split("/");
				double now = System.currentTimeMillis() / 1000;
                
				try
                {
                    // parse start time
                    if (timeRange[0].equalsIgnoreCase("now"))
                    {
                        timeInfo.setBeginNow(true);
                        timeInfo.setStartTime(now);
                    }
                    else
                        timeInfo.setStartTime(DateTimeFormat.parseIso(timeRange[0]));
                    
                    // parse stop time if present
                    if (timeRange.length > 1)
                    {
                        if (timeRange[1].equalsIgnoreCase("now"))
                        {
                            timeInfo.setEndNow(true);
                            timeInfo.setStopTime(now);
                        }
                        else
                            timeInfo.setStopTime(DateTimeFormat.parseIso(timeRange[1]));
                    }
                    
                    // parse step time if present
                    if (timeRange.length > 2)
                    {
                        timeInfo.setTimeStep(Double.parseDouble(timeRange[2]));
                    }
                }
                catch (ParseException e)
                {
                    throw new SOSException(invalidGet + ": Invalid Time Argument: " + argValue);
                }
                
                // copy start to stop
                if (timeRange.length == 1)
                    timeInfo.setStopTime(timeInfo.getStartTime());                					
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
			else if (argName.equalsIgnoreCase("sensorID"))
			{
				String[] sensorList = argValue.split(",");
				query.getProcedures().clear();					
				for (int i=0; i<sensorList.length; i++)
					query.getProcedures().add(sensorList[i]);
			}

			else
				throw new SOSException(invalidGet + ": Unknown Argument " + argName);
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
		try
        {
            readEventTime(domReader, requestElt, query);
        }
        catch (GMLException e)
        {
            throw new SOSException(invalidPost + ": " + e.getMessage());
        }
		
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
	
	
    /**
     * Reads a GML time
     * TODO readEventTime method description
     * @param domReader
     * @param requestElt
     * @param query
     * @throws GMLException
     */
	protected void readEventTime(DOMReader domReader, Element requestElt, SOSQuery query) throws GMLException
	{
		//TODO support other time operators...
        Element timeElt = domReader.getElement(requestElt, "eventTime/*/*");
		TimeInfo time = timeReader.readTimePrimitive(domReader, timeElt);
        query.setTime(time);
	}
}