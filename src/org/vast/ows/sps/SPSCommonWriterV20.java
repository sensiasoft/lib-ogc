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
import org.vast.sweCommon.DataSinkDOM;
import org.vast.sweCommon.SWECommonUtils;
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
		dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "2.0"));
		Element paramDataElt = dom.addElement(parentElt, "sps:ParameterData");
		
		// write encoding
		Element encodingPropertyElt = dom.addElement(paramDataElt, "sps:encoding");
		Element encodingElt = encodingWriter.writeEncoding(dom, paramsData.getDataEncoding());
		encodingPropertyElt.appendChild(encodingElt);
		
		// write values
		Element valuesElt = dom.addElement(paramDataElt, "sps:values");
		DataSinkDOM dataSink = new DataSinkDOM(dom, valuesElt);
		
		// launch serializer to write data to the XML doc
		paramsData.writeData(dataSink);
	}
	
	
	protected void writeBaseReportAttributes(DOMHelper dom, Element reportElt, AbstractReport report) throws CDMException
	{
		String val;
		
		// title
		val = report.getTitle();
		dom.setElementValue(reportElt, "sps:title", val);
		
		// abstract
		val = report.getDescription();
		if (val != null)
			dom.setElementValue(reportElt, "sps:abstract", val);
		
		// sensor ID
		val = report.getSensorID();
		dom.setElementValue(reportElt, "sps:sensorID", val);
		
		// task ID
		val = report.getTaskID();
		dom.setElementValue(reportElt, "sps:taskID", val);
		
		// update time
		DateTime date = report.getLastUpdate();
		dom.setElementValue(reportElt, "sps:updateTime", DateTimeFormat.formatIso(date.getJulianTime(), 0));
		
		// status code
		val = report.getStatusCode();
		dom.setElementValue(reportElt, "sps:statusCode", val);
		
		// estimated time of completion
		date = report.getEstimatedToC();
		if (date != null)
			dom.setElementValue(reportElt, "sps:estimatedToC", DateTimeFormat.formatIso(date.getJulianTime(), 0));
	}
	
	
	protected void writeStatusReportData(DOMHelper dom, Element reportElt, StatusReport report) throws CDMException
	{
		writeBaseReportAttributes(dom, reportElt, report);
		
		// extended data
		SWEData extData = report.getExtendedData();
		if (extData != null && extData.getDataList().getComponentCount() > 0)
		{
			Element extDataElt = dom.addElement(reportElt, "sps:extendedData");
			writeSWEData(dom, extDataElt, extData);
		}
	}
	
	
	/**
	 * Writes any report to the DOM
	 * @param dom
	 * @param report
	 * @return
	 * @throws CDMException
	 */
	public Element writeReport(DOMHelper dom, StatusReport report) throws CDMException
	{
		if (report instanceof FeasibilityReport)
			return writeFeasibilityReport(dom, (FeasibilityReport)report);
		else if (report instanceof ReservationReport)
			return writeReservationReport(dom, (ReservationReport)report);
		else
			return writeStatusReport(dom, report);
	}
	
	
	/**
	 * Writes a Status Report as a DOM element according to SPS v2.0 schema
	 * @param dom
	 * @param report
	 * @return
	 * @throws OWSException
	 */
	public Element writeStatusReport(DOMHelper dom, StatusReport report) throws CDMException
	{
		Element reportElt = dom.createElement("sps:StatusReport");
		writeStatusReportData(dom, reportElt, report);
		return reportElt;
	}
	
	
	/**
	 * Writes a Feasibility Report as a DOM element according to SPS v2.0 schema
	 * @param dom
	 * @param report
	 * @return
	 * @throws OWSException
	 */
	public Element writeFeasibilityReport(DOMHelper dom, FeasibilityReport report) throws CDMException
	{
		Element reportElt = dom.createElement("sps:FeasibilityReport");
		writeStatusReportData(dom, reportElt, report);
		
		// TODO write feasible alternatives
		
		return reportElt;
	}
	
	
	/**
	 * Writes a Reservation report as a DOM element according to SPS v2.0 schema
	 * @param dom
	 * @param report
	 * @return
	 * @throws OWSException
	 */
	public Element writeReservationReport(DOMHelper dom, ReservationReport report) throws CDMException
	{
		Element reportElt = dom.createElement("sps:ReservationReport");
		writeStatusReportData(dom, reportElt, report);
		
		// expiration
		DateTime expDate = report.getReservationExpiration();
		if (expDate != null)
			dom.setElementValue(reportElt, "sps:reservationExpiration", expDate.formatIso(0));
		
		return reportElt;
	}
}
