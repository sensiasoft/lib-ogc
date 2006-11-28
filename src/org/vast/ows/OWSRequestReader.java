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

import java.io.*;
import java.text.ParseException;
import org.vast.io.xml.DOMReader;
import org.vast.io.xml.DOMReaderException;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;
import org.vast.util.DateTimeFormat;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * Abstract class for all OWS Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST SOS request and
 * create an OWSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 4, 2005
 * @version 1.0
 */
public abstract class OWSRequestReader
{
    protected final static String invalidGet = "Invalid SOS GET Request";
    protected final static String invalidPost = "Invalid SOS POST Request";
    
    
	public OWSRequestReader()
	{	
	}
	
		
	public abstract OWSQuery readGetRequest(String queryString) throws OWSException;	
	public abstract OWSQuery readRequestXML(DOMReader domReader, Element requestElt) throws OWSException;
	
	
	public OWSQuery readPostRequest(InputStream input) throws OWSException
	{
		try
		{
			DOMReader domReader = new DOMReader(input, false);
			return readRequestXML(domReader, domReader.getBaseElement());
		}
		catch (DOMReaderException e)
		{
			throw new OWSException("", e);
		}
	}
    
    
    /**
     * Utility method to parse time argument from GET request.
     * Format is YYYY-MM-DDTHH:MM:SS.sss/YYYY-MM-DDTHH:MM:SS.sss/PYMDTHMS
     * @param timeInfo
     * @param argValue
     */
    protected void parseTimeArg(TimeInfo timeInfo, String argValue) throws OWSException
    {
        String[] timeRange = argValue.split("/");
        double now = System.currentTimeMillis() / 1000;
        
        try
        {
            // parse start time
            if (timeRange[0].equalsIgnoreCase("now"))
            {
                //timeInfo.setBeginNow(true);
                timeInfo.setStartTime(now);
            }
            else
                timeInfo.setStartTime(DateTimeFormat.parseIso(timeRange[0]));
            
            // parse stop time if present
            if (timeRange.length > 1)
            {
                if (timeRange[1].equalsIgnoreCase("now"))
                {
                    //timeInfo.setEndNow(true);
                    timeInfo.setStopTime(now);
                }
                else
                    timeInfo.setStopTime(DateTimeFormat.parseIso(timeRange[1]));
            }
            
            // parse step time if present
            if (timeRange.length > 2)
            {
                timeInfo.setTimeStep(DateTimeFormat.parseIsoPeriod(timeRange[2]));
            }
        }
        catch (ParseException e)
        {
            throw new OWSException(invalidGet + ": Invalid Time: " + argValue);
        }
        
        // make sure deltas are null for time instant
        if (timeRange.length == 1)
            timeInfo.setDeltaTimes(0, 0);
    }
    
    
    /**
     * Utility method to parse bbox argument from GET request
     * Format is minY,minX,maxY,maxX
     * @param bbox
     * @param argValue
     */
    protected void parseBboxArg(Bbox bbox, String argValue) throws OWSException
    {
        try
        {
            String[] coords = argValue.split("[ ,]");
            
            bbox.setMinX(Double.parseDouble(coords[0]));
            bbox.setMinY(Double.parseDouble(coords[1]));
            bbox.setMaxX(Double.parseDouble(coords[2]));
            bbox.setMaxY(Double.parseDouble(coords[3]));
        }
        catch (Exception e)
        {
            throw new OWSException(invalidGet + ": Invalid Bbox: " + argValue, e);
        }
    }
	
	
	/**
	 * Reads common XML request parameters and fill up the OWSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @param query
	 */
	protected void readCommonXML(DOMReader dom, Element requestElt, OWSQuery query)
	{
		query.setRequest(requestElt.getLocalName());
		query.setService(dom.getAttributeValue(requestElt, "service"));
		query.setVersion(dom.getAttributeValue(requestElt, "version"));
	}
	
	
	/**
	 * Reads a GetCapabilities XML request and fill up the OWSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @param query
	 */
	protected void readGetCapabilitiesXML(DOMReader dom, Element requestElt, OWSQuery query)
	{
		query.setSection(dom.getElementValue(requestElt, "Section"));
	}
}