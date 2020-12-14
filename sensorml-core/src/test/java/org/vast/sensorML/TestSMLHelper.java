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
import org.vast.sensorML.SMLBuilders.PhysicalSystemBuilder;
import org.vast.sensorML.helper.CommonIdentifiers;
import org.vast.swe.SWEHelper;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.PhysicalSystem;
import net.opengis.sensorml.v20.Term;


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
            .typeOf(TYPEOF_UID, TYPEOF_URL)
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

}
