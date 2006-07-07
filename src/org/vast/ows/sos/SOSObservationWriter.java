/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.io.*;

import org.w3c.dom.*;
import org.vast.io.xml.*;
import org.vast.ows.SweDataWriter;
import org.vast.ows.SweResultWriter;
import org.vast.ows.util.TimeInfo;
import org.vast.util.*;


/**
 * <p><b>Title:</b><br/> ObservationWriter</p>
 *
 * <p><b>Description:</b><br/>
 * TODO ObservationWriter type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class SOSObservationWriter extends SweResultWriter
{
	SweDataWriter dataWriter;
	
	
	public SOSObservationWriter()
	{		
	}
	
	
	public void setDataWriter(SweDataWriter dataWriter)
	{
		this.dataWriter = dataWriter;
	}
	
	
	/**
	 * Change eventTime element in the DOM to contain the request times
	 * @param time
	 */
	public void setTime(TimeInfo time, int zone)
	{
		DOMReader templates = null;
		
		try
		{
			// preload templates doc
			InputStream templateFile = SOSObservationWriter.class.getResourceAsStream("templates.xml");
			templates = new DOMReader(templateFile, false);
		}
		catch (DOMReaderException e)
		{
			e.printStackTrace();
		}
		
		// keep pointers to needed nodes
		NodeList eventTimes = respXML.getRootElement().getElementsByTagName("om:eventTime");//eventTime");
		
		for (int i=0; i<eventTimes.getLength(); i++)
		{
			Element eventTime = (Element)eventTimes.item(i);
			Element timeInstantElt = (Element)respDocument.importNode(templates.getElement("TimeInstant"), true);
			Element timePeriodElt = (Element)respDocument.importNode(templates.getElement("TimePeriod"), true);
			Text timeText = (Text)respXML.getElement(timeInstantElt, "timePosition").getFirstChild();
			Text beginText = (Text)respXML.getElement(timePeriodElt, "beginPosition").getFirstChild();
			Text endText = (Text)respXML.getElement(timePeriodElt, "endPosition").getFirstChild();
						
			// erase old time parameters
			if (eventTime.hasChildNodes())
				eventTime.removeChild(eventTime.getFirstChild());	
			
			// TimeInstant case
			if (time.getStartTime() == time.getStopTime())
			{
				timeText.setData(DateTimeFormat.formatIso(time.getStartTime(), zone));
				eventTime.appendChild(timeInstantElt);
			}
			
			// TimePeriod case
			else
			{
				beginText.setData(DateTimeFormat.formatIso(time.getStartTime(), zone));
				endText.setData(DateTimeFormat.formatIso(time.getStopTime(), zone));
				eventTime.appendChild(timePeriodElt);
			}
		}
	}
	
	
	protected void writeResultDefinition(Element elt)
	{
		
	}
	
	
	protected void writeResultEncoding(Element elt)
	{
		
	}
	
	
	protected void writeResultData(Element elt) throws IOException
	{
		dataWriter.write();
	}
	
	
	/**
	 * Adds a hook on result
	 */
	protected void serializeElement(Element elt) throws IOException
	{
		if (elt.getLocalName().equals("result"))
		{
			this._format.setIndenting(false);
			this._printer.printText("\n<om:result>");
			this._printer.flush();
			
			this.writeResultData(elt);
			
			this._printer.printText("</om:result>");
			this._printer.flush();
			this._format.setIndenting(true);
		}
		else
		{
			super.serializeElement(elt);
		}
	}	
}
