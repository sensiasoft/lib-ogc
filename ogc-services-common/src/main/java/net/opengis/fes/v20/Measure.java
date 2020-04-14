/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20;



/**
 * POJO class for XML type MeasureType(@http://www.opengis.net/fes/2.0).
 *
 */
public interface Measure
{
        
    /**
     * Gets the uom property
     */
    public String getUom();
    
    
    /**
     * Sets the uom property
     */
    public void setUom(String uom);
    
    
    /**
     * Gets the inline value
     */
    public double getValue();
    
    
    /**
     * Sets the inline value
     */
    public void setValue(double value);
}
