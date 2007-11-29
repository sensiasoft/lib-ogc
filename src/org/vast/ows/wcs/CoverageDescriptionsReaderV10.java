/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.ows.AbstractResponseReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSIdentification;
import org.vast.ows.gml.GMLEnvelopeReader;
import org.vast.ows.util.Bbox;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * Coverage Descriptions Reader V1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 27 nov. 07
 * @version 1.0
 */
public class CoverageDescriptionsReaderV10 extends AbstractResponseReader<CoverageDescriptions>
{
	protected GMLEnvelopeReader gmlReader = new GMLEnvelopeReader();


	public CoverageDescriptions readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		CoverageDescriptions response = new CoverageDescriptions();
		String version = dom.getAttributeValue(responseElt, "@version");
		response.setVersion(version);

		NodeList descElts = dom.getElements(responseElt, "CoverageOffering");
		for (int i = 0; i < descElts.getLength(); i++)
		{
			Element offeringElt = (Element) descElts.item(i);
			CoverageDescription desc = new CoverageDescription();

			// title, abstract, identifier elts
			readIdentification(dom, offeringElt, desc);

			// spatial domain
			Element spDomainElt = dom.getElement(offeringElt, "domainSet/spatialDomain");

			// envelope
			NodeList envElts = dom.getElements(spDomainElt, "Envelope");
			for (int j = 0; j < envElts.getLength(); j++)
			{
				Element envElt = (Element) envElts.item(j);
				Bbox bbox = gmlReader.readEnvelope(dom, envElt);
				desc.getBboxList().add(bbox);
			}

			// TODO read gml:Grids
			// TODO read gml:Polygons

			// TODO read time domain
			//Element timeDomainElt = dom.getElement(descElt, "domainSet/temporalDomain");

			// range set = single field in 1.0
			Element rangeSetElt = dom.getElement(offeringElt, "rangeSetRange/RangeSet");
			RangeField field = new RangeField();
			readIdentification(dom, rangeSetElt, field);

			// all axes
			NodeList axisElts = dom.getElements(rangeSetElt, "axisDescription/AxisDescription");
			for (int j = 0; j < axisElts.getLength(); j++)
			{
				Element axisElt = (Element) axisElts.item(j);
				RangeAxis axis = new RangeAxis();

				// title, abstract, identifier elts
				readIdentification(dom, axisElt, axis);

				// values
				NodeList keyElts = dom.getElements(axisElt, "values/singleValue");
				for (int h = 0; h < keyElts.getLength(); h++)
				{
					Element keyElt = (Element) keyElts.item(h);
					String key = dom.getElementValue(keyElt);
					axis.getKeys().add(key);
				}

				// TODO read data type
				// TODO read unit

				field.getAxisList().add(axis);
			}

			// supported formats
			NodeList formatElts = dom.getElements(offeringElt, "supportedFormats/formats");
			for (int j = 0; j < formatElts.getLength(); j++)
			{
				Element formatElt = (Element) formatElts.item(j);
				String formatName = dom.getElementValue(formatElt);
				desc.getFormatList().add(formatName);
			}
			
			// native format
			desc.setNativeFormat(dom.getElementValue(offeringElt, "supportedFormats/nativeFormat"));

			// supported crs
			NodeList crsElts = dom.getElements(offeringElt, "supportedCRSs/requestResponseCRSs");
			for (int j = 0; j < crsElts.getLength(); j++)
			{
				Element crsElt = (Element) crsElts.item(j);
				String crsName = dom.getElementValue(crsElt);
				desc.getCrsList().add(crsName);
			}
			
			// native crs
			desc.setNativeCrs(dom.getElementValue(offeringElt, "supportedCRSs/nativeCRSs"));

			// other interpolation methods
			NodeList interpElts = dom.getElements(offeringElt, "supportedInterpolations/interpolationMethod");
			for (int j = 0; j < interpElts.getLength(); j++)
			{
				Element interpElt = (Element) interpElts.item(j);
				String methodName = dom.getElementValue(interpElt);
				field.getInterpolationMethods().add(methodName);
			}

			// set default method
			String defaultMethod = dom.getAttributeValue(offeringElt, "supportedInterpolations/@default");
			field.setDefaultInterpolationMethod(defaultMethod);

			response.getDescriptions().add(desc);
		}
		
		return response;
	}


	public void readIdentification(DOMHelper dom, Element parentElt, OWSIdentification idObject)
	{
		// label
		String title = dom.getElementValue(parentElt, "label");
		idObject.setTitle(title);

		// description
		String description = dom.getElementValue(parentElt, "description");
		idObject.setDescription(description);

		// name
		String identifier = dom.getElementValue(parentElt, "name");
		idObject.setIdentifier(identifier);
	}

}