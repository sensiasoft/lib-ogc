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
 * Provides methods to parse a KVP or SOAP/XML SOS GetObservation
 * request and create a GetObservationRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Aug 1, 2012
 * */
public class GetObservationReaderV20 extends SWERequestReader<GetObservationRequest>
{
    protected FESUtils fesUtils = new FESUtils(FESUtils.V2_0);
    
    
    public GetObservationReaderV20()
	{
	}
    
    
    @Override
	public GetObservationRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetObservationRequest request = new GetObservationRequest();
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
			    request.getOfferings().clear();
                for (String off: argValue.split(","))
                    request.getOfferings().add(off);
			}
			
			// observed properties
            else if (argName.equalsIgnoreCase("observedProperty"))
            {
                request.getObservables().clear();
                for (String obs: argValue.split(","))
                    request.getObservables().add(obs);
            }
			
			// procedures
            else if (argName.equalsIgnoreCase("procedure"))
            {
                request.getProcedures().clear();
                for (String proc: argValue.split(","))
                    request.getProcedures().add(proc);
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
	public GetObservationRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetObservationRequest request = new GetObservationRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// procedures
        NodeList procList = dom.getElements(requestElt, "procedure");
        for (int i = 0; i < procList.getLength(); i++)
        {
            String val = dom.getElementValue((Element)procList.item(i));
            request.getProcedures().add(val);
        }
        
        // offerings
        NodeList offList = dom.getElements(requestElt, "offering");
        for (int i = 0; i < offList.getLength(); i++)
        {
            String val = dom.getElementValue((Element)offList.item(i));
            request.getOfferings().add(val);
        }        
		
        // observables
		NodeList obsList = dom.getElements(requestElt, "observedProperty");
		for (int i = 0; i < obsList.getLength(); i++)
		{
			String val = dom.getElementValue((Element)obsList.item(i));
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
            String val = dom.getElementValue((Element)foiList.item(i));
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
            throw new OWSException(SOSException.invalid_param_code, "spatialFilter", e);
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
    protected void checkParameters(GetObservationRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);		
		report.process();
    }
}