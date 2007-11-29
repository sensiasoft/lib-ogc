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

import org.vast.ows.OWSReference;
import org.vast.ows.util.Bbox;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;

/**
 * <p><b>Title:</b><br/>
 * OWS Reference Reader V11
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Utility methods to read OWS common References and Reference Groups
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 22 nov. 07
 * @version 1.0
 */
public class OWSCommonReaderV11
{
	protected final static String invalidCoordinates = "Invalid Coordinates: ";
	
	
	public Bbox readBbox(DOMHelper dom, Element bboxElt) throws OWSException
    {
    	Bbox bbox = new Bbox();
        String coordsText = "";
        String[] coords;
        
        // read CRS
        String crs = dom.getAttributeValue(bboxElt, "@crs");
        bbox.setCrs(crs);
        
        try
        {
            // read lower corner
            coordsText = dom.getElementValue(bboxElt, "LowerCorner");
            coords = coordsText.split(" ");
            bbox.setMinX(Double.parseDouble(coords[0]));
            bbox.setMinY(Double.parseDouble(coords[1]));
            if (coords.length == 3)
            	bbox.setMinZ(Double.parseDouble(coords[2]));
            
            // read upper corner
            coordsText = dom.getElementValue(bboxElt, "UpperCorner");
            coords = coordsText.split(" ");
            bbox.setMaxX(Double.parseDouble(coords[0]));
            bbox.setMaxY(Double.parseDouble(coords[1]));
            if (coords.length == 3)
            	bbox.setMaxZ(Double.parseDouble(coords[2]));
        }
        catch (Exception e)
        {
            throw new OWSException(invalidCoordinates + coordsText, e);
        }
        
        return bbox;
    }
	
	
	public OWSReference readReference(DOMHelper dom, Element refElt) throws OWSException
	{
		OWSReference ref = new OWSReference();
		
		// role
		String role = dom.getAttributeValue(refElt, "@role");
		ref.setRole(role);
		
		// href
		String endpoint = dom.getAttributeValue(refElt, "@href");
		ref.setHref(endpoint);
		
		// format
		String format = dom.getElementValue(refElt, "Format");
		ref.setFormat(format);
		
		return ref;
	}
	
	
	public void readIdentification(DOMHelper dom, Element parentElt, OWSIdentification idObject)
	{
		// title, abstract, keywords
		readDescription(dom, parentElt, idObject);
		
		// identifier
		String identifier = dom.getElementValue(parentElt, "Identifier");
		idObject.setIdentifier(identifier);
		
		// metadata??
	}
	
	
	public void readDescription(DOMHelper dom, Element parentElt, OWSIdentification idObject)
	{
		// title
		String title = dom.getElementValue(parentElt, "Title");
		idObject.setTitle(title);
		
		// abstract
		String description = dom.getElementValue(parentElt, "Abstract");
		idObject.setDescription(description);
		
		// keywords
		NodeList keywordElts = dom.getElements(parentElt, "Keywords/Keyword");
		for (int i = 0; i < keywordElts.getLength(); i++)
		{
			Element keywordElt = (Element) keywordElts.item(i);
			String keyword = dom.getElementValue(keywordElt);
			idObject.getKeywords().add(keyword);
		}
	}
}