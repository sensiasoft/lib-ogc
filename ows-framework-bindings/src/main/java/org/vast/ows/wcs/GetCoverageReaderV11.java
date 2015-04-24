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
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;


/**
 * <p>
 * Provides methods to parse a KVP or XML GetCoverage request and
 * create a GetCoverage object for version 1.1.1
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Sep 21, 2007
 * */
public class GetCoverageReaderV11 extends AbstractRequestReader<GetCoverageRequest>
{
	protected WCSCommonReaderV11 wcsReader = new WCSCommonReaderV11();
	protected OWSCommonReaderV11 owsReader = new OWSCommonReaderV11();
	
	
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
	            else if (argName.equalsIgnoreCase("identifier"))
	            {
	                request.setCoverage(argValue);
	            }
	            
	            // Sequence of time periods or instants
	            else if (argName.equalsIgnoreCase("TimeSequence"))
	            {
	            	String[] timeList = argValue.split(",");
	                for (int i=0; i<timeList.length; i++)
	                {
	                	TimeExtent newTime = parseTimeArg(timeList[i]);
	                	request.getTimes().add(newTime);
	                }
	            }
	            
	            // BoundingBox + integrated CRS
	            else if (argName.equalsIgnoreCase("BoundingBox"))
	            {
	            	Bbox bbox = parseBboxArg(argValue);
	                request.setBbox(bbox);
	            }
	            
	            // RangeSubset
	            else if (argName.equalsIgnoreCase("RangeSubset"))
	            {
	            	this.parseRangeSubset(request, argValue);
	            }
	            
	            // GridBaseCRS
	            else if (argName.equalsIgnoreCase("GridBaseCRS"))
	            {
	            	request.getGridCrs().setBaseCrs(argValue);
	            }
	            
	            // GridCS
	            else if (argName.equalsIgnoreCase("GridCS"))
	            {
	            	request.getGridCrs().setGridCs(argValue);
	            }
	            
	            // GridType
	            else if (argName.equalsIgnoreCase("GridType"))
	            {
	            	request.getGridCrs().setGridType(argValue);
	            }
	            
	            // GridOrigin
	            else if (argName.equalsIgnoreCase("GridOrigin"))
	            {
	            	double[] origin = parseVector(argValue);
	            	request.getGridCrs().setGridDimension(origin.length);
	            	request.getGridCrs().setGridOrigin(origin);
	            }
	            
	            // GridOffsets
	            else if (argName.equalsIgnoreCase("GridOffsets"))
	            {
	            	double[][] vectors = WCSCommonReaderV11.parseGridOffsets(argValue);
	            	request.getGridCrs().setGridOffsets(vectors);
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
	
	
	protected void parseRangeSubset(GetCoverageRequest query, String kvpToken)
	{
		// TODO parse range subset in KVP
	}
	
	
	@Override
	public GetCoverageRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		GetCoverageRequest request = new GetCoverageRequest();
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI("OWS", "1.1"));		
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// coverage identifier
		String covID = dom.getElementValue(requestElt, "Identifier");
		request.setCoverage(covID);
				
		////// domain subset //////
		Element domainElt = dom.getElement(requestElt, "DomainSubset");
		
		// bbox
		Element bboxElt = dom.getElement(domainElt, "ows:BoundingBox");
		if (bboxElt != null)
		{
			Bbox bbox = owsReader.readBbox(dom, bboxElt);
			request.setBbox(bbox);
		}
		
