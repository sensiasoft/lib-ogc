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

package org.vast.ows.sos;

import org.vast.util.Bbox;
import org.vast.util.TimeInfo;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLEnvelopeWriter;
import org.vast.ogc.gml.GMLException;
import org.vast.ogc.gml.GMLTimeWriter;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS GetObservation Request Writer v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS GetObservation
 * request based on values contained in a GetObservationRequest
 * object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 10, 2007
 * @version 1.0
 */
public class GetObservationWriterV10 extends AbstractRequestWriter<GetObservationRequest>
{
	protected GMLEnvelopeWriter bboxWriter;
    protected GMLTimeWriter timeWriter;
    
    
	public GetObservationWriterV10()
	{
        bboxWriter = new GMLEnvelopeWriter();
        timeWriter = new GMLTimeWriter();
	}

	
	@Override
	public String buildURLQuery(GetObservationRequest request) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(request.getGetServer());
        addCommonArgs(urlBuff, request);
		
		// offering
		urlBuff.append("&offering=" + request.getOffering());
        
		// event time
		if (request.getTime() != null)
		{
			urlBuff.append("&time=");
	        this.writeTimeArgument(urlBuff, request.getTime());
		}
        
        // procedure list
        int procCount = request.getProcedures().size();
        for (int i=0; i<procCount; i++)
        {
            if (i == 0)
                urlBuff.append("&procedures=");
            
            String nextProc = request.getProcedures().get(i);            
            urlBuff.append(urlEncode(nextProc));
            
            if (i != procCount-1)
                urlBuff.append(',');                
        }
        
		// observable list
		int obsCount = request.getObservables().size();
		for (int i=0; i<obsCount; i++)
		{
			if (i == 0)
				//urlBuff.append("&observables=");
				urlBuff.append("&observedProperty=");
			
            String nextObs = request.getObservables().get(i);            
            urlBuff.append(urlEncode(nextObs));
			
			if (i != obsCount-1)
				urlBuff.append(',');				
		}
		
		// add bbox only if specified
		Bbox bbox = request.getBbox();
        if (bbox != null && !bbox.isNull())
        {
            urlBuff.append("&bbox=");
            this.writeBboxArgument(urlBuff, bbox);
        }
        
        // format
        urlBuff.append("&format=" + urlEncode(request.getFormat()));
        
        // response mode
        if (request.getResponseMode() != null)
            urlBuff.append("&responseMode=" + this.getResponseMode(request));
        
        // result model
        if (request.getResultModel() != null)
            urlBuff.append("&resultModel=" + request.getResultModel());
		
        return urlBuff.toString();
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetObservationRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OGCRegistry.OGC));
		
		// root element
		Element rootElt = dom.createElement("sos:GetObservation");
		addCommonXML(dom, rootElt, request);
		
		// offering
		dom.setElementValue(rootElt, "sos:offering", request.getOffering());
		
		// event time
        try
        {
            TimeInfo timeInfo = request.getTime();
            if (timeInfo != null)
            {
                Element timeElt = timeWriter.writeTime(dom, timeInfo);
                Element opElt = null;

                if(timeInfo.isBaseAtNow())
                	opElt = dom.addElement(rootElt, "sos:eventTime/ogc:TM_After");
                else
                	opElt = dom.addElement(rootElt, "sos:eventTime/ogc:TM_During");
                
                dom.setElementValue(opElt, "ogc:PropertyName", "om:samplingTime");                
                opElt.appendChild(timeElt);
            }
        }
        catch (GMLException e)
        {
            throw new SOSException("Error while writing time", e);
        }
        
        // procedures
		int procCount = request.getProcedures().size();
		for (int i=0; i<procCount; i++)
			dom.setElementValue(rootElt, "+sos:procedure", request.getProcedures().get(i));
        
		// observables
		int obsCount = request.getObservables().size();
		for (int i=0; i<obsCount; i++)
			dom.setElementValue(rootElt, "+sos:observedProperty", request.getObservables().get(i));
		
        // foi bbox
		Bbox bbox = request.getBbox();
        if (bbox != null && !bbox.isNull())
        {
            Element envelopeElt = bboxWriter.writeEnvelope(dom, bbox);
            Element elt = dom.addElement(rootElt, "sos:featureOfInterest/ogc:BBOX");
            elt.appendChild(envelopeElt);
        }
		
		// response format
		dom.setElementValue(rootElt, "sos:responseFormat", request.getFormat());
        
		// result model
        if (request.getResultModel() != null)
            dom.setElementValue(rootElt, "sos:resultModel", request.getResultModel());
        
        // response mode (inline, attached, etc...)
        if (request.getResponseMode() != null)
            dom.setElementValue(rootElt, "sos:responseMode", this.getResponseMode(request));
        
		return rootElt;
	}
    
    
    /**
     * Convert response mode to token according to standard
     * @param query
     * @return
     */
    protected String getResponseMode(GetObservationRequest query)
    {
        switch (query.getResponseMode())
        {
            case INLINE: return "inline";
            case ATTACHED: return "attached";
            case OUT_OF_BAND: return "out-of-band";
            case RESULT_TEMPLATE: return "resultTemplate";
            case RESULT_ONLY: return "resultOnly";
            default: return null;
        }
    }
}