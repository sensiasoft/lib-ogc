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
 Tony Cook <tcook@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.server;

import javax.servlet.http.*;
import javax.servlet.*;

import java.io.*;
import java.text.ParseException;
import java.util.*;

import org.vast.ows.OWSException;
import org.vast.ows.sos.SOSException;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;
import org.vast.ows.wcs.WCSException;
import org.vast.ows.wcs.WCSQuery;
import org.vast.util.*;


//import org.vast.io.xml.*;

/**
 * <p>Title: WCSServlet</p>
 *
 * <p>Description:
 * Base abstract class for implementing WCS servlets
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 * @author Alexandre Robin, Tony Cook
 * @version 1.0
 */
public abstract class WCSServlet extends OWSServlet
{
    // Table of WCS handlers: 1 for each ObservationSet
    protected Hashtable<String, WCSHandler> dataSetHandlers = new Hashtable<String, WCSHandler>();


    // Sends an XML Exception to the user
    protected void sendErrorMessage(OutputStream resp, String message)
    {
        PrintWriter buffer = new PrintWriter(resp);
        buffer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buffer.println("<ServiceException>");
        buffer.println("\t" + message);
        buffer.println("</ServiceException>");
        buffer.flush();
        buffer.close();
        //this.log(message);
    }


    protected void processQuery(WCSQuery query) throws Exception
    {
        String requestName = query.getRequest();

        // GetCapabilities request
        if (requestName.equalsIgnoreCase("GetCapabilities"))
        {
            sendCapabilities("ALL", query.getResponseStream());
            return;
        }

        // GetObservation request
        else if (requestName.equalsIgnoreCase("GetCoverage"))
        {
            //DOMReader capsReader = new DOMReader(this.capsDoc);
            String layer = query.getLayer();

            if (layer == null)
                throw new WCSException("WCS request missing LAYER.");

            WCSHandler handler = dataSetHandlers.get(layer);

            if (handler != null)
            {
                /*
                 Iterator<TimeInfo> it;

                 // first check all requested times
                 it = query.times.iterator();
                 while (it.hasNext()) {
                 query.time = it.next();
                 checkQueryTime(query, capsReader);
                 }
                 
                 // check query format
                 //checkQueryFormat(query, capsReader);

                 // then retrieve observation for each time or period
                 it = query.times.iterator();
                 while (it.hasNext()) {
                 query.time = it.next();
                 handler.getCoverage(query);
                 }*/

                handler.getCoverage(query);
            }
            else
                throw new WCSException("LAYER " + layer + " is unavailable on this server");
        }

        // Unrecognized request type
        else
        {
            throw new WCSException("Invalid request: Use GetCapabilities or GetCoverage");
        }
    }


    /**
     * Parse and process HTTP GET request
     * TODO  Modify to use WCSRequestReader
     */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
    {
        String invalidQuery = "Invalid WCS GET request";

        // parse query arguments
        WCSQuery queryInfo = new WCSQuery();
        String query = req.getQueryString();
        //this.log("GET REQUEST: " + query + " from IP " + req.getRemoteAddr() + " (" + req.getRemoteHost() + ")");

        try
        {
            if (query == null)
                sendErrorMessage(resp.getOutputStream(), invalidQuery);

            StringTokenizer st = new StringTokenizer(query, "&");
            queryInfo.setResponseStream(resp.getOutputStream());

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
                    throw new SOSException(invalidQuery);
                }

                // parse args into WCSQuery object
                if (argName.equalsIgnoreCase("request"))
                {
                    queryInfo.setRequest(argValue);
                }
                else if (argName.equalsIgnoreCase("layers") || argName.equalsIgnoreCase("layer"))
                {
                    queryInfo.setLayer(argValue);
                }
                else if (argName.equalsIgnoreCase("format"))
                {
                    queryInfo.setFormat(argValue);
                }
                else if (argName.equalsIgnoreCase("skipX"))
                {
                    queryInfo.setSkipX(Integer.parseInt(argValue));
                }
                else if (argName.equalsIgnoreCase("skipY"))
                {
                    queryInfo.setSkipY(Integer.parseInt(argValue));
                }
                else if (argName.equalsIgnoreCase("bbox"))
                {
                    String[] bbStr = argValue.split(",");
                    Bbox bbox = new Bbox();
                    queryInfo.setBbox(bbox);
                    bbox.setMinX(Double.parseDouble(bbStr[0]));
                    bbox.setMinY(Double.parseDouble(bbStr[1]));
                    bbox.setMaxX(Double.parseDouble(bbStr[2]));
                    bbox.setMaxY(Double.parseDouble(bbStr[3]));
                    //					bbox.westLon = bbox.minX;
                    //					bbox.eastLon = bbox.maxX;
                    //					bbox.northLat = bbox.maxY;
                    //					bbox.southLat = bbox.minY;
                }

                // singleTime
                //  
                else if (argName.equalsIgnoreCase("time"))
                {
                    TimeInfo timeInfo = new TimeInfo();
                	if(argValue.equalsIgnoreCase("now")){
                		timeInfo.setBeginNow(true);
                        timeInfo.setStartTime(System.currentTimeMillis() / 1000);
                    } else {
                    	//  Added to support time range (this needs to be migrated
                    	//  to WCSRequestReader
                    	this.parseTimeArg(timeInfo, argValue);
                    	//timeInfo.setStartTime(DateTimeFormat.parseIso(argValue));
                    	//timeInfo.setStopTime(timeInfo.getStartTime());
                    }
                    queryInfo.getTimes().add(timeInfo);
                }

                //  Don't throw exception- just ignore any args we don't understand for now
                //				else
                //					throw new WCSException(invalidQuery);
            }

            resp.setContentType("text/xml");
            this.processQuery(queryInfo);
        }
        catch (WCSException e)
        {
            try
            {
                sendErrorMessage(resp.getOutputStream(), e.getMessage());
            }
            catch (IOException e1)
            {
                e.printStackTrace();
            }
        }
        catch (ParseException e)
        {
            
            try
            {
                sendErrorMessage(resp.getOutputStream(), "Invalid time format: use ISO8601");
            }
            catch (IOException e1)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            throw new ServletException(internalErrorMsg, e);
        }
        finally
        {
            try
            {
                resp.getOutputStream().close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * Parse and process HTTP POST request
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
    {
        //WCSQuery queryInfo = new WCSQuery();
        this.log("POST REQUEST from IP " + req.getRemoteAddr() + " (" + req.getRemoteHost() + ")");

        /** Your code **/
        /** Should parse request and call processQuery(queryInfo) **/
    }


    /**
     * Adds a new handler for the given coverage ID
     * @param coverageID
     * @param handlerClass
     */
    public void addDataSetHandler(String coverageID, WCSHandler handlerClass)
    {
        dataSetHandlers.put(coverageID, handlerClass);
    }


    public void removeDataSetHandler(String dataSetID)
    {
        dataSetHandlers.remove(dataSetID);
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
                timeInfo.setBeginNow(true);
                timeInfo.setStartTime(now);
            }
            else
                timeInfo.setStartTime(DateTimeFormat.parseIso(timeRange[0]));
            
            // parse stop time if present
            if (timeRange.length > 1)
            {
                if (timeRange[1].equalsIgnoreCase("now"))
                {
                    timeInfo.setEndNow(true);
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
            throw new OWSException("Invalid WCS Get Request: Invalid Time: " + argValue);
        }
        
        // copy start to stop
        if (timeRange.length == 1)
            timeInfo.setStopTime(timeInfo.getStartTime());  
    }    
}
