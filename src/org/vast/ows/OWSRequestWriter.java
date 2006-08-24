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
import java.net.HttpURLConnection;
import java.net.URL;
import org.w3c.dom.*;

import org.vast.io.xml.DOMWriter;


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
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public abstract class OWSRequestWriter
{
	
	public OWSRequestWriter()
	{	
	}

	
	/**
	 * Builds a String containing the GET request URL
	 * @param query
	 * @return
	 */
	public abstract String buildGetRequest(OWSQuery query) throws OWSException;
	
	
	/**
	 * Builds a DOM element containing the request XML
	 * @param query
	 * @return
	 */
	public abstract Element buildRequestXML(OWSQuery query, DOMWriter domWriter) throws OWSException;
	
	
	/**
	 * Builds a String containing the POST request
	 * @param owsQuery
	 * @return
	 * @throws OWSException
	 */
	public String buildPostRequest(OWSQuery owsQuery) throws OWSException
	{
		try
		{
			DOMWriter domWriter = new DOMWriter();
			domWriter.createDocument("Request");
			Element requestElt = buildRequestXML(owsQuery, domWriter);
			ByteArrayOutputStream charArray = new ByteArrayOutputStream();
			domWriter.writeDOM(requestElt, charArray, null);
			//System.out.println(charArray.toString());
			return charArray.toString();
		}
		catch (IOException e)
		{
			throw new OWSException("IO Error while building request", e);
		}
	}
	
	
	/**
	 * Sends the request using either GET or POST
	 * @param query OWSQuery object
	 * @param usePost true if using POST
	 * @return Server Response InputStream
	 * @throws OWSException
	 */
	public InputStream sendRequest(OWSQuery query, boolean usePost) throws OWSException
	{
		String requestString = null;
		
		try
		{
			boolean canDoPost = (query.getPostServer() != null);
            boolean canDoGet = (query.getGetServer() != null);
            
            if (usePost && canDoPost)
			{
				String server = query.getPostServer();
				URL url;
				
				// make sure server URL is valid
				if (server.endsWith("?"))
					url = new URL(server.substring(0, server.length()-1));
				else
					url = new URL(server);
				
				// initiatlize HTTP connection
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.setRequestProperty( "Content-type", "text/plain" );
				PrintStream out = new PrintStream(connection.getOutputStream());
				
				// send post data
				requestString = buildPostRequest(query);
				out.print(requestString);
				out.flush();
				connection.connect();
				out.close();
				
				// return server response stream
				return connection.getInputStream();
			}
			else if (canDoGet)
			{
				requestString = buildGetRequest(query);
				URL url = new URL(requestString);
				return url.openStream();
			}
            else
                throw new OWSException("No GET or POST endpoint URL specified in request");
		}
		catch (IOException e)
		{
			throw new OWSException("Error while sending request:\n" + requestString, e);
		}
	}
}