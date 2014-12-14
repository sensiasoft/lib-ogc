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

package org.vast.ows.sos;

import org.vast.ows.OWSCommonReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseReader;
import org.vast.swe.SWECommonUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * GetResultTemplate Response Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reader for XML GetResultTemplate response for SOS v2.0 
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Mar, 19 2008
 * @version 1.0
 */
public class GetResultTemplateResponseReaderV20 extends SWEResponseReader<GetResultTemplateResponse>
{
	protected OWSCommonReaderV11 owsReader = new OWSCommonReaderV11();
	protected SWECommonUtils sweUtils = new SWECommonUtils();
	
	
	public GetResultTemplateResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
	    GetResultTemplateResponse response = new GetResultTemplateResponse();
		response.setVersion("2.0");
		
		// result structure
		try
        {
            Element resultStructElt = dom.getElement(responseElt, "resultStructure/*");
            response.setResultStructure(sweUtils.readComponent(dom, resultStructElt));
        }
        catch (XMLReaderException e)
        {
            throw new OWSException("Error while reading result structure", e);
        }
        
        // result encoding
        try
        {
            Element resultEncElt = dom.getElement(responseElt, "resultEncoding/*");
            response.setResultEncoding(sweUtils.readEncoding(dom, resultEncElt));
        }
        catch (XMLReaderException e)
        {
            throw new OWSException("Error while reading result structure", e);
        }		
		
		// read extensions
		readXMLExtensions(dom, responseElt, response);
		
		return response;
	}	
}