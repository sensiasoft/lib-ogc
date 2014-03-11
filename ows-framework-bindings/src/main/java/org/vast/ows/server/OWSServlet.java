/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSResponse;
import org.vast.ows.OWSUtils;
import org.vast.ows.util.PostRequestFilter;
import org.vast.util.WriterException;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;


/**
 * <p><b>Title:</b><br/> OWSServlet</p>
 *
 * <p><b>Description:</b><br/>
 * Abstract Base Class for all OWS Style Servlets
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 9, 2005
 * @version 1.0
 */
public abstract class OWSServlet extends HttpServlet
{
    private static final long serialVersionUID = 4970153267344348035L;
    protected static final String XML_MIME_TYPE = "text/xml";
    protected static final String invalidKVPRequestMsg = "Invalid KVP request. Please check your syntax";
    protected static final String invalidXMLRequestMsg = "Invalid XML request. Please check your syntax";
    protected static final String internalErrorMsg = "Internal Error while processing the request. Please contact maintenance";
    
    protected Log log = LogFactory.getLog(OWSServlet.class);
    protected String owsVersion = "1.0";
    protected OWSUtils owsUtils = new OWSUtils();
    protected DOMHelper capsHelper;
        
    
    protected abstract void handleRequest(OWSRequest request) throws Exception;
    

	protected void handleRequest(GetCapabilitiesRequest request) throws Exception
	{
	    sendCapabilities(((GetCapabilitiesRequest)request).getSection(), request.getResponseStream());
	}


	/**
	 * Sends the whole capabilities document in response to GetCapabilities request
	 * @param resp
	 * @throws IOException
	 */
	protected void sendCapabilities(String section, OutputStream resp) throws Exception
	{
	    OWSCapabilitiesSerializer serializer = new OWSCapabilitiesSerializer();
        serializer.setOutputByteStream(resp);
        serializer.serialize(capsHelper.getRootElement());
        
//      The code below is a better way to do it but we cannot remove the internalInfo elements on the fly
//	    try
//		{
//		    Transformer serializer = TransformerFactory.newInstance().newTransformer();
//		    serializer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
//		    Source input = new DOMSource(capsHelper.getRootElement());
//		    Result output = new StreamResult(resp);
//		    serializer.transform(input, output);
//		}
//		catch (Exception e)
//		{
//		}
	}
	

	public synchronized void updateCapabilities(InputStream capsFile)
    {
        try
        {
            capsHelper = new DOMHelper(capsFile, false);
        }
        catch (DOMHelperException e)
        {
            e.printStackTrace();
        }
    }
	
	
	/**
     * Parse and process HTTP GET request
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
    {
        log.info("GET REQUEST from IP " + req.getRemoteAddr());       
        processRequest(req, resp, false);
    }
    
    
	/**
     * Parse and process HTTP POST request
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
    {
        log.info("POST REQUEST from IP " + req.getRemoteAddr());
        processRequest(req, resp, true);
    }
    
    
    /**
     * Parse and process all types of requests
     * @param req
     * @param resp
     * @param post
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp, boolean post)
    {
        //  get actual request URL
        String requestURL = req.getRequestURL().toString();
        OWSRequest request = null;
        
        try
        {
            // parse request            
            try
            {
                if (post)
                {
                    InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
                    DOMHelper dom = new DOMHelper(xmlRequest, false);
                    request = owsUtils.readXMLQuery(dom, dom.getBaseElement());
                }
                else
                {
                    String queryString = req.getQueryString();
                    if (queryString == null)
                        throw new IllegalArgumentException();
                    request = owsUtils.readURLQuery(queryString);
                }
            }
            catch (IllegalArgumentException e)
            {
                try
                {
                    resp.sendError(400, invalidKVPRequestMsg);
                }
                catch (IOException e1)
                {
                    log.error(internalErrorMsg, e1);
                }            
            }
            catch (DOMHelperException e)
            {
                try
                {
                    resp.sendError(400, invalidXMLRequestMsg);
                }
                catch (IOException e1)
                {
                    log.error(internalErrorMsg, e1);
                }            
            }
               
            // write response
            // set default mime type 
            resp.setContentType(XML_MIME_TYPE);
            
            // keep http objects in request
            request.setHttpRequest(req);
            request.setHttpResponse(resp);
            request.setPostServer(requestURL);
            
            // if capabilities request, deal with it in a generic manner
            if (request instanceof GetCapabilitiesRequest)
                this.handleRequest(request);
                
            // else let specific code handle the request
            else
                this.handleRequest(request);
        }
        catch (OWSException e)
        {
            try
            {
                resp.setContentType(XML_MIME_TYPE);
                String version = (request != null) ? request.getVersion() : getDefaultVersion();
                owsUtils.writeXMLException(new BufferedOutputStream(resp.getOutputStream()), getServiceType(), version, e);
                log.debug(e.getMessage(), e);
            }
            catch (IOException e1)
            {
                log.error(internalErrorMsg, e1);
            }            
        }
        catch (WriterException e)
        {
            log.debug(internalErrorMsg, e);
        }
        catch (Exception e)
        {
            try
            {
                if (!resp.isCommitted())
                    resp.sendError(500, internalErrorMsg);
                log.error(internalErrorMsg, e);
            }
            catch (IOException e1)
            {
            }
        }
        finally
        {
            try
            {
                resp.getOutputStream().flush();
            }
            catch (IOException e)
            {
            }
        }
    }
    
    
    protected void sendResponse(OWSRequest request, OWSResponse resp) throws OWSException, IOException
    {
        // write response to response stream
        OutputStream os = new BufferedOutputStream(request.getResponseStream());
        owsUtils.writeXMLResponse(os, resp, request.getVersion());
        os.flush();
    }
    
    
    protected String getDefaultVersion()
    {
        return "1.0";
    }
    
    
    protected abstract String getServiceType();
}
