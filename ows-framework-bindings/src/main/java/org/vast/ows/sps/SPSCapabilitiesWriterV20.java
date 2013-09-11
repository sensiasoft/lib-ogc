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
package org.vast.ows.sps;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSCapabilitiesWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SPS Capabilities Writer V2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writes a SPS server capabilities document from
 * OWSServiceCapabilities and SPSLayerCapabilities
 * objects for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2009</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 27 nov. 07
 * @version 1.0
 */
public class SPSCapabilitiesWriterV20 extends OWSCapabilitiesWriterV11
{

	@Override
	public Element buildXMLResponse(DOMHelper dom, OWSServiceCapabilities caps, String version) throws OWSException
	{
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.SPS, version));
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI(OWSUtils.OWS, "1.1"));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		Element capsElt = dom.createElement("Capabilities");
		writeRootAttributes(dom, capsElt, caps, version);
		writeServiceIdentification(dom, capsElt, caps);		
		writeServiceProvider(dom, capsElt, caps.getServiceProvider());
		writeOperationsMetadata(dom, capsElt, caps);
		writeContents(dom, capsElt, caps, version);
				
		return capsElt;
	}
	
	
	@Override
	protected void writeContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps, String version) throws OWSException
	{
		// TODO
	}
}
