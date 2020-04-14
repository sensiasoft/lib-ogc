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

package org.vast.ows.sas;

import org.w3c.dom.Element;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.xml.DOMHelper;


/**
* <p>
* Provides methods to generate a XML SAS alert
* </p>
*
* @author Gregoire Berthiau 
* @date Jan, 28 2009
**/
public class SASAlertWriter
{
	/**
	 * XML Request
	 */
	public Element buildXMLQuery(String sensorID, String timestamp, String alertData) throws OWSException
	{
		DOMHelper dom = new DOMHelper(OGCRegistry.getNamespaceURI("SAS", "0.0"));
		
		Element rootElt = dom.getBaseElement();
		
		// sensorID
		if (sensorID != null)
			dom.setElementValue(rootElt , "SensorID", sensorID);
		else 
			throw new OWSException("a sensorID must be provided");
		
		// timestamp
		if (timestamp != null)
			dom.setElementValue(rootElt, "Timestamp", timestamp);
		else 
			throw new OWSException("a timestamp must be provided");
		
		// alertData
		if (alertData != null)
			dom.setElementValue(rootElt, "AlertData", alertData);
		else 
			throw new OWSException("Alert data must be provided");
		
		return rootElt;
	}
}