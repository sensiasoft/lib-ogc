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

import java.util.List;
import org.vast.xml.DOMHelper;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.OWSException;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.util.Bbox;
import org.vast.ows.wrs.WRSQuery.QueryType;
import org.w3c.dom.Element;


public class WRSRequestWriter extends AbstractRequestWriter<WRSQuery>
{
	//  TODO  hardwired for ionic catalog (move to resource file eventually)
	
	public WRSRequestWriter(){
	}
	
	//  TODO  Modify to filter on keywd, layerName 
	public Element buildXMLQuery(DOMHelper domWriter, WRSQuery query) throws OWSException {
		Element rootElt = buildQueryRootElement(domWriter);
		List<QueryType> queryTypes = query.getQueryTypeList();
		
		if(queryTypes.isEmpty()) {
			buildQueryAllSosElement(domWriter, rootElt);
			return rootElt;
		}
		if(queryTypes.contains(QueryType.SERVICE_SOS)) {
			buildServiceSearchElement(domWriter, rootElt, query.getServiceSearchId());
			return rootElt;
		}
		//  Filter elements- should be able to be combined, but right now, it's either
		//  keyword OR bbox
		if(queryTypes.contains(QueryType.KEYWORD_SOS)) {
		    buildQueryKeywordElement(domWriter, rootElt, query.getKeyword() );
		    return rootElt;
		}
		if(queryTypes.contains(QueryType.BBOX_SOS))
			buildQueryBboxElement(domWriter, rootElt, query.getBbox());

		return rootElt;
	}
	
