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
 * Base reader for all capabilities document based on OWS common 1.1
 * </p>
 *
 * @author Alex Robin
 * @since 27 nov. 07
 * */
public abstract class OWSCapabilitiesReaderV11 extends AbstractCapabilitiesReader
{
	protected OWSCommonReaderV11 owsReader = new OWSCommonReaderV11();
	
	
	@Override
    public OWSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException
    {
        try
        {
            return readOWSCapabilities(dom, capabilitiesElt, new OWSServiceCapabilities());
        }
        catch (Exception e)
        {
            throw new OWSException(xmlError, e);
        }
    }
	
	
	protected OWSServiceCapabilities readOWSCapabilities(DOMHelper dom, Element capabilitiesElt, OWSServiceCapabilities serviceCaps) throws OWSException
	{
	    // version
        String version = dom.getAttributeValue(capabilitiesElt, "version");
        serviceCaps.setVersion(version);
        
        // update sequence
        String updateSequence = dom.getAttributeValue(capabilitiesElt, "updateSequence");
        serviceCaps.setUpdateSequence(updateSequence);
        
        // service identification section
        Element serviceIdElt = dom.getElement(capabilitiesElt, "ServiceIdentification");
        if (serviceIdElt != null)
            owsReader.readIdentification(dom, serviceIdElt, serviceCaps.getIdentification());
        else
            serviceCaps.setIdentification(null);
                
        // service type
        String serviceType = dom.getElementValue(serviceIdElt, "ServiceType");
        serviceCaps.setService(serviceType.replaceAll("OGC:", ""));
        
        // supported service versions
        NodeList versionElts = dom.getElements(serviceIdElt, "ServiceTypeVersion");
        int numElts = versionElts.getLength();
        for (int i = 0; i < numElts; i++)
        {
            Element versionElt = (Element) versionElts.item(i);
            String supportedVersion = dom.getElementValue(versionElt);
            serviceCaps.getSupportedVersions().add(supportedVersion);
        }
        
        // profiles
        NodeList profileElts = dom.getElements(serviceIdElt, "Profile");
        for (int i = 0; i < profileElts.getLength(); i++)
        {
            Element profileElt = (Element) profileElts.item(i);
            String profileURI = dom.getElementValue(profileElt);
            serviceCaps.getProfiles().add(profileURI);
        }
        
        // fees and access constraints
        String fees = dom.getElementValue(serviceIdElt, "Fees");
        serviceCaps.setFees(fees);
        String constraints = dom.getElementValue(serviceIdElt, "AccessConstraints");
        serviceCaps.setAccessConstraints(constraints);
        
        // service provider
        Element providerElt = dom.getElement(capabilitiesElt, "ServiceProvider");
        if (providerElt != null)
            readServiceProvider(dom, providerElt, serviceCaps);
        else
            serviceCaps.setServiceProvider(null);
        
        // server URLS
        readOperationsMetadata(dom, capabilitiesElt, serviceCaps);
        
        // contents section
        readContents(dom, capabilitiesElt, serviceCaps);
        
        return serviceCaps;
	}
	
	
	@Override
	protected void readOperationsMetadata(DOMHelper dom, Element capabilitiesElt, OWSServiceCapabilities serviceCaps)
	{
		NodeList opElts = dom.getElements(capabilitiesElt, "OperationsMetadata/Operation");
		int numElts = opElts.getLength();
		for (int i = 0; i < numElts; i++)
		{
			Element opElt = (Element) opElts.item(i);
			String opName = dom.getAttributeValue(opElt, "@name");
			String getUrl = dom.getAttributeValue(opElt, "DCP/HTTP/Get/@href");
			String postUrl = dom.getAttributeValue(opElt, "DCP/HTTP/Post/@href");
			
			if (getUrl != null)
				serviceCaps.getGetServers().put(opName, getUrl);
			
			if (postUrl != null)
				serviceCaps.getPostServers().put(opName, postUrl);
		}
	}

	
	protected void readServiceProvider(DOMHelper dom, Element providerElt, OWSServiceCapabilities serviceCaps)
	{
		ResponsibleParty provider = serviceCaps.getServiceProvider();
		String text;
		
		text = dom.getElementValue(providerElt, "ProviderName");
		provider.setOrganizationName(text);
		
		text = dom.getAttributeValue(providerElt, "ProviderSite/@href");
		provider.setWebsite(text);
		
		Element contactElt = dom.getElement(providerElt, "ServiceContact");		
		text = dom.getElementValue(contactElt, "IndividualName");
		provider.setIndividualName(text);		
		text = dom.getElementValue(contactElt, "PositionName");
		provider.setPositionName(text);		
		text = dom.getElementValue(contactElt, "Role");
		provider.setRole(text);
		
		Element contactInfoElt = dom.getElement(contactElt, "ContactInfo");
		if (contactInfoElt != null)
		{
    		text = dom.getElementValue(contactInfoElt, "Phone/Voice");
    		provider.setVoiceNumber(text);		
    		text = dom.getElementValue(contactInfoElt, "Phone/Facsimile");
    		provider.setFaxNumber(text);		
    		text = dom.getElementValue(contactInfoElt, "HoursOfService");
    		provider.setHoursOfService(text);		
    		text = dom.getElementValue(contactInfoElt, "ContactInstructions");
    		provider.setContactInstructions(text);  
    		
    		Element addressElt = dom.getElement(contactInfoElt, "Address");
    		if (addressElt != null)
    		{
        		text = dom.getElementValue(addressElt, "DeliveryPoint");
        		provider.setDeliveryPoint(text);		
        		text = dom.getElementValue(addressElt, "City");
        		provider.setCity(text);
        		text = dom.getElementValue(addressElt, "AdministrativeArea");
        		provider.setAdministrativeArea(text);
        		text = dom.getElementValue(addressElt, "PostalCode");
        		provider.setPostalCode(text);
        		text = dom.getElementValue(addressElt, "Country");
        		provider.setCountry(text);
        		text = dom.getElementValue(addressElt, "ElectronicMailAddress");
        		provider.setEmail(text);
    		}
		}
	}
}
