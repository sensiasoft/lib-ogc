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

import org.vast.sweCommon.SWEData;
import org.vast.util.DateTime;


/**
 * <p><b>Title:</b><br/>
 * Feasibility Study
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 25 2008
 * @version 1.0
 */
public class FeasibilityStudy
{
	public final static String FEASIBLE = "FEASIBLE";
	public final static String NOT_FEASIBLE = "NOT_FEASIBLE";
	public final static String DELAYED = "delayed";

	protected String id;
	protected String feasibilityCode;
	protected DateTime estimatedToC;
	protected double successRate = Double.NaN;
	protected String description;
	protected SWEData studyParameters;


	public String getId()
	{
		return id;
	}


	public void setId(String id)
	{
		this.id = id;
	}


	public String getFeasibilityCode()
	{
		return feasibilityCode;
	}


	public void setFeasibilityCode(String feasibilityCode)
	{
		this.feasibilityCode = feasibilityCode;
	}


	public double getSuccessRate()
	{
		return successRate;
	}


	public void setSuccessRate(double successRate)
	{
		this.successRate = successRate;
	}


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


	public SWEData getStudyParameters()
	{
		return studyParameters;
	}


	public void setStudyParameters(SWEData studyParameters)
	{
		this.studyParameters = studyParameters;
	}


	public DateTime getEstimatedToC()
	{
		return estimatedToC;
	}


	public void setEstimatedToC(DateTime estimatedToC)
	{
		this.estimatedToC = estimatedToC;
	}
}
