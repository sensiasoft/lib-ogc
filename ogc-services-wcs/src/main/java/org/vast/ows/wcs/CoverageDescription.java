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
import org.vast.ows.OWSIdentification;
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
public class CoverageDescription extends OWSIdentification
{

	// spatial domain
	protected List<Bbox> bboxList;
	protected WCSRectifiedGridCrs gridCrs;
	protected Object transformation;
	// protected List<Polygon> -> use JTS

	// temporal domain
	protected List<TimeExtent> timeList;

	// range
	protected List<RangeField> rangeFields;
	
	// output
	protected List<String> formatList;
	protected String nativeFormat;
	protected List<String> crsList;
	protected String nativeCrs;

	
	public CoverageDescription()
	{
		bboxList = new ArrayList<Bbox>(1);
		
		timeList = new ArrayList<TimeExtent>(0);
		rangeFields = new ArrayList<RangeField>(1);
		
		formatList = new ArrayList<String>(3);
		crsList = new ArrayList<String>(3);
	}
	
	
	public List<String> getFormatList()
	{
		return formatList;
	}


	public void setFormatList(List<String> formatList)
	{
		this.formatList = formatList;
	}


	public String getNativeFormat()
	{
		return nativeFormat;
	}


	public void setNativeFormat(String nativeFormat)
	{
		this.nativeFormat = nativeFormat;
	}


	public List<String> getCrsList()
	{
		return crsList;
	}


	public void setCrsList(List<String> crsList)
	{
		this.crsList = crsList;
	}
	
	
	public String getNativeCrs()
	{
		return nativeCrs;
	}


	public void setNativeCrs(String nativeCrs)
	{
		this.nativeCrs = nativeCrs;
	}


	public List<Bbox> getBboxList()
	{
		return bboxList;
	}


	public void setBboxList(List<Bbox> bboxList)
	{
		this.bboxList = bboxList;
	}


	public List<TimeExtent> getTimeList()
	{
		return timeList;
	}


	public void setTimeList(List<TimeExtent> timeList)
	{
		this.timeList = timeList;
	}


	public WCSRectifiedGridCrs getGridCrs()
	{
		return gridCrs;
	}


	public void setGridCrs(WCSRectifiedGridCrs gridCrs)
	{
		this.gridCrs = gridCrs;
	}


	public Object getTransformation()
	{
		return transformation;
	}


	public void setTransformation(Object transformation)
	{
		this.transformation = transformation;
	}


	public List<RangeField> getRangeFields()
	{
		return rangeFields;
	}


	public void setRangeFields(List<RangeField> rangeFields)
	{
		this.rangeFields = rangeFields;
	}
}
