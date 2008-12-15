/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.util.List;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLEnvelopeWriter;
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.w3c.dom.*;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * Coverage Descriptions Writer V1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 23 nov. 07
 * @version 1.0
 */
public class CoverageDescriptionsWriterV10 extends AbstractResponseWriter<CoverageDescriptions>
{
	protected GMLEnvelopeWriter gmlWriter = new GMLEnvelopeWriter();
		
	
	public Element buildXMLResponse(DOMHelper dom, CoverageDescriptions response, String version) throws OWSException
	{
		// setup ns and create root elt
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WCS));
		Element rootElt = dom.createElement("CoverageDescription");
		dom.setAttributeValue(rootElt, "@version", "1.0");

		// add all coverage descriptions
		List<CoverageDescription> descriptions = response.getDescriptions();
		for (int i=0; i<descriptions.size(); i++)
		{
			CoverageDescription desc = descriptions.get(i);
			Element descElt = dom.addElement(rootElt, "+CoverageOffering");
			
			// title, abstract, keywords elts
			WCSCapabilitiesWriterV10.writeIdentification(dom, descElt, desc);
			
			// TODO lonLatEnvelope
						
			// spatial domain
			Element spDomainElt = dom.addElement(descElt, "domainSet/spatialDomain");
			
			// bbox list
			List<Bbox> bboxList = desc.getBboxList();
			for (int j=0; j<bboxList.size(); j++)
			{
				Element envElt = gmlWriter.writeEnvelopeWithPos(dom, bboxList.get(j));
				spDomainElt.appendChild(envElt);
			}
			
			// grid CRS
			WCSRectifiedGridCrs gridCrs = desc.getGridCrs();
			if (gridCrs != null)
			{
				dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML));
				Element gridElt = dom.addElement(spDomainElt, "gml:RectifiedGrid");
				dom.setAttributeValue(gridElt, "srsName", gridCrs.getBaseCrs());
				
				// limits
				Element limitsElt = dom.addElement(gridElt, "gml:limits");
				Bbox gridBox = new Bbox();
				gridBox.setMinX(0);
				gridBox.setMinY(0);
				gridBox.setMaxX(gridCrs.getGridSizes()[0]);
				gridBox.setMaxY(gridCrs.getGridSizes()[1]);
				Element gridEnvElt = gmlWriter.writeGridEnvelope(dom, gridBox);
				limitsElt.appendChild(gridEnvElt);
				
				// axisNames
				dom.setElementValue(gridElt, "+gml:axisName", "X");
				dom.setElementValue(gridElt, "+gml:axisName", "Y");
				
				// origin coordinates
				double[] origin = gridCrs.getGridOrigin();
				String originVal = origin[0] + " " + origin[1];
				dom.setElementValue(gridElt, "gml:origin/gml:pos", originVal);
				
				// offset vectors
				double[] vecX = gridCrs.getGridOffsets()[0];
				double[] vecY = gridCrs.getGridOffsets()[1];
				dom.setElementValue(gridElt, "+gml:offsetVector", vecX[0] + " " + vecX[1]);
				dom.setElementValue(gridElt, "+gml:offsetVector", vecY[0] + " " + vecY[1]);
			}
			
			// TODO write polygons list
			
			// TODO write time domain
			//Element timeDomainElt = dom.addElement(descElt, "domainSet/temporalDomain");
						
			// rangeSet
			Element rangeSetElt = dom.addElement(descElt, "rangeSet/RangeSet");
			RangeField field = desc.getRangeFields().get(0);
			
			// description, label, name
			WCSCapabilitiesWriterV10.writeIdentification(dom, rangeSetElt, field);
			
			// all range axes
			for (int j=0; j<field.getAxisList().size(); j++)
			{
				RangeAxis axis = field.getAxisList().get(j);
				Element axisElt = dom.addElement(rangeSetElt, "+axisDescription/AxisDescription");
				
				// description, label, name
				WCSCapabilitiesWriterV10.writeIdentification(dom, axisElt, axis);
				
				// axis values
				List<String> keys = axis.getKeys();
				for (int k=0; k<keys.size(); k++)
				{
					String key = keys.get(k);
					dom.setElementValue(axisElt, "values/+singleValue", key);
				}
				
				// TODO write semantic
				// TODO write refSys or refSysLabel	
			}
			
			// null value
			String nullValue = field.getNullValue().toString();
			dom.setElementValue(rangeSetElt, "nullValues/singleValue", nullValue);
			//*** end rangeSet
						
			// supported CRS and formats
			writeCRSList(dom, descElt, desc);
			writeFormatList(dom, descElt, desc);
			writeInterpMethodList(dom, descElt, field);			
		}
				
		return rootElt;
	}
	
	
	/**
	 * Writes all supported CRSs
	 * @param layerElt
	 * @return
	 */
	protected void writeCRSList(DOMHelper dom, Element descElt, CoverageDescription desc)
	{
		int numElts = desc.getCrsList().size();
		for (int i = 0; i < numElts; i++)
		{
			String crs = desc.getCrsList().get(i);
			dom.setElementValue(descElt, "supportedCRSs/+requestResponseCRSs", crs);
		}
		
		// native crs
		String nativeCrs = desc.getNativeCrs();
		if (nativeCrs != null)
			dom.setElementValue(descElt, "supportedCRSs/nativeCRSs", nativeCrs);
	}
	
	
	/**
	 * Writes all supported formats
	 * @param layerElt
	 * @return
	 */
	protected void writeFormatList(DOMHelper dom, Element descElt, CoverageDescription desc)
	{
		int numElts = desc.getFormatList().size();
		for (int i = 0; i < numElts; i++)
		{
			String format = desc.getFormatList().get(i);
			dom.setElementValue(descElt, "supportedFormats/+formats", format);
		}
		
		// native format
		String nativeFormat = desc.getNativeFormat();
		if (nativeFormat != null)
			dom.setAttributeValue(descElt, "supportedFormats/@nativeFormat", nativeFormat);
	}
	
	
	/**
	 * Writes all supported interpolation methods
	 * @param layerElt
	 * @return
	 */
	protected void writeInterpMethodList(DOMHelper dom, Element descElt, RangeField field)
	{
		// interpolation methods
		for (int k=0; k<field.getInterpolationMethods().size(); k++)
		{
			Element interpMethodElt = dom.addElement(descElt, "supportedInterpolations/+interpolationMethod");
			dom.setElementValue(interpMethodElt, field.getInterpolationMethods().get(k));
		}
		
		// default method
		String defaultMethod = field.getDefaultInterpolationMethod();
		if (defaultMethod != null)
			dom.setAttributeValue(descElt, "supportedInterpolations/@default", defaultMethod);
	}
}