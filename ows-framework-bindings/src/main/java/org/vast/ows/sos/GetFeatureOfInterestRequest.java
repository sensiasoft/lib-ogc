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

import java.util.*;
import net.opengis.fes.v20.BinarySpatialOp;
import org.vast.ows.OWSRequest;
import org.vast.ows.fes.FESRequestUtils;
import org.vast.util.Bbox;


/**
 * <p><b>Title:</b><br/>
 * GetFeatureOfInterest Request
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for SOS GetFeatureOfInterest request parameters
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 02, 2014
 * @version 1.0
 */
public class GetFeatureOfInterestRequest extends OWSRequest
{
    protected List<String> procedures;
	protected List<String> observables;
	protected List<String> foiIDs;
	protected BinarySpatialOp spatialFilter;
	
	
	public GetFeatureOfInterestRequest()
	{
		service = "SOS";
		operation = "GetObservation";
		procedures = new ArrayList<String>(2);
		observables = new ArrayList<String>(2);
		foiIDs = new ArrayList<String>(2);
	}


    public List<String> getObservables()
	{
		return observables;
	}


	public List<String> getProcedures()
	{
		return procedures;
	}


	public List<String> getFoiIDs()
    {
        return foiIDs;
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
}
