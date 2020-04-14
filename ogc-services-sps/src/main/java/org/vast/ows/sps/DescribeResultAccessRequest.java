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
    Philippe Merigot <philippe.merigot@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.ows.OWSRequest;


/**
 * <p>
 * Container for SPS DescribeResultAccess request parameters
 * </p>
 *
 * @author Alex Robin
 * @date Mar 03, 2008
 * */
public class DescribeResultAccessRequest extends OWSRequest
{
	protected String procedureID;
	protected String taskID;
	
	
	public DescribeResultAccessRequest()
	{
		setService(SPSUtils.SPS);
		setOperation("DescribeResultAccess");
	}
	
	
	public String getProcedureID()
	{
		return procedureID;
	}


	public void setProcedureID(String sensorID)
	{
		this.procedureID = sensorID;
	}


	public String getTaskID()
	{
		return taskID;
	}


	public void setTaskID(String id)
	{
		taskID = id;
	}
}
