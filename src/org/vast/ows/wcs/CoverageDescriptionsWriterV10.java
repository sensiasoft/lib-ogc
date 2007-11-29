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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.util.List;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractResponseWriter;
import org.vast.ows.OWSCommonWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.util.Bbox;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;


/**
 * <p><b>Title:</b><br/>
 * Coverage Descriptions Writer V11
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
	protected OWSCommonWriterV11 owsWriter = new OWSCommonWriterV11();
	
	
	public Element buildXMLResponse(DOMHelper dom, CoverageDescriptions response, String version) throws OWSException
	{
		// setup ns and create root elt
		dom.addUserPrefix(QName.DEFAULT_PREFIX, OGCRegistry.getNamespaceURI(OGCRegistry.WCS, version));
		Element rootElt = dom.createElement("CoverageDescriptions");

		// add all coverage descriptions
		List<CoverageDescription> descriptions = response.getDescriptions();
		for (int i=0; i<descriptions.size(); i++)
		{
			CoverageDescription desc = descriptions.get(i);
			Element descElt = dom.addElement(rootElt, "CoverageDescription");
			
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
			
			// TODO write grid CRS
			// TODO write Transformation -> link to SensorML?
			
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
				
				// TODO read definition
				// TODO read null value
				
				// interpolation methods
				for (int k=0; k<field.getInterpolationMethods().size(); k++)
				{
					Element interpMethodElt = dom.addElement(fieldElt, "InterpolationMethods/+InterpolationMethod");
					dom.setElementValue(interpMethodElt, field.getInterpolationMethods().get(k));
					
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
					Element axisElt = dom.addElement(descElt, "Axis/Field");
					
					// title, abstract
					owsWriter.buildDescription(dom, axisElt, axis);
					
					// identifier attribute!
					dom.setAttributeValue(axisElt, "@identifier", axis.getIdentifier());
					
					// keys
					List<String> keys = axis.getKeys();
					for (int h=0; h<keys.size(); h++)
					{
						String key = keys.get(h);
						dom.setElementValue(fieldElt, "AvailableKeys/+Key", key);
					}
					
					// TODO write meaning
					// TODO write data type
					// TODO write unit
				}
			}
			
			
		}
				
		return rootElt;
	}
}