/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.LinkedHashMap;
import java.util.Map;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.fes.FESUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS GetObservation Request Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS GetObservation
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a GetObservationRequest object 
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Aug 4, 2012
 * @version 1.0
 */
public class GetObservationWriterV20 extends SWERequestWriter<GetObservationRequest>
{
	protected FESUtils fesUtils = new FESUtils();
	
    
	public GetObservationWriterV20()
	{	    
	}

	
	@Override
	public Map<String, String> buildURLParameters(GetObservationRequest request) throws OWSException
	{
		Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
		
		// offerings
        if (!request.getOfferings().isEmpty())
        {
            StringBuilder buf = new StringBuilder();
            
            for (String off: request.getOfferings())
            {
                buf.append(off);
                buf.append(',');
            }
            buf.deleteCharAt(buf.length()-1);
            
            urlParams.put("offering", buf.toString());
        }
        
        // observed properties
        if (!request.getObservables().isEmpty())
        {
            StringBuilder buf = new StringBuilder();
            
            for (String off: request.getObservables())
            {
                buf.append(off);
                buf.append(',');
            }
            buf.deleteCharAt(buf.length()-1);
            
            urlParams.put("observedProperty", buf.toString());
        }
        
        // procedures
        if (!request.getProcedures().isEmpty())
        {
            StringBuilder buf = new StringBuilder();
            
            for (String off: request.getProcedures())
            {
                buf.append(off);
                buf.append(',');
            }
            buf.deleteCharAt(buf.length()-1);
            
            urlParams.put("procedure", buf.toString());
        }
        
        // features of interest
        if (!request.getFoiIDs().isEmpty())
        {
            StringBuilder buf = new StringBuilder();
            
            for (String off: request.getFoiIDs())
            {
                buf.append(off);
                buf.append(',');
            }
            buf.deleteCharAt(buf.length()-1);
            
            urlParams.put("featureOfInterest", buf.toString());
        }
        
        // TODO namespaces
        
		// temporal filter
		if (request.getTemporalFilter() != null && !request.getTime().isNull())
		{
		    StringBuilder buf = new StringBuilder();
		    buf.append(request.getTemporalFilter().getOperand1().toString());
		    buf.append(',');
	        this.writeTimeArgument(buf, request.getTime());
	        urlParams.put("temporalfilter", buf.toString());
		}
        		
		// spatial filter
		if (request.getSpatialFilter() != null && !request.getBbox().isNull())
        {
            StringBuilder buf = new StringBuilder();
            buf.append(request.getSpatialFilter().getOperand1().toString());
            buf.append(',');
            this.writeBboxArgument(buf, request.getBbox());
            urlParams.put("spatialFilter", buf.toString());
        }
        
        // format
        if (request.getFormat() != null)
            urlParams.put("responseFormat", request.getFormat());
		
        return urlParams;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetObservationRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OWSUtils.OGC));
		fesUtils.resetIdCounters();
		
		// root element
		Element rootElt = dom.createElement("sos:GetObservation");
		addCommonXML(dom, rootElt, request);
		
		// procedures
        for (String proc: request.getProcedures())
            dom.setElementValue(rootElt, "+sos:procedure", proc);
        
		// offering
		for (String off: request.getOfferings())
		    dom.setElementValue(rootElt, "+sos:offering", off);
		
		// observed properties
        for (String obs: request.getObservables())
            dom.setElementValue(rootElt, "+sos:observedProperty", obs);
		
		// temporal filter
        if (request.getTemporalFilter() != null)
        {
            try
            {
                Element propElt = dom.addElement(rootElt, "sos:temporalFilter");
                Element filterElt = fesUtils.writeTemporalFilter(dom, request.getTemporalFilter());
                propElt.appendChild(filterElt);
                
            }
            catch (Exception e)
            {
                throw new SOSException("Error while writing temporal filter", e);
            }
        }
        
        // Foi IDs
        for (String foi: request.getFoiIDs())
            dom.setElementValue(rootElt, "+sos:featureOfInterest", foi);
        
        // spatial filter
        if (request.getSpatialFilter() != null)
        {
            try
            {
                Element propElt = dom.addElement(rootElt, "sos:spatialFilter");
                Element filterElt = fesUtils.writeSpatialFilter(dom, request.getSpatialFilter());
                propElt.appendChild(filterElt);
            }
            catch (Exception e)
            {
                throw new SOSException("Error while writing spatial filter", e);
            }
        }
        
        // response format
        if (request.getFormat() != null)
            dom.setElementValue(rootElt, "sos:responseFormat", request.getFormat());
        
		return rootElt;
	}
}