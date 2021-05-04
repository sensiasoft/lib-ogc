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

import org.vast.swe.SWEBuilders.VectorBuilder;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import net.opengis.swe.v20.DataType;
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
    public static final String DEF_COORD = SWEHelper.getPropertyUri("Coordinate");
    public static final String DEF_ROT_MATRIX = SWEHelper.getPropertyUri("RotationMatrix");
    public static final String DEF_ORIENTATION_EULER = SWEHelper.getPropertyUri("EulerAngles");
    public static final String DEF_ORIENTATION_QUAT = SWEHelper.getPropertyUri("RotationQuaternion");
    public static final String DEF_UNIT_VECTOR = SWEHelper.getDBpediaUri("Unit_vector");
    public static final String DEF_ROW = SWEHelper.getDBpediaUri("Row_vector");

    public static final String DEF_LOCATION = SWEHelper.getPropertyUri("LocationVector");
    public static final String DEF_DISTANCE = SWEHelper.getQudtUri("Distance");
    public static final String DEF_VELOCITY = SWEHelper.getQudtUri("LinearVelocity");
    public static final String DEF_ACCELERATION = SWEHelper.getQudtUri("LinearAcceleration");
    public static final String DEF_ANGULAR_VELOCITY = SWEHelper.getQudtUri("AngularVelocity");
    public static final String DEF_ANGULAR_ACCEL = SWEHelper.getQudtUri("AngularAcceleration");
    public static final String DEF_ANGLE = SWEHelper.getQudtUri("PlaneAngle");


    protected Vector newVector3D()
    {
        return newVector3(null, null);
    }


    /**
     * Creates a 3D vector with arbitrary axes called u1, u2, u3
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createVector3()
    {
        return createVector()
            .definition(DEF_UNIT_VECTOR)
            .addCoordinate("u1", createQuantity()
                .definition(DEF_COORD)
                .uomCode("1")
                .build())
            .addCoordinate("u2", createQuantity()
                .definition(DEF_COORD)
                .uomCode("1")
                .build())
            .addCoordinate("u3", createQuantity()
                .definition(DEF_COORD)
                .uomCode("1")
                .build());
    }
    
    
    /**
     * Creates a 3D vector with arbitrary axes called u1, u2, u3
     * @param def semantic definition of velocity vector (must be set)
     * @param refFrame reference frame within which the vector is expressed
     * @return the new Vector component object
     */
    public Vector newVector3(String def, String refFrame)
    {
        return createVector3()
            .definition(def)
            .refFrame(refFrame)
            .build();
    }


    /**
     * Creates a 3D unit vector in an ortho-normal frame with X/Y/Z axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createUnitVectorXYZ()
    {
        return createVector()
            .definition(DEF_UNIT_VECTOR)
            .addCoordinate("ux", createQuantity()
                .definition(DEF_COORD)
                .axisId("X")
                .uomCode("1")
                .build())
            .addCoordinate("uy", createQuantity()
                .definition(DEF_COORD)
                .axisId("Y")
                .uomCode("1")
                .build())
            .addCoordinate("uz", createQuantity()
                .definition(DEF_COORD)
                .axisId("Z")
                .uomCode("1")
                .build());
    }


    /**
     * Creates a 3D unit vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of vector (if null, {@link #DEF_UNIT_VECTOR} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @return the new Vector component object
     */
    public Vector newUnitVectorXYZ(String def, String refFrame)
    {
        return createUnitVectorXYZ()
            .definition(def != null ? def : DEF_UNIT_VECTOR)
            .refFrame(refFrame)
            .build();
    }
    
    
    /**
     * Creates a 3D location vector in an ortho-normal frame with X/Y/Z axes
     * @param uomCode unit of distance to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createLocationVectorXYZ(String uomCode)
    {
        return createVector()
            .definition(DEF_LOCATION)
            .addCoordinate("x", createQuantity()
                .definition(DEF_DISTANCE)
                .label("X Pos")
                .axisId("X")
                .uomCode(uomCode)
                .build())
            .addCoordinate("y", createQuantity()
                .definition(DEF_DISTANCE)
                .label("Y Pos")
                .axisId("Y")
                .uomCode(uomCode)
                .build())
            .addCoordinate("z", createQuantity()
                .definition(DEF_DISTANCE)
                .label("Z Pos")
                .axisId("Z")
                .uomCode(uomCode)
                .build());
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
        return createLocationVectorXYZ(uomCode)
            .definition(def != null ? def : DEF_LOCATION)
            .refFrame(refFrame)
            .build();
    }


    /**
     * Creates a 3D velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param uomCode unit of velocity to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createVelocityVector(String uomCode)
    {
        return createVector()
            .definition(DEF_VELOCITY)
            .dataType(DataType.FLOAT)
            .addCoordinate("vx", createQuantity()
                .definition(DEF_VELOCITY)
                .label("X Velocity")
                .axisId("X")
                .uomCode(uomCode)
                .build())
            .addCoordinate("vy", createQuantity()
                .definition(DEF_VELOCITY)
                .label("Y Velocity")
                .axisId("Y")
                .uomCode(uomCode)
                .build())
            .addCoordinate("vz", createQuantity()
                .definition(DEF_VELOCITY)
                .label("Z Velocity")
                .axisId("Z")
                .uomCode(uomCode)
                .build());
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
        return createVelocityVector(uomCode)
            .definition(def != null ? def : DEF_VELOCITY)
            .refFrame(refFrame)
            .build();
    }


    /**
     * Creates a 3D acceleration vector in an ortho-normal frame with X/Y/Z axes
     * @param uomCode unit of acceleration to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createAccelerationVector(String uomCode)
    {
        return createVector()
            .definition(DEF_ACCELERATION)
            .addCoordinate("ax", createQuantity()
                .definition(DEF_ACCELERATION)
                .label("X Accel")
                .axisId("X")
                .uomCode(uomCode)
                .build())
            .addCoordinate("ay", createQuantity()
                .definition(DEF_ACCELERATION)
                .label("Y Accel")
                .axisId("Y")
                .uomCode(uomCode)
                .build())
            .addCoordinate("az", createQuantity()
                .definition(DEF_ACCELERATION)
                .label("Z Accel")
                .axisId("Z")
                .uomCode(uomCode)
                .build());
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
        return createAccelerationVector(uomCode)
            .definition(def != null ? def : DEF_ACCELERATION)
            .refFrame(refFrame)
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
        return createVector()
            .definition(DEF_ORIENTATION_EULER)
            .refFrame(refFrame)
            .dataType(DataType.FLOAT)
            .addCoordinate("r1", createQuantity()
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .build())
            .addCoordinate("r2", createQuantity()
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .build())
            .addCoordinate("r3", createQuantity()
                .definition(DEF_ANGLE)
                .uomCode(uomCode)
                .build())
            .build();
    }


    /**
     * Creates a 3D angular velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param uomCode unit of acceleration to use on all 3 axes
     * @return A builder to set other options and build the final vector
     */
    public VectorBuilder createAngularVelocityVector(String uomCode)
    {
        if (uomCode == null)
            uomCode = "deg/s";

        return createVector()
            .definition(DEF_ANGULAR_VELOCITY)
            .dataType(DataType.FLOAT)
            .addCoordinate("wx", createQuantity()
                .definition(DEF_ANGULAR_VELOCITY)
                .label("X Angular Rate")
                .uomCode(uomCode)
                .axisId("X")
                .build())
            .addCoordinate("wy", createQuantity()
                .definition(DEF_ANGULAR_VELOCITY)
                .label("Y Angular Rate")
                .uomCode(uomCode)
                .axisId("Y")
                .build())
            .addCoordinate("wz", createQuantity()
                .definition(DEF_ANGULAR_VELOCITY)
                .label("Z Angular Rate")
                .uomCode(uomCode)
                .axisId("Z")
                .build());
    }


    /**
     * Creates a 3D angular velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of angular velocity vector (if null, {@link #DEF_ANGULAR_VELOCITY} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of acceleration to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newAngularVelocityVector(String def, String refFrame, String uomCode)
    {
        return createAngularVelocityVector(uomCode)
            .definition(def != null ? def : DEF_ANGULAR_VELOCITY)
            .refFrame(refFrame)
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
        Matrix m = createMatrix()
            .size(nRows, nCols, true)
            .withElement("elt", createQuantity()
                .definition(SWEConstants.DEF_COEF)
                .uomCode("1")
                .build())
            .build();

        m.getElementType().setDefinition(DEF_ROW);
        return m;
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
        Matrix m = createMatrix()
            .definition(def)
            .refFrame(refFrame)
            .size(nRows, nCols, true)
            .withElement("elt", createQuantity()
                .definition(SWEConstants.DEF_COEF)
                .uomCode("1")
                .build())
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
