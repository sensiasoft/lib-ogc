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

package org.vast.ows.sos;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.sweCommon.DataSinkDOM;
import org.vast.sweCommon.SWECommonUtils;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS InsertResult Request Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS InsertResult
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertResultRequest object 
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 2, 2014
 * @version 1.0
 */
public class InsertResultWriterV20 extends SWERequestWriter<InsertResultRequest>
{
    
	public InsertResultWriterV20()
	{
	}

	
	@Override
	public Map<String, String> buildURLParameters(InsertResultRequest request) throws OWSException
	{
		Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
		
		// offerings
        urlParams.put("template", request.getTemplateId());
        
        return urlParams;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertResultRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0"));
		
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
            throw new RuntimeException("Error while writing SWE Common data", e);
        }   
        
		return rootElt;
	}
}