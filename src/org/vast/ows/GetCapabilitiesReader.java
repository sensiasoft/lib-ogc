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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.util.StringTokenizer;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;


/**
 * <p><b>Title:</b><br/>
 * GetCapabilities Request Reader v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or XML GetCapabilities request and
 * create a GetCapabilities object for all version
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Sep 21, 2007
 * @version 1.0
 */
public class GetCapabilitiesReader extends AbstractRequestReader<GetCapabilitiesRequest>
{
	protected String service;
	
	
    public GetCapabilitiesReader()
	{	
	}
    
    
    public GetCapabilitiesReader(String service)
	{
    	this.service = service;
	}

	
	@Override
	public GetCapabilitiesRequest readURLQuery(String queryString) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport();
		GetCapabilitiesRequest request = new GetCapabilitiesRequest();
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
                throw new OWSException(invalidKVP);
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

            // section argument
            else if (argName.equalsIgnoreCase("section"))
            {
                request.setSection(argValue);
            }
        }

        super.checkParameters(request, report, service);
        report.process();
        
        return request;
	}
	
	
	@Override
	public GetCapabilitiesRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport();
		GetCapabilitiesRequest request = new GetCapabilitiesRequest();
		readCommonXML(dom, requestElt, request);
		request.setSection(dom.getElementValue(requestElt, "section"));
		
		super.checkParameters(request, report, service);
		report.process();
		
		return request;
	}
}