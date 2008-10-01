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
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.sweCommon.DataSinkXML;
import org.vast.sweCommon.SWEData;
import org.vast.sweCommon.SweEncodingWriterV20;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * SPS Common Writer v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Helper routines for writing common info present
 * in several SPS requests/responses (i.e. SWE Data)
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date Feb, 29 2008
 * @version 1.0
 */
public class SPSCommonWriterV20
{
	protected SweEncodingWriterV20 encodingWriter = new SweEncodingWriterV20();
	
	
	/**
	 * Writes a SWE Data stream such as inside taskingParameters or reportParameters
	 * @param dom
	 * @param parentElt
	 * @param paramsData
	 * @param paramStructure
	 * @throws CDMException
	 */
	public void writeSWEData(DOMHelper dom, Element parentElt, SWEData paramsData) throws CDMException
	{
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(OGCRegistry.SWE, "2.0"));
		
		// write encoding
		Element encodingPropertyElt = dom.addElement(parentElt, "swe:encoding");
		Element encodingElt = encodingWriter.writeEncoding(dom, paramsData.getDataEncoding());
		encodingPropertyElt.appendChild(encodingElt);
		
		// write values
		Element valuesElt = dom.addElement(parentElt, "swe:values");
		DataSinkXML dataSink = new DataSinkXML(valuesElt);
		
		// launch serializer to write data to the XML doc
		paramsData.writeData(dataSink);
	}
	
	
	/**
	 * Writes a Progress Report as a DOM element according to SPS v2.0 schema
	 * @param dom
	 * @param report
	 * @return
	 * @throws OWSException
	 */
	public Element writeProgressReport(DOMHelper dom, ProgressReport report) throws CDMException
	{
		Element reportElt = dom.createElement("sps:ProgressReport");
		String val;
		
		// task/reservation ID
		val = report.getId();
		if (val != null)
			dom.setElementValue(reportElt, "sps:ID", val);
		
		// status code
		val = report.getStatusCode();
		if (val != null)
			dom.setElementValue(reportElt, "sps:statusCode", val);
		
		// estimated time of completion
		DateTime date = report.getEstimatedToC();
		if (date != null)
			dom.setElementValue(reportElt, "sps:estimatedToC",
					DateTimeFormat.formatIso(date.getJulianTime(), 0));
		
		// description
		val = report.getDescription();
		if (val != null)
			dom.setElementValue(reportElt, "sps:description", val);
		
		// report parameters
		SWEData reportParams = report.getReportParameters();
		if (reportParams != null)
		{
			Element reportParamsElt = dom.addElement(reportElt, "sps:reportParameters");
			writeSWEData(dom, reportParamsElt, reportParams);
		}
		
		return reportElt;
	}
	
	
	/**
	 * Writes a Feasibility Study as a DOM element according to SPS v2.0 schema
	 * @param dom
	 * @param study
	 * @return
	 * @throws OWSException
	 */
	public Element writeFeasibilityStudy(DOMHelper dom, FeasibilityStudy study) throws CDMException
	{
		Element studyElt = dom.createElement("sps:FeasibilityStudy");
		String val;
		
		// task/reservation ID
		val = study.getId();
		if (val != null)
			dom.setElementValue(studyElt, "sps:ID", val);
		
		// feasibility code
		val = study.getFeasibilityCode();
		if (val != null)
			dom.setElementValue(studyElt, "sps:feasibilityCode", val);
		
		// estimated time of completion
		DateTime date = study.getEstimatedToC();
		if (date != null)
			dom.setElementValue(studyElt, "sps:estimatedToC",
					DateTimeFormat.formatIso(date.getJulianTime(), 0));
		
		// success rate
		double successRate = study.getSuccessRate();
		if (!Double.isNaN(successRate))
			dom.setElementValue(studyElt, "sps:successRate", Double.toString(successRate));
		
		// description
		val = study.getDescription();
		if (val != null)
			dom.setElementValue(studyElt, "sps:description", val);
		
		// study parameters
		SWEData studyParams = study.getStudyParameters();
		if (studyParams != null)
		{
			Element studyParamsElt = dom.addElement(studyElt, "sps:studyParameters");
			writeSWEData(dom, studyParamsElt, studyParams);
		}
		
		return studyElt;
	}
}
