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

import java.util.ArrayList;
import java.util.List;
import org.vast.ows.OWSReferenceGroup;
import org.vast.ows.OWSResponse;


/**
 * <p><b>Title:</b>
 * DescribeResultAccess Response
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO DescribeResultAccessResponse type description
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Mar, 03 2008
 * @version 1.0
 */
public class DescribeResultAccessResponse extends OWSResponse
{
	protected List<OWSReferenceGroup> resultGroups;
	
	// when data is not available
	protected String reasonCode;
	protected String description;

	
	public DescribeResultAccessResponse()
	{
		this.service = SPSUtils.SPS;
        this.messageType = "DescribeResultAccessResponse";
	}


	public List<OWSReferenceGroup> getResultGroups()
	{
        if (this.resultGroups == null)
        	this.resultGroups = new ArrayList<OWSReferenceGroup>();
        
		return resultGroups;
	}


	public void setResultGroups(List<OWSReferenceGroup> referenceGroups)
	{
		this.resultGroups = referenceGroups;
	}


	public String getReasonCode()
	{
		return reasonCode;
	}


	public void setReasonCode(String reasonCode)
	{
		this.reasonCode = reasonCode;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}
}
