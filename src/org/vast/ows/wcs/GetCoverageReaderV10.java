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
import java.util.StringTokenizer;
import org.vast.util.Bbox;
import org.vast.util.DateTimeFormat;
import org.vast.util.Interval;
import org.vast.util.TimeInfo;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLEnvelopeReader;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * GetCoverage Request Reader v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or XML GetCoverage request and
 * create a GetCoverage object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Sep 21, 2007
 * @version 1.0
 */
public class GetCoverageReaderV10 extends AbstractRequestReader<GetCoverageRequest>
{
	protected GMLEnvelopeReader envelopeReader = new GMLEnvelopeReader();
	protected GMLTimeReader timeReader = new GMLTimeReader();
	
	
	@Override
	public GetCoverageRequest readURLQuery(String queryString) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_10);
		GetCoverageRequest request = new GetCoverageRequest();
		StringTokenizer st = new StringTokenizer(queryString, "&");
        String crs = null;
        
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
            
            // SERVICE
            if (argName.equalsIgnoreCase("SERVICE"))
            {
                request.setService(argValue);
            }
            
            // VERSION
            else if (argName.equalsIgnoreCase("VERSION"))
            {
                request.setVersion(argValue);
            }

            // REQUEST
            else if (argName.equalsIgnoreCase("REQUEST"))
            {
                request.setOperation(argValue);
            }
            
            // COVERAGE
            else if (argName.equalsIgnoreCase("COVERAGE"))
            {
                request.setCoverage(argValue);
            }
            
            // TIME period or time list
            else if (argName.equalsIgnoreCase("TIME"))
            {
                String[] timeList = argValue.split(",");
                for (int i=0; i<timeList.length; i++)
                {
                	TimeInfo newTime = parseTimeArg(timeList[i]);
                	request.getTimes().add(newTime);
                }
            }
            
            // BBOX
            else if (argName.equalsIgnoreCase("BBOX"))
            {
            	Bbox bbox = parseBboxArg(argValue);
                request.setBbox(bbox);
                if (crs != null)
                	bbox.setCrs(crs);
            }
            
            // CRS
            else if (argName.equalsIgnoreCase("CRS"))
            {
            	Bbox bbox = request.getBbox();
            	if (bbox != null)
            		bbox.setCrs(argValue);
            	else
            		crs = argValue;
            }
            
            // RESPONSE_CRS
            else if (argName.equalsIgnoreCase("RESPONSE_CRS"))
            {
            	request.getGridCrs().setBaseCrs(argValue);
            }
            
            // RESX
            else if (argName.equalsIgnoreCase("RESX"))
            {
                try
				{
					double resX = Double.parseDouble(argValue);
					request.setResX(resX);
				}
				catch (NumberFormatException e)
				{
					throw new WCSException(invalidKVP + ": Invalid value for RESX: " + argValue);
				}
            }
            
            // RESY
            else if (argName.equalsIgnoreCase("RESY"))
            {
                try
				{
					double resY = Double.parseDouble(argValue);
					request.setResY(resY);
				}
				catch (NumberFormatException e)
				{
					throw new WCSException(invalidKVP + ": Invalid value for RESY: " + argValue);
				}
            }
            
            // RESZ
            else if (argName.equalsIgnoreCase("RESZ"))
            {
                try
				{
					double resZ = Double.parseDouble(argValue);
					request.setResZ(resZ);
				}
				catch (NumberFormatException e)
				{
					throw new WCSException(invalidKVP + ": Invalid value for RESZ: " + argValue);
				}
            }
            
            // WIDTH
            else if (argName.equalsIgnoreCase("WIDTH"))
            {
                try
				{
					int width = Integer.parseInt(argValue);
					request.setWidth(width);
				}
				catch (NumberFormatException e)
				{
					throw new WCSException(invalidKVP + ": Invalid value for WIDTH: " + argValue);
				}
            }
            
            // HEIGHT
            else if (argName.equalsIgnoreCase("HEIGHT"))
            {
                try
				{
					int height = Integer.parseInt(argValue);
					request.setHeight(height);
				}
				catch (NumberFormatException e)
				{
					throw new WCSException(invalidKVP + ": Invalid value for HEIGHT: " + argValue);
				}
            }
            
            // DEPTH
            else if (argName.equalsIgnoreCase("DEPTH"))
            {
                try
				{
					int depth = Integer.parseInt(argValue);
					request.setDepth(depth);
				}
				catch (NumberFormatException e)
				{
					throw new WCSException(invalidKVP + ": Invalid value for DEPTH: " + argValue);
				}
            }
            
            // INTERPOLATION
            else if (argName.equalsIgnoreCase("INTERPOLATION"))
            {
                request.getFieldSubsets().get(0).setInterpolationMethod(argValue);
            }
            
            // FORMAT
            else if (argName.equalsIgnoreCase("FORMAT"))
            {
                request.setFormat(argValue);
            }
            
            // EXCEPTIONS
            else if (argName.equalsIgnoreCase("EXCEPTIONS"))
            {
                request.setExceptionType(argValue);
            }
            
            // other axis subsets and vendor specific parameters
            else
            {
            	request.getVendorParameters().put(argName.toUpperCase(), argValue);
            }
        }
		
        this.checkParameters(request, report);
        return request;
	}
	
	
	@Override
	public GetCoverageRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_10);
		GetCoverageRequest request = new GetCoverageRequest();
		dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI("GML", "3.1.1"));		
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// source coverage
		String covID = dom.getElementValue(requestElt, "sourceCoverage");
		request.setCoverage(covID);
				
		// envelope
		Element spatialElt = dom.getElement(requestElt, "domainSubset/spatialSubset");
		
		Element envelopeElt = dom.getElement(spatialElt, "gml:Envelope");
		if (envelopeElt != null)
		{
			Bbox bbox = envelopeReader.readEnvelope(dom, envelopeElt);
			request.setBbox(bbox);
		}
		
		// rectified grid
		Element gridElt = dom.getElement(spatialElt, "gml:Grid");
		if (gridElt != null)
		{
			String gridBounds = dom.getElementValue(gridElt, "gml:limits/gml:GridEnvelope/gml:high");
			String[] bounds = gridBounds.split(" ");
			
			try
			{
				int width = Integer.parseInt(bounds[0]);
				request.setWidth(width);
				int height = Integer.parseInt(bounds[1]);
				request.setHeight(height);
				
				if (bounds.length == 3)
				{
					int depth = Integer.parseInt(bounds[2]);
					request.setDepth(depth);
				}
			}
			catch (NumberFormatException e)
			{
				throw new WCSException(invalidXML + ": Invalid value for grid bounds: " + gridBounds);
			}
		}
		
		// temporal subset
		NodeList timeElts = dom.getElements(requestElt, "domainSubset/temporalSubset/*");
		for (int i=0; i<timeElts.getLength(); i++)
		{
			Element timeElt = (Element)timeElts.item(i);
			
			if (timeElt.getLocalName().equals("timePosition"))
			{
				request.getTimes().add(timeReader.readTimeInstant(dom, timeElt));
			}
			else
			{
				TimeInfo timeInfo = new TimeInfo();
				String beginTime = dom.getElementValue(timeElt, "beginTime");
				String endTime = dom.getElementValue(timeElt, "endTime");
				
				try
				{
					double begin = DateTimeFormat.parseIso(beginTime);
					timeInfo.setStartTime(begin);
				}
				catch (ParseException e)
				{
					throw new WCSException(invalidXML + ": Invalid value for beginTime: " + beginTime);
				}
				
				try
				{
					double end = DateTimeFormat.parseIso(endTime);
					timeInfo.setStopTime(end);
				}
				catch (ParseException e)
				{
					throw new WCSException(invalidXML + ": Invalid value for endTime: " + endTime);
				}
				
				String timeRes = dom.getElementValue(timeElt, "timeResolution");
				if (timeRes != null)
				{
					double step = 0;
					
					try
					{
						if (timeRes.contains("P"))
							step = DateTimeFormat.parseIsoPeriod(timeRes);
						else
							step = Double.parseDouble(timeRes);
						
						timeInfo.setTimeStep(step);
					}
					catch (Exception e)
					{
						throw new WCSException(invalidXML + ": Invalid value for timeResolution: " + timeRes);
					}
				}
				
				request.getTimes().add(timeInfo);
			}
		}

		FieldSubset fieldSubset = new FieldSubset();
		fieldSubset.setIdentifier("Field0");
		request.getFieldSubsets().add(fieldSubset);
		
		////// range subset //////
		NodeList axisElts = dom.getElements(requestElt, "rangeSubset/axisSubset");
		for (int i=0; i<axisElts.getLength(); i++)
		{
			AxisSubset axis = new AxisSubset();			
			Element axisElt = (Element)axisElts.item(i);
			String name = dom.getAttributeValue(axisElt, "name");
			axis.setIdentifier(name);
			
			NodeList intervalElts = dom.getElements(axisElt, "interval");
			for (int p=0; p<intervalElts.getLength(); p++)
			{
				Interval interval = new Interval();
				Element intervalElt = (Element)intervalElts.item(p);
				String minText = dom.getElementValue(intervalElt, "min");
				String maxText = dom.getElementValue(intervalElt, "max");
				String resText = dom.getElementValue(intervalElt, "res");
				
				try
				{
					double min = Double.parseDouble(minText);
					interval.setMin(min);
					double max = Double.parseDouble(maxText);
					interval.setMax(max);					
					double res = Double.parseDouble(resText);
					interval.setResolution(res);
				}
				catch (NumberFormatException e)
				{
					throw new WCSException(invalidXML + ": Invalid range interval: " + minText+"/"+maxText+"/");
				}
				axis.getRangeIntervals().add(interval);
			}
			
			NodeList valueElts = dom.getElements(axisElt, "singleValue");
			for (int p=0; p<valueElts.getLength(); p++)
			{
				Element valueElt = (Element)valueElts.item(p);
				String val = dom.getElementValue(valueElt);
				if (val == null)
					throw new WCSException(invalidXML + ": Invalid range subset value: " + val);				
				axis.getKeys().add(val);
			}
			
			fieldSubset.getAxisSubsets().add(axis);
		}	
				
		// interpolation method
		String interpMethod = dom.getElementValue(requestElt, "interpolationMethod");
		fieldSubset.setInterpolationMethod(interpMethod);
		
		// response CRS
		String responseCrs = dom.getElementValue(requestElt, "output/crs");
		request.getGridCrs().setBaseCrs(responseCrs);
		
		// format
		String format = dom.getElementValue(requestElt, "output/format");
		request.setFormat(format);
		
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
		super.checkParameters(request, report, OGCRegistry.WCS);
		
		// need coverage
		if (request.getCoverage() == null)
			report.add(new OWSException(OWSException.missing_param_code, "COVERAGE"));
		
		// need at least BBOX or TIME
		if (request.getBbox() == null && request.getTime() == null)
			report.add(new OWSException(OWSException.missing_param_code, "TIME/BBOX"));
		
		// need crs
		if (request.getBbox() != null)
		{
			if (request.getBbox().getCrs() == null)
				report.add(new OWSException(OWSException.missing_param_code, "CRS"));
			
			// copy crs to responseCrs if needed
			else if (request.getGridCrs().getBaseCrs() == null && request.getBbox() != null)
				request.getGridCrs().setBaseCrs(request.getBbox().getCrs());
		}
		
		// need at least WIDTH or RESX
		if ((request.getWidth() < 0 || request.getHeight() < 0) && Double.isNaN(request.getResX()))
			report.add(new OWSException(OWSException.missing_param_code, "WIDTH/HEIGHT or RESX/RESY"));
		
		// need format
		if (request.getFormat() == null)
			report.add(new OWSException(OWSException.missing_param_code, "FORMAT"));
				
		report.process();
	}
}