	protected Element buildQueryRootElement(DOMHelper domWriter) throws OWSException {
		domWriter.addUserPrefix("csw", OGCRegistry.getNamespaceURI(OGCRegistry.CSW));
		domWriter.addUserPrefix("ogc", OGCRegistry.getNamespaceURI(OGCRegistry.OGC));
		domWriter.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML));
		
		// root element
		Element rootElt = domWriter.createElement("csw:GetRecords");
		domWriter.setAttributeValue(rootElt, "version", "2.0.0");
		domWriter.setAttributeValue(rootElt, "maxRecords", "80");
		domWriter.setAttributeValue(rootElt, "outputSchema", "EBRIM");
		
		return rootElt;
	}
	
	/**
	 * 
	 * @param domWriter
	 * @param rootElt
	 * @return A CSW XML Query for getting all SOS instances from a Catalog
	 * @throws OWSException
	 */
	protected Element buildQueryAllSosElement(DOMHelper domWriter, Element rootElt) throws OWSException {
		Element queryElt = domWriter.addElement(rootElt, "csw:Query");
		queryElt.setAttribute("typeNames", "Service Concept Classification");
		domWriter.setElementValue(queryElt,"csw:ElementName","/Service");
		Element constraintElt = domWriter.addElement(queryElt, "csw:Constraint");
		constraintElt.setAttribute("version", "1.0.0");
		Element filterElt = domWriter.addElement(constraintElt,"ogc:Filter/ogc:And");
		addPropertyIsEqualToLiteral(domWriter, filterElt, "/Concept/Name/LocalizedString/@value", "SOS");
		addPropertyIsEqualToProperty(domWriter, filterElt, "/Concept/@id", "/Classification/@classificationNode");
		addPropertyIsEqualToProperty(domWriter, filterElt, "/Service/@id", "/Classification/@classifiedObject");
		
		return queryElt;
	}
	
	/**
	 * 
	 * @param domWriter
	 * @param rootElt
	 * @param bbox
	 * @return a CSW XML Query for finding all ExtrinsicObjects within the requested BBOX of the query
	 * @throws OWSException
	 */
	protected Element buildQueryBboxElement(DOMHelper domWriter, Element rootElt, Bbox bbox) 
			throws OWSException {
		Element queryElt = domWriter.addElement(rootElt, "csw:Query");
		queryElt.setAttribute("typeNames", "ExtrinsicObject");
		domWriter.setElementValue(queryElt,"csw:ElementName","/ExtrinsicObject");
		Element constraintElt = domWriter.addElement(queryElt, "csw:Constraint");
		constraintElt.setAttribute("version", "1.0.0");
		Element filterElt = domWriter.addElement(constraintElt,"ogc:Filter/ogc:And");
		addPropertyIsEqualToLiteral( domWriter, filterElt, "/ExtrinsicObject/@objectType", "ObservationOffering");
		Element intersectsElt = domWriter.addElement(filterElt, "ogc:Intersects");
		domWriter.setElementValue(intersectsElt,"ogc:PropertyName", 
				"/ExtrinsicObject/Slot[@name=\"FootPrint\"]/ValueList/Value[1]");
		Element gmlBoxElt = domWriter.addElement(intersectsElt,"gml:Box");
		gmlBoxElt.setAttribute("srsName","EPSG:4326");
		String bboxStr = bbox.getMinX() + "," + bbox.getMinY()+ " " + bbox.getMaxX() + "," + bbox.getMaxY();
		domWriter.setElementValue(gmlBoxElt, "gml:coordinates", bboxStr);
		
		return queryElt;
	}
	
	/**
	 * 
	 * @param domWriter
	 * @param rootElt
	 * @param targetId
	 * @return  a CSW XML Query for finding the Service assocaited extrinsicObject targetId
	 */
	protected Element buildServiceSearchElement(DOMHelper domWriter, Element rootElt, 
													 String targetId){
		Element targetElt = domWriter.addElement(rootElt, "csw:Query");
		targetElt.setAttribute("typeNames", "Service Association");
		domWriter.setElementValue(targetElt,"csw:ElementName","/Service");
		Element constraintElt = domWriter.addElement(targetElt, "csw:Constraint");
		constraintElt.setAttribute("version", "1.0.0");
		Element filterElt = domWriter.addElement(constraintElt,"ogc:Filter/ogc:And");
		addPropertyIsEqualToProperty(domWriter, filterElt, "/Association/@sourceObject", "/Service/@id");
		addPropertyIsEqualToLiteral(domWriter, filterElt, "/Association/@targetObject", targetId);
		
		//  missing one prop I think...
		return targetElt;
	}

	protected Element buildQueryKeywordElement(DOMHelper domWriter, Element rootElt, String keyword) 
		throws OWSException {
		String kws = "%" + keyword + "%";
		Element queryElt = domWriter.addElement(rootElt, "csw:Query");
		queryElt.setAttribute("typeNames", "ExtrinsicObject");
		domWriter.setElementValue(queryElt,"csw:ElementName","/ExtrinsicObject");
		Element constraintElt = domWriter.addElement(queryElt, "csw:Constraint");
		constraintElt.setAttribute("version", "1.0.0");
		Element filterElt = domWriter.addElement(constraintElt,"ogc:Filter/ogc:And");
		addPropertyIsEqualToLiteral(domWriter, filterElt, "/ExtrinsicObject/@objectType", "ObservationOffering");
		Element orElt = domWriter.addElement(filterElt, "ogc:Or");
		addPropertyIsLikeLiteral(domWriter, orElt, "/ExtrinsicObject/Name/LocalizedString/@value", kws);
		addPropertyIsLikeLiteral(domWriter, orElt, "/ExtrinsicObject/Description/LocalizedString/@value", kws);
		addPropertyIsLikeLiteral(domWriter, orElt, "/ExtrinsicObject/Slot[@name=\"ResultFormats\"]/ValueList/Value[1]", kws);
		addPropertyIsLikeLiteral(domWriter, orElt, "/ExtrinsicObject/Slot[@name=\"Procedures\"]/ValueList/Value[1]", kws);
		addPropertyIsLikeLiteral(domWriter, orElt, "/ExtrinsicObject/Slot[@name=\"ResultModels\"]/ValueList/Value[1]", kws);
		addPropertyIsLikeLiteral(domWriter, orElt, "/ExtrinsicObject/Slot[@name=\"ObservedProperties\"]/ValueList/Value[1]", kws);
//		Element andElt = domWriter.addElement(orElt, "ogc:And");
//		addPropertyIsEqualToProperty(domWriter, andElt, "/Association/@sourceObject", "/ExtrinsicObject/@id");
//		addPropertyIsEqualToProperty(domWriter, andElt, "/Association/@targetObject", "/A/@id");
//		addPropertyIsEqualToLiteral(domWriter, andElt, "/Association/@associationType", "HasFeatureOfInterest");
//		Element or2Elt = domWriter.addElement(andElt, "ogc:Or");
//		addPropertyIsLikeLiteral(domWriter, or2Elt, "/A/Name/LocalizedString/@value", kws);
//		addPropertyIsLikeLiteral(domWriter, or2Elt, "/A/Description/LocalizedString/@value", kws);
		
		return queryElt;
	}
	
	protected Element addPropertyIsEqualToProperty(DOMHelper domWriter, Element parentElt, 
			String propName, String targetProp){
		Element propElt = domWriter.addElement(parentElt, "+ogc:PropertyIsEqualTo");
		domWriter.setElementValue(propElt,"ogc:PropertyName", propName);
		domWriter.setElementValue(propElt,"+ogc:PropertyName", targetProp);

		return propElt;
	}

	protected Element addPropertyIsEqualToLiteral(DOMHelper domWriter, Element parentElt, 
			String propName, String literal){
		Element propElt = domWriter.addElement(parentElt, "+ogc:PropertyIsEqualTo");
		domWriter.setElementValue(propElt,"ogc:PropertyName", propName);
		domWriter.setElementValue(propElt,"ogc:Literal", literal);
		
		return propElt;
	}

	protected Element addPropertyIsLikeLiteral(DOMHelper domWriter, Element parentElt, 
			String propName, String literal){
		Element propElt = domWriter.addElement(parentElt, "+ogc:PropertyIsLike");
		domWriter.setElementValue(propElt,"ogc:PropertyName", propName);
		domWriter.setElementValue(propElt,"ogc:Literal", literal);
		
		return propElt;
	}


	@Override
	//  WRS does not support KVP request
	public String buildURLQuery(WRSQuery query) throws OWSException {
		// TODO Auto-generated method stub
		return null;
	}
}
