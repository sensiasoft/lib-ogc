/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML;

import static org.junit.Assert.*;
import org.junit.Test;
import org.vast.ogc.geopose.Pose;
import org.vast.ogc.geopose.PoseImpl;
import org.vast.sensorML.SMLBuilders.PhysicalSystemBuilder;
import org.vast.sensorML.helper.CommonIdentifiers;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import org.vast.swe.helper.GeoPosHelper;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.PhysicalSystem;
import net.opengis.sensorml.v20.Term;
import net.opengis.sensorml.v20.impl.SpatialFrameImpl;


public class TestSMLHelper
{
    SWEHelper swe = new SWEHelper();
    SMLHelper sml = new SMLHelper();
    
    String TEST_SYS_UID = "urn:osh:test:system:001";
    String TEST_SYS_DESC = "This gamma sensor is deployed in my garden to warn me of imminent radiation exposure";
    String TEST_SYS_NAME = "My Gamma Sensor";
    
    
    void printAsXml(AbstractProcess p) throws Exception
    {
        new SMLUtils(SMLUtils.V2_0).writeProcess(System.out, p, true);
        System.out.println();
        System.out.println();
    }
    
    
    PhysicalSystemBuilder createSystemBuilder()
    {
        return sml.createPhysicalSystem()
            .uniqueID(TEST_SYS_UID)
            .name(TEST_SYS_NAME)
            .description(TEST_SYS_DESC);
    }
    
    
    @Test
    public void testCreateSimpleSystem() throws Exception
    {
        String TYPEOF_URL = "http://www.sensorml.com/sensorML-2.0/examples/xml/gamma2070.xml";
        String TYPEOF_UID = "urn:heath:gamma2070";
        
        PhysicalSystem sys = createSystemBuilder()
            .typeOf(TYPEOF_URL, TYPEOF_UID)
            .build();
        
        printAsXml(sys);
        
        assertEquals(TEST_SYS_UID, sys.getUniqueIdentifier());
        assertEquals(TEST_SYS_NAME, sys.getName());
        assertEquals(TEST_SYS_DESC, sys.getDescription());
        assertEquals(TYPEOF_URL, sys.getTypeOf().getHref());
        assertEquals(TYPEOF_UID, sys.getTypeOf().getTitle());
    }
    
    
    @Test
    public void testAddidentifiers() throws Exception
    {
        String SERIAL_NUM = "FG4578002-D";
        String MODEL_NUM = "GM2070";
        Term identifier;
        
        PhysicalSystem sys = createSystemBuilder()
            .addIdentifier(sml.identifiers.serialNumber(SERIAL_NUM))
            .addIdentifier(sml.identifiers.modelNumber(MODEL_NUM))
            .build();
        
        printAsXml(sys);
                
        identifier = sys.getIdentificationList().get(0).getIdentifierList().get(0);
        assertEquals(CommonIdentifiers.SERIAL_NUMBER_DEF, identifier.getDefinition());
        assertEquals(SERIAL_NUM, identifier.getValue());
        
        identifier = sys.getIdentificationList().get(0).getIdentifierList().get(1);
        assertEquals(CommonIdentifiers.MODEL_NUMBER_DEF, identifier.getDefinition());
        assertEquals(MODEL_NUM, identifier.getValue());
    }
    
    
    @Test
    public void testRefFrameAndPosition() throws Exception
    {
        var vec = new GeoPosHelper();
        
        var uavFrame = new SpatialFrameImpl();
        uavFrame.setId("PLATFORM_FRAME");
        uavFrame.setLabel("UAV Platform Reference Frame");
        uavFrame.setOrigin("Center of gravity of the aircraft");
        uavFrame.addAxis("X", "Along the longitudinal axis of the aircraft, parallel to the fuselage reference line, directed forward (roll axis)");
        uavFrame.addAxis("Y", "Parallel to the line drawn from wingtip to wingtip, directed to the right when looking forward in the aircraft (pitch axis)");
        uavFrame.addAxis("Z", "Directed towards the bottom of the aircraft, perpendicular to the wings and to the fuselage reference line (yaw axis)");
        
        var gimbalFrame = new SpatialFrameImpl();
        gimbalFrame.setId("GIMBAL_FRAME");
        gimbalFrame.setLabel("Gimbal Mounting Reference Frame");
        gimbalFrame.setOrigin("Center of rotation of the gimbal");
        gimbalFrame.addAxis("X", "Toward the yellow X marking located on the external housing of the gimbal");
        gimbalFrame.addAxis("Y", "Toward the yellow Y marking located on the external housing of the gimbal");
        gimbalFrame.addAxis("Z", "Toward the yellow Z marking located on the external housing of the gimbal");
        
        var cameraFrame = new SpatialFrameImpl();
        cameraFrame.setId("SENSOR_FRAME");
        cameraFrame.setLabel("Camera Reference Frame");
        cameraFrame.setOrigin("Optical center of the camera");
        cameraFrame.addAxis("X", "In the plane orthogonal to the optical axis, pointing to the right of camera (in the ideal pinhole model, parallel to an image row");
        cameraFrame.addAxis("Y", "In the plane orthogonal to the optical axis, pointing up (in the ideal pinhole model, parallel to an image column");
        cameraFrame.addAxis("Z", "Along the optical axis of the camera, pointing in the view direction of the camera");
        
        var sys = sml.createPhysicalSystem()
            .name("Predator UAV")
            .addLocalReferenceFrame(uavFrame)
            /*.position(vec.createRecord()
                .addField("location", vec.createLocationVectorLLA()
                    .definition(SWEConstants.DEF_PLATFORM_LOC)
                    .values(new double[] {-86.5861, 34.7304, 3045}))
                .addField("attitude", vec.createEulerOrientationNED("deg")
                    .definition(SWEConstants.DEF_PLATFORM_ORIENT)
                    .values(new double[] {25.3, -1.3, -4.6}))
                .build())*/
            .position(Pose.create()
                .llaPos(34.7304, -86.5861, 3045)
                .ltpReferenceFrame(SWEConstants.REF_FRAME_NED)
                .eulerAngles(25.3, -1.3, -4.6)
                .build())
            .addComponent("gimbal", sml.createPhysicalComponent()
                .name("Camera Gimbal")
                .addLocalReferenceFrame(gimbalFrame)
                /*.position(vec.createRecord()
                    .addField("location", vec.createLocationVectorXYZ("m")
                        .refFrame("#PLATFORM_FRAME")
                        .values(new double[] {0, 2.3, -0.7}))
                    .addField("orientation", vec.createEulerOrientationYPR("deg")
                        .refFrame("#PLATFORM_FRAME")
                        .values(new double[] {0, 0, 0}))
                    .build())*/
                .position(Pose.create()
                    .referenceFrame("#PLATFORM_FRAME")
                    .xyzPos(0, 2.3, -0.7)
                    .eulerAngles(0, 0, 0)
                    .build())
                .build())
            .addComponent("camera_sensor", sml.createPhysicalComponent()
                .name("Camera Sensor")
                .addLocalReferenceFrame(cameraFrame)
                /*.position(vec.createRecord()
                    .addField("location", vec.createLocationVectorXYZ("m")
                        .definition(SWEConstants.DEF_SENSOR_LOC)
                        .refFrame("#GIMBAL_FRAME")
                        .values(new double[] {0, 0, 0.05}))
                    .addField("orientation", vec.createEulerOrientationYPR("deg")
                        .definition(SWEConstants.DEF_SENSOR_ORIENT)
                        .refFrame("#GIMBAL_FRAME")
                        .values(new double[] {-82.3, -25.6, 0.0}))
                    .build())*/
                .position(Pose.create()
                    .referenceFrame("#GIMBAL_FRAME")
                    .xyzPos(0, 0, 0.05)
                    .eulerAngles(-82.3, -25.6, 0.0)
                    .build())
                .build())
            .build();
        
        printAsXml(sys);
    }

}
