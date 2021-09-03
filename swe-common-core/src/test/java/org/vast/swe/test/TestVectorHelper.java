/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import static org.junit.Assert.*;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Vector;
import org.junit.Test;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import org.vast.swe.SWEUtils;
import org.vast.swe.helper.VectorHelper;


public class TestVectorHelper
{
    VectorHelper fac = new VectorHelper();
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);


    @Test
    public void testCreateUnitVector() throws Exception
    {
        Vector v = fac.newUnitVectorXYZ(null, SWEConstants.REF_FRAME_WGS84_ECEF);
        utils.writeComponent(System.out, v, false, true);

        assertEquals(3, v.getNumCoordinates());
        assertEquals(VectorHelper.DEF_UNIT_VECTOR, v.getDefinition());
        assertEquals(SWEConstants.REF_FRAME_WGS84_ECEF, v.getReferenceFrame());

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateLocationVector() throws Exception
    {
        String uom = "m";
        Vector v = fac.newLocationVectorXYZ(null, SWEConstants.REF_FRAME_WGS84_ECEF, uom);
        utils.writeComponent(System.out, v, false, true);

        assertEquals(3, v.getNumCoordinates());
        assertEquals(VectorHelper.DEF_LOCATION, v.getDefinition());
        assertEquals(SWEConstants.REF_FRAME_WGS84_ECEF, v.getReferenceFrame());
        for (ScalarComponent c: v.getCoordinateList())
        {
            assertEquals(uom, ((Quantity)c).getUom().getCode());
            assertEquals(VectorHelper.DEF_DISTANCE, ((Quantity)c).getDefinition());
        }

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateVelocityVector() throws Exception
    {
        String def = SWEHelper.getPropertyUri("TargetVelocity");
        String uom = "m/s";
        Vector v = fac.newVelocityVector(def, SWEConstants.REF_FRAME_WGS84_ECEF, uom);
        utils.writeComponent(System.out, v, false, true);

        assertEquals(3, v.getNumCoordinates());
        assertEquals(def, v.getDefinition());
        assertEquals(SWEConstants.REF_FRAME_WGS84_ECEF, v.getReferenceFrame());
        for (ScalarComponent c: v.getCoordinateList())
        {
            assertEquals(uom, ((Quantity)c).getUom().getCode());
            assertEquals(VectorHelper.DEF_VELOCITY, ((Quantity)c).getDefinition());
        }

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateAccelerationVector() throws Exception
    {
        String def = SWEHelper.getPropertyUri("PlatformAcceleration");
        String uom = "m.s-2";
        Vector v = fac.newAccelerationVector(def, SWEConstants.REF_FRAME_ECI_J2000, uom);
        utils.writeComponent(System.out, v, false, true);

        assertEquals(3, v.getNumCoordinates());
        assertEquals(def, v.getDefinition());
        assertEquals(SWEConstants.REF_FRAME_ECI_J2000, v.getReferenceFrame());
        for (ScalarComponent c: v.getCoordinateList())
        {
            assertEquals(uom, ((Quantity)c).getUom().getCode());
            assertEquals(VectorHelper.DEF_ACCELERATION, ((Quantity)c).getDefinition());
        }

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateMatrix() throws Exception
    {
        Matrix m = fac.newMatrix(3, 3);
        utils.writeComponent(System.out, m, false, true);

        assertEquals(3, m.getElementCount().getData().getIntValue());
        assertEquals(3, ((Matrix)m.getElementType()).getElementCount().getData().getIntValue());

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateMatrixWithSemantics() throws Exception
    {
        String def = SWEHelper.getPropertyUri("MyMatrix");
        Matrix m = fac.newMatrix(def, "#SENSOR_FRAME", 2, 5);
        utils.writeComponent(System.out, m, false, true);

        assertEquals(2, m.getElementCount().getData().getIntValue());
        assertEquals(5, ((Matrix)m.getElementType()).getElementCount().getData().getIntValue());
        assertEquals(def, m.getDefinition());
        assertEquals(VectorHelper.DEF_ROW, m.getElementType().getDefinition());

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateRotationMatrix() throws Exception
    {
        Matrix m = fac.newRotationMatrix("#GIMBAL_FRAME");
        utils.writeComponent(System.out, m, false, true);

        assertEquals(3, m.getElementCount().getData().getIntValue());
        assertEquals(3, ((Matrix)m.getElementType()).getElementCount().getData().getIntValue());
        assertEquals(VectorHelper.DEF_ROT_MATRIX, m.getDefinition());
        assertEquals(VectorHelper.DEF_ROW, m.getElementType().getDefinition());

        System.out.println();
        System.out.println();
    }


    @Test
    public void testCreateVectorWithLocalFrame() throws Exception
    {
        String uom = "m";
        var v = fac.createLocationVectorXYZ(uom)
            .refFrame(SWEConstants.REF_FRAME_WGS84_ECEF)
            .localFrame("#SENSOR_FRAME")
            .build();
        
        utils.writeComponent(System.out, v, false, true);

        assertEquals(3, v.getNumCoordinates());
        assertEquals(VectorHelper.DEF_LOCATION, v.getDefinition());
        assertEquals(SWEConstants.REF_FRAME_WGS84_ECEF, v.getReferenceFrame());
        for (ScalarComponent c: v.getCoordinateList())
        {
            assertEquals(uom, ((Quantity)c).getUom().getCode());
            assertEquals(VectorHelper.DEF_DISTANCE, ((Quantity)c).getDefinition());
        }

        System.out.println();
        System.out.println();
    }
}
