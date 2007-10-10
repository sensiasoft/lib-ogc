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
import java.util.ArrayList;
import java.util.Hashtable;
import org.vast.ogc.OGCRegistry;


/**
 * <p><b>Title:</b><br/>
 * OWS Query
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base class for all OWS style query objects
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 27, 2005
 * @version 1.0
 */
public class OWSQuery
{
	protected final static String EXCEPTION_MIME_TYPE = "application/vnd.ogc.se_xml";
	protected OutputStream responseStream;
	protected String getServer;
	protected String postServer;
	protected String service;
	protected String version;
	protected String request;
    protected String exceptionType;
    protected Hashtable<String, String> vendorParameters;
    
	
    public OWSQuery()
    {
    	exceptionType = EXCEPTION_MIME_TYPE;
    }
    
    
    public void checkParameters() throws OWSException
    {
    	ArrayList<String> missingParams = new ArrayList<String>();
		checkParameters(missingParams);
    }
    
    
    /**
	 * Checks that OWS common mandatory parameters are present
	 * @param query
	 * @param missingParams
	 */
	protected void checkParameters(ArrayList<String> missingParams) throws OWSException
	{
		// need VERSION
		if (this.getVersion() == null)
			missingParams.add(0, "VERSION");
		
		// need REQUEST
		if (this.getRequest() == null)
			missingParams.add(0, "REQUEST");
		
		// need SERVICE
		if (this.getService() == null)
			missingParams.add(0, "SERVICE");
				
		// send exception with all missing parameters
		if (!missingParams.isEmpty())
		{
			StringBuffer params = new StringBuffer();
			int listSize = missingParams.size();
			for (int i=0; i<listSize; i++)
			{
				params.append(missingParams.get(i));
				if (i < listSize-1)
					params.append(", ");
			}
			
			throw new OWSException("Missing " + params.toString() + " parameter(s)");
		}
	}
    
	
	/**
	 * Method used to normalize server KVP endpoint (always add ? or &) 
	 * @param url
	 * @return
	 */
	public static String checkServer(String url)
    {
    	if (url == null) return null;
		
		// Sometimes the server name already contains the ?
        // It can also contain a first argument and the final &
        String firstChar = "?";
        if (url.endsWith("&") || url.endsWith("?"))
            firstChar = "";
        else if (url.indexOf("?") != -1)
            firstChar = "&";
        
        return url + firstChar;
    }
	
	
	public OutputStream getResponseStream()
	{
		return responseStream;
	}


	public void setResponseStream(OutputStream responseStream)
	{
		this.responseStream = responseStream;
	}
	
	
	public String getGetServer()
	{
		return getServer;
	}


	public void setGetServer(String getServer)
	{
		this.getServer = checkServer(getServer);
	}


	public String getPostServer()
	{
		return postServer;
	}


	public void setPostServer(String postServer)
	{
		this.postServer = checkServer(postServer);
	}


	public String getRequest()
	{
		return request;
	}


	public void setRequest(String request)
	{
		this.request = request;
	}


	public String getService()
	{
		return service;
	}


	public void setService(String service)
	{
		this.service = service;
	}


	public String getVersion()
	{
		return version;
	}
	
	
	public String getNormalizedVersion()
	{
		return OGCRegistry.normalizeVersionString(version);
	}


	public void setVersion(String version)
	{
		this.version = version;
	}


    public String getExceptionType()
    {
        return exceptionType;
    }


    public void setExceptionType(String exceptionType)
    {
        this.exceptionType = exceptionType;
    }
    
    
    public Hashtable<String, String> getVendorParameters()
	{
		return vendorParameters;
	}
}
