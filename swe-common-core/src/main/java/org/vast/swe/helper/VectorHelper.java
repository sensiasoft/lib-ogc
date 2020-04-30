/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.helper;

import org.vast.swe.SWEBuilders;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Helper class to create SWE structures used in vector math
 * </p>
 *
 * @author Alex Robin
 * @since March 2016
 */
public class VectorHelper extends SWEHelper
{
    public static final String DEF_UNIT_VECTOR = SWEHelper.getPropertyUri("UnitVector");
    public static final String DEF_ROW = SWEHelper.getPropertyUri("Row");
    public static final String DEF_COORD = SWEHelper.getPropertyUri("Coordinate");
    public static final String DEF_MATRIX = SWEHelper.getPropertyUri("Matrix");
    public static final String DEF_ROT_MATRIX = SWEHelper.getPropertyUri("RotationMatrix");
    public static final String DEF_ORIENTATION_EULER = SWEHelper.getPropertyUri("Orientation/EulerAngles");
    public static final String DEF_ORIENTATION_QUAT = SWEHelper.getPropertyUri("Orientation/Quaternion");

    public static final String DEF_LOCATION = SWEHelper.getPropertyUri("Location");
    public static final String DEF_DISTANCE = SWEHelper.getPropertyUri("Distance");
    public static final String DEF_VELOCITY = SWEHelper.getPropertyUri("LinearVelocity");
    public static final String DEF_ACCELERATION = SWEHelper.getPropertyUri("LinearAcceleration");
    public static final String DEF_ANGULAR_RATE = SWEHelper.getPropertyUri("AngularRate");
    public static final String DEF_ANGLE = SWEHelper.getPropertyUri("Angle");


    protected Vector newVector3D()
    {
        return newVector3(null, null);
    }


    /**
     * Creates a 3D vector with arbitrary axes called u1, u2, u3
     * @param def semantic definition of velocity vector (must be set)
     * @param refFrame reference frame within which the vector is expressed
     * @return the new Vector component object
     */
    public Vector newVector3(String def, String refFrame)
    {
        return SWEBuilders.newVector()
            .definition(def)
            .refFrame(refFrame)
            .addQuantityCoord("u1")
                .definition(DEF_COORD)
                .uomCode("1")
                .done()
            .addQuantityCoord("u2")
                .definition(DEF_COORD)
                .uomCode("1")
                .done()
            .addQuantityCoord("u3")
                .definition(DEF_COORD)
                .uomCode("1")
                .done()
            .build();

    }


