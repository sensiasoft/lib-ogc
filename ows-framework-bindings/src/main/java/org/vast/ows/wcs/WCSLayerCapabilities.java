/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
    Tony Cook <tcook@nsstc.uah.edu>
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.util.*;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 27 nov. 07
 * */
public class WCSLayerCapabilities extends OWSLayerCapabilities
{
	protected List<String> formatList;
	protected List<String> crsList;
	protected List<Bbox> bboxList;
	protected List<TimeExtent> timeList;
	protected List<WCSLayerCapabilities> childCoverages;
	protected CoverageDescription fullDescription; 
	

	public WCSLayerCapabilities()
	{
		formatList = new ArrayList<String>();
		crsList = new ArrayList<String>();
		bboxList = new ArrayList<Bbox>();
		childCoverages = new ArrayList<WCSLayerCapabilities>(0);
	}


	public List<String> getCrsList()
	{
		return crsList;
	}


	public List<String> getFormatList()
	{
		return formatList;
	}


	public List<Bbox> getBboxList()
	{
		return bboxList;
	}
	
	
	public List<TimeExtent> getTimeList()
	{
		return timeList;
	}


	public List<WCSLayerCapabilities> getChildCoverages()
	{
		return childCoverages;
	}


	public void setFormatList(List<String> formatList)
	{
		this.formatList = formatList;
	}


	public void setCrsList(List<String> crsList)
	{
		this.crsList = crsList;
	}


	public void setBboxList(List<Bbox> bboxList)
	{
		this.bboxList = bboxList;
	}


	public void setTimeList(List<TimeExtent> timeList)
	{
		this.timeList = timeList;
	}


	public CoverageDescription getFullDescription()
	{
		return fullDescription;
	}


	public void setFullDescription(CoverageDescription fullDescription)
	{
		this.fullDescription = fullDescription;
	}

}
