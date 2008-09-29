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


public class TMLFilter extends InputStream
{
	InputStream source;
	boolean startElt = false;
	boolean stopElt = false;
	boolean readData = false;
	boolean endData = false;
	StringBuffer buf;
	
	
	public TMLFilter(InputStream source)
	{
		this.source = source;
	}
	
	
	public int read() throws IOException
	{
		if (endData)
		{
			return -1;
		}
		else if (readData)
		{
			int val = -1;
			
			// skip all control characters (before space)
			do
			{
				val = source.read();
				
				if ((char)val == '<' || val == -1)
				{
					//readData = false;
					endData = true;
					return -1;
				}
			}
			while (val < 32);
			return val;				
		}
		else
		{
			int val = source.read();
						
			if (((char)val) == '<')
			{
				buf = new StringBuffer(30);
				startElt = true;
				stopElt = false;
			}
			else if (((char)val) == '>')
			{
				startElt = false;
				stopElt = true;
			}
			else if (startElt)
			{
				buf.append((char)val);
			}
				
			return val;
		}
	}
	
	
	public void close() throws IOException
	{
		//source.close();
	}
	
	
	public TMLCluster getNext() throws IOException
	{
		endData = false;
		
		if (readData)
		{
			readData = false;
			
			// read closing tag
			stopElt = false;
			while (!stopElt)
				this.read();
		}
		
		// read opening tag
		readData = false;
		stopElt = false;
		while (!stopElt)
			this.read();
		
		// parse attribute values
		int start = buf.indexOf("\"");
		int end = buf.indexOf("\"", start+1);
		if(start == -1 || end == -1)
			return null;   // bad cluster
		String clusterID = buf.substring(start+1, end);
		
		// construct cluster object
		TMLCluster cluster = new TMLCluster(clusterID, 0, this);
		
		readData = true;
		
		return cluster;
	}
}
