/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.vast.swe.SWEHelper;
import com.google.common.collect.Lists;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;


public class TestTextDataParser
{
     
    protected void writeReadAndCompare(DataComponent dataStruct, List<DataBlock> records) throws IOException
    {
        var multipleRecords = records.size() > 1;
        
        // write CSV to byte buffer
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        TextDataWriter writer = new TextDataWriter();
        writer.setDataComponents(dataStruct.copy());
        writer.setOutput(os);
        writer.startStream(multipleRecords);
        for (var rec: records)
            writer.write(rec);
        writer.endStream();
        writer.flush();
        byte[] bytes = os.toByteArray();
        //if (!multipleRecords)
            System.out.println(new String(bytes));
        
        // read back CSV
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        TextDataParser parser = new TextDataParser();
        parser.setDataComponents(dataStruct.copy());
        parser.setInput(is);
        parser.setRenewDataBlock(true);
        var parsedRecords = new ArrayList<DataBlock>();
        DataBlock dataBlk;
        do {
            dataBlk = parser.parseNextBlock();
            if (dataBlk != null)
                parsedRecords.add(dataBlk);
        } while (dataBlk != null);
                
        // compare
        assertEquals("Wrong number of records", records.size(), parsedRecords.size());
        for (int i = 0; i < records.size(); i++)
        {
            //System.out.println("Record #" + i);
            var expected = records.get(i);
            var actual = parsedRecords.get(i);
            assertEquals("Wrong datablock size", expected.getAtomCount(), actual.getAtomCount());
            
            for (int j = 0; j < expected.getAtomCount(); j++)
            {
                //if (!multipleRecords)
                //    System.out.format("expected=%s, actual=%s\n", expected.getStringValue(j), actual.getStringValue(j));
                assertEquals(expected.getStringValue(j), actual.getStringValue(j));
            }
        }
    }
    
    
    @Test
    public void testWriteAndReadBackSimpleRecord() throws IOException
    {
        // create record structure
        SWEHelper fac = new SWEHelper();
        DataRecord dataStruct = fac.createRecord()
            .addSamplingTimeIsoUTC("t0")
            .addField("q1", fac.createQuantity().build())
            .addField("t2", fac.createText().build())
            .addField("t3", fac.createText().build())
            .addField("c4", fac.createCount().build())
            .addField("cat5", fac.createCategory().build())
            .build();
        
        // test with multiple records
        var now = (double)Instant.now().getEpochSecond();
        var records = new ArrayList<DataBlock>();
        int numRecords = 10;
        for (int r=0; r<numRecords; r++)
        {
            var rec = dataStruct.createDataBlock();
            int dataBlkIdx = 0;
            double val = (double)r;
            rec.setDoubleValue(dataBlkIdx++, now+val);
            rec.setDoubleValue(dataBlkIdx++, val);
            rec.setStringValue(dataBlkIdx++, (r % 2 == 0) ? "" : "text1." + (int)val);
            rec.setStringValue(dataBlkIdx++, "text2." + (int)val);
            rec.setIntValue(dataBlkIdx++, ((int)val)+100);
            rec.setStringValue(dataBlkIdx++, "cat_val=" + val);
            
            records.add(rec);
        }
        
        writeReadAndCompare(dataStruct, records);
    }
    
    
    @Test
    public void testWriteAndReadBackFixedSizeArray() throws IOException
    {
        // create record structure
        SWEHelper fac = new SWEHelper();
        int arraySize = 5;
        DataArray dataStruct = fac.createArray()
            .withFixedSize(arraySize)
            .name("array")
            .withElement("elt", fac.createRecord()
                .addSamplingTimeIsoUTC("f0")
                .addField("f1", fac.createQuantity().build())
                .addField("f2", fac.createQuantity().build())
                .addField("rec2", fac.createRecord()
                    .addField("f3", fac.createCount().build())
                    .addField("f4", fac.createQuantity().build())
                    .build())
                .build())
            .build();

        // test with one record
        var now = (double)Instant.now().getEpochSecond();
        var rec = dataStruct.createDataBlock();
        int dataBlkIdx = 0;
        for (int i=0; i<arraySize; i++)
        {
            double val = (double)i;
            rec.setDoubleValue(dataBlkIdx++, now+val);
            rec.setDoubleValue(dataBlkIdx++, val/1000.);
            rec.setDoubleValue(dataBlkIdx++, val+100);
            rec.setIntValue(dataBlkIdx++, ((int)val)+200);
            rec.setDoubleValue(dataBlkIdx++, val+300);
        }
        writeReadAndCompare(dataStruct, Lists.newArrayList(rec));
        
        // test with multiple records
        var records = new ArrayList<DataBlock>();
        int numRecords = 10;
        for (int r=0; r<numRecords; r++)
        {
            rec = dataStruct.createDataBlock();
            dataBlkIdx = 0;
            for (int i=0; i<arraySize; i++)
            {
                double val = (double)i + r*60.;
                rec.setDoubleValue(dataBlkIdx++, now+val);
                rec.setDoubleValue(dataBlkIdx++, val/1000.);
                rec.setDoubleValue(dataBlkIdx++, val+100);
                rec.setIntValue(dataBlkIdx++, ((int)val)+200);
                rec.setDoubleValue(dataBlkIdx++, val+300);
            }
            
            records.add(rec);
        }
        
        writeReadAndCompare(dataStruct, records);
    }
    
    
    @Test
    public void testWriteAndReadBackVarSizeArray() throws IOException
    {
        // create record structure
        SWEHelper fac = new SWEHelper();
        Count sizeField;
        DataRecord dataStruct = fac.createRecord()
            .addSamplingTimeIsoUTC("t0")
            .addField("size", sizeField = fac.createCount()
                .id("NUM_POINTS")
                .build())
            .addField("array", fac.createArray()
                .withSizeComponent(sizeField)
                .withElement("elt", fac.createVector()
                    .addCoordinate("c1", fac.createQuantity().build())
                    .addCoordinate("c2", fac.createQuantity().build())
                    .build())
                .build())
            .build();

        // test with multiple records
        var now = (double)Instant.now().getEpochSecond();
        var records = new ArrayList<DataBlock>();
        int numRecords = 5;
        for (int r=0; r<numRecords; r++)
        {
            int dataBlkIdx = 0;
            
            int arraySize = r+2;
            ((DataArray)dataStruct.getComponent("array")).updateSize(arraySize);
            DataBlock rec = dataStruct.createDataBlock();
            
            rec.setDoubleValue(dataBlkIdx++, now+r);
            rec.setIntValue(dataBlkIdx++, arraySize);
            
            for (int i=0; i<arraySize; i++)
            {
                double val = (double)i + r*10.;
                rec.setDoubleValue(dataBlkIdx++, val-100.);
                rec.setDoubleValue(dataBlkIdx++, val+100);
            }
            
            records.add(rec);
        }
        
        writeReadAndCompare(dataStruct, records);
    }


