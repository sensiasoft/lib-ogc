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
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vast.ogc.OGCException;
import org.vast.ogc.OGCExceptionReader;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;


/**
 * <p>
 * Utility methods for common handling of requests/responses in OGC web services
 * </p>
 *
 * @author Alex Robin
 * @since Jan 16, 2007
 */
public class OWSUtils extends OWSCommonUtils
{
    private static final Logger log = LoggerFactory.getLogger(OWSUtils.class);
    
    public static final String ANONYMOUS_USER = "anonymous";
    
    public static final String TEXT_MIME_TYPE = "text/plain";
    public static final String XML_MIME_TYPE = "text/xml";
    public static final String XML_MIME_TYPE2 = "application/xml";
    public static final String JSON_MIME_TYPE = "application/json";
    public static final String BINARY_MIME_TYPE = "application/octet-stream";
    public static final String SOAP11_URI = "http://schemas.xmlsoap.org/soap/envelope/";
    public static final String SOAP12_URI = "http://www.w3.org/2003/05/soap-envelope";
    
    public static final String OGC = "OGC";
    public static final String OWS = "OWS";
	public static final String WMS = "WMS";
	public static final String WFS = "WFS";
	public static final String WCS = "WCS";
	public static final String SWES = "SWES";
	public static final String SOS = "SOS";
	public static final String CSW = "CSW";
	public static final String WNS = "WNS";
	public static final String SAS = "SAS";	
	public static final String SPS = "SPS";
	public static final String WPS = "WPS";	
	public static final String SLD = "SLD";
    
