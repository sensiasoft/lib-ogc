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

package org.vast.ows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.vast.ogc.OGCRegistry;
import org.vast.xml.DOMHelper;
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
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Jan 16, 2007
 * @version 1.0
 */
public class OWSUtils implements OWSRequestReader<OWSRequest>, OWSRequestWriter<OWSRequest>
{
    public final static String soapUri = "http://schemas.xmlsoap.org/soap/envelope/";    
	public final static String unsupportedVersion = "Unsupported Request Version: ";
    public final static String invalidEndpoint = "No Endpoint URL specified in request object";
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     */
    public OWSRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
    {
        String queryType = requestElt.getLocalName();
        String serviceType = dom.getAttributeValue(requestElt, "@service");
        String version = readXMLVersion(dom, requestElt);
        
        try
        {
            OWSRequestReader reader = (OWSRequestReader)OGCRegistry.createReader(serviceType, queryType, version);
            OWSRequest query = reader.readXMLQuery(dom, requestElt);
            return query;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + version, e);
        }
    }
    
    
    /**
     * Helper method to parse any OWS query directly from an InputStream
     */
    public OWSRequest readXMLQuery(InputStream is) throws OWSException
    {
        return null;
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     */
    public OWSRequest readURLQuery(String queryString) throws OWSException
    {
        return readURLQuery(queryString, null);
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * The service type is also specified in case it is missing in the query
     */
    public OWSRequest readURLQuery(String queryString, String service) throws OWSException
    {
    	OWSRequest query = null;
    	
    	try
        {
    		try {queryString = URLDecoder.decode(queryString, "UTF-8");}
    		catch (UnsupportedEncodingException e){}
    		query = readCommonQueryArguments(queryString);
            
            if (service != null)
                query.setService(service);        
        
            OWSRequestReader reader = (OWSRequestReader)OGCRegistry.createReader(query.service, query.operation, query.version);
            query = reader.readURLQuery(queryString);
            return query;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + query.version, e);
        }
    }
    
    
    /**
     * Helper method to read service, operation name and version from any OWS query string
     * @param queryString
     * @return
     */
    public OWSRequest readCommonQueryArguments(String queryString) throws OWSException
    {
        OWSRequest query = new OWSRequest();
        try {queryString = URLDecoder.decode(queryString, "UTF-8");}
        catch (UnsupportedEncodingException e) {};
        StringTokenizer st = new StringTokenizer(queryString, "&");
        
        while (st.hasMoreTokens())
        {
            String argName = null;
            String argValue = null;
            String nextArg = st.nextToken();

            // separate argument name and value
            try
            {
                int sepIndex = nextArg.indexOf('=');
                argName = nextArg.substring(0, sepIndex);
                argValue = nextArg.substring(sepIndex + 1);
            }
            catch (IndexOutOfBoundsException e)
            {
                throw new OWSException(AbstractRequestReader.invalidKVP);
            }
            
            // service ID
            if (argName.equalsIgnoreCase("SERVICE"))
            {
                query.setService(argValue);
            }
            
            // service version
            else if (argName.equalsIgnoreCase("VERSION"))
            {
                query.setVersion(argValue);
            }

            // request argument
            else if (argName.equalsIgnoreCase("REQUEST"))
            {
                query.setOperation(argValue);
            }
        }
        
        query.checkParameters(new ArrayList<String>());
        return query;
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
        
        // throw exception if no version is specified
        if (version == null || version.length() == 0)
            throw new OWSException("Version attribute is mandatory");
        
        return version;
    }
    
    
    /**
     * Helper method to build an URL query from given query object
     */
    public String buildURLQuery(OWSRequest query) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSRequest> writer = (OWSRequestWriter)OGCRegistry.createWriter(query.service, query.operation, query.version);
            String url = writer.buildURLQuery(query);
            return url;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + query.version);
        }
    }


    /**
     * Helper method to build a DOM element containing the request XML
     * Note that the element is not yet appended to any parent.
     */
    public Element buildXMLQuery(DOMHelper dom, OWSRequest query) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSRequest> writer = (OWSRequestWriter)OGCRegistry.createWriter(query.service, query.operation, query.version);
            Element requestElt = writer.buildXMLQuery(dom, query);
            return requestElt;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + query.version, e);
        }
    }
    
    
    /**
     * Helper method to write any OWS XML request to an output stream
     */
    public void writeXMLQuery(OutputStream os, OWSRequest query) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSRequest> writer = (OWSRequestWriter)OGCRegistry.createWriter(query.service, query.operation, query.version);
            writer.writeXMLQuery(os, query);
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + query.version, e);
        }        
    }


    /**
     * Helper method to send any OWS request to the server URL using GET
     * @param query OWSQuery object
     * @param usePost true if using POST
     * @return HTTP Connection Object
     * @throws OWSException
     */
    public HttpURLConnection sendGetRequest(OWSRequest query) throws OWSException
    {
	    String requestString = null;
        
        try
        {
        	String endpoint = query.getGetServer();
        	
        	if (endpoint == null)
        		endpoint = query.getPostServer();
        	
        	if (endpoint == null)
        		throw new OWSException(invalidEndpoint);
            
            requestString = buildURLQuery(query);                
            URL url = new URL(requestString);
            return (HttpURLConnection)url.openConnection();
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + query.version, e);
        }
        catch (IOException e)
        {
            throw new OWSException("IO Error while sending request:\n" + requestString, e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using POST
     * @param query OWSQuery object
     * @return HTTP Connection Object
     * @throws OWSException
     */
    public HttpURLConnection sendPostRequest(OWSRequest query) throws OWSException
    {
	    String requestString = null;
        
        try
        {
        	URL url;
        	String endpoint = query.getPostServer();
        	
        	if (endpoint == null)
        		endpoint = query.getGetServer();
        	
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
            connection.setRequestProperty("SOAPAction", query.getOperation());
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            // send post data            
            writeXMLQuery(out, query);
            out.flush();
            connection.connect();
            out.close();
            
            // return server response stream
            return connection;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + query.version, e);
        }
        catch (IOException e)
        {
            throw new OWSException("IO Error while sending request:\n" + requestString, e);
        }
    }
    
    
    /**
     * Helper method to send any OWS request to the server URL using SOAP
     * @param query OWSQuery object
     * @return HTTP Connection Object
     * @throws OWSException
     */
    public HttpURLConnection sendSoapRequest(OWSRequest query) throws OWSException
    {
	    String requestString = null;
        
        try
        {
        	URL url;
        	String endpoint = query.getPostServer();
        	
        	if (endpoint == null)
        		endpoint = query.getGetServer();
        	
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
            connection.setRequestProperty( "Content-type", "text/xml");
            PrintStream out = new PrintStream(connection.getOutputStream());
            
            // send post data
            DOMHelper dom = new DOMHelper();
            dom.addUserPrefix("soap", "soapUri");
            Element envElt = dom.createElement("soap:Envelope");		
			Element bodyElt = dom.addElement(envElt, "soap:Body");
			Element reqElt = buildXMLQuery(dom, query);
            bodyElt.appendChild(reqElt);
            dom.serialize(envElt, out, false);
            out.flush();
            connection.connect();
            out.close();
            
            // return server response stream
            return connection;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + query.version, e);
        }
        catch (IOException e)
        {
            throw new OWSException("IO Error while sending request:\n" + requestString, e);
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
            OWSCapabilitiesReader reader = (OWSCapabilitiesReader)OGCRegistry.createReader(serviceType, "Capabilities", version);
            OWSServiceCapabilities caps = reader.getCapabilities(server, version);
            return caps;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + version, e);
        }
    }
    
    
    /**
     * Helper method to parse capabilities from the given DOM Element
     * @param dom
     * @param serviceType
     * @param capsElt
     * @return
     * @throws OWSException
     */
    public OWSServiceCapabilities readCapabilities(DOMHelper dom, Element capsElt, String serviceType) throws OWSException
    {
    	String version = null;
    	
    	try
        {
            version = readXMLVersion(dom, capsElt);
            OWSCapabilitiesReader reader = (OWSCapabilitiesReader)OGCRegistry.createReader(serviceType, "Capabilities", version);
            OWSServiceCapabilities caps = reader.readCapabilities(dom, capsElt);
            return caps;
        }
        catch (IllegalStateException e)
        {
            throw new OWSException(unsupportedVersion + version, e);
        }
    }
    
}
