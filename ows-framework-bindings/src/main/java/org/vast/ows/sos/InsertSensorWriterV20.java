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

import java.io.IOException;
import org.vast.xml.DOMHelper;
import org.vast.xml.IXMLWriterDOM;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.sensorML.SMLProcess;
import org.vast.sensorML.SMLUtils;
import org.vast.sensorML.SystemWriterV1;
import org.vast.sensorML.SystemWriterV20;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS InsertSensor Request Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS InsertSensor
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertSensorRequest object 
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 2, 2014
 * @version 1.0
 */
public class InsertSensorWriterV20 extends SWERequestWriter<InsertSensorRequest>
{
    
	public InsertSensorWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertSensorRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));
		
		// root element
		Element rootElt = dom.createElement("swes:" + request.getOperation());
		addCommonXML(dom, rootElt, request);
		
		// format
		dom.setElementValue(rootElt, "swes:procedureDescriptionFormat", request.getProcedureDescriptionFormat());
		
		// procedure
		try
        {
		    IXMLWriterDOM<SMLProcess> writer = null;
		    String format = request.getProcedureDescriptionFormat();
		    if (format.startsWith((OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "1.0"))))
		        writer = new SystemWriterV1();
	        else if (format.equals(OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "2.0")))
	            writer = new SystemWriterV20();
	        else
	            throw new OWSException("Unsupported format: " + request.getProcedureDescriptionFormat());
	            
		    Element procedureElt = dom.addElement(rootElt, "swes:procedureDescription");
		    Element sysElt = writer.write(dom, request.getProcedureDescription());
            procedureElt.appendChild(sysElt);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error while writing SensorML document", e);
        }   
        
        // observable properties
        for (String observable: request.getObservableProperties())
            dom.setElementValue(rootElt, "+swes:observableProperty", observable);
        
        // related features
        for (FeatureRef featureRef: request.getRelatedFeatures())
            dom.setElementValue(rootElt, "+swes:relatedFeature/swes:FeatureRelationship", featureRef.getHref());
        
        // sos metadata
        Element insertMetaElt = dom.addElement(rootElt, "swes:metadata/sos:SosInsertionMetadata");
        for (String obsType: request.getObservationTypes())
            dom.setElementValue(insertMetaElt, "+sos:observationType", obsType);
        for (String foiType: request.getFoiTypes())
            dom.setElementValue(insertMetaElt, "+sos:featureOfInterestType", foiType);
        
		return rootElt;
	}
}