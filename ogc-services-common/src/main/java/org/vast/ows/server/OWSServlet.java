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
import java.security.AccessControlException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSResponse;
import org.vast.ows.OWSUtils;
import org.vast.ows.util.PostRequestFilter;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Abstract Base Class for all OWS Style Servlets
 * </p>
 *
 * @author Alex Robin
 * @since Aug 9, 2005
 * */
public abstract class OWSServlet extends HttpServlet
{
    private static final long serialVersionUID = 4970153267344348035L;
    
    protected static final String LOG_REQUEST_MSG = "{} {}{} (from ip={}, user={})";
    protected static final String INVALID_REQUEST_MSG = "Invalid request: {}";
    protected static final String INVALID_KVP_REQUEST_MSG = "Invalid KVP request. Please check your syntax";
    protected static final String INVALID_XML_REQUEST_MSG = "Invalid XML request. Please check your syntax";
    protected static final String INTERNAL_ERROR_MSG = "Internal error while processing request";
    protected static final String INTERNAL_ERROR_HTTP_MSG = INTERNAL_ERROR_MSG + ". Please contact maintenance";
    protected static final String INTERNAL_SEND_ERROR_MSG = "Cannot send error";
    protected static final String SEND_RESPONSE_ERROR_MSG = "Cannot write response";
    protected static final String UNSUPPORTED_MSG = " operation is not supported on this server";
    
