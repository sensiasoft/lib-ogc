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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.vast.cdm.common.AsciiEncoding;
import org.vast.data.DataGroup;
import org.vast.ows.OWSUtils;
import org.vast.ows.sos.InsertResultRequest;
import org.vast.ows.sos.InsertResultTemplateRequest;
import org.vast.ows.sos.SOSUtils;
import org.vast.ows.test.OWSTestCase;
import org.vast.xml.DOMHelper;


public class TestSosInsertResultBindingsV20 extends OWSTestCase
{    
    
    protected InsertResultRequest readXmlInsertResult(String path, String templatePath) throws Exception
    {
        InsertResultTemplateRequest template = (InsertResultTemplateRequest)readXmlRequest(templatePath);
        
        SOSUtils utils = new SOSUtils();
        InputStream is;
        InsertResultRequest request;
        
        is = TestSosInsertResultBindingsV20.class.getResourceAsStream(path);
        DOMHelper dom = new DOMHelper(is, false);
        request = (InsertResultRequest)utils.readSweEncodedRequest(dom, dom.getBaseElement(), template.getResultStructure(), template.getResultEncoding());        
        is.close();
        
        return request;
    }
    
    
    protected void readReadWriteCompareXmlInsertResult(String path, String templatePath) throws Exception
    {
        InsertResultTemplateRequest template = (InsertResultTemplateRequest)readXmlRequest(templatePath);
        
        SOSUtils utils = new SOSUtils();
        InsertResultRequest request1;
        DOMHelper dom1, dom2;
        
        // read request XML
        InputStream is = TestSosInsertResultBindingsV20.class.getResourceAsStream(path);
        dom1 = new DOMHelper(is, false);
        request1 = (InsertResultRequest)utils.readSweEncodedRequest(dom1, dom1.getBaseElement(), template.getResultStructure(), template.getResultEncoding());
        is.close();
        
        // write it
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLQuery(System.out, request1);
        utils.writeXMLQuery(os, request1);
        os.close();
        
        // read back and check equality
        dom2 = new DOMHelper(new ByteArrayInputStream(os.toByteArray()), false);        
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    public void testReadXmlInsertResult() throws Exception
    {
        InsertResultRequest request = readXmlInsertResult("examples_v20/resultHandling/InsertResult1_SOAP.xml", "examples_v20/resultHandling/InsertResultTemplate1.xml");
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("InsertResult", request.getOperation());
        assertEquals("http://my.organization.org/sos/resultTemplate1", request.getTemplateId());
        assertEquals(3, request.getResultData().getElementCount());
        assertEquals(2, request.getResultData().getElementType().getComponentCount());        
    }
    
    
    public void testWriteXmlInsertResult() throws Exception
    {
        readReadWriteCompareXmlInsertResult("examples_v20/resultHandling/InsertResult1.xml", "examples_v20/resultHandling/InsertResultTemplate1.xml");
    }
    
    
    public void testReadXmlInsertResultTemplate() throws Exception
    {
        InsertResultTemplateRequest request = (InsertResultTemplateRequest)readXmlRequest("examples_v20/resultHandling/InsertResultTemplate1_SOAP.xml");
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("InsertResultTemplate", request.getOperation());
        assertEquals("http://www.my_namespace.org/water_gage_2_observations", request.getOffering());
        assertEquals(DataGroup.class, request.getResultStructure().getClass());
        assertEquals(AsciiEncoding.class, request.getResultEncoding().getClass());
    }
    
    
    public void testWriteXmlInsertResultTemplate() throws Exception
    {
        readWriteCompareXmlRequest("examples_v20/resultHandling/InsertResultTemplate1.xml");
    }
}
