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
import java.util.ArrayList;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.InputStreamProvider;
import org.vast.sweCommon.AsciiDataParser;
import org.vast.sweCommon.BinaryDataParser;
import org.vast.sweCommon.SWEFilter;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.util.ReaderException;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.*;


public class TMLReader implements InputStreamProvider
{
	SWEFilter streamFilter;
	InputStream inputStream;
	TMLStreamParser tmlParser;
	ArrayList<DataStreamParser> parsers;
	
	
	public TMLReader()
	{
		parsers = new ArrayList<DataStreamParser>();
	}
	
	
	public void parse(InputStream inputStream) throws IOException
	{		
		try
		{
			this.streamFilter = new SWEFilter(inputStream);
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
			DOMHelper dom = new DOMHelper(this.streamFilter, false);
			
			// create and register a parser for each product
            SWECommonUtils utils = new SWECommonUtils();
			NodeList products = dom.getElements("product/DataBlockDefinition");
			for (int i=0; i<products.getLength(); i++)
			{
				Element defElt = (Element)products.item(i);
				String clusterID = dom.getAttributeValue(defElt, "id");
				
				// get structure and encoding elements
				Element dataElt = dom.getElement(defElt, "components");
				Element encElt = dom.getElement(defElt, "encoding");
				
				// parse structure and encoding                
                DataComponent dataInfo = utils.readComponentProperty(dom, dataElt);
                DataEncoding dataEncoding = utils.readEncodingProperty(dom, encElt);
                				
				// create parser
				DataStreamParser parser = createDataParser(dataInfo, dataEncoding);
				
				// register parser
				this.tmlParser.addDataParser(clusterID, parser);
				this.parsers.add(parser);
			}
		}
		catch (DOMHelperException e)
		{
			throw new ReaderException("Error while parsing TML XML", e);
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
	
	
	private DataStreamParser createDataParser(DataComponent dataInfo, DataEncoding dataEncoding) throws IOException
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
	
	
	public InputStream getDataStream() throws IOException
	{
		return this.inputStream;
	}
}
