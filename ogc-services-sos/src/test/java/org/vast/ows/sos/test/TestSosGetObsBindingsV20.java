/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;
import net.opengis.fes.v20.GMLExpression;
import org.vast.ows.OWSUtils;
import org.vast.ows.sos.GetObservationRequest;
import org.vast.ows.test.OWSTestCase;
import org.vast.util.Bbox;
import org.vast.util.DateTimeFormat;
import com.vividsolutions.jts.geom.Polygon;


public class TestSosGetObsBindingsV20 extends OWSTestCase
{
    protected static String DEFAULT_FORMAT = "http://www.opengis.net/om/2.0";
    
    
    public void testReadKvpGetObs() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/core/GetObservationKVP.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        
        String queryString;
        GetObservationRequest request;
        
        queryString = r.readLine();
        request = (GetObservationRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(1, request.getOfferings().size());
        assertEquals("http://www.my_namespace.org/thermometer1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/atmoThermo.owl#EffectiveTemperature", request.getObservables().iterator().next());
        assertEquals(1, request.getFoiIDs().size());
        assertEquals("http://wfs.example.org?request=getFeature&featureid=building1", request.getFoiIDs().iterator().next());
        assertEquals(1, request.getProcedures().size());
        assertEquals("http://www.my_namespace.org/sensors/thermometer1", request.getProcedures().iterator().next());
        assertEquals("2009-01-10T10:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStartTime(), 0));
        assertEquals("2009-01-10T11:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStopTime(), 0));
        assertEquals(22.32, request.getBbox().getMinX());
        assertEquals(11.2, request.getBbox().getMinY());
        assertEquals(32.32, request.getBbox().getMaxX());
        assertEquals(22.2, request.getBbox().getMaxY());
        assertEquals("urn:ogc:def:crs:EPSG::4326", request.getBbox().getCrs());
        assertEquals(null, request.getFormat());
        
        queryString = r.readLine();
        request = (GetObservationRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(1, request.getOfferings().size());
        assertEquals("http://www.my_namespace.org/thermometer2_observations", request.getOffering());
        assertEquals(0, request.getObservables().size());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(2, request.getProcedures().size());
        assertTrue("Time is not null", request.getTime().isNull());
        assertTrue("BBOX is not null", request.getBbox().isNull());
        Iterator<String> proceduresIt = request.getProcedures().iterator();
        assertEquals("http://myserver.org/sensors/th1", proceduresIt.next());
        assertEquals("http://myserver.org/sensors/th2", proceduresIt.next());
        assertEquals("http://www.opengis.net/om/1.0", request.getFormat());
        
        r.close();
    }
    
    public void testReadXmlGetObs() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is;
        GetObservationRequest request;
        
        is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/core/GetObservation1_obsProps.xml");        
        request = (GetObservationRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(1, request.getOfferings().size());
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", request.getObservables().iterator().next());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(0, request.getProcedures().size());
        assertEquals("2008-03-01T17:44:15Z", new DateTimeFormat().formatIso(request.getTime().getBaseTime(), 0));
        assertTrue("BBOX is not null", request.getBbox().isNull());
        assertEquals(null, request.getFormat());
        is.close();
        
        is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/core/GetObservation2_obsProps_Procedure.xml");        
        request = (GetObservationRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(1, request.getOfferings().size());
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", request.getObservables().iterator().next());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(1, request.getProcedures().size());
        assertEquals("http://www.my_namespace.org/sensors/Water_Gage_1", request.getProcedures().iterator().next());
        assertTrue("Time is not null", request.getTime().isNull());
        assertTrue("BBOX is not null", request.getBbox().isNull());
        assertEquals(null, request.getFormat());
        is.close();
        
        is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/core/GetObservation3_foiIDFilter.xml");        
        request = (GetObservationRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(1, request.getOfferings().size());
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", request.getObservables().iterator().next());
        assertEquals(1, request.getFoiIDs().size());
        assertEquals("http://wfs.example.org?request=getFeature&featureid=\"river1\"", request.getFoiIDs().iterator().next());
        assertEquals(1, request.getProcedures().size());
        assertEquals("http://www.my_namespace.org/sensors/Water_Gage_1", request.getProcedures().iterator().next());
        assertTrue("Time is not null", request.getTime().isNull());
        assertTrue("BBOX is not null", request.getBbox().isNull());
        assertEquals(null, request.getFormat());
        is.close();
        
        is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/core/GetObservation4_spatialFilter.xml");        
        request = (GetObservationRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(1, request.getOfferings().size());
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", request.getObservables().iterator().next());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(1, request.getProcedures().size());
        assertEquals("http://www.my_namespace.org/sensors/Water_Gage_1", request.getProcedures().iterator().next());
        assertTrue("Time is not null", request.getTime().isNull());
        Polygon poly = (Polygon)((GMLExpression)request.getSpatialFilter().getOperand2()).getGmlObject();
        assertEquals(52.90, poly.getCoordinates()[0].x);
        assertEquals(7.52, poly.getCoordinates()[0].y);
        assertEquals(52.92, poly.getCoordinates()[1].x);
        assertEquals(7.51, poly.getCoordinates()[1].y);
        assertEquals(52.96, poly.getCoordinates()[2].x);
        assertEquals(7.54, poly.getCoordinates()[2].y);
        assertEquals(52.90, poly.getCoordinates()[3].x);
        assertEquals(7.52, poly.getCoordinates()[3].y);        
        assertEquals(null, request.getFormat());
        is.close();
        
        is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/_useCase_airbase_station_network/GetObservation.xml");        
        request = (GetObservationRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(0, request.getOfferings().size());
        assertEquals(2, request.getObservables().size());
        Iterator<String> observablesIt = request.getObservables().iterator();
        assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[NH3]", observablesIt.next());
        assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[CO]", observablesIt.next());
        assertEquals(2, request.getFoiIDs().size());
        assertEquals("http://myServer.org/features/SamplingPointAtMoersbach", request.getFoiIDs().iterator().next());
        assertEquals("http://myServer.org/features/SamplingPointAtMoersbach", request.getFoiIDs().iterator().next());
        assertEquals(0, request.getProcedures().size());
        assertEquals("2008-01-01T00:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStartTime(), 0));
        assertEquals("2011-05-01T17:44:15Z", new DateTimeFormat().formatIso(request.getTime().getStopTime(), 0));
        assertTrue("BBOX is not null", request.getBbox().isNull());
        assertEquals(null, request.getFormat());
        is.close();
        
        is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/_useCase_mobile_sensors/GetObservation.xml");        
        request = (GetObservationRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(0, request.getOfferings().size());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://marinemetadata.org/cf#sea_water_salinity", request.getObservables().iterator().next());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(1, request.getProcedures().size());
        assertEquals("http://myServer.org/sensors/glider1", request.getProcedures().iterator().next());
        assertEquals("2008-01-01T00:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStartTime(), 0));
        assertEquals("2008-01-02T00:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStopTime(), 0));
        Bbox bbox = request.getBbox();
        assertEquals( 21.0, bbox.getMinX());
        assertEquals(-94.0, bbox.getMinY());
        assertEquals( 22.0, bbox.getMaxX());
        assertEquals(-90.0, bbox.getMaxY());
        assertEquals(null, request.getFormat());
        is.close();
        
        is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/_useCase_homogeneous_sensor_network/GetObservation.xml");        
        request = (GetObservationRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetObservation", request.getOperation());
        assertEquals(0, request.getOfferings().size());
        assertEquals(4, request.getObservables().size());
        observablesIt = request.getObservables().iterator();
        assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[NH3]", observablesIt.next());
        assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[CO]", observablesIt.next());
        assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[H2S]", observablesIt.next());
        assertEquals("http://giv-genesis.uni-muenster.de:8080/SOR/REST/phenomenon/OGC/Concentration[SO2]", observablesIt.next());
        assertEquals(1, request.getFoiIDs().size());
        assertEquals("http://myServer.org/features/SamplingPointAtMoersbach", request.getFoiIDs().iterator().next());
        assertEquals(0, request.getProcedures().size());
        assertEquals("2008-01-01T00:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStartTime(), 0));
        assertEquals("2011-05-01T17:44:15Z", new DateTimeFormat().formatIso(request.getTime().getStopTime(), 0));
        assertTrue("BBOX is not null", request.getBbox().isNull());
        assertEquals(null, request.getFormat());
        is.close();
    }
    
    
    public void testReadWriteXmlGetObs() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/core/GetObservation1_obsProps.xml");
        readWriteCompareXmlRequest("examples_v20/core/GetObservation2_obsProps_Procedure.xml");
        readWriteCompareXmlRequest("examples_v20/core/GetObservation3_foiIDFilter.xml");
        readWriteCompareXmlRequest("examples_v20/core/GetObservation4_spatialFilter.xml");
        readWriteCompareXmlRequest("examples_v20/_useCase_airbase_station_network/GetObservation.xml");
        //readWriteCompareXmlRequest("examples_v20/_useCase_mobile_sensors/GetObservation.xml");
        readWriteCompareXmlRequest("examples_v20/_useCase_homogeneous_sensor_network/GetObservation.xml");
        readWriteCompareXmlRequest("examples_v20/spatialFilteringProfile/GetObservation1_spatialFilteringProfile.xml");
    }
    
    
    public void testWriteKvpGetObs() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosGetObsBindingsV20.class.getResourceAsStream("examples_v20/core/GetObservationKVP.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        
        String queryString;
        GetObservationRequest request1, request2;
        
        while ((queryString = r.readLine()) != null)
        {
            request1 = (GetObservationRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
            request1.setGetServer("");
            queryString = utils.buildURLQuery(request1).substring(1);
            System.out.println(queryString);
            request2 = (GetObservationRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
            checkRequestsEquals(request1, request2);
        }
    }
   
    
    protected void checkRequestsEquals(GetObservationRequest request1, GetObservationRequest request2)
    {
        assertEquals(request1.getService(), request2.getService());
        assertEquals(request1.getOperation(), request2.getOperation());
        assertEquals(request1.getVersion(), request2.getVersion());
        checkSetsEquals(request1.getProcedures(), request2.getProcedures());
        checkSetsEquals(request1.getOfferings(), request2.getOfferings());
        checkSetsEquals(request1.getObservables(), request2.getObservables());
        checkSetsEquals(request1.getFoiIDs(), request2.getFoiIDs());
        assertEquals(request1.getTemporalFilter(), request2.getTemporalFilter());
        assertEquals(request1.getSpatialFilter(), request2.getSpatialFilter());
        assertEquals(request1.getFormat(), request2.getFormat());
    }
    
    
    protected void checkSetsEquals(Set<String> set1, Set<String> set2)
    {
        assertTrue("Sets are not of same size", set1.size() == set2.size());
        assertEquals("Sets don't contain the same elements", set1, set2);
    }
}
