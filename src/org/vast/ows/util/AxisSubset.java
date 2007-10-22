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

package org.vast.ows.util;

import java.util.ArrayList;


/**
 * <p><b>Title:</b><br/>
 * AxisSubset
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Specifies subsetting parameters for any axis other than spatial and temporal.
 * For space and time use Bbox and TimeInfo
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alex Robin
 * @date Sep 24, 2007
 * @version 1.0
 */
public class AxisSubset
{
	protected String name;
	protected ArrayList<Interval> rangeIntervals;
	protected ArrayList<String> rangeValues;


	public AxisSubset()
	{
		rangeIntervals = new ArrayList<Interval>();
		rangeValues = new ArrayList<String>();
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public ArrayList<Interval> getRangeIntervals()
	{
		return rangeIntervals;
	}


	public ArrayList<String> getRangeValues()
	{
		return rangeValues;
	}
}
