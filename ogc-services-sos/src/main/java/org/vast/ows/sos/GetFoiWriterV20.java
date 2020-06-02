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
 * Provides methods to generate a KVP or XML SOS GetFeatureOfInterest
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a GetFeatureOfInterestRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date May 21, 2015
 * */
public class GetFoiWriterV20 extends SWERequestWriter<GetFeatureOfInterestRequest>
{
	protected FESUtils fesUtils = new FESUtils(FESUtils.V2_0);
	
    
	public GetFoiWriterV20()
	{	    
	}

	
	@Override
	public Map<String, String> buildURLParameters(GetFeatureOfInterestRequest request) throws OWSException
	{
		Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
		
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
	public Element buildXMLQuery(DOMHelper dom, GetFeatureOfInterestRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OWSUtils.OGC));
		fesUtils.resetIdCounters();
		
		// root element
		Element rootElt = dom.createElement("sos:GetFeatureOfInterest");
		addCommonXML(dom, rootElt, request);
		
		// procedures
        for (String proc: request.getProcedures())
            dom.setElementValue(rootElt, "+sos:procedure", proc);
        
		// observed properties
        for (String obs: request.getObservables())
            dom.setElementValue(rootElt, "+sos:observedProperty", obs);
        
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