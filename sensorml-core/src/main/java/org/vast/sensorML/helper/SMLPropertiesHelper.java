/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML.helper;

import org.vast.sensorML.SMLHelper;
import org.vast.unit.Unit;
import org.vast.unit.UnitParserUCUM;
import org.vast.util.Asserts;


/**
 * <p>
 * Base class for property helpers providing unit validation routines
 * </p>
 *
 * @author Alex Robin
 * @date Jul 17, 2020
 */
public class SMLPropertiesHelper
{
    final static String INVALID_UOM = "Invalid unit";
    final static Unit MASS_UNIT = new Unit().setKilogram(1.0);
    final static Unit TIME_UNIT = new Unit().setSecond(1.0);
    final static Unit DISTANCE_UNIT = new Unit().setMeter(1.0);
    final static Unit ANGLE_UNIT = new Unit().setRadian(1.0);
    final static Unit TEMP_UNIT = new Unit().setKelvin(1.0);
    final static Unit SURFACE_UNIT = new Unit().setMeter(2.0);
    final static Unit VOLUME_UNIT = new Unit().setMeter(3.0);
    final static Unit VOLTAGE_UNIT = new Unit().setKilogram(1.0).setMeter(2.0).setSecond(-3).setAmpere(-1);
    final static Unit POWER_UNIT = new Unit().setKilogram(1.0).setMeter(2.0).setSecond(-3);
    
    SMLHelper sml;
    UnitParserUCUM uomParser = new UnitParserUCUM();
    
    
    public SMLPropertiesHelper(SMLHelper sml)
    {
        this.sml = sml;
    }
    
    
    void checkUom(String uom, Unit baseUnit)
    {
        Unit uomObj = uomParser.getUnit(uom);
        Asserts.checkArgument(uomObj.isCompatible(baseUnit), INVALID_UOM);
    }
}
