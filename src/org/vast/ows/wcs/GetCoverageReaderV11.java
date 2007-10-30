/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
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
		OWSExceptionReport report = new OWSExceptionReport();
		GetCoverageRequest request = new GetCoverageRequest();
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
                	TimeInfo newTime = parseTimeArg(timeList[i]);
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
            	request.setGridCrs(argValue);
            }
            
            // GridCS
            else if (argName.equalsIgnoreCase("GridCS"))
            {
            	request.setGridCs(argValue);
            }
            
            // GridType
            else if (argName.equalsIgnoreCase("GridType"))
            {
            	request.setGridType(argValue);
            }
            
            // GridOrigin
            else if (argName.equalsIgnoreCase("GridOrigin"))
            {
            	double[] origin = parseVector(argValue);
            	request.setGridDimension(origin.length);
            	request.setGridOrigin(origin);
            }
            
            // GridOffsets
            else if (argName.equalsIgnoreCase("GridOffsets"))
            {
            	double[] offsets = parseVector(argValue);
            	request.setGridOffsets(offsets);
            }
            
            // format
            else if (argName.equalsIgnoreCase("format"))
            {
                request.setFormat(argValue);
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
	
	
	protected void parseRangeSubset(GetCoverageRequest query, String kvpToken)
	{
		
	}
	
	
	@Override
	public GetCoverageRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport();
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
			Bbox bbox = bboxReader.readBbox(dom, bboxElt);
			request.setBbox(bbox);
		}
		
		// temporal subset
		NodeList timeElts = dom.getElements(domainElt, "TemporalSubset/*");
		for (int i=0; i<timeElts.getLength(); i++)
		{
			Element timeElt = (Element)timeElts.item(i);
			
			if (timeElt.getLocalName().equals("timePosition"))
			{
				request.getTimes().add(timeReader.readTimeInstant(dom, timeElt));
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
				
				request.getTimes().add(timeRange);
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
			
			request.getAxisSubsets().add(axis);
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
		
		// rectified grid
		Element gridElt = dom.getElement(outputElt, "GridCRS");
		if (gridElt != null)
		{
			// base CRS
			String responseCrs = dom.getElementValue(gridElt, "GridBaseCRS");
			request.setGridCrs(responseCrs);
			
			// type
			String gridType = dom.getElementValue(gridElt, "GridType");
			if (gridType != null)
				request.setGridType(gridType);
			
			// CS
			String gridCs = dom.getElementValue(gridElt, "GridCS");
			if (gridCs != null)
				request.setGridCs(gridCs);
			
			// origin
			String gridOrigin = dom.getElementValue(gridElt, "GridOrigin");
			if (gridOrigin != null)
			{
				double[] vecOrig = this.parseVector(gridOrigin);
				request.setGridDimension(vecOrig.length);
				request.setGridOrigin(vecOrig);
			}
			
			// offsets
			String gridOffsets = dom.getElementValue(gridElt, "GridOffsets");
			if (gridOffsets != null)
			{
				double[] vecOff = this.parseVector(gridOffsets);
				request.setGridOffsets(vecOff);
			}
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
		// copy crs to responseCrs if needed
		if (request.gridCrs == null && request.getBbox() != null)
			request.gridCrs = request.getBbox().getCrs();
		
		// check common params
		super.checkParameters(request, report);
		
		// need coverage
		if (request.getCoverage() == null)
			report.add(new OWSException(OWSException.missing_param_code, "Identifier"));
		
		// need at least BBOX or TIME
		if (request.getBbox() == null && request.getTime() == null)
			report.add(new OWSException(OWSException.missing_param_code, "TimeSequence/BoundingBox"));
		
		// TODO check Grid info??
		//if (request.getWidth() < 0 && request.getResX() < 0)
		//	list.add(new OWSException(OWSException.missing_param_code, "WIDTH/HEIGHT/RESX/RESY"));
		
		// need format
		if (request.getFormat() == null)
			report.add(new OWSException(OWSException.missing_param_code, "Format"));
				
		report.process();
	}
}