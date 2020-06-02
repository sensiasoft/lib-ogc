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

package org.vast.ows.sps;

import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.swe.SWEData;
import org.w3c.dom.Element;


/**
 * <p>
 * Container for alternative tasking parameters
 * </p>
 *
 * @author Alex Robin
 * @date Feb 25, 2008
 * */
public class Alternative
{
	protected String id;
	protected String title;
	protected String description;
	protected SWEData taskingParameters;
	protected Map<QName, Object> extensions;
	

	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}
	
	
	public String getTitle()
	{
		return title;
	}


	public void setTitle(String title)
	{
		this.title = title;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
	public SWEData getTaskingParameters()
	{
		return taskingParameters;
	}


	public void setTaskingParameters(SWEData taskingParameters)
	{
		this.taskingParameters = taskingParameters;
	}
	
	
	public Map<QName, Object> getExtensions()
	{
		return extensions;
	}


	public void addExtension(Element extElt)
	{
		QName qname = new QName(extElt.getNamespaceURI(), extElt.getLocalName());
		extensions.put(qname, extElt);
	}
}
