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
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wms;

import java.util.List;
import java.util.ArrayList;

import org.vast.ows.OWSQuery;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * WMS Query
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for WMS query parameters
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 27, 2005
 * @version 1.0
 */
public class WMSQuery extends OWSQuery
{
    protected final static String unsupportedVersion = "Unsupported WMS version";
    
    protected TimeInfo time;
	protected Bbox bbox;
	protected String srs;
	protected String format;	
	protected int width;
	protected int height;
	protected boolean transparent;

	protected List<String> layers;
	protected List<String> styles;

	
	public WMSQuery()
	{
		service = "WMS";
		bbox = new Bbox();
		time = new TimeInfo();
		layers = new ArrayList<String>(2);
		styles = new ArrayList<String>(2);		
	}
	

	public Bbox getBbox()
	{
		return bbox;
	}


	public void setBbox(Bbox bbox)
	{
		this.bbox = bbox;
	}


	public String getFormat()
	{
		return format;
	}


	public void setFormat(String format)
	{
		this.format = format;
	}


	public int getHeight()
	{
		return height;
	}


	public void setHeight(int height)
	{
		this.height = height;
	}


	public List<String> getLayers()
	{
		return layers;
	}


	public void setLayers(List<String> layers)
	{
		this.layers = layers;
	}


	public String getSrs()
	{
		return srs;
	}


	public void setSrs(String srs)
	{
		this.srs = srs;
	}


	public List<String> getStyles()
	{
		return styles;
	}


	public void setStyles(List<String> styles)
	{
		this.styles = styles;
	}


	public TimeInfo getTime()
	{
		return time;
	}


	public void setTime(TimeInfo time)
	{
		this.time = time;
	}


	public boolean isTransparent()
	{
		return transparent;
	}


	public void setTransparent(boolean transparent)
	{
		this.transparent = transparent;
	}


	public int getWidth()
	{
		return width;
	}


	public void setWidth(int width)
	{
		this.width = width;
	}
}
