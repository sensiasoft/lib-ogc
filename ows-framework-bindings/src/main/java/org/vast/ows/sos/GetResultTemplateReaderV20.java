/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;
import org.w3c.dom.Element;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p><b>Title:</b><br/>
 * SOS GetResultTemplate Request Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or SOAP/XML SOS GetResultTemplate
 * request and create a GetResultTemplateRequest object for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Aug 8, 2012
 * @version 1.0
 */
public class GetResultTemplateReaderV20 extends SWERequestReader<GetResultTemplateRequest>
{
        
    public GetResultTemplateReaderV20()
	{
       
	}

	
	@Override
	public GetResultTemplateRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetResultTemplateRequest request = new GetResultTemplateRequest();
		readCommonQueryArguments(queryParameters, request);
				
		// parse other params
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
		while (it.hasNext())
		{
		    Entry<String, String> item = it.next();
		    String argName = item.getKey();
			String argValue = item.getValue();
			
			// offering argument
			if (argName.equalsIgnoreCase("offering"))
			    request.setOffering(argValue);
			
			// observed property
            else if (argName.equalsIgnoreCase("observedProperty"))
                request.getObservables().add(argValue);

			// vendor parameters
            else
            {
                if (argValue == null)
                    argValue = "";
                request.getExtensions().put(new QName(argName), argValue);
            }
		}

		this.checkParameters(request, report);
		return request;
	}
	
	
	@Override
	public GetResultTemplateRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetResultTemplateRequest request = new GetResultTemplateRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		String val;
		
        // offering
        val = dom.getElementValue(requestElt, "offering");
        request.setOffering(val);        
		
        // observed property
		val = dom.getElementValue(requestElt, "observedProperty");
        request.getObservables().add(val); 

        this.checkParameters(request, report);
        return request;
	}
    
    
    /**
     * Checks that GetObservation mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(GetResultTemplateRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);		
        
        // need offering
        if (request.getOffering() == null)
            report.add(new OWSException(OWSException.missing_param_code, "offering"));
        
        // need observed property
        if (request.getObservables().size() != 1)
            report.add(new OWSException(OWSException.missing_param_code, "observedProperty"));
        
        report.process();
    }
}