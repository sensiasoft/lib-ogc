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
package org.vast.ows;

import org.vast.util.ResponsibleParty;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <p>
 * Base reader for all capabilities document based
 * on a pre-version of OWS common (here called version 0)
 * </p>
 *
 * @author Alex Robin
 * @date 27 nov. 07
 * */
public abstract class OWSCapabilitiesReaderV0 extends AbstractCapabilitiesReader
{

    @Override
    public OWSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
        return readOWSCapabilities(dom, capabilitiesElt, new OWSServiceCapabilities());
    }
    
    
    protected OWSServiceCapabilities readOWSCapabilities(DOMHelper dom, Element capabilitiesElt, OWSServiceCapabilities serviceCaps) throws OWSException
    {
		// Version
        String version = dom.getAttributeValue(capabilitiesElt, "version");
        serviceCaps.setVersion(version);
        
        // Update Sequence
        String updateSequence = dom.getAttributeValue(capabilitiesElt, "updateSequence");
        serviceCaps.setUpdateSequence(updateSequence);
        
        // Read basic identification
        Element serviceElt = dom.getElement(capabilitiesElt, "Service");
        readIdentification(serviceCaps.getIdentification(), dom, serviceElt);
                
        // fees and access constraints
        String fees = dom.getElementValue(serviceElt, "fees");
        serviceCaps.setFees(fees);
        String constraints = dom.getElementValue(serviceElt, "accessConstraints");
        serviceCaps.setAccessConstraints(constraints);
        
        // responsible party
        Element respPartyElt = dom.getElement(serviceElt, "responsibleParty");
        readResponsibleParty(dom, respPartyElt, serviceCaps);
        
        // Server URLS
        readOperationsMetadata(dom, capabilitiesElt, serviceCaps);
        
        // Contents section
        readContents(dom, capabilitiesElt, serviceCaps);
        
        return serviceCaps;
	}
	
	
	@Override
	protected void readOperationsMetadata(DOMHelper dom, Element capabilitiesElt, OWSServiceCapabilities serviceCaps)
	{
		NodeList opElts = dom.getElements(capabilitiesElt, "Capability/Request/*");
		int numElts = opElts.getLength();
		for (int i = 0; i < numElts; i++)
		{
			Element opElt = (Element) opElts.item(i);
			String opName = opElt.getLocalName();
			String getUrl = dom.getAttributeValue(opElt, "DCPType/HTTP/Get/OnlineResource/@href");
			String postUrl = dom.getAttributeValue(opElt, "DCPType/HTTP/Post/OnlineResource/@href");
			
			if (getUrl != null)
				serviceCaps.getGetServers().put(opName, getUrl);
			
			if (postUrl != null)
				serviceCaps.getPostServers().put(opName, postUrl);
		}
	}
	
	
	/**
	 * Reads a keyword list
	 */
	protected void readIdentification(OWSIdentification idObject, DOMHelper dom, Element parentElt)
	{
		String desc = dom.getElementValue(parentElt, "description");
        idObject.setDescription(desc);
        
        String name = dom.getElementValue(parentElt, "name");
        idObject.setIdentifier(name);
        
        String serviceTitle = dom.getElementValue(parentElt, "label");
		idObject.setTitle(serviceTitle);
		
		NodeList keywordElts = dom.getElements(parentElt, "keywords/keyword");
		int numElts = keywordElts.getLength();

		for (int i = 0; i < numElts; i++)
		{
			Element keywordElt = (Element) keywordElts.item(i);
			String keyword = dom.getElementValue(keywordElt);
			idObject.getKeywords().add(keyword);
		}
	}

	
	/**
	 * Reads the responsibleParty section
	 */
	protected void readResponsibleParty(DOMHelper dom, Element respPartyElt, OWSServiceCapabilities serviceCaps)
	{
		ResponsibleParty provider = serviceCaps.getServiceProvider();
		String text;
		
		text = dom.getElementValue(respPartyElt, "individualName");
		provider.setIndividualName(text);
		text = dom.getElementValue(respPartyElt, "organisationName");
		provider.setOrganizationName(text);
		text = dom.getElementValue(respPartyElt, "positionName");
		provider.setPositionName(text);
		
		Element contactInfoElt = dom.getElement(respPartyElt, "contactInfo");
		text = dom.getElementValue(contactInfoElt, "phone/voice");
		provider.setVoiceNumber(text);
		text = dom.getElementValue(contactInfoElt, "phone/facsimile");
		provider.setFaxNumber(text);
		
		Element addressElt = dom.getElement(contactInfoElt, "address");
		text = dom.getElementValue(addressElt, "deliveryPoint");
		provider.setDeliveryPoint(text);
		text = dom.getElementValue(addressElt, "city");
		provider.setCity(text);
		text = dom.getElementValue(addressElt, "administrativeArea");
		provider.setAdministrativeArea(text);
		text = dom.getElementValue(addressElt, "postalCode");
		provider.setPostalCode(text);
		text = dom.getElementValue(addressElt, "country");
		provider.setCountry(text);
		text = dom.getElementValue(addressElt, "electronicMailAddress");
		provider.setEmail(text);
		
		text = dom.getAttributeValue(respPartyElt, "onlineResource/@href");
		provider.setWebsite(text);
	}
}
