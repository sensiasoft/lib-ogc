/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

The Initial Developer is Botts Innovative Research Inc. Portions created by the Initial
Developer are Copyright (C) 2016 the Initial Developer. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.helper;

import org.vast.swe.SWEBuilders.VectorBuilder;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Helper class to create SWE structures used for geo-positioning<br/>
 * This includes location and attitude but also linear velocity, angular rate,
 * and linear acceleration
 * </p>
 *
 * @author Alex Robin
 * @since March 2016
 */
public class GeoPosHelper extends VectorHelper
{
    public static final String DEF_LATITUDE_GEODETIC = SWEHelper.getPropertyUri("GeodeticLatitude");
    public static final String DEF_LONGITUDE = SWEHelper.getPropertyUri("Longitude");
    public static final String DEF_ALTITUDE_ELLIPSOID = SWEHelper.getPropertyUri("HeightAboveEllipsoid");
    public static final String DEF_ALTITUDE_GEOID = SWEHelper.getPropertyUri("HeightAboveMSL");
    public static final String DEF_HEADING_TRUE = SWEHelper.getPropertyUri("TrueHeading");
    public static final String DEF_HEADING_MAGNETIC = SWEHelper.getPropertyUri("MagneticHeading");
    public static final String DEF_YAW = SWEHelper.getPropertyUri("YawAngle");
    public static final String DEF_PITCH = SWEHelper.getPropertyUri("PitchAngle");
    public static final String DEF_ROLL = SWEHelper.getPropertyUri("RollAngle");


