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
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.sps.SPSOfferingCapabilities;


/**
 * <p>
 * Representation of SPS service capabilities document.
 * </p>
 *
 * @author Alex Robin
 * @date Dec 10, 2008
 * */
public class SPSServiceCapabilities extends OWSServiceCapabilities
{
	double minStatusTime; // in seconds
    List<String> supportedEncodings;
	
	
	public SPSServiceCapabilities()
	{
		this.service = SPSUtils.SPS;
	    supportedEncodings = new ArrayList<String>(2);
	}
	
	
	public List<String> getSupportedEncodings()
	{
		return supportedEncodings;
	}


    public double getMinStatusTime()
    {
        return minStatusTime;
    }


    public void setMinStatusTime(double minStatusTime)
    {
        this.minStatusTime = minStatusTime;
    }
    
    
    @Override
    public List<SPSOfferingCapabilities> getLayers()
    {
        return (List<SPSOfferingCapabilities>)super.getLayers();
    }


    @Override
    public SPSOfferingCapabilities getLayer(String layerId)
    {
        return (SPSOfferingCapabilities)super.getLayer(layerId);
    }
}