	protected static final String UNSUPPORTED_SPEC_MSG = "No support for ";
	protected static final String INVALID_ENDPOINT_MSG = "No Endpoint URL specified in request object";
	protected static final String IO_ERROR_MSG = "I/O Error while sending request: ";
	protected static final String SERVER_ERROR_MSG = "Exception received from server";
    
    
    OWSCommonUtils dataTypeUtils = new OWSCommonUtils();
    
    
    static
    {
    	loadRegistry();
    }
    
    
    public static void loadRegistry()
    {
    	String mapFileUrl = OWSUtils.class.getResource("OWSRegistry.xml").toString();
    	OGCRegistry.loadMaps(mapFileUrl, false);
    	
    	// also load all web service bindings found on the classpath
    	ServiceLoader<OWSBindingProvider> sl = ServiceLoader.load(OWSBindingProvider.class);
    	for (OWSBindingProvider provider: sl)
    	{
        	try
            {
        	    provider.loadBindings();
            }
            catch (Exception e)
            {
                log.error("Cannot load bindings from " + provider.getClass().getCanonicalName(), e);
            }
    	}
    }
    
    
    public static String getNamespaceURI(String spec, String version)
    {
    	return OGCRegistry.getNamespaceURI(spec, version);
    }
    
    
    /**
     * Checks if exception was caused by EOF<br/>
     * This is used so we can ignore exceptions due to client disconnections
     * @param e exception to check (along with its causes)
     * @return true if exception trace contains an EOF exception, false otherwise
     */
    public static boolean isClientDisconnectError(Exception e)
    {
        Throwable ex = e.getCause();
        while (ex != null)
        {
            if (ex instanceof EOFException)
                return true;
            ex = ex.getCause();
        }
        
        return false;
    }
    
    
    public OWSUtils()
    {    	
    }
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     * @param dom DOM helper instance that will be used to parse the DOM tree
     * @param requestElt root element of the request
     * @param serviceType service type to check if service in query is correct
     * @param defaultVersion version used in case no version is specified in the query
     * @return OWS request object filled with all parameters parsed from XML request
     * @throws OWSException 
     */
    public OWSRequest readXMLQuery(DOMHelper dom, Element requestElt, String serviceType, String defaultVersion) throws OWSException
    {
        // read common params and check that they're present
        OWSRequest request = new OWSRequest();
        request.setVersion(defaultVersion);
    	AbstractRequestReader.readCommonXML(dom, requestElt, request);
		OWSExceptionReport report = new OWSExceptionReport();
		AbstractRequestReader.checkParameters(request, report, serviceType);
        report.process();
        
        // parse request with appropriate reader
        try
        {
            OWSRequestReader<OWSRequest> reader = (OWSRequestReader<OWSRequest>)OGCRegistry.createReader(request.service, request.operation, request.version);
            request = reader.readXMLQuery(dom, requestElt);
            return request;
        }
        catch (IllegalStateException e)
        {
            String spec = request.service + " " + request.operation + " v" + request.version;
        	throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     * @param dom DOM helper instance that will be used to parse the DOM tree
     * @param requestElt root element of the request
     * @param serviceType service type to check if service parameter in query is correct
     * @return OWS request object filled with all parameters parsed from XML request
     * @throws OWSException 
     */
    public OWSRequest readXMLQuery(DOMHelper dom, Element requestElt, String serviceType) throws OWSException
    {
    	return readXMLQuery(dom, requestElt, serviceType, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     * @param dom DOM helper instance that will be used to parse the DOM tree
     * @param requestElt root element of the request 
     * @return OWS request object filled with all parameters parsed from XML request
     * @throws OWSException 
     */
    public OWSRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
    {
    	return readXMLQuery(dom, requestElt, null, null);
    }
    
    
    /**
     * Helper method to parse any OWS query directly from an InputStream
     * @param is input stream containing the XML document to read from
     * @param serviceType service type to check if service parameter in query is correct 
     * @return OWS request object filled with all parameters parsed from XML request
     * @throws OWSException 
     */
    public OWSRequest readXMLQuery(InputStream is, String serviceType) throws OWSException
    {
    	try
		{
			DOMHelper dom = new DOMHelper(is, false);
			OWSRequest request = readXMLQuery(dom, dom.getRootElement(), serviceType);
			return request;
		}
		catch (DOMHelperException e)
		{
			throw new OWSException(AbstractRequestReader.invalidXML, e);
		}
    }
    
    
    /**
     * Helper method to parse any OWS query directly from an InputStream
     * @param is input stream containing the XML document to read from
     * @return OWS request object filled with all parameters parsed from XML request
     * @throws OWSException 
     */
    public OWSRequest readXMLQuery(InputStream is) throws OWSException
    {
        return readXMLQuery(is, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * @param queryString query string to read request parameters from
     * @param serviceType service type to check if service parameter in query is correct 
     * @param defaultVersion version used in case no version is specified in the query
     * @return OWS request object containing all parameters parsed from query string
     * @throws OWSException 
     */
    public OWSRequest readURLQuery(String queryString, String serviceType, String defaultVersion) throws OWSException
    {
        // init generic request object
        OWSRequest request = new OWSRequest();
        
        try
        {
            Map<String, String> queryParams = parseQueryParameters(queryString);
            
            // read common params to figure out what reader to use
            request.setService(queryParams.get("service"));
            request.setOperation(queryParams.get("request"));
            request.setVersion(queryParams.get("version"));
            if (request.getVersion() == null)
                request.setVersion(defaultVersion);
            
            OWSExceptionReport report = new OWSExceptionReport();
            AbstractRequestReader.checkParameters(request, report, serviceType);
            report.process();
        
            OWSRequestReader<OWSRequest> reader = (OWSRequestReader<OWSRequest>)OGCRegistry.createReader(request.service, request.operation, request.version);
            request = reader.readURLParameters(queryParams);
            return request;
        }
        catch (IllegalStateException e)
        {
            String spec = request.service + " " + request.operation + " v" + request.version;
            throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * The service type is also specified in case it is missing in the query
     * @param queryString query string to read request parameters from
     * @param serviceType service type to check if service parameter in query is correct 
     * @return OWS request object containing all parameters parsed from query string
     * @throws OWSException 
     */
    public OWSRequest readURLQuery(String queryString, String serviceType) throws OWSException
    {
    	return readURLQuery(queryString, serviceType, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * @param queryString query string to read request parameters from 
     * @return OWS request object containing all parameters parsed from query string
     * @throws OWSException 
     */
    public OWSRequest readURLQuery(String queryString) throws OWSException
    {
        return readURLQuery(queryString, null);
    }
    
    
    /**
     * Helper method to guess the version from the request XML
     * @param dom DOM helper instance that will be used to parse the DOM tree
     * @param objectElt DOM element to read version from
     * @return version string
     * @throws OWSException 
     */
    public String readXMLVersion(DOMHelper dom, Element objectElt) throws OWSException
    {
        String version = dom.getAttributeValue(objectElt, "@version");
        
        // if version is still null get it from namespace of elt
        if (version == null || version.length() == 0)
        {
        	String nsUri = objectElt.getNamespaceURI();
        	version = nsUri.substring(nsUri.lastIndexOf('/') + 1);
        }
        
        // throw exception if no version is specified
        if (version == null || version.length() == 0) {
            throw new OWSException("Cannot figure out document version");
        }
        
        return version;
    }
    
    
    /**
     * Helper method to build an URL query from given request object
     * @param request OWS request object
     * @return query string generated from the given request object
     * @throws OWSException 
     */
    public String buildURLQuery(OWSRequest request) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSRequest> writer = (OWSRequestWriter<OWSRequest>)OGCRegistry.createWriter(request.service, request.operation, request.version);
            String url = writer.buildURLQuery(request);
            return url;
        }
        catch (IllegalStateException e)
        {
        	String spec = request.service + " " + request.operation + " v" + request.version;
        	throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }


    /**
     * Helper method to build a DOM element containing the request XML
     * Note that the element is not yet appended to any parent.
     * @param dom DOM helper instance that will be used to generate the DOM tree
     * @param request OWS request object
     * @return DOM element containing the request XML
     * @throws OWSException 
     */
    public Element buildXMLQuery(DOMHelper dom, OWSRequest request) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSRequest> writer = (OWSRequestWriter<OWSRequest>)OGCRegistry.createWriter(request.service, request.operation, request.version);
            Element requestElt = writer.buildXMLQuery(dom, request);
            return requestElt;
        }
        catch (IllegalStateException e)
        {
        	String spec = request.service + " " + request.operation + " v" + request.version;
        	throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }
    
    
    /**
     * Helper method to write any OWS XML request directly to an output stream
     * @param os output stream to write to
     * @param request OWS request object
     * @throws OWSException 
     */
    public void writeXMLQuery(OutputStream os, OWSRequest request) throws OWSException
    {
    	try
        {
            DOMHelper dom = new DOMHelper();
            Element requestElt = buildXMLQuery(dom, request);
            dom.serialize(requestElt, os, true);
        }
        catch (IOException e)
        {
            throw new OWSException(AbstractRequestWriter.ioError, e);
        }        
    }


    /**
     * Helper method to parse any OWSResponse from service type only
     * This tries to guess the response type and version from the root element
     * @param dom DOM helper instance that will be used to parse the DOM tree
     * @param responseElt root element of the XML response
     * @param serviceType type of OGC service sending the response
     * @return OWS response object
     * @throws OWSException
     */
    public OWSResponse readXMLResponse(DOMHelper dom, Element responseElt, String serviceType) throws OWSException
    {
        return readXMLResponse(dom, responseElt, serviceType, null, null);
    }
    
    
    /**
     * Helper method to parse any OWSResponse from service type and response type only
     * This tries to guess the version from a version attribute or the end of the namespace uri
     * @param dom DOM helper instance that will be used to parse the DOM tree
     * @param responseElt root element of the XML response
     * @param serviceType type of OGC service sending the response
     * @param responseType type of response object to read (if null XML element name is used)
     * @return OWS response object
     * @throws OWSException
     */
    public OWSResponse readXMLResponse(DOMHelper dom, Element responseElt, String serviceType, String responseType) throws OWSException
    {
    	return readXMLResponse(dom, responseElt, serviceType, responseType, null);
    }
    
    
    /**
     * Helper method to parse any OWSResponse from service type, response type and version
     * @param dom DOM helper instance that will be used to parse the DOM tree
     * @param responseElt root element of the XML response
     * @param serviceType type of OGC service sending the response
     * @param responseType type of response object to read (if null XML element name is used)
     * @param version version of reader to use
     * @return OWS response object
     * @throws OWSException
     */
    public OWSResponse readXMLResponse(DOMHelper dom, Element responseElt, String serviceType, String responseType, String version) throws OWSException
    {
        // this will throw an exception if the response contains one 
        // instead of the actual response message
        OWSExceptionReader.checkException(dom, responseElt);
        
        // autodetect response type if non specified
        if (responseType == null)
            responseType = responseElt.getLocalName();
        
        // auto detect version if non specified
        if (version == null)
            version = readXMLVersion(dom, responseElt);
        
        try
        {
        	OWSResponseReader<OWSResponse> reader = (OWSResponseReader<OWSResponse>)OGCRegistry.createReader(serviceType, responseType, version);
            OWSResponse response = reader.readXMLResponse(dom, responseElt);
            response.setService(serviceType);
            return response;
        }
        catch (IllegalStateException e)
        {
            String spec = serviceType + " " + responseType + " v" + version;
        	throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS response directly from an InputStream
     * @param is input stream to read from
     * @param serviceType type of OGC service sending the response
     * @param responseType type of response to read (get it from 
     * @return OWS response object
     * @throws OWSException
     */
    public OWSResponse readXMLResponse(InputStream is, String serviceType, String responseType) throws OWSException
    {
        try
        {
            DOMHelper dom = new DOMHelper(is, false);
            OWSResponse resp = readXMLResponse(dom, dom.getRootElement(), serviceType, responseType);
            return resp;
        }
        catch (DOMHelperException e)
        {
            throw new OWSException(AbstractResponseReader.invalidXML, e);
        }
    }
    
    
    /**
	 * Helper method to build a DOM element containing the response XML
	 * for the specified version 
	 * @param dom DOM helper instance that will be used to generate the DOM tree
	 * @param response OWS response object
	 * @param version version of writer to use
	 * @return DOM element containing the response XML
     * @throws OWSException 
	 */
	public Element buildXMLResponse(DOMHelper dom, OWSResponse response, String version) throws OWSException
	{
		try
	    {
	        OWSResponseWriter<OWSResponse> writer = (OWSResponseWriter<OWSResponse>)OGCRegistry.createWriter(response.service, response.messageType, version);
	        Element requestElt = writer.buildXMLResponse(dom, response, version);
	        return requestElt;
	    }
	    catch (IllegalStateException e)
	    {
	    	String spec = response.service + " " + response.messageType + " v" + version;
	    	throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
	    }
	}
	
	
	/**
	 * Helper method to build a DOM element containing the response XML.<br/>
	 * in a version agnostic way. The element is not appended to any parent 
	 * @param dom DOM helper instance that will be used to generate the DOM tree
	 * @param response OWS response object
	 * @return DOM element containing the response XML
	 * @throws OWSException 
	 */
	public Element buildXMLResponse(DOMHelper dom, OWSResponse response) throws OWSException
	{
		return buildXMLResponse(dom, response, response.getVersion());
	}
	
	
	/**
     * Helper method to write any OWS XML response directly to an output stream
	 * @param os output stream to write to
	 * @param response OWS response object
	 * @param version version of writer to use
	 * @throws OWSException 
     */
    public void writeXMLResponse(OutputStream os, OWSResponse response, String version) throws OWSException
    {
    	writeXMLResponse(os, response, version, null);      
    }
    
    
    /**
     * Helper method to write any OWS XML response directly to an output stream.<br/>
     * The version specified in the response object determines the version of writer to use
     * @param os output stream to write to
     * @param response OWS response object
     * @throws OWSException 
     */
    public void writeXMLResponse(OutputStream os, OWSResponse response) throws OWSException
    {
    	writeXMLResponse(os, response, response.getVersion());
    }
    
    
    /**
     * Helper method to write any OWS XML response directly to an output stream
     * @param os output stream to write to
     * @param response OWS response object
     * @param version version of writer to use
     * @param soapVersion version of SOAP used to wrap the response ({@link #SOAP11_URI} or {@link #SOAP12_URI}).
     *                    If null, response is sent as-is without envelope
     * @throws OWSException 
     */
    public void writeXMLResponse(OutputStream os, OWSResponse response, String version, String soapVersion) throws OWSException
    {
        try
        {
            DOMHelper dom = new DOMHelper();
            Element responseElt = buildXMLResponse(dom, response, version);
            
            // wrap with SOAP envelope if requested
            if (soapVersion != null)
            {
                dom.addUserPrefix("soap", soapVersion);
                
                Element bodyElt = dom.createElement("soap:Body");
                bodyElt.appendChild(responseElt);
                
                Element envElt = dom.createElement("soap:Envelope");
                envElt.appendChild(bodyElt);
                responseElt = envElt;
            }            
            
            dom.serialize(responseElt, os, true);
        }
        catch (IOException e)
        {
            throw new OWSException(AbstractResponseWriter.ioError, e);
        }        
    }
    
    
    /**
     * Helper method to write OWS exception reports
     * @param os output stream to write to
     * @param serviceType type of OGC service generating the exception
     * @param version version of exception report to write
     * @param e OWS exception to write
     */
    public void writeXMLException(OutputStream os, String serviceType, String version, OWSException e)
    {
    	OWSExceptionWriter writer = new OWSExceptionWriter();
    	String owsVersion = OGCRegistry.getOWSVersion(serviceType, version);
    	e.setVersion(owsVersion);    	
		writer.writeException(os, e);
    }
    
    
    /**
     * Helper method to send any OWS request using either GET or POST URL as specified in request object.<br/>
     * If both GET and POST URLs are specified, GET is used.
     * @param request OWS request object to send
     * @param useSoap set to true to wrap the POST request into a SOAP envelope
     * @return OWS response object obtained from service response
     * @throws OWSException if service returns an OWS exception report or an HTTP error
     */
    public <ResponseType extends OWSResponse> ResponseType sendRequest(OWSRequest request, boolean useSoap) throws OWSException
    {
        HttpURLConnection conn = null;
        
        if (request.getGetServer() != null)
            conn = sendGetRequest(request);
        else if (request.getPostServer() != null)
        {
            if (useSoap)
                conn = sendSoapRequest(request);
            else
                conn = sendPostRequest(request);
        }
        else
            throw new IllegalStateException("Either GET or POST server must be set in request object");
        
        // try to parse XML response
        try
        {
            DOMHelper dom = new DOMHelper(new BufferedInputStream(conn.getInputStream()), false);
            OWSExceptionReader.checkException(dom, dom.getBaseElement());
            return (ResponseType)readXMLResponse(dom, dom.getBaseElement(), request.getService(), dom.getBaseElement().getLocalName(), request.getVersion());
        }
        catch (OWSException e)
        {
            throw e;
        }
        catch (IOException e)
        {
            throw new OWSException("Error while reading service response", e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using GET
     * @param request OWS request object
     * @return HTTP connection object
     * @throws OWSException if service returns an OWS exception report or an HTTP error
     */
    public HttpURLConnection sendGetRequest(OWSRequest request) throws OWSException
    {
	    String requestString = null;
        
        try
        {
        	if (request.getGetServer() == null)
        		throw new OWSException(INVALID_ENDPOINT_MSG);
            
            requestString = buildURLQuery(request);
            URL url = new URL(requestString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(request.getConnectTimeOut());
            connection.setReadTimeout(request.getConnectTimeOut());
            connection.connect();
            
            // catch server side exceptions
            if (connection.getResponseCode() > 202)
            	OGCExceptionReader.parseException(connection.getErrorStream());
            
            return connection;
        }
        catch (OGCException e)
		{
	    	throw new OWSException(SERVER_ERROR_MSG, e);
		}
        catch (IOException e)
        {
            throw new OWSException(IO_ERROR_MSG + requestString, e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using POST
     * @param request OWS request object to send
     * @return HTTP connection object
     * @throws OWSException if service returns an OWS exception report or an HTTP error
     */
    public HttpURLConnection sendPostRequest(OWSRequest request) throws OWSException
    {
	    try
        {
        	URL url;
        	String endpoint = request.getPostServer();
        	
        	if (endpoint == null)
        		endpoint = request.getGetServer();
        	
        	if (endpoint == null)
        		throw new OWSException(INVALID_ENDPOINT_MSG);
        	
            // remove ? at the end of Endpoint URL
            if (endpoint.endsWith("?"))
                url = new URL(endpoint.substring(0, endpoint.length()-1));
            else
                url = new URL(endpoint);
            
            // initiatlize HTTP connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(request.getConnectTimeOut());
            connection.setReadTimeout(request.getConnectTimeOut());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", XML_MIME_TYPE);            
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            // send post data            
            writeXMLQuery(out, request);
            out.flush();
            connection.connect();
            out.close();
            
            // catch server side exceptions
            if (connection.getResponseCode() > 202)
            	OGCExceptionReader.parseException(connection.getErrorStream());
            
            return connection;
        }
	    catch (OGCException e)
		{
	    	throw new OWSException(SERVER_ERROR_MSG, e);
		}
        catch (IOException e)
        {
        	if (log.isDebugEnabled())
        	{
        	    ByteArrayOutputStream buf = new ByteArrayOutputStream();
        	    writeXMLQuery(buf, request);
        	    log.debug(IO_ERROR_MSG + "\n" + buf);
        	}
        	
        	throw new OWSException(IO_ERROR_MSG + request.getOperation(), e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request in the query string by using POST for additional content.<br/>
     * This is typically used to send SWE data to the server in a persistent HTTP connection, so the connection
     * object returned is not connected to let the caller add the desired content headers connection options.
     * @param request OWS request object to send
     * @return HTTP connection object
     * @throws OWSException if there is an error serializing or sending the request
     */
    public HttpURLConnection sendPostRequestWithQuery(OWSRequest request) throws OWSException
    {
        try
        {
            String endpoint = request.getGetServer();
            if (endpoint == null)
            {
                endpoint = request.getPostServer();
                request.setGetServer(endpoint);
            }
            
            if (endpoint == null)
                throw new OWSException(INVALID_ENDPOINT_MSG);            
            
            // initiatlize HTTP connection
            String requestString = buildURLQuery(request);
            URL url = new URL(requestString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(request.getConnectTimeOut());
            connection.setReadTimeout(request.getConnectTimeOut());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            
            return connection;
        }
        catch (IOException e)
        {
            if (log.isDebugEnabled())
            {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                writeXMLQuery(buf, request);
                log.debug(IO_ERROR_MSG + "\n" + buf);
            }
            
            throw new OWSException(IO_ERROR_MSG + request.getOperation(), e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using POST with SOAP
     * @param request OWS request object to send
     * @return HTTP connection object
     * @throws OWSException if service returns an OWS exception report or an HTTP error
     */
    public HttpURLConnection sendSoapRequest(OWSRequest request) throws OWSException
    {
	    try
        {
        	URL url;
        	String endpoint = request.getPostServer();
        	
        	if (endpoint == null)
        		endpoint = request.getGetServer();
        	
        	if (endpoint == null)
        		throw new OWSException(INVALID_ENDPOINT_MSG);
        	
            // remove ? at the end of Endpoint URL
            if (endpoint.endsWith("?"))
                url = new URL(endpoint.substring(0, endpoint.length()-1));
            else
                url = new URL(endpoint);
            
            // initialize HTTP connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(request.getConnectTimeOut());
            connection.setReadTimeout(request.getConnectTimeOut());
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", XML_MIME_TYPE);
            connection.setRequestProperty("SOAPAction", request.getOperation());
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            // determine SOAP version/namespace
            String soapUri = request.getSoapVersion();
            if (soapUri == null)
                soapUri = OWSUtils.SOAP12_URI;
            
            // send post data
            DOMHelper dom = new DOMHelper();
            dom.addUserPrefix("soap", soapUri);
            Element envElt = dom.createElement("soap:Envelope");		
			Element bodyElt = dom.addElement(envElt, "soap:Body");
			Element reqElt = buildXMLQuery(dom, request);
            bodyElt.appendChild(reqElt);
            dom.serialize(envElt, out, true);
            out.flush();
            connection.connect();
            out.close();
            
            // catch server side exceptions
            if (connection.getResponseCode() > 202)
            	OGCExceptionReader.parseException(connection.getErrorStream());

            return connection;
        }
	    catch (OGCException e)
		{
	    	throw new OWSException(SERVER_ERROR_MSG, e);
		}
        catch (IOException e)
        {
            if (log.isDebugEnabled())
            {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                writeXMLQuery(buf, request);
                log.debug(IO_ERROR_MSG + "\n" + buf);
            }
            
        	throw new OWSException(IO_ERROR_MSG + request.getOperation(), e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS capabilities document from an XML/DOM tree
     * @param dom DOM helper instance to use to parse the DOM tree
     * @param capsElt root of the capabilities document
     * @param serviceType type of service to read capabilities from
     * @return OWS capabilities
     * @throws OWSException 
     */
    public OWSServiceCapabilities readCapabilities(DOMHelper dom, Element capsElt, String serviceType) throws OWSException
    {
        // read common params and check that they're present
        String version = dom.getAttributeValue(capsElt, "@version");
        String message = "Capabilities";
        
        try
        {
            OWSResponseReader<OWSResponse> reader = (OWSResponseReader<OWSResponse>)OGCRegistry.createReader(serviceType, message, version);
            OWSServiceCapabilities caps = (OWSServiceCapabilities)reader.readXMLResponse(dom, capsElt);
            return caps;
        }
        catch (IllegalStateException e)
        {
            String spec = serviceType + " " + message + " v" + version;
        	throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }

    
    /**
     * Helper method to get capabilities from an OWS service and parse it
     * @param server URL of server to get capabilities from (without getCapabilities query arguments)
     * @param serviceType type of service the request will be sent to
     * @param version version of service the request will be sent to
     * @return OWS capabilities
     * @throws OWSException
     */
    public OWSServiceCapabilities getCapabilities(String server, String serviceType, String version) throws OWSException
    {
        try
        {
        	// prepare request
        	GetCapabilitiesRequest request = new GetCapabilitiesRequest();
        	request.setGetServer(server);
        	request.setService(serviceType);
        	request.setVersion(version);
        	
        	URLConnection connection = this.sendGetRequest(request);
    		DOMHelper dom = new DOMHelper(connection.getInputStream(), false);
    		
    		// parse capabilities doc
    		OWSServiceCapabilities caps = (OWSServiceCapabilities)readXMLResponse(dom, dom.getBaseElement(), serviceType, "Capabilities");
    		return caps;    		          
        }
        catch (DOMHelperException e)
		{
			throw new OWSException(AbstractResponseReader.invalidXML, e);
		}
        catch (IOException e)
        {
            throw new OWSException(IO_ERROR_MSG, e);
        }  
        catch (IllegalStateException e)
        {
        	String spec = serviceType + " Capabilities v" + version;
        	throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
    }
}
