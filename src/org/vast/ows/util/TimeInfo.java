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

import org.vast.physics.TimeExtent;
import org.vast.util.DateTimeFormat;


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
public class TimeInfo extends TimeExtent
{
    protected int timeZone = 0;
    
    
    @Override
    public TimeInfo copy()
    {
        TimeInfo timeInfo = new TimeInfo();
        
        timeInfo.baseTime = this.getBaseTime();
        timeInfo.timeBias = this.timeBias;
        timeInfo.timeStep = this.timeStep;
        timeInfo.leadTimeDelta = this.leadTimeDelta;
        timeInfo.lagTimeDelta = this.lagTimeDelta;
        timeInfo.baseAtNow = this.baseAtNow;
        timeInfo.endNow = this.endNow;
        timeInfo.beginNow = this.beginNow;
        timeInfo.timeZone = this.timeZone;
        
        return timeInfo;
    }
    
    
    public double getStartTime()
    {
        return getAdjustedLagTime();
    }
    
    
    /**
     * Helper method to set start time
     * @param startTime
     */
    public void setStartTime(double startTime)
    {
        beginNow = false;
        
        if (Double.isNaN(baseTime) || baseAtNow)
        {
            baseTime = startTime;
            lagTimeDelta = 0.0;
            baseAtNow = false;
        }
        
        else if (startTime > baseTime)
        {
            double stopTime = baseTime + leadTimeDelta;
            baseTime = startTime;
            leadTimeDelta = Math.max(0.0, stopTime - baseTime);
            lagTimeDelta = 0.0;
        }
        
        else
        {
            lagTimeDelta = baseTime - startTime;
        }
    }


    public double getStopTime()
    {
        return getAdjustedLeadTime();
    }
    
    
    /**
     * Helper method to set stop time
     * @param stopTime
     */
    public void setStopTime(double stopTime)
    {
        endNow = false;
        
        if (Double.isNaN(baseTime) || baseAtNow)
        {
            baseTime = stopTime;
            leadTimeDelta = 0.0;
            baseAtNow = false;
        }
        
        else if (stopTime < baseTime)
        {
            double startTime = baseTime - lagTimeDelta;
            baseTime = stopTime;
            lagTimeDelta = Math.max(0.0, baseTime - startTime);
            leadTimeDelta = 0.0;
        }
        
        else
        {
            lagTimeDelta = stopTime - baseTime;
        }
    }
	
	
	public boolean isTimeInstant()
	{
        if (leadTimeDelta == 0 && lagTimeDelta == 0)
			return true;
		else
			return false; 
	}
    
    
    public String getIsoString(int zone)
    {
        if (baseAtNow)
        {
            String start = beginNow ? "now" : "unknown";
            String stop = endNow ? "now" : "unknown";
            String duration = DateTimeFormat.formatIsoPeriod(getTimeRange());
            return start + "/" + stop + "/" + duration;
        }
        else
        {
            String start = beginNow ? "now" : DateTimeFormat.formatIso(getStartTime(), zone);
            String stop = endNow ? "now" : DateTimeFormat.formatIso(getStopTime(), zone);
            return start + "/" + stop;
        }
    }


    public int getTimeZone()
    {
        return timeZone;
    }


    public void setTimeZone(int timeZone)
    {
        this.timeZone = timeZone;
    }
}