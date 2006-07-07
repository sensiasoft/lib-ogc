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

package org.vast.ows.server;

import javax.servlet.http.*;
import java.io.*;

import org.vast.io.xml.*;
import org.w3c.dom.*;


/**
 * <p><b>Title:</b><br/> OWSServlet</p>
 *
 * <p><b>Description:</b><br/>
 * Abstract Base Class for all OWS Style Servlets
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 9, 2005
 * @version 1.0
 */
public abstract class OWSServlet extends HttpServlet
{
	protected Document capsDoc;


	// Sends an OWS Service Exception to the user
	protected void sendErrorMessage(OutputStream resp, String message)
	{
		PrintWriter buffer = new PrintWriter(resp);
		buffer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		buffer.println("<ServiceExceptionReport version=\"1.0\">");
		buffer.println("<ServiceException>");
		buffer.println(message);
		buffer.println("</ServiceException>");
		buffer.println("</ServiceExceptionReport>");
		buffer.flush();
		buffer.close();
		//this.log(message);
	}


	public synchronized void updateCapabilities(InputStream capFile)
	{
		try
		{
			DOMReader capsReader = new DOMReader(capFile, false);
			this.capsDoc = capsReader.getXmlFragment().getParentDocument().getDocument();
		}
		catch (DOMReaderException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * Sends the whole capabilities document in response to GetCapabilities request
	 * @param resp
	 * @throws IOException
	 */
	protected void sendCapabilities(String section, OutputStream resp)
	{
		try
		{
			DOMWriter domWriter = new DOMWriter();
			domWriter.setOutputStream(resp);
			domWriter.writeDOM(capsDoc.getDocumentElement());
		}
		catch (IOException e)
		{
		}
	}
}
