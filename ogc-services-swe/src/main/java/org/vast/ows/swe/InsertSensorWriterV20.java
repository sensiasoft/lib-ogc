/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import java.io.IOException;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.sensorML.SMLUtils;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SOS InsertSensor
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertSensorRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
public class InsertSensorWriterV20<RequestType extends InsertSensorRequest> extends SWERequestWriter<RequestType>
{
    SMLUtils smlUtils = new SMLUtils(SMLUtils.V2_0);
    
    
	public InsertSensorWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, RequestType request) throws OWSException
	{
		dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));
		
		// root element
		Element rootElt = dom.createElement("swes:" + request.getOperation());
		addCommonXML(dom, rootElt, request);
		
		// format
		dom.setElementValue(rootElt, "swes:procedureDescriptionFormat", request.getProcedureDescriptionFormat());
		
		// procedure
		try
        {
		    // check format is supported
		    String format = request.getProcedureDescriptionFormat();
		    if (!format.equals(OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "2.0")))
		        throw new OWSException("Unsupported format: " + request.getProcedureDescriptionFormat());
	            
		    Element procedureElt = dom.addElement(rootElt, "swes:procedureDescription");
		    Element processElt = smlUtils.writeProcess(dom, request.getProcedureDescription());
            procedureElt.appendChild(processElt);
        }
        catch (IOException e)
        {
            throw new OWSException("Cannot write SensorML document", e);
        }   
        
        // observable properties
        for (String observable: request.getObservableProperties())
            dom.setElementValue(rootElt, "+swes:observableProperty", observable);
        
        // related features
        for (FeatureRef featureRef: request.getRelatedFeatures())
            dom.setElementValue(rootElt, "+swes:relatedFeature/swes:FeatureRelationship", featureRef.getHref());
        
        return rootElt;
	}
}