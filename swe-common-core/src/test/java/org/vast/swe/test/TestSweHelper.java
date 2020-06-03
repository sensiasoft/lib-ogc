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

import static org.junit.Assert.*;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import org.junit.Test;
import org.vast.data.BinaryEncodingImpl;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEHelper;


public class TestSweHelper
{
    SWEHelper helper = new SWEHelper();
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);


    @Test
    public void testCreateQuantities() throws Exception
    {
        utils.writeComponent(System.out, helper.createQuantity()
            .definition(SWEHelper.getPropertyUri("AirTemperature"))
            .label("Air Temperature")
            .description("Temperature of air in the garden")
            .uomCode("Cel")
            .build(), false, true);

        System.out.println();
        System.out.println();

        utils.writeComponent(System.out, helper.createQuantity()
            .definition(SWEHelper.getPropertyUri("LinearAcceleration"))
            .label("Acceleration")
            .uomCode("m/s2")
            .build(), false, true);

        System.out.println();
        System.out.println();
    }


    /*@Test
    public void testCreateRasters() throws Exception
    {
        // RGB
        utils.writeComponent(System.out, fac.newRgbImage(640, 480, DataType.BYTE), false, true);
    }*/


    @Test
    public void testCreateWeatherRecord() throws Exception
    {
        utils.writeComponent(System.out, helper.createDataRecord()
            .label("Weather Record")
            .addSamplingTimeIsoUTC("time")
            .addField("temp", helper.createQuantity()
                .definition(SWEHelper.getPropertyUri("AirTemperature"))
                .label("Air Temperature")
                .uomCode("Cel")
                .build())
            .addField("press", helper.createQuantity()
                .definition(SWEHelper.getPropertyUri("AtmosphericPressure"))
                .label("Air Pressure")
                .uomCode("hPa")
                .build())
            .addField("windSpeed", helper.createQuantity()
                .definition(SWEHelper.getPropertyUri("WindSpeed"))
                .label("Wind Speed")
                .uomCode("km/h")
                .build())
            .build(), false, true);

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateMixedTypeRecord() throws Exception
    {
        utils.writeComponent(System.out, helper.createDataRecord()
            .label("Mixed Type Record")
            .addSamplingTimeIsoUTC("time")
            .addField("boolean", helper.createBoolean()
                .definition(SWEHelper.getPropertyUri("AboveThreshold"))
                .build())
            .addField("cat", helper.createCategory()
                .definition(SWEHelper.getPropertyUri("Species"))
                .label("Species Name")
                .build())
            .addField("text", helper.createText()
                .definition(SWEHelper.getPropertyUri("VIN"))
                .label("Vehicle Identification Number")
                .visitor(t -> System.out.println(t + "@" + System.identityHashCode(t)))
                .build())
            .build(), false, true);

        System.out.println();
        System.out.println();
    }


    @Test
    public void testNestedRecords() throws Exception
    {
        utils.writeComponent(System.out, helper.createDataRecord()
            .label("Parent Record")
            .addSamplingTimeIsoUTC("time")
            .addField("boolean", helper.createBoolean()
                .definition(SWEHelper.getPropertyUri("Flag"))
                .build())
            .addField("child", helper.createRecord()
                .definition(SWEHelper.getPropertyUri("VIN"))
                .label("Child Record")
                .addField("scan_start", helper.createTime()
                    .uomCode("ms")
                    .value(10245)
                    .build())
                .addField("num_samples", helper.createCount()
                    .value(2400)
                    .build())
                .build())
            .build(), false, true);

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateDefaultEncodingForImage() throws Exception
    {
        DataArray array = helper.newRgbImage(640, 480, DataType.BYTE);
        DataEncoding encoding = SWEHelper.getDefaultEncoding(array);
        assertEquals(BinaryEncodingImpl.class, encoding.getClass());
        utils.writeEncoding(System.out, encoding, true);
    }


    @Test
    public void testGetComponentByPath() throws Exception
    {
        DataRecord rec = helper.createRecord()
            .addSamplingTimeIsoUTC("time")
            .addField("temp", helper.createQuantity().build())
            .addField("press", helper.createQuantity().build())
            .addField("rec", helper.createRecord()
                .addField("flag1", helper.createBoolean().build())
                .addField("status", helper.createCategory().build())
                .build())
            .build();

        DataComponent c;

        c = SWEHelper.findComponentByPath(rec, "temp");
        assertEquals(c.getName(), "temp");

        c = SWEHelper.findComponentByPath(rec, "press");
        assertEquals(c.getName(), "press");

        c = SWEHelper.findComponentByPath(rec, "rec/flag1");
        assertEquals(c.getName(), "flag1");

        c = SWEHelper.findComponentByPath(rec, "rec/status");
        assertEquals(c.getName(), "status");
    }
}
