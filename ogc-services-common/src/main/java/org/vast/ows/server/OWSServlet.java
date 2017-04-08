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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSResponse;
import org.vast.ows.OWSUtils;
import org.vast.ows.util.PostRequestFilter;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;


/**
 * <p>
 * Abstract Base Class for all OWS Style Servlets
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Aug 9, 2005
 * */
public abstract class OWSServlet extends HttpServlet
{
    private static final long serialVersionUID = 4970153267344348035L;
    protected static final String invalidKVPRequestMsg = "Invalid KVP request. Please check your syntax";
    protected static final String invalidXMLRequestMsg = "Invalid XML request. Please check your syntax";
    protected static final String internalErrorMsg = "Internal Error while processing the request. Please contact maintenance";
        
    private static Logger log = LoggerFactory.getLogger(OWSServlet.class);
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
//	    OWSCapabilitiesSerializer serializer = new OWSCapabilitiesSerializer();
//	    serializer.setOutputByteStream(resp);
//	    serializer.serialize(capsHelper.getRootElement());
        
        // The code below is a better way to do it but we cannot remove the internalInfo elements on the fly
	    try
		{
		    Transformer serializer = TransformerFactory.newInstance().newTransformer();
		    serializer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
		    Source input = new DOMSource(capsHelper.getRootElement());
		    Result output = new StreamResult(resp);
		    serializer.transform(input, output);
		}
		catch (Exception e)
		{
		}
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
        
        // assume XML if content type is not specified
        String contentType = req.getContentType();
        boolean isXml = (contentType == null) || (contentType.contains("xml"));
        
        processRequest(req, resp, isXml);
    }
    
    
    /*
     * Checks if client was disconnected
     * This is used so we can ignore exceptions due to interrupted connections
     */
    protected boolean isClientDisconnected(HttpServletRequest req, HttpServletResponse resp)
    {
        try
        {
            resp.flushBuffer();
        }
        catch (IOException e)
        {
            return true;
        }
        
        return false;
    }
    
    
    /**
     * Process all types of requests
     * @param req
     * @param resp
     * @param isXmlRequest
     */
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp, boolean isXmlRequest)
    {
        OWSRequest request = null;
        String soapVersion = null;
        
        try
        {
            // parse request
            request = parseRequest(req, resp, isXmlRequest);
            
            if (request != null)
            {
                soapVersion = request.getSoapVersion();
                
                // set default mime type 
                resp.setContentType(OWSUtils.XML_MIME_TYPE);
                
                // handle request
                this.handleRequest(request);
                
                resp.getOutputStream().flush();
            }
        }
        catch (OWSException e)
        {
            if (!isClientDisconnected(req, resp))
            {
                try
                {
                    log.trace("Error while processing request", e);
                    String version = null;
                    if (request != null)
                        version = request.getVersion();
                    e.setSoapVersion(soapVersion);
                    sendException(req, resp, e, version);
                }
                catch (Exception e1)
                {
                }
            }           
        }
        catch (SecurityException e)
        {
            try
            {
                log.trace("Access Forbidden", e.getMessage());
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            }
            catch (IOException e1)
            {
            }
        }
        catch (Exception e)
        {
            try
            {
                log.error(internalErrorMsg, e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, internalErrorMsg);
            }
            catch (IOException e1)
            {
            }
        }
    }
    
    
    /**
     * Parse KVP or XML request to generate java request object
     * @param req
     * @param isXmlRequest
     * @return
     * @throws Exception
     */
    protected OWSRequest parseRequest(HttpServletRequest req, HttpServletResponse resp, boolean isXmlRequest) throws Exception
    {
        String requestURL = req.getRequestURL().toString();
        String soapVersion = null;
        OWSRequest owsRequest = null;
        
        try
        {
            if (isXmlRequest)
            {
                InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
                DOMHelper dom = new DOMHelper(xmlRequest, false);
                //dom.serialize(dom.getBaseElement(), System.out, true);
                Element requestElt = dom.getBaseElement();
                
                // detect and skip SOAP envelope if present
                soapVersion = getSoapVersion(dom);
                if (soapVersion != null)
                    requestElt = getSoapBody(dom);
                                
                // parse request
                owsRequest = owsUtils.readXMLQuery(dom, requestElt);
                owsRequest.setSoapVersion(soapVersion);                
                owsRequest.setPostServer(requestURL);
            }
            else
            {
                String queryString = req.getQueryString();
                if (queryString == null)
                    throw new IllegalArgumentException();
                
                owsRequest = owsUtils.readURLQuery(queryString);
                owsRequest.setGetServer(requestURL);
            }
            
            // keep http objects in request
            owsRequest.setHttpRequest(req);
            owsRequest.setHttpResponse(resp);
            
            return owsRequest;
        }
        catch (IllegalArgumentException e)
        {
            try
            {
                log.trace(invalidKVPRequestMsg, e);
                resp.sendError(400, invalidKVPRequestMsg);
            }
            catch (IOException e1)
            {
            }            
        }
        catch (IOException | DOMHelperException e)
        {
            try
            {
                log.trace(invalidXMLRequestMsg, e);
                resp.sendError(400, invalidXMLRequestMsg);
            }
            catch (IOException e1)
            {
            }            
        }
        catch (OWSException e)
        {
            log.trace("Error while parsing request", e);
            e.setSoapVersion(soapVersion);
            sendException(req, resp, e, req.getParameter("version"));
        }
        
        return null;
    }
    
    
    protected String getSoapVersion(DOMHelper dom)
    {
        Element requestElt = dom.getBaseElement();
        String nsUri = requestElt.getNamespaceURI();        
        if (OWSUtils.SOAP11_URI.equals(nsUri) || OWSUtils.SOAP12_URI.equals(nsUri))
            return nsUri;
        return null;
    }
    
    
    protected Element getSoapBody(DOMHelper dom) throws IOException
    {
        Element requestElt = dom.getBaseElement();
        requestElt = dom.getElement(requestElt, "Body/*");
        if (requestElt == null)
            throw new IOException("No request in SOAP body");        
        return requestElt;
    }
    
    
    protected void sendException(HttpServletRequest req, HttpServletResponse resp, OWSException e, String version)
    {
        try
        {
            resp.setContentType(OWSUtils.XML_MIME_TYPE);
            if (version == null)
                version = getDefaultVersion();
            BufferedOutputStream os = new BufferedOutputStream(resp.getOutputStream());
            owsUtils.writeXMLException(os, getServiceType(), version, e);
            os.close();
        }
        catch (IOException e1)
        {
            log.trace("Error while sending exception report", e1);
        }
    }
    
    
    protected void sendResponse(OWSRequest request, OWSResponse resp) throws OWSException, IOException
    {
        // write response to response stream
        OutputStream os = new BufferedOutputStream(request.getResponseStream());
        owsUtils.writeXMLResponse(os, resp, request.getVersion(), request.getSoapVersion());        
        os.flush();
    }
    
    
    protected String getDefaultVersion()
    {
        return "1.0";
    }
    
    
    protected abstract String getServiceType();
}