    /**
     * Creates a 3D location vector with latitude/longitude/altitude axes and WGS84 datum (EPSG 4979)
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createLocationVectorLLA()
    {
        return createVector()
            .definition(DEF_LOCATION)
            .refFrame(SWEConstants.REF_FRAME_4979)
            .addCoordinate("lat", createQuantity()
                .definition(DEF_LATITUDE_GEODETIC)
                .label("Geodetic Latitude")
                .axisId("Lat")
                .uomCode("deg")
                .build())
            .addCoordinate("lon", createQuantity()
                .definition(DEF_LONGITUDE)
                .label("Longitude")
                .axisId("Lon")
                .uomCode("deg")
                .build())
            .addCoordinate("alt", createQuantity()
                .definition(DEF_ALTITUDE_ELLIPSOID)
                .label("Ellipsoidal Height")
                .axisId("h")
                .uomCode("m")
                .build());
    }
    
    
    /**
     * Creates a 3D location vector with latitude/longitude/altitude axes and WGS84 datum (EPSG 4979)
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorLLA(String def)
    {
        return createLocationVectorLLA()
            .definition(def != null ? def : DEF_LOCATION)
            .build();
    }


    /**
     * Creates a 2D location vector with latitude/longitude axes (EPSG 4326)
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createLocationVectorLatLon()
    {
        return createVector()
            .definition(DEF_LOCATION)
            .refFrame(SWEConstants.REF_FRAME_4326)
            .addCoordinate("lat", createQuantity()
                .definition(DEF_LATITUDE_GEODETIC)
                .label("Geodetic Latitude")
                .axisId("Lat")
                .uomCode("deg")
                .build())
            .addCoordinate("lon", createQuantity()
                .definition(DEF_LONGITUDE)
                .label("Longitude")
                .axisId("Lon")
                .uomCode("deg")
                .build());
    }
    
    
    /**
     * @see #createLocationVectorLatLon(String)
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorLatLon(String def)
    {
        return createLocationVectorLatLon()
            .definition(def != null ? def : DEF_LOCATION)
            .build();
    }
    
    
    /**
     * Creates a 3D location vector with ECEF X/Y/Z axes (EPSG 4978)
     * @param uomCode unit of distance to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createLocationVectorECEF(String uomCode)
    {
        return createLocationVectorXYZ(uomCode)
            .refFrame(SWEConstants.REF_FRAME_ECEF);
    }


    /**
     * Creates a 3D location vector with ECEF X/Y/Z axes (EPSG 4978)
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @param uomCode unit of distance to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newLocationVectorECEF(String def, String uomCode)
    {
        return newLocationVectorXYZ(def, SWEConstants.REF_FRAME_ECEF, "m");
    }


    /**
     * Creates a 3D location vector with ECEF X/Y/Z axes (EPSG 4978), measured in meters
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorECEF(String def)
    {
        return newLocationVectorECEF(def, "m");
    }
    
    
    /**
     * Creates a 3D velocity vector with ECEF X/Y/Z axes (EPSG 4978)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createVelocityVectorECEF(String uomCode)
    {
        return createVelocityVector(uomCode)
            .refFrame(SWEConstants.REF_FRAME_ECEF);
    }


    /**
     * Creates a 3D velocity with ECEF X/Y/Z axes (EPSG 4978)
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorECEF(String def, String uomCode)
    {
        return newVelocityVector(def, SWEConstants.REF_FRAME_ECEF, uomCode);
    }
    
    
    /**
     * Creates a 3D velocity vector with ENU X/Y/Z axes
     * @param uomCode unit of velocity to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createVelocityVectorENU(String uomCode)
    {
        return createVelocityVector(uomCode)
            .refFrame(SWEConstants.REF_FRAME_ENU);
    }


    /**
     * Creates a 3D velocity with ENU X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorENU(String def, String uomCode)
    {
        return newVelocityVector(def, SWEConstants.REF_FRAME_ENU, uomCode);
    }
    
    
    /**
     * Creates a 3D velocity vector with NED X/Y/Z axes
     * @param uomCode unit of velocity to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createVelocityVectorNED(String uomCode)
    {
        return createVelocityVector(uomCode)
            .refFrame(SWEConstants.REF_FRAME_NED);
    }


    /**
     * Creates a 3D velocity with NED X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorNED(String def, String uomCode)
    {
        return newVelocityVector(def, SWEConstants.REF_FRAME_NED, uomCode);
    }


    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in a local
     * frame (order of rotations is yaw/Z, pitch/X, roll/Y in rotating frame)
     * @param uomCode angular unit to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createEulerOrientationYPR(String uomCode)
    {
        if (uomCode == null)
            uomCode = "deg";

        return createVector()
            .definition(DEF_ORIENTATION_EULER)
            .description("Euler angles with order of rotation yaw/pitch/roll in rotating frame")
            .dataType(DataType.FLOAT)
            .addCoordinate("yaw", createQuantity()
                .definition(DEF_YAW)
                .label("Yaw Angle")
                .uomCode(uomCode)
                .axisId("Z")
                .build())
            .addCoordinate("pitch", createQuantity()
                .definition(DEF_PITCH)
                .label("Pitch Angle")
                .uomCode(uomCode)
                .axisId("X")
                .build())
            .addCoordinate("roll", createQuantity()
                .definition(DEF_ROLL)
                .label("Roll Angle")
                .uomCode(uomCode)
                .axisId("Y")
                .build());
    }


    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * East-North-Up (ENU) frame (order of rotations is heading/Z, pitch/X, roll/Y in rotating frame)
     * @param uomCode angular unit to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createEulerOrientationENU(String uomCode)
    {
        if (uomCode == null)
            uomCode = "deg";

        return createVector()
            .definition(DEF_ORIENTATION_EULER)
            .description("Euler angles with order of rotation heading/pitch/roll in rotating frame")
            .refFrame(SWEConstants.REF_FRAME_ENU)
            .dataType(DataType.FLOAT)
            .addCoordinate("heading", createQuantity()
            	.definition(DEF_HEADING_TRUE)
                .label("Heading Angle")
                .description("Heading angle from east direction, measured counter clockwise")
                .uomCode(uomCode)
                .axisId("Z")
                .build())
            .addCoordinate("pitch", createQuantity()
            	.definition(DEF_PITCH)
                .label("Pitch Angle")
                .description("Rotation around the lateral axis, up/down from the local horizontal plane (positive when pointing up)")
                .uomCode(uomCode)
                .axisId("X")
                .build())
            .addCoordinate("roll", createQuantity()
            	.definition(DEF_ROLL)
                .label("Roll Angle")
                .description("Rotation around the longitudinal axis")
                .uomCode(uomCode)
                .axisId("Y")
                .build());
    }


    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * East-North-Up (ENU) frame (order of rotations is heading/Z, pitch/X, roll/Y in rotating frame)
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_EULER} is used)
     * @param uomCode angular unit to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newEulerOrientationENU(String def, String uomCode)
    {
        return createEulerOrientationENU(uomCode)
            .definition(def != null ? def : DEF_ORIENTATION_EULER)
            .build();
    }


    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * North-East-Down (NED) frame (order of rotations is heading/Z, pitch/Y, roll/X in rotating frame)
     * @param uomCode angular unit to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createEulerOrientationNED(String uomCode)
    {
        if (uomCode == null)
            uomCode = "deg";

        return createVector()
            .definition(DEF_ORIENTATION_EULER)
            .description("Euler angles with order of rotation heading/pitch/roll in rotating frame")
            .refFrame(SWEConstants.REF_FRAME_NED)
            .dataType(DataType.FLOAT)
            .addCoordinate("heading", createQuantity()
            	.definition(DEF_HEADING_TRUE)
                .label("Heading Angle")
                .description("Heading angle from true north, measured clockwise")
                .uomCode(uomCode)
                .axisId("Z")
                .build())
            .addCoordinate("pitch", createQuantity()
            	.definition(DEF_PITCH)
                .label("Pitch Angle")
                .description("Rotation around the lateral axis, up/down from the local horizontal plane (positive when pointing up)")
                .uomCode(uomCode)
                .axisId("Y")
                .build())
            .addCoordinate("roll", createQuantity()
            	.definition(DEF_ROLL)
                .label("Roll Angle")
                .description("Rotation around the longitudinal axis")
                .uomCode(uomCode)
                .axisId("X")
                .build());
    }


    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * North-East-Down (NED) frame (order of rotations is heading/Z, pitch/Y, roll/X in rotating frame)
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_EULER} is used)
     * @param uomCode angular unit to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newEulerOrientationNED(String def, String uomCode)
    {
        return createEulerOrientationNED(uomCode)
            .definition(def != null ? def : DEF_ORIENTATION_EULER)
            .build();
    }


    /**
     * Default version of {@link #newEulerOrientationNED(String, String)} with
     * units set to degrees
     * @param def
     * @return the new Vector component object
     */
    public Vector newEulerOrientationNED(String def)
    {
        return newEulerOrientationNED(def, "deg");
    }


