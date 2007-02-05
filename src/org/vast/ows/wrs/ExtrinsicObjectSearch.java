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

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import org.vast.xml.DOMHelper;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSQuery;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.OWSUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ExtrinsicObjectSearch extends DOMHelper
{
	protected static String Service;
	protected static String Keyword;
	protected static String LayerName;
	protected static float MinX, MinY, MaxX, MaxY;
	protected static String SRSNumber;
	protected static String Format;
	protected static String ServerURL;
    DOMHelper dom;
	ArrayList<String> serviceList;
	String sosVersion = "0.0.31";
	String wmsVersion = "1.1.1";
	String wfsVersion = "1.0.0";
    OWSUtils owsUtils = new OWSUtils();
	
	public ExtrinsicObjectSearch()
	{
		Keyword="";
	}
	public ExtrinsicObjectSearch(String services, String name, String keyword, 
			String nr, float minx, float miny, float maxx, float maxy, String format, String serverURL)
	{
		Service=services;
		LayerName=name;
		Keyword=keyword;
		SRSNumber=nr;
		MinX=minx; MinY=miny; MaxX=maxx; MaxY=maxy;
		Format=format;
		ServerURL=serverURL;
		serviceList = new ArrayList<String>();
			serviceList.add("ObservationOffering");
			serviceList.add("WMS_Layer");
			serviceList.add("FeatureType");

	}

	public String CatalogRequest()//Make StringBuffer
	{
		//Beginnings XML
		String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<GetRecords maxRecords=\"\" outputFormat=\"application/xml; "+
				"	charset=UTF-8\" outputSchema=\"EBRIM\" version=\"2.0.0\" "+
				"	xmlns=\"http://www.opengis.net/cat/csw\"> "+
				"	<Query typeNames=\"ExtrinsicObject\"> "+
				"		<ElementName>/ExtrinsicObject</ElementName> "+
				"		<Constraint version=\"1.0.0\"> "+
				"			<ogc:Filter xmlns:ebxml=\"urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.5\" "+
				"				xmlns:gml=\"http://www.opengis.net/gml\" "+
				"				xmlns:ogc=\"http://www.opengis.net/ogc\" "+
				"				xmlns:csw=\"http://www.opengis.net/csw\"> ";

		//Add Service XML
		boolean addService=false;
		if (Service.trim().length()!=0)
		{
			addService = true;
			request = request + "<ogc:And> "+
								"	<ogc:PropertyIsEqualTo> "+
	                    		"		<ogc:PropertyName>/ExtrinsicObject/@objectType</ogc:PropertyName> "+
	                    		"		<ogc:Literal>"+ Service +"</ogc:Literal> "+
	                			"	</ogc:PropertyIsEqualTo> ";
		}

		//Add Name&Keyword XML
		if (LayerName.trim().length()!=0 || Keyword.trim().length()!=0)
        {
        	//For Maximum Results:
			//If LayerName is empty,
			//Keyword in put in LayerName field.
			String variableName=LayerName;
        	String variableKeyword=Keyword;
        	if(LayerName.equalsIgnoreCase(""))
        	    variableName=Keyword;

        	request = request + "<ogc:Or> "+
        						"	<ogc:PropertyIsLike escape=\"\\\" singleChar=\"_\" wildCard=\"%\"> "+
        						"		<ogc:PropertyName> "+
                    			"			/ExtrinsicObject/Name/LocalizedString/@value "+
                                "        </ogc:PropertyName> "+
                				"		<ogc:Literal>%"+ variableName +"%</ogc:Literal> "+
            					"	</ogc:PropertyIsLike> "+
        						"	<ogc:PropertyIsLike escape=\"\\\" singleChar=\"_\" wildCard=\"%\"> "+
        						"		<ogc:PropertyName> "+
                				"			/ExtrinsicObject/Description/LocalizedString/@value "+
                                "        </ogc:PropertyName> "+
                				"	<ogc:Literal>%"+ variableKeyword +"%</ogc:Literal> "+
               					"	</ogc:PropertyIsLike> "+	
        						"	<ogc:PropertyIsLike> "+
            					"		<ogc:Function name=\"CATALOG_TOOLS.SLOT_AS_STRING\"> "+
            					"			<ogc:PropertyName>/ExtrinsicObject/Slot/Phenomenas</ogc:PropertyName> "+
                                "        </ogc:Function> "+
                                "        <ogc:Literal>%"+ variableKeyword +"%</ogc:Literal> "+
                                "    </ogc:PropertyIsLike> "+
                                "    <ogc:PropertyIsLike> "+
                                "        <ogc:Function name=\"CATALOG_TOOLS.SLOT_AS_STRING\"> "+
                                "            <ogc:PropertyName>/ExtrinsicObject/Slot/Identifier</ogc:PropertyName> "+
                                "        </ogc:Function> "+
                                "        <ogc:Literal>%"+ variableKeyword +"%</ogc:Literal> "+
                                "    </ogc:PropertyIsLike> "+
        						"</ogc:Or> ";
        }

		//Add Bounded XML
        if (MinX!=-180 || MinY!=-90 || MaxX!= 180 || MaxY!=90)
        {
        	if(!addService) request = request + "<ogc:And>";
        	request = request + "<ogc:Contains> "+
            					"	<ogc:PropertyName>/ExtrinsicObject/Slot/FootPrint</ogc:PropertyName>" +
            					"	<gml:Box srsName=\"EPSG:"+ SRSNumber +"\"> "+
            					"		<gml:coordinates>"+ MinX +","+ MinY +" "+ MaxX +","+ MaxY +"</gml:coordinates> "+
                                "    </gml:Box> "+
                                "</ogc:Contains> ";
        	if(!addService) request = request + "</ogc:And>";
        }
		//Add Endings
		if(addService)
		{
			request = request + "</ogc:And> ";
		}
		request = request + "            </ogc:Filter> "+
        					"		</Constraint> "+
							"	</Query> "+
							"</GetRecords> ";
		
		return request;
	}
	
	public List<OWSLayerCapabilities> EOSSearch()
	{
		try
		{
			URL url = new URL(ServerURL);

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty( "Content-type", "text/plain" );
			PrintStream out = new PrintStream(connection.getOutputStream());

			String postRequest;

			postRequest = CatalogRequest();

			out.print(postRequest);
			out.flush();
			connection.connect();
			out.close();

			InputStream in = connection.getInputStream();
            DOMHelper reader = new DOMHelper(in,false);
			String server = null;
			
			NodeList list = reader.getElements("SearchResults/ExtrinsicObject");
			
			ArrayList<OWSLayerCapabilities> layerCapAl = new ArrayList<OWSLayerCapabilities>();
			ArrayList<String>serverList = new ArrayList<String>();
			ArrayList<String>errorList = new ArrayList<String>();
			ArrayList idList = createIDList(reader);
			
			//Creates a list of the layer's servers
			for(int i=0; i<list.getLength(); i++)
			{
				String objectType = reader.getAttributeValue((Element)list.item(i), "objectType");
				
				if(serviceList.contains(objectType))
				{
					ServiceSearch search = new ServiceSearch(ServerURL, reader.getAttributeValue((Element)list.item(i),"id"));
					server = search.getServer();
				}

				if(server == null) 
					continue;
				if(serverList.contains(server))  //  We've already gotten this cap doc.
					continue;
				
				//Check for connection to server
				if(!(errorList.contains(server)) && testServer(server))
					{
						serverList.add(server);
					}
				else
					{
						errorList.add(server);
						continue;
					}

				List<OWSLayerCapabilities> layerCapTmp = getEOSLayers(idList,server);
				//  
				//  Mod here first if we want to show layers separated by server and capDoc in OGCMain window.
				//  
				layerCapAl.addAll(layerCapTmp);
			}

			List<OWSLayerCapabilities> layerCap = layerCapAl;
			
			return layerCap;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	protected List<OWSLayerCapabilities> getEOSLayers(ArrayList idList, String server)
	{
		server = OWSQuery.checkServer(server);	
			
		OWSServiceCapabilities owsCap=null;
		
		try {
			String Version;
			//Creates ServiceCapabilities for SOS
			if(Service.equalsIgnoreCase("ObservationOffering"))
			{
				Version = sosVersion;
                owsUtils.getCapabilities(server, "SOS", Version);
			}
			//Creates ServiceCapabilities for WMS
			if(Service.equalsIgnoreCase("WMS_Layer"))
			{
				Version = wmsVersion;
                owsUtils.getCapabilities(server, "WCS", Version);
			}
			//Creates ServiceCapabilities for WFS
			if(Service.equalsIgnoreCase("FeatureType"))
			{
				Version = wfsVersion;
                owsUtils.getCapabilities(server, "WFS", Version);
			}
			
			List<OWSLayerCapabilities> capList = new ArrayList<OWSLayerCapabilities>
								(idList.size());
			
			boolean found;
			int capCount=0;
			
			//Eliminates null values from owsCap list
			for(int i=0; i<owsCap.getLayers().size(); i++)
			{
				if(owsCap.getLayers().get(i).getId()==null) 
				{
					for(int j=i; j<owsCap.getLayers().size()-1; j++)
					{
						owsCap.getLayers().set(j, owsCap.getLayers().get(j+1));
					}
				}
			}	

			//Parses owsCap for wanted layers
			for(int i=0; i<owsCap.getLayers().size() || capCount<capList.size(); i++)
			{
				found=false;
				for(int j=0; j<idList.size(); j++)
				{
					if(idList.get(j)==null)
						continue;
					if(owsCap.getLayers().get(i).getId().equalsIgnoreCase(
							idList.get(j).toString()))
					{
						found=true;
						break;
					}
				}
				if(found)
				{
					capList.add(capCount,owsCap.getLayers().get(i));
					capCount++;
				}
			}
			//Stores wanted layers in owsCap
			owsCap.setLayers(capList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		List<OWSLayerCapabilities> capList = owsCap.getLayers();
		return capList;
	}
	
	protected ArrayList createIDList(DOMHelper reader)
	{	
		NodeList list = reader.getElements("SearchResults/ExtrinsicObject");
		ArrayList<String>idList = new ArrayList<String>();
		
		String id=null;
		
		//Creates a list of wanted layers' IDs
		for(int i=0; i<list.getLength(); i++)
		{
			NodeList slotList = reader.getElements((Element)list.item(i),"Slot");
			//SOS IDs
			if(Service.equalsIgnoreCase("ObservationOffering"))
				for(int j=0; j<slotList.getLength(); j++)
				{
					if(reader.getAttributeValue((Element)slotList.item(j),"name").equalsIgnoreCase("ObservationOfferingId"))
					{
						id = reader.getElementValue((Element)slotList.item(j),"ValueList/Value");
						break;
					}
				}
			//WMS and WFS IDs
			else
				for(int j=0; j<slotList.getLength(); j++)
				{
					if(reader.getAttributeValue((Element)slotList.item(j),"name").equalsIgnoreCase("Identifier"))
					{	
						id = reader.getElementValue((Element)slotList.item(j),"ValueList/Value");
						break;
					}
				}
			if(!idList.contains(id))
				idList.add(id);	
		}
		return idList;
	}
	
	public boolean testServer(String server)
	{
		try
		{
			System.out.print("Checking: ");
			
			//Check SOS server
			if(Service.equalsIgnoreCase("ObservationOffering"))
			{
                owsUtils.getCapabilities(server, "SOS", sosVersion);
			}
			//Check WMS server
			else if(Service.equalsIgnoreCase("WMS_Layer"))
			{
                owsUtils.getCapabilities(server, "WMS", wmsVersion);
			}
		    //Check WFS server
			else if(Service.equalsIgnoreCase("FeatureType"))
			{
                owsUtils.getCapabilities(server, "WFS", wfsVersion);
			}
		}
		catch (Exception e)
		{
			System.out.println("Error reading server: " + server);
			return false;
		}
		return true;
	}

}
