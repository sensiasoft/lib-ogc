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
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;
import org.vast.util.DateTimeFormat;


/**
 * <p><b>Title:</b><br/>
 * OWS Request Builder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base abstract class for all service specific POST/GET
 * request builders
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public abstract class AbstractRequestWriter<RequestType extends OWSRequest> implements OWSRequestWriter<RequestType>
{
    protected final static String noKVP = "KVP request not supported in ";
    protected final static String noXML = "XML request not supported in ";
    
    
	public AbstractRequestWriter()
	{	
	}

	
	public abstract String buildURLQuery(RequestType request) throws OWSException;		
	public abstract Element buildXMLQuery(DOMHelper dom, RequestType request) throws OWSException;
    
    
    public void writeXMLQuery(OutputStream os, RequestType request) throws OWSException
    {
        try
        {
            DOMHelper dom = new DOMHelper();
            Element requestElt = buildXMLQuery(dom, request);
            dom.serialize(requestElt, os, null);
        }
        catch (IOException e)
        {
            throw new OWSException("IO Error while writing XML request", e);
        }
    }
    
    
    /**
     * Utility method to add time argument to a GET request
     * Format is YYYY-MM-DDTHH:MM:SS.sss/YYYY-MM-DDTHH:MM:SS.sss/PYMDTHMS
     * @param buffer
     * @param time
     */
    protected void writeTimeArgument(StringBuffer buffer, TimeInfo time)
    {
        if (time.isTimeInstant())
        {
            if (time.isBaseAtNow())
                buffer.append("now");
            else
                buffer.append(DateTimeFormat.formatIso(time.getBaseTime(), 0));
        }
        else
        {
            // add start time
            if (time.isBeginNow())
                buffer.append("now");
            else
                buffer.append(DateTimeFormat.formatIso(time.getStartTime(), 0));
        
            // add stop and step time
            buffer.append('/');
            
            if (time.isEndNow())
                buffer.append("now");
            else
                buffer.append(DateTimeFormat.formatIso(time.getStopTime(), 0));
            
            // add time step if specified
            if (time.getTimeStep() > 0)
            {
                buffer.append('/');
                buffer.append(DateTimeFormat.formatIsoPeriod(time.getTimeStep()));
            }
        }
    }
    
    
    /**
     * Utility method to add bbox argument to a GET request
     * Format is minY,minX,maxY,maxX
     * @param buffer
     * @param bbox
     */
    protected void writeBboxArgument(StringBuffer buffer, Bbox bbox)
    {
        buffer.append(bbox.getMinX());
        buffer.append("," + bbox.getMinY());
        buffer.append("," + bbox.getMaxX());
        buffer.append("," + bbox.getMaxY());
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
     * @param dom
     * @param requestElt
     * @param request
     */
    protected void addCommonXML(DOMHelper dom, Element requestElt, OWSRequest request)
	{
    	requestElt.setAttribute("service", request.getService());
    	requestElt.setAttribute("version", request.getVersion());
	}
    
    
    /**
     * Adds common arguments to URL query
     * @param urlBuff
     * @param request
     */
    protected void addCommonArgs(StringBuffer urlBuff, OWSRequest request)
    {
        urlBuff.append("service=" + request.getService());
        urlBuff.append("&version=" + request.getVersion());
        urlBuff.append("&request=" + request.getOperation());
    }
}