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

import org.vast.io.xml.DOMWriter;
import org.vast.ows.util.Bbox;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * GML Envelope Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Used to serialize Bbox objects to GML.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Oct 25, 2006
 * @version 1.0
 */
public class GMLEnvelopeWriter
{
    
    public GMLEnvelopeWriter()
    {
    }
    
        
    public Element writeEnvelope(DOMWriter dom, Bbox bbox) throws GMLException
    {
        Element envelopeElt = dom.createElement("gml:Envelope");
        
        String lowerCoords = bbox.getMinX() + " " + bbox.getMinY();
        dom.setElementValue(envelopeElt, "gml:lowerCorner", lowerCoords);
        String upperCoords = bbox.getMaxX() + " " + bbox.getMaxY();
        dom.setElementValue(envelopeElt, "gml:upperCorner", upperCoords);
        
        return envelopeElt;
    }
}
