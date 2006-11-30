/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sas;

import org.vast.io.xml.DOMWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSQuery;
import org.vast.ows.OWSRequestWriter;
import org.vast.ows.gml.GMLEnvelopeWriter;
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLTimeWriter;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2006</p>
 * @author Tony Cook
 * @date Nov 21, 2006
 * @version 1.0
 */
public class SASRequestWriter extends OWSRequestWriter
{
	public SASRequestWriter()
	{
	}

	
	@Override
	public String buildGetRequest(OWSQuery owsQuery)
	{
		//  Some SAS ops don't support KVP, so I'm not dealing with it yet
		return null;
	}
	
	
	//  Initially, just try to do a Subscribe request
	@Override
	public Element buildRequestXML(OWSQuery owsQuery, DOMWriter domWriter) throws OWSException
	{
		Element elt;
		SASQuery query = (SASQuery)owsQuery;
		domWriter.addNS("http://www.opengis.net/sas", "sas");
		domWriter.addNS("http://www.opengis.net/ogc", "ogc");
//		domWriter.addNS("http://www.opengeospatial.net/swe", "swe");
		
		// root element
		Element rootElt = domWriter.createElement("sas:Subscribe");
		domWriter.setAttributeValue(rootElt, "version", query.getVersion());
		domWriter.setAttributeValue(rootElt, "service", query.getService());
		
		// Sub ID
		if(query.getSubscriptionId() != null)
			domWriter.setElementValue(rootElt, "sas:SubscriptionOfferingID", query.getSubscriptionId());
        
		//  FeatureOfInterest
		if(query.getFeatureOfInterest() != null)
			domWriter.setElementValue(rootElt, "sas:FeatureOfInterestName", query.getFeatureOfInterest());
		// display request
       // try {domWriter.writeDOM(rootElt, System.out, null);}
       // catch (Exception e) {e.printStackTrace();}        
        
		return rootElt;
	}
}