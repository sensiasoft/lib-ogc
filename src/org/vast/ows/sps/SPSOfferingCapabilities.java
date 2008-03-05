/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import java.util.List;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.util.Bbox;


/**
 * <p><b>Title:</b><br/>
 * SPS Offering Capabilities
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
public class SPSOfferingCapabilities extends OWSLayerCapabilities
{
	protected Bbox areaOfService;
	protected List<String> observableList;
	protected DescribeTaskingResponse parametersDescription;
	protected Object sensorDescription;


	public Bbox getAreaOfService()
	{
		return areaOfService;
	}


	public void setAreaOfService(Bbox areaOfService)
	{
		this.areaOfService = areaOfService;
	}


	public List<String> getObservableList()
	{
		return observableList;
	}


	public void setObservableList(List<String> observableList)
	{
		this.observableList = observableList;
	}


	public DescribeTaskingResponse getParametersDescription()
	{
		return parametersDescription;
	}


	public void setParametersDescription(DescribeTaskingResponse parameterDescription)
	{
		this.parametersDescription = parameterDescription;
	}


	public Object getSensorDescription()
	{
		return sensorDescription;
	}


	public void setSensorDescription(Object sensorDescription)
	{
		this.sensorDescription = sensorDescription;
	}

}