    /*
    @Test
    public void test() throws IOException
    {
        SWEHelper fac = new SWEHelper();
        int arraySize = 5;

        DataArray array = fac.createArray()
            .withFixedSize(arraySize)
            .name("array")
            .withElement("elt", fac.createRecord()
                .addSamplingTimeIsoUTC("f0")
                .addField("f1", fac.createQuantity().build())
                .addField("f2", fac.createQuantity().build())
                .addField("rec2", fac.createRecord()
                    .addField("f3", fac.createCount().build())
                    .addField("f4", fac.createQuantity().build())
                    .build())
                .build())
            .build();

        // generate data
        String tokenSep = "!";
        String blockSep = "@@\n";
        DateTimeFormat timeFormat = new DateTimeFormat();
        double now = new Date().getTime()/1000.;
        StringBuilder buf = new StringBuilder();
        for (int r=0; r<100; r++)
        {
            for (int i=0; i<arraySize; i++)
            {
                double val = (double)i + r*10;
                buf.append(timeFormat.formatIso(now+val, 0)).append(tokenSep)
                   .append(val/1000.).append(tokenSep)
                   .append(val+100).append(tokenSep)
                   .append(((int)val)+200).append(tokenSep)
                   .append(val+300);//.append(tokenSep)
                   //.append("f5").append(tokenSep)
                   //.append("test" + i);
                if (i == arraySize-1)
                    buf.append(blockSep);
                else
                    buf.append(tokenSep);
            }
        }
        //System.out.println(buf.toString());

        for (int it=0; it<10; it++)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(buf.toString().getBytes());

            // init & launch parser
            //DataStreamParser parser = new AsciiDataParser();
            DataStreamParser parser = new TextDataParser();
            parser.setDataComponents(array);
            parser.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            parser.setInput(is);
            parser.setRenewDataBlock(false);

            long t0 = System.currentTimeMillis();
            DataBlock dataBlk;
            do
            {
                //System.out.println();
                dataBlk = parser.parseNextBlock();
                //if (dataBlk != null)
                {
                //    for (int i=0; i<dataBlk.getAtomCount();i++)
                //    System.out.print(dataBlk.getStringValue(i) + ",");
                }
            }
            while (dataBlk != null);
            System.out.println("Exec Time = " + (System.currentTimeMillis()-t0));
        }

        for (int it=0; it<10; it++)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(buf.toString().getBytes());

            // init parser
            DataStreamParser parser = new TextDataParser();
            parser.setDataComponents(array);
            parser.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            parser.setInput(is);
            parser.setRenewDataBlock(false);

            // init writer
            DataStreamWriter writer = new TextDataWriter();
            writer.setDataComponents(array);
            writer.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            //DataStreamWriter writer = new JsonDataWriter();
            //writer.setDataComponents(array);
            //writer.setDataEncoding(fac.newXMLEncoding());
            //writer.setOutput(System.out);
            writer.setOutput(new ByteArrayOutputStream(1024*1024));

            long t0 = System.currentTimeMillis();
            DataBlock dataBlk;
            do
            {
                dataBlk = parser.parseNextBlock();
                if (dataBlk != null)
                {
                    writer.write(dataBlk);
                    writer.flush();
                }
            }
            while (dataBlk != null);

            System.out.println("Exec Time = " + (System.currentTimeMillis()-t0));
        }
    }*/

}
