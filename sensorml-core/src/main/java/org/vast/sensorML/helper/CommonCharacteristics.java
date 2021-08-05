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
import org.vast.sensorML.SMLMetadataBuilders.CharacteristicListBuilder;
import org.vast.swe.SWEBuilders.QuantityBuilder;
import org.vast.swe.SWEBuilders.QuantityRangeBuilder;
import org.vast.swe.SWEHelper;


/**
 * <p>
 * Helper methods to generate common system characteristics
 * </p>
 *
 * @author Alex Robin
 * @date Jul 16, 2020
 */
public class CommonCharacteristics extends SMLPropertiesHelper
{
    public final static String OPERATING_RANGE_DEF = "http://www.w3.org/ns/ssn/systems/OperatingRange";
    public final static String SURVIVAL_RANGE_DEF = "http://www.w3.org/ns/ssn/systems/SurvivalRange";
    
    public static final String MASS_DEF = SWEHelper.getQudtUri("Mass");
    public static final String MASS_LABEL = "Mass";
    public static final String LENGTH_DEF = SWEHelper.getQudtUri("Length");
    public static final String LENGTH_LABEL = "Length";
    public static final String WIDTH_DEF = SWEHelper.getPropertyUri("Width");
    public static final String WIDTH_LABEL = "Width";
    public static final String HEIGHT_DEF = SWEHelper.getPropertyUri("Height");
    public static final String HEIGHT_LABEL = "Height";
    public static final String VOLTAGE_DEF = SWEHelper.getQudtUri("Voltage");
    public static final String VOLTAGE_LABEL = "Operating Voltage";
    public static final String CURRENT_DEF = SWEHelper.getQudtUri("Current");
    public static final String CURRENT_LABEL = "Operating Current";
    public static final String POWER_CONSUMPTION_DEF = SWEHelper.getPropertyUri("ElectricalPowerConsumption");
    public static final String POWER_CONSUMPTION_LABEL = "Power Consumption";

    public static final String BATT_CAPACITY_DEF = SWEHelper.getPropertyUri("BatteryCapacity");
    public static final String BATT_CAPACITY_LABEL = "Battery Capacity";
    public static final String BATT_LIFETIME_DEF = "http://www.w3.org/ns/ssn/systems/BatteryLifetime";
    public static final String BATT_LIFETIME_LABEL = "Battery Lifetime";
    public static final String SYSTEM_LIFETIME_DEF = "http://www.w3.org/ns/ssn/systems/SystemLifetime";
    public static final String SYSTEM_LIFETIME_LABEL = "System Lifetime";
    
    
    public CommonCharacteristics(SMLHelper sml)
    {
        super(sml);
    }
    
    
    /**
     * Creates a characteristic list describing the system's operating range
     * @return The pre-configured builder for chaining
     */
    public CharacteristicListBuilder operatingCharacteristics()
    {
        return sml.createCharacteristicList()
            .definition(OPERATING_RANGE_DEF)
            .label("Operating Characteristics");
    }
    
    
    /**
     * Creates a characteristic list describing the system's survival range
     * @return The pre-configured builder for chaining
     */
    public CharacteristicListBuilder survivalCharacteristics()
    {
        return sml.createCharacteristicList()
            .definition(SURVIVAL_RANGE_DEF)
            .label("Survival Characteristics");
    }
    

    // mechanical properties
    
    public QuantityBuilder mass(double val, String uom)
    {
        checkUom(uom, MASS_UNIT);
        
        return sml.createQuantity()
            .definition(MASS_DEF)
            .label(MASS_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
    public QuantityBuilder length(double val, String uom)
    {
        checkUom(uom, DISTANCE_UNIT);
        
        return sml.createQuantity()
            .definition(LENGTH_DEF)
            .label(LENGTH_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
    public QuantityBuilder width(double val, String uom)
    {
        checkUom(uom, DISTANCE_UNIT);
        
        return sml.createQuantity()
            .definition(WIDTH_DEF)
            .label(WIDTH_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
    public QuantityBuilder height(double val, String uom)
    {
        checkUom(uom, DISTANCE_UNIT);
        
        return sml.createQuantity()
            .definition(HEIGHT_DEF)
            .label(HEIGHT_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
    
    // electrical properties
        
    public QuantityBuilder operatingVoltage(double val, String uom)
    {
        checkUom(uom, VOLTAGE_UNIT);
        
        return sml.createQuantity()
            .definition(VOLTAGE_DEF)
            .label(VOLTAGE_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
    public QuantityRangeBuilder operatingVoltageRange(double min, double max, String uom)
    {
        checkUom(uom, VOLTAGE_UNIT);
        
        return sml.createQuantityRange()
            .definition(VOLTAGE_DEF)
            .label(VOLTAGE_LABEL + " Range")
            .uomCode(uom)
            .value(min, max);
    }


    public QuantityBuilder operatingCurrent(double val, String uom)
    {
        checkUom(uom, CURRENT_UNIT);
        
        return sml.createQuantity()
            .definition(CURRENT_DEF)
            .label(CURRENT_LABEL)
            .uomCode(uom)
            .value(val);
    }


    public QuantityRangeBuilder operatingCurrentRange(double min, double max, String uom)
    {
        checkUom(uom, CURRENT_UNIT);
        
        return sml.createQuantityRange()
            .definition(CURRENT_DEF)
            .label(CURRENT_LABEL)
            .uomCode(uom)
            .value(min, max);
    }


    public QuantityBuilder nominalPowerConsumption(double val, String uom)
    {
        checkUom(uom, POWER_UNIT);
        
        return sml.createQuantity()
            .definition(POWER_CONSUMPTION_DEF)
            .label(POWER_CONSUMPTION_LABEL)
            .uomCode(uom)
            .value(val);
    }


    public QuantityBuilder batteryCapacity(double val, String uom)
    {
        checkUom(uom, uomParser.getUnit("W.h"));
        
        return sml.createQuantity()
            .definition(BATT_CAPACITY_DEF)
            .label(BATT_CAPACITY_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
    public QuantityBuilder batteryLifetime(double val, String uom)
    {
        checkUom(uom, TIME_UNIT);
        
        return sml.createQuantity()
            .definition(BATT_LIFETIME_DEF)
            .label(BATT_LIFETIME_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
    public QuantityBuilder systemLifetime(double val, String uom)
    {
        checkUom(uom, TIME_UNIT);
        
        return sml.createQuantity()
            .definition(SYSTEM_LIFETIME_DEF)
            .label(SYSTEM_LIFETIME_LABEL)
            .uomCode(uom)
            .value(val);
    }
    
}
