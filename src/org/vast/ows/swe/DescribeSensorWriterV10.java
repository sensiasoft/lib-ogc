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

package org.vast.ows.swe;

import org.vast.util.TimeInfo;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLEnvelopeWriter;
import org.vast.ogc.gml.GMLException;
import org.vast.ogc.gml.GMLTimeWriter;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.sos.SOSException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS DescribeSensor Request Writer v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS DescribeSensor
 * request based on values contained in a DescribeSensorRequest
 * object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 10, 2007
 * @version 1.0
 */
public class DescribeSensorWriterV10 extends AbstractRequestWriter<DescribeSensorRequest>
{
	protected GMLEnvelopeWriter bboxWriter;
    protected GMLTimeWriter timeWriter;
    
    
	public DescribeSensorWriterV10()
	{
        bboxWriter = new GMLEnvelopeWriter();
        timeWriter = new GMLTimeWriter();
	}

	
	@Override
	public String buildURLQuery(DescribeSensorRequest request) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(request.getGetServer());
        addCommonArgs(urlBuff, request);
        
		// procedure
        urlBuff.append("&procedure=" + request.getProcedure());
        
        if (request.getTime() != null)
        {
        	urlBuff.append("&time=");
        	this.writeTimeArgument(urlBuff, request.getTime());
        }
        
        // format
        urlBuff.append("&format=" + request.getFormat());

        String url = urlBuff.toString();
        url = url.replaceAll(" ","%20");
		return url;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, DescribeSensorRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OGCRegistry.SOS, request.getVersion()));			
		
		// root element
		Element rootElt = dom.createElement("sos:GetObservation");
		addCommonXML(dom, rootElt, request);
		
		// procedure
        if (request.getProcedure() != null)
        	dom.setElementValue(rootElt, "sos:procedure", request.getProcedure());
        
		// time instant or period
        try
        {
            TimeInfo timeInfo = request.getTime();
            if (timeInfo != null)
            {
                Element timeElt = timeWriter.writeTime(dom, timeInfo);
                Element elt = dom.addElement(rootElt, "sos:time");
                elt.appendChild(timeElt);
            }
        }
        catch (GMLException e)
        {
            throw new SOSException("Error while writing time", e);
        }
		
		// result format
		dom.setAttributeValue(rootElt, "@resultFormat", request.getFormat());
        
		return rootElt;
	}
}