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
   Philippe Merigot <philippe.merigot@spotimage.fr>

******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.w3c.dom.Element;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.xml.DOMHelper;


/**
* <p>
* Provides methods to generate an XML SPS Confirm
* request based on values contained in a ConfirmRequest object
* for version 2.0
* </p>
*
* @author Alexandre Robin <alexandre.robin@spotimage.fr>
* @date Feb, 258 2008
**/
public class CancelRequestWriterV20 extends SWERequestWriter<CancelRequest>
{

	/**
	 * KVP Request
	 */
	@Override
	public String buildURLQuery(CancelRequest request) throws OWSException
	{
		throw new SPSException(noKVP + "SPS 2.0 " + request.getOperation());
	}


	/**
	 * XML Request
	 */
	@Override
	public Element buildXMLQuery(DOMHelper dom, CancelRequest request) throws OWSException
	{
		dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(SPSUtils.SPS, request.getVersion()));

		// root element
		Element rootElt = dom.createElement("sps:" + request.getOperation());
		addCommonXML(dom, rootElt, request);

		// taskID
		dom.setElementValue(rootElt, "sps:task", request.getTaskID());
	
		return rootElt;
	}
}