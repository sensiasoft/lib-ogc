/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License Version
1.1 (the "License"); you may not use this file except in compliance with
the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/MPL-1.1.html

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

The Original Code is the "OGC Service Framework".

The Initial Developer of the Original Code is the VAST team at the University
of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the
Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved.
Please Contact Mike Botts <mike.botts@uah.edu> for more information.

Contributor(s):
Alexandre Robin <alexandre.robin@spotimage.fr>

 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.server;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.text.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionWriter;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.util.PostRequestFilter;
import org.vast.ows.wfs.GetFeatureRequest;
import org.vast.util.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;


//import org.vast.io.xml.*;
/**
 * <p>Title: WFSServlet</p>
 *
 * <p>Description:
 * Base abstract class for implementing WFS servlets
 * </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public abstract class WFSServlet extends OWSServlet
{
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(WFSServlet.class);
	protected OWSUtils owsUtils = new OWSUtils();


	public void processQuery(GetCapabilitiesRequest query) throws Exception
	{
		sendCapabilities("ALL", query.getResponseStream());
	}


	public abstract void processQuery(GetFeatureRequest query) throws Exception;


	/**
	 * Parse and process HTTP GET request
	 * TODO  Modify to use WCSRequestReader
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		// parse query arguments
		try
		{
			//this.log("GET REQUEST: " + query + " from IP " + req.getRemoteAddr() + " (" + req.getRemoteHost() + ")");
			OWSRequest query = (OWSRequest) owsUtils.readURLQuery(req.getQueryString());
			query.setHttpRequest(req);
            query.setHttpResponse(resp);

			if (query instanceof GetCapabilitiesRequest)
			{
				resp.setContentType("text/xml");
				processQuery((GetCapabilitiesRequest) query);
			}
			else if (query instanceof GetFeatureRequest)
			{
				//resp.setContentType(((GetFeatureRequest) query).getFormat());
				resp.setContentType("text/xml");
				processQuery((GetFeatureRequest) query);
			}
		}
		catch (OWSException e)
		{
			try
			{
				resp.setContentType("text/xml");
				OWSExceptionWriter writer = new OWSExceptionWriter();
				writer.writeException(resp.getOutputStream(), e);
			}
			catch (IOException e1)
			{
			}
		}
		catch (Exception e)
		{
			try
			{
				resp.sendError(500, internalErrorMsg);
				log.error(internalErrorMsg, e);
			}
			catch (IOException e1)
			{
			}
		}
		finally
		{
			try
			{
				resp.getOutputStream().flush();
				resp.getOutputStream().close();
			}
			catch (IOException e)
			{
			}
		}
	}


	/**
	 * Parse and process HTTP POST request
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException
	{
		// parse query arguments
		try
		{
			InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(req.getInputStream()));
            DOMHelper dom = new DOMHelper(xmlRequest, false);
            OWSRequest query = (OWSRequest)owsUtils.readXMLQuery(dom, dom.getBaseElement());
			query.setHttpRequest(req);
            query.setHttpResponse(resp);

			if (query instanceof GetCapabilitiesRequest)
			{
				resp.setContentType("text/xml");
				processQuery((GetCapabilitiesRequest) query);
			}
			else if (query instanceof GetFeatureRequest)
			{
				//resp.setContentType(((GetFeatureRequest) query).getFormat());
				resp.setContentType("text/xml");
				processQuery((GetFeatureRequest) query);
			}
		}
		catch (OWSException e)
		{
			try
			{
				resp.setContentType("text/xml");
				OWSExceptionWriter writer = new OWSExceptionWriter();
				writer.writeException(resp.getOutputStream(), e);
			}
			catch (IOException e1)
			{
			}
		}
		catch (DOMHelperException e)
		{
            try
            {
                sendErrorMessage(resp.getOutputStream(), "Invalid XML request. Please check request format");
            }
            catch (IOException e1)
            {
            }
		}
		catch (Exception e)
		{
			try
			{
				resp.sendError(500, internalErrorMsg);
				log.error(internalErrorMsg, e);
			}
			catch (IOException e1)
			{
			}
		}
		finally
		{
			try
			{
				resp.getOutputStream().flush();
				resp.getOutputStream().close();
			}
			catch (IOException e)
			{
			}
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
				timeInfo.setBeginNow(true);
				timeInfo.setStartTime(now);
			}
			else
			{
				timeInfo.setStartTime(DateTimeFormat.parseIso(timeRange[0]));
			}

			// parse stop time if present
			if (timeRange.length > 1)
			{
				if (timeRange[1].equalsIgnoreCase("now"))
				{
					timeInfo.setEndNow(true);
					timeInfo.setStopTime(now);
				}
				else
				{
					timeInfo.setStopTime(DateTimeFormat.parseIso(timeRange[1]));
				}
			}

			// parse step time if present
			if (timeRange.length > 2)
			{
				timeInfo.setTimeStep(DateTimeFormat.parseIsoPeriod(timeRange[2]));
			}
		}
		catch (ParseException e)
		{
			throw new OWSException("Invalid WMS Get Request: Invalid Time: " + argValue);
		}

		// copy start to stop
		if (timeRange.length == 1)
		{
			timeInfo.setStopTime(timeInfo.getStartTime());
		}
	}
}