    /**
     * Creates a 3D unit vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link #DEF_UNIT_VECTOR} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @return the new Vector component object
     */
    public Vector newUnitVectorXYZ(String def, String refFrame)
    {
        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_UNIT_VECTOR)
            .refFrame(refFrame)
            .addQuantityCoord("ux")
                .definition(DEF_COORD)
                .axisId("X")
                .uomCode("1")
                .done()
            .addQuantityCoord("uy")
                .definition(DEF_COORD)
                .axisId("Y")
                .uomCode("1")
                .done()
            .addQuantityCoord("uz")
                .definition(DEF_COORD)
                .axisId("Z")
                .uomCode("1")
                .done()
            .build();
    }


    /**
     * Creates a 3D location vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of location vector (if null, {@link #DEF_LOCATION} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of distance to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newLocationVectorXYZ(String def, String refFrame, String uomCode)
    {
        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_LOCATION)
            .refFrame(refFrame)
            .addQuantityCoord("x")
                .definition(DEF_DISTANCE)
                .label("X Pos")
                .axisId("X")
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("y")
                .definition(DEF_DISTANCE)
                .label("Y Pos")
                .axisId("Y")
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("z")
                .definition(DEF_DISTANCE)
                .label("Z Pos")
                .axisId("Z")
                .uomCode(uomCode)
                .done()
            .build();
    }


    /**
     * Creates a 3D velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@link #DEF_VELOCITY} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVector(String def, String refFrame, String uomCode)
    {
        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_VELOCITY)
            .refFrame(refFrame)
            .addQuantityCoord("vx")
                .definition(DEF_VELOCITY)
                .label("X Velocity")
                .axisId("X")
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("vy")
                .definition(DEF_VELOCITY)
                .label("Y Velocity")
                .axisId("Y")
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("vz")
                .definition(DEF_VELOCITY)
                .label("Z Velocity")
                .axisId("Z")
                .uomCode(uomCode)
                .done()
            .build();
    }


    /**
     * Creates a 3D acceleration vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of acceleration vector (if null, {@link #DEF_ACCELERATION} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of acceleration to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newAccelerationVector(String def, String refFrame, String uomCode)
    {
        if (def == null)
            def = DEF_ACCELERATION;

        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_ACCELERATION)
            .refFrame(refFrame)
            .addQuantityCoord("ax")
                .definition(DEF_ACCELERATION)
                .label("X Accel")
                .axisId("X")
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("ay")
                .definition(DEF_ACCELERATION)
                .label("Y Accel")
                .axisId("Y")
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("az")
                .definition(DEF_ACCELERATION)
                .label("Z Accel")
                .axisId("Z")
                .uomCode(uomCode)
                .done()
            .build();
    }


    /**
     * Creates a 3D euler angles vector with unspecified order of rotations about X/Y/Z axes.<br/>
     * This is only used when order and axes of rotations are specified separately.
     * @param refFrame reference frame within which the angles are expressed
     * @param uomCode angular unit to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newEulerAngles(String refFrame, String uomCode)
    {
        return SWEBuilders.newVector()
            .definition(DEF_ORIENTATION_EULER)
            .refFrame(refFrame)
            .addQuantityCoord("r1")
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("r2")
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .done()
            .addQuantityCoord("r3")
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .done()
            .build();
    }


    /**
     * Creates a 3D angular velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of angular velocity vector (if null, {@link #DEF_ANGULAR_RATE} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of acceleration to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newAngularVelocityVector(String def, String refFrame, String uomCode)
    {
        if (uomCode == null)
            uomCode = "deg/s";

        return SWEBuilders.newVector()
            .definition(def != null ? def : DEF_ANGULAR_RATE)
            .refFrame(SWEConstants.REF_FRAME_NED)
            .addQuantityCoord("wx")
                .label("X Angular Rate")
                .definition(DEF_ANGULAR_RATE)
                .uomCode(uomCode)
                .axisId("X")
                .done()
            .addQuantityCoord("wy")
                .label("Y Angular Rate")
                .definition(DEF_ANGULAR_RATE)
                .uomCode(uomCode)
                .axisId("Y")
                .done()
            .addQuantityCoord("wz")
                .label("Z Angular Rate")
                .definition(DEF_ANGULAR_RATE)
                .uomCode(uomCode)
                .axisId("Z")
                .done()
            .build();
    }


    /**
     * Creates a matrix (i.e. 2D tensor) with no definition nor reference frame
     * @param nRows number of rows
     * @param nCols number of columns
     * @return the new Matrix component object
     */
    public Matrix newMatrix(int nRows, int nCols)
    {
        return newMatrix(null, null, nRows, nCols);
    }


    /**
     * Creates a matrix (i.e. 2D tensor)
     * @param def semantic definition of matrix (e.g. rotation matrix, stress matrix, etc.)
     * @param refFrame reference frame within which the matrix is expressed, if applicable, null otherwise
     * @param nRows number of rows
     * @param nCols number of columns
     * @return the new Matrix component object
     */
    public Matrix newMatrix(String def, String refFrame, int nRows, int nCols)
    {
        Matrix m = SWEBuilders.newMatrix()
            .definition(def)
            .refFrame(refFrame)
            .size(nRows, nCols, true)
            .withQuantityElement("coef")
                .definition(SWEConstants.DEF_COEF)
                .uomCode("1")
                .done()
            .build();

        m.getElementType().setDefinition(DEF_ROW);
        return m;
    }


    /**
     * Creates a 3x3 rotation matrix
     * @param refFrame reference frame within which the rotation matrix is expressed
     * @return the new Matrix component object
     */
    public Matrix newRotationMatrix(String refFrame)
    {
        return newMatrix(DEF_ROT_MATRIX, refFrame, 3, 3);
    }
}
