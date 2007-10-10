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
import java.util.Hashtable;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.util.AxisSubset;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * WCS GetCoverage Request
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for GetCoverage request parameters
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alex Robin
 * @date Sep 21, 2007
 * @version 1.0
 */
public class GetCoverageRequest extends OWSRequest
{
    public final static String SIMPLE_GRID = "urn:ogc:def:method:WCS:1.1:2dSimpleGrid";
    public final static String GRID_2D_IN_CRS_2D = "urn:ogc:def:method:WCS:1.1:2dGridin2dCrs";
    public final static String GRID_2D_IN_CRS_3D = "urn:ogc:def:method:WCS:1.1:2dGridin3dCrs";
    
	protected String coverage;
	protected String format;
	protected String interpolationMethod;
	protected Bbox bbox;
	protected int width, height, depth;
	protected int skipX, skipY, skipZ;
	protected String gridCrs;
	protected String gridCs;
	protected String gridType;
	protected int gridDimension;
	protected double[] gridOrigin;
	protected double[] gridOffsets;
	protected boolean store;
	protected boolean useResolution;
	protected ArrayList<AxisSubset> axisSubsets;
	protected ArrayList<TimeInfo> times;
		
	
	public GetCoverageRequest()
    {
        service = "WCS";
        operation = "GetCoverage";
        
        axisSubsets = new ArrayList<AxisSubset>();
        times = new ArrayList<TimeInfo>();
        vendorParameters = new Hashtable<String, String>();
        
        gridCs = "urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS";
        gridType = SIMPLE_GRID;
        gridDimension = 2;
        gridOrigin = new double[gridDimension];
        gridOffsets = new double[gridDimension*gridDimension];
        width = height = depth = -1;
        useResolution = false;
    }
	
	
	public void checkParameters() throws OWSException
	{
		ArrayList<String> missingParams = new ArrayList<String>();
		
		// need coverage
		if (this.getCoverage() == null)
			missingParams.add("Coverage Identifier");
		
		// need at least BBOX or TIME
		if (this.getBbox() == null && this.getTime() == null)
			missingParams.add("Bounding Box or Time");
		
		// need at least WIDTH or RESX
		if (this.getWidth() < 0 && this.getResX() < 0)
			missingParams.add("Grid Size or Resolution");
		
		// need format
		if (this.getFormat() == null)
			missingParams.add("Format");
		
		// copy crs to responseCrs if needed
		if (gridCrs == null)
			gridCrs = bbox.getCrs();
		
		// check common params + generate exception
		super.checkParameters(missingParams);
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
		return bbox;
	}


	public void setBbox(Bbox bbox)
	{
		this.bbox = bbox;
	}
	
	
	public TimeInfo getTime()
	{
		if (times.isEmpty())
			return null;
		return times.get(0);
	}
	
	
	public void setTime(TimeInfo time)
	{
		this.times.add(time);
	}


	public ArrayList<TimeInfo> getTimes()
	{
		return times;
	}


	public ArrayList<AxisSubset> getAxisSubsets()
	{
		return axisSubsets;
	}


	public int getGridDimension()
	{
		return gridDimension;
	}


	public void setGridDimension(int gridDimension)
	{
		if (this.gridDimension != gridDimension)
		{
			this.gridDimension = gridDimension;
			gridOrigin = new double[gridDimension];
	        gridOffsets = new double[gridDimension*gridDimension];
		}
	}


	public String getGridCrs()
	{
		return gridCrs;
	}


	public void setGridCrs(String gridCrs)
	{
		this.gridCrs = gridCrs;
	}


	public String getGridCs()
	{
		return gridCs;
	}


	public void setGridCs(String gridCs)
	{
		this.gridCs = gridCs;
	}


	public String getGridType()
	{
		return gridType;
	}


	public void setGridType(String gridType)
	{
		this.gridType = gridType;
	}


	public double[] getGridOrigin()
	{
		return gridOrigin;
	}


	public void setGridOrigin(double[] gridOrigin)
	{
		this.gridOrigin = gridOrigin;
	}


	public double[] getGridOffsets()
	{
		if (useResolution)
			return gridOffsets;
		else
			return null;
	}


	public void setGridOffsets(double[] gridOffsets)
	{
		this.gridOffsets = gridOffsets;
		useResolution = true;
	}


	public double getResX()
	{
		if (useResolution && gridOffsets != null && gridDimension > 0)
			return gridOffsets[0];
		else
			return Double.NaN;
	}


	public void setResX(double resX)
	{
		if (gridOffsets != null && gridDimension > 0)
		{
			this.gridOffsets[0] = resX;
			this.useResolution = true;
		}
	}


	public double getResY()
	{
		if (useResolution && gridOffsets != null && gridDimension > 1)
			return gridOffsets[1];
		else
			return Double.NaN;
	}


	public void setResY(double resY)
	{
		if (gridOffsets != null && gridDimension > 1)
		{
			this.gridOffsets[1] = resY;
			this.useResolution = true;
		}		
	}


	public double getResZ()
	{
		if (useResolution && gridOffsets != null && gridDimension > 2)
			return gridOffsets[2];
		else
			return Double.NaN;
	}


	public void setResZ(double resZ)
	{
		if (gridOffsets != null && gridDimension > 2)
		{
			this.gridOffsets[2] = resZ;
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
	
	
	public String getInterpolationMethod()
	{
		return interpolationMethod;
	}


	public void setInterpolationMethod(String interpolationMethod)
	{
		this.interpolationMethod = interpolationMethod;
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
