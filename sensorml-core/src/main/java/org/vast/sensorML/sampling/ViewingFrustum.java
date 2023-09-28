/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML.sampling;

import javax.xml.namespace.QName;
import com.google.common.collect.ImmutableMap;
import net.opengis.gml.v32.Point;


public class ViewingFrustum extends ParametricSamplingFeature<Point>
{
    private static final long serialVersionUID = -8288967473198534666L;
    public static final String TYPE = TYPE_URI_PREFIX + "ViewingFrustum";
    
    public static final QName PROP_FOV = new QName(SML_NS_URI, "fov", SML_NS_PREFIX);
    public static final QName PROP_ASPECT_RATIO = new QName(SML_NS_URI, "aspectRatio", SML_NS_PREFIX);
    public static final QName PROP_LENGTH = new QName(SML_NS_URI, "length", SML_NS_PREFIX);
    
    protected double fov = Math.PI/6;
    protected double aspectRatio = 1.0;
    protected double length = 10.0;
    
    
    public ViewingFrustum()
    {
        super(TYPE);
    }
    
    
    /**
     * Sets the FOV of the frustum
     * @param fov in degrees
     */
    public void setFov(double fov)
    {
        properties = null; // reset cached properties map
        this.fov = fov;
    }
    
    
    /**
     * Sets separate horizontal and vertical FOV for the frustum
     * @param hfov in degrees
     * @param vfov in degrees
     */
    public void setFov(double hfov, double vfov)
    {
        properties = null; // reset cached properties map
        this.fov = hfov;
        this.aspectRatio = Math.tan(hfov/2) / Math.tan(vfov/2);
    }
    
    
    /**
     * @return The (horizontal) FOV in degrees
     */
    public double getFov()
    {
        return fov;
    }
    
    
    /**
     * Sets the aspect ratio of the frustum
     * @param val aspect ratio (unitless)
     */
    public void setAspectRatio(double val)
    {
        properties = null; // reset cached properties map
        this.aspectRatio = val;
    }
    
    
    /**
     * @return The aspect ratio of the frustum
     */
    public double getAspectRatio()
    {
        return aspectRatio;
    }
    
    
    /**
     * Sets the length of the frustum
     * @param val length in meters
     */
    public void setLength(double val)
    {
        properties = null; // reset cached properties map
        this.length = val;
    }
    
    
    /**
     * @return The length of the frustum
     */
    public double getLength()
    {
        return length;
    }
    
    
    @Override
    protected void appendProperties(ImmutableMap.Builder<QName, Object> builder)
    {
        super.appendProperties(builder);
        builder.put(PROP_FOV, getFov());
        builder.put(PROP_ASPECT_RATIO, getAspectRatio());
        builder.put(PROP_LENGTH, getLength());
    }
}
