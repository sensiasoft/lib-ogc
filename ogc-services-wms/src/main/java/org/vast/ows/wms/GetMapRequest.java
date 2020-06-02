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

package org.vast.ows.wms;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import org.vast.ows.OWSRequest;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Container for GetMap request parameters
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 10, 2007
 * */
public class GetMapRequest extends OWSRequest
{
    protected TimeExtent time;
	protected Bbox bbox;
	protected String srs;
	protected String format;	
	protected int width;
	protected int height;
	protected boolean transparent;
	protected Color backgroundColor;

	protected List<String> layers;
	protected List<String> styles;

	
	public GetMapRequest()
	{
		service = "WMS";
        operation = "GetMap";
		layers = new ArrayList<String>(1);
		styles = new ArrayList<String>(1);
		transparent = false;
		backgroundColor = Color.WHITE;
	}
	
	
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}


	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
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


	public TimeExtent getTime()
	{
		return time;
	}


	public void setTime(TimeExtent time)
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
