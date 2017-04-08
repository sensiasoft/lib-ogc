/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2016-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.swe.SWEUtils;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate an XML SPS InsertTaskingTemplate
 * request as defined in version 2.0 of the SPS standard from the
 * corresponding object 
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class InsertTaskingTemplateWriterV20 extends SWERequestWriter<InsertTaskingTemplateRequest>
{	
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    
    
	public InsertTaskingTemplateWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertTaskingTemplateRequest request) throws OWSException
	{
		dom.addUserPrefix("sps", OGCRegistry.getNamespaceURI(OWSUtils.SPS, request.getVersion()));
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWEUtils.SWE, "2.0"));
		
		// root element
		Element rootElt = dom.createElement("sps:" + request.getOperation());
		addCommonXML(dom, rootElt, request);
		
		// template element
        Element templateElt = dom.addElement(rootElt, "sps:proposedTemplate/sps:TaskingTemplate");
        
        // offering
		dom.setElementValue(templateElt, "+sps:procedure", request.getProcedureID());
		
		// result structure
        try
        {
            Element taskingParamsPropElt = dom.addElement(templateElt, "sps:taskingParameters");        
            Element taskingParamsElt = sweUtils.writeComponent(dom, request.getTaskingParameters(), false);
            taskingParamsPropElt.appendChild(taskingParamsElt);
        }
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing SWE Common tasking parameters", e);
        }
        
        // result encoding
        try
        {
            Element encodingPropertyElt = dom.addElement(templateElt, "sps:encoding");        
            Element encodingElt = sweUtils.writeEncoding(dom, request.getEncoding());
            encodingPropertyElt.appendChild(encodingElt); 
    	}
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing SWE Common encoding", e);
        }
        
		return rootElt;
	}
}