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

import org.vast.ows.OWSRequest;
import org.vast.sweCommon.SweData;
import org.vast.util.DateTime;


/**
 * <p><b>Title:</b><br/>
 * Submit Request
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for SPS Submit request parameters
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb 25, 2008
 * @version 1.0
 */
public class SubmitRequest extends OWSRequest
{
	protected String sensorID;
	protected String feasibilityID;
	protected SweData taskingParameters;
	protected SweData additionalParameters;
	protected DateTime timeFrame;
	
	
	public SubmitRequest()
	{
		setService("SPS");
		setOperation("Submit");
	}
	
	
	public String getSensorID()
	{
		return sensorID;
	}


	public void setSensorID(String sensorID)
	{
		this.sensorID = sensorID;
	}


	public String getFeasibilityID()
	{
		return feasibilityID;
	}


	public void setFeasibilityID(String feasibilityStudyID)
	{
		this.feasibilityID = feasibilityStudyID;
	}


	public SweData getTaskingParameters()
	{
		return taskingParameters;
	}


	public void setTaskingParameters(SweData taskingParameters)
	{
		this.taskingParameters = taskingParameters;
	}


	public SweData getAdditionalParameters()
	{
		return additionalParameters;
	}


	public void setAdditionalParameters(SweData additionalParameters)
	{
		this.additionalParameters = additionalParameters;
	}


	public DateTime getTimeFrame()
	{
		return timeFrame;
	}


	public void setTimeFrame(DateTime timeFrame)
	{
		this.timeFrame = timeFrame;
	}
}
