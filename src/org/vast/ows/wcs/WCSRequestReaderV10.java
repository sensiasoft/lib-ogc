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

import java.util.StringTokenizer;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * WCS Request Reader v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST WCS request and
 * create an WCSQuery object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin, Tony Cook
 * @date Nov 17, 2005
 * @version 1.0
 */
public class WCSRequestReaderV10 extends WCSRequestReader
{
	
    public WCSRequestReaderV10()
	{	
	}

	
	@Override
	public WCSQuery readURLQuery(String queryString) throws OWSException
	{
		WCSQuery query = new WCSQuery();
		StringTokenizer st = new StringTokenizer(queryString, "&");
        
        while (st.hasMoreTokens())
        {
            String argName = null;
            String argValue = null;
            String nextArg = st.nextToken();

            // separate argument name and value
            try
            {
                int sepIndex = nextArg.indexOf('=');
                argName = nextArg.substring(0, sepIndex);
                argValue = nextArg.substring(sepIndex + 1);
            }
            catch (IndexOutOfBoundsException e)
            {
                throw new WCSException(invalidGet);
            }
            
            // service ID
            if (argName.equalsIgnoreCase("service"))
            {
                query.setService(argValue);
            }
            
            // service version
            else if (argName.equalsIgnoreCase("version"))
            {
                query.setVersion(argValue);
            }

            // request argument
            else if (argName.equalsIgnoreCase("request"))
            {
                query.setRequest(argValue);
            }

            // format argument
            else if (argName.equalsIgnoreCase("format"))
            {
                query.setFormat(argValue);
            }
            
            // time
            else if (argName.equalsIgnoreCase("time"))
            {
                this.parseTimeArg(query.getTime(), argValue);
            }
            
            // bbox
            else if (argName.equalsIgnoreCase("bbox"))
            {
                this.parseBboxArg(query.getBbox(), argValue);
            }

            else
                throw new WCSException(invalidGet + ": Unknown Argument " + argName);
        }

        return query;
	}
	
	
	@Override
	public WCSQuery readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		String opName = requestElt.getLocalName();
		WCSQuery query;
		
		if (opName.equalsIgnoreCase("GetCapabilities"))
		{
			query = new WCSQuery();
			readGetCapabilitiesXML(dom, requestElt, query);
		}
		
		else if (opName.equalsIgnoreCase("GetCoverage"))
			query = readGetCoverageXML(dom, requestElt);
		
		else throw new WCSException("Operation " + opName + "not supported");
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, query);		
		
		return query;
	}
	
	
	/**
	 * Reads a GetCoverage XML request and fill up the WCSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @return
	 * @throws WCSException
	 */
	protected WCSQuery readGetCoverageXML(DOMHelper dom, Element requestElt) throws OWSException
	{
		WCSQuery query = new WCSQuery();
		
        // TODO WCSRequestReader v1.0 readGetCoverageXML
        
		return query;
	}
}