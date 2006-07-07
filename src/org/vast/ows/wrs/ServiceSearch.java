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
    Kevin Carter <kcarter@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wrs;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.vast.io.xml.DOMReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ServiceSearch extends DOMReader
{
	public String server;
	
	public ServiceSearch(String serverURL, String id)
	{
		try
		{
			URL url = new URL(serverURL);
			
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty( "Content-type", "text/plain" );
			PrintStream out = new PrintStream(connection.getOutputStream());
			
			String postRequest = createServerRequest(id);
			
			out.print(postRequest);
			out.flush();
			connection.connect();
			out.close();
		
			InputStream in = connection.getInputStream();
			
			DOMReader reader = new DOMReader(in,false);
			
			NodeList list = reader.getElements("SearchResults/Service/ServiceBinding");
			
			for(int i=0; i<list.getLength(); i++)
				{
					server = reader.getAttributeValue((Element)list.item(0),"accessURI");
				}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public String createServerRequest(String id)
	{	
		return  "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "+
				"<GetRecords maxRecords=\"1\" "+
				"	outputFormat=\"application/xml; charset=UTF-8\" "+
				"	outputSchema=\"EBRIM\" version=\"2.0.0\" "+
				"	xmlns=\"http://www.opengis.net/cat/csw\"> "+
		    	"	<Query typeNames=\"Service Association\"> "+
		    	"		<ElementName>/Service</ElementName> "+
		    	"		<Constraint version=\"1.0.0\"> "+
		    	"			<ogc:Filter "+
		    	"				xmlns:ebxml=\"urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.5\" "+
		    	"				xmlns:gml=\"http://www.opengis.net/gml\" "+
		    	"				xmlns:ogc=\"http://www.opengis.net/ogc\"> "+
		    	"				<ogc:And> "+
		    	"					<ogc:PropertyIsEqualTo> "+
		        "                		<ogc:PropertyName>/Association/@sourceObject</ogc:PropertyName> "+
		        "                		<ogc:PropertyName>/Service/@id</ogc:PropertyName> "+
		    	"					</ogc:PropertyIsEqualTo> "+
		    	"					<ogc:PropertyIsEqualTo> "+
		        "                		<ogc:PropertyName>/Association/@targetObject</ogc:PropertyName> "+
		        "                		<ogc:Literal>"+ id +"</ogc:Literal> "+
		    	"					</ogc:PropertyIsEqualTo> "+
		    	"				</ogc:And> "+    
		    	"			</ogc:Filter> "+
		        "        </Constraint> "+
		        "    </Query> "+
		        "</GetRecords> ";

	}

	public String getServer() 
	{
		return server;
	}
	
		
}

