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

import java.io.IOException;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSUtils;
import org.vast.ows.sps.StatusReport.RequestStatus;
import org.vast.ows.sps.StatusReport.TaskStatus;
import org.vast.ows.swe.SWESUtils;
import org.vast.swe.DataSinkDOM;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEData;
import org.vast.util.DateTime;
import org.vast.util.DateTimeFormat;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLWriterException;
import org.w3c.dom.Element;


/**
 * <p>
 * Helper routines for writing common info present
 * in several SPS requests/responses (i.e. SWE Data)
 * </p>
 *
 * @author Alex Robin
 * @date Feb, 29 2008
 * */
public class SPSCommonWriterV20
{
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
	protected DateTimeFormat timeFormat = new DateTimeFormat();
	
	
	/**
	 * Writes a SWE Data stream such as inside taskingParameters or reportParameters
	 * @param dom
	 * @param parentElt
	 * @param paramsData
	 * @throws XMLWriterException
	 */
	public void writeSWEData(DOMHelper dom, Element parentElt, SWEData paramsData) throws XMLWriterException
	{
	    Element paramDataElt = dom.addElement(parentElt, "sps:ParameterData");
        
	    try
        {
            // write encoding
            Element encodingPropertyElt = dom.addElement(paramDataElt, "sps:encoding");
            Element encodingElt = sweUtils.writeEncoding(dom, paramsData.getEncoding());
            encodingPropertyElt.appendChild(encodingElt);
            
            // write values
            Element valuesElt = dom.addElement(paramDataElt, "sps:values");
            DataSinkDOM dataSink = new DataSinkDOM(dom, valuesElt);
            
            // launch serializer to write data to the XML doc
            paramsData.writeData(dataSink);
        }
        catch (IOException e)
        {
            throw new XMLWriterException("Error while writing SWE Common data", paramDataElt, e);
        }
	}
	
	
	protected void writeBaseReportAttributes(DOMHelper dom, Element reportElt, StatusReport report) throws XMLWriterException
	{
		String val;
		dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));
		
		// description
		val = report.getDescription();
		if (val != null)
			dom.setElementValue(reportElt, "swes:description", val);
		
		// identifier
		val = report.getId();
		if (val != null)
			dom.setElementValue(reportElt, "swes:identifier", val);
		
		// name
		val = report.getTitle();
		if (val != null)
			dom.setElementValue(reportElt, "swes:name", val);
		
		// extensions
		SWESUtils.writeXMLExtensions(dom, reportElt, "2.0", report.getExtensions());
		
		// task ID
		val = report.getTaskID();
		if (val != null)
			dom.setElementValue(reportElt, "sps:task", val);
		
		// estimated time of completion
		DateTime date = report.getEstimatedToC();
		if (date != null)
			dom.setElementValue(reportElt, "sps:estimatedToC", timeFormat.formatIso(date.getJulianTime(), 0)); 
		
		// event code
		val = report.getEventCode();
		if (val != null)
			dom.setElementValue(reportElt, "sps:event", val);
		
		// percent completion
		float fval = report.getPercentCompletion();
		if (!Float.isNaN(fval))
			dom.setElementValue(reportElt, "sps:percentCompletion", Float.toString(fval));
		
		// procedure ID
		val = report.getSensorID();
		dom.setElementValue(reportElt, "sps:procedure", val);
		
		// request status
		RequestStatus reqStatus = report.getRequestStatus();
		if (reqStatus != null)
			dom.setElementValue(reportElt, "sps:requestStatus", reqStatus.name());
		
		// status message
		val = report.getStatusMessage();
		if (val != null)
			dom.setElementValue(reportElt, "sps:statusMessage", val);
		
		// task status
		TaskStatus taskStatus = report.getTaskStatus();
		if (taskStatus != null)
			dom.setElementValue(reportElt, "sps:taskStatus", taskStatus.name());
			
		// update time
		date = report.getLastUpdate();
		dom.setElementValue(reportElt, "sps:updateTime", timeFormat.formatIso(date.getJulianTime(), 0));		
	}
	
	
	protected void writeStatusReportData(DOMHelper dom, Element reportElt, StatusReport report) throws XMLWriterException
	{
		writeBaseReportAttributes(dom, reportElt, report);
	}
	
	
	protected void writeAlternatives(DOMHelper dom, Element reportElt, FeasibilityReport report) throws XMLWriterException
	{
		String val;
		dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));
		
		for (Alternative alt: report.getAlternatives())
		{
			Element altElt = dom.addElement(reportElt, "+sps:alternative/sps:Alternative");
			
			// description
			val = alt.getDescription();
			if (val != null)
				dom.setElementValue(altElt, "swes:description", val);
			
			// identifier
			val = alt.getId();
			if (val != null)
				dom.setElementValue(altElt, "swes:identifier", val);
			
			// name
			val = alt.getTitle();
			if (val != null)
				dom.setElementValue(altElt, "swes:name", val);
			
			// extensions
			SWESUtils.writeXMLExtensions(dom, altElt, "2.0", alt.getExtensions());
			
			// alternative tasking parameters (mandatory)
			SWEData taskingParams = report.getTaskingParameters();
			Element property = dom.addElement(reportElt, "sps:taskingParameters");
			this.writeSWEData(dom, property, taskingParams);
		}
	}
	
	
	protected void writeTaskParameters(DOMHelper dom, Element reportElt, StatusReport report) throws XMLWriterException
	{
		// tasking parameters
		SWEData taskingParams = report.getTaskingParameters();
		if (taskingParams != null)
		{
			Element property = dom.addElement(reportElt, "sps:taskingParameters");
			this.writeSWEData(dom, property, taskingParams);
		}
	}
	
	
	protected Element writeTask(DOMHelper dom, Task task) throws XMLWriterException
	{
		String val;
		Element taskElt = dom.createElement("sps:Task");
		dom.addUserPrefix("swes", OGCRegistry.getNamespaceURI(OWSUtils.SWES, "2.0"));
		
		// description
		val = task.getDescription();
		if (val != null)
			dom.setElementValue(taskElt, "swes:description", val);
		
		// identifier
		val = task.getId();
		if (val != null)
			dom.setElementValue(taskElt, "swes:identifier", val);
		
		// name
		val = task.getTitle();
		if (val != null)
			dom.setElementValue(taskElt, "swes:name", val);
		
		// extensions
		SWESUtils.writeXMLExtensions(dom, taskElt, "2.0", task.getExtensions());
		
		// write status reports
		for (StatusReport report: task.getStatusReports())
		{
			Element reportElt = writeReport(dom, report);
			writeTaskParameters(dom, reportElt, report);
		}
		
		return taskElt;
	}
	
	
	/**
	 * Writes any report to the DOM
	 * @param dom
	 * @param report
	 * @return new DOM element
	 * @throws XMLWriterException
	 */
	public Element writeReport(DOMHelper dom, StatusReport report) throws XMLWriterException
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
	 * @return new DOM element
	 * @throws XMLWriterException
	 */
	public Element writeStatusReport(DOMHelper dom, StatusReport report) throws XMLWriterException
	{
		Element reportElt = dom.createElement("sps:StatusReport");
		writeStatusReportData(dom, reportElt, report);
		return reportElt;
	}
	
	
	/**
	 * Writes a Feasibility Report as a DOM element according to SPS v2.0 schema
	 * @param dom
	 * @param report
	 * @return new DOM element
	 * @throws XMLWriterException
	 */
	public Element writeFeasibilityReport(DOMHelper dom, FeasibilityReport report) throws XMLWriterException
	{
		Element reportElt = dom.createElement("sps:StatusReport");
		writeStatusReportData(dom, reportElt, report);
		
		if (report.getAlternatives() != null)
			writeAlternatives(dom, reportElt, report);
		
		return reportElt;
	}
	
	
	/**
	 * Writes a Reservation report as a DOM element according to SPS v2.0 schema
	 * @param dom
	 * @param report
	 * @return new DOM element
	 * @throws XMLWriterException
	 */
	public Element writeReservationReport(DOMHelper dom, ReservationReport report) throws XMLWriterException
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
