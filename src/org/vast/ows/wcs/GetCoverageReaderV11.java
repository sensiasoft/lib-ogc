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
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.vast.ows.gml.GMLTimeReader;
import org.vast.ows.util.AxisSubset;
import org.vast.ows.util.Bbox;
import org.vast.ows.util.Interval;
import org.vast.ows.util.TimeInfo;


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
public class GetCoverageReaderV11 extends AbstractRequestReader<GetCoverageRequest>
{
	protected OWSBboxReader bboxReader = new OWSBboxReader();
	protected GMLTimeReader timeReader = new GMLTimeReader();
	
	
	@Override
	public GetCoverageRequest readURLQuery(String queryString) throws OWSException
	{
		GetCoverageRequest query = new GetCoverageRequest();
		StringTokenizer st = new StringTokenizer(queryString, "&");
        
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
            
            // service
            if (argName.equalsIgnoreCase("service"))
            {
                query.setService(argValue);
            }
            
            // version
            else if (argName.equalsIgnoreCase("version"))
            {
                query.setVersion(argValue);
            }

            // request
            else if (argName.equalsIgnoreCase("request"))
            {
                query.setRequest(argValue);
            }
            
            // Coverage Identifier
            else if (argName.equalsIgnoreCase("identifier"))
            {
                query.setCoverage(argValue);
            }
            
            // Sequence of time periods or instants
            else if (argName.equalsIgnoreCase("TimeSequence"))
            {
                String[] timeList = argValue.split(",");
                for (int i=0; i<timeList.length; i++)
                {
                	TimeInfo newTime = new TimeInfo();
                	this.parseTimeArg(newTime, timeList[i]);
                }
            }
            
            // BoundingBox + integrated CRS
            else if (argName.equalsIgnoreCase("BoundingBox"))
            {
            	if (query.getBbox() == null)
            		query.setBbox(new Bbox());
            	this.parseBboxArg(query.getBbox(), argValue);
            }
            
            // RangeSubset
            else if (argName.equalsIgnoreCase("RangeSubset"))
            {
            	this.parseRangeSubset(query, argValue);
            }
            
            // GridBaseCRS
            else if (argName.equalsIgnoreCase("GridBaseCRS"))
            {
            	query.setGridCrs(argValue);
            }
            
            // GridCS
            else if (argName.equalsIgnoreCase("GridCS"))
            {
            	query.setGridCs(argValue);
            }
            
            // GridType
            else if (argName.equalsIgnoreCase("GridType"))
            {
            	query.setGridType(argValue);
            }
            
            // GridOrigin
            else if (argName.equalsIgnoreCase("GridOrigin"))
            {
            	double[] origin = parseVector(argValue);
            	query.setGridDimension(origin.length);
            	query.setGridOrigin(origin);
            }
            
            // GridOffsets
            else if (argName.equalsIgnoreCase("GridOffsets"))
            {
            	double[] offsets = parseVector(argValue);
            	query.setGridOffsets(offsets);
            }
            
            // format
            else if (argName.equalsIgnoreCase("format"))
            {
                query.setFormat(argValue);
            }
            
            // other axis subsets and vendor specific parameters
            else
            {
            	query.getVendorParameters().put(argName.toUpperCase(), argValue);
            }
        }
		
