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
import org.vast.ows.SweResponseSerializer;
import org.vast.ows.util.TimeInfo;
import org.vast.util.*;


/**
 * <p><b>Title:</b><br/>
 * SOS Observation Serializer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This class is a specific serializer used to output
 * Common Observations. It provides an additional setTime
 * method to easily change the time in the XML response and
 * overrides the <swe:result> element serialization by calling
 * the attached DataWriter to handle the job. 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public class SOSObservationSerializer extends SweResponseSerializer
{
	
	
	public SOSObservationSerializer()
	{		
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
			InputStream templateFile = SOSObservationSerializer.class.getResourceAsStream("templates.xml");
			templates = new DOMReader(templateFile, false);
		}
		catch (DOMReaderException e)
		{
			e.printStackTrace();
		}
		
		// keep pointers to needed nodes
		NodeList eventTimes = respXML.getRootElement().getElementsByTagName("om:eventTime");//eventTime");
		Document respDocument = respXML.getRootElement().getOwnerDocument();
        
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
	
	
	/**
	 * Adds a hook to override serialization of the result element
     * Uses the DataWriter to write the content of the element.
	 */
	protected void serializeElement(Element elt) throws IOException
	{
		if (elt.getLocalName().equals("result"))
		{
			this._format.setIndenting(false);
			this._printer.printText("\n<om:result>");
			this._printer.flush();
			
            dataWriter.write();
			
			this._printer.printText("\n</om:result>");
			this._printer.flush();
			this._format.setIndenting(true);
		}
		else
		{
			super.serializeElement(elt);
		}
	}	
}
