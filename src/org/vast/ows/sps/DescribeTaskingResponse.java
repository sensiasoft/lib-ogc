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

package org.vast.ows.sps;

import org.vast.cdm.common.DataComponent;
import org.vast.ows.OWSResponse;


/**
 * <p><b>Title:</b>
 * DescribeTasking Response
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DescribeTaskingResponse type description
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 25 2008
 * @version 1.0
 */
public class DescribeTaskingResponse extends OWSResponse
{
	protected DataComponent taskingParameters;
	protected DataComponent updatableParameters;
	protected DataComponent feasibilityReportExtendedData;
	protected DataComponent statusReportExtendedData;
	

	public DescribeTaskingResponse()
	{
		this.service = "SPS";
        this.messageType = "DescribeTaskingResponse";
	}


	public DataComponent getTaskingParameters()
	{
		return taskingParameters;
	}


	public void setTaskingParameters(DataComponent taskingParameters)
	{
		this.taskingParameters = taskingParameters;
	}


	public DataComponent getUpdatableParameters()
	{
		return updatableParameters;
	}


	public void setUpdatableParameters(DataComponent updatableParameters)
	{
		this.updatableParameters = updatableParameters;
	}


	public DataComponent getFeasibilityReportExtendedData()
	{
		return feasibilityReportExtendedData;
	}


	public void setFeasibilityReportExtendedData(DataComponent feasibilityReportParameters)
	{
		this.feasibilityReportExtendedData = feasibilityReportParameters;
	}


	public DataComponent getStatusReportExtendedData()
	{
		return statusReportExtendedData;
	}


	public void setStatusReportExtendedData(DataComponent statusReportParameters)
	{
		this.statusReportExtendedData = statusReportParameters;
	}	
}
