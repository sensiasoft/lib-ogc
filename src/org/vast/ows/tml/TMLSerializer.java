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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.tml;

import java.io.*;

import org.w3c.dom.*;
//import org.vast.io.xml.*;
//import org.vast.util.*;
import org.vast.ows.SweDataWriter;
import org.vast.ows.SweResponseSerializer;
import org.vast.ows.util.TimeInfo;


/**
 * <p><b>Title:</b><br/>
 * TML Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * TODO TMLWriter type description
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @version 1.0
 */
public class TMLSerializer extends SweResponseSerializer
{
	SweDataWriter resultWriter;
	
	
	public TMLSerializer()
	{		
	}
	
	
	public void setDataWriter(SweDataWriter dataWriter)
	{
		this.resultWriter = dataWriter;
	}
	
	
	/**
	 * Change eventTime element in the DOM to contain the request times
	 * @param time
	 */
	public void setTime(TimeInfo time, int zone)
	{
		/*
		DOMReader templates = null;
		
		try
		{
			// preload templates doc
			InputStream templateFile = TMLWriter.class.getResourceAsStream("templates.xml");
			templates = new DOMReader(templateFile, false);
		}
		catch (DOMReaderException e)
		{
			e.printStackTrace();
		}
		
		// keep pointers to needed nodes
		NodeList eventTimes = templates.getRootElement().getElementsByTagName("eventTime");
		
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
			if (time.startTime == time.stopTime)
			{
				timeText.setData(DateTimeFormat.formatIso(time.startTime, -1));
				eventTime.appendChild(timeInstantElt);
			}
			
			// TimePeriod case
			else
			{
				beginText.setData(DateTimeFormat.formatIso(time.startTime, -1));
				endText.setData(DateTimeFormat.formatIso(time.stopTime, -1));
				eventTime.appendChild(timePeriodElt);
			}
		}
		*/
	}
	
	
	protected void serializeElement(Element elt) throws IOException
	{
		if (elt.getLocalName().equals("tmlData"))
		{
			this._printer.printText("\n<tmlData>");
			this._printer.flush();
			
            resultWriter.write();
			
			this._printer.printText("</tmlData>");
			this._printer.flush();
		}
		else
		{
			super.serializeElement(elt);
		}
	}
}
