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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.io.xml.DOMReader;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * WMS Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST WCS request and
 * create an WCSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin, Tony Cook
 * @date Nov 17, 2005
 * @version 1.0
 */
public class WCSRequestReader extends OWSRequestReader
{
//	 TODO WCSRequestReader THIS VERSION IS JUST A COPY OF WMS
	public WCSRequestReader()
	{	
	}

	
	@Override
	public WCSQuery readGetRequest(String queryString)
	{
		WCSQuery query = new WCSQuery();
		
		return query;
	}
	
	
	@Override
	public WCSQuery readRequestXML(DOMReader domReader, Element requestElt) throws WCSException
	{
		String opName = requestElt.getLocalName();
		WCSQuery query;
		
		if (opName.equalsIgnoreCase("GetCapabilities"))
		{
			query = new WCSQuery();
			readGetCapabilitiesXML(domReader, requestElt, query);
		}
		
		else if (opName.equalsIgnoreCase("GetCoverage"))
			query = readGetCoverageXML(domReader, requestElt);
		
		else throw new WCSException("Operation " + opName + "not supported");
		
		// do common stuffs like version, request name and service type
		readCommonXML(domReader, requestElt, query);		
		
		return query;
	}
	
	
	/**
	 * Reads a GetCoverage XML request and fill up the WCSQuery accordingly
	 * @param domReader
	 * @param requestElt
	 * @return
	 * @throws WCSException
	 */
	protected WCSQuery readGetCoverageXML(DOMReader domReader, Element requestElt) throws WCSException
	{
		WCSQuery query = new WCSQuery();
		
		return query;
	}
	
	
	/**
	 * Parses comma separated BBOX coordinates and set corresponding fields in WCQuery
	 * @param query
	 * @param coordText
	 */
	protected void parseBbox(WCSQuery query, String coordText)
	{
		String[] coords = coordText.split("[ ,]");
		query.getBbox().setMinX(Double.parseDouble(coords[0]));
		query.getBbox().setMinY(Double.parseDouble(coords[1]));
		query.getBbox().setMaxX(Double.parseDouble(coords[2]));
		query.getBbox().setMaxY(Double.parseDouble(coords[3]));
	}
}