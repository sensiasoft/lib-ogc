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

package org.vast.ows.wcs;

import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSReferenceGroup;
import org.vast.ows.OWSCommonWriterV11;
import org.vast.ows.OWSUtils;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * Writer to generate an XML Coverage Manifest based
 * on values contained in a CoverageManifest object for version 1.1.1
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 11, 2007
 * */
public class CoverageManifestWriterV11 extends AbstractResponseWriter<CoverageManifest>
{
	protected OWSCommonWriterV11 owsWriter = new OWSCommonWriterV11();
	
	
	public Element buildXMLResponse(DOMHelper dom, CoverageManifest manifest, String version) throws OWSException
	{
		// setup ns and create root elt
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WCS, version));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		// root element
		Element rootElt = dom.createElement("Coverages");
				
		// add all coverage briefs
		for (int i=0; i<manifest.getCoverages().size(); i++)
		{
			Element coverageElt = dom.addElement(rootElt, "+Coverage");
			OWSReferenceGroup coverageInfo = manifest.getCoverages().get(i);
			owsWriter.buildRefGroup(dom, coverageElt, coverageInfo);
		}
				
		return rootElt;
	}
	
}