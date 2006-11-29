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
    Tony Cook <tcook@nsstc.uah.edu>
    Alexandre Robin <robin@nsstc.uah.edu>
    Kevin Carter <kcarter@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wrs;

import java.util.ArrayList;

import org.vast.io.xml.DOMReader;
import org.vast.io.xml.DOMWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSQuery;
import org.vast.ows.OWSRequestWriter;
import org.w3c.dom.Element;

public class WRSRequestWriter extends OWSRequestWriter
{
	protected String serviceId;
	protected String keyword;
	protected String layerName;
	protected float minX, minY, maxX, maxY;
	protected String srs;
	protected String format;
	//  TODO  hardwired for ionic catalog (move to resource file eventually)
	protected String serverUrl = "http://dev.ionicsoft.com:8082/ows4catalog/wrs/WRS";
	DOMReader dom;
	ArrayList<String> serviceList;
	
	public WRSRequestWriter(){
	}
	
	//  TODO  Modify to filter on keywd, layerName and bbox
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
