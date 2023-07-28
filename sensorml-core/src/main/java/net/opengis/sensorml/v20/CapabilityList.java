/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.sensorml.v20;

import net.opengis.OgcPropertyList;
import net.opengis.swe.v20.DataComponent;


/**
 * POJO class for XML type CapabilityListType(@http://www.opengis.net/sensorml/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface CapabilityList extends AbstractMetadataList
{
    /**
     * Gets the property that this group of capabilities relates to
     */
    public String getRelatedProperty();
    
    
    /**
     * Checks if definition is set
     */
    public boolean isSetRelatedProperty();
    
    
    /**
     * Sets the related property
     */
    public void setRelatedProperty(String definition);
    
    
    /**
     * Gets the list of capability properties
     */
    public OgcPropertyList<DataComponent> getCapabilityList();
    
    
    /**
     * Returns number of capability properties
     */
    public int getNumCapabilitys();
    
    
    /**
     * Gets the capability property with the given name
     */
    public DataComponent getCapability(String name);
    
    
    /**
     * Adds a new capability property
     */
    public void addCapability(String name, DataComponent capability);
    
    
    /**
     * Gets the list of conditions
     */
    public OgcPropertyList<DataComponent> getConditionList();
    
    
    /**
     * Returns number of conditions
     */
    public int getNumConditions();
    
    
    /**
     * Gets the condition with the given name
     */
    public DataComponent getCondition(String name);
    
    
    /**
     * Adds a new condition
     */
    public void addCondition(String name, DataComponent condition);
}
