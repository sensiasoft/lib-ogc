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

import java.util.StringTokenizer;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ows.*;
import org.vast.ows.gml.GMLEnvelopeReader;
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLTimeReader;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;
import org.vast.ows.sos.SOSQuery.ResponseMode;


/**
 * <p><b>Title:</b><br/>
 * SOS Request Reader v0.0.31
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST SOS request and
 * create an SOSQuery object for OWS4 version 0.0.31
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 4, 2005
 * @version 1.0
 * @deprecated Oct 10, 2007 use GetObservationReaderVXX, DescribeSensorReaderVXX...
 */
public class SOSRequestReaderV031 extends SOSRequestReader
{
    protected GMLTimeReader timeReader;
    protected GMLEnvelopeReader bboxReader;
    
    
    public SOSRequestReaderV031()
	{
        timeReader = new GMLTimeReader();
        bboxReader = new GMLEnvelopeReader();
	}

	
	@Override
	public SOSQuery readURLQuery(String queryString) throws OWSException
	{
		SOSQuery request = new SOSQuery();
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

			// offering argument
			else if (argName.equalsIgnoreCase("offering"))
			{
				request.setOffering(argValue);
			}

			// format argument
			else if (argName.equalsIgnoreCase("format"))
			{
				request.setFormat(argValue);
			}
            
			// responseMode argument
            else if (argName.equalsIgnoreCase("responseMode"))
            {
                parseResponseMode(argValue, request);
            }
            
			// resultModel argument
            else if (argName.equalsIgnoreCase("resultModel"))
            {
                request.setResultModel(argValue);
            }
			
			// time
			else if (argName.equalsIgnoreCase("time"))
			{
			    TimeInfo time = parseTimeArg(argValue);
			    request.setTime(time);
			}
            
            // bbox
            else if (argName.equalsIgnoreCase("bbox"))
            {
            	Bbox bbox = parseBboxArg(argValue);
                request.setBbox(bbox);
            }
			
			// observables
			else if (argName.equalsIgnoreCase("observables") ||
					argName.equalsIgnoreCase("observedProperty"))
			{
				String[] obsList = argValue.split(",");
				request.getObservables().clear();					
				for (int i=0; i<obsList.length; i++)
					request.getObservables().add(obsList[i]);
			}
			
			// procedures
			else if (argName.equalsIgnoreCase("procedures") || argName.equalsIgnoreCase("SensorId"))
			{
				String[] sensorList = argValue.split(",");
				request.getProcedures().clear();					
				for (int i=0; i<sensorList.length; i++)
					request.getProcedures().add(sensorList[i]);
			}

			else
				throw new SOSException(invalidKVP + ": Unknown Argument " + argName);
		}

		return request;
	}
	
	
	@Override
	public SOSQuery readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
        String opName = requestElt.getLocalName();
		SOSQuery query;
		
		if (opName.equalsIgnoreCase("GetObservation"))
			query = readGetObservationXML(dom, requestElt);
		
		else if (opName.equalsIgnoreCase("DescribeSensor"))
			query = readDescribeSensorXML(dom, requestElt);
		
		else throw new SOSException("Operation " + opName + " not supported");
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, query);
		
		return query;
	}
	
	
	/**
	 * Reads a GetObservation XML request and fill up the SOSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @return
	 * @throws SOSException
	 */
	protected SOSQuery readGetObservationXML(DOMHelper dom, Element requestElt) throws SOSException
	{
		SOSQuery query = new SOSQuery();
		
		// read main request parameters
		query.setOffering(dom.getElementValue(requestElt, "offering"));
		String resFormat = dom.getElementValue(requestElt, "resultFormat");
		if(resFormat == null)
			resFormat = dom.getElementValue(requestElt, "responseFormat");
		query.setFormat(resFormat);
        query.setResultModel(dom.getElementValue(requestElt, "resultModel"));
        
        // read responseMode
        String mode = dom.getElementValue(requestElt, "responseMode");
        parseResponseMode(mode, query);

		// read time instant or period
		try
        {
            readTemporalOps(dom, requestElt, query);
        }
        catch (GMLException e)
        {
            throw new SOSException(invalidXML + ": " + e.getMessage());
        }
        
        // read bbox
        try
        {
            readSpatialOps(dom, requestElt, query);
        }
        catch (GMLException e)
        {
            throw new SOSException(invalidXML + ": " + e.getMessage());
        }
		
		// read observables
		NodeList obsList = dom.getElements(requestElt, "observedProperty");
		for (int i = 0; i < obsList.getLength(); i++)
		{
			String val = dom.getElementValue((Element)obsList.item(i), "");
			query.getObservables().add(val);
		}
		
		// read procedures
		NodeList procList = dom.getElements(requestElt, "procedure");
		for (int i = 0; i < procList.getLength(); i++)
		{
			String val = dom.getElementValue((Element)procList.item(i), "");
			query.getProcedures().add(val);
		}

		return query;
	}
	
	
	/**
	 * Reads a DescribeSensor XML request and fill up the SOSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @return
	 * @throws SOSException
	 */
	protected SOSQuery readDescribeSensorXML(DOMHelper dom, Element requestElt) throws SOSException
	{
		SOSQuery query = new SOSQuery();
		
		NodeList sensorList = dom.getElements("sensorId");
		for (int i = 0; i < sensorList.getLength(); i++)
		{
			String val = dom.getElementValue((Element)sensorList.item(i), "");
			query.getProcedures().add(val);
		}

		return query;
	}
    
    
    protected void parseResponseMode(String mode, SOSQuery query) throws SOSException
    {
        if (mode == null)
            query.setResponseMode(null);
        else if (mode.equalsIgnoreCase("inline"))
            query.setResponseMode(ResponseMode.INLINE);
        else if (mode.equalsIgnoreCase("attached"))
            query.setResponseMode(ResponseMode.ATTACHED);
        else if (mode.equalsIgnoreCase("out-of-band"))
            query.setResponseMode(ResponseMode.OUT_OF_BAND);
        else if (mode.equalsIgnoreCase("resultTemplate"))
            query.setResponseMode(ResponseMode.RESULT_TEMPLATE);
        else if (mode.equalsIgnoreCase("resultOnly"))
            query.setResponseMode(ResponseMode.RESULT_ONLY);
        else
            throw new SOSException("Invalid response mode: " + mode);
    }
	
	
    /**
     * Reads the temporalOps section
     * TODO support other time operators... (currently supporting only During)
     * @param dom
     * @param requestElt
     * @param query
     * @throws GMLException
     */
	protected void readTemporalOps(DOMHelper dom, Element requestElt, SOSQuery query) throws GMLException
	{
        Element timeElt = dom.getElement(requestElt, "eventTime/*/*");
        
        if (timeElt != null)
        {
    		TimeInfo time = timeReader.readTimePrimitive(dom, timeElt);
            query.setTime(time);
        }
	}
    
    
    /**
     * Reads the spatialOps section
     * TODO support more spatial operators... (currently supporting only BBOX)
     * @param dom
     * @param requestElt
     * @param query
     * @throws GMLException
     */
    protected void readSpatialOps(DOMHelper dom, Element requestElt, SOSQuery query) throws GMLException
    {
        Element envelopeElt = dom.getElement(requestElt, "featureOfInterest/BBOX/Envelope");
        
        if (envelopeElt != null)
        {
            Bbox bbox = bboxReader.readEnvelope(dom, envelopeElt);
            query.setBbox(bbox);
        }
    }
}