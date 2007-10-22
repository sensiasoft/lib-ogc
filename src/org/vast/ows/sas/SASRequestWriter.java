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

import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.AbstractRequestWriter;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * Provides methods to build a GET or POST SAS request and
 * using an SASQuery object for OWS4 version 0.0.31
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Tony Cook
 * @date Nov 21, 2006
 * @version 1.0
 */
public class SASRequestWriter extends AbstractRequestWriter<SASQuery>
{
	public SASRequestWriter()
	{
	}

	
	@Override
	public String buildURLQuery(SASQuery query)
	{
		//  Some SAS ops don't support KVP, so I'm not dealing with it yet
		return null;
	}
	
	
	//  Initially, just try to do a Subscribe request
	@Override
	public Element buildXMLQuery(DOMHelper dom, SASQuery query) throws OWSException
	{
		dom.addUserPrefix("sas", OGCRegistry.getNamespaceURI(OGCRegistry.SAS, query.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OGCRegistry.OGC));
//		domWriter.addNS("http://www.opengeospatial.net/swe", "swe");
		
		// root element
		Element rootElt = dom.createElement("sas:Subscribe");
		dom.setAttributeValue(rootElt, "version", query.getVersion());
		dom.setAttributeValue(rootElt, "service", query.getService());
		
		// Sub ID
		if(query.getSubscriptionId() != null)
			dom.setElementValue(rootElt, "sas:SubscriptionOfferingID", query.getSubscriptionId());
        
		//  FeatureOfInterest
		if(query.getFeatureOfInterest() != null)
			dom.setElementValue(rootElt, "sas:FeatureOfInterestName", query.getFeatureOfInterest());
		// display request
       // try {domWriter.writeDOM(rootElt, System.out, null);}
       // catch (Exception e) {e.printStackTrace();}        
        
		return rootElt;
	}
}