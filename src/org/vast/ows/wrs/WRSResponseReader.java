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
    Kevin Carter <kcarter@nsstc.uah.edu>
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wrs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.vast.io.xml.DOMReader;
import org.vast.io.xml.DOMReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p><b>Title:</b>
 *  WRSResponseReader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2006</p>
 * @author Tony Cook
 * @date Nov 28, 2006
 * @version 1.0
 */

public class WRSResponseReader {
	
	public List<String> parseSOSEndpoint(InputStream inputStream) throws IOException, DOMReaderException {
		List<String> sosUri = new ArrayList<String>();
		
		DOMReader reader = new DOMReader(inputStream, false);
		NodeList svcList = reader.getElements("SearchResults/Service");
		for(int i=0; i<svcList.getLength(); i++) {
			Element svcElt = (Element)svcList.item(i);
			Element svcBindingElt = reader.getElement(svcElt, "ServiceBinding");
			String accessUri = svcBindingElt.getAttribute("accessURI");
			if(accessUri != null) {
				if(!sosUri.contains(accessUri))
					sosUri.add(accessUri);
			}
		}
		return sosUri;
	}
	
	public List<String> parseExtrinsicObjects(InputStream inputStream) throws IOException, DOMReaderException {
		List<String> extObjIds = new ArrayList<String>();
		
		DOMReader reader = new DOMReader(inputStream, false);
		NodeList objList = reader.getElements("SearchResults/ExtrinsicObject");
		for(int i=0; i<objList.getLength(); i++) {
			Element objElt = (Element)objList.item(i);
			//if(skipHyperion(reader, objElt))
			//	continue;
			String id = objElt.getAttribute("id");
			if(id != null)
				extObjIds.add(id);
		}
		
		return extObjIds;
	}
	
	private boolean skipHyperion(DOMReader reader, Element objElt){
		Element nameElt = reader.getElement(objElt, "Name");
		if(nameElt!=null){
			Element lsElt = reader.getElement(nameElt, "LocalizedString");
			if(lsElt != null) {
				String nameAtt = lsElt.getAttribute("value");
				if(nameAtt.contains("EO1"))
					return true;
			}
		}
		return false;
	}
}

