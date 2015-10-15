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
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSCommonWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSUtils;
import org.w3c.dom.*;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 23 nov. 07
 * */
public class CoverageDescriptionsWriterV11 extends AbstractResponseWriter<CoverageDescriptions>
{
	protected WCSCommonWriterV11 wcsWriter = new WCSCommonWriterV11();
	protected OWSCommonWriterV11 owsWriter = new OWSCommonWriterV11();
	
	
	public Element buildXMLResponse(DOMHelper dom, CoverageDescriptions response, String version) throws OWSException
	{
		// setup ns and create root elt
		dom.addUserPrefix(DOMHelper.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OWSUtils.WCS, version));
		Element rootElt = dom.createElement("CoverageDescriptions");

		// add all coverage descriptions
		List<CoverageDescription> descriptions = response.getDescriptions();
		for (int i=0; i<descriptions.size(); i++)
		{
			CoverageDescription desc = descriptions.get(i);
			Element descElt = dom.addElement(rootElt, "+CoverageDescription");
			
			// title, abstract, keywords elts
			owsWriter.buildDescription(dom, descElt, desc);
			
			// identifier
			dom.setElementValue(descElt, "Identifier", desc.getIdentifier());
						
			// spatial domain
			Element spDomainElt = dom.addElement(descElt, "Domain/SpatialDomain");
			
			// bbox list
			List<Bbox> bboxList = desc.getBboxList();
			for (int j=0; j<bboxList.size(); j++)
			{
				Element bboxElt = owsWriter.buildBbox(dom, bboxList.get(j));
				spDomainElt.appendChild(bboxElt);
			}
			
			// grid CRS
			WCSRectifiedGridCrs gridCrs = desc.getGridCrs();
			if (gridCrs != null)
			{
				String id = desc.getIdentifier() + "_GRID_CRS";
				Element gridCrsElt = wcsWriter.buildGridCRS(dom, gridCrs, id);
				spDomainElt.appendChild(gridCrsElt);
				
			}
			
			// TODO write Transformation -> link to SensorML?
			// TODO write image CRS if unrectified
			// TODO write polygon list
			
			// TODO write time domain
			//Element timeDomainElt = dom.addElement(descElt, "Domain/TemporalDomain");
						
			// range fields
			for (int j=0; j<desc.getRangeFields().size(); j++)
			{
				RangeField field = desc.getRangeFields().get(j);
				Element fieldElt = dom.addElement(descElt, "Range/Field");
				
				// title, abstract, keywords
				owsWriter.buildDescription(dom, fieldElt, field);
				
				// identifier
				dom.setElementValue(fieldElt, "Identifier", field.getIdentifier());
				
				// TODO write complete definition
				dom.addElement(fieldElt, "Definition/ows:AnyValue");
				
				// null value
				String nullValue = field.getNullValue().toString();
				dom.setElementValue(fieldElt, "NullValue", nullValue);
				
				// interpolation methods
				for (int k=0; k<field.getInterpolationMethods().size(); k++)
				{
					Element interpMethodElt = dom.addElement(fieldElt, "InterpolationMethods/+InterpolationMethod");
					String interpMethod = field.getInterpolationMethods().get(k);
					
					// hack to handle differente value properly (change btw 1.0 and 1.1)
					if (interpMethod.startsWith("nearest"))
						interpMethod = "nearest";
					
					dom.setElementValue(interpMethodElt, interpMethod);
					
					// TODO write nullResilience
				}
				
				// default method
				String defaultMethod = field.getDefaultInterpolationMethod();
				if (defaultMethod != null)
					dom.setElementValue(fieldElt, "InterpolationMethods/Default", defaultMethod);
				
				// all field axes
				for (int k=0; k<field.getAxisList().size(); k++)
				{
					RangeAxis axis = field.getAxisList().get(k);
					Element axisElt = dom.addElement(fieldElt, "Axis");
					
					// title, abstract
					owsWriter.buildDescription(dom, axisElt, axis);
					
					// identifier attribute!
					dom.setAttributeValue(axisElt, "@identifier", axis.getIdentifier());
					
					// keys
					List<String> keys = axis.getKeys();
					for (int h=0; h<keys.size(); h++)
					{
						String key = keys.get(h);
						dom.setElementValue(axisElt, "AvailableKeys/+Key", key);
					}
					
					// TODO write meaning
					// TODO write data type
					// TODO write unit
				}
			}
			
			// supported CRS and formats
			writeCRSList(dom, descElt, desc);
			writeFormatList(dom, descElt, desc);
		}
				
		return rootElt;
	}
	
	
	/**
	 * Writes all supported CRSs
	 * @param layerElt
	 * @return
	 */
	protected void writeCRSList(DOMHelper dom, Element layerElt, CoverageDescription desc)
	{
		int numElts = desc.getCrsList().size();
		for (int i = 0; i < numElts; i++)
		{
			String crs = desc.getCrsList().get(i);
			dom.setElementValue(layerElt, "+SupportedCRS", crs);
		}
	}
	
	
	/**
	 * Writes all supported formats
	 * @param layerElt
	 * @return
	 */
	protected void writeFormatList(DOMHelper dom, Element layerElt, CoverageDescription desc)
	{
		int numElts = desc.getFormatList().size();
		for (int i = 0; i < numElts; i++)
		{
			String format = desc.getFormatList().get(i);
			dom.setElementValue(layerElt, "+SupportedFormat", format);
		}
	}
}