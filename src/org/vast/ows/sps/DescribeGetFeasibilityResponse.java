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
import org.vast.cdm.common.DataEncoding;
import org.vast.ows.OWSResponse;


/**
 * <p><b>Title:</b>
 * DescribeFeasibilityResponse
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DescribeFeasibilityResponse type description
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date 27 déc. 07
 * @version 1.0
 */
public class DescribeGetFeasibilityResponse extends OWSResponse
{
	protected DataComponent inputDataComponents;
	protected DataEncoding inputDataEncoding;
	protected DataComponent outputDataComponents;
	protected DataEncoding outputDataEncoding;


	public DescribeGetFeasibilityResponse()
	{
		this.service = "SPS";
        this.messageType = "DescribeGetFeasibilityResponse";
	}
	
	
	public DataComponent getInputDataComponents()
	{
		return inputDataComponents;
	}


	public void setInputDataComponents(DataComponent inputDataComponents)
	{
		this.inputDataComponents = inputDataComponents;
	}


	public DataEncoding getInputDataEncoding()
	{
		return inputDataEncoding;
	}


	public void setInputDataEncoding(DataEncoding inputDataEncoding)
	{
		this.inputDataEncoding = inputDataEncoding;
	}


	public DataComponent getOutputDataComponents()
	{
		return outputDataComponents;
	}


	public void setOutputDataComponents(DataComponent outputDataComponents)
	{
		this.outputDataComponents = outputDataComponents;
	}


	public DataEncoding getOutputDataEncoding()
	{
		return outputDataEncoding;
	}


	public void setOutputDataEncoding(DataEncoding outputDataEncoding)
	{
		this.outputDataEncoding = outputDataEncoding;
	}
}
