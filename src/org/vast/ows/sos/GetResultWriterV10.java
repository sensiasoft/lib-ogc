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
 * SOS GetResult Request Writer v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS GetResult
 * request based on values contained in a GetResultRequest
 * object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 10, 2007
 * @version 1.0
 */
public class GetResultWriterV10 extends AbstractRequestWriter<GetResultRequest>
{
	protected GMLEnvelopeWriter bboxWriter;
    protected GMLTimeWriter timeWriter;
    
    
	public GetResultWriterV10()
	{
        bboxWriter = new GMLEnvelopeWriter();
        timeWriter = new GMLTimeWriter();
	}

	
	@Override
	public String buildURLQuery(GetResultRequest request) throws OWSException
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
            try {urlBuff.append(URLEncoder.encode(nextProc, "UTF-8"));}
            catch (UnsupportedEncodingException e) {e.printStackTrace();}
            
            if (i != procCount-1)
                urlBuff.append(',');                
        }
        
		// observable list
		int obsCount = request.getObservables().size();
		for (int i=0; i<obsCount; i++)
		{
			if (i == 0)
				urlBuff.append("&observables=");
			
            String nextObs = request.getObservables().get(i);            
			try {urlBuff.append(URLEncoder.encode(nextObs, "UTF-8"));}
            catch (UnsupportedEncodingException e) {e.printStackTrace();}
			
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
        urlBuff.append("&format=" + request.getFormat());
        
        String url = urlBuff.toString();
        url = url.replaceAll(" ","%20");
		return url;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetResultRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OGCRegistry.SOS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OGCRegistry.OGC));
		
		// root element
		Element rootElt = dom.createElement("sos:GetResult");
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
                Element opElt = dom.addElement(rootElt, "sos:eventTime/ogc:TM_During");
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
        try
        {
            Bbox bbox = request.getBbox();
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
		dom.setElementValue(rootElt, "sos:responseFormat", request.getFormat());
        
		return rootElt;
	}
    
}