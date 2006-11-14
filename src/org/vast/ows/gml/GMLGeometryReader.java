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

import org.vast.io.xml.DOMReader;
import org.vast.math.Vector3d;
import org.w3c.dom.Element;


/**
 *  * <p><b>Title:</b>
 * GML Geometry Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Reads different GML geometry objects (coords + CRS)
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 25, 2006
 * @version 1.0
 */
public class GMLGeometryReader
{
    protected final static String invalidCoordinates = "Invalid Coordinates: ";
    
    
    public GMLGeometryReader()
    {
    }
    
        
    public Vector3d readPoint(DOMReader dom, Element pointElt) throws GMLException
    {
        Vector3d point = new Vector3d();
        String coordsText = "";
        String[] coords;
        
        // read SRS
        String srs = dom.getAttributeValue(pointElt, "@srsName");
        point.setCrs(srs);
        
        try
        {
            // read lower corner
            coordsText = dom.getElementValue(pointElt, "coordinates");
            coords = coordsText.split(" ");
            point.x = Double.parseDouble(coords[0]);
            point.y = Double.parseDouble(coords[1]);
            point.z = Double.parseDouble(coords[2]);
        }
        catch (Exception e)
        {
            throw new GMLException(invalidCoordinates + coordsText, e);
        }
        
        return point;
    }
}
