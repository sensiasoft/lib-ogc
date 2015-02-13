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
 Alex Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.util.Interval;


/**
 * <p>
 * DimensionSubset
 * </p>
 *
 * <p>Description:
 * Dimension subset structure for storing WCS domain subset information
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Aug 16, 2011
 * */
public class DimensionSubset extends Interval
{
	public final static String X = "X";
	public final static String Y = "Y";
	public final static String Z = "Z";
	public final static String T = "T";
    
	protected String crs;
	protected String axis;
	

	public DimensionSubset()
	{		
	}
	
	
	public DimensionSubset(double min, double max)
	{
		super(min, max);
	}
	
	
	public DimensionSubset(String axis, double min, double max)
	{
		this(min, max);
		this.axis = axis;		
	}
	
	
	public DimensionSubset(String crs, String axis, double min, double max)
	{
		this(min, max);
		this.crs = crs;
		this.axis = axis;		
	}
	
	
	public DimensionSubset(double min, double max, double res)
	{
		super(min, max, res);
	}
	
	
	public DimensionSubset(String axis, double min, double max, double res)
	{
		super(min, max, res);
		this.axis = axis;		
	}
	
	
	public DimensionSubset(String crs, String axis, double min, double max, double res)
	{
		this(min, max, res);
		this.crs = crs;
		this.axis = axis;		
	}
	
	
	public String getAxis()
	{
		return axis;
	}


	public void setAxis(String axis)
	{
		this.axis = axis;
	}


	public String getCrs()
	{
		return crs;
	}


	public void setCrs(String crs)
	{
		this.crs = crs;
	}


	public DimensionSubset copy()
	{
		DimensionSubset subset = new DimensionSubset();
		subset.min = this.min;
		subset.max = this.max;
		subset.resolution = this.resolution;
		subset.axis = this.axis;
		subset.crs = this.crs;
		return subset;
	}
}
