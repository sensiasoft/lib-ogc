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

import org.vast.io.xml.DOMReader;
import org.vast.io.xml.DOMWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSQuery;
import org.vast.ows.OWSRequestReader;
import org.vast.ows.OWSRequestWriter;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.ows.sas.SASQuery;
import org.vast.ows.sos.SOSCapabilitiesReader;
import org.vast.ows.wfs.WFSCapabilitiesReader;
import org.vast.ows.wms.WMSCapabilitiesReader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class WRSRequestWriter extends OWSRequestWriter
{
	protected String serviceId;
	protected String keyword;
	protected String layerName;
	protected float minX, minY, maxX, maxY;
	protected String srs;
	protected String format;
	protected String serverUrl = "http://dev.ionicsoft.com:8082/ows4catalog/wrs/WRS";
	DOMReader dom;
	ArrayList<String> serviceList;
	String sosVersion = "0.0.31";
	String wmsVersion = "1.1.1";
	String wfsVersion = "1.0.0";
	
	public WRSRequestWriter(){
	}
	
	public WRSRequestWriter(String svcId, String name, String keyword, 
			String srs, float minx, float miny, float maxx, float maxy)
	{
		this.serviceId=svcId;
		this.layerName=name;
		this.keyword=keyword;
		this.srs=srs;
		this.minX=minx; 
		this.minY=miny; 
		this.maxX=maxx; 
		this.maxY=maxy;
		serviceList = new ArrayList<String>();
			serviceList.add("ObservationOffering");
			serviceList.add("WMS_Layer");
			serviceList.add("FeatureType");

	}

	public Element buildRequestXML(OWSQuery owsQuery, DOMWriter domWriter) throws OWSException
	{
		WRSQuery query = (WRSQuery)owsQuery;
		domWriter.addNS("http://www.opengis.net/cat/csw", "csw");
		domWriter.addNS("http://www.opengis.net/ogc", "ogc");
//		domWriter.addNS("http://www.opengis.net/gml", "gml");
//		domWriter.addNS("urn:oasis:names:tc:ebxml-regrep:rim:xsd:2.5", "ebxml");
		
		// root element
		Element rootElt = domWriter.createElement("csw:GetRecords");
		domWriter.setAttributeValue(rootElt, "version", "2.0.0");
//		domWriter.setAttributeValue(rootElt, "maxRecords", "10");
		domWriter.setAttributeValue(rootElt, "outputSchema", "EBRIM");
//		domWriter.setAttributeValue(rootElt, "outputFormat", "application/xml");
//		domWriter.setAttributeValue(rootElt, "charset", "UTF-8");
		
		//  Query element
		Element queryElt = domWriter.addElement(rootElt, "csw:Query");
		queryElt.setAttribute("typeNames", "Service Concept Classification");
		domWriter.setElementValue(queryElt,"csw:ElementName","/Service");
		Element constraintElt = domWriter.addElement(queryElt, "csw:Constraint");
		Element filterElt = domWriter.addElement(constraintElt,"ogc:Filter/ogc:And");
		constraintElt.setAttribute("version", "1.0.0");
		Element propElt = domWriter.addElement(filterElt, "ogc:PropertyIsEqualTo");
		domWriter.setElementValue(propElt,"ogc:PropertyName", "/Concept/Name/LocalizedString/@value");
		domWriter.setElementValue(propElt,"ogc:Literal", "SOS");
//		propElt = domWriter.addElement(filterElt, "+ogc:PropertyIsEqualTo");
//		domWriter.setElementValue(propElt,"ogc:PropertyName", "/Concept/@id");
//		domWriter.setElementValue(propElt,"+ogc:PropertyName", "/Classification/@classificationNode");
		propElt = domWriter.addElement(filterElt, "+ogc:PropertyIsEqualTo");
		domWriter.setElementValue(propElt,"ogc:PropertyName", "/Service/@id");
		domWriter.setElementValue(propElt,"+ogc:PropertyName", "/Classification/@classifiedObject");

		// display request
//        try {domWriter.writeDOM(rootElt, System.out, null);}
//        catch (Exception e) {e.printStackTrace();}        

		return rootElt;
	}
	
	public String oldBuildRequest()
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
		if (serviceId.trim().length()!=0)
		{
			addService = true;
			request = request + "<ogc:And> "+
								"	<ogc:PropertyIsEqualTo> "+
	                    		"		<ogc:PropertyName>/ExtrinsicObject/@objectType</ogc:PropertyName> "+
	                    		"		<ogc:Literal>"+ serviceId +"</ogc:Literal> "+
	                			"	</ogc:PropertyIsEqualTo> ";
		}

		//Add Name&Keyword XML
		if (layerName.trim().length()!=0 || keyword.trim().length()!=0)
        {
        	//For Maximum Results:
			//If LayerName is empty,
			//Keyword in put in LayerName field.
			String variableName=layerName;
        	String variableKeyword=keyword;
        	if(layerName.equalsIgnoreCase(""))
        	    variableName=keyword;

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
        if (minX!=-180 || minY!=-90 || maxX!= 180 || maxY!=90)
        {
        	if(!addService) request = request + "<ogc:And>";
        	request = request + "<ogc:Contains> "+
            					"	<ogc:PropertyName>/ExtrinsicObject/Slot/FootPrint</ogc:PropertyName>" +
            					"	<gml:Box srsName=\"EPSG:"+ srs +"\"> "+
            					"		<gml:coordinates>"+ minX +","+ minY +" "+ maxX +","+ maxY +"</gml:coordinates> "+
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
			URL url = new URL(serverUrl);

			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty( "Content-type", "text/plain" );
			PrintStream out = new PrintStream(connection.getOutputStream());

			String postRequest;

			postRequest = oldBuildRequest();

			out.print(postRequest);
			out.flush();
			connection.connect();
			out.close();

			InputStream in = connection.getInputStream();
			DOMReader reader = new DOMReader(in,false);
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
					ServiceSearch search = new ServiceSearch(serverUrl, reader.getAttributeValue((Element)list.item(i),"id"));
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
			if(serviceId.equalsIgnoreCase("ObservationOffering"))
			{
				Version = sosVersion;
				owsCap = new SOSCapabilitiesReader().readCapabilities(server,Version);
			}
			//Creates ServiceCapabilities for WMS
			if(serviceId.equalsIgnoreCase("WMS_Layer"))
			{
				Version = wmsVersion;
				owsCap = new WMSCapabilitiesReader().readCapabilities(server,Version);
			}
			//Creates ServiceCapabilities for WFS
			if(serviceId.equalsIgnoreCase("FeatureType"))
			{
				Version = wfsVersion;
				owsCap = new WFSCapabilitiesReader().readCapabilities(server,Version);
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
		} catch (OWSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		List<OWSLayerCapabilities> capList = owsCap.getLayers();
		return capList;
	}
	
	protected ArrayList createIDList(DOMReader reader)
	{	
		NodeList list = reader.getElements("SearchResults/ExtrinsicObject");
		ArrayList<String>idList = new ArrayList<String>();
		
		String id=null;
		
		//Creates a list of wanted layers' IDs
		for(int i=0; i<list.getLength(); i++)
		{
			NodeList slotList = reader.getElements((Element)list.item(i),"Slot");
			//SOS IDs
			if(serviceId.equalsIgnoreCase("ObservationOffering"))
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
			if(serviceId.equalsIgnoreCase("ObservationOffering"))
			{
				SOSCapabilitiesReader testReader = new SOSCapabilitiesReader();
				testReader.readCapabilities(server,sosVersion);
			}
			//Check WMS server
			else if(serviceId.equalsIgnoreCase("WMS_Layer"))
			{
				WMSCapabilitiesReader testReader = new WMSCapabilitiesReader();
				testReader.readCapabilities(server,wmsVersion);
			}
		    //Check WFS server
			else if(serviceId.equalsIgnoreCase("FeatureType"))
			{
				WFSCapabilitiesReader testReader = new WFSCapabilitiesReader();
				testReader.readCapabilities(server,wfsVersion);
			}
		}
		catch (Exception e)
		{
			System.out.println("Error reading server: " + server);
			return false;
		}
		return true;
	}

	public static void main(String [] args) throws OWSException{
		WRSRequestWriter req = new WRSRequestWriter();
		OWSQuery query = new WRSQuery();
		req.buildPostRequest(query);
	}

	@Override
	public String buildGetRequest(OWSQuery query) throws OWSException {
		// TODO Auto-generated method stub
		return null;
	}
}
