/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.soap.*;
import org.vast.cdm.common.CDMException;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSBindingProvider;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSRequestWriter;
import org.vast.ows.OWSResponse;
import org.vast.ows.OWSResponseReader;
import org.vast.ows.OWSUtils;
import org.vast.ows.util.PostRequestFilter;
import org.vast.xml.DOMHelper;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <p>
 * Utility methods for common stuffs for WPS and SOAP binding and attachment
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Dec 22, 2008
 * */

public class WPSUtils extends OWSUtils implements OWSBindingProvider
{
    public final static String ioError = "IO Error while sending request:";
    protected MessageFactory soapMessageFactory;
    protected SOAPConnectionFactory soapConnectionFactory;
	private OWSUtils owsUtils;
    

    public WPSUtils() throws SOAPException
    {
		soapMessageFactory = MessageFactory.newInstance();
		soapConnectionFactory = SOAPConnectionFactory.newInstance();
    }
    
    
    public void loadBindings()
    {
        String mapFileUrl = getClass().getResource("WPSRegistry.xml").toString();
        OGCRegistry.loadMaps(mapFileUrl, false);
    }
    
    
    @SuppressWarnings("unchecked")
	public OWSResponse getWPSResponse(OWSRequest request) throws SOAPException
    {
    	URL url;
		try {
			url = new URL(request.getPostServer());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			throw new SOAPException(e1);
		}
    	SOAPMessage soapMessage = createSoapMessage(request);
    	SOAPMessage soapResponse = sendSoapMessage(soapMessage, url);
    	
    	Element OWSResponseRootElement = (Element)soapResponse.getSOAPBody().getChildElements().next();
    	DOMHelper dom = new DOMHelper((Document)OWSResponseRootElement);
    	OWSResponseReader<OWSResponse> reader = (OWSResponseReader<OWSResponse>)OGCRegistry.createReader(request.getService(), request.getOperation(), request.getVersion());
    	
    	OWSResponse response;
		try {
			response = reader.readXMLResponse(dom, OWSResponseRootElement);
		} catch (OWSException e) {
			// TODO Auto-generated catch block
			throw new SOAPException(e);
		}

    	if(response instanceof ExecuteProcessResponse)
    	{
    		((ExecuteProcessResponse) response).setDataStream((InputStream)(soapResponse.getAttachments().next()));
    	}
    	return response;
    }
    
    
    public SOAPMessage sendSoapMessage(SOAPMessage soapMessage, URL url) throws SOAPException
    {
    	SOAPConnection soapConnection = soapConnectionFactory.createConnection();
    	SOAPMessage soapResponse = soapConnection.call(soapMessage, url);
    	return soapResponse; 
    }

    
    public SOAPMessage createSoapMessage(DOMHelper dom) throws SOAPException
    {
    	SOAPMessage soapMessage = soapMessageFactory.createMessage();
    	soapMessage.getSOAPHeader().detachNode();
    	soapMessage.getSOAPBody().addDocument((Document)dom.getDocument());
		return soapMessage;  	
    }
    
    @SuppressWarnings("unchecked")
	public SOAPMessage createSoapMessage(OWSRequest request) throws SOAPException
    {
    	SOAPMessage soapMessage = soapMessageFactory.createMessage();
    	soapMessage.getSOAPHeader().detachNode();
    	
    	OWSRequestWriter<OWSRequest> writer = (OWSRequestWriter<OWSRequest>)OGCRegistry.createWriter(request.getService(), request.getOperation(), request.getVersion());

    	Element bodyElement = null;
		try {
			DOMHelper dom = new DOMHelper();
			bodyElement = writer.buildXMLQuery(dom , request);
		} catch (OWSException e) {
			// TODO Auto-generated catch block
			throw new SOAPException(e);
		}
    	soapMessage.getSOAPBody().addDocument((Document)bodyElement);

    	if(request instanceof ExecuteProcessRequest)
			try {
				soapMessage = attachInputData((ExecuteProcessRequest)request, soapMessage);
			} catch (OWSException e) {
				// TODO Auto-generated catch block
				throw new SOAPException(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new SOAPException(e);
			}
    	
    	return soapMessage; 
    }
    
    
    public OWSRequest extractWPSRequest(InputStream inputStream) throws SOAPException, OWSException, CDMException 
    {
    	OWSRequest request = null;
    	MimeHeaders header = null;
    	
    	try {
			SOAPMessage soapMessage = soapMessageFactory.createMessage(header,inputStream);
			
			String bodyContent = soapMessage.getSOAPBody().getTextContent();
			byte[] bytes = bodyContent.getBytes("UTF-8");
			InputStream input = new ByteArrayInputStream(bytes);
			InputStream xmlRequest = new PostRequestFilter(new BufferedInputStream(input));
			DOMHelper dom = new DOMHelper(xmlRequest, false);
			request = (OWSRequest)owsUtils.readXMLQuery(dom, dom.getBaseElement());

			if (request instanceof ExecuteProcessRequest)
				{
					AttachmentPart attachment = (AttachmentPart) soapMessage.getAttachments().next();
					((ExecuteProcessRequest)request).setAttachmentPart(attachment);
				}
			
		} catch (DOMException e) {
			throw new SOAPException(e);
		} catch (UnsupportedEncodingException e) {
			throw new SOAPException(e);
		} catch (IOException e) {
			throw new SOAPException(e);
		}	
		
		return request;
	}
    
    
    private SOAPMessage attachInputData(ExecuteProcessRequest request, SOAPMessage soapMessage) throws SOAPException, OWSException, IOException 
    {
    	AttachmentPart attachmentPart = soapMessage.createAttachmentPart();
    	attachmentPart.setRawContent(ExecuteProcessRequestWriter.writeData(request), ExecuteProcessRequestWriter.getContentType(request));
    	return soapMessage;		
	}
    
    
	public void setSoapConnectionFactory(SOAPConnectionFactory soapConnectionFactory) {
		this.soapConnectionFactory = soapConnectionFactory;
	}


	public SOAPConnectionFactory getSoapConnectionFactory() {
		return soapConnectionFactory;
	}


}
