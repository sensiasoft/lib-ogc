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

import org.vast.ows.AbstractResponseReader;
import org.vast.ows.OWSCommonReaderV11;
import org.vast.ows.OWSException;
import org.w3c.dom.*;
import org.vast.util.Bbox;
import org.vast.xml.DOMHelper;


/**
 * <p>
 * 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 27 nov. 07
 * */
public class CoverageDescriptionsReaderV11 extends AbstractResponseReader<CoverageDescriptions>
{
	protected WCSCommonReaderV11 wcsReader = new WCSCommonReaderV11();
	protected OWSCommonReaderV11 owsReader = new OWSCommonReaderV11();
	
	
	public CoverageDescriptions readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		CoverageDescriptions response = new CoverageDescriptions();
		response.setVersion("1.1.1");
		
		// try to read one description at the root first
		if (responseElt.getLocalName().equals("CoverageDescription"))
		{
			CoverageDescription desc = readDescription(dom, responseElt);
			response.getDescriptions().add(desc);
		}
		else
		{
			// otherwise loop through collection of descriptions
			NodeList descElts = dom.getElements(responseElt, "CoverageDescription");		
			for (int i=0; i<descElts.getLength(); i++)
			{ 
				Element descElt = (Element)descElts.item(i);
				CoverageDescription desc = readDescription(dom, descElt);
				response.getDescriptions().add(desc);
			}
		}
		
		return response;
	}
	
	
	protected CoverageDescription readDescription(DOMHelper dom, Element descElt) throws OWSException
	{
		CoverageDescription desc = new CoverageDescription();
				
		// title, abstract, identifier elts
		owsReader.readIdentification(dom, descElt, desc);
		
		// spatial domain
		Element spDomainElt = dom.getElement(descElt, "Domain/SpatialDomain");
					
		// bbox list
		// TODO handle WGS84BoundingBox allowed here by substitution!
		NodeList bboxElts = dom.getElements(spDomainElt, "BoundingBox");		
		for (int j=0; j<bboxElts.getLength(); j++)
		{
			Element bboxElt = (Element)bboxElts.item(j);
			Bbox bbox = owsReader.readBbox(dom, bboxElt);
			desc.getBboxList().add(bbox);
		}
		
		// grid crs
		Element gridElt = dom.getElement(spDomainElt, "GridCRS");
		if (gridElt != null)
		{
			WCSRectifiedGridCrs gridCrs = new WCSRectifiedGridCrs();
			wcsReader.readGridCRS(dom, gridElt, gridCrs);
		}
		
		// TODO read Transformation -> link to SensorML
		
		// TODO read time domain
		//Element timeDomainElt = dom.getElement(descElt, "Domain/TemporalDomain");
		
		// range definition
		NodeList fieldElts = dom.getElements(descElt, "Range/Field");
		for (int j=0; j<fieldElts.getLength(); j++)
		{
			Element fieldElt = (Element)fieldElts.item(j);				
			RangeField field = new RangeField();
			
			// title, abstract, keywords, identifier elts
			owsReader.readIdentification(dom, fieldElt, field);
			
			// TODO read definition
			
			// try to read one numerical null value
			try
			{
				String nullValue = dom.getElementValue(fieldElt, "NullValue");
				if (nullValue != null)
					field.setNullValue(Double.parseDouble(nullValue));
			}
			catch (NumberFormatException e)
			{
			}
			
			// interpolation methods
			NodeList interpElts = dom.getElements(fieldElt, "InterpolationMethods/InterpolationMethod");
			for (int k=0; k<interpElts.getLength(); k++)
			{
				Element interpElt = (Element)interpElts.item(k);
				String methodName = dom.getElementValue(interpElt);
				field.getInterpolationMethods().add(methodName);
				
				// TODO read nullResilience
			}
			
			// set default method
			String defaultMethod = dom.getElementValue(fieldElt, "InterpolationMethods/Default");
			field.setDefaultInterpolationMethod(defaultMethod);
			
			// all field axes
			NodeList axisElts = dom.getElements(fieldElt, "Axis");
			for (int k=0; k<axisElts.getLength(); k++)
			{
				Element axisElt = (Element)axisElts.item(k);
				RangeAxis axis = new RangeAxis();
				
				// title, abstract elts
				owsReader.readDescription(dom, axisElt, axis);
				
				// identifier as attribute!
				String id = dom.getAttributeValue(axisElt, "@identifier");
				axis.setIdentifier(id);
				
				// keys
				NodeList keyElts = dom.getElements(axisElt, "AvailableKeys/Key");
				for (int h=0; h<keyElts.getLength(); h++)
				{
					Element keyElt = (Element)keyElts.item(h);
					String key = dom.getElementValue(keyElt);
					axis.getKeys().add(key);
				}
				
				// TODO read meaning
				// TODO read data type
				// TODO read unit
				
				field.getAxisList().add(axis);
			}
			
			desc.getRangeFields().add(field);
		}
		
		// supported CRSs and formats
		readCRSList(desc, dom, descElt);
		readFormatList(desc, dom, descElt);
		
		return desc;
	}
	
	
	/**
	 * Reads all supported format for given layer
	 * @param layerElt
	 * @return
	 */
	protected void readFormatList(CoverageDescription desc, DOMHelper dom, Element descElt)
	{
		NodeList formatElts = dom.getElements(descElt, "SupportedFormat");
		int numElts = formatElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element formatElt = (Element) formatElts.item(i);
			String formatName = dom.getElementValue(formatElt);
			desc.getFormatList().add(formatName);
		}
	}


	/**
	 * Reads all supported CRS for given layer
	 * @param layerElt
	 * @return
	 */
	protected void readCRSList(CoverageDescription desc, DOMHelper dom, Element descElt)
	{
		NodeList crsElts = dom.getElements(descElt, "SupportedCRS");
		int numElts = crsElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element crsElt = (Element) crsElts.item(i);
			String crsName = dom.getElementValue(crsElt);
			desc.getCrsList().add(crsName);
		}
	}
}