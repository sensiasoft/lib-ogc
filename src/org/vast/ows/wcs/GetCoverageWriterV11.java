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

import java.text.NumberFormat;
import java.util.Enumeration;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.vast.ows.gml.GMLEnvelopeWriter;
import org.vast.ows.util.AxisSubset;
import org.vast.ows.util.Interval;
import org.vast.ows.util.TimeInfo;
import org.w3c.dom.*;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * GetCoverage Request Builder v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to generate a KVP or XML GetCoverage request based
 * on values contained in a GetCoverage object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Sep 21, 2007
 * @version 1.0
 */
public class GetCoverageWriterV11 extends AbstractRequestWriter<GetCoverageRequest>
{
	protected GMLEnvelopeWriter envelopeWriter = new GMLEnvelopeWriter();
	
	
	@Override
	public String buildURLQuery(GetCoverageRequest query) throws OWSException
	{
		StringBuffer urlBuff = new StringBuffer(query.getGetServer());
		
        urlBuff.append("SERVICE=WCS");
        urlBuff.append("&VERSION=" + query.getVersion());
        urlBuff.append("&REQUEST=" + query.getRequest());
        
        // COVERAGE
        urlBuff.append("&COVERAGE=" + query.getCoverage());
        
        // TIME
        if (query.getTime() != null)
        {
            urlBuff.append("&TIME=");
            this.writeTimeArgument(urlBuff, query.getTime());
        }
        
        // BBOX and CRS if specified
        if (query.getBbox() != null)
        {
        	urlBuff.append("&CRS=" + query.getBbox().getCrs());
        	urlBuff.append("&BBOX=");
	        this.writeBboxArgument(urlBuff, query.getBbox());
	        
	        // add either RESX/RESY or WIDTH/HEIGHT
	        if (query.isUseResolution())
	        {	        
	        	urlBuff.append("&RESX=" + query.getResX());
	        	urlBuff.append("&RESY=" + query.getResY());
	        	if (query.getResZ() > 0)
	        		urlBuff.append("&RESZ=" + query.getResZ());
	        }
	        else
	        {
	        	urlBuff.append("&WIDTH=" + query.getWidth());
	        	urlBuff.append("&HEIGHT=" + query.getHeight());
	        	if (query.getDepth() > 0)
	        		urlBuff.append("&DEPTH=" + query.getDepth());
	        }
        }
        
        // {PARAMETERS}
        for (int i=0; i<query.getAxisSubsets().size(); i++)
        {
	        AxisSubset param = query.getAxisSubsets().get(i);
	        urlBuff.append("&" + param.getName().toUpperCase() + "=");
	        
        	if (!param.getRangeIntervals().isEmpty()) // case of interval
	        {
        		// only one interval is supportted in KVP
        		Interval interval = param.getRangeIntervals().get(0);
        		urlBuff.append(interval.getMin());
	        	urlBuff.append("/" + interval.getMax());
	        	if (interval.getResolution() > 0)
	        		urlBuff.append("/" + interval.getResolution());
	        }
	        else if (!param.getRangeValues().isEmpty())
	        {
	        	int listSize = param.getRangeValues().size();
	        	for (int v=0; v<listSize; v++)
	        	{
	        		urlBuff.append(param.getRangeValues().get(v));
	        		if (v < listSize-1)
	        			urlBuff.append(",");
	        	}
	        }
        }
        
        // RESPONSE_CRS
        if (query.getGridCrs() != null)
        	urlBuff.append("&RESPONSE_CRS=" + query.getGridCrs());
        
        // FORMAT
        urlBuff.append("&FORMAT=" + query.getFormat());
        
        // vendor parameters
        Enumeration<String> paramEnum = query.getVendorParameters().keys();
        while (paramEnum.hasMoreElements())
        {
        	String key = paramEnum.nextElement();
        	String val = query.getVendorParameters().get(key);
        	urlBuff.append("&" + key.toUpperCase() + "=" + val);
        }        	
        
        // EXCEPTIONS
        if (query.getExceptionType() != null)
        	urlBuff.append("&EXCEPTIONS=" + query.getExceptionType());
        
        // replace spaces
        String url = urlBuff.toString();
        url = url.replaceAll(" ","%20");
		return url;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, GetCoverageRequest query) throws OWSException
	{
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI("WCS", query.getVersion()));
		dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI("GML", "3.1.1"));
		
		// root element
		Element rootElt = dom.createElement("GetCoverage");
		addCommonXML(dom, rootElt, query);
		
		// source coverage
		dom.setElementValue(rootElt, "sourceCoverage", query.getCoverage());
		
		////// domain subset //////
		Element domainElt = dom.addElement(rootElt, "domainSubset");
		
		// spatial subset
		if (query.getBbox() != null)
		{
			Element spatialElement = dom.addElement(domainElt, "spatialSubset");
			
			// envelope
			Element envelopeElt = envelopeWriter.writeEnvelope(dom, query.getBbox());
			spatialElement.appendChild(envelopeElt);
			
			// grid definition
			if (!query.isUseResolution()) // grid width/height
			{
				Element gridElt = dom.addElement(spatialElement, "gml:Grid");
				dom.setAttributeValue(gridElt, "@dimension", "2");				
				
				Element gridEnvElt = dom.addElement(gridElt, "gml:limits/gml:GridEnvelope");
				dom.setElementValue(gridEnvElt, "gml:low", "0 0");
				dom.setElementValue(gridEnvElt, "gml:high", query.getWidth() + " " + query.getHeight());
				
				dom.setElementValue(gridElt, "+gml:axisName", "u");
				dom.setElementValue(gridElt, "+gml:axisName", "v");
			}		
			else // grid resolution
			{
				
			}
		}
		
		// temporal subset
		if (query.getTime() != null)
		{
			Element temporalElt = dom.addElement(domainElt, "temporalSubset");
			
			for (int i=0; i<query.getTimes().size(); i++)
			{
				TimeInfo timeInfo = query.getTimes().get(i);
				if (timeInfo.isTimeInstant())
				{
					String time = DateTimeFormat.formatIso(timeInfo.getBaseTime(), timeInfo.getTimeZone());
					dom.setElementValue(temporalElt, "+gml:timePosition", time);
				}
				else
				{
					Element periodElt = dom.addElement(temporalElt, "+timePeriod");
					String begin = DateTimeFormat.formatIso(timeInfo.getStartTime(), timeInfo.getTimeZone());
					String end = DateTimeFormat.formatIso(timeInfo.getStopTime(), timeInfo.getTimeZone());
					dom.setElementValue(periodElt, "beginTime", begin);
					dom.setElementValue(periodElt, "endTime", end);
					
					if (timeInfo.getTimeStep() != 0)
					{
						NumberFormat.getNumberInstance().setMaximumFractionDigits(2);
						String step = NumberFormat.getNumberInstance().format(timeInfo.getTimeStep());
						dom.setElementValue(periodElt, "timeResolution", step);
					}
				}
			}
		}
		
		////// range subset //////
		for (int i=0; i<query.getAxisSubsets().size(); i++)
        {
	        AxisSubset param = query.getAxisSubsets().get(i);
			Element axisElt = dom.addElement(rootElt, "rangeSubset/+axisSubset");
			dom.setAttributeValue(axisElt, "@name", param.getName());
			
			// add all intervals
			for (int j=0; j<param.getRangeIntervals().size(); j++)
			{
				Interval interval = param.getRangeIntervals().get(j);
				Element intervalElt = dom.addElement(axisElt, "+interval");
				dom.setElementValue(intervalElt, "min", Double.toString(interval.getMin()));
				dom.setElementValue(intervalElt, "max", Double.toString(interval.getMax()));
				if (interval.getResolution() > 0)
					dom.setElementValue(intervalElt, "res", Double.toString(interval.getResolution()));
			}
			
			// add all single values
			for (int j=0; j<param.getRangeValues().size(); j++)
				dom.setElementValue(axisElt, "+singleValue", param.getRangeValues().get(j));
		}
				
		////// interpolation method //////
		if (query.getInterpolationMethod() != null)
			dom.setElementValue(rootElt, "interpolationMethod", query.getInterpolationMethod());
		
		////// output parameters //////
		Element outputElt = dom.addElement(rootElt, "output");
		
		if (query.getGridCrs() != null)
			dom.setElementValue(outputElt, "crs", query.getGridCrs());
		
		dom.setElementValue(outputElt, "format", query.getFormat());
		
		return rootElt;
	}
}