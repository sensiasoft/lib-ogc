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
import org.vast.ows.OWSUtils;
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLTimeReader;
import org.vast.ows.sos.*;
import org.vast.ows.util.PostRequestFilter;
import org.vast.ows.util.TimeInfo;
import org.w3c.dom.*;
import org.vast.xml.*;


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
	protected OWSUtils sosUtils = new OWSUtils();

    
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void processQuery(SOSQuery query) throws Exception
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
			
            DOMHelper capsReader = new DOMHelper(this.capsDoc);
			sendSensorMLDocument(sensorId, capsReader, query.getResponseStream());
		}
		
		// GetObservation request
		else if (query.getRequest().equalsIgnoreCase("GetObservation"))
		{
            DOMHelper capsReader = new DOMHelper(this.capsDoc);
			
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
	protected void checkQueryObservables(SOSQuery query, DOMHelper capsReader) throws SOSException
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
	protected void checkQueryFormat(SOSQuery query, DOMHelper capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);
        
        // get format lists from either resultFormat or responseFormat elts
		NodeList formatElts = capsReader.getElements(offeringElt, "resultFormat");
    	if(formatElts.getLength() == 0)
        {
    		formatElts =  capsReader.getElements(offeringElt, "responseFormat");
    	}
        
        // check query format vs. each supported format in the capabilities
        int listSize = formatElts.getLength();
		for (int i=0; i<listSize; i++)
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
	protected void checkQueryTime(SOSQuery query, DOMHelper capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);
        Element timeElt = capsReader.getElement(offeringElt, "eventTime/*");
        if(timeElt == null) {
        	timeElt = capsReader.getElement(offeringElt, "time/*");
        	if(timeElt == null)
        		throw new SOSException("Internal Error: Invalid Time in Capabilities Document");
        }
        TimeInfo capsTime = null;
        
        try
        {
            GMLTimeReader timeReader = new GMLTimeReader();
            capsTime = timeReader.readTimePrimitive(capsReader, timeElt);
        }
        catch (GMLException e)
        {
            throw new SOSException("Internal Error: Invalid Time in Capabilities Document", e);
        } 
        
        if (query.getTime().getStartTime() > query.getTime().getStopTime())
            throw new SOSException("The requested period must begin before the it ends");
        
		if (!capsTime.contains(query.getTime()))
            throw new SOSException("The requested period must be contained in " + capsTime.getIsoString(0));			
	}
	
	
    /**
     * Lookup offering element in caps using ID
     * @param obsID
     * @param capsReader
     * @return
     * @throws SOSException
     */
	protected Element getOffering(String obsID, DOMHelper capsReader) throws SOSException
	{
		Element obsElt = null;
		
		try
		{
			obsElt = capsReader.getXmlDocument().getElementByID(obsID);
		}
		catch (Exception e)
		{
			throw new SOSException("No observation with ID " + obsID + " available on this server");
		}
		
		return obsElt;
	}
	
	
	protected void sendSensorMLDocument(String sensorId, DOMHelper capsReader, OutputStream resp) throws SOSException
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
            logger.info("GET REQUEST: " + queryString + " from IP " + req.getRemoteAddr());
            query = (SOSQuery)sosUtils.readURLQuery(queryString);			
			query.setResponseStream(resp.getOutputStream());
			this.processQuery(query);			
		}
		catch (SOSException e)
		{
			try
            {
                sendErrorMessage(resp.getOutputStream(), e.getMessage());
            }
            catch (IOException e1)
            {
                e.printStackTrace();
            }
		}
		catch (Exception e)
		{
			throw new ServletException(internalErrorMsg, e);
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
		SOSQuery query = null;
		logger.info("POST REQUEST from IP " + req.getRemoteAddr());
		
		try
		{
			InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
            DOMHelper dom = new DOMHelper(xmlRequest, false);
            query = (SOSQuery)sosUtils.readXMLQuery(dom, dom.getBaseElement());
			query.setResponseStream(resp.getOutputStream());
			this.processQuery(query);
		}
		catch (SOSException e)
		{
            try
            {
                sendErrorMessage(resp.getOutputStream(), e.getMessage());
            }
            catch (IOException e1)
            {
                e.printStackTrace();
            }
		}
		catch (DOMHelperException e)
		{
            try
            {
                sendErrorMessage(resp.getOutputStream(), "Invalid XML request. Please check request format");
            }
            catch (IOException e1)
            {
                e.printStackTrace();
            }
		}
		catch (Exception e)
		{
			throw new ServletException(internalErrorMsg, e);
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
