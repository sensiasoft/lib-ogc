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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.util.*;

import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


public class WCSLayerCapabilities extends OWSLayerCapabilities
{
	protected ArrayList<String> formatList;
	protected ArrayList<String> crsList;
	protected ArrayList<TimeInfo> timeList;
	protected Bbox bbox;  //  Don't think this needs to be an ArrayList for WMS


	public WCSLayerCapabilities()
	{
	}


	public ArrayList<String> getCrsList() {
		return crsList;
	}


	public void setCrsList(ArrayList<String> crsList) {
		this.crsList = crsList;
	}


	public ArrayList<String> getFormatList() {
		return formatList;
	}


	public void setFormatList(ArrayList<String> formatList) {
		this.formatList = formatList;
	}


	public ArrayList<TimeInfo> getTimeList() {
		return timeList;
	}


	public void setTimeList(ArrayList<TimeInfo> timeList) {
		this.timeList = timeList;
	}


	public Bbox getBbox() {
		return bbox;
	}


	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}
}
