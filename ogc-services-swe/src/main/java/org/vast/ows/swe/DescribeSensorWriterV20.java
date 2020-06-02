/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import java.util.LinkedHashMap;
import java.util.Map;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SOS DescribeSensor
 * request based on values contained in a DescribeSensorRequest
 * object for version 1.0
 * </p>
 *
 * @author Alex Robin
 * @date Oct 10, 2008
 * */
public class DescribeSensorWriterV20 extends AbstractRequestWriter<DescribeSensorRequest>
{
    protected GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
    
    
	public DescribeSensorWriterV20()
	{
	}

	
	@Override
	public Map<String, String> buildURLParameters(DescribeSensorRequest request) throws OWSException
	{
	    Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
        
		// procedure
        urlParams.put("procedure", request.getProcedureID());
        
        // format
        urlParams.put("procedureDescriptionFormat", request.getFormat());
        
        // validTime
        if (request.getTime() != null)
        {
            StringBuilder buf = new StringBuilder();
            writeTimeArgument(buf, request.getTime());
            urlParams.put("validTime", buf.toString());
        }
        
		return urlParams;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, DescribeSensorRequest request) throws OWSException
	{
		dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));			
		
		// root element
		Element rootElt = dom.createElement("swes:DescribeSensor");
		addCommonXML(dom, rootElt, request);
		
		// procedure
        dom.setElementValue(rootElt, "swes:procedure", request.getProcedureID());
        
        // format
        dom.setElementValue(rootElt, "swes:procedureDescriptionFormat", request.getFormat());
        
		// time instant or period
        TimeExtent timeExtent = request.getTime();
        if (timeExtent != null)
        {
            try
            {
                Element timeElt = gmlUtils.writeTimeExtentAsTimePrimitive(dom, timeExtent);
                Element elt = dom.addElement(rootElt, "swes:validTime");
                elt.appendChild(timeElt);
            }
            catch (XMLWriterException e)
            {
                throw new OWSException("Cannot write time primitive", e);
            }
        }
        
		return rootElt;
	}
}