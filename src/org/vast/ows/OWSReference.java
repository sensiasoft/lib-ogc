/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.util.ArrayList;
import org.w3c.dom.Element;


public class OWSReference
{
	protected String role;
	protected String href;
	protected Element requestXML;
	
	protected String description;
	protected String identifier;
	protected ArrayList<Object> metadata;
	

	public OWSReference()
	{
		metadata = new ArrayList<Object>();
	}
	
	
	public String getRole()
	{
		return role;
	}


	public void setRole(String role)
	{
		this.role = role;
	}


	public String getHref()
	{
		return href;
	}


	public void setHref(String endpoint)
	{
		this.href = endpoint;
	}


	public Element getRequestXML()
	{
		return requestXML;
	}


	public void setRequestXML(Element requestXML)
	{
		this.requestXML = requestXML;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


	public String getIdentifier()
	{
		return identifier;
	}


	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}


	public ArrayList<Object> getMetadata()
	{
		return metadata;
	}
}
