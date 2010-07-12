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
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.StringTokenizer;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.util.Bbox;
import org.vast.util.DateTimeFormat;
import org.vast.util.TimeInfo;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * Abstract class for all OWS Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST OWS request and
 * create an OWSQuery object
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Nov 4, 2005
 * @version 1.0
 */
public abstract class AbstractRequestReader<RequestType extends OWSRequest> implements OWSRequestReader<RequestType>
{
	protected final static String versionRegex = "^[0-9]+\\.[0-9]+(\\.[0-9]+)?$";
	protected final static String invalidReq = "Invalid Request";
	protected final static String invalidKVP = "Invalid KVP Request";
    protected final static String invalidXML = "Invalid XML Request";
    protected final static String invalidValue = "Invalid Value for ";
    protected final static String noKVP = "KVP request not supported in ";
    protected final static String noXML = "XML request not supported in ";
    
    
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
    protected TimeInfo parseTimeArg(String argValue) throws OWSException
    {
    	TimeInfo timeInfo = new TimeInfo();
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
        
        return timeInfo;
    }
    
    
    /**
     * Utility method to parse bbox argument from request
     * Format is minX,minY{,minZ},maxX,maxY{,maxZ}{,crs}
     * @param bbox
     * @param argValue
     */
    protected Bbox parseBboxArg(String bboxText) throws OWSException
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
        
        return bbox;
    }
    
    
    /**
     * Utility method to parse vector composed of comma/space separated decimal values
     * @param argValue
     * @return
     */
    public static double[] parseVector(String vectorText) throws OWSException
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
	 * @param request
	 */
	public static void readCommonXML(DOMHelper dom, Element requestElt, OWSRequest request)
	{
		request.setOperation(requestElt.getLocalName());
		request.setService(dom.getAttributeValue(requestElt, "service"));
		request.setVersion(dom.getAttributeValue(requestElt, "version"));
	}
	
	
	/**
     * Helper method to read service, operation name and version from any OWS query string
     * @param request
     * @param queryString
     * @return
     */
    public static void readCommonQueryArguments(String queryString, OWSRequest request) throws OWSException
    {
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
            if (argName.equalsIgnoreCase("service"))
            {
                request.setService(argValue);
            }
            
            // service version
            else if (argName.equalsIgnoreCase("version"))
            {
                request.setVersion(argValue);
            }

            // request argument
            else if (argName.equalsIgnoreCase("request"))
            {
                request.setOperation(argValue);
            }
        }
    }
    
    
	/**
	 * Helper method to read KVP extensions
	 * @param argName
	 * @param argValue
	 * @param request
	 * @throws OWSException
	 */
    public static void addKVPExtension(String argName, String argValue, OWSRequest request) throws OWSException
    {
    	request.getExtensions().add((argName + "=" + argValue));
    }
    	
	
    public static void checkParameters(OWSRequest request, OWSExceptionReport report) throws OWSException
    {
    	checkParameters(request, report, null);
    }
    
    
	/**
	 * Checks that OWS common mandatory parameters are present
	 * @param request
	 * @param report
	 */
	public static void checkParameters(OWSRequest request, OWSExceptionReport report, String serviceType) throws OWSException
	{
		// need SERVICE
		if (request.getService() == null)
			report.add(new OWSException(OWSException.missing_param_code, "SERVICE"));
		
		// must be correct service 
		else if (serviceType != null)
		{
			String reqService = request.getService();
			if (!reqService.equalsIgnoreCase(serviceType))
				report.add(new OWSException(OWSException.invalid_param_code, "SERVICE", reqService, ""));
		}
		
		// need VERSION
		//  VERSION is no longer required parameter for all services.  SOS doesn't use it at all
		//  
		if (request.getVersion() == null)
		{
			report.add(new OWSException(OWSException.missing_param_code, "VERSION"));
		}
		
		// check version validity
		else if (!request.getVersion().matches(versionRegex))
		{
			OWSException ex = new OWSException(OWSException.invalid_param_code, "VERSION");
			ex.setBadValue(request.getVersion());
			report.add(ex);
		}
		
		// need REQUEST
		if (request.getOperation() == null)
			report.add(new OWSException(OWSException.missing_param_code, "REQUEST"));
	}
}