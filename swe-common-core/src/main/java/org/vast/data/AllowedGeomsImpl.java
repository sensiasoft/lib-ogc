/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.util.ArrayList;
import java.util.List;
import net.opengis.swe.v20.AllowedGeoms;
import net.opengis.swe.v20.GeometryData.GeomType;


/**
 * POJO class for XML type AllowedGeomsType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class AllowedGeomsImpl extends AbstractSWEImpl implements AllowedGeoms
{
    private static final long serialVersionUID = 137517201008043714L;
    protected ArrayList<GeomType> geomList = new ArrayList<>();
    
    
    public AllowedGeomsImpl()
    {
    }
    
    
    @Override
    public AllowedGeomsImpl copy()
    {
        AllowedGeomsImpl newObj = new AllowedGeomsImpl();
        for (GeomType val: geomList)
            newObj.geomList.add(val);
        return newObj;
    }
    
    
    public boolean isValid(String value)
    {
        // validate value against constraint
        for (var allowedVal: geomList)
            if (allowedVal.equals(value))
                return true;
        
        return false;
    }
    
    
    protected String getAssertionMessage()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("Must ");
                
        if (!geomList.isEmpty())
        {
            msg.append("be one of {");
            
            int i = 0;
            for (var allowedValue: geomList)
            {
                if (i++ > 0)
                    msg.append(", ");
                msg.append(allowedValue);
            }
            
            msg.append('}');
        }
        
        return msg.toString();
    }
    
    
    /**
     * Gets the list of value properties
     */
    @Override
    public List<GeomType> getGeomList()
    {
        return geomList;
    }
    
    
    /**
     * Returns number of value properties
     */
    @Override
    public int getNumGeomTypes()
    {
        if (geomList == null)
            return 0;
        return geomList.size();
    }
    
    
    /**
     * Adds a new value property
     */
    @Override
    public void addGeomType(GeomType value)
    {
        this.geomList.add(value);
    }
}
