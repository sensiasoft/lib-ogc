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

import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p>
 * Provides methods to parse an XML Coverage Manifest response and
 * create a CoverageManifest object for version 1.1.1
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Oct 11, 2007
 * */
public class CoverageManifestReaderV11 implements OWSResponseReader<CoverageManifest>
{
	protected OWSCommonReaderV11 owsReader = new OWSCommonReaderV11();


	public CoverageManifest readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		CoverageManifest manifest = new CoverageManifest();
		manifest.setVersion("1.1.1");
		
		// get all Coverage Reference Groups
		NodeList covElts = dom.getElements(responseElt, "InputCoverages/Coverage");
		for (int i=0; i<covElts.getLength(); i++)
		{
			CoverageRefGroup coverageRef = new CoverageRefGroup();
			Element coverageElt = (Element)covElts.item(i);			
			owsReader.readReferenceGroup(dom, coverageElt, coverageRef);			
			manifest.getCoverages().add(coverageRef);
		}
		
		return manifest;
	}
}