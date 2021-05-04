/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2021 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.vast.unit.UnitConversion;
import org.vast.unit.UnitParserUCUM;


public class TestUnitConversion
{

    protected void testConversion(String srcUom, String destUom, double srcVal, double expectedVal)
    {
        var ucum = new UnitParserUCUM();
        var srcUnit = ucum.getUnit(srcUom);
        var destUnit = ucum.getUnit(destUom);
        var converter =UnitConversion.getConverter(srcUnit, destUnit);
        
        assertEquals(expectedVal, converter.convert(srcVal), 1e-12);
    }
    
    
    @Test
    public void testConvertSimpleUnits() throws Exception
    {
        testConversion("m", "cm", 1.0, 100.0);
        testConversion("g", "kg", 1.0, 1e-3);
        testConversion("us", "h", 1.0, 1e-6/3600.);
        testConversion("%", "[ppm]", 0.1, 1000.);
        testConversion("%", "[ppth]", 1.0, 10.);
        testConversion("mbar", "atm", 1013.25, 1.0);
    }
    
    
    @Test
    public void testConvertExpressions() throws Exception
    {
        testConversion("N/m2", "Pa", 1.0, 1.0);
        testConversion("N/m2", "bar", 1.0, 1e-5);
        testConversion("[mu_0]", "N/A2", 1.0, 4*Math.PI*1e-7);
        testConversion("m3", "l", 1, 1000);
    }
    
    
    @Test
    public void testConvertWithFunc() throws Exception
    {
        testConversion("Cel", "[degF]", 0.0, 32.0);
        testConversion("Cel", "K", 0.0, 273.15);
        testConversion("Cel", "K", -273.15, 0.0);
    }
    
    
    @Test
    public void testConvertDb() throws Exception
    {
        testConversion("B[W]", "W", 3, 1000.0);
        testConversion("dB[mW]", "W", 30, 1.0);
        testConversion("dB[mW]", "mW", 30, 1e3);
        testConversion("dB[mW]", "dB[W]", 30, 0);
        testConversion("dB[W]", "B[W]", 30, 3);
    }
    
    
    @Test
    public void testConvertPH() throws Exception
    {
        testConversion("[pH]", "mol/l", 4, 0.0001);
    }
}
