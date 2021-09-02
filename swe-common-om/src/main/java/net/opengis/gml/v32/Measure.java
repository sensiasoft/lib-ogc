/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2021 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32;

import java.io.Serializable;

/**
 * POJO class for XML type MeasureType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public interface Measure extends Serializable
{
    
    /**
     * @return the measured value
     */
    public double getValue();
    
    
    /**
     * Sets the measured value
     * @param value decimal value
     */
    public void setValue(double value);
    
    
    /**
     * @return the uom property
     */
    public String getUom();
    
    
    /**
     * Sets the uom property
     * @param uom URI or UCUM code
     */
    public void setUom(String uom);
}
