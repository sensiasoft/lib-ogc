/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SWE Common Data Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import static org.junit.Assert.assertEquals;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import org.junit.Test;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class TestGeoJsonBindings
{

    protected void readWriteCompareJson(String path) throws Exception
    {
        var geoJsonBindings = new GeoJsonBindings(true, false); 
        
        // read as reference JSON tree
        var is = getClass().getResourceAsStream(path);
        var json1 = JsonParser.parseReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        System.out.println(new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
            .toJson(json1));
        
        // also read using bindings
        is = getClass().getResourceAsStream(path);
        var reader = new JsonReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        var f = geoJsonBindings.readFeature(reader);
        
        // write back as JSON
        var output = new StringWriter();
        var writer = new JsonWriter(output);
        geoJsonBindings.writeFeature(writer, f);
        var json2 = JsonParser.parseReader(new StringReader(output.getBuffer().toString()));
        System.out.println(new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()
            .toJson(json2));
        
        // compare JSON trees
        assertEquals(json1, json2);
    }
    
    
    @Test
    public void testReadWriteFeatureWithGeom() throws Exception
    {
        readWriteCompareJson("geojson/feature_with_point.json");
    }
    
    
    @Test
    public void testReadWriteFeatureTypeNotFirst() throws Exception
    {
        readWriteCompareJson("geojson/feature_with_point_typemoved.json");
    }
    
    
    @Test
    public void testReadWriteFeatureWithLink() throws Exception
    {
        readWriteCompareJson("geojson/feature_with_link.json");
        readWriteCompareJson("geojson/feature_with_links.json");
    }
    
    
    @Test
    public void testReadWriteTimeExtent() throws Exception
    {
        
    }
}
