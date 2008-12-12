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
import java.io.OutputStream;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.util.PostRequestFilter;
import org.vast.ows.wps.DescribeProcessRequest;
import org.vast.ows.wps.ExecuteProcessRequest;
import org.vast.ows.wps.WPSDescribeProcessResponseSerializer;
import org.vast.ows.wps.WPSException;
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
public abstract class WPSServlet extends OWSServlet
{
    private static final long serialVersionUID = 6940984824581209178L;
    protected OWSUtils owsUtils = new OWSUtils();
    
	// Table of SOS handlers: 1 for each observation offering
	protected Hashtable<String, WPSHandler> dataSetHandlers = new Hashtable<String, WPSHandler>();
	
	// Table of <ProcedureId, SensorMLUrl> - 1 per procedure 
	protected Hashtable<String, String> sensorMLUrls = new Hashtable<String, String>();
	    
	
	protected void processQuery(GetCapabilitiesRequest query) throws Exception
    {
	    sendCapabilities("ALL", query.getResponseStream());
    }
	    
	
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void processQuery(DescribeProcessRequest query) throws Exception
	{
		if (query.getOffering() == null)
			throw new WPSException("A DescribeProcess WPS request must specify an offeringId argument");
			
		sendDescribeProcess("ALL", query.getResponseStream());
	}
	
	
	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */
	protected void processQuery(ExecuteProcessRequest query) throws Exception
	{
		if (query.getOffering() == null)
			throw new WPSException("An ExecuteProcess WPS request must specify an offeringId argument");
		
		if (query.getResponseMode() == null)
			throw new WPSException("An ExecuteProcessRequest WPS request must specify a response mode");
		
		if (query.getResponseFormat() == null)
			throw new WPSException("An ExecuteProcess WPS request must specify a response mode");
		
					
		WPSHandler handler = dataSetHandlers.get(query.getOffering());

		if (handler != null)
		{
			checkQueryFormat(query, capsHelper);
            handler.executeProcess(query);				
		}
		else
			throw new WPSException("Data for observation " + query.getOffering() + " is unavailable on this server");
	}

	
	/**
	 * Checks if selected format is available in this offering
	 * @param query
	 * @param capsReader
	 * @throws WPSException
	 */
	protected void checkQueryFormat(ExecuteProcessRequest query, DOMHelper capsReader) throws WPSException
	{
		Element offeringElt = getOffering(query.getOffering(), capsReader);
        
    	NodeList formatElts =  capsReader.getElements(offeringElt, "responseFormat");
        
        // check query format vs. each supported format in the capabilities
        int listSize = formatElts.getLength();
		for (int i=0; i<listSize; i++)
		{
			String formatString = capsReader.getElementValue((Element)formatElts.item(i), "");
			
			if (formatString.equals(query.getResponseFormat()))
				return;
		}
		
		throw new WPSException("Format - " + query.getResponseFormat() + " - is not available for offering " + query.getOffering());
	}
	
	
    /**
     * Lookup offering element in caps using ID
     * @param obsID
     * @param capsReader
     * @return
     * @throws WPSException
     */
	protected Element getOffering(String obsID, DOMHelper capsReader) throws WPSException
	{
		Element obsElt = null;
		
		try
		{
			obsElt = capsReader.getXmlDocument().getElementByID(obsID);
		}
		catch (Exception e)
		{
			throw new WPSException("No observation with ID " + obsID + " available on this server");
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
			//  get request URL
			String requestURL = req.getRequestURL().toString();   
			//  get query string
			String queryString = req.getQueryString();            
            if (queryString == null)
                sendErrorMessage(resp.getOutputStream(), "Invalid request");
            
            // parse query arguments
            logger.info("GET REQUEST: " + queryString + " from IP " + req.getRemoteAddr());
            query = (OWSRequest)owsUtils.readURLQuery(queryString, "WPS");
            
            // setup response stream
            resp.setContentType("text/xml");
            query.setResponse(resp);
			query.setGetServer(requestURL);
			
			if (query instanceof GetCapabilitiesRequest)
	        	processQuery((GetCapabilitiesRequest)query);
	        else if (query instanceof DescribeProcessRequest)
	        	processQuery((DescribeProcessRequest)query);
	        else if (query instanceof ExecuteProcessRequest)
	        	processQuery((ExecuteProcessRequest)query);
		}
		catch (WPSException e)
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
                resp.getOutputStream().flush();
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
		
		//  get request URL
		String requestURL = req.getRequestURL().toString();
		
		try
		{
			InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
            DOMHelper dom = new DOMHelper(xmlRequest, false);
            query = (OWSRequest)owsUtils.readXMLQuery(dom, dom.getBaseElement());
            
            // setup response stream
            resp.setContentType("text/xml");
            query.setResponse(resp);
			query.setPostServer(requestURL);
			
			if (query instanceof GetCapabilitiesRequest)
	        	processQuery((GetCapabilitiesRequest)query);
	        else if (query instanceof DescribeProcessRequest)
	        	processQuery((DescribeProcessRequest)query);
	        else if (query instanceof ExecuteProcessRequest)
	        	processQuery((ExecuteProcessRequest)query);
		}
        catch (WPSException e)
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

	
	/**
	 * Adds a new handler or the given observation ID
	 * @param obsID
	 * @param handlerClass
	 */
	public void addDataSetHandler(String obsID, WPSHandler handlerClass)
	{
		dataSetHandlers.put(obsID, handlerClass);
	}


	public void removeDataSetHandler(String dataSetID)
	{
		dataSetHandlers.remove(dataSetID);
	}
	
	
	/**
	 * Sends the whole DescribeProcess document in response to DescribeProcess request
	 * @param resp
	 * @throws IOException
	 */
	protected void sendDescribeProcess(String section, OutputStream resp)
	{
		try
		{
            WPSDescribeProcessResponseSerializer serializer = new WPSDescribeProcessResponseSerializer();
            serializer.setOutputByteStream(resp);
            serializer.serialize(capsHelper.getRootElement());
		}
		catch (IOException e)
		{
		}
	}
	
}