    /**
     * Creates an orientation vector component composed of 3 Euler angles expressed in
     * Earth-Centered-Earth-Fixed (ECEF) frame (order of rotations is X, Y, Z)
     * @param uomCode angular unit to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createEulerOrientationECEF(String uomCode)
    {
        if (uomCode == null)
            uomCode = "deg";

        return createVector()
            .definition(DEF_ORIENTATION_EULER)
            .description("Euler angles with order of rotation Z/Y/X in rotating frame")
            .refFrame(SWEConstants.REF_FRAME_NED)
            .dataType(DataType.FLOAT)
            .addCoordinate("rz", createQuantity()
            	.definition(DEF_ANGLE)
                .label("Z Rotation")
                .uomCode(uomCode)
                .axisId("Z")
                .build())
            .addCoordinate("ry", createQuantity()
            	.definition(DEF_ANGLE)
                .label("Y Rotation")
                .uomCode(uomCode)
                .axisId("Y")
                .build())
            .addCoordinate("rx", createQuantity()
            	.definition(DEF_ANGLE)
                .label("X Rotation")
                .uomCode(uomCode)
                .axisId("X")
                .build());
    }


    /**
     * Creates an orientation vector component composed of 3 Euler angles expressed in
     * Earth-Centered-Earth-Fixed (ECEF) frame (order of rotations is X, Y, Z)
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_EULER} is used)
     * @param uomCode angular unit to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newEulerOrientationECEF(String def, String uomCode)
    {
        return createEulerOrientationECEF(uomCode)
            .definition(def != null ? def : DEF_ORIENTATION_EULER)
            .build();
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in the given frame (scalar comes last).
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @param refFrame reference frame with respect to which the coordinates of this quaternion are expressed
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createQuatOrientation()
    {
        return createVector()
            .definition(DEF_ORIENTATION_QUAT)
            .description("Orientation quaternion, usually normalized")
            .dataType(DataType.FLOAT)
            .addCoordinate("qx", createQuantity()
            	.definition(SWEConstants.DEF_COEF)
                .label("X Component")
                .uomCode("1")
                .axisId("X")
                .build())
            .addCoordinate("qy", createQuantity()
            	.definition(SWEConstants.DEF_COEF)
                .label("Y Component")
                .uomCode("1")
                .axisId("Y")
                .build())
            .addCoordinate("qz", createQuantity()
            	.definition(SWEConstants.DEF_COEF)
                .label("Z Component")
                .uomCode("1")
                .axisId("Z")
                .build())
            .addCoordinate("q0", createQuantity()
            	.definition(SWEConstants.DEF_COEF)
                .label("Scalar Component")
                .uomCode("1")
                .build());
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in the given frame (scalar comes last).
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @param refFrame reference frame with respect to which the coordinates of this quaternion are expressed
     * @return the new Vector component object
     */
    public Vector newQuatOrientation(String def, String refFrame)
    {
        return createQuatOrientation()
            .definition(def != null ? def : DEF_ORIENTATION_QUAT)
            .refFrame(refFrame)
            .build();            
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in ENU frame.
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createQuatOrientationENU()
    {
        return createQuatOrientation()
            .refFrame(SWEConstants.REF_FRAME_ENU);
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in ENU frame.
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationENU(String def)
    {
        return newQuatOrientation(def, SWEConstants.REF_FRAME_ENU);
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in NED frame.
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createQuatOrientationNED()
    {
        return createQuatOrientation()
            .refFrame(SWEConstants.REF_FRAME_NED);
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in NED frame.
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationNED(String def)
    {
        return newQuatOrientation(def, SWEConstants.REF_FRAME_NED);
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in ECEF frame.
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createQuatOrientationECEF()
    {
        return createQuatOrientation()
            .refFrame(SWEConstants.REF_FRAME_ECEF);
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in ECEF frame.
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationECEF(String def)
    {
        return newQuatOrientation(def, SWEConstants.REF_FRAME_ECEF);
    }
}
