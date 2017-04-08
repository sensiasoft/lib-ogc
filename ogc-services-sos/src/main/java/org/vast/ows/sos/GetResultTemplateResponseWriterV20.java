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

import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseWriter;
import org.vast.swe.SWEUtils;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;


/**
 * <p>
 * Writer to generate an XML GetResultTemplate response based
 * on values contained in a GetResultTemplateResponse object for SOS v2.0
 * </p>
 *
 * @author Alex Robin
 * @date Aug 19, 2012
 * */
public class GetResultTemplateResponseWriterV20 extends SWEResponseWriter<GetResultTemplateResponse>
{
	protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
	
	
	public Element buildXMLResponse(DOMHelper dom, GetResultTemplateResponse response, String version) throws OWSException
	{
		try
		{
			dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(SOSUtils.SOS, version));
			
			// root element
			Element rootElt = dom.createElement("sos:" + response.getMessageType());
			
			// write extensions
			writeExtensions(dom, rootElt, response);
            
			// result structure
			DataComponent resultStructure = response.getResultStructure();
			if (resultStructure != null)
			{
				Element structPropElt = dom.addElement(rootElt, "sos:resultStructure");
				Element componentElt = sweUtils.writeComponent(dom, resultStructure, false);
				structPropElt.appendChild(componentElt);
			}
			else
				throw new SOSException("Result structure must be provided");
			
			// result encoding
			DataEncoding resultEncoding = response.getResultEncoding();
            if (resultEncoding != null)
            {
                Element encPropElt = dom.addElement(rootElt, "sos:resultEncoding");
                Element encElt = sweUtils.writeEncoding(dom, resultEncoding);
                encPropElt.appendChild(encElt);
            }
            else
                throw new SOSException("Result encoding must be provided");
			
			return rootElt;
		}
		catch (XMLWriterException e)
		{
			throw new SOSException("Cannot write result template", e);
		}
	}
	
}