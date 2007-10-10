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
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
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
public abstract class AbstractRequestReader<RequestType extends OWSRequest> implements OWSRequestReader<RequestType>
{
	protected final static String invalidReq = "Invalid Request";
	protected final static String invalidKVP = "Invalid KVP Request";
    protected final static String invalidXML = "Invalid XML Request";
    protected final static String invalidValue = "Invalid Value for ";
    
    
	public AbstractRequestReader()
	{	
	}
	
    
    public abstract RequestType readURLQuery(String queryString) throws OWSException;
	public abstract RequestType readXMLQuery(DOMHelper domHelper, Element requestElt) throws OWSException;
	
    
    public RequestType readXMLQuery(InputStream input) throws OWSException
	{
		try
		{
            DOMHelper dom = new DOMHelper(input, false);
			return readXMLQuery(dom, dom.getBaseElement());
		}
		catch (DOMHelperException e)
		{
			throw new OWSException(e);
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
        
        try
        {
            // parse start time
            if (timeRange[0].equalsIgnoreCase("now"))
                timeInfo.setBeginNow(true);
            else
                timeInfo.setStartTime(DateTimeFormat.parseIso(timeRange[0]));
            
            // parse stop time if present
            if (timeRange.length > 1)
            {
                if (timeRange[1].equalsIgnoreCase("now"))
                    timeInfo.setEndNow(true);
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
            throw new OWSException(invalidKVP + ": Invalid Time: " + argValue);
        }
        
        // make sure deltas are null for time instant
        if (timeRange.length == 1)
            timeInfo.setDeltaTimes(0, 0);
    }
    
    
    /**
     * Utility method to parse bbox argument from request
     * Format is minX,minY{,minZ},maxX,maxY{,maxZ}{,crs}
     * @param bbox
     * @param argValue
     */
    protected void parseBboxArg(Bbox bbox, String bboxText) throws OWSException
    {
        try
        {
            String[] coords = bboxText.trim().split("[ ,]");
            
            // case of 1D
            if (coords.length == 2 || coords.length == 3)
            {
	            bbox.setMinX(Double.parseDouble(coords[0]));
	            bbox.setMaxX(Double.parseDouble(coords[1]));
            }
            
            // case of 2D
            else if (coords.length == 4 || coords.length == 5)
            {
	            bbox.setMinX(Double.parseDouble(coords[0]));
	            bbox.setMinY(Double.parseDouble(coords[1]));
	            bbox.setMaxX(Double.parseDouble(coords[2]));
	            bbox.setMaxY(Double.parseDouble(coords[3]));
            }
            
            // case of 3D
            else if (coords.length == 6 || coords.length == 7)
            {
            	bbox.setMinX(Double.parseDouble(coords[0]));
	            bbox.setMinY(Double.parseDouble(coords[1]));
	            bbox.setMaxX(Double.parseDouble(coords[2]));
	            bbox.setMaxY(Double.parseDouble(coords[3]));
	            bbox.setMinZ(Double.parseDouble(coords[4]));
	            bbox.setMaxZ(Double.parseDouble(coords[5]));
            }
            
            else
            	throw new Exception();
            
            // try to parse crs id as the last part
            // if number of coords is odd
            if (coords.length % 2 != 0)
            {
            	String crs = coords[coords.length-1];
            	bbox.setCrs(crs);
            }
        }
        catch (Exception e)
        {
            throw new OWSException(invalidReq + ": Invalid Bbox: " + bboxText, e);
        }
    }
    
    
    /**
     * Utility method to parse vector composed of comma/space separated decimal values
     * @param argValue
     * @return
     */
    protected double[] parseVector(String vectorText) throws OWSException
    {
    	try
        {
    		String[] elts = vectorText.trim().split("[ ,]");
    		double[] vec = new double[elts.length];
	    	
    		for (int i=0; i<elts.length; i++)
    			vec[i] = Double.parseDouble(elts[i]);
    			
	    	return vec;
        }
        catch (NumberFormatException e)
        {
            throw new OWSException(invalidReq + ": Invalid Vector: " + vectorText, e);
        }
    }
	
	
	/**
	 * Reads common XML request parameters and fill up the OWSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @param query
	 */
	protected void readCommonXML(DOMHelper dom, Element requestElt, OWSRequest query)
	{
		query.setOperation(requestElt.getLocalName());
		query.setService(dom.getAttributeValue(requestElt, "service"));
		query.setVersion(dom.getAttributeValue(requestElt, "version"));
	}
}