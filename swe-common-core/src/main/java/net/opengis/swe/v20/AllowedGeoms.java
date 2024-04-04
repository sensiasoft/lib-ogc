/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import java.util.List;
import net.opengis.swe.v20.GeometryData.GeomType;


/**
 * POJO class for XML type AllowedGeometryType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AllowedGeoms extends AbstractSWE, DataConstraint
{
    
    /**
     * Gets the list of allowed geometry types
     */
    public List<GeomType> getGeomList();
    
    
    /**
     * Returns number of allowed geometry types
     */
    public int getNumGeomTypes();
    
    
    /**
     * Adds a new geometry type
     */
    public void addGeomType(GeomType value);
}
