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
import org.vast.io.xml.DOMReader;
import org.vast.io.xml.DOMReaderException;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b><br/>
 * Abstract class for all OWS Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST SOS request and
 * create an OWSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 4, 2005
 * @version 1.0
 */
public abstract class OWSRequestReader
{
  
	public OWSRequestReader()
	{	
	}
	
		
	public abstract OWSQuery readGetRequest(String queryString) throws OWSException;	
	public abstract OWSQuery readRequestXML(DOMReader domReader, Element requestElt) throws OWSException;
	
	
	public OWSQuery readPostRequest(InputStream input) throws OWSException
	{
		try
		{
			DOMReader domReader = new DOMReader(input, false);
			return readRequestXML(domReader, domReader.getBaseElement());
		}
		catch (DOMReaderException e)
		{
			throw new OWSException("", e);
		}
	}
	
	
	/**
	 * Reads common XML request parameters and fill up the OWSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @param query
	 */
	protected void readCommonXML(DOMReader dom, Element requestElt, OWSQuery query)
	{
		query.setRequest(requestElt.getLocalName());
		query.setService(dom.getAttributeValue(requestElt, "service"));
		query.setVersion(dom.getAttributeValue(requestElt, "version"));
	}
	
	
	/**
	 * Reads a GetCapabilities XML request and fill up the OWSQuery accordingly
	 * @param dom
	 * @param requestElt
	 * @param query
	 */
	protected void readGetCapabilitiesXML(DOMReader dom, Element requestElt, OWSQuery query)
	{
		query.setSection(dom.getElementValue(requestElt, "Section"));
	}
}