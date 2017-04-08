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
 Tony Cook <tcook@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.io.IOException;
import java.util.*;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.server.OWSServlet;
import org.vast.ows.wcs.DescribeCoverageRequest;
import org.vast.ows.wcs.GetCoverageRequest;
import org.vast.ows.wcs.WCSException;


/**
 * <p>Title: WCSServlet</p>
 *
 * <p>Description:
 * Base abstract class for implementing WCS servlets
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * */
public abstract class WCSServlet extends OWSServlet
{
    private static final long serialVersionUID = 7155979257491196521L;
    protected String DEFAULT_VERSION = "1.0";
    protected OWSUtils owsUtils = new OWSUtils();
    
	// Table of WCS handlers: 1 for each coverage offering
    protected Hashtable<String, WCSHandler> dataSetHandlers = new Hashtable<String, WCSHandler>();
        

    @Override
    public void handleRequest(OWSRequest request) throws OWSException
    {
        if (request instanceof GetCoverageRequest)
        {
            processQuery((GetCoverageRequest) request);
        }
        else
            throw new OWSException("Unsupported operation " + request.getOperation());
    }
    

    protected void processQuery(GetCoverageRequest query) throws OWSException
    {
    	String coverage = query.getCoverage();
        WCSHandler handler = dataSetHandlers.get(coverage);

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

            try
            {
                handler.getCoverage(query);
            }
            catch (IOException e)
            {
                throw new OWSException("Error retrieving coverage data", e);
            }
        }
        else
            throw new WCSException("COVERAGE " + coverage + " is unavailable on this server");        
    }
    
    
    protected void processQuery(DescribeCoverageRequest query) throws Exception
    {
    	
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
}
