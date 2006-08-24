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

package org.vast.ows;

import java.io.IOException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * OWS Capabilities Serializer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Seializes a Capabilities document to the given
 * output stream. This will also skip elements called
 * internalInfo that are used internally by our servlets.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Aug 22, 2006
 * @version 1.0
 */
public class OWSCapabilitiesSerializer extends XMLSerializer
{
    
    public OWSCapabilitiesSerializer()
    {
        this._format = new OutputFormat();
        this._format.setMethod("xml");
        this._format.setIndenting(true);
        this._format.setLineWidth(0);
    }
    
    
    @Override
    protected void serializeElement(Element elt) throws IOException
    {
        if (elt.getLocalName().equals("internalInfo"))
        {
            // do nothing (skip serialization of this element!)
        }
        else
        {
            super.serializeElement(elt);
        }
    }
}
