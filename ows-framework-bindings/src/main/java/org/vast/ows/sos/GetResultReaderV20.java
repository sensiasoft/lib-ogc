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
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.BinaryTemporalOp;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ows.*;
import org.vast.ows.fes.FESUtils;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p><b>Title:</b><br/>
 * SOS GetResult Request Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or SOAP/XML SOS GetResult
 * request and create a GetResultRequest object for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @date Aug 1, 2012
 * @version 1.0
 */
public class GetResultReaderV20 extends SWERequestReader<GetResultRequest>
{
    protected FESUtils fesUtils = new FESUtils();
    
    
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
			
			// observed property
            else if (argName.equalsIgnoreCase("observedProperty"))
            {
                request.getObservables().add(argValue);
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
                    throw new SOSException(SOSException.invalid_param_code, "temporalFilter", null, null);
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
                    throw new SOSException(SOSException.invalid_param_code, "spatialFilter", null, null);
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
                    throw new SOSException(SOSException.invalid_param_code, "xmlWrapper", argValue, null);
                }
            }

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
		
        // observed property
		val = dom.getElementValue(requestElt, "observedProperty");
        request.getObservables().add(val); 
		
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
            throw new SOSException(SOSException.invalid_param_code, "temporalFilter", null, null);
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
            throw new SOSException(SOSException.invalid_param_code, "spatialFilter", null, null);
        }

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
        if (request.getObservables().size() != 1)
            report.add(new OWSException(OWSException.missing_param_code, "observedProperty"));
        
		report.process();
    }
}