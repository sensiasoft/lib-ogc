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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wfs;

import java.util.*;

import org.vast.ows.OWSLayerCapabilities;
import org.vast.util.Bbox;


/**
 * <p>
 * Contains WFS layer capabilities like formats,
 * reference systems (srs), max bbox...
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 30, 2005
 * */
public class WFSLayerCapabilities extends OWSLayerCapabilities
{
    protected List<String> formatList;
    protected List<String> srsList;
    protected List<Bbox> bboxList;
    protected int maxWidth, maxHeight;
    

    public WFSLayerCapabilities()
    {
    	formatList = new ArrayList<String>(3);
    	srsList = new ArrayList<String>(3);
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
	
	
	public List<String> getSrsList()
	{
		return srsList;
	}


	public void setSrsList(List<String> srsList)
	{
		this.srsList = srsList;
	}


	public List<Bbox> getBboxList()
	{
		return bboxList;
	}


	public void setBboxList(List<Bbox> bboxList)
	{
		this.bboxList = bboxList;
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
}
