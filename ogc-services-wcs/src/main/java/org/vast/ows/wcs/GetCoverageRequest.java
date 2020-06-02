/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.util.ArrayList;
import java.util.List;
import org.vast.ows.OWSRequest;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Container for GetCoverage request parameters
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Sep 21, 2007
 * */
public class GetCoverageRequest extends OWSRequest
{
    protected String coverage;
	protected String format;
	protected List<DimensionSubset> dimensionSubsets;
	protected List<FieldSubset> fieldSubsets;
	
	// old stuffs for WCS 1.0 & 1.1
	protected int width, height, depth;
	protected int skipX, skipY, skipZ;
	protected WCSRectifiedGridCrs gridCrs;
	protected boolean store;
	protected boolean useResolution;
	
		
	
	public GetCoverageRequest()
    {
        service = "WCS";
        operation = "GetCoverage";
        
        dimensionSubsets = new ArrayList<DimensionSubset>();
        fieldSubsets = new ArrayList<FieldSubset>();

        gridCrs = new WCSRectifiedGridCrs();
        
        width = height = depth = -1;
        useResolution = false;
    }
	
	
	public String getCoverage()
	{
		return coverage;
	}


	public void setCoverage(String coverage)
	{
		this.coverage = coverage;
	}
    
	
	public Bbox getBbox()
	{
		Bbox bbox = new Bbox();
		
		for (DimensionSubset range: dimensionSubsets)
		{
			String axisName = range.getAxis();
			
			if (axisName.equals(DimensionSubset.X))
			{
				bbox.setMinX(range.getMin());
				bbox.setMaxX(range.getMax());
				bbox.setCrs(range.getCrs());
			}
			
			else if (axisName.equals(DimensionSubset.Y))
			{
				bbox.setMinY(range.getMin());
				bbox.setMaxY(range.getMax());
			}
			
			else if (axisName.equals(DimensionSubset.Z))
			{
				bbox.setMinZ(range.getMin());
				bbox.setMaxZ(range.getMax());
			}
		}
		
		if (bbox.isNull())
			return null;
		
		return bbox;
	}


	public void setBbox(Bbox bbox)
	{
		String crs = bbox.getCrs();
		
		if (!Double.isNaN(bbox.getMinX()))
			dimensionSubsets.add(new DimensionSubset(crs, DimensionSubset.X, bbox.getMinX(), bbox.getMaxX()));
		
		if (!Double.isNaN(bbox.getMinY()))
			dimensionSubsets.add(new DimensionSubset(crs, DimensionSubset.Y, bbox.getMinY(), bbox.getMaxY()));
		
		if (!Double.isNaN(bbox.getMinZ()))
			dimensionSubsets.add(new DimensionSubset(crs, DimensionSubset.Z, bbox.getMinZ(), bbox.getMaxZ()));
	}
	
	
	public TimeExtent getTime()
	{
		for (DimensionSubset range: dimensionSubsets)
		{
			String axisName = range.getAxis();
			
			if (axisName.equals(DimensionSubset.T))
			{
				TimeExtent timeInfo = new TimeExtent();
				timeInfo.setStartTime(range.getMin());
				timeInfo.setStopTime(range.getMax());
				return timeInfo;
			}
		}
		
		return null;
	}
	
	
	public void setTime(TimeExtent time)
	{
		if (!time.isNull())
			dimensionSubsets.add(new DimensionSubset(DimensionSubset.T, time.getStartTime(), time.getStopTime()));
	}


	public List<TimeExtent> getTimes()
	{
		ArrayList<TimeExtent> times = new ArrayList<TimeExtent>();
		
		for (DimensionSubset range: dimensionSubsets)
		{
			String axisName = range.getAxis();
			
			if (axisName.equals(DimensionSubset.T))
			{
				TimeExtent timeInfo = new TimeExtent();
				timeInfo.setStartTime(range.getMin());
				timeInfo.setStopTime(range.getMax());
				times.add(timeInfo);
			}
		}
		
		return times;
	}


	public List<DimensionSubset> getDimensionSubsets()
	{
		return dimensionSubsets;
	}
	
	
	public List<FieldSubset> getFieldSubsets()
	{
		return fieldSubsets;
	}


	public void setGridCrs(WCSRectifiedGridCrs gridCrs)
	{
		this.gridCrs = gridCrs;
	}

	
	public WCSRectifiedGridCrs getGridCrs()
	{
		return gridCrs;
	}
	
	
	public double getResX()
	{
		if (useResolution && gridCrs.gridOffsets != null && gridCrs.gridDimension > 0)
			return gridCrs.gridOffsets[0][0];
		else
			return Double.NaN;
	}


	public void setResX(double resX)
	{
		if (gridCrs.gridOffsets != null && gridCrs.gridDimension > 0)
		{
			gridCrs.gridOffsets[0][0] = resX;
			this.useResolution = true;
		}
	}


	public double getResY()
	{
		if (useResolution && gridCrs.gridOffsets != null && gridCrs.gridDimension > 1)
			return gridCrs.gridOffsets[1][1];
		else
			return Double.NaN;
	}


	public void setResY(double resY)
	{
		if (gridCrs.gridOffsets != null && gridCrs.gridDimension > 1)
		{
			gridCrs.gridOffsets[1][1] = resY;
			this.useResolution = true;
		}		
	}


	public double getResZ()
	{
		if (useResolution && gridCrs.gridOffsets != null && gridCrs.gridDimension > 2)
			return gridCrs.gridOffsets[2][2];
		else
			return Double.NaN;
	}


	public void setResZ(double resZ)
	{
		if (gridCrs.gridOffsets != null && gridCrs.gridDimension > 2)
		{
			gridCrs.gridOffsets[2][2] = resZ;
			this.useResolution = true;
		}
	}


	public int getSkipX()
	{
		return skipX;
	}


	public void setSkipX(int skipX)
	{
		this.skipX = skipX;
	}


	public int getSkipY()
	{
		return skipY;
	}


	public void setSkipY(int skipY)
	{
		this.skipY = skipY;
	}


	public int getSkipZ()
	{
		return skipZ;
	}


	public void setSkipZ(int skipZ)
	{
		this.skipZ = skipZ;
	}


	public int getWidth()
	{
		if (!useResolution)
			return width;
		else 
			return -1;
	}


	public void setWidth(int width)
	{
		this.width = width;
		this.useResolution = false;
	}


	public int getHeight()
	{
		if (!useResolution)
			return height;
		else 
			return -1;
	}


	public void setHeight(int height)
	{
		this.height = height;
		this.useResolution = false;
	}


	public int getDepth()
	{
		if (!useResolution)
			return depth;
		else 
			return -1;
	}


	public void setDepth(int depth)
	{
		this.depth = depth;
		this.useResolution = false;
	}
	
	
	public String getFormat()
	{
		return format;
	}


	public void setFormat(String format)
	{
		this.format = format;
	}


	public boolean getStore()
	{
		return store;
	}


	public void setStore(boolean store)
	{
		this.store = store;
	}


	public boolean isUseResolution()
	{
		return useResolution;
	}


	public void setUseResolution(boolean useResolution)
	{
		this.useResolution = useResolution;
	}
}
