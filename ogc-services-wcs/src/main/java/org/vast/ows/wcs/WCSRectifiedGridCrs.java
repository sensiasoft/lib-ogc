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

/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 28 nov. 07
 * */
public class WCSRectifiedGridCrs
{
	public final static String SIMPLE_GRID = "urn:ogc:def:method:WCS:1.1:2dSimpleGrid";
    public final static String GRID_2D_IN_CRS_2D = "urn:ogc:def:method:WCS:1.1:2dGridin2dCrs";
    public final static String GRID_2D_IN_CRS_3D = "urn:ogc:def:method:WCS:1.1:2dGridin3dCrs";
    public final static String SQUARE_CS_2D = "urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS";
    
    protected String srsName;
    protected String baseCrs;
	protected String gridCs;
	protected String gridType;
	protected int gridDimension;
	protected int[] gridSizes;
	protected double[] gridOrigin;
	protected double[][] gridOffsets;	


	public WCSRectifiedGridCrs()
	{
		gridCs = SQUARE_CS_2D;
        gridType = SIMPLE_GRID;
        gridDimension = 2;
        gridSizes = new int[gridDimension];
        gridOrigin = new double[gridDimension];
        gridOffsets = new double[gridDimension][gridDimension];
        gridOffsets[0][0] = 1.0;
        gridOffsets[1][1] = 1.0;
	}
	
	
	public String getBaseCrs()
	{
		return baseCrs;
	}


	public void setBaseCrs(String baseCrs)
	{
		this.baseCrs = baseCrs;
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


	public int getGridDimension()
	{
		return gridDimension;
	}


	public void setGridDimension(int gridDimension)
	{
		this.gridDimension = gridDimension;
	}


	public double[] getGridOrigin()
	{
		return gridOrigin;
	}


	public void setGridOrigin(double[] gridOrigin)
	{
		this.gridOrigin = gridOrigin;
	}


	public double[][] getGridOffsets()
	{
		return gridOffsets;
	}


	public void setGridOffsets(double[][] gridOffsets)
	{
		this.gridOffsets = gridOffsets;
	}


	public int[] getGridSizes()
	{
		return gridSizes;
	}


	public void setGridSizes(int[] gridSizes)
	{
		this.gridSizes = gridSizes;
	}


	public String getSrsName()
	{
		return srsName;
	}


	public void setSrsName(String srsName)
	{
		this.srsName = srsName;
	}
}
