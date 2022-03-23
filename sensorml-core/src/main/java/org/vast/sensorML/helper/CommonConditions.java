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
import net.opengis.swe.v20.QuantityRange;


/**
 * <p>
 * Helper methods to generate common operating or survival conditions
 * </p>
 *
 * @author Alex Robin
 * @date Jul 16, 2020
 */
public class CommonConditions extends SMLPropertiesHelper
{    
    
    
    public CommonConditions(SMLHelper sml)
    {
        super(sml);
    }

    
    public QuantityRange temperatureRange(double min, double max, String uom)
    {
        checkUom(uom, SWEHelper.TEMP_UNIT);
        
        return sml.createQuantityRange()
            .definition(SWEHelper.getQudtUri("Temperature"))
            .label("Temperature Range")
            .uomCode(uom)
            .value(min, max)
            .build();
    }
}
