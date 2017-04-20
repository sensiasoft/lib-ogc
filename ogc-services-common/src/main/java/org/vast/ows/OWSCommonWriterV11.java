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

import java.util.List;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSReference;
import org.w3c.dom.*;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;

/**
 * <p>
 * Utility methods to write OWS common References and Reference Groups
 * </p>
 *
 * @author Alex Robin
 * @since 22 nov. 07
 * */
public class OWSCommonWriterV11
{

	public Element buildBbox(DOMHelper dom, Bbox bbox) throws OWSException
    {
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		Element bboxElt = dom.createElement("ows:BoundingBox");
        
        // set crs
        String crs = bbox.getCrs();
        if (crs != null)
        	dom.setAttributeValue(bboxElt, "@crs", crs);
                
        // write lower corner
        String lowCoords = Double.toString(bbox.getMinX());
    	if (!Double.isNaN(bbox.getMinY()))
    		lowCoords += " " + bbox.getMinY();
    	if (!Double.isNaN(bbox.getMinZ()))
    		lowCoords += " " + bbox.getMinZ();
    	
        dom.setElementValue(bboxElt, "ows:LowerCorner", lowCoords);
                    
        // write upper corner
        String upCoords = Double.toString(bbox.getMaxX());
    	if (!Double.isNaN(bbox.getMaxY()))
    		upCoords += " " + bbox.getMaxY();
    	if (!Double.isNaN(bbox.getMaxZ()))
    		upCoords += " " + bbox.getMaxZ();
        dom.setElementValue(bboxElt, "ows:UpperCorner", upCoords);
        
        return bboxElt;
    }
	
	
	public void buildRefGroup(DOMHelper dom, Element refGroupElt, OWSReferenceGroup refGroup) throws OWSException
	{
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		
		// read identification elements
		buildIdentification(dom, refGroupElt, refGroup);
		
		// references
		for (int j=0; j<refGroup.getReferenceList().size(); j++)
		{
			OWSReference ref = refGroup.getReferenceList().get(j);
			Element refElt = this.buildReference(dom, ref);
			refGroupElt.appendChild(refElt);
		}
	}

	
	public Element buildReference(DOMHelper dom, OWSReference ref) throws OWSException
	{
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		Element requestXML = ref.getRequestXML();
		Element refElt;
		
		// pick right element name
		if (requestXML != null)
			refElt = dom.createElement("ows:ServiceReference");
		else
			refElt = dom.createElement("ows:Reference");
		
		// mandatory role
		String role = ref.getRole();
		if (role != null)
			dom.setAttributeValue(refElt, "@xlink:role", role);
		
		// mandatory href
		String href = ref.getHref();
		if (href != null)
		    dom.setAttributeValue(refElt, "@xlink:href", href);
		
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
		
		// metadata
		for (Object obj: ref.getMetadata())
		{
			if (obj instanceof Element)
			{
				Element metaElt = dom.addElement(refElt, "+ows:Metadata");
				Node newElt = refElt.getOwnerDocument().importNode((Element)obj, true);
				metaElt.appendChild(newElt);
			}
		}
		
		// optional XML request message
		if (requestXML != null)
		{
			Element msgElt = dom.addElement(refElt, "ows:RequestMessage");
			Node newElt = refElt.getOwnerDocument().importNode(requestXML, true);
			msgElt.appendChild(newElt);
		}
				
		return refElt;
	}
	
	
	public void buildIdentification(DOMHelper dom, Element parentElt, OWSIdentification idObject) throws OWSException
	{
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		
		// title, abstract, keywords
		buildDescription(dom, parentElt, idObject);
		
		// identifier
		if (idObject.getIdentifier() != null)
			dom.setElementValue(parentElt, "ows:Identifier", idObject.getIdentifier());
		
		// metadata ??
	}
	
	
	public void buildDescription(DOMHelper dom, Element parentElt, OWSIdentification idObject) throws OWSException
	{
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		
		// title
		if (idObject.getTitle() != null)
			dom.setElementValue(parentElt, "ows:Title", idObject.getTitle());

		// abstract
		if (idObject.getDescription() != null)
			dom.setElementValue(parentElt, "ows:Abstract", idObject.getDescription());
		
		// keywords
		List<String> keywords = idObject.getKeywords();
		for (int i = 0; i < keywords.size(); i++)
		{
			String keyword = keywords.get(i);
			dom.setElementValue(parentElt, "Keywords/+Keyword", keyword);
		}
	}
}