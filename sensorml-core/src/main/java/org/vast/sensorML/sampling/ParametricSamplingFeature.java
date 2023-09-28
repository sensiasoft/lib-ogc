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
import org.vast.ogc.geopose.Pose;
import org.vast.ogc.om.SamplingFeature;
import com.google.common.collect.ImmutableMap;
import net.opengis.gml.v32.AbstractGeometry;


/**
 * <p>
 * Base class for all sampling features that can be positioned relatively to a local CRS,
 * that is either a local tangent plane CRS (e.g. NED, ENU), or w.r.t. a reference frame
 * attached to a moving object such as a platform.
 * </p><p>
 * If the sampling feature also contains a Point geometry, any tangent reference frame
 * used for expressing pose coordinates is assumed to be based at that point location.
 * </p>
 *
 * @param <T> Type of geometry
 * 
 * @author Alex Robin
 * @since Sep 27, 2023
 */
public abstract class ParametricSamplingFeature<T extends AbstractGeometry> extends SamplingFeature<T>
{
    private static final long serialVersionUID = 7083438336435379158L;
    public static final String SML_NS_PREFIX = "sml";
    public static final String SML_NS_URI = "http://www.opengis.net/sensorML/2.0";
    public static final String TYPE_URI_PREFIX = "http://www.opengis.net/def/samplingFeatureType/OGC-SML/2.0/";
    
    public static final QName PROP_POSE = new QName(SML_NS_URI, "pose", SML_NS_PREFIX);
    
    protected Pose pose;
    
    
    public ParametricSamplingFeature(String type)
    {
        super(type);
    }
    
    
    /**
     * Sets the pose of the sampling feature
     * @param pose
     */
    public void setPose(Pose pose)
    {
        properties = null; // reset cached properties map
        this.pose = pose;
    }
    
    
    /**
     * @return The pose of the sampling feature
     */
    public Pose getPose()
    {
        return pose;
    }
    
    
    protected void appendProperties(ImmutableMap.Builder<QName, Object> builder)
    {
        super.appendProperties(builder);
        
        if (pose != null)
            builder.put(PROP_POSE, pose);
    }
}
