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

import java.util.*;

import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * WMS Layer Capabilities
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Contains WMS layer capabilities like formats, styles,
 * reference systems (srs), max bbox, available time ranges...
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 30, 2005
 * @version 1.0
 */
public class WMSLayerCapabilities extends OWSLayerCapabilities
{
    protected List<String> formatList;
    protected List<String> styleList;
    protected List<String> srsList;
    protected List<TimeInfo> timeList;
    protected List<Bbox> bboxList;
    protected List<WMSLayerCapabilities> childLayers;
    protected int maxWidth;
    protected int maxHeight;
    protected boolean opaque;
    protected boolean resizable;
    protected boolean subsetable;
    protected boolean cascaded;
    

    public WMSLayerCapabilities()
    {
    	formatList = new ArrayList<String>(3);
    	styleList = new ArrayList<String>(3);
    	srsList = new ArrayList<String>(3);
    	timeList = new ArrayList<TimeInfo>(1);
    	bboxList = new ArrayList<Bbox>(1);
    	maxWidth = -1;
    	maxHeight = -1;
    }


	public List<String> getFormatList()
	{
		return formatList;
	}


	public void setFormatList(List<String> formatList)
	{
		this.formatList = formatList;
	}
	
	
	public List<String> getStyleList()
	{
		return styleList;
	}


	public void setStyleList(List<String> styleList)
	{
		this.styleList = styleList;
	}
	
	
	public List<String> getSrsList()
	{
		return srsList;
	}


	public void setSrsList(List<String> srsList)
	{
		this.srsList = srsList;
	}


	public List<TimeInfo> getTimeList()
	{
		return timeList;
	}


	public void setTimeList(List<TimeInfo> timeList)
	{
		this.timeList = timeList;
	}


	public List<Bbox> getBboxList()
	{
		return bboxList;
	}


	public void setBboxList(List<Bbox> bboxList)
	{
		this.bboxList = bboxList;
	}
	
	
	public List<WMSLayerCapabilities> getChildLayers()
	{
		return childLayers;
	}


	public void setChildLayers(List<WMSLayerCapabilities> childLayers)
	{
		this.childLayers = childLayers;
	}


	public int getMaxHeight()
	{
		return maxHeight;
	}


	public void setMaxHeight(int maxHeight)
	{
		this.maxHeight = maxHeight;
	}


	public int getMaxWidth()
	{
		return maxWidth;
	}


	public void setMaxWidth(int maxWidth)
	{
		this.maxWidth = maxWidth;
	}


	public boolean isCascaded()
	{
		return cascaded;
	}


	public void setCascaded(boolean cascaded)
	{
		this.cascaded = cascaded;
	}


	public boolean isResizable()
	{
		return resizable;
	}


	public void setResizable(boolean noResize)
	{
		this.resizable = noResize;
	}


	public boolean isSubsetable()
	{
		return subsetable;
	}


	public void setSubsetable(boolean noSubsets)
	{
		this.subsetable = noSubsets;
	}


	public boolean isOpaque()
	{
		return opaque;
	}


	public void setOpaque(boolean opaque)
	{
		this.opaque = opaque;
	}
}
