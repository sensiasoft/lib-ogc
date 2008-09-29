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

package org.vast.ows.sos;

import java.util.*;

import org.vast.ows.OWSLayerCapabilities;
import org.vast.util.Bbox;
import org.vast.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * SOS Layer Capabilities
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Contains SOS layer capabilities like available formats,
 * observables, procedures...
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 27, 2005
 * @version 1.0
 */
public class SOSLayerCapabilities extends OWSLayerCapabilities
{
    protected List<String> formatList;
    protected List<String> observableList;
    protected List<String> procedureList;
    protected List<TimeInfo> timeList;
    protected List<Bbox> bboxList;
    

    public SOSLayerCapabilities()
    {
    	formatList = new ArrayList<String>(2);
    	observableList = new ArrayList<String>(2);
    	procedureList = new ArrayList<String>(2);
    	timeList = new ArrayList<TimeInfo>(1);
    	bboxList = new ArrayList<Bbox>();
    }


	public List<String> getFormatList()
	{
		return formatList;
	}


	public void setFormatList(List<String> formatList)
	{
		this.formatList = formatList;
	}


	public List<String> getObservableList()
	{
		return observableList;
	}


	public void setObservableList(List<String> observableList)
	{
		this.observableList = observableList;
	}


	public List<String> getProcedureList()
	{
		return procedureList;
	}


	public void setProcedureList(List<String> procedureList)
	{
		this.procedureList = procedureList;
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
}
