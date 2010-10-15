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
Mathieu Dhainaut <mathieu.dhainaut@ext.spotimage.fr>
Alexandre Robin <alexandre.robin@spotimage.fr>

 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.server;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.wms.GetMapRequest;


/**
 * <p>Title: WMSServlet</p>
 *
 * <p>Description:
 * Base abstract class for implementing WMS servlets
 * </p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 * @author Mathieu Dhainaut
 * @version 1.0
 */
public abstract class WMSServlet extends OWSServlet
{
	private static final long serialVersionUID = 1265879257871196681L;
	protected OWSUtils owsUtils = new OWSUtils();


	// Sends an XML Exception to the user
	@Override
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


	public void processQuery(GetCapabilitiesRequest query) throws Exception
	{
		sendCapabilities("ALL", query.getResponseStream());
	}


	public abstract void processQuery(GetMapRequest query) throws Exception;


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
			else if (query instanceof GetMapRequest)
			{
				resp.setContentType(((GetMapRequest) query).getFormat());
				processQuery((GetMapRequest) query);
			}
		}
		catch (OWSException e)
		{
			try
			{
				resp.setContentType("text/xml");
				sendErrorMessage(resp.getOutputStream(), "Invalid request or unrecognized version");
			}
			catch (IOException e1)
			{
				e.printStackTrace();
			}
		}
		catch (Exception e)
		{
			throw new ServletException(e);
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
		//this.log("POST REQUEST from IP " + req.getRemoteAddr() + " (" + req.getRemoteHost() + ")");
		/** Your code **/
		/** Should parse request and call processQuery(queryInfo) **/
	}
}
