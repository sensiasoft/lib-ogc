/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2016-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps.test;

import org.vast.ows.sps.ConnectTaskingRequest;
import org.vast.ows.sps.SPSUtils;
import org.vast.ows.test.OWSTestCase;


public class TestSpsInsertTaskingBindingsV20 extends OWSTestCase
{    
    
    public void testReadWriteXmlInsertTaskingTemplate() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/transactional/InsertTaskingTemplate1.xml");
    }
    
    
    public void testReadWriteKvpConnectTasking() throws Exception
    {
        SPSUtils utils = new SPSUtils();
        String kvp = "service=SPS&version=2.0&request=ConnectTasking&session=urn%3Asps%3Atasking%3A001";
        
        ConnectTaskingRequest request = (ConnectTaskingRequest)utils.readURLQuery(kvp);
        assertEquals("urn:sps:tasking:001", request.getSessionID());
        
        request.setGetServer("http://myserver.org");
        String url = utils.buildURLQuery(request);
        System.out.println(url);
        assertTrue("Incorrect correct tasking URL", url.endsWith(kvp));
    }
}
