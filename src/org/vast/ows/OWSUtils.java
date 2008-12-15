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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * OWS Utils
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Utility methods for common stuffs in OGC services
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Jan 16, 2007
 * @version 1.0
 */
public class OWSUtils implements OWSRequestReader<OWSRequest>, OWSRequestWriter<OWSRequest>
{
	public final static String OWS = "OWS";
	public final static String WMS = "WMS";
	public final static String WFS = "WFS";
	public final static String WCS = "WCS";
	public final static String SOS = "SOS";
	public final static String CSW = "CSW";
	public final static String WNS = "WNS";
	public final static String SAS = "SAS";	
	public final static String SPS = "SPS";
	public final static String WPS = "WPS";	
	
	public final static String soapUri = "http://schemas.xmlsoap.org/soap/envelope/";
	public final static String unsupportedSpec = "No support for ";
    public final static String invalidEndpoint = "No Endpoint URL specified in request object";
    public final static String ioError = "IO Error while sending request:";
    
    
    public OWSUtils()
    {
    	OWSUtils.loadMaps();
    }
    
    
    public static void loadMaps()
    {
    	String mapFileUrl = OWSUtils.class.getResource("OWSRegistry.xml").toString();
    	OGCRegistry.loadMaps(mapFileUrl, false);
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
     * Helper method to parse any OWS query from a URL query string
     * The service type is also specified to check if service in query is correct
     * The default version is used in case no version is specified in the query
     */
    public OWSRequest readURLQuery(String queryString, String serviceType, String defaultVersion) throws OWSException
    {
    	OWSRequest request = new OWSRequest();
    	request.setVersion(defaultVersion);
    	
    	try
        {
    		try {queryString = URLDecoder.decode(queryString, "UTF-8");}
    		catch (UnsupportedEncodingException e){}
    		
    		// read common params and check that they're present
    		AbstractRequestReader.readCommonQueryArguments(queryString, request);
    		OWSExceptionReport report = new OWSExceptionReport();
    		AbstractRequestReader.checkParameters(request, report, serviceType);
            report.process();
        
            OWSRequestReader<OWSRequest> reader = (OWSRequestReader<OWSRequest>)OGCRegistry.createReader(request.service, request.operation, request.version);
            request = reader.readURLQuery(queryString);
            return request;
        }
        catch (IllegalStateException e)
        {
        	String spec = request.service + " " + request.operation + " v" + request.version;
        	throw new OWSException(unsupportedSpec + spec, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * The service type is also specified in case it is missing in the query
     */
    public OWSRequest readURLQuery(String queryString, String service) throws OWSException
    {
    	return readURLQuery(queryString, service, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     */
    public OWSRequest readURLQuery(String queryString) throws OWSException
    {
        return readURLQuery(queryString, null);
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
            dom.serialize(requestElt, os, null);
        }
        catch (IOException e)
        {
            throw new OWSException(AbstractRequestWriter.ioError, e);
        }        
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
    	String version = readXMLVersion(dom, responseElt);
    	return readXMLResponse(dom, responseElt, serviceType, responseType, version);
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
            dom.serialize(responseElt, os, null);
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
            return (HttpURLConnection)url.openConnection();
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
            
            // return server response stream
            return connection;
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
            
            // initiatlize HTTP connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", "text/xml");
            connection.setRequestProperty("SOAPAction", request.getOperation());
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            // send post data
            DOMHelper dom = new DOMHelper();
            dom.addUserPrefix("soap", soapUri);
            Element envElt = dom.createElement("soap:Envelope");		
			Element bodyElt = dom.addElement(envElt, "soap:Body");
			Element reqElt = buildXMLQuery(dom, request);
            bodyElt.appendChild(reqElt);
            dom.serialize(envElt, out, false);
            out.flush();
            connection.connect();
            out.close();
            
            // return server response stream
            return connection;
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
