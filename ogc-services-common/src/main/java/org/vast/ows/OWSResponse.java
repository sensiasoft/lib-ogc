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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.ogc.OGCRegistry;


/**
 * 
 * <p>
 * Base class for all OWS service responses
 * </p>
 *
 * @author Alex Robin
 * @since 21 nov. 07
 * */
public class OWSResponse
{
	protected String service;
	protected String version;
	protected String messageType;
	protected Map<QName, Object> extensions;
	
	
    public OWSResponse()
    {
    	extensions = new HashMap<QName, Object>();
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


	public String getMessageType()
	{
		return messageType;
	}
    
	
    public Map<QName, Object> getExtensions()
	{
		return extensions;
	}
}
