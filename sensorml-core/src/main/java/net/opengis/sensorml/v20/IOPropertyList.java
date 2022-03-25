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

import org.vast.sensorML.SMLHelper;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.AbstractSWEIdentifiable;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataStream;


/**
 * <p>
 * Specialized list for holding input/output/parameter properties in SensorML.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 9, 2014
 */
public class IOPropertyList extends SMLPropertyList<AbstractSWEIdentifiable>
{
    private static final long serialVersionUID = 3858884993292819910L;


    public IOPropertyList()
    {
    }
    

    @Override
    public OgcProperty<AbstractSWEIdentifiable> add(String name, AbstractSWEIdentifiable ioDesc)
    {
        assignComponentName(name, ioDesc);
        return super.add(name, ioDesc);
    }
    
    
    @Override
    public void add(OgcProperty<AbstractSWEIdentifiable> prop)
    {
        if (prop.hasValue() && prop.getName() != null)
            assignComponentName(prop.getName(), prop.getValue());
        
        super.add(prop);
    }
    
    
    @Override
    public void add(int index, AbstractSWEIdentifiable component)
    {
        String name = SMLHelper.getIOComponent(component).getName();
        checkName(name);
        OgcPropertyImpl<AbstractSWEIdentifiable> prop = new OgcPropertyImpl<>(name, component);
        items.add(index, prop);
        nameMap.put(name, prop);
    }
    
    
    protected void assignComponentName(String name, AbstractSWEIdentifiable ioDesc)
    {
        if (ioDesc instanceof DataComponent)
            ((DataComponent)ioDesc).setName(name);
        else if (ioDesc instanceof DataStream)
            ((DataStream)ioDesc).getElementTypeProperty().setName(name);
        else if (ioDesc instanceof DataInterface)
            ((DataInterface)ioDesc).getData().getElementTypeProperty().setName(name);
    }


    public DataComponent getComponent(int i)
    {
        return SMLHelper.getIOComponent(get(i));
    }
    
    
    public DataComponent getComponent(String name)
    {
        return SMLHelper.getIOComponent(get(name));
    }
    
    
    public int getComponentIndex(String name)
    {
        AbstractSWEIdentifiable comp = this.get(name);
        if (comp == null)
            return -1;
        return indexOf(comp);
    }
    
}
