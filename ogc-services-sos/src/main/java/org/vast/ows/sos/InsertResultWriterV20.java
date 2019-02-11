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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.swe.DataSinkDOM;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SOS InsertResult
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertResultRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
public class InsertResultWriterV20 extends SWERequestWriter<InsertResultRequest>
{
    
	public InsertResultWriterV20()
	{
	}

	
	@Override
	public Map<String, String> buildURLParameters(InsertResultRequest request) throws OWSException
	{
		Map<String, String> urlParams = new LinkedHashMap<>();
        addCommonArgs(urlParams, request);
		
		// template id
        urlParams.put("template", request.getTemplateId());
        
        return urlParams;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertResultRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		
		// root element
		Element rootElt = dom.createElement("sos:" + request.getOperation());
		addCommonXML(dom, rootElt, request);
		
		// template id
		dom.setElementValue(rootElt, "+sos:template", request.getTemplateId());
		
		// result values
		try
        {
            Element valuesElt = dom.addElement(rootElt, "sos:resultValues");
            DataSinkDOM dataSink = new DataSinkDOM(dom, valuesElt);
            request.getResultData().writeData(dataSink);
        }
        catch (IOException e)
        {
            throw new SOSException("Cannot write result data", e);
        }   
        
		return rootElt;
	}
}