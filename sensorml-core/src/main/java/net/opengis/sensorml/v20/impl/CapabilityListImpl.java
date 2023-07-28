/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.sensorml.v20.impl;

import org.vast.data.DataComponentPropertyList;
import net.opengis.OgcPropertyList;
import net.opengis.sensorml.v20.CapabilityList;
import net.opengis.swe.v20.DataComponent;


/**
 * POJO class for XML type CapabilityListType(@http://www.opengis.net/sensorml/2.0).
 *
 * This is a complex type.
 */
public class CapabilityListImpl extends AbstractMetadataListImpl implements CapabilityList
{
    private static final long serialVersionUID = 1077880441572054254L;
    protected String relatedProperty;
    protected OgcPropertyList<DataComponent> capabilityList = new DataComponentPropertyList<DataComponent>();
    protected OgcPropertyList<DataComponent> conditionList = new DataComponentPropertyList<DataComponent>();
    
    
    public CapabilityListImpl()
    {
    }


    @Override
    public String getRelatedProperty()
    {
        return relatedProperty;
    }


    @Override
    public boolean isSetRelatedProperty()
    {
        return relatedProperty != null;
    }


    @Override
    public void setRelatedProperty(String definition)
    {
        this.relatedProperty = definition;
    }
    
    
    /**
     * Gets the list of capability properties
     */
    @Override
    public OgcPropertyList<DataComponent> getCapabilityList()
    {
        return capabilityList;
    }
    
    
    /**
     * Returns number of capability properties
     */
    @Override
    public int getNumCapabilitys()
    {
        if (capabilityList == null)
            return 0;
        return capabilityList.size();
    }
    
    
    /**
     * Gets the capability property with the given name
     */
    @Override
    public DataComponent getCapability(String name)
    {
        if (capabilityList == null)
            return null;
        return capabilityList.get(name);
    }
    
    
    /**
     * Adds a new capability property
     */
    @Override
    public void addCapability(String name, DataComponent capability)
    {
        this.capabilityList.add(name, capability);
    }
    
    
    /**
     * Gets the list of conditions
     */
    @Override
    public OgcPropertyList<DataComponent> getConditionList()
    {
        return conditionList;
    }
    
    
    /**
     * Returns number of conditions
     */
    @Override
    public int getNumConditions()
    {
        if (conditionList == null)
            return 0;
        return conditionList.size();
    }
    
    
    /**
     * Gets the condition with the given name
     */
    @Override
    public DataComponent getCondition(String name)
    {
        if (conditionList == null)
            return null;
        return conditionList.get(name);
    }
    
    
    /**
     * Adds a new condition
     */
    @Override
    public void addCondition(String name, DataComponent condition)
    {
        this.conditionList.add(name, condition);
    }
}
