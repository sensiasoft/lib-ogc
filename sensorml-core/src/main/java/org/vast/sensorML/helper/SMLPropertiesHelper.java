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


/**
 * <p>
 * Base class for SML property helpers
 * </p>
 *
 * @author Alex Robin
 * @date Jul 17, 2020
 */
public class SMLPropertiesHelper
{
    SMLHelper sml;
    
    
    public SMLPropertiesHelper(SMLHelper sml)
    {
        this.sml = sml;
    }
    
    
    void checkUom(String uom, Unit baseUnit)
    {
        sml.checkUom(uom, baseUnit);
    }
    
    
    void checkUom(String uom, String baseUnit)
    {
        sml.checkUom(uom, baseUnit);
    }
}
