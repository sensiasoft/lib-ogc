/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseReader;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Reader for XML InsertObservation response for SOS v2.0 
 * </p>
 *
 * @author Alex Robin
 * @date Feb, 19 2014
 * */
public class InsertObservationResponseReaderV20 extends SWEResponseReader<InsertObservationResponse>
{
		
    @Override
    public InsertObservationResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
	    InsertObservationResponse response = new InsertObservationResponse();
		response.setVersion("2.0");		
		
		// read extensions
		readXMLExtensions(dom, responseElt, response);
		
		// assigned procedure
		String obsId = dom.getElementValue(responseElt, "observation");
		response.setObsId(obsId);
        
		return response;
	}	
}