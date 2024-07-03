/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2024 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;


public interface GeometryData extends DataComponent, HasRefFrames, HasConstraints<AllowedGeoms>
{
    enum GeomType {
        Point,
        LineString,
        Polygon
    }
    
    
    /**
     * @return The currently selected geometry type
     */
    public GeomType getGeomType();
    
    
    /**
     * Select the geometry type before generating a datablock for this component
     * @param geomType
     */
    public void setGeomType(GeomType geomType);
    
    
    /**
     * @return The number of dimensions in the geometry (2D, 3D, etc.)
     */
    public int getNumDims();
    
    
    /**
     * Set the number of dimensions for the geometry
     * @param numDims
     */
    public void setNumDims(int numDims);
}
