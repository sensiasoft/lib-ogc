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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * <p>
 * Class representing an SPS task as a chronological list of status changes.
 * </p>
 *
 * @author Alex Robin
 * @date Feb 25, 2008
 * */
public class Task
{
	protected String id;
	protected String title;
	protected String description;
	protected Map<QName, Object> extensions;
	protected List<StatusReport> statusReports;
	
	
	public Task()
	{
		statusReports = new LinkedList<StatusReport>();
	}
	
	
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
	
	
	public List<StatusReport> getStatusReports()
	{
		return statusReports;
	}


	public void setStatusReports(List<StatusReport> statusReports)
	{
		this.statusReports = statusReports;
	}
	
	
	public void addStatusReport(StatusReport report)
	{
		this.statusReports.add(report);
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
