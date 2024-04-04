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

import java.io.PrintWriter;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.GeometryData;
import net.opengis.swe.v20.GeometryData.GeomType;
import org.junit.Test;
import org.vast.data.DataBlockMixed;
import org.vast.data.JSONEncodingImpl;
import org.vast.json.JsonInliningWriter;
import org.vast.swe.SWEUtils;
import org.vast.swe.fast.JsonDataWriterGson;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEJsonBindings;
import org.vast.swe.helper.GeoPosHelper;


public class TestGeometries
{
    static final String TEST1_FAIL_MSG = "Wrong data block size before resizing";
    static final String TEST2_FAIL_MSG = "Wrong data block size after resizing";
    static final String WRONG_ARRAY_SIZE_MSG = "Wrong array size";

    GeoPosHelper fac = new GeoPosHelper();
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);
    
    
    protected void writeSchema(DataComponent rec) throws Exception
    {
        var writer = new JsonInliningWriter(new PrintWriter(System.out));
        writer.setIndent("  ");
        var jsonBindings = new SWEJsonBindings();
        jsonBindings.writeDataComponent(writer, rec, true);
        writer.flush();
    }
    
    
    protected void writeData(DataComponent rec, DataBlock data) throws Exception
    {
        var dataWriter = new JsonDataWriterGson();
        dataWriter.setOutput(System.out);
        dataWriter.setDataComponents(rec);
        dataWriter.setDataEncoding(new JSONEncodingImpl());
        dataWriter.write(data);
        dataWriter.flush();
    }
    
    
    @Test
    public void testPoint() throws Exception
    {
        var rec = fac.createRecord()
            .label("My Record")
            .addField("val", fac.createQuantity()
                .label("Numerical Value")
                .uom("Cel")
                .value(23.6))
            .addField("geom", fac.createGeometry()
                .definition(GeoPosHelper.DEF_LOCATION)
                .label("My Geom")
                .refFrame(SWEConstants.REF_FRAME_4979))
            .build();
        
        ((GeometryData)rec.getComponent("geom")).setGeomType(GeomType.Point);
        var dblk = rec.createDataBlock();
        dblk.setDoubleValue(0, 25.6);
        dblk.setDoubleValue(2, 1.0);
        dblk.setDoubleValue(3, 2.0);
        dblk.setDoubleValue(4, 3.0);
        
        writeSchema(rec);
        System.out.println();
        
        rec.setData(dblk);
        writeSchema(rec);
        System.out.println();
        
        System.out.println();
        writeData(rec, dblk);
    }
    
    
    @Test
    public void testLineString() throws Exception
    {
        var rec = fac.createRecord()
            .label("My Record")
            .addField("val", fac.createQuantity()
                .label("Numerical Value")
                .uom("Cel")
                .value(23.6))
            .addField("geom", fac.createGeometry()
                .definition(SWEConstants.SML_ONTOLOGY_ROOT + "RegionOfInterest")
                .label("My Geom")
                .addAllowedGeoms(GeomType.LineString)
                .refFrame(SWEConstants.REF_FRAME_CRS84h))
            .build();
        
        ((GeometryData)rec.getComponent("geom")).setGeomType(GeomType.LineString);
        //((GeometryData)rec.getComponent("geom")).setNumPoints(10);
        var dblk = rec.createDataBlock();
        dblk.setDoubleValue(0, 25.6);
        
        int lineSize = 3;
        var geomData = ((DataBlockMixed)dblk).getUnderlyingObject()[1];
        var pointsData = ((DataBlockMixed)geomData).getUnderlyingObject()[1];
        var coordsData = ((DataBlockMixed)pointsData).getUnderlyingObject()[1];
        pointsData.setIntValue(0, lineSize); // num points
        coordsData.resize(3*lineSize);
        ((DataBlockMixed)dblk).updateAtomCount();
        for (int p = 0; p < lineSize; p++)
        {
            int idx = p*3;
            coordsData.setDoubleValue(idx, p*10+1.0);
            coordsData.setDoubleValue(idx+1, p*10+2.0);
            coordsData.setDoubleValue(idx+2, p*10+3.0);
        }
        
        writeSchema(rec);
        System.out.println();
        
        rec.setData(dblk);
        writeSchema(rec);
        System.out.println();
        
        System.out.println();
        writeData(rec, dblk);
    }
}
