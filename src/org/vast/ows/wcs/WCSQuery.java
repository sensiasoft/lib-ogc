/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.util.ArrayList;

import org.vast.ows.OWSQuery;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * WCS Query
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for WCS query parameters
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Tony Cook, Alex Robin
 * @date Oct 27, 2005
 * @version 1.0
 */
public class WCSQuery extends OWSQuery
{
    protected final static String unsupportedVersion = "Unsupported WCS version";
    
    protected String layer;
	protected String format;		
	protected TimeInfo time;
	protected Bbox bbox;
	protected int skipX, skipY, skipZ;
	protected double resX, resY, resZ;
	protected String srs;
	//  TODO: add domain and range stuff
	
	protected ArrayList<TimeInfo> times = new ArrayList<TimeInfo>();
	
    
    public WCSQuery()
    {
        service = "WCS";
        bbox = new Bbox();
        time = new TimeInfo();
    }
    
	
	public Bbox getBbox()
	{
		return bbox;
	}


	public void setBbox(Bbox bbox)
	{
		this.bbox = bbox;
	}


	public String getSrs()
	{
		return srs;
	}


	public void setSrs(String srs)
	{
		this.srs = srs;
	}


	public String getFormat()
	{
		return format;
	}


	public void setFormat(String format)
	{
		this.format = format;
	}


	public String getLayer()
	{
		return layer;
	}


	public void setLayer(String layer)
	{
		this.layer = layer;
	}


	public double getResX()
	{
		return resX;
	}


	public void setResX(double resX)
	{
		this.resX = resX;
	}


	public double getResY()
	{
		return resY;
	}


	public void setResY(double resY)
	{
		this.resY = resY;
	}


	public double getResZ()
	{
		return resZ;
	}


	public void setResZ(double resZ)
	{
		this.resZ = resZ;
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


	public TimeInfo getTime()
	{
		return time;
	}


	public void setTime(TimeInfo time)
	{
		this.time = time;
	}


	public ArrayList<TimeInfo> getTimes()
	{
		return times;
	}


	public void setTimes(ArrayList<TimeInfo> times)
	{
		this.times = times;
	}	
}
