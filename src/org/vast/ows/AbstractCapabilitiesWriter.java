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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.io.IOException;
import java.io.OutputStream;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * Abstract Capabilities Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base abstract class for writing an OWS server capabilities document
 * as a DOM element or in an output stream.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 2, 2007
 * @version 1.0
 */
public abstract class AbstractCapabilitiesWriter<CapsType extends OWSLayerCapabilities> implements OWSCapabilitiesWriter<CapsType>
{

    public abstract Element buildServiceCapabilities(DOMHelper dom, OWSServiceCapabilities caps) throws OWSException;
    public abstract Element buildLayerCapabilities(DOMHelper dom, CapsType caps) throws OWSException;


    public void writeServiceCapabilities(OutputStream os, OWSServiceCapabilities caps) throws OWSException
    {
        try
        {
            DOMHelper dom = new DOMHelper();
            dom.createDocument("Capabilities");
            Element requestElt = buildServiceCapabilities(dom, caps);
            dom.serialize(requestElt, os, null);                   
        }
        catch (IOException e)
        {
            throw new OWSException("IO Error while writing service capabilities XML", e);
        }
    }


    public void writeLayerCapabilities(OutputStream os, CapsType caps) throws OWSException
    {
        try
        {
            DOMHelper dom = new DOMHelper();
            dom.createDocument("Capabilities");
            Element requestElt = buildLayerCapabilities(dom, caps);
            dom.serialize(requestElt, os, null);                   
        }
        catch (IOException e)
        {
            throw new OWSException("IO Error while writing layer capabilities XML", e);
        }
    }

}
