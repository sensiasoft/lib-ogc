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

import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.om.ObservationWriterV20;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestWriter;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.sweCommon.SweComponentWriterV20;
import org.vast.sweCommon.SweEncodingWriterV20;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SOS InsertResultTemplate Request Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML SOS InsertResultTemplate
 * request as defined in version 2.0 of the SOS standard using
 * values contained in a InsertResultTemplateRequest object 
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 2, 2014
 * @version 1.0
 */
public class InsertResultTemplateWriterV20 extends SWERequestWriter<InsertResultTemplateRequest>
{	
    protected SweComponentWriterV20 componentWriter = new SweComponentWriterV20();    
    protected SweEncodingWriterV20 encodingWriter = new SweEncodingWriterV20();
    protected ObservationWriterV20 obsWriter = new ObservationWriterV20();
    
    
	public InsertResultTemplateWriterV20()
	{
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, InsertResultTemplateRequest request) throws OWSException
	{
		dom.addUserPrefix("sos", OGCRegistry.getNamespaceURI(OWSUtils.SOS, request.getVersion()));
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0"));
		
		// root element
		Element rootElt = dom.createElement("sos:InsertResult");
		addCommonXML(dom, rootElt, request);
		
		// offering
		dom.setElementValue(rootElt, "+sos:offering", request.getOffering());
		
		// observation template
		try
        {
		    Element obsPropertyElt = dom.addElement(rootElt, "sos:observationTemplate");
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
            Element structurePropertyElt = dom.addElement(rootElt, "sos:resultStructure");        
            Element structureElt = componentWriter.writeComponent(dom, request.getResultStructure());
            structurePropertyElt.appendChild(structureElt);
        }
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing SWE Common result structure", e);
        }
        
        // result encoding
        try
        {
            Element encodingPropertyElt = dom.addElement(rootElt, "sos:resultEncoding");        
            Element encodingElt = encodingWriter.writeEncoding(dom, request.getResultEncoding());
            encodingPropertyElt.appendChild(encodingElt); 
    	}
        catch (XMLWriterException e)
        {
            throw new OWSException("Error while writing SWE Common result encoding", e);
        }
        
		return rootElt;
	}
}