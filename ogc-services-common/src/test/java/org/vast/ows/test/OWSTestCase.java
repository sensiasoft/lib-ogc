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

package org.vast.ows.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSResponse;
import org.vast.ows.OWSUtils;
import org.vast.xml.DOMHelper;
import org.xml.sax.InputSource;


public abstract class OWSTestCase extends XMLTestCase
{

    public void setUp() throws Exception
    {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
    }
    
    
    protected void validate(InputStream is, String schemaUrl) throws Exception
    {
        InputSource saxIs = new InputSource(is);
        Validator v = new Validator(saxIs);
        v.useXMLSchema(true);
        v.setJAXP12SchemaSource(schemaUrl);
        assertTrue(v.isValid());
    }
        
    
    protected OWSRequest readXmlRequest(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        
        InputStream is = getClass().getResourceAsStream(path);
        OWSRequest request = utils.readXMLQuery(is);
        is.close();
        
        return request;
    }
    
    
    protected OWSResponse readXmlResponse(String path, String serviceType, String responseType) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        
        InputStream is = getClass().getResourceAsStream(path);
        OWSResponse resp = utils.readXMLResponse(is, serviceType, responseType);
        is.close();
        
        return resp;
    }
    
    
    protected void readWriteCompareXmlRequest(String path) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        OWSRequest request1 = utils.readXMLQuery(dom1, dom1.getBaseElement());
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLQuery(System.out, request1);
        utils.writeXMLQuery(os, request1);
        os.close();
        
        DOMHelper dom2 = new DOMHelper(new ByteArrayInputStream(os.toByteArray()), false);
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
    
    
    
    protected void readWriteCompareXmlResponse(String path, String serviceType) throws Exception
    {
        OWSUtils utils = new OWSUtils();
        OWSResponse resp1;
        
        InputStream is = getClass().getResourceAsStream(path);
        DOMHelper dom1 = new DOMHelper(is, false);
        resp1 = utils.readXMLResponse(dom1, dom1.getBaseElement(), serviceType);
        is.close();
        
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.out.println();
        utils.writeXMLResponse(System.out, resp1);
        utils.writeXMLResponse(os, resp1);
        os.close();
        
        DOMHelper dom2 = new DOMHelper(new ByteArrayInputStream(os.toByteArray()), false);
        assertXMLEqual(dom1.getDocument(), dom2.getDocument());
    }
}
