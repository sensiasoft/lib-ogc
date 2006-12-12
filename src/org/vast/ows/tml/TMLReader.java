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

package org.vast.ows.tml;

import java.io.*;
import java.util.ArrayList;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.InputStreamProvider;
import org.vast.cdm.reader.*;
import org.vast.io.xml.*;
import org.w3c.dom.*;


public class TMLReader implements InputStreamProvider
{
	CDMFilter streamFilter;
	InputStream inputStream;
	TMLStreamParser tmlParser;
	ArrayList<DataStreamParser> parsers;
	
	
	public TMLReader()
	{
		parsers = new ArrayList<DataStreamParser>();
	}
	
	
	public void parse(InputStream inputStream) throws CDMException
	{		
		try
		{
			this.streamFilter = new CDMFilter(inputStream);
			this.streamFilter.setDataElementName("tmlData");
			this.inputStream = inputStream;
			
//			int val;
//			do
//			{
//				val = this.streamFilter.read();
//				System.out.print((char)val);
//			}
//			while (val != -1);
//			System.exit(0);
			
			// create TMLParser
			this.tmlParser = new TMLStreamParser();
			
			// parse xml header using DataComponent and DataEncoding readers
			DOMReader domReader = new DOMReader(this.streamFilter, false);
			
			// create and register a parser for each product
			NodeList products = domReader.getElements("product/DataDefinition");
			for (int i=0; i<products.getLength(); i++)
			{
				Element defElt = (Element)products.item(i);
				String clusterID = domReader.getAttributeValue(defElt, "id");
				
				// get structure and encoding elements
				Element dataElt = domReader.getElement(defElt, "dataComponents");
				Element encElt = domReader.getElement(defElt, "encoding");
				
				// parse structure and encoding
				DataComponentsReader infReader = new DataComponentsReader(domReader);
				EncodingReader encReader = new EncodingReader(domReader);			
				DataComponent dataInfo = infReader.readComponentProperty(dataElt);
				DataEncoding dataEncoding = encReader.readEncodingProperty(encElt);
				
				// create parser
				DataStreamParser parser = createDataParser(dataInfo, dataEncoding);
				
				// register parser
				this.tmlParser.addDataParser(clusterID, parser);
				this.parsers.add(parser);
			}
		}
		catch (DOMReaderException e)
		{
			throw new CDMException("Error while parsing TML XML", e);
		}
	}
	
	
	public ArrayList<DataStreamParser> getDataParsers()
	{
		return parsers;
	}
	
	
	public TMLStreamParser getTmlParser()
	{
		return this.tmlParser;
	}
	
	
	private DataStreamParser createDataParser(DataComponent dataInfo, DataEncoding dataEncoding)
	{
		DataStreamParser parser = null;
		
		switch (dataEncoding.getEncodingType())
		{
			case ASCII:
				parser = new AsciiDataParser();
				break;
				
			case BINARY:
				parser = new BinaryDataParser();
				break;				
		}
		
		parser.setDataEncoding(dataEncoding);
		parser.setDataComponents(dataInfo);
		parser.reset();
		
		return parser;
	}
	
	
	public InputStream getDataStream() throws CDMException
	{
		return this.inputStream;
	}
}
