/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
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
    
    
    public void testReadKvpDescribeSensor() throws Exception
    {
        SOSUtils utils = new SOSUtils();
        String kvp = "service=SOS&version=2.0&request=DescribeSensor&procedure=urn:test:sensors:fakegps&procedureDescriptionFormat=http://www.opengis.net/sensorml/2.0.0";
        DescribeSensorRequest request = (DescribeSensorRequest)utils.readURLQuery(kvp);
        assertEquals("urn:test:sensors:fakegps", request.getProcedureID());
        assertEquals("http://www.opengis.net/sensorml/2.0.0", request.getFormat());
    }
}
