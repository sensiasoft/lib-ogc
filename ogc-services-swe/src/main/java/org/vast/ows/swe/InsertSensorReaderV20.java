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

import net.opengis.sensorml.v20.AbstractProcess;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;
import org.vast.sensorML.SMLUtils;


/**
 * <p>
 * Provides methods to parse a SOAP/XML SWES InsertSensor
 * request and create a InsertSensorRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public abstract class InsertSensorReaderV20<RequestType extends InsertSensorRequest> extends SWERequestReader<RequestType>
{
    SMLUtils smlUtils = new SMLUtils(SMLUtils.V2_0);
    
    
    protected RequestType readXMLQuery(DOMHelper dom, Element requestElt, RequestType request, OWSExceptionReport report) throws OWSException
	{
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		String val;
				
        // procedureDescriptionFormat
        val = dom.getElementValue(requestElt, "procedureDescriptionFormat");
        request.setProcedureDescriptionFormat(val);
        if (request.getProcedureDescriptionFormat() == null)
        {
            report.add(new OWSException(OWSException.missing_param_code, "procedureDescriptionFormat"));
            report.process();
        }
        
        // check format is supported
        if (!val.equals(OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "2.0")))
            throw new OWSException(OWSException.invalid_param_code, "procedureDescription", "Unsupported format: " + val);
            
        // read SensorML procedure description
        try
        {            
            Element procedureElt = dom.getElement(requestElt, "procedureDescription/*");
            AbstractProcess process = smlUtils.readProcess(dom, procedureElt);
            request.setProcedureDescription(process);
        }
        catch (XMLReaderException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "procedureDescription", "Unable to read SensorML description:\n" + e.getMessage(), e);
        }
        
        // observable properties
        NodeList obsPropElts = dom.getElements(requestElt, "observableProperty");
        for (int i=0; i<obsPropElts.getLength(); i++)
        {
            String obsProp = dom.getElementValue((Element)obsPropElts.item(i));
            request.getObservableProperties().add(obsProp);
        }
        
        // related features
        NodeList featureElts = dom.getElements(requestElt, "relatedFeature/FeatureRelationship");
        for (int i=0; i<featureElts.getLength(); i++)
        {
            Element featElt = (Element)featureElts.item(i);
            
            FeatureRef featureRef = new FeatureRef();
            Element targetElt = dom.getElement(featElt, "target");
            XlinkUtils.readXlinkAttributes(dom, targetElt, featureRef);
            
            String role = dom.getElementValue(featElt, "role");
            if (role != null)
                featureRef.setArcRole(role);
            
            request.getRelatedFeatures().add(featureRef);
        }
        
        return request;
	}
}