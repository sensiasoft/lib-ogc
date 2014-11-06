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

package org.vast.ows.sos.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.ows.sos.SOSOfferingCapabilities;
import org.vast.ows.sos.SOSServiceCapabilities;
import org.vast.util.Bbox;
import org.vast.util.DateTimeFormat;
import org.vast.util.TimeExtent;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import junit.framework.TestCase;


public class TestSosGetCapsBindingsV20 extends TestCase
{

    public void testReadCapabilitiesXml() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is;
        SOSServiceCapabilities caps;

        // example 1
        is = TestSosGetCapsBindingsV20.class.getResourceAsStream("examples_v20/core/GetCapabilities1_response.xml");
        caps = (SOSServiceCapabilities) utils.readXMLResponse(is, OWSUtils.SOS, "Capabilities");

        // identification section
        assertEquals("SOS", caps.getService());
        assertEquals("2.0.0", caps.getVersion());
        assertEquals("Capabilities", caps.getMessageType());

        assertEquals("My SOS", caps.getIdentification().getTitle());
        assertEquals("My SOS serves water observations", caps.getIdentification().getDescription());

        assertEquals(2, caps.getIdentification().getKeywords().size());
        assertEquals("water level", caps.getIdentification().getKeywords().get(0));
        assertEquals("gauge height", caps.getIdentification().getKeywords().get(1));

        assertEquals(15, caps.getProfiles().size());

        assertEquals("NONE", caps.getFees());
        assertEquals("NONE", caps.getAccessConstraints());

        // service provider section
        assertEquals("My Provider", caps.getServiceProvider().getOrganizationName());
        assertEquals("http://my.organization.org/sos", caps.getServiceProvider().getWebsite());
        assertEquals("+49.123.456 789- 0", caps.getServiceProvider().getVoiceNumber());
        assertEquals("+49.123.456 789- 0", caps.getServiceProvider().getFaxNumber());
        assertEquals("My Street 24", caps.getServiceProvider().getDeliveryPoint());
        assertEquals("MyCity", caps.getServiceProvider().getCity());
        assertEquals("12345", caps.getServiceProvider().getPostalCode());
        assertEquals("MyCountry", caps.getServiceProvider().getCountry());
        assertEquals("info@my.organization.org", caps.getServiceProvider().getEmail());

        // filter capabilities

        // contents section        
        assertEquals(1, caps.getLayers().size());
        assertTrue(caps.getLayers().get(0) instanceof SOSOfferingCapabilities);
        SOSOfferingCapabilities offeringCaps = (SOSOfferingCapabilities) caps.getLayers().get(0);
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", offeringCaps.getIdentifier());
        assertEquals("http://www.my_namespace.org/sensors/Water_Gage_1", offeringCaps.getProcedures().get(0));
        assertEquals("http://www.opengis.net/sensorML/1.0.1", offeringCaps.getProcedureFormats().get(0));
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", offeringCaps.getObservableProperties().get(0));
        assertEquals("http://wfs.example.org?request=getFeature&featureid=Rhine_Sandbank_123", offeringCaps.getRelatedFeatures().get(0));
        offeringCaps.getObservedAreas().get(0).equals(new Bbox(50.7167, 7.76667, 53.7167, 9.76667, "http://www.opengis.net/def/crs/EPSG/0/4326"));
        offeringCaps.getPhenomenonTimes().get(0).equals(new TimeExtent(DateTimeFormat.parseIso("2009-01-11T16:22:25.00Z"), DateTimeFormat.parseIso("2010-08-21T08:32:10.00Z")));
        offeringCaps.getResponseFormats().get(0).equals("http://www.opengis.net/om/2.0");
        offeringCaps.getObservationTypes().get(0).equals("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");

        is.close();

        // airbase example
        is = TestSosGetCapsBindingsV20.class.getResourceAsStream("examples_v20/_useCase_airbase_station_network/GetCapabilities_response.xml");
        caps = (SOSServiceCapabilities) utils.readXMLResponse(is, OWSUtils.SOS, "Capabilities");

