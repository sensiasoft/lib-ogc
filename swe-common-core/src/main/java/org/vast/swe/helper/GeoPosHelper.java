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

import java.util.Arrays;
import java.util.List;
import org.vast.swe.SWEBuilders;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.DataRecord;
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
    public static final String DEF_LATITUDE_GEOCENTRIC = SWEHelper.getPropertyUri("GeocentricLatitude");
    public static final String DEF_LONGITUDE = SWEHelper.getPropertyUri("Longitude");
    public static final String DEF_ALTITUDE_ELLIPSOID = SWEHelper.getPropertyUri("EllipsoidalHeight");
    public static final String DEF_ALTITUDE_GEOID = SWEHelper.getPropertyUri("OrthometricHeight");
    public static final String DEF_HEADING_TRUE = SWEHelper.getPropertyUri("TrueHeading");
    public static final String DEF_HEADING_MAGNETIC = SWEHelper.getPropertyUri("MagneticHeading");
    public static final String DEF_YAW = SWEHelper.getPropertyUri("YawAngle");
    public static final String DEF_PITCH = SWEHelper.getPropertyUri("PitchAngle");
    public static final String DEF_ROLL = SWEHelper.getPropertyUri("RollAngle");


    /**
     * Creates a 3D location vector with latitude/longitude/altitude axes (EPSG 4979)
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorLLA(String def)
    {
        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_LOCATION)
            .refFrame(SWEConstants.REF_FRAME_4979)
            .addQuantityCoord("lat")
                .definition(DEF_LATITUDE_GEODETIC)
                .label("Geodetic Latitude")
                .axisId("Lat")
                .uomCode("deg")
                .done()
            .addQuantityCoord("lon")
                .definition(DEF_LONGITUDE)
                .label("Longitude")
                .axisId("Long")
                .uomCode("deg")
                .done()
            .addQuantityCoord("alt")
                .definition(DEF_ALTITUDE_ELLIPSOID)
                .label("Altitude")
                .axisId("h")
                .uomCode("m")
                .done()
            .build();
    }


    /**
     * Creates a 2D location vector with latitude/longitude axes (EPSG 4326)
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorLatLon(String def)
    {
        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_LOCATION)
            .refFrame(SWEConstants.REF_FRAME_4979)
            .addQuantityCoord("lat")
                .definition(DEF_LATITUDE_GEODETIC)
                .label("Geodetic Latitude")
                .axisId("Lat")
                .uomCode("deg")
                .done()
            .addQuantityCoord("lon")
                .definition(DEF_LONGITUDE)
                .label("Longitude")
                .axisId("Long")
                .uomCode("deg")
                .done()
            .build();
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
     * Creates a 3D location vector with ECEF X/Y/Z axes (EPSG 4978)
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @param uomCode unit of distance to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newLocationVectorECEF(String def, String uomCode)
    {
        return newLocationVectorXYZ(def, SWEConstants.REF_FRAME_ECEF, uomCode);
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
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * East-North-Up (ENU) frame (order of rotations is heading/Z, pitch/X, roll/Y in rotating frame)
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_EULER} is used)
     * @param uomCode angular unit to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newEulerOrientationENU(String def, String uomCode)
    {
        if (uomCode == null)
            uomCode = "deg";

        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_ORIENTATION_EULER)
            .description("Euler angles with order of rotation heading/pitch/roll in rotating frame")
            .refFrame(SWEConstants.REF_FRAME_ENU)
            .addQuantityCoord("yaw")
                .label("Heading Angle")
                .description("True heading to north, measured counter clockwise")
                .definition(DEF_HEADING_TRUE)
                .uomCode(uomCode)
                .axisId("Z")
                .done()
            .addQuantityCoord("pitch")
                .label("Pitch Angle")
                .definition(DEF_PITCH)
                .uomCode(uomCode)
                .axisId("X")
                .done()
            .addQuantityCoord("roll")
                .label("Roll Angle")
                .definition(DEF_ROLL)
                .uomCode(uomCode)
                .axisId("Y")
                .done()
            .build();
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
        if (uomCode == null)
            uomCode = "deg";

        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_ORIENTATION_EULER)
            .description("Euler angles with order of rotation heading/pitch/roll in rotating frame")
            .refFrame(SWEConstants.REF_FRAME_NED)
            .addQuantityCoord("yaw")
                .label("Heading Angle")
                .description("True heading from north, measured clockwise")
                .definition(DEF_HEADING_TRUE)
                .uomCode(uomCode)
                .axisId("Z")
                .done()
            .addQuantityCoord("pitch")
                .label("Pitch Angle")
                .definition(DEF_PITCH)
                .uomCode(uomCode)
                .axisId("Y")
                .done()
            .addQuantityCoord("roll")
                .label("Roll Angle")
                .definition(DEF_ROLL)
                .uomCode(uomCode)
                .axisId("X")
                .done()
            .build();
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
        if (uomCode == null)
            uomCode = "deg";

        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_ORIENTATION_EULER)
            .description("Euler angles with order of rotation Z/Y/X in rotating frame")
            .refFrame(SWEConstants.REF_FRAME_NED)
            .addQuantityCoord("rz")
                .label("Z Rotation")
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .axisId("Z")
                .done()
            .addQuantityCoord("ry")
                .label("Y Rotation")
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .axisId("Y")
                .done()
            .addQuantityCoord("rx")
                .label("X Rotation")
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .axisId("X")
                .done()
            .build();
    }


    /**
     * Creates a 4d vector representing an orientation quaternion expressed in the given frame (scalar comes last).
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @param refFrame reference frame with respect to which the coordinates of this quaternion are expressed
     * @return the new Vector component object
     */
    public Vector newQuatOrientation(String def, String refFrame)
    {
        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_ORIENTATION_QUAT)
            .description("Orientation quaternion, usually normalized")
            .refFrame(SWEConstants.REF_FRAME_NED)
            .addQuantityCoord("qx")
                .label("X Component")
                .definition(SWEConstants.DEF_COEF)
                .uomCode("1")
                .axisId("X")
                .done()
            .addQuantityCoord("qy")
                .label("Y Component")
                .definition(SWEConstants.DEF_COEF)
                .uomCode("1")
                .axisId("Y")
                .done()
            .addQuantityCoord("qz")
                .label("Z Component")
                .definition(SWEConstants.DEF_COEF)
                .uomCode("1")
                .axisId("Z")
                .done()
            .addQuantityCoord("q0")
                .label("Scalar Component")
                .definition(SWEConstants.DEF_COEF)
                .uomCode("1")
                .done()
            .build();
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
     * @param def semantic definition of orientation vector (if null, {@link #DEF_ORIENTATION_QUAT} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationNED(String def)
    {
        return newQuatOrientation(def, SWEConstants.REF_FRAME_NED);
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


    ///
    // Methods providing complete output structure including the time tag
    ///

    public enum ImuFields
    {
        GYRO,
        ACCEL,
        MAG
    }

    public DataRecord newImuOutput(String name, String localFrame, ImuFields... imuFields)
    {
        List<ImuFields> fields = Arrays.asList(imuFields);
        DataRecord imuData = SWEBuilders.newDataRecord()
            .name(name)
            .definition(SWEHelper.getPropertyUri("ImuData"))
            .addSamplingTimeIsoUTC("time")
            .build();

        // angular rate vector
        if (fields.contains(ImuFields.GYRO))
        {
            Vector angRate = newAngularVelocityVector(null, localFrame, "deg/s");
            angRate.setDataType(DataType.FLOAT);
            imuData.addComponent("angRate", angRate);
        }

        // acceleration vector
        if (fields.contains(ImuFields.ACCEL))
        {
            Vector accel = newAccelerationVector(null, localFrame, "m/s2");
            accel.setDataType(DataType.FLOAT);
            imuData.addComponent("accel", accel);
        }

        // magnetic field vector
        if (fields.contains(ImuFields.MAG))
        {
            Vector mag = newAngularVelocityVector(null, localFrame, "deg/s");
            mag.setDataType(DataType.FLOAT);
            imuData.addComponent("magField", mag);
        }

        return imuData;
    }
}
