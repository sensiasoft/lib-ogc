/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import javax.xml.namespace.QName;
import org.vast.swe.SWEConstants;
import com.google.common.collect.ImmutableMap;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.impl.MeasureImpl;


public class SamplingSphere extends SamplingFeature<Point>
{
    private static final long serialVersionUID = 7558030611682120450L;
    public static final String TYPE = SWEConstants.SML_ONTOLOGY_ROOT + "swe/foi/SF_SamplingSphere";
    public static final QName PROP_RADIUS = new QName(SAMS_NS_URI, "radius", SAMS_NS_PREFIX);
    
    protected double radius;
    
    
    public SamplingSphere()
    {
        super(TYPE);
    }
    
    
    /**
     * Sets the sphere radius
     * @param radius in meters
     */
    public void setRadius(double radius)
    {
        properties = null; // reset cached properties map
        this.radius = radius;
    }
    
    
    /**
     * @return The sphere radius in meters
     */
    public double getRadius()
    {
        return radius;
    }
    
    
    @Override
    protected void appendProperties(ImmutableMap.Builder<QName, Object> builder)
    {
        super.appendProperties(builder);
        builder.put(PROP_RADIUS, new MeasureImpl(getRadius(), "m"));
    }
}
