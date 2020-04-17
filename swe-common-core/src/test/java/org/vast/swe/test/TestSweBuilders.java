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
            .withDefinition(SWEHelper.getPropertyUri("AirTemperature"))
            .withLabel("Air Temperature")
            .withDescription("Temperature of air in the garden")
            .withUomCode("Cel")
            .build(), false, true);
        
        System.out.println();
        System.out.println();
        
        utils.writeComponent(System.out, SWEBuilders.newQuantity()
            .withDefinition(SWEHelper.getPropertyUri("LinearAcceleration"))
            .withLabel("Acceleration")
            .withUomCode("m/s2")
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
        utils.writeComponent(System.out, SWEBuilders.newRecord()
            .withLabel("Weather Record")
            .withIsoTimeStampUTC("time")
            .withQuantityField("temp")
                .withDefinition(SWEHelper.getPropertyUri("AirTemperature"))
                .withLabel("Air Temperature")
                .withUomCode("Cel")
                .done()
            .withQuantityField("press")
                .withDefinition(SWEHelper.getPropertyUri("AtmosphericPressure"))
                .withLabel("Air Pressure")
                .withUomCode("hPa")
                .done()
            .withQuantityField("windSpeed")
                .withDefinition(SWEHelper.getPropertyUri("WindSpeed"))
                .withLabel("Wind Speed")
                .withUomCode("km/h")
                .done()
            .build(), false, true);
        
        System.out.println();
        System.out.println();
    }
    
    
    @Test
    public void testCreateMixedTypeRecord() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newRecord()
            .withLabel("Mixed Type Record")
            .withIsoTimeStampUTC("time")
            .withBooleanField("boolean")
                .withDefinition(SWEHelper.getPropertyUri("AboveThreshold"))
                .done()
            .withCategoryField("cat")
                .withDefinition(SWEHelper.getPropertyUri("Species"))
                .withLabel("Species Name")
                .done()
            .withTextField("text")
                .withDefinition(SWEHelper.getPropertyUri("VIN"))
                .withLabel("Vehicle Identification Number")
                .visitor(t -> System.out.println(t + "@" + System.identityHashCode(t)))
                .done()
            .build(), false, true);
        
        System.out.println();
        System.out.println();
    }
        
    
    @Test
    public void testNestedRecords() throws Exception
    {
        utils.writeComponent(System.out, SWEBuilders.newRecord()
            .withLabel("Parent Record")
            .withIsoTimeStampUTC("time")
            .withBooleanField("boolean")
                .withDefinition(SWEHelper.getPropertyUri("Flag"))
                .done()
            .withNestedRecord("child")
                .withDefinition(SWEHelper.getPropertyUri("VIN"))
                .withLabel("Child Record")
                .withTimeField("scan_start")
                    .withUomCode("ms")
                    .withValue(10245)
                    .done()
                .withCountField("num_samples")
                    .withValue(2400)
                    .done()
                .done()
            .build(), false, true);
        
        System.out.println();
        System.out.println();
    }
}
