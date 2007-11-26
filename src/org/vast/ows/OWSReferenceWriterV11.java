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

package org.vast.ows;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSReference;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;

/**
 * <p><b>Title:</b><br/>
 * OWS Reference Writer V11
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Utility methods to write OWS common References and Reference Groups
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 22 nov. 07
 * @version 1.0
 */
public class OWSReferenceWriterV11
{

	public Element buildReferenceXML(DOMHelper dom, OWSReference ref) throws OWSException
	{
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OGCRegistry.OWS, "1.1"));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		Element requestXML = ref.getRequestXML();
		Element refElt;
		
		// pick right element name
		if (requestXML != null)
			refElt = dom.createElement("ows:ServiceReference");
		else
			refElt = dom.createElement("ows:Reference");			
		
		// mandatory role
		dom.setAttributeValue(refElt, "@xlink:role", ref.getRole());
		
		// mandatory href
		dom.setAttributeValue(refElt, "@xlink:href", ref.getHref());
		
		// optional identifier
		String identifier = ref.getIdentifier();
		if (identifier != null)
			dom.setElementValue(refElt, "ows:Identifier", identifier);
		
		// optional abstract
		String desc = ref.getDescription();
		if (desc != null)
			dom.setElementValue(refElt, "ows:Abstract", desc);
		
		// optional format
		String format = ref.getFormat();
		if (format != null)
			dom.setElementValue(refElt, "ows:Format", format);
		
		// optional XML request message
		if (requestXML != null)
		{
			Element msgElt = dom.addElement(refElt, "ows:RequestMessage");
			msgElt.appendChild(requestXML);
		}
				
		return refElt;
	}
	
	
	public Element buildRefGroupXML(DOMHelper dom, Element refGroupElt, OWSReferenceGroup refGroup) throws OWSException
	{
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OGCRegistry.OWS, "1.1"));
		
		// title
		if (refGroup.getTitle() != null)
			dom.setElementValue(refGroupElt, "ows:Title", refGroup.getTitle());

		// abstract
		if (refGroup.getDescription() != null)
			dom.setElementValue(refGroupElt, "ows:Abstract", refGroup.getDescription());
		
		// identifier
		if (refGroup.getIdentifier() != null)
			dom.setElementValue(refGroupElt, "ows:Identifier", refGroup.getIdentifier());
		
		// references
		for (int j=0; j<refGroup.getReferenceList().size(); j++)
		{
			OWSReference ref = refGroup.getReferenceList().get(j);
			Element refElt = this.buildReferenceXML(dom, ref);
			refGroupElt.appendChild(refElt);
		}
		
		return refGroupElt;
	}
}