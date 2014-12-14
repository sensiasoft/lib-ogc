/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/
package org.vast.ows;

import java.io.IOException;
import java.io.OutputStream;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

/**
 * <p>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @since 28 nov. 07
 * @version 1.0
 * @param <ResponseType> Type of OWS response object accepted by this writer
 */
public abstract class AbstractResponseWriter<ResponseType extends OWSResponse> implements OWSResponseWriter<ResponseType>
{
    public final static String xmlError = "Error while generating XML for ";
    public final static String ioError = "IO error while writing XML response to stream";
	public final static String unsupportedVersion = "No support for version ";
	
	protected DateTimeFormat timeFormat = new DateTimeFormat();
	
	
	public abstract Element buildXMLResponse(DOMHelper dom, ResponseType response, String version) throws OWSException;
	
	
	@Override
	public Element buildXMLResponse(DOMHelper dom, ResponseType response) throws OWSException
	{
		try
        {
            return buildXMLResponse(dom, response, response.getVersion());
        }
		catch (Exception e)
        {
            throw new OWSException(xmlError + response.getClass().getName(), e);
        }
	}
	
	
	@Override
    public void writeXMLResponse(OutputStream os, ResponseType response, String version) throws OWSException
    {
    	try
        {
            DOMHelper dom = new DOMHelper();
            Element responseElt = buildXMLResponse(dom, response, version);
            dom.serialize(responseElt, os, null);
        }
        catch (IOException e)
        {
            throw new OWSException(ioError, e);
        }        
    }
	
	
	@Override
    public void writeXMLResponse(OutputStream os, ResponseType response) throws OWSException
    {
    	writeXMLResponse(os, response, response.getVersion());      
    }

}
