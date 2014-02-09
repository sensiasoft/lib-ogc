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

import java.util.Map;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ogc.xlink.XlinkUtils;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;
import org.vast.sensorML.SMLProcess;
import org.vast.sensorML.SMLReader;
import org.vast.sensorML.SMLUtils;
import org.vast.sensorML.SystemReaderV1;
import org.vast.sensorML.SystemReaderV20;


/**
 * <p><b>Title:</b><br/>
 * SOS InsertSensor Request Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertSensor
 * request and create a InsertSensorRequest object for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin
 * @date Feb 2, 2014
 * @version 1.0
 */
public class InsertSensorReaderV20 extends SWERequestReader<InsertSensorRequest>
{
    
    public InsertSensorReaderV20()
	{       
	}


    @Override
    public InsertSensorRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SOSException(noKVP + "SOS 2.0 InsertSensor");
    }
    
    
	@Override
	public InsertSensorRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertSensorRequest request = new InsertSensorRequest();
		
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
        
        // create reader depending on format
        SMLReader procedureReader;
        if (val.startsWith((OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "1.0"))))
            procedureReader = new SystemReaderV1();
        else if (val.equals(OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "2.0")))
            procedureReader = new SystemReaderV20();
        else
            throw new OWSException(OWSException.invalid_param_code, "procedureDescription", "Unsupported format: " + val);
            
        // procedure description
        try
        {
            Element procedureElt = dom.getElement(requestElt, "procedureDescription/*");
            SMLProcess process = procedureReader.read(dom, procedureElt);
            request.setProcedureDescription(process);
        }
        catch (XMLReaderException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "procedure", "Unable to read SensorML description:\n" + e.getMessage());
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
            XlinkUtils.readXlinkAttributes(targetElt, featureRef);
            
            String role = dom.getElementValue(featElt, "role");
            if (role != null)
                featureRef.setArcRole(role);
            
            request.getRelatedFeatures().add(featureRef);
        }
        
        // insertion metadata
        Element metadataElt = dom.getElement(requestElt, "metadata/SosInsertionMetadata");
        if (metadataElt != null)
        {
            NodeList obsTypeElts = dom.getElements(metadataElt, "observationType");
            for (int i=0; i<obsTypeElts.getLength(); i++)
            {
                val = dom.getElementValue((Element)obsTypeElts.item(i));
                request.getObservationTypes().add(val);
            }
            
            NodeList foiTypeElts = dom.getElements(metadataElt, "featureOfInterestType");
            for (int i=0; i<foiTypeElts.getLength(); i++)
            {
                val = dom.getElementValue((Element)foiTypeElts.item(i));
                request.getFoiTypes().add(val);
            }
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    /**
     * Checks that InsertSensor mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(InsertSensorRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);
        
        // need observation type
        if (request.getObservationTypes().isEmpty())
            report.add(new OWSException(OWSException.missing_param_code, "observationType"));
        
        // need foi type
        if (request.getFoiTypes().isEmpty())
            report.add(new OWSException(OWSException.missing_param_code, "featureOfInterestType"));
        
        // procedure
        if (request.getProcedureDescription() == null)
            report.add(new OWSException(OWSException.missing_param_code, "procedure"));
        
        report.process();
    }

}