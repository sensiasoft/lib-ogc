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

import org.vast.sensorML.SMLBuilders.TermBuilder;
import org.vast.sensorML.SMLHelper;
import org.vast.swe.SWEHelper;


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
    public static final String SITEID_DEF = SWEHelper.getPropertyUri("SiteID");
    public static final String SITEID_LABEL = "Site ID";
    public static final String CALLSIGN_DEF = SWEHelper.getDBpediaUri("Call_sign");
    public static final String CALLSIGN_LABEL = "Callsign";
    
    // contact roles
    public static final String MANUFACTURER_DEF = SWEHelper.getPropertyUri("Manufacturer");
    public static final String MANUFACTURER_LABEL = "Manufacturer Name";
    public static final String OWNER_DEF = SWEHelper.getPropertyUri("Owner");
    public static final String OWNER_LABEL = "Owner";
    public static final String AUTHOR_DEF = SWEHelper.getDBpediaUri("Author");
    public static final String AUTHOR_LABEL = "Author Name";
    public static final String OPERATOR_DEF = SWEHelper.getPropertyUri("Operator");
    public static final String OPERATOR_LABEL = "Operator";
    
    // document roles
    public static final String USER_MANUAL_DEF = SWEHelper.getDBpediaUri("User_guide");
    public static final String SPECSHEET_DEF = SWEHelper.getDBpediaUri("Datasheet");
    public static final String WEBPAGE_DEF = SWEHelper.getDBpediaUri("Web_page");
    public static final String REPORT_DEF = SWEHelper.getDBpediaUri("Report");
    
    
    SMLHelper sml;
    
    
    public CommonIdentifiers(SMLHelper sml)
    {
        this.sml = sml;
    }
    
    
    public TermBuilder shortName(String value)
    {
        return sml.createTerm()
            .definition(SHORT_NAME_DEF)
            .label(SHORT_NAME_LABEL)
            .value(value);
    }
    
    public TermBuilder longName(String value)
    {
        return sml.createTerm()
            .definition(LONG_NAME_DEF)
            .label(LONG_NAME_LABEL)
            .value(value);
    }
    
    public TermBuilder serialNumber(String value)
    {
        return sml.createTerm()
            .definition(SERIAL_NUMBER_DEF)
            .label(SERIAL_NUMBER_LABEL)
            .value(value);
    }
    
    public TermBuilder modelNumber(String value)
    {
        return sml.createTerm()
            .definition(MODEL_NUMBER_DEF)
            .label(MODEL_NUMBER_LABEL)
            .value(value);
    }
    
    public TermBuilder softwareVersion(String value)
    {
        return sml.createTerm()
            .definition(SOFTWARE_VERSION_DEF)
            .label(SOFTWARE_VERSION_LABEL)
            .value(value);
    }
    
    public TermBuilder firmwareVersion(String value)
    {
        return sml.createTerm()
            .definition(FIRMWARE_VERSION_DEF)
            .label(FIRMWARE_VERSION_LABEL)
            .value(value);
    }
    
    public TermBuilder manufacturer(String value)
    {
        return sml.createTerm()
            .definition(MANUFACTURER_DEF)
            .label(MANUFACTURER_LABEL)
            .value(value);
    }
    
    public TermBuilder author(String value)
    {
        return sml.createTerm()
            .definition(AUTHOR_DEF)
            .label(AUTHOR_LABEL)
            .value(value);
    }
    
    public TermBuilder operator(String value)
    {
        return sml.createTerm()
            .definition(OPERATOR_DEF)
            .label(OPERATOR_LABEL)
            .value(value);
    }
    
    public TermBuilder siteId(String value, String codeSpace)
    {
        return sml.createTerm()
            .definition(SITEID_DEF)
            .codeSpace(codeSpace)
            .label(SITEID_LABEL)
            .value(value);
    }
    
    public TermBuilder callsign(String value)
    {
        return sml.createTerm()
            .definition(CALLSIGN_DEF)
            .label(CALLSIGN_LABEL)
            .value(value);
    }
}
