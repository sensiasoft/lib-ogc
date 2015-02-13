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
    Kevin Carter <kcarter@nsstc.uah.edu>
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wrs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>
 * </p>
 *
 * @author Tony Cook
 * @date Nov 28, 2006
 * */

public class WRSResponseReader {
	
	public List<String> parseSOSEndpoint(InputStream inputStream) throws IOException, DOMHelperException {
		List<String> sosUri = new ArrayList<String>();
		
        DOMHelper reader = new DOMHelper(inputStream, false);
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
	
	public HashMap<String,String> parseExtrinsicObjects(InputStream inputStream) throws IOException, DOMHelperException {
		HashMap<String,String> eoMap = new HashMap<String,String>();
		
        DOMHelper reader = new DOMHelper(inputStream, false);
		NodeList objList = reader.getElements("SearchResults/ExtrinsicObject");
		for(int i=0; i<objList.getLength(); i++) {
			Element objElt = (Element)objList.item(i);
			//if(skipHyperion(reader, objElt))
			//	continue;
			Element nameElt = reader.getElement(objElt, "Name");
			if(nameElt == null)
				continue;   // skip it
			Element lsElt = reader.getElement(nameElt, "LocalizedString");
			if(lsElt == null) 
				continue;  // skip it
			String name = lsElt.getAttribute("value");
			String id = objElt.getAttribute("id");
			if(id != null)
				eoMap.put(id, name);
		}
		
		return eoMap;
	}
}

