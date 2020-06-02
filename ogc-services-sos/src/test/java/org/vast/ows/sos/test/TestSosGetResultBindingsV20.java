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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;
import net.opengis.fes.v20.GMLExpression;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.ows.OWSUtils;
import org.vast.ows.sos.GetResultRequest;
import org.vast.ows.sos.GetResultTemplateRequest;
import org.vast.ows.sos.GetResultTemplateResponse;
import org.vast.util.DateTimeFormat;
import org.xml.sax.InputSource;
import com.vividsolutions.jts.geom.Polygon;


public class TestSosGetResultBindingsV20 extends XMLTestCase
{
    
    public void setUp() throws Exception
    {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }
    
    
    public void testReadKvpGetResult() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResultKVP.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        
        String queryString;
        GetResultRequest request;
        
        queryString = r.readLine();
        request = (GetResultRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetResult", request.getOperation());
        assertEquals("urn:sensia:th1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/atmoThermo.owl#EffectiveTemperature", request.getObservables().iterator().next());
        assertEquals(1, request.getFoiIDs().size());
        assertEquals(0, request.getProcedures().size());
        assertEquals("urn:sensia:foi:b1", request.getFoiIDs().iterator().next());
        assertEquals("2009-01-10T10:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStartTime(), 0));
        assertEquals("2009-01-10T11:00:00Z", new DateTimeFormat().formatIso(request.getTime().getStopTime(), 0));
        assertEquals(22.32, request.getBbox().getMinX());
        assertEquals(11.2, request.getBbox().getMinY());
        assertEquals(32.32, request.getBbox().getMaxX());
        assertEquals(22.2, request.getBbox().getMaxY());
        assertEquals("urn:ogc:def:crs:EPSG::4326", request.getBbox().getCrs());
    }
    
    
    public void testReadKvpGetResultTemplate() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResultTemplateKVP.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        
        String queryString;
        GetResultTemplateRequest request;
        
        queryString = r.readLine();
        request = (GetResultTemplateRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetResultTemplate", request.getOperation());
        assertEquals("urn:sensia:barometer_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/atmoThermo.owl#AtmosphericPressure", request.getObservables().iterator().next());
    }
    
    
    public void testReadXmlGetResult() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is;
        GetResultRequest request;
        
        is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResult1.xml");        
        request = (GetResultRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetResult", request.getOperation());
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", request.getObservables().iterator().next());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(0, request.getProcedures().size());
        assertEquals("2008-03-01T17:44:15Z", new DateTimeFormat().formatIso(request.getTime().getBaseTime(), 0));
        assertTrue("BBOX is not null", request.getBbox().isNull());
        is.close();
        
        is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResult2.xml");        
        request = (GetResultRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetResult", request.getOperation());
        assertEquals("urn:nga:datasets:off1", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("urn:nga:video", request.getObservables().iterator().next());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(0, request.getProcedures().size());
        assertTrue("Temporal filter is not null", request.getTemporalFilter() == null);
        assertTrue("Spatial filter is not null", request.getSpatialFilter() == null);
        is.close();
        
        is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResult3.xml");        
        request = (GetResultRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetResult", request.getOperation());
        assertEquals("urn:nga:datasets:off1", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("urn:nga:video", request.getObservables().iterator().next());
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(0, request.getProcedures().size());
        assertTrue("Temporal filter is not null", request.getTemporalFilter() == null);
        Polygon poly = (Polygon)((GMLExpression)request.getSpatialFilter().getOperand2()).getGmlObject();
        assertEquals(52.90, poly.getCoordinates()[0].x);
        assertEquals(7.52, poly.getCoordinates()[0].y);
        assertEquals(52.92, poly.getCoordinates()[1].x);
        assertEquals(7.51, poly.getCoordinates()[1].y);
        assertEquals(52.96, poly.getCoordinates()[2].x);
        assertEquals(7.54, poly.getCoordinates()[2].y);
        assertEquals(52.90, poly.getCoordinates()[3].x);
        assertEquals(7.52, poly.getCoordinates()[3].y);
        is.close();
    }
    
    
    public void testReadXmlGetResultTemplate() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is;
        GetResultTemplateRequest request;
        
        is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResultTemplate1.xml");        
        request = (GetResultTemplateRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetResultTemplate", request.getOperation());
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", request.getObservables().iterator().next());
        is.close();
    }
    
    
    public void testWriteKvpGetResult() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResultKVP.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        
        String queryString;
        GetResultRequest request1, request2;
        
        while ((queryString = r.readLine()) != null)
        {
            request1 = (GetResultRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
            request1.setGetServer("");
            queryString = utils.buildURLQuery(request1).substring(1);
            System.out.println(queryString);
            request2 = (GetResultRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
            checkRequestsEquals(request1, request2);
        }
    }
    
    
    public void testWriteKvpGetResultTemplate() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosGetResultBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResultTemplateKVP.txt");
        BufferedReader r = new BufferedReader(new InputStreamReader(is));
        
        String queryString;
        GetResultTemplateRequest request1, request2;
        
        while ((queryString = r.readLine()) != null)
        {
            request1 = (GetResultTemplateRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
            request1.setGetServer("");
            queryString = utils.buildURLQuery(request1).substring(1);
            System.out.println(queryString);
            request2 = (GetResultTemplateRequest)utils.readURLQuery(queryString, OWSUtils.SOS);
            checkRequestsEquals(request1, request2);
        }
    }
    
    
    public void testWriteXmlGetResult() throws Exception
    {
        testWriteXmlGetResult("examples_v20/resultHandling/GetResult1.xml");
        testWriteXmlGetResult("examples_v20/resultHandling/GetResult2.xml");
        testWriteXmlGetResult("examples_v20/resultHandling/GetResult3.xml");
    }
    
    
    protected void testWriteXmlGetResult(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        GetResultRequest request1, request2;
        
        InputStream is = TestSosGetResultBindingsV20.class.getResourceAsStream(path);
        request1 = (GetResultRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLQuery(System.out, request1);
        utils.writeXMLQuery(os, request1);
        os.close();
        
        ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
        request2 = (GetResultRequest)utils.readXMLQuery(bis, OWSUtils.SOS);
        bis.close();
        
        checkRequestsEquals(request1, request2);
    }
    
    
    public void testWriteXmlGetResultTemplate() throws Exception
    {
        testWriteXmlGetResultTemplate("examples_v20/resultHandling/GetResultTemplate1.xml");
    }
    
    
    protected void testWriteXmlGetResultTemplate(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        GetResultTemplateRequest request1, request2;
        
        InputStream is = getClass().getResourceAsStream(path);
        request1 = (GetResultTemplateRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLQuery(System.out, request1);
        utils.writeXMLQuery(os, request1);
        os.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
        request2 = (GetResultTemplateRequest)utils.readXMLQuery(bis, OWSUtils.SOS);
        bis.close();
        
        checkRequestsEquals(request1, request2);
    }
    
    
    protected void checkRequestsEquals(GetResultRequest request1, GetResultRequest request2)
    {
        assertEquals(request1.getService(), request2.getService());
        assertEquals(request1.getOperation(), request2.getOperation());
        assertEquals(request1.getVersion(), request2.getVersion());
        assertEquals(request1.getOffering(), request2.getOffering());
        checkSetsEquals(request1.getProcedures(), request2.getProcedures());
        checkSetsEquals(request1.getObservables(), request2.getObservables());
        checkSetsEquals(request1.getFoiIDs(), request2.getFoiIDs());
        assertEquals(request1.getTemporalFilter(), request2.getTemporalFilter());
        assertEquals(request1.getSpatialFilter(), request2.getSpatialFilter());
        assertEquals(request1.getFormat(), request2.getFormat());
    }
    
    
    protected void checkRequestsEquals(GetResultTemplateRequest request1, GetResultTemplateRequest request2)
    {
        assertEquals(request1.getService(), request2.getService());
        assertEquals(request1.getOperation(), request2.getOperation());
        assertEquals(request1.getVersion(), request2.getVersion());
        assertEquals(request1.getOffering(), request2.getOffering());
        checkSetsEquals(request1.getObservables(), request2.getObservables());
        assertEquals(request1.getFormat(), request2.getFormat());
    }
    
    
    protected void checkSetsEquals(Set<String> set1, Set<String> set2)
    {
        assertTrue("Sets are not of same size", set1.size() == set2.size());
        assertEquals("Sets don't contain the same elements", set1, set2);
    }
    
    
    public void testReadWriteXmlGetResultTemplateResponse() throws Exception
    {
        testReadWriteXmlGetResultTemplateResponse("examples_v20/resultHandling/GetResultTemplate1_response.xml");
    }
    
    
    protected void testReadWriteXmlGetResultTemplateResponse(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        
        InputStream is = getClass().getResourceAsStream(path);
        GetResultTemplateResponse resp = (GetResultTemplateResponse)utils.readXMLResponse(is, OWSUtils.SOS, "GetResultTemplateResponse");
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLResponse(System.out, resp);
        utils.writeXMLResponse(os, resp);
        os.close();
        
        // compare with original
        InputSource src1 = new InputSource(getClass().getResourceAsStream(path));
        InputSource src2 = new InputSource(new ByteArrayInputStream(os.toByteArray()));
        assertXMLEqual(src1, src2);
    }
}
