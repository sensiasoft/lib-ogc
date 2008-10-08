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
public class FeasibilityStudy extends ProgressReport
{
	public final static String FEASIBLE = "FEASIBLE";
	public final static String NOT_FEASIBLE = "NOT_FEASIBLE";

	protected double successRate = Double.NaN;


	public double getSuccessRate()
	{
		return successRate;
	}


	public void setSuccessRate(double successRate)
	{
		this.successRate = successRate;
	}
}
