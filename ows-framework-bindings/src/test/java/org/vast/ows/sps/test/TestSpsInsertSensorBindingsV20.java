/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps.test;

import org.vast.ows.OWSUtils;
import org.vast.ows.test.OWSTestCase;


public class TestSpsInsertSensorBindingsV20 extends OWSTestCase
{
    
    public void testReadXmlSensor() throws Exception
    {
        readXmlRequest("examples_v20/transactional/InsertSensor1.xml");
    }
    
    
    public void testReadWriteXmlInsertSensor() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/transactional/InsertSensor1.xml");
    }
    
    
    public void testReadWriteXmlInsertSensorResponse() throws Exception
    {
        readWriteCompareXmlResponse("examples_v20/transactional/InsertSensor1_response.xml", OWSUtils.SPS);
    }
    
    
    public void testReadWriteXmlDeleteSensor() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/transactional/DeleteSensor1.xml");
    }
    
    
    public void testReadWriteXmlDeleteSensorResponse() throws Exception
    {
        readWriteCompareXmlResponse("examples_v20/transactional/DeleteSensor1_response.xml", OWSUtils.SPS);
    }
    
    
    public void testReadWriteXmlUpdateSensor() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/transactional/UpdateSensorDescription1.xml");
    }
    
    
    public void testReadWriteXmlUpdateSensorResponse() throws Exception
    {
        readWriteCompareXmlResponse("examples_v20/transactional/UpdateSensorDescription1_response.xml", OWSUtils.SPS);
    }
}