    protected final transient Logger log;
    protected final transient OWSUtils owsUtils;
    
        
    public OWSServlet()
    {
        this(new OWSUtils());
    }
    
    
    protected OWSServlet(OWSUtils owsUtils)
    {
        this(owsUtils, LoggerFactory.getLogger(OWSServlet.class));
    }
    
    
    protected OWSServlet(Logger log)
    {
        this(new OWSUtils(), log);
    }
    
    
    protected OWSServlet(OWSUtils owsUtils, Logger log)
    {
        this.owsUtils = owsUtils;
        this.log = log;
    }
    
    
    protected abstract void handleRequest(OWSRequest request) throws IOException, OWSException;
	
	
	/**
     * Parse and process HTTP GET request
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
    {
        processRequest(req, resp, false);
    }
    
    
	/**
     * Parse and process HTTP POST request
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
    {
        // assume XML if content type is not specified
        String contentType = req.getContentType();
        boolean isXml = (contentType == null) || (contentType.contains("xml"));
        
        processRequest(req, resp, isXml);
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
        catch (AccessControlException e)
        {
            // ask to authenticate if anonymous wasn't allowed
            if (req.getRemoteUser() == null)
            {
                sendAuthRequested(req, resp);
                return;
            }
            
            log.info("Access Forbidden: {}", e.getMessage());
            sendError(resp, HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
        catch (OWSException e)
        {
            if (!OWSUtils.isClientDisconnectError(e))
            {
                if (log.isDebugEnabled()) // these are client errors so we log them only in debug
                    log.error(INVALID_REQUEST_MSG, e.getMessage());
                String version = null;
                if (request != null)
                    version = request.getVersion();
                e.setSoapVersion(soapVersion);
                sendException(req, resp, e, version);
            }
        }
        catch (Exception e)
        {
            if (!OWSUtils.isClientDisconnectError(e))
            {
                log.error(INTERNAL_ERROR_MSG, e);
                sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, INTERNAL_ERROR_HTTP_MSG);
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
    protected OWSRequest parseRequest(HttpServletRequest req, HttpServletResponse resp, boolean isXmlRequest) throws OWSException
    {
        String requestURL = req.getRequestURL().toString();
        String soapVersion = null;
        OWSRequest owsRequest = null;
        DOMHelper dom = null;
        
        try
        {
            if (isXmlRequest)
            {
                InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
                dom = new DOMHelper(xmlRequest, false);
                Element requestElt = dom.getBaseElement();
                
                // detect and skip SOAP envelope if present
                soapVersion = getSoapVersion(dom);
                if (soapVersion != null)
                    requestElt = getSoapBody(dom);
                
                // log request
                logRequest(req, requestElt.getLocalName());
                
                // parse request
                owsRequest = parseRequest(dom, requestElt);
                owsRequest.setSoapVersion(soapVersion);
                owsRequest.setPostServer(requestURL);
            }
            else
            {
                String queryString = req.getQueryString();
                if (queryString == null)
                    throw new IllegalArgumentException();
                
                // log request
                logRequest(req, null);
                
                owsRequest = owsUtils.readURLQuery(queryString);
                owsRequest.setGetServer(requestURL);
            }
            
            // keep http objects in request
            owsRequest.setHttpRequest(req);
            owsRequest.setHttpResponse(resp);
            
            return owsRequest;
        }
        catch (OWSException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            String msg = isXmlRequest ? INVALID_XML_REQUEST_MSG : INVALID_KVP_REQUEST_MSG;
            if (log.isDebugEnabled()) // these are usually client errors so we log them only in debug
                log.error(msg, e);
            sendError(resp, 400, msg);          
        }
        
        return null;
    }
    
    
    /*
     * parse in separate method so it can be overriden
     */
    protected OWSRequest parseRequest(DOMHelper dom, Element requestElt) throws OWSException
    {
        return owsUtils.readXMLQuery(dom, requestElt);
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
    
    
    protected void sendError(HttpServletResponse resp, int errorCode, String errorMsg)
    {
        try
        {
            resp.sendError(errorCode, errorMsg);
        }
        catch (IOException e)
        {
            if (!OWSUtils.isClientDisconnectError(e) && log.isDebugEnabled())
                log.error(INTERNAL_SEND_ERROR_MSG, e);
        } 
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
            if (!OWSUtils.isClientDisconnectError(e) && log.isDebugEnabled())
                log.error(INTERNAL_SEND_ERROR_MSG, e1);
        }
    }
    
    
    protected void sendAuthRequested(HttpServletRequest req, HttpServletResponse resp)
    {
        try
        {
            req.authenticate(resp);
        }
        catch (Exception e)
        {
            if (!OWSUtils.isClientDisconnectError(e) && log.isDebugEnabled())
                log.error(INTERNAL_SEND_ERROR_MSG, e);
        } 
    }
    
    
    protected void sendResponse(OWSRequest request, OWSResponse resp) throws IOException
    {
        try
        {
            // write response to response stream
            OutputStream os = new BufferedOutputStream(request.getResponseStream());
            owsUtils.writeXMLResponse(os, resp, request.getVersion(), request.getSoapVersion());        
            os.flush();
        }
        catch (OWSException e)
        {
            throw new IOException(SEND_RESPONSE_ERROR_MSG, e);
        }
    }
    
    
    /*
     * Log request details
     */
    protected void logRequest(HttpServletRequest req, String opName)
    {
        if (log.isInfoEnabled())
        {
            String method = req.getMethod();
            String url = req.getRequestURI();
            String ip = req.getRemoteAddr();
            String user = req.getRemoteUser() != null ? req.getRemoteUser() : OWSUtils.ANONYMOUS_USER;
            
            // if proxy header present, use source ip instead of proxy ip
            String proxyHeader = req.getHeader("X-Forwarded-For");
            if (proxyHeader != null)
            {
                String[] ips = proxyHeader.split(",");
                if (ips.length >= 1)
                    ip = ips[0];
            }
            
            // detect websocket upgrade
            if ("websocket".equalsIgnoreCase(req.getHeader("Upgrade")))
                method += "/Websocket";
            
            // append decoded request if any
            String query = "";
            if (req.getQueryString() != null)
                query = "?" + req.getQueryString();
            if (opName != null)
                query += " " + opName;
            
            log.info(LOG_REQUEST_MSG, method, url, query, ip, user);
        }
    }
    
    
    protected String getDefaultVersion()
    {
        return "1.0";
    }
    
    
    protected abstract String getServiceType();
}
