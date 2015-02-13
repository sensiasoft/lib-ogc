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

import java.util.ArrayList;
import java.util.List;

import org.vast.ows.OWSRequest;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Container for WFS query parameters
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 27, 2005
 * */
public class GetFeatureRequest extends OWSRequest
{
    protected TimeExtent time;
	protected Bbox bbox;
	protected String srs;
	protected String format;
	protected String typeName;
	protected List<String> featureIds;
	protected int maxFeatures = Integer.MAX_VALUE;

	
	public GetFeatureRequest()
	{
		service = "WFS";
		bbox = new Bbox();
		time = new TimeExtent();
		featureIds = new ArrayList<String>(1);
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


	public String getTypeName()
	{
		return typeName;
	}


	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}


	public List<String> getFeatureIds()
	{
		return featureIds;
	}


	public void setFeatureIds(List<String> fids)
	{
		this.featureIds = fids;
	}


	public String getSrs()
	{
		return srs;
	}


	public void setSrs(String srs)
	{
		this.srs = srs;
	}


	public TimeExtent getTime()
	{
		return time;
	}


	public void setTime(TimeExtent time)
	{
		this.time = time;
	}


	public int getMaxFeatures()
	{
		return maxFeatures;
	}


	public void setMaxFeatures(int maxFeatures)
	{
		this.maxFeatures = maxFeatures;
	}
}
