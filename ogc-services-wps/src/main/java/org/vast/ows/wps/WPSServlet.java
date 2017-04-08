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

package org.vast.ows.wps;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.server.OWSServlet;
import org.vast.ows.util.PostRequestFilter;
import org.vast.ows.wps.DescribeProcessRequest;
import org.vast.ows.wps.ExecuteProcessRequest;
import org.vast.ows.wps.WPSException;
import org.vast.ows.wps.WPSUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;


/**
 * <p>
 * Base abstract class for implementing SOS servlets
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * */
public abstract class WPSServlet extends OWSServlet
{
    private static final long serialVersionUID = 6940984824581209178L;
    private static final Logger log = LoggerFactory.getLogger(WPSServlet.class);
    protected OWSUtils owsUtils = new OWSUtils();
	private WPSUtils wpsUtils;
	protected DOMHelper describeProcessDomHelper;
	
	
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
		if (query.getRequestFormat() == null)
			throw new WPSException("A DescribeProcess WPS request must specify an request format argument");
		if (query.getOffering() == null)
			throw new WPSException("A DescribeProcess WPS request must specify an request offering argument");

		SOAPMessage describeProcessSOAPMessage = wpsUtils.createSoapMessage(describeProcessDomHelper);
		describeProcessSOAPMessage.writeTo(query.getResponseStream());
	}


	/**
	 * Decode the query, check validity and call the right handler
	 * @param query
	 */	
	protected void processQuery(ExecuteProcessRequest query) {
		// TODO Auto-generated method stub
		
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
                resp.sendError(400, "Invalid request");
            
            // parse query arguments
            log.info("GET REQUEST: " + queryString + " from IP " + req.getRemoteAddr());
            query = (OWSRequest)owsUtils.readURLQuery(queryString, "WPS");
            
            // setup response stream
            resp.setContentType("text/xml");
            query.setHttpResponse(resp);
			query.setGetServer(requestURL);
			
			if (query instanceof GetCapabilitiesRequest)
	        	processQuery((GetCapabilitiesRequest)query);
        	else throw new ServletException("WPS accept request of content type 'text/xml' only for the" +
				"GetCapabilities request");
		}
		catch (WPSException e)
		{
			try
            {
			    resp.sendError(500, e.getMessage());
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
                resp.sendError(400, e.getMessage());
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
		log.info("POST REQUEST from IP " + req.getRemoteAddr());
		
		//  get request URL
		String requestURL = req.getRequestURL().toString();
		
		if(req.getContentType().equalsIgnoreCase("text/xml"))
		{
			try 
			{
				InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
				DOMHelper dom = new DOMHelper(xmlRequest, false);
				query = (OWSRequest)owsUtils.readXMLQuery(dom, dom.getBaseElement());
				
				// setup response stream
				resp.setContentType("text/xml");
				query.setHttpResponse(resp);
				query.setPostServer(requestURL);
				
				if (query instanceof GetCapabilitiesRequest)
					processQuery((GetCapabilitiesRequest)query);
				else throw new ServletException("WPS accepts requests of content type 'text/xml' only for the" +
						"GetCapabilities request");
			}
			
			catch (WPSException e)
			{
				try
		        {
				    resp.sendError(500, e.getMessage());
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
		            resp.sendError(400, e.getMessage());
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
		
		else if(req.getContentType().equalsIgnoreCase("application/soap+xml"))
		{

			try
			{
				InputStream soapRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));				
				query = wpsUtils.extractWPSRequest(soapRequest);

				// setup response stream
				resp.setContentType(req.getContentType());
				query.setHttpResponse(resp);
				query.setPostServer(requestURL);
			
				if (query instanceof GetCapabilitiesRequest)
					processQuery((GetCapabilitiesRequest)query);
				else if (query instanceof DescribeProcessRequest)
					processQuery((DescribeProcessRequest)query);
				else if (query instanceof DescribeProcessRequest)
					processQuery((ExecuteProcessRequest)query);
			}
			catch (WPSException e)
			{
				try
				{
				    resp.sendError(500, e.getMessage());
				}
				catch (IOException e1)
				{
					e.printStackTrace();
				}
			}
			catch (SOAPException e)
			{
				try
				{
				    resp.sendError(500, e.getMessage());
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
				    resp.sendError(400, "Invalid request or unrecognized version");
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
				    resp.sendError(400, "Invalid XML request. Please check request format");
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
	}
	
	
	@Override
    protected String getServiceType()
    {
        return "WPS";
    }	
}
