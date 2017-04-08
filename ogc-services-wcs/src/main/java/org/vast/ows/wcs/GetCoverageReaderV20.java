/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.text.ParseException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p>
 * Provides methods to parse a KVP or XML GetCoverage request and
 * create a GetCoverage object for version 2.0.0
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Aug 16, 2011
 * */
public class GetCoverageReaderV20 extends AbstractRequestReader<GetCoverageRequest>
{
    	
	@Override
	public GetCoverageRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetCoverageRequest request = new GetCoverageRequest();
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
        
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
            
            try
			{
	            // service
	            if (argName.equalsIgnoreCase("service"))
	            {
	                request.setService(argValue);
	            }
	            
	            // version
	            else if (argName.equalsIgnoreCase("version"))
	            {
	                request.setVersion(argValue);
	            }
	
	            // request
	            else if (argName.equalsIgnoreCase("request"))
	            {
	                request.setOperation(argValue);
	            }
	            
	            // Coverage Identifier
	            else if (argName.equalsIgnoreCase("coverageId"))
	            {
	                request.setCoverage(argValue);
	            }
	            
	            // each subset argument subsets along one dimension
	            else if (argName.equalsIgnoreCase("subset"))
	            {
	            	request.getDimensionSubsets().add(parseKVPDimensionSubset(argValue));
	            }
	            
	            // format
	            else if (argName.equalsIgnoreCase("format"))
	            {
	                request.setFormat(argValue);
	            }
	            
	            // other axis subsets and vendor specific parameters
	            else
	            	addKVPExtension(argName, argValue, request);
			}
            catch (Exception e)
			{
				report.add(new WCSException(OWSException.invalid_param_code, argName.toUpperCase(), argValue, null));
			}
        }
		
        report.process();
        this.checkParameters(request, report);
        return request;
	}
	
	
	protected DimensionSubset parseKVPDimensionSubset(String kvpToken) throws ParseException
	{
		DimensionSubset subset = new DimensionSubset();
		Matcher m;
		
		m = Pattern.compile("^(.+?)(,(.+?))?\\((.+?)\\)$").matcher(kvpToken);
		if (m.matches())
		{
			// axis/dimension
			subset.setAxis(m.group(1));
		
			// crs
			subset.setCrs(m.group(3));
		
			// slice/trim values
			String[] bounds = m.group(4).split(",");
			subset.setMin(parseNumberOrTime(bounds[0]));
			if (bounds.length > 1)
				subset.setMax(parseNumberOrTime(bounds[1]));
			else
				subset.setMax(parseNumberOrTime(bounds[0]));
		}
		else
			throw new IllegalArgumentException();
		
		return subset;
	}
	
	
	@Override
	public GetCoverageRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetCoverageRequest request = new GetCoverageRequest();
				
		// read common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// coverage identifier
		String covID = dom.getElementValue(requestElt, "CoverageId");
		request.setCoverage(covID);
				
		// dimension subsets
		NodeList dimSubsetElts = dom.getChildElements(requestElt);
		for (int i = 0; i < dimSubsetElts.getLength(); i++)
		{
			Element dimSubsetElt = (Element)dimSubsetElts.item(i);
			String eltName = dimSubsetElt.getLocalName();
			
			if (eltName.startsWith("Dimension"))
			{
				DimensionSubset subset = new DimensionSubset();
				String axisName = dom.getElementValue(dimSubsetElt, "Dimension");
				subset.setAxis(axisName);
				
				try
				{
					if (eltName.equals("DimensionTrim"))
					{
						double low = parseNumberOrTime(dom.getElementValue(dimSubsetElt, "TrimLow"));
						subset.setMin(low);						
						double high = parseNumberOrTime(dom.getElementValue(dimSubsetElt, "TrimHigh"));
						subset.setMax(high);
					}
					
					else if (eltName.equals("DimensionSlice"))
					{
						double val = parseNumberOrTime(dom.getElementValue(dimSubsetElt, "SlicePoint"));
						subset.setMin(val);
						subset.setMax(val);
					}
					
					else
						continue;
				}
				catch (Exception e)
				{
					report.add(new WCSException(OWSException.invalid_param_code, "DimensionSubset"));
				}
				
				request.getDimensionSubsets().add(subset);
			}
		}
		
		// format extension
		
		this.checkParameters(request, report);
		return request;
	}
	
	
	protected double parseNumberOrTime(String text) throws ParseException
	{
		try
		{
			return Double.parseDouble(text);
		}
		catch (NumberFormatException e)
		{
			if (text.startsWith("\""))
				text.replace("\"", "");
			return timeFormat.parseIso(text);
		}
	}
	
	
	/**
     * Checks that GetCoverage mandatory parameters are present
     * @param request
     * @throws OWSException
     */
	protected void checkParameters(GetCoverageRequest request, OWSExceptionReport report) throws OWSException
    {
		// check common params
		super.checkParameters(request, report, OWSUtils.WCS);
		
		// need coverage
		if (request.getCoverage() == null)
			report.add(new OWSException(OWSException.missing_param_code, "CoverageId"));		
		
		// need format
		//if (request.getFormat() == null)
		//	report.add(new OWSException(OWSException.missing_param_code, "Format"));
				
		report.process();
	}
}