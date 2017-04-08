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

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.util.Bbox;
import org.vast.util.DateTimeFormat;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Base abstract class for all service specific POST/GET
 * request builders
 * </p>
 *
 * @author Alex Robin
 * @since Oct 30, 2005
 * * @param <RequestType> Type of OWS request object accepted by this writer
 */
public abstract class AbstractRequestWriter<RequestType extends OWSRequest> implements OWSRequestWriter<RequestType>
{
	public final static String ioError = "IO error while writing XML request to stream";
	protected final static String noKVP = "KVP request not supported in ";
    protected final static String noXML = "XML request not supported in ";
    	
    protected DateTimeFormat timeFormat = new DateTimeFormat();
    
    
	public AbstractRequestWriter()
	{	
	}

	public Map<String, String> buildURLParameters(RequestType request) throws OWSException
	{
	    throw new UnsupportedOperationException("buildURLParameters() method not implemented");
	}
	
	
	@Override
	public String buildURLQuery(RequestType request) throws OWSException
	{
	    Map<String, String> urlParams = buildURLParameters(request);
	    StringBuffer urlBuff = new StringBuffer(request.getGetServer());
	    Iterator<Entry<String, String>> it = urlParams.entrySet().iterator();
	    
	    while (it.hasNext())
	    {
	        Entry<String, String> arg = it.next();
	        urlBuff.append(arg.getKey());
	        urlBuff.append('=');
	        
	        // url encode parameter value
	        urlBuff.append(urlEncode(arg.getValue()));
	        urlBuff.append('&');
	    }
	    
	    urlBuff.deleteCharAt(urlBuff.length()-1);
	    return urlBuff.toString();
	}
	
    
    public void writeXMLQuery(OutputStream os, RequestType request) throws OWSException
    {
        try
        {
            DOMHelper dom = new DOMHelper();
            Element requestElt = buildXMLQuery(dom, request);
            dom.serialize(requestElt, os, true);
        }
        catch (IOException e)
        {
            throw new OWSException(ioError, e);
        }
    }
    
    
    /**
     * Helper method to properly encode certain URL query parameters
     * @param urlParam
     * @return
     */
    protected String urlEncode(String urlParam)
    {
        /*urlParam = urlParam.replaceAll(" ", "%20");
        urlParam = urlParam.replaceAll("\"", "%22");
        urlParam = urlParam.replaceAll("#", "%23");
        urlParam = urlParam.replaceAll("%", "%25");
        urlParam = urlParam.replaceAll("&", "%26");
        urlParam = urlParam.replaceAll("\\+", "%2B");
        urlParam = urlParam.replaceAll("\\?", "%3F");
        //urlParam = urlParam.replaceAll("\\/", "%2F");*/
        try
        {
            urlParam = URLEncoder.encode(urlParam, StandardCharsets.UTF_8.name());
        }
        catch (UnsupportedEncodingException e)
        {
            throw new IllegalStateException(e);
        }
        
        return urlParam;
    }
    
    
    /**
     * Utility method to add time argument to a GET request
     * Format is YYYY-MM-DDTHH:MM:SS.sss/YYYY-MM-DDTHH:MM:SS.sss/PYMDTHMS
     * @param buffer
     * @param time
     */
    protected void writeTimeArgument(StringBuilder buffer, TimeExtent time)
    {
        if (time.isTimeInstant())
        {
            if (time.isBaseAtNow())
                buffer.append("now");
            else
                buffer.append(timeFormat.formatIso(time.getBaseTime(), 0));
        }
        else
        {
            // add start time
            if (time.isBeginNow())
                buffer.append("now");
            else
                buffer.append(timeFormat.formatIso(time.getStartTime(), 0));
        
            // add stop and step time
            buffer.append('/');
            
            if (time.isEndNow())
                buffer.append("now");
            else
                buffer.append(timeFormat.formatIso(time.getStopTime(), 0));
            
            // add time step if specified
            if (time.getTimeStep() > 0)
            {
                buffer.append('/');
                buffer.append(timeFormat.formatIsoPeriod(time.getTimeStep()));
            }
        }
    }
    
    
    /**
     * Utility method to add bbox argument to a GET request.<br/>
     * Format is minY,minX,maxY,maxX
     * @param buffer string buffer to append to
     * @param bbox Bbox object to be serialized
     */
    protected void writeBboxArgument(StringBuilder buffer, Bbox bbox)
    {
        writeBboxArgument(buffer, bbox, false);
    }
    
    
    /**
     * Utility method to add bbox with CRS argument to a GET request.<br/>
     * Format is minY,minX,maxY,maxX[,crsUri]
     * @param buffer string buffer to append to
     * @param bbox Bbox object to be serialized
     * @param writeCrs true if crs URI should be appended at the end of the string
     */
    protected void writeBboxArgument(StringBuilder buffer, Bbox bbox, boolean writeCrs)
    {
        buffer.append(bbox.getMinX());
        buffer.append("," + bbox.getMinY());
        buffer.append("," + bbox.getMaxX());
        buffer.append("," + bbox.getMaxY());        
        if (bbox.getCrs() != null && writeCrs)
            buffer.append(',').append(bbox.getCrs());
    }
    
    
    /**
     * Create comma separated list for gml:coordinates XML
     * @param query
     * @return
     */
    protected String getGmlBboxCoordsList(Bbox bbox)
    {
        StringBuffer buff = new StringBuffer();
        
        buff.append(bbox.getMinX());
        buff.append("," + bbox.getMinY());
        buff.append(" " + bbox.getMaxX());
        buff.append("," + bbox.getMaxY());
        
        return buff.toString();
    }
    
    
    /**
     * Adds common attributes to XML request element
     * @param requestElt
     * @param request
     */
    protected void addCommonXML(DOMHelper dom, Element requestElt, OWSRequest request)
	{
    	dom.setAttributeValue(requestElt, "service", request.getService());
    	dom.setAttributeValue(requestElt, "version", request.getVersion());
	}
    
    
    /**
     * Adds common arguments to the map of URL parameters
     * @param urlParams
     * @param request
     */
    protected void addCommonArgs(Map<String, String> urlParams, OWSRequest request)
    {
        urlParams.put("service", request.getService());
        urlParams.put("version", request.getVersion());
        urlParams.put("request", request.getOperation());
    }
    
    
    /**
     * Adds common arguments to the URL query string
     * @param urlParams
     * @param request
     */
    protected void addCommonArgs(StringBuilder buf, OWSRequest request)
    {
        buf.append("service=");
        buf.append(request.getService());
        buf.append("&version=");
        buf.append(request.getVersion());
        buf.append("&request=");
        buf.append(request.getOperation());
    }
    
    
    /**
     * Helper method to append vendor extensions at the end of URL query
     * @param urlBuff
     * @param request
     */
    protected void writeKVPExtensions(StringBuilder urlBuff, OWSRequest request)
    {
    	for (Entry<QName, Object> extension : request.getExtensions().entrySet())
        {
            String paramName = extension.getKey().getLocalPart();
            Object extValue = extension.getValue();
            String paramValue = getExtensionKvpValue(extValue);
            
            if (paramValue != null)
            {
                urlBuff.append('&');
                urlBuff.append(paramName);
                urlBuff.append('=');                
                urlBuff.append(paramValue);
            }
        }
    }
    
    
    /**
     * Adds extension parameters to KVP request
     * @param urlParams
     * @param request
     */
    protected void writeKvpExtensions(Map<String, String> urlParams, OWSRequest request)
    {
        for (Entry<QName, Object> extension : request.getExtensions().entrySet())
        {
            String paramName = extension.getKey().getLocalPart();
            Object extValue = extension.getValue();
            String paramValue = getExtensionKvpValue(extValue);
            
            if (paramValue != null)
                urlParams.put(paramName, paramValue);
        }
    }
    
    
    protected String getExtensionKvpValue(Object extValue)
    {
        if (extValue instanceof String || extValue instanceof Number || extValue instanceof Boolean)
        {
            return extValue.toString();
        }
        else if (extValue instanceof Date)
        {
            return timeFormat.formatIso(((Date)extValue).getTime() / 1000.0, 0);
        }
        else if (extValue instanceof TimeExtent)
        {
            StringBuilder buf = new StringBuilder();
            this.writeTimeArgument(buf, (TimeExtent)extValue);
            return buf.toString();
        }
        
        return null;
    }
}