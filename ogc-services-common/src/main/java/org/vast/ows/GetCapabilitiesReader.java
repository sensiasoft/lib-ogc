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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;


/**
 * <p>
 * Provides methods to parse a KVP or XML GetCapabilities request and
 * create a GetCapabilities object for all version
 * </p>
 *
 * @author Alex Robin
 * @since Sep 21, 2007
 * */
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
	public GetCapabilitiesRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport();
		GetCapabilitiesRequest request = new GetCapabilitiesRequest();
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
        
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
            
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
            
            // accepted versions
            else if (argName.equalsIgnoreCase("acceptVersions"))
            {
                request.getAcceptedVersions().clear();
                for (String version: argValue.split(","))
                    request.getAcceptedVersions().add(version);
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

        checkParameters(request, report, service);
        report.process();
        
        return request;
	}
	
	
	@Override
	public GetCapabilitiesRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport();
		GetCapabilitiesRequest request = new GetCapabilitiesRequest();
		readCommonXML(dom, requestElt, request);
		
		// section
		request.setSection(dom.getElementValue(requestElt, "section"));
		
		// accepted versions
        NodeList versionList = dom.getElements(requestElt, "acceptVersions");
        for (int i = 0; i < versionList.getLength(); i++)
        {
            String val = dom.getElementValue((Element)versionList.item(i));
            request.getAcceptedVersions().add(val);
        }
		
		checkParameters(request, report, service);
		report.process();
		
		return request;
	}
	
	
	/**
	 * Checks that mandatory parameters are present
	 * @param request
	 * @param report
	 */
	protected void checkParameters(GetCapabilitiesRequest request, OWSExceptionReport report, String serviceType) throws OWSException
	{
		// need SERVICE
		if (request.getService() == null)
			report.add(new OWSException(OWSException.missing_param_code, "service"));
		
		// must be correct service 
		else if (serviceType != null)
		{
			String reqService = request.getService();
			if (!reqService.equalsIgnoreCase(serviceType))
				report.add(new OWSException(OWSException.invalid_param_code, "SERVICE", reqService, ""));
		}
		
		// if version is present, check version validity
		if (request.getVersion() != null)
		{
			if (!request.getVersion().matches(versionRegex))
			{
				OWSException ex = new OWSException(OWSException.invalid_param_code, "VERSION");
				ex.setBadValue(request.getVersion());
				report.add(ex);
			}
		}
		
		// check accept versions validity
		for (String version: request.getAcceptedVersions())
		{
		    if (!version.matches(versionRegex))
            {
                OWSException ex = new OWSException(OWSException.invalid_param_code, "VERSION");
                ex.setBadValue(request.getVersion());
                report.add(ex);
            }
		}
		
		// need REQUEST
		if (request.getOperation() == null)
			report.add(new OWSException(OWSException.missing_param_code, "REQUEST"));
	}
}