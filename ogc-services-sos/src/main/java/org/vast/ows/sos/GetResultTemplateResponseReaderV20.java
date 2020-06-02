/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import org.vast.ows.OWSCommonReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseReader;
import org.vast.swe.SWEUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;


/**
 * <p>
 * Reader for XML GetResultTemplate response for SOS v2.0 
 * </p>
 *
 * @author Alex Robin
 * @date Mar, 19 2008
 * */
public class GetResultTemplateResponseReaderV20 extends SWEResponseReader<GetResultTemplateResponse>
{
	protected OWSCommonReaderV11 owsReader = new OWSCommonReaderV11();
	protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
	
	
	@Override
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