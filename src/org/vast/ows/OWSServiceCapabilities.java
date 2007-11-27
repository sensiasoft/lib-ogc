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

import java.util.*;
import org.vast.util.ResponsibleParty;


/**
 * <p><b>Title:</b><br/>
 * OWS Capabilities
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Representation of an OWS service capabilities document.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 27, 2005
 * @version 1.0
 */
public class OWSServiceCapabilities extends OWSResponse
{
	// service identification
	protected OWSIdentification identification;
	
	// additional service options
	protected String fees;
	protected String accessConstraints;
	protected List<String> supportedVersions;
	
	// service provider
	protected ResponsibleParty serviceProvider;
		
	// list of layers
	protected List<OWSLayerCapabilities> layers;
	
	// list of supported exception types
	protected List<String> exceptionTypes;
	
	// tables of operation->url
	protected Hashtable<String, String> getServers;
	protected Hashtable<String, String> postServers;
	
	
    public OWSServiceCapabilities()
    {
    	messageType = "Capabilities";
    	identification = new OWSIdentification();
    	serviceProvider = new ResponsibleParty();
    	supportedVersions = new ArrayList<String>(1);
    	layers = new ArrayList<OWSLayerCapabilities>(5);
    	exceptionTypes = new ArrayList<String>(1);
    	getServers = new Hashtable<String, String>();
    	postServers = new Hashtable<String, String>();    	
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


	public void setVersion(String version)
	{
		this.version = version;
	}
	
	
	public OWSIdentification getIdentification()
	{
		return identification;
	}


	public void setIdentification(OWSIdentification identification)
	{
		this.identification = identification;
	}
	
	
	public String getFees()
	{
		return fees;
	}


	public void setFees(String fees)
	{
		this.fees = fees;
	}


	public String getAccessConstraints()
	{
		return accessConstraints;
	}


	public void setAccessConstraints(String accessConstraints)
	{
		this.accessConstraints = accessConstraints;
	}
	
	
	public List<String> getSupportedVersions()
	{
		return supportedVersions;
	}


	public ResponsibleParty getServiceProvider()
	{
		return serviceProvider;
	}


	public void setServiceProvider(ResponsibleParty serviceProvider)
	{
		this.serviceProvider = serviceProvider;
	}
    
    
    public Hashtable<String, String> getGetServers()
	{
		return getServers;
	}
    
    
	public void setGetServers(Hashtable<String, String> getServers)
	{
		this.getServers = getServers;
	}


	public Hashtable<String, String> getPostServers()
	{
		return postServers;
	}
	
	
	public void setPostServers(Hashtable<String, String> postServers)
	{
		this.postServers = postServers;
	}
	
	
	public List<OWSLayerCapabilities> getLayers()
	{
		return layers;
	}
	
	
	public void setLayers(List<OWSLayerCapabilities> layers)
	{
		this.layers = layers;
	}


	public List<String> getExceptionTypes()
	{
		return exceptionTypes;
	}


	public void setExceptionTypes(List<String> exceptionTypes)
	{
		this.exceptionTypes = exceptionTypes;
	}
	
	
	public String toString()
    {
    	StringBuffer text = new StringBuffer(service + " Service Capabilities: ");
    	
    	text.append(identification.getTitle());
    			
    	String desc = identification.getDescription();
    	if (desc != null)
    	{
    		text.append("\n");
        	text.append("Description: ");
    		text.append(desc);
    	}
    	
    	text.append("\n");
    	text.append("Layer list: \n");
    	
    	for (int i=0; i<layers.size(); i++)
    	{
    		text.append("   ");
    		text.append(layers.get(i).toString());
    		text.append("\n");
    	}
    	
    	return text.toString();
    }
}
