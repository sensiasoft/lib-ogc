/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sas;

import org.vast.ows.OWSLayerCapabilities;



/**
 * <p><b>Title:</b><br/>
 * SAS Layer Capabilities
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Contains SAS layer capabilities like available formats,
 * observables, procedures...
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Gregoire Berthiau
 * @date Jul 2, 2008
 * @version 1.0
 */
public class SASLayerCapabilities extends OWSLayerCapabilities
{
    protected String subscriptionOfferingID;
    protected String sensorID;
    protected String messageStructure;
    protected String frequencyUnit;
    protected String frequencyValue;
    
    public SASLayerCapabilities()
    {
    	messageStructure = null;
    	subscriptionOfferingID = null;
    	sensorID = null;
        frequencyUnit = null;
        frequencyValue = null;	
    }

    public String getMessageStructure()
	{
		return messageStructure;
	}

	public void setMessageStructure(String messageStructure)
	{
		this.messageStructure = messageStructure;
	}
    
	public String getSubscriptionOfferingID()
	{
		return subscriptionOfferingID;
	}

	public void setSubscriptionOfferingID(String subscriptionOfferingID)
	{
		this.subscriptionOfferingID = subscriptionOfferingID;
	}
	
	public String getFrequencyUnit()
	{
		return frequencyUnit;
	}

	public void setFrequencyUnit(String frequencyUnit)
	{
		this.frequencyUnit = frequencyUnit;
	}

	public String getSensorID()
	{
		return sensorID;
	}

	public void setSensorID(String sensorID)
	{
		this.sensorID = sensorID;
	}
	
	public String getFrequencyValue()
	{
		return frequencyValue;
	}

	public void setFrequencyValue(String frequencyValue)
	{
		this.frequencyValue = frequencyValue;
	}
	
}
