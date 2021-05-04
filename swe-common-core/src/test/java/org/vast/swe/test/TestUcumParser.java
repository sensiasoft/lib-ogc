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
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.vast.unit.Unit;
import org.vast.unit.UnitFunctionOffset;
import org.vast.unit.UnitParserUCUM;


public class TestUcumParser
{

    @Test
    public void testBaseUnits() throws Exception
    {
        var parser = new UnitParserUCUM();
        assertEquals(1, (int)parser.getUnit("m").getMeter());
        assertEquals(1, (int)parser.getUnit("s").getSecond());
        assertEquals(1, (int)parser.getUnit("kg").getKilogram());
        assertEquals(1, (int)parser.getUnit("rad").getRadian());
        assertEquals(1, (int)parser.getUnit("K").getKelvin());
        assertEquals(1, (int)parser.getUnit("A").getAmpere());
        assertEquals(1, (int)parser.getUnit("cd").getCandela());
    }
    
    
    @Test
    public void testPrefixes() throws Exception
    {
        var parser = new UnitParserUCUM();
        assertEquals(1e24, parser.getUnit("Ym").getScaleToSI(), 1e-8);
        assertEquals(1e21, parser.getUnit("Zm").getScaleToSI(), 1e-8);
        assertEquals(1e18, parser.getUnit("Em").getScaleToSI(), 1e-8);
        assertEquals(1e15, parser.getUnit("Pm").getScaleToSI(), 1e-8);
        assertEquals(1e12, parser.getUnit("Tm").getScaleToSI(), 1e-8);
        assertEquals(1e9, parser.getUnit("Gm").getScaleToSI(), 1e-8);
        assertEquals(1e6, parser.getUnit("Mm").getScaleToSI(), 1e-8);
        assertEquals(1e3, parser.getUnit("km").getScaleToSI(), 1e-8);
        assertEquals(1e2, parser.getUnit("hm").getScaleToSI(), 1e-8);
        assertEquals(1e1, parser.getUnit("dam").getScaleToSI(), 1e-8);
        assertEquals(1e-1, parser.getUnit("dm").getScaleToSI(), 1e-8);
        assertEquals(1e-2, parser.getUnit("cm").getScaleToSI(), 1e-8);
        assertEquals(1e-3, parser.getUnit("mm").getScaleToSI(), 1e-8);
        assertEquals(1e-6, parser.getUnit("um").getScaleToSI(), 1e-8);
        assertEquals(1e-9, parser.getUnit("nm").getScaleToSI(), 1e-9);
        assertEquals(1e-12, parser.getUnit("pm").getScaleToSI(), 1e-12);
        assertEquals(1e-15, parser.getUnit("fm").getScaleToSI(), 1e-15);
        assertEquals(1e-18, parser.getUnit("am").getScaleToSI(), 1e-18);
        assertEquals(1e-21, parser.getUnit("zm").getScaleToSI(), 1e-21);
        assertEquals(1e-24, parser.getUnit("ym").getScaleToSI(), 1e-24);
    }
    
    
    @Test
    public void testDerivedUnits() throws Exception
    {
        var parser = new UnitParserUCUM();
        assertEquals(6.0221367e23, parser.getUnit("mol").getScaleToSI(), 1);
        Unit unit;
        
        unit = parser.getUnit("sr");
        assertEquals(2, unit.getRadian(), 1e-9);
        
        unit = parser.getUnit("Hz");
        assertEquals(-1, unit.getSecond(), 1e-9);
        
        unit = parser.getUnit("N");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(1, unit.getMeter(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
        
        unit = parser.getUnit("Pa");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(-1, unit.getMeter(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
        
        unit = parser.getUnit("J");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
        
        unit = parser.getUnit("W");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-3, unit.getSecond(), 1e-9);
        
        unit = parser.getUnit("C");
        assertEquals(1, unit.getSecond(), 1e-9);
        assertEquals(1, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("V");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-3, unit.getSecond(), 1e-9);
        assertEquals(-1, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("F");
        assertEquals(-1, unit.getKilogram(), 1e-9);
        assertEquals(-2, unit.getMeter(), 1e-9);
        assertEquals(4, unit.getSecond(), 1e-9);
        assertEquals(2, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("Ohm");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-3, unit.getSecond(), 1e-9);
        assertEquals(-2, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("S");
        assertEquals(-1, unit.getKilogram(), 1e-9);
        assertEquals(-2, unit.getMeter(), 1e-9);
        assertEquals(3, unit.getSecond(), 1e-9);
        assertEquals(2, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("Wb");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
        assertEquals(-1, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("Cel");
        assertEquals(1, unit.getKelvin(), 1e-9);
        assertTrue(unit.getFunction() instanceof UnitFunctionOffset);
        
        unit = parser.getUnit("T");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
        assertEquals(-1, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("H");
        assertEquals(1, unit.getKilogram(), 1e-9);
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
        assertEquals(-2, unit.getAmpere(), 1e-9);
        
        unit = parser.getUnit("lm");
        assertEquals(1, unit.getCandela(), 1e-9);
        assertEquals(2, unit.getRadian(), 1e-9);
                
        unit = parser.getUnit("lx");
        assertEquals(1, unit.getCandela(), 1e-9);
        assertEquals(2, unit.getRadian(), 1e-9);
        assertEquals(-2, unit.getMeter(), 1e-9);
        
        unit = parser.getUnit("Bq");
        assertEquals(-1, unit.getSecond(), 1e-9);
        
        unit = parser.getUnit("Gy");
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
        
        unit = parser.getUnit("Sv");
        assertEquals(2, unit.getMeter(), 1e-9);
        assertEquals(-2, unit.getSecond(), 1e-9);
    }
    
    
    @Test
    public void testDegFunctions() throws Exception
    {
        var parser = new UnitParserUCUM();
        
        var degC = parser.getUnit("Cel");
        System.out.println(degC);
        assertEquals(-273.15, degC.getFunction().fromProperUnit(0.0), 1e-9);
        assertEquals(0.0, degC.getFunction().toProperUnit(-273.15), 1e-9);
        assertEquals(0.0, degC.getFunction().fromProperUnit(273.15), 1e-9);
        assertEquals(273.15, degC.getFunction().toProperUnit(0.0), 1e-9);
        assertEquals(100.0, degC.getFunction().fromProperUnit(373.15), 1e-9);
        assertEquals(373.15, degC.getFunction().toProperUnit(100.0), 1e-9);
        
        var degF = parser.getUnit("[degF]");
        System.out.println(degF);
        assertEquals(-459.67, degF.getFunction().fromProperUnit(0.0), 1e-9);
        assertEquals(32.0, degF.getFunction().fromProperUnit(273.15), 1e-9);
        assertEquals(212.0, degF.getFunction().fromProperUnit(373.15), 1e-9);
    }
    
    
    @Test
    public void testDbFunctions() throws Exception
    {
        var parser = new UnitParserUCUM();
        
        var dBW = parser.getUnit("B[W]");
        System.out.println(dBW);
        assertEquals(-3, dBW.getFunction().fromProperUnit(0.001), 1e-9);
        assertEquals(1000, dBW.getFunction().toProperUnit(3), 1e-9);
        
        var dBmW = parser.getUnit("B[mW]");
        System.out.println(dBmW);
        assertEquals(0, dBmW.getFunction().fromProperUnit(0.001), 1e-9);
        assertEquals(1e-3, dBmW.getFunction().toProperUnit(0), 1e-9);
        assertEquals(1e-6, dBmW.getFunction().toProperUnit(-3), 1e-9);        
    }
}
