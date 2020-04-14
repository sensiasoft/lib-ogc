/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2016-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseReader;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Reader for XML InsertTaskingTemplate response for SPS v2.0 
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class InsertTaskingTemplateResponseReaderV20 extends SWEResponseReader<InsertTaskingTemplateResponse>
{
		
    @Override
    public InsertTaskingTemplateResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
	    InsertTaskingTemplateResponse response = new InsertTaskingTemplateResponse();
		response.setVersion("2.0");
		
		// session id
		String sessionID = dom.getElementValue("session");
		response.setSessionID(sessionID);
		
		// read extensions
		readXMLExtensions(dom, responseElt, response);
		
		return response;
	}	
}