        // contents section
        assertEquals(4, caps.getLayers().size());
        for (OWSLayerCapabilities layerCaps : caps.getLayers())
        {
            offeringCaps = (SOSOfferingCapabilities) layerCaps;

            assertEquals(6, offeringCaps.getRelatedFeatures().size());
            assertEquals("http://myServer.org/features/Germany", offeringCaps.getRelatedFeatures().get(0));
            assertEquals("http://myServer.org/features/Netherlands", offeringCaps.getRelatedFeatures().get(1));
            assertEquals("http://myServer.org/features/Raansdonkveer", offeringCaps.getRelatedFeatures().get(2));
            assertEquals("http://myServer.org/features/Marknesse", offeringCaps.getRelatedFeatures().get(3));
            assertEquals("http://myServer.org/features/HoheMark", offeringCaps.getRelatedFeatures().get(4));
            assertEquals("http://myServer.org/features/Moersbach", offeringCaps.getRelatedFeatures().get(5));

            assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[NH3]", offeringCaps.getObservableProperties().get(0));
            assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[CO]", offeringCaps.getObservableProperties().get(1));
            assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[H2S]", offeringCaps.getObservableProperties().get(2));
            assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[SO2]", offeringCaps.getObservableProperties().get(3));
        }

        is.close();
    }


    public void testWriteAndReadBackCapabilitiesXml() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is;
        SOSServiceCapabilities caps1, caps2;

        is = TestSosGetCapsBindingsV20.class.getResourceAsStream("examples_v20/_useCase_homogeneous_sensor_network/GetCapabilities_response_homogeneous_sensor_network.xml");
        caps1 = (SOSServiceCapabilities) utils.readXMLResponse(is, OWSUtils.SOS, "Capabilities");

        // write to byte array
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLResponse(System.out, caps1);
        utils.writeXMLResponse(os, caps1);
        os.close();

        // read back
        ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
        caps2 = (SOSServiceCapabilities) utils.readXMLResponse(bis, OWSUtils.SOS, "Capabilities");
        bis.close();
        bis.reset();

        // validate what we read against online schema (specified in example)
        /*Map<String, String> schemaLocations = new HashMap<String, String>();
        schemaLocations.put("http://www.opengis.net/sos/2.0", "http://schemas.opengis.net/sos/2.0/sos.xsd");
        DOMHelper validator = new DOMHelper(bis, true, schemaLocations);*/

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://xml.org/sax/features/namespaces", true);
        factory.setFeature("http://xml.org/sax/features/validation", true);
        factory.setFeature("http://apache.org/xml/features/validation/schema", true);
        factory.setAttribute("http://apache.org/xml/properties/schema/external-schemaLocation", "http://www.opengis.net/sos/2.0 http://schemas.opengis.net/sos/2.0/sos.xsd");
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");

        DocumentBuilder builder = factory.newDocumentBuilder();
        final StringBuilder errors = new StringBuilder();
        builder.setErrorHandler(new ErrorHandler(){

            @Override
            public void error(SAXParseException e) throws SAXException
            {
                errors.append("Line " + e.getLineNumber() + ": " + e.getMessage());
                errors.append('\n');
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXException
            {
                errors.append("Line " + e.getLineNumber() + ": " + e.getMessage());
                errors.append('\n');                
            }

            @Override
            public void warning(SAXParseException e) throws SAXException
            {
                errors.append("Line " + e.getLineNumber() + ": " + e.getMessage());
                errors.append('\n');                
            }
            
        });
        
        //builder.parse(bis);
        if (errors.length() > 0)
            throw new Exception("Validation errors:\n" + errors.toString());
    }


    protected void checkListsEquals(List<String> list1, List<String> list2)
    {
        assertTrue("List are not of same size", list1.size() == list2.size());

        for (int i = 0; i < list1.size(); i++)
            assertEquals(list1.get(i), list2.get(i));
    }
}
