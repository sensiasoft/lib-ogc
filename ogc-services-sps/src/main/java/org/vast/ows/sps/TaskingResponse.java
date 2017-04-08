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
import org.vast.ows.OWSResponse;
import org.vast.util.DateTime;


/**
 * <p>
 * Container for a tasking response.
 * (i.e. GetFeasibility, Submit, Update, Reserve or Confirm operations)
 * </p>
 *
 * @author Alex Robin
 * @date Feb, 29 2008
 * @param <Report> Type of status report contained in this response
 */
public abstract class TaskingResponse<Report extends StatusReport> extends OWSResponse
{
	protected DateTime latestResponseTime;
	protected Report report;


	public DateTime getLatestResponseTime()
	{
		return latestResponseTime;
	}


	public void setLatestResponseTime(DateTime latestResponseTime)
	{
		this.latestResponseTime = latestResponseTime;
	}
	
	
	public void setMaxResponseDelay(int seconds)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		cal.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.add(Calendar.SECOND, seconds);
		cal.set(Calendar.MILLISECOND, 0);
		this.latestResponseTime = new DateTime(cal.getTimeInMillis());
	}
	
	
	public Report getReport()
	{
		return report;
	}


	public void setReport(Report report)
	{
		this.report = report;
	}
}
