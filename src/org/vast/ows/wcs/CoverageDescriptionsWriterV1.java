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

package org.vast.ows.wcs;

import java.util.List;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSException;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * Coverage Descriptions Writer V11
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 23 nov. 07
 * @version 1.0
 */
public class CoverageDescriptionsWriterV1 extends AbstractResponseWriter<CoverageDescriptions>
{
	
	
	public Element buildXMLResponse(DOMHelper dom, CoverageDescriptions desc, String version) throws OWSException
	{
		// root element and ns URI according to version
		Element rootElt;
		if (OGCRegistry.normalizeVersionString(version).equals("1"))
		{
			dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OGCRegistry.WCS));
			rootElt = dom.createElement("CoverageDescription");
		}
		else
		{
			dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OGCRegistry.WCS, version));
			rootElt = dom.createElement("CoverageDescriptions");
		}
		
		// write response version
		dom.setAttributeValue(rootElt, "@version", version);

		// add all coverage descriptions
		List<WCSMetadataProxy> metadataProxys = desc.getMetadataProxys();
		for (int i=0; i<metadataProxys.size(); i++)
		{
			// delegates work to each metadata provider
			WCSMetadataProxy proxy = metadataProxys.get(i);
			WCSMetadataProvider metaProvider = proxy.getMetadataProvider(desc.getVersion());
			Element descElt = metaProvider.getCoverageDescription();
			Node importedNode = dom.getDocument().importNode(descElt, true);
			rootElt.appendChild(importedNode);
		}
				
		return rootElt;
	}
}