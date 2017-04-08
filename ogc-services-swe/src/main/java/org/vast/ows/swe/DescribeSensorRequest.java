/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.sensorML.SMLUtils;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Container for SOS Describe Sensor request parameters
 * </p>
 *
 * @author Alex Robin
 * @date Oct 09, 2007
 * */
public class DescribeSensorRequest extends OWSRequest
{
    public final static String DEFAULT_FORMAT = OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "2.0");
    	
	protected String procedureID;
	protected TimeExtent time;
    protected String format;

	
	public DescribeSensorRequest()
	{
		service = OWSUtils.SOS;
		operation = "DescribeSensor";
		format = DEFAULT_FORMAT;
	}
	
	
	public String getFormat()
	{
		return format;
	}


	public void setFormat(String format)
	{
		this.format = format;
	}


	public String getProcedureID()
	{
		return procedureID;
	}


	public void setProcedureID(String procedure)
	{
		this.procedureID = procedure;
	}
	
	
	public TimeExtent getTime()
	{
		return time;
	}


	public void setTime(TimeExtent time)
	{
		this.time = time;
	}	
}
