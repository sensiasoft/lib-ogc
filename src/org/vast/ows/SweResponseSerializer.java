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
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.apache.xml.serialize.*;


/**
 * <p><b>Title:</b><br/>
 * Swe Response Serializer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Helper class to handle serialization of SWE XML responses
 * based on a template document obtained from an input stream.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Aug 9, 2005
 * @version 1.0
 */
public abstract class SweResponseSerializer extends XMLSerializer
{
	protected DOMHelper dom;
    protected SweDataWriter dataWriter; 
	
	
	public SweResponseSerializer()
	{
		OutputFormat outFormat = new OutputFormat();
		outFormat.setMethod("xml");
		outFormat.setIndenting(true);
		outFormat.setLineWidth(0);
		this.setOutputFormat(outFormat);
	}
	
	
    /**
     * Assign the DataWriter to use for the raw data content.
     * @param dataWriter
     */
    public void setDataWriter(SweDataWriter dataWriter)
    {
        this.dataWriter = dataWriter;
    }
    
	
	/**
	 * Assign the template as an xml stream
	 * @param baseXML
	 */
	public void setTemplate(InputStream baseXML)
	{
		try
		{
			// preload base observation document
            DOMHelper newDom = new DOMHelper(baseXML, false);
            this.setTemplate(newDom);
		}
		catch (DOMHelperException e)
		{
			e.printStackTrace();
		}
	}
    
    
    /**
     * Assign the template as DOMHelper wrapping a DOM document
     * @param dom
     */
    public void setTemplate(DOMHelper dom)
    {
        this.dom = dom;
    }
	
	
	/**
	 * Write xml to outputStream
	 */
	public void writeResponse() throws IOException
	{
		serialize(dom.getRootElement());
	}
}