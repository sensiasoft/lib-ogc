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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.xml.namespace.QName;
import org.vast.swe.SWEData;
import org.vast.util.DateTime;
import org.w3c.dom.Element;


/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin
 * @date Feb 25, 2008
 * */
public class StatusReport
{
	// request status codes
	public enum RequestStatus {Pending, Accepted, Rejected};
	
	// task status codes
	public enum TaskStatus {Reserved, InExecution, Completed, Cancelled, Expired, Failed};
	
	protected String description;
	protected String title;
	protected String id;
	protected Map<QName, Object> extensions;
	protected String taskID;
	protected DateTime estimatedToC;
	protected String eventCode;
	protected float percentCompletion = Float.NaN;
	protected String sensorID;
	protected RequestStatus requestStatus;
	protected String statusMessage;
	protected TaskStatus taskStatus;
	protected DateTime lastUpdate;
	protected SWEData taskingParameters;
	

	public StatusReport()
	{
		extensions = new HashMap<QName, Object>();
	}
	
	
	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public String getTaskID()
	{
		return taskID;
	}


	public void setTaskID(String id)
	{
		this.taskID = id;
	}


	public String getSensorID()
	{
		return sensorID;
	}


	public void setSensorID(String sensorId)
	{
		this.sensorID = sensorId;
	}


	public DateTime getLastUpdate()
	{
		return lastUpdate;
	}


	public void setLastUpdate(DateTime lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}


	public RequestStatus getRequestStatus()
	{
		return requestStatus;
	}


	public void setRequestStatus(RequestStatus statusCode)
	{
		this.requestStatus = statusCode;
	}
	
	
	public TaskStatus getTaskStatus()
	{
		return taskStatus;
	}


	public void setTaskStatus(TaskStatus statusCode)
	{
		this.taskStatus = statusCode;
	}
	
	
	public String getEventCode()
	{
		return eventCode;
	}


	public void setEventCode(String eventCode)
	{
		this.eventCode = eventCode;
	}


	public float getPercentCompletion()
	{
		return percentCompletion;
	}


	public void setPercentCompletion(float percentCompletion)
	{
		this.percentCompletion = percentCompletion;
	}


	public String getStatusMessage()
	{
		return statusMessage;
	}


	public void setStatusMessage(String statusMessage)
	{
		this.statusMessage = statusMessage;
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


	public DateTime getEstimatedToC()
	{
		return estimatedToC;
	}
	
	
	public double getEstimatedDelay()
	{
		double toc = estimatedToC.getJulianTime();
		double now = new DateTime().getJulianTime();
		return toc - now;
	}


	public void setEstimatedToC(DateTime estimatedToC)
	{
		this.estimatedToC = estimatedToC;
	}
	
	
	public void setEstimatedDelay(int seconds)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.add(Calendar.SECOND, seconds);
		cal.set(Calendar.MILLISECOND, 0);
		this.estimatedToC = new DateTime(cal.getTimeInMillis());
	}
	
	
	public void touch()
	{
		this.lastUpdate = new DateTime();
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
