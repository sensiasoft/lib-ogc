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
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.BinaryTemporalOp;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.om.OMUtils;
import org.vast.ows.OWSRequest;
import org.vast.ows.fes.FESRequestUtils;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Container for SOS GetObservation request parameters
 * </p>
 *
 * @author Alex Robin
 * @date Oct 09, 2007
 * */
public class GetObservationRequest extends OWSRequest
{
    public enum ResponseMode
    {
        INLINE, ATTACHED, OUT_OF_BAND, RESULT_TEMPLATE, RESULT_ONLY
    }
    
    public static final String DEFAULT_FORMAT = OGCRegistry.getNamespaceURI(OMUtils.OM, "2.0");
    
    protected Set<String> procedures;
	protected Set<String> offerings;
	protected Set<String> observables;
	protected Set<String> foiIDs;
	protected BinaryTemporalOp temporalFilter;
	protected BinarySpatialOp spatialFilter;
    protected String format;
    protected String resultModel;
	protected ResponseMode responseMode;
	
	
	public GetObservationRequest()
	{
		service = "SOS";
		operation = "GetObservation";
		procedures = new LinkedHashSet<>();
		offerings = new LinkedHashSet<>();
		observables = new LinkedHashSet<>();
		foiIDs = new LinkedHashSet<>();
	}
	
	
	public String getOffering()
	{
		if (offerings.isEmpty())
		    return null;
		else
		    return offerings.iterator().next();
	}


	public void setOffering(String offering)
	{
		this.offerings.add(offering);
	}
	
	
	public Set<String> getOfferings()
    {
        return offerings;
    }


    public Set<String> getObservables()
	{
		return observables;
	}


	public Set<String> getProcedures()
	{
		return procedures;
	}


	public Set<String> getFoiIDs()
    {
        return foiIDs;
    }


	public BinaryTemporalOp getTemporalFilter()
    {
        return temporalFilter;
    }


    public void setTemporalFilter(BinaryTemporalOp temporalFilter)
    {
        this.temporalFilter = temporalFilter;
    }


    public TimeExtent getTime()
	{
        return FESRequestUtils.filterToTimeExtent(temporalFilter);
	}


	public void setTime(TimeExtent time)
	{
	    this.temporalFilter = FESRequestUtils.timeExtentToFilter(time);
	}	


    public BinarySpatialOp getSpatialFilter()
    {
        return spatialFilter;
    }


    public void setSpatialFilter(BinarySpatialOp spatialFilter)
    {
        this.spatialFilter = spatialFilter;
    }


    public Bbox getBbox()
	{
        return FESRequestUtils.filterToBbox(spatialFilter);
	}


	public void setBbox(Bbox bbox)
	{
	    this.spatialFilter = FESRequestUtils.bboxToFilter(bbox);
	}
	
	
	public String getFormat()
    {
        return format;
    }


    public void setFormat(String format)
    {
        this.format = format;
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
