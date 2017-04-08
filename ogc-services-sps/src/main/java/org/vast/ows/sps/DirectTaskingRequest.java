/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import net.opengis.swe.v20.DataEncoding;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.vast.ows.OWSRequest;
import org.vast.util.DateTime;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Container for SPS DirectTasking request parameters.<br/>
 * This is used to reserve a slot for direct exclusive access to a taskable asset
 * </p>
 *
 * @author Alex Robin
 * @date Jan 24, 2017
 * */
public class DirectTaskingRequest extends OWSRequest
{
    protected String procedureID;
    protected TimeExtent timeSlot;
    protected DataEncoding encoding;
    protected DateTime latestResponseTime;


    public DirectTaskingRequest()
    {
        setService(SPSUtils.SPS);
        setOperation("DirectTasking");
    }
    
    
    public String getProcedureID()
    {
        return procedureID;
    }


    public void setProcedureID(String sensorID)
    {
        this.procedureID = sensorID;
    }


    public DataEncoding getEncoding()
    {
        return encoding;
    }


    public void setEncoding(DataEncoding encoding)
    {
        this.encoding = encoding;
    }


    public TimeExtent getTimeSlot()
    {
        return timeSlot;
    }


    public void setTimeSlot(TimeExtent timeSlot)
    {
        this.timeSlot = timeSlot;
    }


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
}