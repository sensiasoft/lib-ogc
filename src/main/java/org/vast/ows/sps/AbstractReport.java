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
import java.util.TimeZone;
import org.vast.util.DateTime;


public class AbstractReport
{
	public static final String PENDING = "PENDING";
	public static final String ACCEPTED = "ACCEPTED";
	public static final String REJECTED = "REJECTED";
	
	protected String taskID;
	protected String sensorID;
	protected String title;
	protected String description;
	protected DateTime lastUpdate;
	protected String statusCode;
	protected DateTime estimatedToC;


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


	public String getStatusCode()
	{
		return statusCode;
	}


	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
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
}