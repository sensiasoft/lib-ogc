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
import org.vast.ogc.om.SamplingFeature;
import com.google.common.collect.ImmutableMap;
import net.opengis.gml.v32.Point;


/**
 * <p>
 * Sampling feature used to model a viewing sector in the shape of a spherical
 * sector, defined by min/max radius, min/max elevation and min/max azimuth.
 * 
 * Elevation angles are expressed about the Y axis, while azimuth angles are
 * expressed about the Z axis.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 26, 2023
 */
public class ViewingSector extends ParametricSamplingFeature<Point>
{
    private static final long serialVersionUID = 4835503882088787437L;
    public static final String TYPE = "http://www.opengis.net/def/samplingFeatureType/CS-API/ViewingSector";
    public static final QName PROP_RADIUS = new QName(SAMS_NS_URI, "radius", SAMS_NS_PREFIX);
    public static final QName PROP_INNER_RADIUS = new QName(SAMS_NS_URI, "innerRadius", SAMS_NS_PREFIX);
    public static final QName PROP_MIN_ELEV = new QName(SAMS_NS_URI, "minElev", SAMS_NS_PREFIX);
    public static final QName PROP_MAX_ELEV = new QName(SAMS_NS_URI, "maxElev", SAMS_NS_PREFIX);
    public static final QName PROP_MIN_AZIM = new QName(SAMS_NS_URI, "minAzim", SAMS_NS_PREFIX);
    public static final QName PROP_MAX_AZIM = new QName(SAMS_NS_URI, "maxAzim", SAMS_NS_PREFIX);
    
    protected double radius = 1.0; // meters
    protected double innerRadius = 0; // meters
    protected double minElev = -90; // degrees
    protected double maxElev = 90; // degrees
    protected double minAzim = -180; // degrees
    protected double maxAzim = 180; // degrees
    
    
    public ViewingSector()
    {
        super(TYPE);
    }
    
    
    /**
     * Sets the sphere radius
     * @param val radius in meters
     */
    public void setRadius(double val)
    {
        properties = null; // reset cached properties map
        this.radius = val;
    }
    
    
    /**
     * @return The sphere radius in meters
     */
    public double getRadius()
    {
        return radius;
    }
    
    
    /**
     * Sets the sphere inner radius
     * @param val inner radius in meters
     */
    public void setInnerRadius(double val)
    {
        properties = null; // reset cached properties map
        this.innerRadius = val;
    }
    
    
    /**
     * @return The sphere inner radius in meters
     */
    public double getInnerRadius()
    {
        return innerRadius;
    }
    
    
    /**
     * Sets the minimum elevation angle of the viewing sector
     * @param val elevation in degrees, in the range [-90, 90]
     */
    public void setMinElevation(double val)
    {
        properties = null; // reset cached properties map
        this.minElev = val;
    }
    
    
    /**
     * @return The minimum elevation of the viewing sector in degrees
     */
    public double getMinElevation()
    {
        return minElev;
    }
    
    
    /**
     * Sets the maximum elevation angle of the viewing sector
     * @param val elevation in degrees, in the range [-90, 90]
     */
    public void setMaxElevation(double val)
    {
        properties = null; // reset cached properties map
        this.maxElev = val;
    }
    
    
    /**
     * @return The maximum elevation of the viewing sector in degrees
     */
    public double getMaxElevation()
    {
        return maxElev;
    }
    
    
    /**
     * Sets the minimum azimuth angle of the viewing sector
     * @param val azimuth in degrees, in the range [-180, 360]
     */
    public void setMinAzimuth(double val)
    {
        properties = null; // reset cached properties map
        this.minAzim = val;
    }
    
    
    /**
     * @return The minimum azimuth of the viewing sector in degrees
     */
    public double getMinAzimuth()
    {
        return minAzim;
    }
    
    
    /**
     * Sets the maximum azimuth angle of the viewing sector
     * @param val elevation in degrees, in the range [-180, 360]
     */
    public void setMaxAzimuth(double val)
    {
        properties = null; // reset cached properties map
        this.maxAzim = val;
    }
    
    
    /**
     * @return The maximum azimuth of the viewing sector in degrees
     */
    public double getMaxAzimuth()
    {
        return maxAzim;
    }
    
    
    @Override
    protected void appendProperties(ImmutableMap.Builder<QName, Object> builder)
    {
        super.appendProperties(builder);
        builder.put(PROP_RADIUS, getRadius());
        builder.put(PROP_INNER_RADIUS, getInnerRadius());
        builder.put(PROP_MIN_ELEV, getMinElevation());
        builder.put(PROP_MAX_ELEV, getMaxElevation());
        builder.put(PROP_MIN_AZIM, getMinAzimuth());
        builder.put(PROP_MAX_AZIM, getMaxAzimuth());
    }
}
