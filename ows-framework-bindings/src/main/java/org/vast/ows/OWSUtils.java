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

package org.vast.ows;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import org.vast.ogc.OGCException;
import org.vast.ogc.OGCExceptionReader;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;


/**
 * <p>
 * Utility methods for common stuffs in OGC services
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @since Jan 16, 2007
 * @version 1.0
 */
public class OWSUtils implements OWSRequestReader<OWSRequest>, OWSRequestWriter<OWSRequest>
{
    public final static String OGC = "OGC";
    public final static String OWS = "OWS";
	public final static String WMS = "WMS";
	public final static String WFS = "WFS";
	public final static String WCS = "WCS";
	public final static String SWES = "SWES";
	public final static String SOS = "SOS";
	public final static String CSW = "CSW";
	public final static String WNS = "WNS";
	public final static String SAS = "SAS";	
	public final static String SPS = "SPS";
	public final static String WPS = "WPS";	
	public final static String SLD = "SLD";
    
	public final static String soap11Uri = "http://schemas.xmlsoap.org/soap/envelope/";
	public final static String soap12Uri = "http://www.w3.org/2003/05/soap-envelope";
	public final static String unsupportedSpec = "No support for ";
    public final static String invalidEndpoint = "No Endpoint URL specified in request object";
    public final static String ioError = "IO Error while sending request:";
    
    
    static
    {
    	loadRegistry();
    }
    
    
    public static void loadRegistry()
    {
    	String mapFileUrl = OWSUtils.class.getResource("OWSRegistry.xml").toString();
    	OGCRegistry.loadMaps(mapFileUrl, false);
    }
    
    
    public static String getNamespaceURI(String spec, String version)
    {
    	return OGCRegistry.getNamespaceURI(spec, version);
    }
    
    
    public OWSUtils()
    {    	
    }
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     * The service type is also specified to check if service in query is correct
     * The default version is used in case no version is specified in the query
     */
    public OWSRequest readXMLQuery(DOMHelper dom, Element requestElt, String serviceType, String defaultVersion) throws OWSException
    {
    	OWSRequest request = new OWSRequest();
    	request.setVersion(defaultVersion);
    	
    	// skip SOAP envelope if present
    	if (requestElt.getNamespaceURI().equals(soap12Uri))
    	    requestElt = dom.getElement(requestElt, "Body/*");
    	
    	// read common params and check that they're present
        AbstractRequestReader.readCommonXML(dom, requestElt, request);
		OWSExceptionReport report = new OWSExceptionReport();
		AbstractRequestReader.checkParameters(request, report, serviceType);
        report.process();
        
        try
        {
            OWSRequestReader<OWSRequest> reader = (OWSRequestReader<OWSRequest>)OGCRegistry.createReader(request.service, request.operation, request.version);
            request = reader.readXMLQuery(dom, requestElt);            
            return request;
        }
        catch (IllegalStateException e)
        {
            String spec = request.service + " " + request.operation + " v" + request.version;
        	throw new OWSException(unsupportedSpec + spec, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     */
    public OWSRequest readXMLQuery(DOMHelper dom, Element requestElt, String serviceType) throws OWSException
    {
    	return readXMLQuery(dom, requestElt, serviceType, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     */
    public OWSRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
    {
    	return readXMLQuery(dom, requestElt, null, null);
    }
    
    
    /**
     * Helper method to parse any OWS query directly from an InputStream
     */
    public OWSRequest readXMLQuery(InputStream is, String service) throws OWSException
    {
    	try
		{
			DOMHelper dom = new DOMHelper(is, false);
			OWSRequest request = readXMLQuery(dom, dom.getRootElement(), service);
			return request;
		}
		catch (DOMHelperException e)
		{
			throw new OWSException(AbstractRequestReader.invalidXML, e);
		}
    }
    
    
    /**
     * Helper method to parse any OWS query directly from an InputStream
     */
    public OWSRequest readXMLQuery(InputStream is) throws OWSException
    {
        return readXMLQuery(is, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * The service type is also specified to check if service in query is correct
     * The default version is used in case no version is specified in the query
     */
    public OWSRequest readURLQuery(String queryString, String serviceType, String defaultVersion) throws OWSException
    {
        Map<String, String> queryParams = AbstractRequestReader.parseQueryParameters(queryString);
       return readURLParameters(queryParams, serviceType, defaultVersion);
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * The service type is also specified in case it is missing in the query
     */
    public OWSRequest readURLQuery(String queryString, String serviceType) throws OWSException
    {
    	return readURLQuery(queryString, serviceType, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     */
    public OWSRequest readURLQuery(String queryString) throws OWSException
    {
        return readURLQuery(queryString, null);
    }
    
    
    /**
     * Helper method to decode URL KVP parameters into the right request object
     * @param queryParams
     * @param serviceType
     * @param defaultVersion
     * @return
     * @throws OWSException
     */
    public OWSRequest readURLParameters(Map<String, String> queryParams, String serviceType, String defaultVersion) throws OWSException
    {
        // init generic request object
        OWSRequest request = new OWSRequest();
        
        try
        {
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
            throw new OWSException(unsupportedSpec + spec, e);
        }
    }
    
    
    /**
     * Helper method to parse KVP arguments of any OWS query
     */    
    public OWSRequest readURLParameters(Map<String, String> queryParams) throws OWSException
    {
        return readURLParameters(queryParams, null, null);
    }
    
    
    /**
     * Helper method to read the version from the request XML
     * @param dom
     * @param objectElt
     * @return
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
     * Helper method to build an URL query from given query object
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
        	throw new OWSException(unsupportedSpec + spec, e);
        }
    }


    /**
     * Helper method to build a DOM element containing the request XML
     * Note that the element is not yet appended to any parent.
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
        	throw new OWSException(unsupportedSpec + spec, e);
        }
    }
    
    
    /**
     * Helper method to write any OWS XML request to an output stream
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
     * @param dom
     * @param responseElt
     * @param serviceType
     * @return
     * @throws OWSException
     */
    public OWSResponse readXMLResponse(DOMHelper dom, Element responseElt, String serviceType) throws OWSException
    {
        return readXMLResponse(dom, responseElt, serviceType, null, null);
    }
    
    
    /**
     * Helper method to parse any OWSResponse from service type and response type only
     * This tries to guess the version from a version attribute or the end of the namespace uri
     * @param dom
     * @param responseElt
     * @param serviceType
     * @param responseType
     * @return
     * @throws OWSException
     */
    public OWSResponse readXMLResponse(DOMHelper dom, Element responseElt, String serviceType, String responseType) throws OWSException
    {
    	return readXMLResponse(dom, responseElt, serviceType, responseType, null);
    }
    
    
    /**
     * Helper method to parse any OWSResponse from service type, response type and version
     * @param dom
     * @param responseElt
     * @param serviceType
     * @param responseType
     * @param version
     * @return
     * @throws OWSException
     */
    public OWSResponse readXMLResponse(DOMHelper dom, Element responseElt, String serviceType, String responseType, String version) throws OWSException
    {
        // skip SOAP envelope if present
        if (responseElt.getNamespaceURI().equals(soap12Uri))
            responseElt = dom.getElement(responseElt, "Body/*");
        
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
            return response;
        }
        catch (IllegalStateException e)
        {
            String spec = serviceType + " " + responseType + " v" + version;
        	throw new OWSException(unsupportedSpec + spec, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS response directly from an InputStream
     * @param is
     * @param serviceType
     * @param responseType
     * @return
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
	 * @param dom
	 * @param response
	 * @param version
	 * @return
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
	    	throw new OWSException(unsupportedSpec + spec, e);
	    }
	}
	
	
	/**
	 * Helper method to build a DOM element containing the response XML
	 * in a version agnostic way. The element is not appended to any parent 
	 * @param dom
	 * @param response
	 * @return
	 */
	public Element buildXMLResponse(DOMHelper dom, OWSResponse response) throws OWSException
	{
		return buildXMLResponse(dom, response, response.getVersion());
	}
	
	
	/**
     * Helper method to write any OWS XML response to an output stream
     */
    public void writeXMLResponse(OutputStream os, OWSResponse response, String version) throws OWSException
    {
    	try
        {
            DOMHelper dom = new DOMHelper();
            Element responseElt = buildXMLResponse(dom, response, version);
            dom.serialize(responseElt, os, true);
        }
        catch (IOException e)
        {
            throw new OWSException(AbstractResponseWriter.ioError, e);
        }        
    }
    
    
    /**
     * Helper method to write any OWS XML response to an output stream
     */
    public void writeXMLResponse(OutputStream os, OWSResponse response) throws OWSException
    {
    	writeXMLResponse(os, response, response.getVersion());
    }
    
    
    /**
     * Helper method to write OWS exception reports
     * @param os
     * @param e
     */
    public void writeXMLException(OutputStream os, String serviceType, String version, OWSException e)
    {
    	OWSExceptionWriter writer = new OWSExceptionWriter();
    	String owsVersion = OGCRegistry.getOWSVersion(serviceType, version);
    	e.setVersion(owsVersion);    	
		writer.writeException(os, e);
    }
    
    
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
            OGCExceptionReader.checkException(dom);
            return (ResponseType)readXMLResponse(dom, dom.getBaseElement(), request.getService(), dom.getBaseElement().getLocalName(), request.getVersion());
        }
        catch (Exception e)
        {
            throw new OWSException("Error while parsing response", e);
        }     
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using GET
     * @param request OWSQuery object
     * @param usePost true if using POST
     * @return HTTP Connection Object
     * @throws OWSException
     */
    public HttpURLConnection sendGetRequest(OWSRequest request) throws OWSException
    {
	    String requestString = null;
        
        try
        {
        	String endpoint = request.getGetServer();
        	
        	if (endpoint == null)
        		endpoint = request.getPostServer();
        	
        	if (endpoint == null)
        		throw new OWSException(invalidEndpoint);
            
            requestString = buildURLQuery(request);                
            URL url = new URL(requestString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            
            // catch server side exceptions
            if (connection.getResponseCode() > 202)
            	OGCExceptionReader.parseException(connection.getErrorStream());
            
            return connection;
        }
        catch (OGCException e)
		{
	    	throw new OWSException(e.getMessage());
		}
        catch (IOException e)
        {
            throw new OWSException(ioError + "\n" + requestString, e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using POST
     * @param request OWSQuery object
     * @return HTTP Connection Object
     * @throws OWSException
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
        		throw new OWSException(invalidEndpoint);
        	
            // remove ? at the end of Endpoint URL
            if (endpoint.endsWith("?"))
                url = new URL(endpoint.substring(0, endpoint.length()-1));
            else
                url = new URL(endpoint);
            
            // initiatlize HTTP connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "text/xml");            
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            // send post data            
            writeXMLQuery(out, request);
            out.flush();
            connection.connect();
            out.close();
            
            // catch server side exceptions
            if (connection.getResponseCode() > 202)
            	OGCExceptionReader.parseException(connection.getErrorStream());
            
            // return server response stream
            return connection;
        }
	    catch (OGCException e)
		{
	    	throw new OWSException(e.getMessage());
		}
        catch (IOException e)
        {
        	ByteArrayOutputStream buf = new ByteArrayOutputStream();
        	writeXMLQuery(buf, request);
        	throw new OWSException(ioError + "\n" + buf, e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using SOAP
     * @param request OWSQuery object
     * @return HTTP Connection Object
     * @throws OWSException
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
        		throw new OWSException(invalidEndpoint);
        	
            // remove ? at the end of Endpoint URL
            if (endpoint.endsWith("?"))
                url = new URL(endpoint.substring(0, endpoint.length()-1));
            else
                url = new URL(endpoint);
            
            // initialize HTTP connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", "text/xml");
            connection.setRequestProperty("SOAPAction", request.getOperation());
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            // send post data
            DOMHelper dom = new DOMHelper();
            dom.addUserPrefix("soap", soap11Uri);
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
                        
            // return server response stream
            return connection;
        }
	    catch (OGCException e)
		{
	    	throw new OWSException(e.getMessage());
		}
        catch (IOException e)
        {
        	ByteArrayOutputStream buf = new ByteArrayOutputStream();
        	writeXMLQuery(buf, request);
        	throw new OWSException(ioError + "\n" + buf, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS capabilities document from an XML/DOM tree
     */
    public OWSServiceCapabilities readCapabilities(DOMHelper dom, Element capsElt, String service) throws OWSException
    {
        // read common params and check that they're present
        String version = dom.getAttributeValue(capsElt, "@version");
        String message = "Capabilities";
        
        try
        {
            OWSResponseReader<OWSResponse> reader = (OWSResponseReader<OWSResponse>)OGCRegistry.createReader(service, message, version);
            OWSServiceCapabilities caps = (OWSServiceCapabilities)reader.readXMLResponse(dom, capsElt);
            return caps;
        }
        catch (IllegalStateException e)
        {
            String spec = service + " " + message + " v" + version;
        	throw new OWSException(unsupportedSpec + spec, e);
        }
    }

    
    /**
     * Helper method to get capabilities from an OWS service and parse it
     * @param server
     * @param serviceType
     * @param version
     * @return
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
        catch (IOException e)
		{
			throw new OWSException(ioError, e);
		}
        catch (DOMHelperException e)
		{
			throw new OWSException(AbstractResponseReader.invalidXML, e);
		}  
        catch (IllegalStateException e)
        {
        	String spec = serviceType + " Capabilities v" + version;
        	throw new OWSException(unsupportedSpec + spec, e);
        }
    }
}
