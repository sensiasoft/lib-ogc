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


/**
 * <p>
 * Base writer for all service capabilities based on OWS common 1.1
 * </p>
 *
 * @author Alex Robin
 * @since 27 nov. 07
 * */
public abstract class OWSCapabilitiesWriterV11 extends AbstractResponseWriter<OWSServiceCapabilities>
{
    
	protected abstract void writeContents(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps, String version) throws OWSException; 
	
	
	/**
	 * Writes version and updateSequence attributes on root Capabilities elt
	 * @param dom
	 * @param capsElt
	 * @param caps
	 */
	protected void writeRootAttributes(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps, String version)
	{
		String text;
		
		// mandatory version attribute
		dom.setAttributeValue(capsElt, "version", normalizeVersionString(version));
		
		// updateSequence attribute
		text = caps.getUpdateSequence();
		if (text != null)
			dom.setAttributeValue(capsElt, "updateSequence", text);
	}
	
	
	protected String normalizeVersionString(String version)
	{
	    // make sure version number always has 3 parts
	    while (version.split("\\.").length < 3)
	        version += ".0";
	    
	    return version;
	}
	
	
	/**
	 * Writes the whole ServiceIndetification section
	 * @param dom
	 * @param capsElt
	 * @param caps
	 */
	protected void writeServiceIdentification(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps)
	{
		if (caps.getIdentification() == null)
		    return;
	    
	    Element idElt = dom.addElement(capsElt, "ows:ServiceIdentification");
		writeIdentification(dom, idElt, caps.getIdentification());
		
		// mandatory service type
		dom.setElementValue(idElt, "ows:ServiceType", "OGC:" + caps.getService());
		
		// at least one supported version
		for (String version: caps.getSupportedVersions())
			dom.setElementValue(idElt, "+ows:ServiceTypeVersion", version);
		
		// profiles
		for (String profile: caps.getProfiles())
		    dom.setElementValue(idElt, "+ows:Profile",profile);
		
		// fees
		String text = caps.getFees();
		if (text != null)
			dom.setElementValue(idElt, "ows:Fees", text);
		
		// access constraints
		text = caps.getAccessConstraints();
		if (text != null)
			dom.setElementValue(idElt, "ows:AccessConstraints", text);
	}
	
	
	/**
	 * Append elements defined in ows:DescriptionType to parentElt
	 * @param dom
	 * @param parentElt
	 * @param identification
	 */
	protected void writeIdentification(DOMHelper dom, Element parentElt, OWSIdentification identification)
	{
		String text;
		
		if (identification == null)
			return;
		
		text = identification.getTitle();
		if (text != null)
			dom.setElementValue(parentElt, "ows:Title", text);
		
		text = identification.getDescription();
		if (text != null)
			dom.setElementValue(parentElt, "ows:Abstract", text);
		
		// write all keywords
		if (!identification.getKeywords().isEmpty())
		{
			Element keywordsElt = dom.addElement(parentElt, "ows:Keywords");
			int numElts = identification.getKeywords().size();
			for (int i = 0; i < numElts; i++)
			{
				String keyword = identification.getKeywords().get(i);
				dom.setElementValue(keywordsElt, "+ows:Keyword", keyword);
			}
		}
	}

	
	/**
	 * Writes the whole ServiceProvider section
	 * @param dom
	 * @param capsElt
	 * @param provider
	 */
	protected void writeServiceProvider(DOMHelper dom, Element capsElt, ResponsibleParty provider)
	{
		String text;
		
		if (provider == null)
			return;
		
		// ServiceProvider element
		Element providerElt = dom.addElement(capsElt, "ows:ServiceProvider");

		text = provider.getOrganizationName();
		dom.setElementValue(providerElt, "ows:ProviderName", text);
	
		text = provider.getWebsite();
		if (text != null)
			dom.setAttributeValue(providerElt, "ows:ProviderSite/@xlink:href", text);
		
		// ServiceContact element
		Element contactElt = dom.addElement(providerElt, "ows:ServiceContact");
		text = provider.getIndividualName();
		if (text != null)
			dom.setElementValue(contactElt, "ows:IndividualName", text);
		text = provider.getPositionName();
		if (text != null)
			dom.setElementValue(contactElt, "ows:PositionName", text);
				
		// Contact Info element and children
		if (provider.hasContactInfo())
		{
    		Element contactInfoElt = dom.addElement(contactElt, "ows:ContactInfo");
    		text = provider.getVoiceNumber();
    		if (text != null)
    			dom.setElementValue(contactInfoElt, "ows:Phone/ows:Voice", text);
    		text = provider.getFaxNumber();
    		if (text != null)
    			dom.setElementValue(contactInfoElt, "ows:Phone/ows:Facsimile", text);
    		
    		// Address element and children
    		if (provider.hasAddress())
    		{
        		Element addressElt = dom.addElement(contactInfoElt, "ows:Address");
        		text = provider.getDeliveryPoint();		
        		if (text != null)
        			dom.setElementValue(addressElt, "ows:DeliveryPoint", text);
        		text = provider.getCity();
        		if (text != null)
        			dom.setElementValue(addressElt, "ows:City", text);
        		text = provider.getAdministrativeArea();
        		if (text != null)
        			dom.setElementValue(addressElt, "ows:AdministrativeArea", text);
        		text = provider.getPostalCode();
        		if (text != null)
        			dom.setElementValue(addressElt, "ows:PostalCode", text);
        		text = provider.getCountry();
        		if (text != null)
        			dom.setElementValue(addressElt, "ows:Country", text);
        		text = provider.getEmail();
        		if (text != null)
        			dom.setElementValue(addressElt, "ows:ElectronicMailAddress", text);
    		}

    		text = provider.getHoursOfService();
    		if (text != null)
    			dom.setElementValue(contactInfoElt, "ows:HoursOfService", text);
    		text = provider.getContactInstructions();
    		if (text != null)
    			dom.setElementValue(contactInfoElt, "ows:ContactInstructions", text);
		}
		
		// end of ServiceContact		
		text = provider.getRole();
		if (text != null)
			dom.setElementValue(contactElt, "ows:Role", text);
	}
	
	
	/**
	 * Writes the whole OperationsMetadata section
	 * @param dom
	 * @param capsElt
	 * @param caps
	 */
	protected void writeOperationsMetadata(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps)
	{
		if (caps.getGetServers().isEmpty() && caps.getPostServers().isEmpty())
			return;
		
		// OperationsMetadata element
		Element opsElt = dom.addElement(capsElt, "ows:OperationsMetadata");
		
		// first add everything in GetServer table
		for (String opName: caps.getGetServers().keySet())
		{
			String opUrl =  caps.getGetServers().get(opName);
			Element opElt = dom.addElement(opsElt, "+ows:Operation");
			dom.setAttributeValue(opElt, "@name", opName);
			dom.setAttributeValue(opElt, "ows:DCP/ows:HTTP/ows:Get/@xlink:href", opUrl);
			
			// also add POST url if found
			String postUrl = caps.getPostServers().get(opName);
			if (postUrl != null)
				dom.setAttributeValue(opElt, "ows:DCP/ows:HTTP/ows:Post/@xlink:href", postUrl);
		}
		
		// now loop through all PostServers + add only the ones with only POST support
		for (String opName: caps.getPostServers().keySet())
        {
			String opUrl =  caps.getPostServers().get(opName);
			if (caps.getGetServers().get(opName) != null)
				continue;
			
			Element opElt = dom.addElement(opsElt, "+ows:Operation");
			dom.setAttributeValue(opElt, "@name", opName);
			dom.setAttributeValue(opElt, "ows:DCP/ows:HTTP/ows:Post/@xlink:href", opUrl);
		}
		
	}
}
