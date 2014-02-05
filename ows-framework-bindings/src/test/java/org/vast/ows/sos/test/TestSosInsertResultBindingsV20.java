/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.vast.cdm.common.AsciiEncoding;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.data.DataGroup;
import org.vast.ows.OWSUtils;
import org.vast.ows.sos.InsertResultRequest;
import org.vast.ows.sos.InsertResultTemplateRequest;
import org.vast.ows.sos.SOSUtils;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import junit.framework.TestCase;


public class TestSosInsertResultBindingsV20 extends TestCase
{
    
    protected void readXmlInsertResult(String path, String templatePath) throws Exception
    {
        InsertResultTemplateRequest template = readXmlInsertResultTemplate(templatePath);
        
        SOSUtils utils = new SOSUtils();
        InputStream is;
        InsertResultRequest request;
        
        is = TestSosInsertResultBindingsV20.class.getResourceAsStream(path);
        DOMHelper dom = new DOMHelper(is, false);
        request = (InsertResultRequest)utils.readSweEncodedRequest(dom, dom.getBaseElement(), template.getResultStructure(), template.getResultEncoding());
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("InsertResult", request.getOperation());
        assertEquals("http://my.organization.org/sos/resultTemplate1", request.getTemplateId());
        assertEquals(3, request.getResultData().getElementCount());
        assertEquals(2, request.getResultData().getElementType().getComponentCount());
        is.close();
    }
    
    
    protected void writeXmlInsertResult(String path, String templatePath) throws Exception
    {
        InsertResultTemplateRequest template = readXmlInsertResultTemplate(templatePath);
        
        SOSUtils utils = new SOSUtils();
        InsertResultRequest request1, request2;
        DOMHelper dom;
        
        // read request XML
        InputStream is = TestSosInsertResultBindingsV20.class.getResourceAsStream(path);
        dom = new DOMHelper(is, false);
        request1 = (InsertResultRequest)utils.readSweEncodedRequest(dom, dom.getBaseElement(), template.getResultStructure(), template.getResultEncoding());
        is.close();
        
        // write it
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLQuery(System.out, request1);
        utils.writeXMLQuery(os, request1);
        os.close();
        
        // read back and check equality
        ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
        dom = new DOMHelper(bis, false);
        request2 = (InsertResultRequest)utils.readSweEncodedRequest(dom, dom.getBaseElement(), template.getResultStructure(), template.getResultEncoding());
        bis.close();
        
        checkRequestsEquals(request1, request2);
    }
    
    
    protected InsertResultTemplateRequest readXmlInsertResultTemplate(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InputStream is = TestSosInsertResultBindingsV20.class.getResourceAsStream(path);        
        InsertResultTemplateRequest request = (InsertResultTemplateRequest)utils.readXMLQuery(is, OWSUtils.SOS);       
        
        assertEquals(OWSUtils.SOS, request.getService());
        assertEquals("2.0.0", request.getVersion());
        assertEquals("InsertResultTemplate", request.getOperation());
        assertEquals("http://www.my_namespace.org/water_gage_2_observations", request.getOffering());
        assertEquals(DataGroup.class, request.getResultStructure().getClass());
        assertEquals(AsciiEncoding.class, request.getResultEncoding().getClass());
        
        return request;
    }
    
    
    protected void writeXmlInsertResultTemplate(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        InsertResultTemplateRequest request1, request2;
        
        InputStream is = TestSosInsertResultBindingsV20.class.getResourceAsStream(path);
        request1 = (InsertResultTemplateRequest)utils.readXMLQuery(is, OWSUtils.SOS);
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLQuery(System.out, request1);
        utils.writeXMLQuery(os, request1);
        os.close();
        
        ByteArrayInputStream bis = new ByteArrayInputStream(os.toByteArray());
        request2 = (InsertResultTemplateRequest)utils.readXMLQuery(bis, OWSUtils.SOS);
        bis.close();
        
        checkRequestsEquals(request1, request2);
    }
    
    
    public void testReadXmlInsertResult() throws Exception
    {
        readXmlInsertResult("examples_v20/resultHandling/InsertResult1.xml", "examples_v20/resultHandling/InsertResultTemplate1.xml");        
    }
    
    
    public void testWriteXmlInsertResult() throws Exception
    {
        writeXmlInsertResult("examples_v20/resultHandling/InsertResult1.xml", "examples_v20/resultHandling/InsertResultTemplate1.xml");
    }
    
    
    public void testReadXmlInsertResultTemplate() throws Exception
    {
        readXmlInsertResultTemplate("examples_v20/resultHandling/InsertResultTemplate1.xml"); 
    }
    
    
    public void testWriteXmlInsertResultTemplate() throws Exception
    {
        writeXmlInsertResultTemplate("examples_v20/resultHandling/InsertResultTemplate1.xml");
    }
    
    
    protected void checkRequestsEquals(InsertResultRequest request1, InsertResultRequest request2)
    {
        assertEquals(request1.getService(), request2.getService());
        assertEquals(request1.getOperation(), request2.getOperation());
        assertEquals(request1.getVersion(), request2.getVersion());
        assertEquals(request1.getTemplateId(), request2.getTemplateId());
    }
    
    
    protected void checkRequestsEquals(InsertResultTemplateRequest request1, InsertResultTemplateRequest request2)
    {
        assertEquals(request1.getService(), request2.getService());
        assertEquals(request1.getOperation(), request2.getOperation());
        assertEquals(request1.getVersion(), request2.getVersion());
        assertEquals(request1.getOffering(), request2.getOffering());
        assertEquals(request1.getResultStructure().getClass(), request2.getResultStructure().getClass());
        assertEquals(request1.getResultEncoding().getClass(), request2.getResultEncoding().getClass());
    }
}
