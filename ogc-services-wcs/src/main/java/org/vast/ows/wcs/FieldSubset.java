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
package org.vast.ows.wcs;

import java.util.ArrayList;
import java.util.List;
import org.vast.ows.OWSIdentification;

/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Dec 4, 2007
 * */
public class FieldSubset extends OWSIdentification
{
	protected List<AxisSubset> axisSubsets;
	protected String interpolationMethod;
	
	
	public FieldSubset()
	{
		axisSubsets = new ArrayList<AxisSubset>();
	}


	public List<AxisSubset> getAxisSubsets()
	{
		return axisSubsets;
	}


	public void setAxisSubsets(List<AxisSubset> axisSubsets)
	{
		this.axisSubsets = axisSubsets;
	}


	public String getInterpolationMethod()
	{
		return interpolationMethod;
	}


	public void setInterpolationMethod(String interpolationMethod)
	{
		this.interpolationMethod = interpolationMethod;
	}
}