		// temporal subset
		NodeList timeElts = dom.getElements(domainElt, "TemporalSubset/*");
		for (int i=0; i<timeElts.getLength(); i++)
		{
			Element timeElt = (Element)timeElts.item(i);
			
			if (timeElt.getLocalName().equals("timePosition"))
			{
				try
				{
				    double time = timeFormat.parseIso(dom.getElementValue(timeElt));
				    request.getTimes().add(new TimeExtent(time));
				}
				catch (ParseException e)
				{
					report.add(new WCSException(invalidXML + ": Invalid Time Position"));
				}
			}
			
			else if (timeElt.getLocalName().equals("TimePeriod"))
			{
				TimeExtent timeRange = new TimeExtent();
				String beginPos = dom.getElementValue(timeElt, "BeginPosition");
				String endPos = dom.getElementValue(timeElt, "EndPosition");
				
				try
				{
					double begin = timeFormat.parseIso(beginPos);
					timeRange.setStartTime(begin);
				}
				catch (ParseException e)
				{
					throw new WCSException(invalidXML + ": " + invalidValue + " BeginPosition: " + beginPos);
				}
				
				try
				{
					double end = timeFormat.parseIso(endPos);
					timeRange.setStopTime(end);
				}
				catch (ParseException e)
				{
					throw new WCSException(invalidXML + ": " + invalidValue + " EndPosition: " + endPos);
				}
				
				String timeRes = dom.getElementValue(timeElt, "TimeResolution");
				if (timeRes != null)
				{
					double step = 0;
					
					try
					{
						if (timeRes.contains("P"))
							step = timeFormat.parseIsoPeriod(timeRes);
						else
							step = Double.parseDouble(timeRes);
						
						timeRange.setTimeStep(step);
					}
					catch (Exception e)
					{
						throw new WCSException(invalidXML + ": " + invalidValue + " TimeResolution: " + timeRes);
					}
				}
				
				request.getTimes().add(timeRange);
			}
			
			else
				throw new WCSException(invalidXML + ": Invalid Time Subset: " + timeElt.getLocalName());
		}

		
		////// range subset (support only one field!) //////
		NodeList fieldElts = dom.getElements(requestElt, "RangeSubset/FieldSubset");
		for (int j=0; j<fieldElts.getLength(); j++)
		{
			Element fieldElt = (Element)fieldElts.item(j);
			FieldSubset field = new FieldSubset();
			request.getFieldSubsets().add(field);
			
			// field identifier
			field.setIdentifier(dom.getElementValue(fieldElt, "Identifier"));
			
			// interpolation method
			String interpolationMethod = dom.getElementValue(fieldElt, "InterpolationType");
			if (interpolationMethod != null)
				field.setInterpolationMethod(interpolationMethod);
			
			// all field axes
			NodeList axisElts = dom.getElements(fieldElt, "AxisSubset");
			for (int k=0; k<axisElts.getLength(); k++)
			{
				Element axisElt = (Element)axisElts.item(k);
				AxisSubset axisSubset = new AxisSubset();
				field.getAxisSubsets().add(axisSubset);
				
				// title, abstract elts
				axisSubset.setIdentifier(dom.getElementValue(axisElt, "Identifier"));
				
				// keys
				NodeList keyElts = dom.getElements(axisElt, "Key");
				for (int h=0; h<keyElts.getLength(); h++)
				{
					Element keyElt = (Element)keyElts.item(h);
					String key = dom.getElementValue(keyElt);
					axisSubset.getKeys().add(key);
				}
			}
		}
		
		////// Output //////
		Element outputElt = dom.getElement(requestElt, "Output");
		
		// format
		String format = dom.getAttributeValue(outputElt, "@format");
		request.setFormat(format);
		
		// store
		String store = dom.getAttributeValue(outputElt, "@store");
		if (store != null && store.equalsIgnoreCase("true"))
			request.setStore(true);
		else
			request.setStore(false);
		
		// grid crs
		Element gridElt = dom.getElement(outputElt, "GridCRS");
		if (gridElt != null)
		{
			WCSRectifiedGridCrs gridCrs = new WCSRectifiedGridCrs();
			wcsReader.readGridCRS(dom, gridElt, gridCrs);
		}
		
		this.checkParameters(request, report);
		return request;
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
			report.add(new OWSException(OWSException.missing_param_code, "Identifier"));
		
		// need at least BBOX or TIME
		if (request.getBbox() == null && request.getTime() == null)
			report.add(new OWSException(OWSException.missing_param_code, "TimeSequence/BoundingBox"));
		
		// need crs
		if (request.getBbox() != null)
		{
			if (request.getBbox().getCrs() == null)
				report.add(new OWSException(OWSException.missing_param_code, "CRS"));
			
			// copy crs to responseCrs if needed
			else if (request.getGridCrs().getBaseCrs() == null && request.getBbox() != null)
				request.getGridCrs().setBaseCrs(request.getBbox().getCrs());
		}
		
		// TODO check Grid info??
		//if (request.getWidth() < 0 && Double.isNaN(request.getResX()))
		//	list.add(new OWSException(OWSException.missing_param_code, "WIDTH/HEIGHT/RESX/RESY"));
		
		// need format
		if (request.getFormat() == null)
			report.add(new OWSException(OWSException.missing_param_code, "Format"));
				
		report.process();
	}
}