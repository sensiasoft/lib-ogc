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
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.*;

import org.vast.ows.OWSQuery;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * SOS Query
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for SOS query parameters
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 27, 2005
 * @version 1.0
 */
public class SOSQuery extends OWSQuery
{
	protected String offering;
	protected String format;
	protected TimeInfo time;
	protected Bbox bbox;

	protected List<String> observables;
	protected List<String> procedures;

	
	public SOSQuery()
	{
		service = "SOS";
		bbox = new Bbox();
		time = new TimeInfo();
		observables = new ArrayList<String>(2);
		procedures = new ArrayList<String>(2);		
	}
	

	public String getFormat()
	{
		return format;
	}


	public void setFormat(String format)
	{
		this.format = format;
	}


	public String getOffering()
	{
		return offering;
	}


	public void setOffering(String offering)
	{
		this.offering = offering;
	}
	
	
	public List<String> getObservables()
	{
		return observables;
	}


	public void setObservables(List<String> observables)
	{
		this.observables = observables;
	}


	public List<String> getProcedures()
	{
		return procedures;
	}


	public void setProcedures(List<String> procedures)
	{
		this.procedures = procedures;
	}


	public TimeInfo getTime()
	{
		return time;
	}


	public void setTime(TimeInfo time)
	{
		this.time = time;
	}	


	public Bbox getBbox()
	{
		return bbox;
	}


	public void setBbox(Bbox bbox)
	{
		this.bbox = bbox;
	}
}
