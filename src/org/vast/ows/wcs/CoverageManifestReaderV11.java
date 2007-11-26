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

import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * Coverage Manifest Reader v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse an XML Coverage Manifest response and
 * create a CoverageManifest object for version 1.1
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 11, 2007
 * @version 1.0
 */
public class CoverageManifestReaderV11 implements OWSResponseReader<CoverageManifest>
{
	protected OWSReferenceReaderV11 owsReader = new OWSReferenceReaderV11();


	public CoverageManifest readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		CoverageManifest manifest = new CoverageManifest();
		
		// get all Coverage Reference Groups
		NodeList covElts = dom.getElements(responseElt, "InputCoverages/Coverage");
		for (int i=0; i<covElts.getLength(); i++)
		{
			CoverageRefGroup coverageRef = new CoverageRefGroup();
			Element coverageElt = (Element)covElts.item(i);
			
			// read group info
			owsReader.readRefGroupInfo(dom, coverageElt, coverageRef);
			
			// simple references
			NodeList refElts = dom.getElements(coverageElt, "Reference");
			for (int j=0; j<refElts.getLength(); j++)
			{
				Element refElt = (Element)refElts.item(j);
				OWSReference ref = owsReader.readXML(dom, refElt);
				coverageRef.getReferenceList().add(ref);
			}
			
			// service references
			refElts = dom.getElements(coverageElt, "ServiceReference");
			for (int j=0; j<refElts.getLength(); j++)
			{
				Element refElt = (Element)refElts.item(j);
				OWSReference ref = owsReader.readXML(dom, refElt);
				coverageRef.getReferenceList().add(ref);
			}
			
			manifest.getCoverages().add(coverageRef);
		}
		
		return manifest;
	}
}