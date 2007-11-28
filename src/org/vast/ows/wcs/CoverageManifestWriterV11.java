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

import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSReferenceGroup;
import org.vast.ows.OWSReferenceWriterV11;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * Coverage Manifest Writer v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Writer to generate an XML Coverage Manifest based
 * on values contained in a CoverageManifest object for version 1.1
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 11, 2007
 * @version 1.0
 */
public class CoverageManifestWriterV11 extends AbstractResponseWriter<CoverageManifest>
{
	protected OWSReferenceWriterV11 owsWriter = new OWSReferenceWriterV11();
	
	
	public Element buildXMLResponse(DOMHelper dom, CoverageManifest manifest, String version) throws OWSException
	{
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OGCRegistry.WCS, version));
		dom.addUserPrefix("xlink", OGCRegistry.getNamespaceURI(OGCRegistry.XLINK));
		
		// root element
		Element rootElt = dom.createElement("Coverages");
		dom.setAttributeValue(rootElt, "@version", version);
		
		// add all coverage briefs
		for (int i=0; i<manifest.getCoverages().size(); i++)
		{
			Element coverageElt = dom.addElement(rootElt, "+Coverage");
			OWSReferenceGroup coverageInfo = manifest.getCoverages().get(i);
			owsWriter.buildRefGroupXML(dom, coverageElt, coverageInfo);
		}
				
		return rootElt;
	}
	
}