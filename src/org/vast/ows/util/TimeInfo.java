/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.util;


/**
 * <p><b>Title:</b><br/>
 * TimeInfo
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Simple structure containing OWS style request time
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 9, 2005
 * @version 1.0
 */
public class TimeInfo
{
	protected double startTime;
	protected double stopTime;
	protected double stepTime;
	
	
	public double getStartTime()
	{
		return startTime;
	}


	public void setStartTime(double startTime)
	{
		this.startTime = startTime;
	}


	public double getStepTime()
	{
		return stepTime;
	}


	public void setStepTime(double stepTime)
	{
		this.stepTime = stepTime;
	}


	public double getStopTime()
	{
		return stopTime;
	}


	public void setStopTime(double stopTime)
	{
		this.stopTime = stopTime;
	}
	
	
	public boolean isTimeInstant()
	{
		if (startTime == stopTime)
			return true;
		else
			return false; 
	}
}