        return query;
	}
	
	
	protected void parseRangeSubset(GetCoverageRequest query, String kvpToken)
	{
		
	}
	
	
	@Override
	public GetCoverageRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		dom.addUserPrefix("ows", OGCRegistry.getNamespaceURI("OWS", "1.1"));
		
		GetCoverageRequest query = new GetCoverageRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, query);
		
		// coverage identifier
		String covID = dom.getElementValue(requestElt, "Identifier");
		query.setCoverage(covID);
				
		////// domain subset //////
		Element domainElt = dom.getElement(requestElt, "DomainSubset");
		
		// bbox
		Element bboxElt = dom.getElement(domainElt, "ows:BoundingBox");
		if (bboxElt != null)
		{
			Bbox bbox = bboxReader.readBbox(dom, bboxElt);
			query.setBbox(bbox);
		}
		
		// temporal subset
		NodeList timeElts = dom.getElements(domainElt, "TemporalSubset/*");
		for (int i=0; i<timeElts.getLength(); i++)
		{
			Element timeElt = (Element)timeElts.item(i);
			
			if (timeElt.getLocalName().equals("timePosition"))
			{
				query.getTimes().add(timeReader.readTimeInstant(dom, timeElt));
			}
			
			else if (timeElt.getLocalName().equals("TimePeriod"))
			{
				TimeInfo timeRange = new TimeInfo();
				String beginPos = dom.getElementValue(timeElt, "BeginPosition");
				String endPos = dom.getElementValue(timeElt, "EndPosition");
				
				try
				{
					double begin = DateTimeFormat.parseIso(beginPos);
					timeRange.setStartTime(begin);
				}
				catch (ParseException e)
				{
					throw new WCSException(invalidXML + ": " + invalidValue + " BeginPosition: " + beginPos);
				}
				
				try
				{
					double end = DateTimeFormat.parseIso(endPos);
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
							step = DateTimeFormat.parseIsoPeriod(timeRes);
						else
							step = Double.parseDouble(timeRes);
						
						timeRange.setTimeStep(step);
					}
					catch (Exception e)
					{
						throw new WCSException(invalidXML + ": " + invalidValue + " TimeResolution: " + timeRes);
					}
				}
				
				query.getTimes().add(timeRange);
			}
			
			else
				throw new WCSException(invalidXML + ": Invalid Time Subset: " + timeElt.getLocalName());
		}

		
		////// range subset //////
		NodeList axisElts = dom.getElements(requestElt, "RangeSubset/axisSubset");
		for (int i=0; i<axisElts.getLength(); i++)
		{
			AxisSubset axis = new AxisSubset();			
			Element axisElt = (Element)axisElts.item(i);
			String name = dom.getAttributeValue(axisElt, "name");
			axis.setName(name);
			
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
					throw new WCSException(invalidXML + ": " + invalidValue + " range subset: " + minText+"/"+maxText+"/");
				}
				axis.getRangeIntervals().add(interval);
			}
			
			NodeList valueElts = dom.getElements(axisElt, "singleValue");
			for (int p=0; p<valueElts.getLength(); p++)
			{
				Element valueElt = (Element)valueElts.item(p);
				String val = dom.getElementValue(valueElt);
				if (val == null)
					throw new WCSException(invalidXML + ": " + invalidValue + " range subset: " + val);				
				axis.getRangeValues().add(val);
			}
			
			query.getAxisSubsets().add(axis);
		}	
		
		
		////// Output //////
		Element outputElt = dom.getElement(requestElt, "Output");
		
		// format
		String format = dom.getAttributeValue(outputElt, "@format");
		query.setFormat(format);
		
		// store
		String store = dom.getAttributeValue(outputElt, "@store");
		if (store != null && store.equalsIgnoreCase("true"))
			query.setStore(true);
		else
			query.setStore(false);
		
		// rectified grid
		Element gridElt = dom.getElement(outputElt, "GridCRS");
		if (gridElt != null)
		{
			// base CRS
			String responseCrs = dom.getElementValue(gridElt, "GridBaseCRS");
			query.setGridCrs(responseCrs);
			
			// type
			String gridType = dom.getElementValue(gridElt, "GridType");
			if (gridType != null)
				query.setGridType(gridType);
			
			// CS
			String gridCs = dom.getElementValue(gridElt, "GridCS");
			if (gridCs != null)
				query.setGridCs(gridCs);
			
			// origin
			String gridOrigin = dom.getElementValue(gridElt, "GridOrigin");
			if (gridOrigin != null)
			{
				double[] vecOrig = this.parseVector(gridOrigin);
				query.setGridDimension(vecOrig.length);
				query.setGridOrigin(vecOrig);
			}
			
			// offsets
			String gridOffsets = dom.getElementValue(gridElt, "GridOffsets");
			if (gridOffsets != null)
			{
				double[] vecOff = this.parseVector(gridOffsets);
				query.setGridOffsets(vecOff);
			}
		}
		
		
		return query;
	}
}