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

package org.vast.ows.sos;

import java.util.*;
import org.vast.ows.OWSRequest;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * GetObservation Request
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for SOS GetObservation request parameters
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 09, 2007
 * @version 1.0
 */
public class GetObservationRequest extends OWSRequest
{
    
	public enum ResponseMode
    {
        INLINE, ATTACHED, OUT_OF_BAND, RESULT_TEMPLATE, RESULT_ONLY
    }
    
    protected String offering;
	protected TimeInfo time;
	protected Bbox bbox;
	protected String foiId;
    protected String format;
    protected String resultModel;
    protected ResponseMode responseMode;
	protected List<String> observables;
	protected List<String> procedures;

	
	public GetObservationRequest()
	{
		service = "SOS";
		operation = "GetObservation";
		observables = new ArrayList<String>(2);
		procedures = new ArrayList<String>(2);
		bbox = new Bbox();
		time = new TimeInfo();
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


    public ResponseMode getResponseMode()
    {
        return responseMode;
    }


    public void setResponseMode(ResponseMode responseMode)
    {
        this.responseMode = responseMode;
    }


    public String getResultModel()
    {
        return resultModel;
    }


    public void setResultModel(String resultModel)
    {
        this.resultModel = resultModel;
    }
}
