/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
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
 * <p>
 * Provides methods to generate a KVP or XML SOS GetResult
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a GetResultRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date Aug 6, 2012
 * */
public class GetResultWriterV20 extends SWERequestWriter<GetResultRequest>
{
    protected FESUtils fesUtils = new FESUtils(FESUtils.V2_0);
	
    
	public GetResultWriterV20()
	{
	}

	
	@Override
	public Map<String, String> buildURLParameters(GetResultRequest request) throws OWSException
	{
		Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
		
		// offerings
        urlParams.put("offering", request.getOffering());
        
        // observed properties (only one officially supported by SOS 2.0!)
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
            this.writeBboxArgument(buf, request.getBbox(), true);
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
		
		// observed properties (only one officially supported by SOS 2.0!)
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
        
		return rootElt;
	}
}