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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.io.*;

import org.ogc.cdm.common.*;
import org.vast.cdm.reader.*;
import org.vast.io.xml.*;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReader;
import org.w3c.dom.*;


public class CoverageReader extends CDMReader
{
	String resultUri;
	CDMFilter streamFilter;
	
	
	public void parse(InputStream inputStream) throws CDMException
	{		
		try
		{
			streamFilter = new CDMFilter(inputStream);
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
			DOMReader domReader = new DOMReader(streamFilter, false);
            OWSExceptionReader.checkException(domReader);
			
			// get structure and encoding elements
			Element defElt = domReader.getElement("result/Data/definition/DataDefinition");
			Element dataElt = domReader.getElement(defElt, "dataComponents");
			Element encElt = domReader.getElement(defElt, "encoding/*");
			
			// parse structure and encoding
			DataComponentsReader infReader = new DataComponentsReader(domReader);
			EncodingReader encReader = new EncodingReader(domReader);			
			this.dataComponents = infReader.readComponentProperty(dataElt);
			this.dataEncoding = encReader.readDataEncoding(encElt);
			
			// read link to out of band data stream if present
			resultUri = domReader.getAttributeValue("result/data/value/externalLink");
		}
		catch (DOMReaderException e)
		{
			throw new CDMException("Error while parsing Coverage XML", e);
		}
        catch (OWSException e)
        {
            throw new CDMException(e.getMessage());
        }
	}
	
	
	public InputStream getDataStream() throws CDMException
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
