/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code part of the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wns;

import java.util.*;
import org.vast.ows.OWSLayerCapabilities;


/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Jan 17, 2008
 * */
public class WNSCapabilities extends OWSLayerCapabilities
{
	protected List<String> supportedProtocols;
	protected double maxMessageTTL;
	

	public WNSCapabilities()
	{
		supportedProtocols = new ArrayList<String>();
	}


	public List<String> getSupportedProtocols()
	{
		return supportedProtocols;
	}


	public double getMaxMessageTTL()
	{
		return maxMessageTTL;
	}


	public void setMaxMessageTTL(double maxMessageTTL)
	{
		this.maxMessageTTL = maxMessageTTL;
	}	
}
