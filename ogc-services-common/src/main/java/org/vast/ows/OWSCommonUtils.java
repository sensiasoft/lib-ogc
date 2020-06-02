/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.vast.ows.OWSException;
import org.vast.util.Bbox;
import org.vast.util.DateTimeFormat;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Helper methods for OWS readers/writers.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 14, 2014
 */
public class OWSCommonUtils
{
    public final static String invalidReq = "Invalid Request";
    public final static String invalidKVP = "Invalid KVP Request";
    public final static String invalidXML = "Invalid XML Request";
    public final static String invalidValue = "Invalid Value for ";
    
    protected DateTimeFormat timeFormat = new DateTimeFormat();
    
    
    /**
     * Parse parameters from KVP query into a map
     * @param queryString
     * @return map of query parameter-value pairs
     * @throws OWSException
     */
    public Map<String, String> parseQueryParameters(String queryString) throws OWSException
    {
        StringTokenizer st = new StringTokenizer(queryString, "&");
        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        
        while (st.hasMoreTokens())
        {
            String nextArg = st.nextToken();

            try
            {
                // separate argument name and value
                int sepIndex = nextArg.indexOf('=');
                String argName = nextArg.substring(0, sepIndex);
                String argValue = nextArg.substring(sepIndex + 1);
                
                // URL decode
                argValue = URLDecoder.decode(argValue, StandardCharsets.UTF_8.name());
                
                // add to map
                queryParams.put(argName.toLowerCase(), argValue);
            }
            catch (Exception e)
            {
                throw new OWSException(invalidKVP, e);
            }
        }
        
        return queryParams;
    }
    
    
    /**
     * Utility method to parse time argument from GET request.
     * Format is YYYY-MM-DDTHH:MM:SS.sss/YYYY-MM-DDTHH:MM:SS.sss/PYMDTHMS
     * @param argValue
     * @return TimeExtent object
     * @throws OWSException 
     */
    public TimeExtent parseTimeArg(String argValue) throws OWSException
    {
        TimeExtent timeInfo = new TimeExtent();
        String[] timeRange = argValue.split("/");
        
        try
        {
            // parse start time
            if (timeRange[0].equalsIgnoreCase("now")) {
                timeInfo.setBaseAtNow(true);
                timeInfo.setBeginNow(true);
                timeInfo.setEndNow(true);
            }
            else
                timeInfo.setStartTime(timeFormat.parseIso(timeRange[0]));
            
            // parse stop time if present
            if (timeRange.length > 1)
            {
                if (timeRange[1].equalsIgnoreCase("now"))
                    timeInfo.setEndNow(true);
                else
                    timeInfo.setStopTime(timeFormat.parseIso(timeRange[1]));
            }
            
            // parse step time if present
            if (timeRange.length > 2)
            {
                timeInfo.setTimeStep(timeFormat.parseIsoPeriod(timeRange[2]));
            }
        }
        catch (ParseException e)
        {
            throw new OWSException("Invalid time argument: " + argValue);
        }
        
        return timeInfo;
    }
    
    
    /**
     * Utility method to parse bbox argument from request
     * Format is minX,minY{,minZ},maxX,maxY{,maxZ}{,crs}
     * @param bboxText
     * @return Bboc parsed from given string representation
     * @throws OWSException 
     */
    public Bbox parseBboxArg(String bboxText) throws OWSException
    {
        Bbox bbox = new Bbox();
        
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
                throw new IllegalArgumentException("Wrong number of coordinates");
            
            // try to parse crs id as the last part
            // if number of coords is odd
            if (coords.length % 2 != 0)
            {
                String crs = coords[coords.length-1];
                bbox.setCrs(crs);
            }
            
            bbox.checkValid();
        }
        catch (Exception e)
        {
            throw new OWSException("Invalid bbox argument: " + bboxText, e);
        }
        
        return bbox;
    }
    
    
    /**
     * Utility method to parse vector composed of comma/space separated decimal values
     * @param vectorText text representation of vector coordinates
     * @return vector coordinates as double array
     * @throws OWSException 
     */
    public double[] parseVector(String vectorText) throws OWSException
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
            throw new OWSException("Invalid vector argument: " + vectorText, e);
        }
    }
    
}
