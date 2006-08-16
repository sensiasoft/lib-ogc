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

package org.vast.ows.server;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;
import java.text.*;

import org.vast.ows.sos.*;
import org.vast.ows.util.PostRequestFilter;
import org.vast.util.*;
import org.w3c.dom.*;
import org.vast.io.xml.*;


/**
 * <p><b>Title:</b><br/> SOSServlet</p>
 *
 * <p><b>Description:</b><br/>
 * Base abstract class for implementing SOS servlets
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public abstract class SOSServlet extends OWSServlet
{
	// Table of SOS handlers: 1 for each ObservationSet
	protected Hashtable<String, SOSHandler> dataSetHandlers = new Hashtable<String, SOSHandler>();


	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void processQuery(SOSQuery query) throws SOSException
	{
		// GetCapabilities request
		if (query.getRequest().equalsIgnoreCase("GetCapabilities"))
		{
			sendCapabilities("ALL", query.getResponseStream());
		}
		
		// DescribeSensor request
		else if (query.getRequest().equalsIgnoreCase("DescribeSensor"))
		{
			String sensorId = query.getProcedures().get(0);
			if (sensorId == null)
				throw new SOSException("A DescribeSensor request must specify a sensorId argument");
			
			DOMReader capsReader = new DOMReader(this.capsDoc);
			sendSensorMLDocument(sensorId, capsReader, query.getResponseStream());
		}
		
		// GetObservation request
		else if (query.getRequest().equalsIgnoreCase("GetObservation"))
		{
			DOMReader capsReader = new DOMReader(this.capsDoc);
			
			if (query.getOffering() == null)
				throw new SOSException("A GetObservation SOS request must specify an offeringId argument");
			
			//if (query.observables.isEmpty())
			//	throw new SOSException("An SOS request must contain at least one observable ID");
			
			if (query.getFormat() == null)
				throw new SOSException("A GetObservation SOS request must specify a format argument");
			
						
			SOSHandler handler = dataSetHandlers.get(query.getOffering());

			if (handler != null)
			{
				// check query parameters
				checkQueryObservables(query, capsReader);
				checkQueryFormat(query, capsReader);
				checkQueryTime(query, capsReader);
				
				// then retrieve observation data
				handler.getObservation(query);				
			}
			else
				throw new SOSException("Data for observation " + query.getOffering() + " is unavailable on this server");				
		}
		
		// Unrecognized request type
		else
		{
			throw new SOSException(query.getRequest() + " request unsupported on this server: Use GetCapabilities, GetObservation, DescribeSensor"); 
		}
	}
	
	
	/**
	 * Checks if all selected observables are available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryObservables(SOSQuery query, DOMReader capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);		
		NodeList observableElts = capsReader.getElements(offeringElt, "observedProperty");
		boolean ok;
		
		for (int j=0; j<query.getObservables().size(); j++)
		{
			ok = false;
			
			for (int i=0; i<observableElts.getLength(); i++)
			{
				String idString = capsReader.getAttributeValue((Element)observableElts.item(i), "*/id");
				
				if (idString.equals(query.getObservables().get(j)))
				{
					ok = true;
					break;
				}
			}
			
			if (!ok)
				throw new SOSException("Observed Property - " + query.getObservables().get(j) + " - is not available for offering " + query.getOffering());
		}
	}
	
	
	/**
	 * Checks if selected format is available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryFormat(SOSQuery query, DOMReader capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);		
		NodeList formatElts = capsReader.getElements(offeringElt, "resultFormat");
		
		for (int i=0; i<formatElts.getLength(); i++)
		{
			String formatString = capsReader.getElementValue((Element)formatElts.item(i), "");
			
			if (formatString.equals(query.getFormat()))
				return;
		}
		
		throw new SOSException("Format - " + query.getFormat() + " - is not available for offering " + query.getOffering());
	}
	
	
	/**
	 * Checks if requested time is available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryTime(SOSQuery query, DOMReader capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);
				
		// check if requested time period is valid
		String startAtt = capsReader.getAttributeValue(offeringElt, "eventTime/TimePeriod/beginPosition/indeterminatePosition");
		String isoStartTime = capsReader.getElementValue(offeringElt, "eventTime/TimePeriod/beginPosition");
		String stopAtt = capsReader.getAttributeValue(offeringElt, "eventTime/TimePeriod/endPosition/indeterminatePosition");
		String isoStopTime = capsReader.getElementValue(offeringElt, "eventTime/TimePeriod/endPosition");
		
		double startTime = 0.0;
		double stopTime = 0.0;
		
		try
		{
			if (startAtt != null)
			{
				if (startAtt.equals("now"))
					startTime = (new DateTime()).getJulianTime()-1;
			}
			else
				startTime = DateTimeFormat.parseIso(isoStartTime);
			
			if (stopAtt != null)
			{
				if (stopAtt.equals("now"))
                {
					stopTime = (new DateTime()).getJulianTime();
                    isoStopTime = DateTimeFormat.formatIso(stopTime, 0); 
                }
			}
			else
				stopTime = DateTimeFormat.parseIso(isoStopTime);
		}
		catch (ParseException e)
		{
			throw new SOSException("Internal Error: Invalid Time Period in Capabilities Document.");
		}
		
		if (query.getTime().getStartTime() > query.getTime().getStopTime())
			throw new SOSException("The requested period must begin before the it ends");
		
		if ((query.getTime().getStartTime() < startTime) || (query.getTime().getStopTime() > stopTime))
			throw new SOSException("The requested period must be contained in " + isoStartTime + "/" + isoStopTime);
	}
	
	
	protected Element getOffering(String obsID, DOMReader capsReader) throws SOSException
	{
		Element obsElt = null;
		
		try
		{
			obsElt = capsReader.getXmlFragment().getParentDocument().getElementByID(obsID);
		}
		catch (Exception e)
		{
			throw new SOSException("No observation with ID " + obsID + " available on this server");
		}
		
		return obsElt;
	}
	
	
	protected void sendSensorMLDocument(String sensorId, DOMReader capsReader, OutputStream resp) throws SOSException
	{
		throw new SOSException("SensorML description for " + sensorId + " not available");
	}


	/**
	 * Parse and process HTTP GET request
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		SOSQuery query = null;
		
		try
		{
			//  parse query arguments
			String queryString = req.getQueryString();		
			this.log("GET REQUEST: " + queryString + " from IP " + req.getRemoteAddr() + " (" + req.getRemoteHost() + ")");
			
			SOSRequestReader reader = new SOSRequestReader();
			query = reader.readGetRequest(queryString);
			
			query.setResponseStream(resp.getOutputStream());
			this.processQuery(query);			
		}
		catch (SOSException e)
		{
			sendErrorMessage(query.getResponseStream(), e.getMessage());
		}
		catch (Exception e)
		{
			throw new ServletException("Problem while processing the request", e);
		}
        finally
        {
            try
            {
                resp.getOutputStream().close();
            }
            catch (IOException e)
            {
                throw new ServletException(e);
            }
        }
	}


	/**
	 * Parse and process HTTP POST request
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		SOSQuery queryInfo = null;
		this.log("POST REQUEST from IP " + req.getRemoteAddr() + " (" + req.getRemoteHost() + ")");
		
		try
		{
			InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
			DOMReader dom = new DOMReader(xmlRequest, false);
			
			SOSRequestReader reader = new SOSRequestReader();
			queryInfo = reader.readRequestXML(dom, dom.getRootElement());

			queryInfo.setResponseStream(resp.getOutputStream());
			this.processQuery(queryInfo);
		}
		catch (SOSException e)
		{
			sendErrorMessage(queryInfo.getResponseStream(), e.getMessage());
		}
		catch (DOMReaderException e)
		{
			sendErrorMessage(queryInfo.getResponseStream(), "Invalid XML request. Please check request format");
		}
		catch (Exception e)
		{
			throw new ServletException("Problem while processing the request", e);
		}
        finally
        {
            try
            {
                resp.getOutputStream().close();
            }
            catch (IOException e)
            {
                throw new ServletException(e);
            }
        }
	}


	/**
	 * Adds a new handler or the given observation ID
	 * @param obsID
	 * @param handlerClass
	 */
	public void addDataSetHandler(String obsID, SOSHandler handlerClass)
	{
		dataSetHandlers.put(obsID, handlerClass);
	}


	public void removeDataSetHandler(String dataSetID)
	{
		dataSetHandlers.remove(dataSetID);
	}
}
