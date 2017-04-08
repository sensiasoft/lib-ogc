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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sas;

import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.swe.SWEUtils;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SAS Subscribe
 * request based on values contained in a SASSubscribeRequest
 * object
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Jul 10, 2008
 * */
public class SASSubscribeWriter extends AbstractRequestWriter<SASSubscribeRequest>
{
    
	public SASSubscribeWriter(){
		
	}
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, SASSubscribeRequest request) throws OWSException
	{
		dom.addUserPrefix("sas", OGCRegistry.getNamespaceURI(OWSUtils.SAS, request.getVersion()));
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWEUtils.SWE, "1.0"));
		// root element		
		Element rootElt = dom.createElement("sas:Subscribe");
		addCommonXML(dom, rootElt, request);
		//dom.
		// SubscriptionOfferingID
		if(request.getSubscriptionOfferingID()!=null)
			dom.setElementValue(rootElt, "sas:EventFilter/sas:SensorID", request.getSubscriptionOfferingID());
		
		// FeatureOfInterestName
		if(request.getfoiName()!=null)
			dom.setElementValue(rootElt, "sas:FeatureOfInterestName", request.getfoiName());
		
		return rootElt;
	}
    

	@Override
	public String buildURLQuery(SASSubscribeRequest request)
			throws OWSException {
		// TODO Auto-generated method stub
		return null;
	}
}