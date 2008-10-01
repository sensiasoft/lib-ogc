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
package org.vast.ows.sps;

import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.ows.OWSException;
import org.vast.sweCommon.DataSourceXML;
import org.vast.sweCommon.SWEData;
import org.vast.sweCommon.SweEncodingReaderV11;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SPS Common Reader v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Helper routines for parsing common info present
 * in several SPS requests/responses (i.e. SWE Data)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 29 2008
 * @version 1.0
 */
public class SPSCommonReaderV20
{
	protected SweEncodingReaderV11 encodingReader = new SweEncodingReaderV11();
		
	
	/**
	 * USed to read SWE Data stream such as inside taskingParameters or reportParameters
	 * @param dom
	 * @param paramsElt
	 * @param paramStructure
	 * @return
	 */
	public SWEData readSWEData(DOMHelper dom, Element paramsElt, DataComponent paramStructure) throws CDMException
	{
		// read encoding
		Element encodingElt = dom.getElement(paramsElt, "encoding");
		DataEncoding dataEncoding = encodingReader.readEncodingProperty(dom, encodingElt);
		
		// create SWEData object
		SWEData paramsData = new SWEData();
		paramsData.setDataComponents(paramStructure.copy());
		paramsData.setDataEncoding(dataEncoding);
		
		// data source is the content of an XML element!
		Element valuesElt = dom.getElement(paramsElt, "values");
		DataSourceXML dataSource = new DataSourceXML(valuesElt);
				
		// launch parser to fill up the DataList inside the SWEData object
		paramsData.parseData(dataSource);
		
		return paramsData;
	}
	
	
	/**
	 * Reads a ProgressReport using the provided structure for report parameters
	 * @param dom
	 * @param reportElt
	 * @param paramStructure
	 * @return
	 * @throws OWSException
	 */
	public ProgressReport readProgressReport(DOMHelper dom, Element reportElt, DataComponent paramStructure) throws OWSException
	{
		try
		{
			ProgressReport report = new ProgressReport();
			
			// ID
			String id = dom.getElementValue(reportElt, "ID");
			report.setId(id);
			
			// statusCode
			String statusCode = dom.getElementValue(reportElt, "statusCode");
			report.setStatusCode(statusCode);
			
			// estimatedToC
			String isoDate = dom.getElementValue(reportElt, "estimatedToC");
			if (isoDate != null)
			{
				DateTime estimatedToC = new DateTime(DateTimeFormat.parseIso(isoDate));
				report.setEstimatedToC(estimatedToC);
			}
			
			// description
			String description = dom.getElementValue(reportElt, "description");
			report.setDescription(description);
			
			// report parameters
			Element reportParamsElt = dom.getElement(reportElt, "reportParameters");
			if (reportParamsElt != null && paramStructure != null)
			{
				// parse data into a SWEData object
				SWEData reportData = readSWEData(dom, reportParamsElt, paramStructure);
				report.setReportParameters(reportData);
			}
			
			return report;
		}
		catch (Exception e)
		{
			throw new OWSException(e);
		}
	}
	
	
	/**
	 * Reads a FeasibilityStudy using the provided structure for study parameters
	 * @param dom
	 * @param reportElt
	 * @param paramStructure
	 * @return
	 * @throws OWSException
	 */
	public FeasibilityStudy readFeasibilityStudy(DOMHelper dom, Element reportElt, DataComponent paramStructure) throws OWSException
	{
		try
		{
			FeasibilityStudy study = new FeasibilityStudy();
			
			// ID
			String id = dom.getElementValue(reportElt, "ID");
			study.setId(id);
			
			// feasibilityCode
			String feasibilityCode = dom.getElementValue(reportElt, "feasibilityCode");
			study.setFeasibilityCode(feasibilityCode);
			
			// estimatedToC
			String isoDate = dom.getElementValue(reportElt, "estimatedToC");
			if (isoDate != null)
			{
				DateTime estimatedToC = new DateTime(DateTimeFormat.parseIso(isoDate));
				study.setEstimatedToC(estimatedToC);
			}
			
			// successRate
			String rateText = dom.getElementValue(reportElt, "successRate");
			if (rateText != null)
			{
				double successRate = Double.parseDouble(rateText);
				study.setSuccessRate(successRate);
			}
			
			// description
			String description = dom.getElementValue(reportElt, "description");
			study.setDescription(description);
			
			// study parameters
			Element studyParamsElt = dom.getElement(reportElt, "studyParameters");
			if (studyParamsElt != null && paramStructure != null)
			{
				// parse data into a SWEData object
				SWEData reportData = readSWEData(dom, studyParamsElt, paramStructure);
				study.setStudyParameters(reportData);
			}
			
			return study;
		}
		catch (Exception e)
		{
			throw new OWSException(e);
		}
	}
}
