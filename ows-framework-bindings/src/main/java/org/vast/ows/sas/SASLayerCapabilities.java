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

import java.util.ArrayList;
import java.util.List;
import org.vast.ows.OWSLayerCapabilities;
import org.w3c.dom.Element;


/**
 * <p>
 * Contains SAS layer capabilities like available formats,
 * observables, procedures...
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Jul 2, 2008
 * */
public class SASLayerCapabilities extends OWSLayerCapabilities
{
	protected List<String> subscriptionOfferingIDList;
    protected List<String> sensorIDList;
    protected List<String> SweDataComponentList;
    protected List<String> messageStructureNameList;
    protected List<String> frequencyList;
    protected String messageStructure = null;
    protected String XMPPURI = null;
    
    public SASLayerCapabilities()
    {
    	subscriptionOfferingIDList = new ArrayList<String>(1);
    	sensorIDList = new ArrayList<String>(1);
    	SweDataComponentList = new ArrayList<String>(1);
    	messageStructureNameList = new ArrayList<String>(1);
    	frequencyList = new ArrayList<String>(1);
    }

    public String getXMPPURI()
	{
		return XMPPURI;
	}

	public void setXMPPURI(String XMPPURI)
	{
		this.XMPPURI = XMPPURI;
	}
    
    public List<String> getSweDataComponentList()
	{
		return SweDataComponentList;
	}

	public void setSweDataComponentString(String SweDataComponent)
	{
		this.SweDataComponentList.add(SweDataComponent);
	}
    
    public String getMessageStructure()
	{
		return messageStructure;
	}

	public void setMessageStructure(String messageStructure)
	{
		this.messageStructure = messageStructure;
	}
	
    public List<String> getMessageStructureNameList()
	{
		return messageStructureNameList;
	}

	public void setMessageStructureName(String messageStructureName)
	{
		(this.messageStructureNameList).add(messageStructureName);
	}
    
	public List<String> getSubscriptionOfferingIDList()
	{
		return subscriptionOfferingIDList;
	}

	public void setSubscriptionOfferingID(String subscriptionOfferingID)
	{
		this.subscriptionOfferingIDList.add(subscriptionOfferingID);
	}
	
	public List<String> getFrequencyList()
	{
		return frequencyList;
	}

	public void setFrequency(String frequency)
	{
		this.frequencyList.add(frequency);
	}

	public List<String> getSensorIDList()
	{
		return sensorIDList;
	}

	public void setSensorID(String sensorID)
	{
		this.sensorIDList.add(sensorID);
	}
	
}
