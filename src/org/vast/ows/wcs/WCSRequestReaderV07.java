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
import org.vast.ows.util.Bbox;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * WCS Request Reader v0.7
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or XML GetCoverage request and
 * create a GetCoverage object for version 0.7
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin, Tony Cook
 * @date Nov 17, 2005
 * @version 1.0
 */
public class WCSRequestReaderV07 extends AbstractRequestReader<GetCoverageRequest>
{
	
    public WCSRequestReaderV07()
	{	
	}

	
	@Override
	public GetCoverageRequest readURLQuery(String queryString) throws OWSException
	{
		GetCoverageRequest request = new GetCoverageRequest();
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
                throw new WCSException(invalidKVP);
            }
            
            // service ID
            if (argName.equalsIgnoreCase("service"))
            {
                request.setService(argValue);
            }
            
            // service version
            else if (argName.equalsIgnoreCase("version"))
            {
                request.setVersion(argValue);
            }

            // request argument
            else if (argName.equalsIgnoreCase("request"))
            {
                request.setOperation(argValue);
            }

            // format argument
            else if (argName.equalsIgnoreCase("format"))
            {
                request.setFormat(argValue);
            }
            
            // time
            else if (argName.equalsIgnoreCase("time"))
            {
            	TimeInfo time = parseTimeArg(argValue);
            	request.setTime(time);
            }
            
            // bbox
            else if (argName.equalsIgnoreCase("bbox"))
            {
            	Bbox bbox = parseBboxArg(argValue);
                request.setBbox(bbox);
            }

            else
                throw new WCSException(invalidKVP + ": Unknown Argument " + argName);
        }

        return request;
	}
	
	
	@Override
	public GetCoverageRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		GetCoverageRequest request = new GetCoverageRequest();
		
		// TODO readXMLQuery
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);		
		
		return request;
	}
}