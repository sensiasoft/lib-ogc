/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.Vector;
import org.vast.data.BaseTreeVisitor;


/**
 * <p>
 * Data component visitor used to filter unwanted observables from the
 * SWE Common result structure.
 * </p>
 *
 * @author Alex Robin
 * @since Oct 9, 2015
 */
public class DataStructFilter extends BaseTreeVisitor
{
    HashSet<String> selectedObsProperties = new HashSet<>();
    
    
    public DataStructFilter(Collection<String> selectedObsProperties)
    {
        this.selectedObsProperties.addAll(selectedObsProperties);
    }
    
    
    @Override
    public void visit(DataRecord record)
    {
        Iterator<DataComponent> it = record.getFieldList().iterator();
        filterChildren(record.getDefinition(), it);
    }
    
    
    @Override
    public void visit(Vector vect)
    {
        Iterator<ScalarComponent> it = vect.getCoordinateList().iterator();
        filterChildren(vect.getDefinition(), it);
    }
    
    
    @Override
    public void visit(DataChoice choice)
    {
        Iterator<DataComponent> it = choice.getItemList().iterator();
        filterChildren(choice.getDefinition(), it);
    }
    
    
    protected void filterChildren(String parentDef, Iterator<? extends DataComponent> it)
    {
        // if the parent is selected, stop recursing cause we want to keep all descendants
        if (parentDef != null && selectedObsProperties.contains(parentDef))
            return;
        
        while (it.hasNext())
        {
            DataComponent child = it.next();
            String defUri = child.getDefinition();
            if (defUri != null && !selectedObsProperties.contains(defUri))
            {
                it.remove();
            }
            else
            {
                child.accept(this);
                
                // also remove composite if all its children have been removed
                if (!(child instanceof SimpleComponent) && child.getComponentCount() == 0)
                    it.remove();
            }
        }
    }

}
