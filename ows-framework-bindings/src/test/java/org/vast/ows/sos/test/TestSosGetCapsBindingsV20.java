/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos.test;

import java.util.List;
import org.vast.ows.test.OWSTestCase;


public class TestSosGetCapsBindingsV20 extends OWSTestCase
{

    public void testWriteAndReadBackCapabilitiesXml() throws Exception
    {
        readWriteCompareXmlResponse("examples_v20/_useCase_homogeneous_sensor_network/GetCapabilities_response_homogeneous_sensor_network.xml", "SOS");
        readWriteCompareXmlResponse("examples_v20/_useCase_mobile_sensors/GetCapabilities_response_mobile_sensor.xml", "SOS");
        readWriteCompareXmlResponse("examples_v20/_useCase_airbase_station_network/GetCapabilities_response.xml", "SOS");
    }


    protected void checkListsEquals(List<String> list1, List<String> list2)
    {
        assertTrue("List are not of same size", list1.size() == list2.size());

        for (int i = 0; i < list1.size(); i++)
            assertEquals(list1.get(i), list2.get(i));
    }
}
