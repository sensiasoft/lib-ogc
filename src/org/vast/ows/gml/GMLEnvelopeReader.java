/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.gml;

import org.vast.xml.DOMHelper;
import org.vast.ows.util.Bbox;
import org.w3c.dom.Element;


/**
 *  * <p><b>Title:</b>
 * GML Envelope Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads GML envelope (coords + CRS)
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 25, 2006
 * @version 1.0
 */
public class GMLEnvelopeReader
{
    protected final static String invalidCoordinates = "Invalid Coordinates: ";
    
    
    public GMLEnvelopeReader()
    {
    }
    
        
    public Bbox readEnvelope(DOMHelper dom, Element envelopeElt) throws GMLException
    {
        Bbox bbox = new Bbox();
        String coordsText = "";
        String[] coords;
        
        // read SRS
        String srs = dom.getAttributeValue(envelopeElt, "@srsName");
        bbox.setCrs(srs);
        
        try
        {
            // read lower corner
            coordsText = dom.getElementValue(envelopeElt, "lowerCorner");
            coords = coordsText.split(" ");
            bbox.setMinX(Double.parseDouble(coords[0]));
            bbox.setMinY(Double.parseDouble(coords[1]));
            
            // read upper corner
            coordsText = dom.getElementValue(envelopeElt, "upperCorner");
            coords = coordsText.split(" ");
            bbox.setMaxX(Double.parseDouble(coords[0]));
            bbox.setMaxY(Double.parseDouble(coords[1]));
        }
        catch (Exception e)
        {
            throw new GMLException(invalidCoordinates + coordsText, e);
        }
        
        return bbox;
    }
}
