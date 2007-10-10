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

package org.vast.ows.sos;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.gml.GMLEnvelopeWriter;
import org.vast.ows.gml.GMLException;
import org.vast.ows.gml.GMLTimeWriter;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;
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
	public String buildURLQuery(GetObservationRequest query) throws OWSException
	{
		StringBuffer urlBuff;
		
		urlBuff = new StringBuffer(query.getGetServer());
		urlBuff.append("service=SOS");
		urlBuff.append("&version=" + query.getVersion());
		urlBuff.append("&request=" + query.getRequest());
		
		// offering
		urlBuff.append("&offering=" + query.getOffering());
        
		// event time
		if (query.getTime() != null)
		{
			urlBuff.append("&time=");
	        this.writeTimeArgument(urlBuff, query.getTime());
		}
        
        // procedure list
        int procCount = query.getProcedures().size();
        for (int i=0; i<procCount; i++)
        {
            if (i == 0)
                urlBuff.append("&procedures=");
            
            String nextProc = query.getProcedures().get(i);            
            try {urlBuff.append(URLEncoder.encode(nextProc, "UTF-8"));}
            catch (UnsupportedEncodingException e) {e.printStackTrace();}
            
            if (i != procCount-1)
                urlBuff.append(',');                
        }
        
		// observable list
		int obsCount = query.getObservables().size();
		for (int i=0; i<obsCount; i++)
		{
			if (i == 0)
				urlBuff.append("&observables=");
			
            String nextObs = query.getObservables().get(i);            
			try {urlBuff.append(URLEncoder.encode(nextObs, "UTF-8"));}
            catch (UnsupportedEncodingException e) {e.printStackTrace();}
			
			if (i != obsCount-1)
				urlBuff.append(',');				
		}
		
		// add bbox only if specified
		Bbox bbox = query.getBbox();
        if (bbox != null && !bbox.isNull())
        {
            urlBuff.append("&bbox=");
            this.writeBboxArgument(urlBuff, bbox);
        }
        
        // format
        urlBuff.append("&format=" + query.getFormat());
        
        // response mode
        if (query.getResponseMode() != null)
            urlBuff.append("&responseMode=" + this.getResponseMode(query));
        
        // result model
        if (query.getResultModel() != null)
            urlBuff.append("&resultModel=" + query.getResultModel());
		
		return urlBuff.toString();
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetObservationRequest query) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI("SOS", "1.0"));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI("OGC"));
		
		// root element
		Element rootElt = dom.createElement("sos:GetObservation");
		addCommonXML(dom, rootElt, query);
		
		// offering
		dom.setElementValue(rootElt, "sos:offering", query.getOffering());
		
		// event time
        try
        {
            TimeInfo timeInfo = query.getTime();
            if (timeInfo != null)
            {
                Element timeElt = timeWriter.writeTime(dom, timeInfo);
                Element elt = dom.addElement(rootElt, "sos:eventTime/ogc:During");
                elt.appendChild(timeElt);
            }
        }
        catch (GMLException e)
        {
            throw new SOSException("Error while writing time", e);
        }
        
        // procedures
		int procCount = query.getProcedures().size();
		for (int i=0; i<procCount; i++)
			dom.setElementValue(rootElt, "+sos:procedure", query.getProcedures().get(i));
        
		// observables
		int obsCount = query.getObservables().size();
		for (int i=0; i<obsCount; i++)
			dom.setElementValue(rootElt, "+sos:observedProperty", query.getObservables().get(i));
		
        // foi bbox
        try
        {
            Bbox bbox = query.getBbox();
            if (bbox != null && !bbox.isNull())
            {
                Element envelopeElt = bboxWriter.writeEnvelope(dom, bbox);
                Element elt = dom.addElement(rootElt, "sos:featureOfInterest/ogc:BBOX");
                elt.appendChild(envelopeElt);
            }
        }
        catch (GMLException e)
        {
            throw new SOSException("Error while writing bbox", e);
        }
		
		// response format
		dom.setElementValue(rootElt, "sos:responseFormat", query.getFormat());
        
		// result model
        if (query.getResultModel() != null)
            dom.setElementValue(rootElt, "sos:resultModel", query.getResultModel());
        
        // response mode (inline, attached, etc...)
        if (query.getResponseMode() != null)
            dom.setElementValue(rootElt, "sos:responseMode", this.getResponseMode(query));
        
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