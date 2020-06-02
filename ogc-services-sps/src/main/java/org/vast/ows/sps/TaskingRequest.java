
package org.vast.ows.sps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import net.opengis.swe.v20.ValidationException;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSRequest;
import org.vast.ows.ParameterizedRequest;
import org.vast.swe.SWEData;
import org.vast.util.DateTime;


/**
 * <p>
 * Base class for all SPS tasking requests
 * </p>
 *
 * @author Alex Robin
 * @date Feb 25, 2008
 * */
public abstract class TaskingRequest extends OWSRequest implements ParameterizedRequest
{
	protected String procedureID;
	protected SWEData taskingParameters;
	protected DateTime latestResponseTime;


	public String getProcedureID()
	{
		return procedureID;
	}


	public void setProcedureID(String sensorID)
	{
		this.procedureID = sensorID;
	}


	@Override
    public SWEData getParameters()
	{
		return taskingParameters;
	}


	@Override
    public void setParameters(SWEData taskingParameters)
	{
		this.taskingParameters = taskingParameters;
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
	
	
	public void validate() throws OWSException
	{
		List<ValidationException> errorList = new ArrayList<ValidationException>();
		taskingParameters.validateData(errorList);
		if (errorList.size() == 0)
			return;
		
		OWSExceptionReport report = new OWSExceptionReport();
		for (int i=0; i<errorList.size(); i++)
		{
			String loc = errorList.get(i).getLocator();
			String msg = errorList.get(i).getMessage();
			report.add(new SPSException(SPSException.invalid_param_code, "taskingParameters/" + loc, null, msg));
		}		
		throw report;
	}

}