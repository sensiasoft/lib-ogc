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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p>
 * Provides methods to parse a KVP or SOAP/XML SOS GetResultTemplate
 * request and create a GetResultTemplateRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Aug 8, 2012
 * */
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
			
			// format
            else if (argName.equalsIgnoreCase("responseFormat"))
                request.setFormat(argValue);
            
			// observed properties (only one officially supported by SOS 2.0!)
            else if (argName.equalsIgnoreCase("observedProperty"))
            {
                request.getObservables().clear();
                for (String obs: argValue.split(","))
                    request.getObservables().add(obs);
            }
			
			// vendor parameters
            else
            {
                if (argValue == null)
                    argValue = "";
                addKVPExtension(argName, argValue, request);
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
		
        // observed properties (only one officially supported by SOS 2.0!)
        NodeList obsList = dom.getElements(requestElt, "observedProperty");
        for (int i = 0; i < obsList.getLength(); i++)
        {
            val = dom.getElementValue((Element)obsList.item(i));
            request.getObservables().add(val);
        }

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
        if (request.getObservables().isEmpty())
            report.add(new OWSException(OWSException.missing_param_code, "observedProperty"));
        
        report.process();
    }
}