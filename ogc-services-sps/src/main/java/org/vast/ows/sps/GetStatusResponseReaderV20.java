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

import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseReader;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Used to read a version 2.0 SPS GetStatus Response into
 * a GetStatusResponse object
 * </p>
 *
 * @author Alex Robin
 * @date Feb, 29 2008
 * */
public class GetStatusResponseReaderV20 extends SWEResponseReader<GetStatusResponse>
{
	protected SPSCommonReaderV20 commonReader = new SPSCommonReaderV20();
	
	
	@Override
    public GetStatusResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
	    GetStatusResponse response = new GetStatusResponse();
	    
        try
		{
			response.setVersion("2.0");
            
            // status reports
            NodeList reportElts = dom.getElements(responseElt, "status/*");
            for (int i=0; i<reportElts.getLength(); i++)
            {
                Element reportElt = (Element)reportElts.item(i);
            	StatusReport report = (StatusReport)commonReader.readReport(dom, reportElt);
                response.getReportList().add(report);
            }
            
            // read extensions
            readXMLExtensions(dom, responseElt, response);
            
            return response;
		}
		catch (Exception e)
		{
			throw new SPSException(READ_ERROR_MSG + response.getMessageType(), e);
		}
	}	
}
