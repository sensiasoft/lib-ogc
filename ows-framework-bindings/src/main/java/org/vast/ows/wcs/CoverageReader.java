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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.io.*;
import org.vast.cdm.common.DataHandler;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.XMLReaderException;
import org.vast.ogc.OGCException;
import org.vast.ogc.OGCExceptionReader;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEFilter;
import org.vast.swe.SWEReader;
import org.vast.swe.URIStreamHandler;
import org.w3c.dom.*;


public class CoverageReader extends SWEReader
{
    SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    String resultUri;
	SWEFilter streamFilter;
	
	
	public void parse(InputStream inputStream, DataHandler handler) throws IOException
	{		
		try
		{
			dataHandler = handler;
		    streamFilter = new SWEFilter(inputStream);
			streamFilter.setDataElementName("value");
						
//			int val;
//			do
//			{
//				val = streamFilter.read();
//				System.out.print((char)val);
//			}
//			while (val != -1);
//			System.exit(0);
			
			// parse xml header using DataComponent and DataEncoding readers
			DOMHelper dom = new DOMHelper(streamFilter, false);
            OGCExceptionReader.checkException(dom);
			
			// get structure and encoding elements
			Element defElt = dom.getElement("result/Data/definition/DataDefinition");
			Element dataElt = dom.getElement(defElt, "dataComponents/*");
			Element encElt = dom.getElement(defElt, "encoding/*");
			
			// parse structure and encoding            
            this.dataComponents = sweUtils.readComponent(dom, dataElt);
			this.dataEncoding = sweUtils.readEncoding(dom, encElt);
			
			// read link to out of band data stream if present
			resultUri = dom.getAttributeValue("result/data/value/externalLink");
		}
		catch (DOMHelperException e)
		{
			throw new XMLReaderException("Error while parsing Coverage XML", e);
		}
        catch (OGCException e)
        {
            throw new XMLReaderException(e.getMessage());
        }
	}
	
	
	public InputStream getDataStream() throws IOException
	{
		if (resultUri != null)
		{
			try
			{
				streamFilter.startReadingData();
				streamFilter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			return URIStreamHandler.openStream(resultUri);
		}
		else
		{
			streamFilter.startReadingData();
			return streamFilter;
		}
	}
}
