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

package org.vast.ows.sos;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseWriter;
import org.vast.sweCommon.SweComponentWriterV20;
import org.vast.sweCommon.SweEncodingWriterV20;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * GetResult Response Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to generate an XML GetResult response based
 * on values contained in a GetResultResponse object for SOS v2.0
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Aug 19, 2012
 * @version 1.0
 */
public class GetResultResponseWriterV20 extends SWEResponseWriter<GetResultResponse>
{
	protected SweComponentWriterV20 componentWriter = new SweComponentWriterV20();
	protected SweEncodingWriterV20 encodingWriter = new SweEncodingWriterV20();
	
	
	public Element buildXMLResponse(DOMHelper dom, GetResultResponse response, String version) throws OWSException
	{
		try
		{
			dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(SOSUtils.SOS, version));
			
			// root element
			Element rootElt = dom.createElement("sos:" + response.getMessageType());
			
			// write extensions
			writeExtensions(dom, rootElt, response);
            
			// result structure
			// TODO write data
			
			return rootElt;
		}
		catch (Exception e)
		{
			throw new SOSException(e);
		}
	}
	
}