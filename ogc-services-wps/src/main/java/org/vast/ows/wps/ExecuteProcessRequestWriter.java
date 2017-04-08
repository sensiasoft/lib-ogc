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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.swe.AbstractDataWriter;
import org.vast.swe.SWEHelper;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Writer to write the Execute Process data into the request stream
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Dec 15, 2008
 * */
public class ExecuteProcessRequestWriter extends AbstractRequestWriter<ExecuteProcessRequest>
{

    public final static String invalidEndpoint = "No Endpoint URL specified in request object";
    public final static String ioError = "IO Error while sending request:";
	
    
	public static InputStream writeData(ExecuteProcessRequest request) throws OWSException, IOException
    {
			ByteArrayOutputStream  out = new ByteArrayOutputStream ();
            AbstractDataWriter writer = (AbstractDataWriter)SWEHelper.createDataWriter(request.getInputDataEncoding());
    		writer.setDataEncoding(request.getInputDataEncoding());
    		writer.setDataComponents(request.getInputDataComponent());
    		writer.write(out);
    		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

    		return in;
    }
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, ExecuteProcessRequest request) throws OWSException 
	{
		dom.addUserPrefix("wps", OGCRegistry.getNamespaceURI(OWSUtils.WPS, request.getVersion()));
		dom.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OWSUtils.OGC));
		
		// root element
		Element rootElt = dom.createElement("wps:executeProcess");
		addCommonXML(dom, rootElt, request);

		// offering
		dom.setElementValue(rootElt, "wps:requestFormat", request.getOffering());
		
		// request format
		dom.setElementValue(rootElt, "wps:requestFormat", getContentType(request));

		return rootElt;
	}
	

	public Element buildXMLQuery(ExecuteProcessRequest request) throws OWSException
	{
		DOMHelper dom = new DOMHelper();
		return buildXMLQuery(dom, request);
	}

	
	@Override
	public String buildURLQuery(ExecuteProcessRequest request)
			throws OWSException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String getContentType(ExecuteProcessRequest request) throws OWSException
	{
		// request format
	    DataEncoding encoding = request.getInputDataEncoding();
        String requestFormat = null;
        
        if (encoding instanceof TextEncoding)
        	requestFormat = "text/plain"; 
        else if (encoding instanceof XMLEncoding)
        	requestFormat = "text/xml";
        else if (encoding instanceof BinaryEncoding)
        	requestFormat = "application/binary";
		else
			throw new WPSException(ioError + "\n" + "this content-type is not handled. " +
						"The content type can only be ascii (text/plain), xml (text/xml)" +
						" or pure binary (application/binary)");
        
		return requestFormat;
	}
	
}