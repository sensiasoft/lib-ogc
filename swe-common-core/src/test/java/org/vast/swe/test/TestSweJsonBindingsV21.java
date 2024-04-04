/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import net.opengis.swe.v20.AbstractSWE;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.Time;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.data.SWEFactory;
import org.vast.json.JsonInliningWriter;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEJsonBindings;
import org.vast.swe.SWEStaxBindings;
import org.vast.swe.SWEUtils;
import org.xml.sax.InputSource;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;


public class TestSweJsonBindingsV21 extends XMLTestCase
{
    
    @Override
    public void setUp() throws Exception
    {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
    }
    
    
    protected AbstractSWE readSweCommonXml(String path, boolean isDataStream) throws Exception
    {
        SWEStaxBindings sweHelper = new SWEStaxBindings();
        
        // read from file
        InputStream is = getClass().getResourceAsStream(path);
        XMLInputFactory input = new com.ctc.wstx.stax.WstxInputFactory();
        XMLStreamReader reader = input.createXMLStreamReader(is);
        reader.nextTag();
        
        AbstractSWE sweObj;
        if (isDataStream)
            sweObj = sweHelper.readDataStream(reader);
        else
            sweObj = sweHelper.readDataComponent(reader);
        is.close();
        
        return sweObj;
    }
    
    
    protected AbstractSWE readSweCommonJson(String path, boolean isDataStream) throws Exception
    {
        SWEJsonBindings sweHelper = new SWEJsonBindings();
        
        // read from file
        InputStream is = getClass().getResourceAsStream(path);
        var jsonReader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        
        AbstractSWE sweObj;
        if (isDataStream)
            sweObj = sweHelper.readDataStream(jsonReader);
        else
            sweObj = sweHelper.readDataComponent(jsonReader);
        is.close();
        
        return sweObj;
    }
    
    
    protected void writeSweCommonJsonToStream(AbstractSWE sweObj, OutputStream os, boolean indent) throws Exception
    {
        var writer = new JsonInliningWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.setIndent("  ");
        
        SWEJsonBindings sweBindings = new SWEJsonBindings(new SWEFactory());
        if (sweObj instanceof DataStream)
            sweBindings.writeDataStream(writer, (DataStream)sweObj);
        else
            sweBindings.writeDataComponent(writer, (DataComponent)sweObj, true);
        
        writer.flush();
    }
    
    
    protected void readXmlWriteJson(String path) throws Exception
    {
        readXmlWriteJson(path, false);
    }
    
    
    protected void readXmlWriteJson(String path, boolean isDataStream) throws Exception
    {
        try
        {
            // read from XML file
            AbstractSWE sweObj = readSweCommonXml(path, isDataStream);
            
            // write back as JSON to stdout and buffer
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeSweCommonJsonToStream(sweObj, System.out, true);
            writeSweCommonJsonToStream(sweObj, os, false);
            System.out.println('\n');
            
            // read back JSON, write as XML and compare with source XML
            var is = new ByteArrayInputStream(os.toByteArray());
            SWEJsonBindings sweBindings = new SWEJsonBindings(new SWEFactory());
            var jsonReader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            ByteArrayOutputStream xmlOutput = new ByteArrayOutputStream();
            
            if (sweObj instanceof DataStream)
            {
                DataStream ds = sweBindings.readDataStream(jsonReader);
                new SWEUtils(SWEUtils.V2_0).writeDataStream(xmlOutput, ds, true);
                new SWEUtils(SWEUtils.V2_0).writeDataStream(System.out, ds, true);
            }
            else
            {
                DataComponent comp = sweBindings.readDataComponent(jsonReader);
                new SWEUtils(SWEUtils.V2_0).writeComponent(xmlOutput, comp, true, true);
                new SWEUtils(SWEUtils.V2_0).writeComponent(System.out, comp, true, true);
            }
            
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
    
    
    protected void readWriteJson(String path, boolean isDataStream) throws Exception
    {
        try
        {
            // read JSON
            var sweObj = readSweCommonJson(path, isDataStream);
            
            // write as JSON to stdout and buffer
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writeSweCommonJsonToStream(sweObj, System.out, true);
            writeSweCommonJsonToStream(sweObj, os, false);
            System.out.println('\n');
            
            var json1 = JsonParser.parseReader(new InputStreamReader(getClass().getResourceAsStream(path)));
            var json2 = JsonParser.parseReader(new InputStreamReader(new ByteArrayInputStream(os.toByteArray())));
            assertEquals(json1, json2);
        }
        catch (Throwable e)
        {
            throw new Exception("Failed test " + path, e);
        }
    }
    
    
    public void testReadWriteScalars() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/simple_components.xml");
    }
    
    
    public void testReadWriteRanges() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/range_components.xml");
    }
    
    
    public void testReadWriteRecord() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/record_weather.xml");
        readXmlWriteJson("examples_v20/spec/record_coefs.xml");
        readXmlWriteJson("examples_v20/sps/TaskingParameter_DataRecord.xml");
    }
    
    
    public void testReadWriteRecordWithConstraints() throws Exception
    {
        readXmlWriteJson("examples_v20/sps/TaskingParameter_DataRecord_constraints.xml");
        readXmlWriteJson("examples_v20/spec/constraints.xml");
    }
    
    
    public void testReadWriteRecordWithOptionals() throws Exception
    {
        readXmlWriteJson("examples_v20/sps/TaskingParameter_DataRecord_optional.xml");
    }
    
    
    public void testReadWriteRecordWithNilValues() throws Exception
    {
        //readXmlWriteJson("examples_v20/spec/nilValues.xml");
        readXmlWriteJson("examples_v20/spec/nilValues_noxlinks.xml");
    }
    
    
    public void testReadWriteRecordWithQuality() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/quality.xml");
    }
    
    
    /*public void testReadWriteRecordWithXlinks() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/record_weather_xlinks.xml");
    }*/
    
    
    public void testReadWriteVector() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/vector_location.xml");
        readXmlWriteJson("examples_v20/spec/vector_quaternion.xml");
        readXmlWriteJson("examples_v20/spec/vector_velocity.xml");
    }
    
    
    public void testReadWriteArrayNoData() throws Exception
    {        
        readXmlWriteJson("examples_v20/spec/array_trajectory.xml");
        readXmlWriteJson("examples_v20/spec/array_image_band_interleaved.xml");
        readXmlWriteJson("examples_v20/spec/array_image_pixel_interleaved.xml");
    }
    
    
    public void testReadWriteArrayWithTextData() throws Exception
    {        
        readXmlWriteJson("examples_v20/spec/array_weather.xml");
        readXmlWriteJson("examples_v20/spec/enc_text_curve.xml");
        readXmlWriteJson("examples_v20/spec/enc_text_profile_series.xml");
        readXmlWriteJson("examples_v20/spec/enc_text_stress_matrix.xml");
        readXmlWriteJson("examples_v20/spec/matrix_rotation.xml");
    }
    
    
    /*public void testReadWriteArrayWithXmlData() throws Exception
    {        
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_curve.xml");
        readWriteCompareSweCommonXml("examples_v20/spec/enc_xml_profile_series.xml");
    }*/
    
    
    public void testReadWriteDataChoice() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/choice_stream.xml");
    }
    
    
    public void testReadWriteDataStream() throws Exception
    {
        readXmlWriteJson("examples_v20/weather_data.xml", true);
        readXmlWriteJson("examples_v20/nav_data.xml", true);
        readXmlWriteJson("examples_v20/image_data.xml", true);
        readXmlWriteJson("examples_v20/spec/datastream_with_quality.xml", true);
    }
    
    
    public void testReadWriteDataStreamWithChoice() throws Exception
    {
        readXmlWriteJson("examples_v20/spec/enc_text_choice_stream.xml", true);
    }
    
    
    public void testReadWithTypePropLast() throws Exception
    {
        // type at end
        var json = new JsonObject();
        json.addProperty("definition", SWEConstants.DEF_SAMPLING_TIME);
        json.addProperty("referenceFrame", SWEConstants.TIME_REF_UTC);
        var uomObj = new JsonObject();
        uomObj.addProperty("href", Time.ISO_TIME_UNIT);
        json.add("uom", uomObj);
        json.addProperty("value", "2002-05-14T23:45:12Z");
        json.addProperty("type", "Time");
        
        var gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(json));
        
        var reader = new JsonReader(new StringReader(json.toString()));
        var comp = new SWEJsonBindings(false).readDataComponent(reader);
        
        try (var writer = gson.newJsonWriter(new PrintWriter(System.out))) {
            new SWEJsonBindings().writeDataComponent(writer, comp, true);
        }
    }
    
    
    public void testReadWithTypePropNotFirst() throws Exception
    {
        // properties in alphabetical order
        var json = new JsonObject();
        json.addProperty("definition", SWEConstants.DEF_SAMPLING_TIME);
        json.addProperty("referenceFrame", SWEConstants.TIME_REF_UTC);
        json.addProperty("type", "Time");
        var uomObj = new JsonObject();
        uomObj.addProperty("href", Time.ISO_TIME_UNIT);
        json.add("uom", uomObj);
        json.addProperty("value", "2002-05-14T23:45:12Z");
        
        var gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(json));
        
        var reader = new JsonReader(new StringReader(json.toString()));
        var comp = new SWEJsonBindings(false).readDataComponent(reader);
        
        try (var writer = gson.newJsonWriter(new PrintWriter(System.out))) {
            new SWEJsonBindings().writeDataComponent(writer, comp, true);
        }
    }
    
    
    public void testReadWriteGeoms() throws Exception
    {
        readWriteJson("examples_v20/spec/json/geometry_novalue.json", false);
        readWriteJson("examples_v20/spec/json/geometry_point.json", false);
        readWriteJson("examples_v20/spec/json/geometry_line.json", false);
        readWriteJson("examples_v20/spec/json/geometry_poly.json", false);
        readWriteJson("examples_v20/spec/json/geometry_poly_holes.json", false);
    }
}
