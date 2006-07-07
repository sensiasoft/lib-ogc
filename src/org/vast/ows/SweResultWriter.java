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

package org.vast.ows;

import java.io.*;

import org.w3c.dom.*;
import org.vast.io.xml.*;
import org.apache.xml.serialize.*;


/**
 * <p><b>Title:</b><br/> SweResultWriter</p>
 *
 * <p><b>Description:</b><br/>
 * Base class handling the SWE XML response writing in the given OutputStream 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 9, 2005
 * @version 1.0
 */
public abstract class SweResultWriter extends XMLSerializer
{
	protected DOMReader respXML;
	protected Document respDocument;
	
	
	public SweResultWriter()
	{
		OutputFormat outFormat = new OutputFormat();
		outFormat.setMethod("xml");
		outFormat.setIndenting(true);
		outFormat.setLineWidth(0);
		this.setOutputFormat(outFormat);
	}
	
	
	public abstract void setDataWriter(SweDataWriter dataWriter);
	
	
	/**
	 * Assign the template xml stream
	 * @param baseXML
	 */
	public void setTemplate(InputStream baseXML)
	{
		try
		{
			// preload base observation document
			this.respXML = new DOMReader(baseXML, false);
			this.respDocument = respXML.getXmlFragment().getParentDocument().getDocument();
		}
		catch (DOMReaderException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Write xml to outputStream
	 */
	public void writeResult() throws IOException
	{
		serialize(this.respDocument.getDocumentElement());
	}
	
	
	protected abstract void writeResultDefinition(Element elt) throws IOException;
	protected abstract void writeResultEncoding(Element elt) throws IOException;
	protected abstract void writeResultData(Element elt) throws IOException;
	
	//protected abstract void serializeElement(Element elt) throws IOException;
}