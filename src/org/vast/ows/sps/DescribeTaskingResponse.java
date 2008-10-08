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
	protected DataComponent commonTaskingParameters;
	protected DataComponent auxiliaryFeasibilityParameters;
	protected DataComponent auxiliarySubmitParameters;
	protected DataComponent feasibilityStudyExtendedData;
	protected DataComponent progressReportExtendedData;
	

	public DescribeTaskingResponse()
	{
		this.service = "SPS";
        this.messageType = "DescribeTaskingResponse";
	}


	public DataComponent getCommonTaskingParameters()
	{
		return commonTaskingParameters;
	}


	public void setCommonTaskingParameters(DataComponent commonTaskingParameters)
	{
		this.commonTaskingParameters = commonTaskingParameters;
	}


	public DataComponent getAuxiliaryFeasibilityParameters()
	{
		return auxiliaryFeasibilityParameters;
	}


	public void setAuxiliaryFeasibilityParameters(DataComponent auxiliaryFeasibilityParameters)
	{
		this.auxiliaryFeasibilityParameters = auxiliaryFeasibilityParameters;
	}


	public DataComponent getAuxiliarySubmitParameters()
	{
		return auxiliarySubmitParameters;
	}


	public void setAuxiliarySubmitParameters(DataComponent auxiliarySubmitParameters)
	{
		this.auxiliarySubmitParameters = auxiliarySubmitParameters;
	}


	public DataComponent getFeasibilityStudyExtendedData()
	{
		return feasibilityStudyExtendedData;
	}


	public void setFeasibilityStudyExtendedData(DataComponent feasibilityStudyParameters)
	{
		this.feasibilityStudyExtendedData = feasibilityStudyParameters;
	}


	public DataComponent getProgressReportExtendedData()
	{
		return progressReportExtendedData;
	}


	public void setProgressReportExtendedData(DataComponent progressReportParameters)
	{
		this.progressReportExtendedData = progressReportParameters;
	}	
}
