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

package org.vast.ows.sos;

import java.util.LinkedHashMap;
import java.util.Map;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS GetResult Request Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS GetResult
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a GetResultRequest object 
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Aug 6, 2012
 * @version 1.0
 */
public class GetResultWriterV20 extends AbstractRequestWriter<GetResultRequest>
{
	protected Encoder filterEncoder;
	
    
	public GetResultWriterV20()
	{
	    Configuration configuration = new org.geotools.filter.v2_0.FESConfiguration();
	    filterEncoder = new Encoder(configuration);
	}

	
	@Override
	public Map<String, String> buildURLParameters(GetResultRequest request) throws OWSException
	{
		Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
		
		// offerings
        urlParams.put("offering", request.getOffering());
        
        // observed properties
        urlParams.put("observedProperty", request.getObservables().get(0));
        
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
		    buf.append(request.getTemporalFilter().getExpression1().toString());
		    buf.append(',');
	        this.writeTimeArgument(buf, request.getTime());
	        urlParams.put("temporalfilter", buf.toString());
		}
        		
		// spatial filter
		if (request.getSpatialFilter() != null && !request.getBbox().isNull())
        {
            StringBuilder buf = new StringBuilder();
            buf.append(request.getSpatialFilter().getExpression1().toString());
            buf.append(',');
            this.writeBboxArgument(buf, request.getBbox());
            urlParams.put("spatialFilter", buf.toString());
        }
		
        return urlParams;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetResultRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		
		// root element
		Element rootElt = dom.createElement("sos:GetResult");
		addCommonXML(dom, rootElt, request);
		
		// procedures
        for (String proc: request.getProcedures())
            dom.setElementValue(rootElt, "+sos:procedure", proc);
        
		// offering
		dom.setElementValue(rootElt, "+sos:offering", request.getOffering());
		
		// observed properties
        dom.setElementValue(rootElt, "+sos:observedProperty", request.getObservables().get(0));
		
		// temporal filter
        if (request.getTemporalFilter() != null)
        {
            try
            {
                Element propElt = dom.addElement(rootElt, "sos:temporalFilter");
                Document doc = filterEncoder.encodeAsDOM(request.getTemporalFilter(), org.geotools.filter.v2_0.FES.temporalOps);
                Element filterElt = (Element)dom.getDocument().importNode(doc.getDocumentElement(), true);
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
                Document doc = filterEncoder.encodeAsDOM(request.getSpatialFilter(), org.geotools.filter.v2_0.FES.spatialOps);
                Element filterElt = (Element)dom.getDocument().importNode(doc.getDocumentElement(), true);
                propElt.appendChild(filterElt);                
            }
            catch (Exception e)
            {
                throw new SOSException("Error while writing spatial filter", e);
            }
        }
        
		return rootElt;
	}
}