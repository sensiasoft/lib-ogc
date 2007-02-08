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

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
public class OWSUtils implements OWSRequestReader<OWSQuery>, OWSRequestWriter<OWSQuery>
{
    public final static String requestParsingError = "Error while parsing request";
    public final static String requestWritingError = "Error while building request";
    public final static String capsParsingError = "Error while parsing capabilities document";
    
    protected boolean showRequest = false;
    
    
    /**
     * Helper method to parse any OWS query from an XML/DOM tree
     * @param dom
     * @param requestElt
     * @return
     * @throws OWSException
     */
    public OWSQuery readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
    {
        String queryType = requestElt.getLocalName();
        String serviceType = dom.getAttributeValue(requestElt, "@service");
        String version = readXMLVersion(dom, requestElt);
        
        try
        {
            OWSRequestReader reader = (OWSRequestReader)OGCRegistry.createReader(serviceType, queryType, version);
            OWSQuery query = reader.readXMLQuery(dom, requestElt);
            return query;
        }
        catch (Exception e)
        {
            throw new OWSException(requestParsingError, e);
        }
    }
    
    
    public OWSQuery readXMLQuery(InputStream is) throws OWSException
    {
        return null;
    }
    
    
    /**
     * Helper method to parse any OWS query from a URL query string
     * @param dom
     * @param requestElt
     * @return
     * @throws OWSException
     */
    public OWSQuery readURLQuery(String queryString) throws OWSException
    {
        OWSQuery query = readCommonQueryArguments(queryString);
        
        try
        {
            OWSRequestReader reader = (OWSRequestReader)OGCRegistry.createReader(query.service, query.request, query.version);
            query = reader.readURLQuery(queryString);
            return query;
        }
        catch (Exception e)
        {
            throw new OWSException(requestParsingError, e);
        }
    }
    
    
    /**
     * Helper method to read service, operation name and version from any OWS query string
     * @param queryString
     * @return
     */
    public OWSQuery readCommonQueryArguments(String queryString) throws OWSException
    {
        OWSQuery query = new OWSQuery();
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
                throw new OWSException(AbstractRequestReader.invalidGet);
            }
            
            // service ID
            if (argName.equalsIgnoreCase("service"))
            {
                query.setService(argValue);
            }
            
            // service version
            else if (argName.equalsIgnoreCase("version"))
            {
                query.setVersion(argValue);
            }

            // request argument
            else if (argName.equalsIgnoreCase("request"))
            {
                query.setRequest(argValue);
            }
        }
        
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
     * @param query
     * @return
     * @throws OWSException
     */
    public String buildURLQuery(OWSQuery query) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSQuery> writer = (OWSRequestWriter)OGCRegistry.createWriter(query.service, query.request, query.version);
            writer.setPrintRequest(showRequest);
            String queryString = writer.buildURLQuery(query);
            return queryString;
        }
        catch (Exception e)
        {
            throw new OWSException(requestWritingError, e);
        }
    }


    /**
     * Helper method to build a DOM element containing the request XML
     * Note that the element is not yet appended to any parent.
     * @param query
     * @return
     */
    public Element buildXMLQuery(DOMHelper dom, OWSQuery query) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSQuery> writer = (OWSRequestWriter)OGCRegistry.createWriter(query.service, query.request, query.version);
            writer.setPrintRequest(showRequest);
            Element requestElt = writer.buildXMLQuery(dom, query);
            return requestElt;
        }
        catch (Exception e)
        {
            throw new OWSException(requestWritingError, e);
        }
    }
    
    
    /**
     * Helper method to write any OWS XML request to an output stream
     * @param os
     * @param query
     * @throws OWSException
     */
    public void writeXMLQuery(OutputStream os, OWSQuery query) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSQuery> writer = (OWSRequestWriter)OGCRegistry.createWriter(query.service, query.request, query.version);
            writer.setPrintRequest(showRequest);
            writer.writeXMLQuery(os, query);
        }
        catch (Exception e)
        {
            throw new OWSException(requestWritingError, e);
        }        
    }


    /**
     * Helper method to send any OWS request to the server URL using either GET or POST
     * @param query OWSQuery object
     * @param usePost true if using POST
     * @return Server Response InputStream
     * @throws OWSException
     */
    public HttpURLConnection sendRequest(OWSQuery query, boolean usePost) throws OWSException
    {
        try
        {
            OWSRequestWriter<OWSQuery> writer = (OWSRequestWriter)OGCRegistry.createWriter(query.service, query.request, query.version);
            writer.setPrintRequest(showRequest);
            HttpURLConnection connection = writer.sendRequest(query, usePost);
            return connection;
        }
        catch (Exception e)
        {
            throw new OWSException(requestWritingError, e);
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
        catch (Exception e)
        {
            throw new OWSException(capsParsingError, e);
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
        try
        {
            String version = readXMLVersion(dom, capsElt);
            OWSCapabilitiesReader reader = (OWSCapabilitiesReader)OGCRegistry.createReader(serviceType, "Capabilities", version);
            OWSServiceCapabilities caps = reader.readCapabilities(dom, capsElt);
            return caps;
        }
        catch (Exception e)
        {
            throw new OWSException(capsParsingError, e);
        }
    }
    
    
    /**
     * Toggles request printing by MessageSystem
     * @param print
     */
    public void setPrintRequest(boolean print)
    {
        showRequest = print;
    }
    
}
