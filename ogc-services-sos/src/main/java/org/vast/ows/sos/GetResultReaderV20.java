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
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.BinaryTemporalOp;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSUtils;
import org.vast.ows.fes.FESUtils;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p>
 * Provides methods to parse a KVP or SOAP/XML SOS GetResult
 * request and create a GetResultRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Aug 1, 2012
 * */
public class GetResultReaderV20 extends SWERequestReader<GetResultRequest>
{
    protected FESUtils fesUtils = new FESUtils(FESUtils.V2_0);
    
    
    public GetResultReaderV20()
	{        
	}
    
    
    @Override
	public GetResultRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetResultRequest request = new GetResultRequest();
		readCommonQueryArguments(queryParameters, request);
				
		// parse namespaces
        Map<String, String> namespaceMap = null;
        String nsList = queryParameters.remove("namespaces");
        if (nsList != null)
            namespaceMap = fesUtils.readKVPNamespaces(nsList);
        
        // parse other params
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
		while (it.hasNext())
		{
		    Entry<String, String> item = it.next();
		    String argName = item.getKey();
			String argValue = item.getValue();
			
			// offering argument
			if (argName.equalsIgnoreCase("offering"))
			{
			    request.setOffering(argValue);
			}
			
			// observed properties (only one officially supported by SOS 2.0!)
            else if (argName.equalsIgnoreCase("observedProperty"))
            {
                request.getObservables().clear();
                for (String obs: argValue.split(","))
                    request.getObservables().add(obs);
            }
			
			// features of interest
            else if (argName.equalsIgnoreCase("featureOfInterest"))
            {
                request.getFoiIDs().clear();                    
                for (String foi: argValue.split(","))
                    request.getFoiIDs().add(foi);
            }
			
			// temporal filter
            else if (argName.equalsIgnoreCase("temporalFilter"))
            {
                try
                {
                    BinaryTemporalOp filter = fesUtils.readKVPTemporalFilter(argValue, namespaceMap);
                    request.setTemporalFilter(filter);
                }
                catch (Exception e)
                {
                    throw new SOSException(SOSException.invalid_param_code, "temporalFilter", e);
                }
            }
            
            // spatial filter
            else if (argName.equalsIgnoreCase("spatialFilter"))
            {
                try
                {
                    BinarySpatialOp filter = fesUtils.readKVPSpatialFilter(argValue, namespaceMap);
                    request.setSpatialFilter(filter);
                }
                catch (Exception e)
                {
                    throw new SOSException(SOSException.invalid_param_code, "spatialFilter", e);
                }
            }
			
			// xml wrapper
            else if (argName.equalsIgnoreCase("xmlWrapper"))
            {
                try
                {
                    request.setXmlWrapper(Boolean.parseBoolean(argValue));
                }
                catch (Exception e)
                {
                    throw new SOSException(SOSException.invalid_param_code, "xmlWrapper", e);
                }
            }
			
			// format argument
            else if (argName.equalsIgnoreCase("responseFormat"))
            {
                request.setFormat(argValue);
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
	public GetResultRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetResultRequest request = new GetResultRequest();
		
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
		
        // temporal filter
        try
        {
            Element timeOpElt = dom.getElement(requestElt, "temporalFilter/*");
            if (timeOpElt != null)
            {
                BinaryTemporalOp filter = fesUtils.readXMLTemporalFilter(timeOpElt);
                request.setTemporalFilter(filter);
            }
        }
        catch (Exception e)
        {
            throw new SOSException(SOSException.invalid_param_code, "temporalFilter", e);
        }
        
        // features of interest
        NodeList foiList = dom.getElements(requestElt, "featureOfInterest");
        for (int i = 0; i < foiList.getLength(); i++)
        {
            val = dom.getElementValue((Element)foiList.item(i));
            request.getFoiIDs().add(val);
        }
        
        // spatial filter
        try
        {
            Element spatialOpElt = dom.getElement(requestElt, "spatialFilter/*");
            if (spatialOpElt != null)
            {
                BinarySpatialOp filter = fesUtils.readXMLSpatialFilter(spatialOpElt);
                request.setSpatialFilter(filter);
            }
        }
        catch (Exception e)
        {
            throw new SOSException(SOSException.invalid_param_code, "spatialFilter", e);
        }
        
        // response format
        String resFormat = dom.getElementValue(requestElt, "responseFormat");
        request.setFormat(resFormat);

        this.checkParameters(request, report);
        return request;
	}
    
    
    /**
     * Checks that GetObservation mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(GetResultRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);
		
		// need offering
        if (request.getOffering() == null)
            report.add(new OWSException(OWSException.missing_param_code, "offering"));
        
        // need observedProperty
        if (request.getObservables().isEmpty())
            report.add(new OWSException(OWSException.missing_param_code, "observedProperty"));
        
		report.process();
    }
}