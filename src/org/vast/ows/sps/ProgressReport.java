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

import org.vast.sweCommon.SWEData;
import org.vast.util.DateTime;


/**
 * <p><b>Title:</b><br/>
 * Progress Report
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb 25, 2008
 * @version 1.0
 */
public class ProgressReport
{
	public final static String PENDING = "PENDING";
	public final static String CONFIRMED = "CONFIRMED";
	public final static String CANCELLED = "CANCELLED";
	public final static String FAILED = "FAILED";
	public final static String DELAYED = "DELAYED";
	public final static String STARTED = "STARTED";
	public final static String FINISHED = "FINISHED";

	protected String id;
	protected String statusCode;
	protected String description;
	protected DateTime estimatedToC;
	protected SWEData reportParameters;


	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public String getStatusCode()
	{
		return statusCode;
	}


	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
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


	public void setEstimatedToC(DateTime estimatedToC)
	{
		this.estimatedToC = estimatedToC;
	}


	public SWEData getReportParameters()
	{
		return reportParameters;
	}


	public void setReportParameters(SWEData reportParameters)
	{
		this.reportParameters = reportParameters;
	}
}
