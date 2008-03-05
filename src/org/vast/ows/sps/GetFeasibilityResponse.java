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
import org.vast.ows.OWSResponse;
import org.vast.util.DateTime;


/**
 * <p><b>Title:</b>
 * GetFeasibility Response
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO GetFeasibilityResponse type description
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 25 2008
 * @version 1.0
 */
public class GetFeasibilityResponse extends OWSResponse
{
	protected FeasibilityStudy feasibilityStudy;
	protected List<Alternative> alternatives;
	protected DateTime latestResponseTime;
		

	public GetFeasibilityResponse()
	{
		this.service = "SPS";
        this.messageType = "GetFeasibilityResponse";
        this.alternatives = new ArrayList<Alternative>();
	}


	public FeasibilityStudy getFeasibilityStudy()
	{
		return feasibilityStudy;
	}


	public void setFeasibilityStudy(FeasibilityStudy feasibilityStudy)
	{
		this.feasibilityStudy = feasibilityStudy;
	}


	public List<Alternative> getAlternatives()
	{
		return alternatives;
	}


	public void setAlternatives(List<Alternative> alternatives)
	{
		this.alternatives = alternatives;
	}


	public DateTime getLatestResponseTime()
	{
		return latestResponseTime;
	}


	public void setLatestResponseTime(DateTime latestResponseTime)
	{
		this.latestResponseTime = latestResponseTime;
	}	
}
