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

import org.vast.ows.sos.SOSUtils;
import org.vast.ows.swe.DescribeSensorRequest;
import org.vast.ows.test.OWSTestCase;


public class TestSosDescribeBindingsV20 extends OWSTestCase
{
    
    public void testReadXmlDescribeSensor() throws Exception
    {
        DescribeSensorRequest request = (DescribeSensorRequest)readXmlRequest("examples_v20/core/DescribeSensor1.xml");
        assertEquals("http://my.company.org/sensors/sensor1", request.getProcedureID());
        assertEquals("http://www.opengis.net/sensorml/1.0.1", request.getFormat());
    }
    
    
    public void testReadWriteXmlDescribeSensor() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/core/DescribeSensor1.xml");
    }
    
    
    public void testReadWriteXmlDescribeSensorWithTime() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/core/DescribeSensorWithTime.xml");
    }
    
    
    public void testReadKvpDescribeSensor() throws Exception
    {
        SOSUtils utils = new SOSUtils();
        String kvp = "service=SOS&version=2.0&request=DescribeSensor&procedure=urn:test:sensors:fakegps&procedureDescriptionFormat=http://www.opengis.net/sensorml/2.0.0";
        DescribeSensorRequest request = (DescribeSensorRequest)utils.readURLQuery(kvp);
        assertEquals("urn:test:sensors:fakegps", request.getProcedureID());
        assertEquals("http://www.opengis.net/sensorml/2.0.0", request.getFormat());
    }
    
    
    public void testReadWriteXmlDescribeSensorResponse() throws Exception
    {
        readWriteCompareXmlResponse("examples_v20/core/DescribeSensor1_response.xml", SOSUtils.SOS);
    }
}
