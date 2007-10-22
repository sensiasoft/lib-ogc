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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLTimeReader;
import org.vast.ows.sos.DescribeSensorRequest;
import org.vast.ows.sos.GetObservationRequest;
import org.vast.ows.sos.SOSException;
import org.vast.ows.util.PostRequestFilter;
import org.vast.ows.util.TimeInfo;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


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
	// Table of <ProcedureId, SensorMLUrl> - 1 per procedure 
	protected Hashtable<String, String> sensorMLUrls = new Hashtable<String, String>();
	protected OWSUtils owsUtils = new OWSUtils();
    
	protected void processQuery(GetCapabilitiesRequest query) throws Exception
    {
	    sendCapabilities("ALL", query.getResponseStream());
    }
	    
	protected void processQuery(DescribeSensorRequest query) throws Exception
	{
		String sensorId = query.getProcedure();
		if (sensorId == null)
			throw new SOSException("A DescribeSensor request must specify a sensorId argument");
		
		//  Check for SensorId in SensorMLUrl hashtable
		String smlUrl = sensorMLUrls.get(sensorId);
		if(smlUrl == null)
			throw new SOSException("SensorML description for " + sensorId + " not available");
		DOMHelper dom = new DOMHelper(smlUrl, false);				
        dom.serialize(dom.getBaseElement(), query.getResponseStream() , null);		
	}
	
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void processQuery(GetObservationRequest query) throws Exception
	{
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
			checkQueryObservables(query, capsHelper);
            checkQueryProcedures(query, capsHelper);
			checkQueryFormat(query, capsHelper);
			checkQueryTime(query, capsHelper);
			
			// then retrieve observation data
            handler.getObservation(query);				
		}
		else
			throw new SOSException("Data for observation " + query.getOffering() + " is unavailable on this server");
	}
	
	
	/**
	 * Checks if all selected observables are available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryObservables(GetObservationRequest query, DOMHelper capsReader) throws SOSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);		
		NodeList observableElts = capsReader.getElements(offeringElt, "observedProperty");
		boolean ok;
		
		for (int j=0; j<query.getObservables().size(); j++)
		{
			ok = false;
			
			for (int i=0; i<observableElts.getLength(); i++)
			{
				String idString =  capsReader.getAttributeValue((Element)observableElts.item(i), "href");
				if(idString == null)
                    idString = capsReader.getAttributeValue((Element)observableElts.item(i), "*/id");
				
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
     * Checks if all selected procedures are available in this offering
     * @param query
     * @param capsReader
     * @throws SOSException
     */
    protected void checkQueryProcedures(GetObservationRequest query, DOMHelper capsReader) throws SOSException
    {
        Element offeringElt = getOffering(query.getOffering(), capsReader);     
        NodeList procedureElts = capsReader.getElements(offeringElt, "procedure");
        boolean ok;
        
        for (int j=0; j<query.getProcedures().size(); j++)
        {
            ok = false;
            
            for (int i=0; i<procedureElts.getLength(); i++)
            {
                String idString =  capsReader.getAttributeValue((Element)procedureElts.item(i), "href");
                if(idString == null)
                    idString = capsReader.getAttributeValue((Element)procedureElts.item(i), "*/id");
                
                if (idString.equals(query.getProcedures().get(j)))
                {
                    ok = true;
                    break;
                }
            }
            
            if (!ok)
                throw new SOSException("Procedure - " + query.getProcedures().get(j) + " - is not available for offering " + query.getOffering());
        }
    }
	
	
	/**
	 * Checks if selected format is available in this offering
	 * @param query
	 * @param capsReader
	 * @throws SOSException
	 */
	protected void checkQueryFormat(GetObservationRequest query, DOMHelper capsReader) throws SOSException
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
	protected void checkQueryTime(GetObservationRequest query, DOMHelper dom) throws SOSException
	{
        // make sure startTime <= stopTime
        if (query.getTime().getStartTime() > query.getTime().getStopTime())
            throw new SOSException("The requested period must begin before the it ends");
        
        // loads capabilities advertised time
        Element offeringElt = getOffering(query.getOffering(), dom);
        Element timeElt = dom.getElement(offeringElt, "eventTime/*");
        if(timeElt == null)
            throw new SOSException("Internal Error: No Time Present in Capabilities Document");        
        GMLTimeReader timeReader = new GMLTimeReader();
        ArrayList<TimeInfo> timeList = new ArrayList<TimeInfo>();
        
        try
        {
            // case of time aggregate
            if (dom.existElement(timeElt, "member"))
            {
                NodeList timeElts = dom.getElements(timeElt, "member/*");                
                for(int i = 0; i < timeElts.getLength(); i++)
                {
                    Element timeMemberElt = (Element)timeElts.item(i);
                    TimeInfo time = timeReader.readTimePrimitive(dom, timeMemberElt);
                    timeList.add(time);
                }
            }            
            // case of single instant/period/grid
            else
            {
                TimeInfo time = timeReader.readTimePrimitive(dom, timeElt);
                timeList.add(time);
            }
        }
        catch (GMLException e)
        {
            throw new SOSException("Internal Error: Invalid Time in Capabilities Document", e);
        }
        
        // check that request time is within one of the advertised periods
        boolean ok = false;
        for (int i=0; i<timeList.size(); i++)
        {
            if (timeList.get(i).contains(query.getTime()))
            {
                ok = true;
                break;
            }
        }
        
        // send error if request time is not contained in any of the advertised periods 
        if (!ok)
        {
            // construct error message
            StringBuffer buf = new StringBuffer("The requested period must be contained in ");
            if (timeList.size() > 1)
                buf.append("one of the following periods:\n");
            
            for (int i=0; i<timeList.size(); i++)
            {
                buf.append(timeList.get(i).getIsoString(0));
                if (i<timeList.size()-1)
                    buf.append(',');
            }
            
            throw new SOSException(buf.toString());
        }            
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
	
	/**
	 * Parse and process HTTP GET request
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		OWSRequest query = null;
		
		try
		{
			//  get query string
			String queryString = req.getQueryString();            
            if (queryString == null)
                sendErrorMessage(resp.getOutputStream(), "Invalid request");
            
            // parse query arguments
            logger.info("GET REQUEST: " + queryString + " from IP " + req.getRemoteAddr());
            query = (OWSRequest)owsUtils.readURLQuery(queryString, "SOS");			
			query.setResponseStream(resp.getOutputStream());
			
			if (query instanceof GetCapabilitiesRequest)
	        	processQuery((GetCapabilitiesRequest)query);
	        else if (query instanceof DescribeSensorRequest)
	        	processQuery((DescribeSensorRequest)query);
	        else if (query instanceof GetObservationRequest)
	        	processQuery((GetObservationRequest)query);
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
        catch (OWSException e)
        {
            try
            {
                sendErrorMessage(resp.getOutputStream(), "Invalid request or unrecognized version");
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
		OWSRequest query = null;
		logger.info("POST REQUEST from IP " + req.getRemoteAddr());
		
		try
		{
			InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
            DOMHelper dom = new DOMHelper(xmlRequest, false);
            query = (OWSRequest)owsUtils.readXMLQuery(dom, dom.getBaseElement());
			query.setResponseStream(resp.getOutputStream());
			
			if (query instanceof GetCapabilitiesRequest)
	        	processQuery((GetCapabilitiesRequest)query);
	        else if (query instanceof DescribeSensorRequest)
	        	processQuery((DescribeSensorRequest)query);
	        else if (query instanceof GetObservationRequest)
	        	processQuery((GetObservationRequest)query);
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
        catch (OWSException e)
        {
            try
            {
                sendErrorMessage(resp.getOutputStream(), "Invalid request or unrecognized version");
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

	@Override
	/**
	 * Load the Caps doc, and build a hashtable of SensorID<=>SensorMLUrl mappings
	 * NOTE : Any subclasses that override this method should call super.updateCaps() first,
	 *        or the SensorML hashtable won't be built. TC
	 * @param capFile - the Capabilites file
	 */
	public synchronized void updateCapabilities(InputStream capFile)
	{
		super.updateCapabilities(capFile);
		//  Populate the sensorMLUrls hashtable
		NodeList procedureElts = 
			capsHelper.getElements("Contents/ObservationOfferingList/ObservationOffering/procedure");
		if(procedureElts == null)
			return;
		//  Clear the hashtable, just in case this gets called multiple times 
		sensorMLUrls.clear();
		for (int i=0; i<procedureElts.getLength(); i++) {
			Element procElt = (Element)procedureElts.item(i);
            String idString =  capsHelper.getAttributeValue(procElt, "href");
            if(idString == null)  // skip this proc if no href
            	continue;
            if(!procElt.hasChildNodes())  // skip if no child elements
            	continue;
            //  I put the child check in b/c the following statement was generating a NPE
            //  in the guts of the XML code when procElt had no children- TC
            Element infoElt = capsHelper.getElement(procElt, "internalInfo");
            if(infoElt == null)
            	continue;
            String docUrl = capsHelper.getAttributeValue(infoElt, "Parameters/sensorMLUrl/href");
            if(docUrl == null)
            	continue;
            sensorMLUrls.put(idString, docUrl);
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
