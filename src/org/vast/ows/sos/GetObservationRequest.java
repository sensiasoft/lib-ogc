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
import org.vast.util.Bbox;
import org.vast.util.TimeInfo;
import org.vast.xml.QName;


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
        
	protected List<String> procedures;
	protected List<String> offerings;
	protected List<String> observables;
	protected List<String> fois;
	
	protected TimeInfo time;
	protected Bbox bbox;
	protected QName temporalProperty;
	protected QName spatialProperty;
		
    protected String format;
    protected String resultModel;
	protected ResponseMode responseMode;
	
	
	public GetObservationRequest()
	{
		service = "SOS";
		operation = "GetObservation";
		procedures = new ArrayList<String>(2);
		offerings = new ArrayList<String>(2);
		observables = new ArrayList<String>(2);
		fois = new ArrayList<String>(2);
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
		if (offerings.size() == 0)
		    return null;
		else
		    return offerings.get(0);
	}


	public void setOffering(String offering)
	{
		this.offerings.add(offering);
	}
	
	
	public List<String> getOfferings()
    {
        return offerings;
    }


    public List<String> getObservables()
	{
		return observables;
	}


	public List<String> getProcedures()
	{
		return procedures;
	}


	public List<String> getFois()
    {
        return fois;
    }


    public TimeInfo getTime()
	{
		return time;
	}


	public void setTime(TimeInfo time)
	{
		this.time = time;
	}	


	public QName getTemporalProperty()
    {
        return temporalProperty;
    }


    public void setTemporalProperty(QName temporalProperty)
    {
        this.temporalProperty = temporalProperty;
    }


    public Bbox getBbox()
	{
		return bbox;
	}


	public void setBbox(Bbox bbox)
	{
		this.bbox = bbox;
	}


    public QName getSpatialProperty()
    {
        return spatialProperty;
    }


    public void setSpatialProperty(QName spatialProperty)
    {
        this.spatialProperty = spatialProperty;
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
