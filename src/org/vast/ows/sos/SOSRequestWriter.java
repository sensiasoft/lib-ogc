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

package org.vast.ows.sos;

import org.vast.io.xml.DOMWriter;
import org.vast.ows.OWSQuery;
import org.vast.ows.OWSRequestWriter;
import org.vast.util.*;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS POST/GET Request Builder
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a GET or POST SOSS request based
 * on values contained in a SOSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Sep 15, 2005
 * @version 1.0
 */
public class SOSRequestWriter extends OWSRequestWriter
{
	
	public SOSRequestWriter()
	{	
	}

	
	@Override
	public String buildGetRequest(OWSQuery owsQuery)
	{
		SOSQuery query = (SOSQuery)owsQuery;
		StringBuffer urlBuff;
		
		urlBuff = new StringBuffer(query.getGetServer());
		urlBuff.append("service=SOS");
		urlBuff.append("&version=" + query.getVersion());
		urlBuff.append("&request=" + query.getRequest());
		urlBuff.append("&offering=" + query.getOffering());
		urlBuff.append("&time=" + DateTimeFormat.formatIso(query.getTime().getStartTime(), 0));
		urlBuff.append("/" + DateTimeFormat.formatIso(query.getTime().getStopTime(), 0));
		urlBuff.append("&format=" + query.getFormat());
		
		// add observable list
		int obsCount = query.getObservables().size();
		for (int i=0; i<obsCount; i++)
		{
			if (i == 0)
				urlBuff.append("&observables=");
			
			urlBuff.append(query.getObservables().get(i));
			
			if (i != obsCount-1)
				urlBuff.append(',');				
		}
		
		// display request
		System.out.println("SOS Request: " + urlBuff);
		
		return urlBuff.toString();
	}
	
	
	@Override
	public Element buildRequestXML(OWSQuery owsQuery, DOMWriter domWriter) throws SOSException
	{
		Element elt;
		SOSQuery query = (SOSQuery)owsQuery;
		domWriter.addNS("http://www.opengis.net/sos", "sos");
		domWriter.addNS("http://www.opengis.net/ogc", "ogc");
		domWriter.addNS("http://www.opengis.net/gml", "gml");
		domWriter.addNS("http://www.opengis.net/swe", "swe");
		
		// root element
		Element rootElt = domWriter.createElement("sos:GetObservation");
		domWriter.setAttributeValue(rootElt, "version", query.getVersion());
		domWriter.setAttributeValue(rootElt, "service", query.getService());
		
		// offering ID
		domWriter.setElementValue(rootElt, "sos:offering", query.getOffering());
		
		// time period
		if (query.getTime().isTimeInstant())
		{
			elt = domWriter.addElement(rootElt, "sos:eventTime/ogc:During/gml:TimeInstant/gml:timePosition");
			domWriter.setElementValue(elt, "", DateTimeFormat.formatIso(query.getTime().getStartTime(), 0));
		}
		else
		{
			elt = domWriter.addElement(rootElt, "sos:eventTime/ogc:During/gml:TimePeriod/gml:beginPosition");
			domWriter.setElementValue(elt, "", DateTimeFormat.formatIso(query.getTime().getStartTime(), 0));
			elt = domWriter.addElement(rootElt, "sos:eventTime/ogc:During/gml:TimePeriod/gml:endPosition");
			domWriter.setElementValue(elt, "", DateTimeFormat.formatIso(query.getTime().getStopTime(), 0));
		}
		
		// procedures
		int procCount = query.getProcedures().size();
		for (int i=0; i<procCount; i++)
			domWriter.setElementValue(rootElt, "+sos:procedure", query.getProcedures().get(i));
		
		// observables
		int obsCount = query.getObservables().size();
		for (int i=0; i<obsCount; i++)
			domWriter.setElementValue(rootElt, "+sos:observedProperty", query.getObservables().get(i));
		
		// result
		domWriter.setElementValue(rootElt, "sos:resultFormat", query.getFormat());
		domWriter.setElementValue(rootElt, "sos:resultModel", "swe:DataValueType");
		
		return rootElt;
	}
}