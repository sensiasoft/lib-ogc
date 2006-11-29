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
    Tony Cook <tcook@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wrs;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.vast.io.xml.DOMReader;
import org.vast.io.xml.DOMReaderException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p><b>Title:</b>
 *  WRSResponseReader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2006</p>
 * @author Tony Cook
 * @date Nov 28, 2006
 * @version 1.0
 */

public class WRSResponseReader {
	
	public List<String> parseAllSOS(InputStream inputStream) throws IOException, DOMReaderException {
		List<String> sosUri = new ArrayList<String>();
		
		DOMReader reader = new DOMReader(inputStream, false);
		NodeList svcList = reader.getElements("SearchResults/Service");
		for(int i=0; i<svcList.getLength(); i++) {
			Element svcElt = (Element)svcList.item(i);
			NodeList slotList = reader.getElements(svcElt, "Slot");
			for (int j=0; j<slotList.getLength(); j++){
				Element slotElt = (Element)slotList.item(j);
				String att = slotElt.getAttribute("name");
				if(att.equals("Identifier")){
					String val = reader.getElementValue(slotElt, "ValueList/Value");
					if(val != null && val.equals("OGC:SOS")){
						// drill down to SOS serviceBinding address
						Element svcBindingElt = reader.getElement(svcElt, "ServiceBinding");
						String accessUri = svcBindingElt.getAttribute("accessURI");
						if(accessUri != null)
							sosUri.add(accessUri);
					}
					//  Found the service, break out of for(j)
					break;
				}
			}
		}
		return sosUri;
	}
	
//	public List<OWSLayerCapabilities> EOSSearch() {
//			InputStream in = null;
//			DOMReader reader = new DOMReader(in,false);
//			String server = null;
//			
//			NodeList list = reader.getElements("SearchResults/ExtrinsicObject");
//			
//			ArrayList<OWSLayerCapabilities> layerCapAl = new ArrayList<OWSLayerCapabilities>();
//			ArrayList<String>serverList = new ArrayList<String>();
//			ArrayList<String>errorList = new ArrayList<String>();
//			ArrayList idList = createIDList(reader);
//			
//			//Creates a list of the layer's servers
//			for(int i=0; i<list.getLength(); i++)
//			{
//				String objectType = reader.getAttributeValue((Element)list.item(i), "objectType");
//				
//				if(serviceList.contains(objectType))
//				{
//					ServiceSearch search = new ServiceSearch(serverUrl, reader.getAttributeValue((Element)list.item(i),"id"));
//					server = search.getServer();
//				}
//
//				if(server == null) 
//					continue;
//				if(serverList.contains(server))  //  We've already gotten this cap doc.
//					continue;
//				
//				//Check for connection to server
//				if(!(errorList.contains(server)) && testServer(server))
//					{
//						serverList.add(server);
//					}
//				else
//					{
//						errorList.add(server);
//						continue;
//					}
//
//				List<OWSLayerCapabilities> layerCapTmp = getEOSLayers(idList,server);
//				//  
//				//  Mod here first if we want to show layers separated by server and capDoc in OGCMain window.
//				//  
//				layerCapAl.addAll(layerCapTmp);
//			}
//
//			List<OWSLayerCapabilities> layerCap = layerCapAl;
//			
//			return layerCap;
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	protected List<OWSLayerCapabilities> getEOSLayers(ArrayList idList, String server)
//	{
//		server = OWSQuery.checkServer(server);	
//			
//		OWSServiceCapabilities owsCap=null;
//		
//		try {
//			String Version;
//			//Creates ServiceCapabilities for SOS
//			if(serviceId.equalsIgnoreCase("ObservationOffering"))
//			{
//				Version = sosVersion;
//				owsCap = new SOSCapabilitiesReader().readCapabilities(server,Version);
//			}
//			//Creates ServiceCapabilities for WMS
//			if(serviceId.equalsIgnoreCase("WMS_Layer"))
//			{
//				Version = wmsVersion;
//				owsCap = new WMSCapabilitiesReader().readCapabilities(server,Version);
//			}
//			//Creates ServiceCapabilities for WFS
//			if(serviceId.equalsIgnoreCase("FeatureType"))
//			{
//				Version = wfsVersion;
//				owsCap = new WFSCapabilitiesReader().readCapabilities(server,Version);
//			}
//			
//			List<OWSLayerCapabilities> capList = new ArrayList<OWSLayerCapabilities>
//								(idList.size());
//			
//			boolean found;
//			int capCount=0;
//			
//			//Eliminates null values from owsCap list
//			for(int i=0; i<owsCap.getLayers().size(); i++)
//			{
//				if(owsCap.getLayers().get(i).getId()==null) 
//				{
//					for(int j=i; j<owsCap.getLayers().size()-1; j++)
//					{
//						owsCap.getLayers().set(j, owsCap.getLayers().get(j+1));
//					}
//				}
//			}	
//
//			//Parses owsCap for wanted layers
//			for(int i=0; i<owsCap.getLayers().size() || capCount<capList.size(); i++)
//			{
//				found=false;
//				for(int j=0; j<idList.size(); j++)
//				{
//					if(idList.get(j)==null)
//						continue;
//					if(owsCap.getLayers().get(i).getId().equalsIgnoreCase(
//							idList.get(j).toString()))
//					{
//						found=true;
//						break;
//					}
//				}
//				if(found)
//				{
//					capList.add(capCount,owsCap.getLayers().get(i));
//					capCount++;
//				}
//			}
//			//Stores wanted layers in owsCap
//			owsCap.setLayers(capList);
//		} catch (OWSException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//		List<OWSLayerCapabilities> capList = owsCap.getLayers();
//		return capList;
//	}
//	
//	protected ArrayList createIDList(DOMReader reader)
//	{	
//		NodeList list = reader.getElements("SearchResults/ExtrinsicObject");
//		ArrayList<String>idList = new ArrayList<String>();
//		
//		String id=null;
//		
//		//Creates a list of wanted layers' IDs
//		for(int i=0; i<list.getLength(); i++)
//		{
//			NodeList slotList = reader.getElements((Element)list.item(i),"Slot");
//			//SOS IDs
//			if(serviceId.equalsIgnoreCase("ObservationOffering"))
//				for(int j=0; j<slotList.getLength(); j++)
//				{
//					if(reader.getAttributeValue((Element)slotList.item(j),"name").equalsIgnoreCase("ObservationOfferingId"))
//					{
//						id = reader.getElementValue((Element)slotList.item(j),"ValueList/Value");
//						break;
//					}
//				}
//			//WMS and WFS IDs
//			else
//				for(int j=0; j<slotList.getLength(); j++)
//				{
//					if(reader.getAttributeValue((Element)slotList.item(j),"name").equalsIgnoreCase("Identifier"))
//					{	
//						id = reader.getElementValue((Element)slotList.item(j),"ValueList/Value");
//						break;
//					}
//				}
//			if(!idList.contains(id))
//				idList.add(id);	
//		}
//		return idList;
//	}
//	
//	public boolean testServer(String server)
//	{
//		try
//		{
//			System.out.print("Checking: ");
//			
//			//Check SOS server
//			if(serviceId.equalsIgnoreCase("ObservationOffering"))
//			{
//				SOSCapabilitiesReader testReader = new SOSCapabilitiesReader();
//				testReader.readCapabilities(server,sosVersion);
//			}
//			//Check WMS server
//			else if(serviceId.equalsIgnoreCase("WMS_Layer"))
//			{
//				WMSCapabilitiesReader testReader = new WMSCapabilitiesReader();
//				testReader.readCapabilities(server,wmsVersion);
//			}
//		    //Check WFS server
//			else if(serviceId.equalsIgnoreCase("FeatureType"))
//			{
//				WFSCapabilitiesReader testReader = new WFSCapabilitiesReader();
//				testReader.readCapabilities(server,wfsVersion);
//			}
//		}
//		catch (Exception e)
//		{
//			System.out.println("Error reading server: " + server);
//			return false;
//		}
//		return true;
//	}
		
}

