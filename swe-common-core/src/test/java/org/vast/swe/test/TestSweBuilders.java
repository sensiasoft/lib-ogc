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
            .setDefinition(SWEHelper.getPropertyUri("AirTemperature"))
            .setLabel("Air Temperature")
            .setDescription("Temperature of air in the garden")
            .setUomCode("Cel")
            .build(), false, true);
        
        System.out.println();
        System.out.println();
        
        utils.writeComponent(System.out, SWEBuilders.newQuantity()
            .setDefinition(SWEHelper.getPropertyUri("LinearAcceleration"))
            .setLabel("Acceleration")
            .setUomCode("m/s2")
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
            .setLabel("Weather Record")
            .addIsoTimeStampUTC("time")
            .addQuantityField("temp")
                .setDefinition(SWEHelper.getPropertyUri("AirTemperature"))
                .setLabel("Air Temperature")
                .setUomCode("Cel")
                .done()
            .addQuantityField("press")
                .setDefinition(SWEHelper.getPropertyUri("AtmosphericPressure"))
                .setLabel("Air Pressure")
                .setUomCode("hPa")
                .done()
            .addQuantityField("windSpeed")
                .setDefinition(SWEHelper.getPropertyUri("WindSpeed"))
                .setLabel("Wind Speed")
                .setUomCode("km/h")
                .done()
            .build(), false, true);
        
        System.out.println();
        System.out.println();
    }
    
    
    @Test
    public void testCreateMixedTypeRecord() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newDataRecord()
            .setLabel("Mixed Type Record")
            .addIsoTimeStampUTC("time")
            .addBooleanField("boolean")
                .setDefinition(SWEHelper.getPropertyUri("AboveThreshold"))
                .done()
            .addCategoryField("cat")
                .setDefinition(SWEHelper.getPropertyUri("Species"))
                .setLabel("Species Name")
                .done()
            .addTextField("text")
                .setDefinition(SWEHelper.getPropertyUri("VIN"))
                .setLabel("Vehicle Identification Number")
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
            .setLabel("Parent Record")
            .addIsoTimeStampUTC("time")
            .addBooleanField("boolean")
                .setDefinition(SWEHelper.getPropertyUri("Flag"))
                .done()
            .addNestedRecord("child")
                .setDefinition(SWEHelper.getPropertyUri("VIN"))
                .setLabel("Child Record")
                .addTimeField("scan_start")
                    .setUomCode("ms")
                    .setValue(10245)
                    .done()
                .addCountField("num_samples")
                    .setValue(2400)
                    .done()
                .done()
            .build(), false, true);
        
        System.out.println();
        System.out.println();
    }
}
