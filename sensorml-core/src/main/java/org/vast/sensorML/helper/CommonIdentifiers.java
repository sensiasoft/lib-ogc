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
import org.vast.swe.SWEHelper;
import net.opengis.sensorml.v20.Term;


/**
 * <p>
 * Helper methods to generate common system/process identifiers
 * </p>
 *
 * @author Alex Robin
 * @date Jul 16, 2020
 */
public class CommonIdentifiers
{
    public static final String LONG_NAME_DEF = SWEHelper.getPropertyUri("LongName");
    public static final String LONG_NAME_LABEL = "Long Name";
    public static final String SHORT_NAME_DEF = SWEHelper.getPropertyUri("ShortName");
    public static final String SHORT_NAME_LABEL = "Short Name";
    public static final String SERIAL_NUMBER_DEF = SWEHelper.getPropertyUri("SerialNumber");
    public static final String SERIAL_NUMBER_LABEL = "Serial Number";
    public static final String MODEL_NUMBER_DEF = SWEHelper.getPropertyUri("ModelNumber");
    public static final String MODEL_NUMBER_LABEL = "Model Number";
    public static final String SOFTWARE_VERSION_DEF = SWEHelper.getPropertyUri("SoftwareVersion");
    public static final String SOFTWARE_VERSION_LABEL = "Software Version";
    public static final String FIRMWARE_VERSION_DEF = SWEHelper.getPropertyUri("FirmwareVersion");
    public static final String FIRMWARE_VERSION_LABEL = "Firmware Version";
    public static final String MANUFACTURER_DEF = SWEHelper.getPropertyUri("Manufacturer");
    public static final String MANUFACTURER_LABEL = "Manufacturer Name";
    
    SMLHelper sml;
    
    
    public CommonIdentifiers(SMLHelper sml)
    {
        this.sml = sml;
    }
    
    
    public Term shortName(String value)
    {
        return sml.createTerm()
            .definition(SHORT_NAME_DEF)
            .label(SHORT_NAME_LABEL)
            .value(value)
            .build();
    }
    
    public Term longName(String value)
    {
        return sml.createTerm()
            .definition(LONG_NAME_DEF)
            .label(LONG_NAME_LABEL)
            .value(value)
            .build();
    }
    
    public Term serialNumber(String value)
    {
        return sml.createTerm()
            .definition(SERIAL_NUMBER_DEF)
            .label(SERIAL_NUMBER_LABEL)
            .value(value)
            .build();
    }
    
    public Term modelNumber(String value)
    {
        return sml.createTerm()
            .definition(MODEL_NUMBER_DEF)
            .label(MODEL_NUMBER_LABEL)
            .value(value)
            .build();
    }
    
    public Term softwareVersion(String value)
    {
        return sml.createTerm()
            .definition(SOFTWARE_VERSION_DEF)
            .label(SOFTWARE_VERSION_LABEL)
            .value(value)
            .build();
    }
    
    public Term firmwareVersion(String value)
    {
        return sml.createTerm()
            .definition(FIRMWARE_VERSION_DEF)
            .label(FIRMWARE_VERSION_LABEL)
            .value(value)
            .build();
    }
    
    public Term manufacturer(String value)
    {
        return sml.createTerm()
            .definition(MANUFACTURER_DEF)
            .label(MANUFACTURER_LABEL)
            .value(value)
            .build();
    }
}
