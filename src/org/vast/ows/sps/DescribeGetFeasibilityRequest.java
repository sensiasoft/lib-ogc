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
    Philippe Merigot <philippe.merigot@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.ows.OWSRequest;


/**
 * <p><b>Title:</b><br/>
 * DescribeSensor Request
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for SPS DescribeGetFeasibility request parameters
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Philippe Merigot
 * @date Oct 18, 2007
 * @version 1.0
 */
public class DescribeGetFeasibilityRequest extends OWSRequest
{
	protected String sensorID;
	
	
	public DescribeGetFeasibilityRequest()
	{
		setService("SPS");
		setOperation("DescribeGetFeasibility");
	}
	
	
	public String getSensorID()
	{
		return sensorID;
	}


	public void setSensorID(String sensorID)
	{
		this.sensorID = sensorID;
	}
}
