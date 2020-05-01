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

import org.junit.Test;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEBuilders;
import org.vast.swe.SWEHelper;


public class TestSweBuilders
{
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);


    @Test
    public void testCreateQuantities() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newQuantity()
            .definition(SWEHelper.getPropertyUri("AirTemperature"))
            .label("Air Temperature")
            .description("Temperature of air in the garden")
            .uomCode("Cel")
            .build(), false, true);

        System.out.println();
        System.out.println();

        utils.writeComponent(System.out, SWEBuilders.newQuantity()
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
        utils.writeComponent(System.out, SWEBuilders.newDataRecord()
            .label("Weather Record")
            .addSamplingTimeIsoUTC("time")
            .addQuantityField("temp")
                .definition(SWEHelper.getPropertyUri("AirTemperature"))
                .label("Air Temperature")
                .uomCode("Cel")
                .done()
            .addQuantityField("press")
                .definition(SWEHelper.getPropertyUri("AtmosphericPressure"))
                .label("Air Pressure")
                .uomCode("hPa")
                .done()
            .addQuantityField("windSpeed")
                .definition(SWEHelper.getPropertyUri("WindSpeed"))
                .label("Wind Speed")
                .uomCode("km/h")
                .done()
            .build(), false, true);

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateMixedTypeRecord() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newDataRecord()
            .label("Mixed Type Record")
            .addSamplingTimeIsoUTC("time")
            .addBooleanField("boolean")
                .definition(SWEHelper.getPropertyUri("AboveThreshold"))
                .done()
            .addCategoryField("cat")
                .definition(SWEHelper.getPropertyUri("Species"))
                .label("Species Name")
                .done()
            .addTextField("text")
                .definition(SWEHelper.getPropertyUri("VIN"))
                .label("Vehicle Identification Number")
                .visitor(t -> System.out.println(t + "@" + System.identityHashCode(t)))
                .done()
            .build(), false, true);

        System.out.println();
        System.out.println();
    }


    @Test
    public void testNestedRecords() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newDataRecord()
            .label("Parent Record")
            .addSamplingTimeIsoUTC("time")
            .addBooleanField("boolean")
                .definition(SWEHelper.getPropertyUri("Flag"))
                .done()
            .addRecordField("child")
                .definition(SWEHelper.getPropertyUri("VIN"))
                .label("Child Record")
                .addTimeField("scan_start")
                    .uomCode("ms")
                    .value(10245)
                    .done()
                .addCountField("num_samples")
                    .value(2400)
                    .done()
                .done()
            .build(), false, true);

        System.out.println();
        System.out.println();
    }
}
