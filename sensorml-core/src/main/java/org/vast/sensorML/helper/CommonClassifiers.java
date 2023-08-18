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
 * Helper methods to generate common system/process classifiers
 * </p>
 *
 * @author Alex Robin
 * @date Jul 16, 2020
 */
public class CommonClassifiers
{
    public static final String SENSOR_TYPE_DEF = SWEHelper.getPropertyUri("SensorType");
    public static final String SENSOR_TYPE_LABEL = "Sensor Type";
    
    SMLHelper sml;
    
    
    public CommonClassifiers(SMLHelper sml)
    {
        this.sml = sml;
    }
    
    
    public TermBuilder sensorType(String value)
    {
        return sml.createTerm()
            .definition(SENSOR_TYPE_DEF)
            .label(SENSOR_TYPE_LABEL)
            .value(value);
    }
    
    
    public TermBuilder sensorType(String codeSpace, String value)
    {
        return sml.createTerm()
            .definition(SENSOR_TYPE_DEF)
            .codeSpace(codeSpace)
            .label(SENSOR_TYPE_LABEL)
            .value(value);
    }
}
