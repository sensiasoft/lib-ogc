/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.DescribedObject;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.json.JsonInliningWriter;
import org.vast.sensorML.SMLJsonBindings;
import org.vast.sensorML.SMLUtils;
import org.vast.swe.SWEUtils;
import org.xml.sax.InputSource;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;


public class TestSMLJsonBindingsV30 extends XMLTestCase
{

    @Override
    public void setUp() throws Exception
    {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }
    
    
    protected void writeJsonToStream(DescribedObject process, OutputStream os, boolean indent) throws Exception
    {
        var writer = new JsonInliningWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        
        var smlBindings = new SMLJsonBindings();
        smlBindings.writeDescribedObject(writer, process);
        
        writer.flush();
    }
    
    
    protected void readWriteCompareXmlJson(String path) throws Exception
    {
        try
        {
            SMLUtils smlUtils = new SMLUtils(SMLUtils.V2_0);
                    
            // read from XML file
            InputStream is = getClass().getResourceAsStream(path);
            AbstractProcess smlObj = smlUtils.readProcess(is);
            is.close();
            
            // write back as JSON to stdout and buffer
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeJsonToStream(smlObj, System.out, true);
            writeJsonToStream(smlObj, os, false);
            System.out.println('\n');
            
            // read back JSON, write as XML and compare with source XML
            is = new ByteArrayInputStream(os.toByteArray());
            SMLJsonBindings smlBindings = new SMLJsonBindings();
            var jsonReader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
            
            AbstractProcess proc = smlBindings.readAbstractProcess(jsonReader);
            new SMLUtils(SWEUtils.V2_0).writeProcess(xmlOutput, proc, true);
            new SMLUtils(SWEUtils.V2_0).writeProcess(System.out, proc, true);
            System.out.println('\n');
            
            InputSource src1 = new InputSource(getClass().getResourceAsStream(path));
            InputSource src2 = new InputSource(new ByteArrayInputStream(xmlOutput.toByteArray()));
            assertXMLEqual(src1, src2);
        }
        catch (Throwable e)
        {
            throw new Exception("Failed test " + path, e);
        }
    }
    
    
    protected void readWriteCompareJson(String path, boolean enforceTypeFirst) throws Exception
    {
        try
        {
            // read from JSON file
            InputStream is = getClass().getResourceAsStream(path);
            var reader = new JsonReader(new InputStreamReader(is));
            SMLJsonBindings smlBindings = new SMLJsonBindings(enforceTypeFirst);
            var smlObj = smlBindings.readDescribedObject(reader);
            
            // write back as JSON to stdout and buffer
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeJsonToStream(smlObj, System.out, true);
            writeJsonToStream(smlObj, os, false);
            System.out.println('\n');
            
            // compare with source JSON
            var json1 = JsonParser.parseReader(new InputStreamReader(getClass().getResourceAsStream(path)));
            var json2 = JsonParser.parseReader(new InputStreamReader(new ByteArrayInputStream(os.toByteArray())));
            assertEquals(json1, json2);
        }
        catch (Throwable e)
        {
            throw new Exception("Failed test " + path, e);
        }
    }
    
    
    public void testReadWriteSimpleSensor() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/SimpleSensor.xml");
    }
    
    
    public void testReadWriteGammaSensor() throws Exception
    {
        //readWriteCompareProcessXml("examples_v20/gamma2070.xml");
        readWriteCompareXmlJson("examples_v20/gamma2070_more.xml");
    }
    
    
    public void testReadWriteCameraSensor() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/KCM-HD_Camera_inline.xml");
    }
    
    
    public void testReadWriteConfiguredCameraSensor() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/KCM-HD Camera.xml");
    }
    
    
    public void testReadWriteSensorWithModes() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/SensorWithModes.xml");
    } 
    
    
    public void testReadWriteDerivedInstance() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/OwnerInstance.xml");
    }
    
    
    public void testReadWriteDavisSensor() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/Davis_7817_complete.xml");
    } 
    
    
    public void testReadWriteWeatherStation() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/WeatherStation.xml");
    } 
    
    
    public void testReadWriteModeInstance() throws Exception
    {
        readWriteCompareXmlJson("examples_v20/ModeInstance.xml");
    }
    
    
    /*public void testReadWriteSensorwithDataStreamOutput() throws Exception
    {
        readWriteCompareProcessJson("examples_v20/SimpleStreaming RS232.xml");
    }*/
    
    
    public void testReadWriteSystem() throws Exception
    {
        readWriteCompareJson("json/weather_station_system.json", true);
    }
    
    
    public void testReadWriteSystemWithTypeNotFirst() throws Exception
    {
        readWriteCompareJson("json/weather_station_system_typemoved.json", false);
    }
    
    
    public void testReadWriteSystemWithGeopose() throws Exception
    {
        readWriteCompareJson("json/sensor_instance_with_geopose_quat.json", true);
        readWriteCompareJson("json/sensor_instance_with_geopose_ypr.json", true);
        
        
    }
    
    
    public void testReadWriteSystemWithRelativePose() throws Exception
    {
        readWriteCompareJson("json/sensor_instance_with_relpose_quat.json", true);
        readWriteCompareJson("json/sensor_instance_with_relpose_ypr.json", true);
        
        
    }
    
    
    public void testReadWriteDeployment() throws Exception
    {
        readWriteCompareJson("json/deployment_with_geometry.json", true);
        
        
    }
}
