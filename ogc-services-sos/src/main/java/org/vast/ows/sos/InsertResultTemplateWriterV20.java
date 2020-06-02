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

import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.om.ObservationWriterV20;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.swe.SWEUtils;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP or XML SOS InsertResultTemplate
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertResultTemplateRequest object 
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
public class InsertResultTemplateWriterV20 extends SWERequestWriter<InsertResultTemplateRequest>
{	
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    protected ObservationWriterV20 obsWriter = new ObservationWriterV20();
    
    
	public InsertResultTemplateWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertResultTemplateRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWEUtils.SWE, "2.0"));
		
		// root element
		Element rootElt = dom.createElement("sos:" + request.getOperation());
		addCommonXML(dom, rootElt, request);
		
		// template element
		Element templateElt = dom.addElement(rootElt, "sos:proposedTemplate/sos:ResultTemplate");
		
		// offering
		dom.setElementValue(templateElt, "+sos:offering", request.getOffering());
		
		// observation template
		try
        {
		    Element obsPropertyElt = dom.addElement(templateElt, "sos:observationTemplate");
            Element obsElt = obsWriter.write(dom, request.getObservationTemplate());
            obsPropertyElt.appendChild(obsElt);
        }
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing observation", e);
        }
		
        // result structure
        try
        {
            Element structurePropertyElt = dom.addElement(templateElt, "sos:resultStructure");        
            Element structureElt = sweUtils.writeComponent(dom, request.getResultStructure(), false);
            structurePropertyElt.appendChild(structureElt);
        }
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing SWE Common result structure", e);
        }
        
        // result encoding
        try
        {
            Element encodingPropertyElt = dom.addElement(templateElt, "sos:resultEncoding");        
            Element encodingElt = sweUtils.writeEncoding(dom, request.getResultEncoding());
            encodingPropertyElt.appendChild(encodingElt); 
    	}
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing SWE Common result encoding", e);
        }
        
		return rootElt;
	}
}