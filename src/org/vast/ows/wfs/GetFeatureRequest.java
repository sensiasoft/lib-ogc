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

package org.vast.ows.wfs;

import java.util.ArrayList;
import java.util.List;

import org.vast.ows.OWSRequest;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * WFS Query
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for WFS query parameters
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 27, 2005
 * @version 1.0
 */
public class GetFeatureRequest extends OWSRequest
{
    protected TimeInfo time;
	protected Bbox bbox;
	protected String srs;
	protected String format;
	protected List<String> features;
	protected int maxFeatures;

	
	public GetFeatureRequest()
	{
		service = "WFS";
		bbox = new Bbox();
		time = new TimeInfo();
		features = new ArrayList<String>(2);
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


	public List<String> getFeatures()
	{
		return features;
	}


	public void setFeatures(List<String> layers)
	{
		this.features = layers;
	}


	public String getSrs()
	{
		return srs;
	}


	public void setSrs(String srs)
	{
		this.srs = srs;
	}


	public TimeInfo getTime()
	{
		return time;
	}


	public void setTime(TimeInfo time)
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
