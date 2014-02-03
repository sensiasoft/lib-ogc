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
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.ArrayList;
import java.util.List;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.vast.ows.OWSRequest;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


/**
 * <p><b>Title:</b><br/>
 * GetResult Request
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for SOS GetResult request parameters
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 09, 2007
 * @version 1.0
 */
public class GetResultRequest extends OWSRequest
{
    protected String offering;
    protected List<String> procedures;
    protected List<String> observables;
    protected List<String> foiIDs;
    protected BinaryTemporalOperator temporalFilter;
    protected BinarySpatialOperator spatialFilter;
    protected String format;
    protected boolean xmlWrapper;
    
	
	public GetResultRequest()
	{
		service = "SOS";
		operation = "GetResult";
		procedures = new ArrayList<String>(2);
        observables = new ArrayList<String>(2);
        foiIDs = new ArrayList<String>(2);
	}


	public String getOffering()
    {
        return offering;
    }


    public void setOffering(String offering)
    {
        this.offering = offering;
    }


    public List<String> getProcedures()
    {
        return procedures;
    }


    public void setProcedures(List<String> procedures)
    {
        this.procedures = procedures;
    }


    public List<String> getObservables()
    {
        return observables;
    }


    public void setObservables(List<String> observables)
    {
        this.observables = observables;
    }


    public List<String> getFoiIDs()
    {
        return foiIDs;
    }


    public void setFoiIDs(List<String> foiIDs)
    {
        this.foiIDs = foiIDs;
    }


    public BinaryTemporalOperator getTemporalFilter()
    {
        return temporalFilter;
    }


    public void setTemporalFilter(BinaryTemporalOperator temporalFilter)
    {
        this.temporalFilter = temporalFilter;
    }


    public TimeExtent getTime()
    {
        return FESUtils.filterToTimeInfo(temporalFilter);
    }


    public void setTime(TimeExtent time)
    {
        this.temporalFilter = FESUtils.timeInfoToFilter(time);
    }   


    public BinarySpatialOperator getSpatialFilter()
    {
        return spatialFilter;
    }


    public void setSpatialFilter(BinarySpatialOperator spatialFilter)
    {
        this.spatialFilter = spatialFilter;
    }


    public Bbox getBbox()
    {
        return FESUtils.filterToBbox(spatialFilter);
    }


    public void setBbox(Bbox bbox)
    {
        this.spatialFilter = FESUtils.bboxToFilter(bbox);
    }


    public String getFormat()
    {
        return format;
    }


    public void setFormat(String format)
    {
        this.format = format;
    }


    public boolean isXmlWrapper()
    {
        return xmlWrapper;
    }


    public void setXmlWrapper(boolean xmlWrapper)
    {
        this.xmlWrapper = xmlWrapper;
    }
}
