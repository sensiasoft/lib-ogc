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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataHandler;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.ErrorHandler;
import org.vast.cdm.common.RawDataHandler;
import org.vast.cdm.common.StreamDemux;
import org.vast.sweCommon.URIStreamHandler;


public class TMLStreamParser implements StreamDemux, DataStreamParser
{
	Hashtable<String,DataStreamParser> parsers;
	boolean stopParsing = false;
		
	
	public TMLStreamParser()
	{
		this.parsers = new Hashtable<String,DataStreamParser>();
	}
	
	
	public void parse(InputStream inputStream) throws CDMException
	{
		try
		{
			TMLFilter filter = new TMLFilter(inputStream);
			TMLCluster newCluster;
			int errCnt = 0;
			
			//while(((newCluster = filter.getNext()) != null) && (!stopParsing))
			while(!stopParsing) //)
			{
				newCluster = filter.getNext();
				if(newCluster == null) {
					errCnt++;
					continue;
				}
				// find the right parser, skip if not available
				DataStreamParser parser = parsers.get(newCluster.id);			
				if (parser!=null)
					parser.parse(newCluster.data);
			}
		}
		catch (IOException e)
		{
			throw new CDMException("Error while parsing TML stream", e);
		}
		finally
		{
			try
			{
				inputStream.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}


	public void addDataParser(String clusterID, DataStreamParser dataParser)
	{
		this.parsers.put(clusterID, dataParser);
	}


	public void removeDataParser(String clusterID)
	{
		this.parsers.remove(clusterID);
	}


	public DataStreamParser getDataParser(String clusterID)
	{
		return this.parsers.get(clusterID);
	}
	
	
	public void parse(String uri) throws CDMException
	{
		InputStream in = URIStreamHandler.openStream(uri);
		this.parse(in);
	}


	public void parse(URI uri) throws CDMException
	{
		InputStream in = URIStreamHandler.openStream(uri);
		this.parse(in);
	}
	
	
	public void stop()
	{
		stopParsing = true;	
	}
	
	
	public void reset()
	{
		// nothing to reset
	}


	public DataComponent getDataComponents()
	{
		return null;
	}


	public DataEncoding getDataEncoding()
	{
		return null;
	}


	public DataHandler getDataHandler()
	{
		return null;
	}


	public ErrorHandler getErrorHandler()
	{
		return null;
	}


	public RawDataHandler getRawDataHandler()
	{
		return null;
	}


	public void setDataComponents(DataComponent components)
	{
	}


	public void setDataEncoding(DataEncoding encoding)
	{
	}


	public void setDataHandler(DataHandler handler)
	{
	}


	public void setErrorHandler(ErrorHandler handler)
	{
	}


	public void setRawDataHandler(RawDataHandler handler)
	{
	}
}
