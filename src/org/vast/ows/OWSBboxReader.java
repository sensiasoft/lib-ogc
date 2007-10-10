/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import org.vast.xml.DOMHelper;
import org.vast.ows.util.Bbox;
import org.w3c.dom.Element;


/**
 *  * <p><b>Title:</b>
 * OWS Bbox Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads OWS Bounding Box (coords + CRS) used in several OGC services
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 08, 2007
 * @version 1.0
 */
public class OWSBboxReader
{
    protected final static String invalidCoordinates = "Invalid Coordinates: ";
    
    
    public OWSBboxReader()
    {
    }
    
        
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
}
