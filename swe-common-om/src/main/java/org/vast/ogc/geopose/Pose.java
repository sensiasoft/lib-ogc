/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.geopose;

import java.io.Serializable;
import org.vast.ogc.geopose.PoseImpl.PoseBuilder;


/**
 * Interface for (Geo)Pose implementations.
 * 
 * A pose consists of a position (either geographic or cartesian) and an
 * orientation. Orientation is always provided w.r.t a cartesian reference
 * frame that can be either a topocentric LTP reference frame, or a local
 * engineering reference frame (e.g. platform or sensor frame) 
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface Pose extends Serializable
{
    
    /**
     * Gets the URI of the reference frame with respect to which the pose is provided
     * @return the pose reference frame
     */
    public String getReferenceFrame();
    
    
    /**
     * Sets the pose reference frame
     * @param uri
     */
    public void setReferenceFrame(String uri);
    
    
    /**
     * Gets the URI of the local tangent plane (LTP) reference frame (topocentric frame).
     * This is only needed if the main reference frame is a geographic or other planetary CRS
     * and orientation is provided relative to the LTP frame.
     * @return the LTP reference frame
     */
    public String getLTPReferenceFrame();
    
    
    /**
     * Sets the LTP reference frame
     * @param uri
     */
    public void setLTPReferenceFrame(String uri);
    
    
    /**
     * Gets the URI of the local frame whose pose is expressed by this object
     * @return the pose local frame
     */
    public String getLocalFrame();
    
    
    /**
     * Sets the local frame whose pose is expressed by this object
     * @param uri
     */
    public void setLocalFrame(String uri);
    
    
    /**
     * Gets the position coordinates (2D or 3D)
     * @return The position coordinates in the order specified by the reference frame
     */
    public double[] getPosition();
    
    
    /**
     * Sets the position coordinates  (2D or 3D)
     * @param pos position coordinates in the order specified by the reference frame
     */
    public void setPosition(double[] pos);
    
    
    /**
     * Gets the orientation coordinates
     * <li>If 3 coordinates are provided, yaw/pitch/roll Euler angles in degrees about the
     * local (rotated) axes z, y, and x, applied in that order, are assumed.</li>
     * <li>If 4 coordinates are provided, unit quaternion coordinates in the order x,y,z,w
     * are assumed.</li>
     */
    public double[] getOrientation();
    
    
    /**
     * Sets the orientation coordinates (3 components for Euler angles, 4 for quaternions)
     * @param pos euler angles or quaternion coordinates
     * @see #getOrientation() for the order and units of the coordinates
     */
    public void setOrientation(double[] coords);
    
    
    /**
     * @return a builder to build a new Pose object using the default implementation
     */
    public static PoseBuilder create()
    {
        return new PoseBuilder();
    }
    
}
