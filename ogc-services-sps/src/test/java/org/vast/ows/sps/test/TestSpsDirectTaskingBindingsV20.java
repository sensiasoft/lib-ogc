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

import org.vast.ows.test.OWSTestCase;


public class TestSpsDirectTaskingBindingsV20 extends OWSTestCase
{    
    
    public void testReadWriteXmlDirectTasking() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/DirectTasking1.xml");
    }
    
    
    public void testReadWriteXmlDirectTaskingResponse() throws Exception
    {
        readWriteCompareXmlResponse("examples_v20/DirectTasking1_response.xml", "SPS");
    }
}
