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

package org.vast.test.ows.sos;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.vast.ows.OWSUtils;
import org.vast.ows.sos.GetResultRequest;
import org.vast.util.DateTimeFormat;
import junit.framework.TestCase;


public class TestSosDescribeBindingsV20 extends TestCase
{
    
    public void testReadKvpGetResultV20() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosDescribeBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResultKVP.txt");
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
        assertEquals("http://sweet.jpl.nasa.gov/2.0/atmoThermo.owl#EffectiveTemperature", request.getObservables().get(0));
        assertEquals(1, request.getFoiIDs().size());
        assertEquals(0, request.getProcedures().size());
        assertEquals("urn:sensia:foi:b1", request.getFoiIDs().get(0));
        assertEquals("2009-01-10T10:00:00Z", DateTimeFormat.formatIso(request.getTime().getStartTime(), 0));
        assertEquals("2009-01-10T11:00:00Z", DateTimeFormat.formatIso(request.getTime().getStopTime(), 0));
        assertEquals(22.32, request.getBbox().getMinX());
        assertEquals(11.2, request.getBbox().getMinY());
        assertEquals(32.32, request.getBbox().getMaxX());
        assertEquals(22.2, request.getBbox().getMaxY());
        assertEquals("urn:ogc:def:crs:EPSG::4326", request.getBbox().getCrs());
    }
    
    
    public void testReadXmlGetResultV20() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is;
        GetResultRequest request;
        
        is = TestSosDescribeBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResult1.xml");        
        request = (GetResultRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("GetResult", request.getOperation());
        assertEquals("http://www.my_namespace.org/water_gage_1_observations", request.getOffering());
        assertEquals(1, request.getObservables().size());
        assertEquals("http://sweet.jpl.nasa.gov/2.0/hydroSurface.owl#WaterHeight", request.getObservables().get(0));
        assertEquals(0, request.getFoiIDs().size());
        assertEquals(0, request.getProcedures().size());
        assertEquals("2008-03-01T17:44:15Z", DateTimeFormat.formatIso(request.getTime().getBaseTime(), 0));
        assertTrue("BBOX is not null", request.getBbox().isNull());
        is.close();
    }
    
    
    public void testWriteKvpGetResultV20() throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosDescribeBindingsV20.class.getResourceAsStream("examples_v20/resultHandling/GetResultKVP.txt");
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
    
    
    public void testWriteXmlGetObsV20() throws Exception
    {
        testWriteXmlGetResultV20("examples_v20/resultHandling/GetResult1.xml");
        testWriteXmlGetResultV20("examples_v20/resultHandling/GetResult2.xml");
    }
    
    
    protected void testWriteXmlGetResultV20(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        GetResultRequest request1, request2;
        
        InputStream is = TestSosDescribeBindingsV20.class.getResourceAsStream(path);
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
    
    
    protected void checkRequestsEquals(GetResultRequest request1, GetResultRequest request2)
    {
        assertEquals(request1.getService(), request2.getService());
        assertEquals(request1.getOperation(), request2.getOperation());
        assertEquals(request1.getVersion(), request2.getVersion());
        assertEquals(request1.getOffering(), request2.getOffering());
        checkListsEquals(request1.getProcedures(), request2.getProcedures());
        checkListsEquals(request1.getObservables(), request2.getObservables());
        checkListsEquals(request1.getFoiIDs(), request2.getFoiIDs());
        assertEquals(request1.getTemporalFilter(), request2.getTemporalFilter());
        assertEquals(request1.getSpatialFilter(), request2.getSpatialFilter());
        assertEquals(request1.getFormat(), request2.getFormat());
    }
    
    
    protected void checkListsEquals(List<String> list1, List<String> list2)
    {
        assertTrue("List are not of same size", list1.size() == list2.size());
        
        for (int i = 0; i < list1.size(); i++)
            assertEquals(list1.get(i), list2.get(i));
    }
}
