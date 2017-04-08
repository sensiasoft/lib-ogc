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
 * Base writer for all service capabilities based on the
 * pre-version of OWS common (here called v0)
 * </p>
 *
 * @author Alex Robin
 * @date 27 nov. 07
 * */
public abstract class OWSCapabilitiesWriterV0 extends AbstractResponseWriter<OWSServiceCapabilities>
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
		dom.setAttributeValue(capsElt, "version", version);
		
		// updateSequence attribute
		text = caps.getUpdateSequence();
		if (text != null)
			dom.setAttributeValue(capsElt, "updateSequence", text);
	}
	
	
	/**
	 * Writes the whole ServiceIdentification section
	 * @param dom
	 * @param capsElt
	 * @param caps
	 */
	protected void writeService(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps)
	{
		Element serviceElt = dom.addElement(capsElt, "Service");
		
		writeIdentification(dom, serviceElt, caps.getIdentification());				
		writeKeywords(dom, serviceElt, caps.getIdentification());
		writeResponsibleParty(dom, serviceElt, caps.getServiceProvider());
		
		// fees
		String text = caps.getFees();
		if (text != null)
			dom.setElementValue(serviceElt, "fees", text);
		
		// access constraints
		text = caps.getAccessConstraints();
		if (text != null)
			dom.setElementValue(serviceElt, "accessConstraints", text);
	}
	
	
	/**
	 * Append elements defined in ows:DescriptionType to parent element
	 * @param dom
	 * @param parentElt
	 * @param identification
	 */
	public static void writeIdentification(DOMHelper dom, Element parentElt, OWSIdentification identification)
	{
		String text;
		
		if (identification == null)
			return;
		
		text = identification.getDescription();
		if (text != null)
			dom.setElementValue(parentElt, "description", text);
		
		text = identification.getIdentifier();
		if (text != null)
			dom.setElementValue(parentElt, "name", text);
		
		text = identification.getTitle();
		if (text != null)
			dom.setElementValue(parentElt, "label", text);
	}
	
	
	/**
	 * Append keyword list to parent parent element
	 * @param dom
	 * @param parentElt
	 * @param identification
	 */
	public static void writeKeywords(DOMHelper dom, Element parentElt, OWSIdentification identification)
	{
		// write all keywords
		if (!identification.getKeywords().isEmpty())
		{
			Element keywordsElt = dom.addElement(parentElt, "keywords");
			int numElts = identification.getKeywords().size();
			for (int i = 0; i < numElts; i++)
			{
				String keyword = identification.getKeywords().get(i);
				dom.setElementValue(keywordsElt, "+keyword", keyword);
			}
		}
	}

	
	/**
	 * Writes the whole ServiceProvider section
	 * @param dom
	 * @param capsElt
	 * @param provider
	 */
	public static void writeResponsibleParty(DOMHelper dom, Element capsElt, ResponsibleParty provider)
	{
		String text;
		
		if (provider == null)
			return;
		
		// ServiceProvider element
		Element respPartyElt = dom.addElement(capsElt, "responsibleParty");
		text = provider.getIndividualName();
		if (text != null)
			dom.setElementValue(respPartyElt, "individualName", text);
		text = provider.getOrganizationName();
		if (text != null)
			dom.setElementValue(respPartyElt, "organisationName", text);
		text = provider.getPositionName();
		if (text != null)
			dom.setElementValue(respPartyElt, "positionName", text);
				
		// Contact Info element and children
		if (provider.hasContactInfo())
		{
    		Element contactInfoElt = dom.addElement(respPartyElt, "contactInfo");
    		text = provider.getVoiceNumber();
    		if (text != null)
    			dom.setElementValue(contactInfoElt, "phone/voice", text);
    		text = provider.getFaxNumber();
    		if (text != null)
    			dom.setElementValue(contactInfoElt, "phone/facsimile", text);
    		
    		if (provider.hasAddress())
    		{
        		// Address element and children	
        		Element addressElt = dom.addElement(contactInfoElt, "address");
        		text = provider.getDeliveryPoint();		
        		if (text != null)
        			dom.setElementValue(addressElt, "deliveryPoint", text);
        		text = provider.getCity();
        		if (text != null)
        			dom.setElementValue(addressElt, "city", text);
        		text = provider.getAdministrativeArea();
        		if (text != null)
        			dom.setElementValue(addressElt, "administrativeArea", text);
        		text = provider.getPostalCode();
        		if (text != null)
        			dom.setElementValue(addressElt, "postalCode", text);
        		text = provider.getCountry();
        		if (text != null)
        			dom.setElementValue(addressElt, "country", text);
        		text = provider.getEmail();
        		if (text != null)
        			dom.setElementValue(addressElt, "electronicMailAddress", text);
    		}
		}
	}
	
	
	/**
	 * Writes the whole OperationsMetadata section
	 * @param dom
	 * @param capsElt
	 * @param caps
	 */
	protected void writeCapability(DOMHelper dom, Element capsElt, OWSServiceCapabilities caps)
	{
		if (caps.getGetServers().isEmpty() && caps.getPostServers().isEmpty())
			return;
		
		// OperationsMetadata element
		Element requestElt = dom.addElement(capsElt, "Capability/Request");
		
		// first add everything in GetServer table
		for (String opName: caps.getGetServers().keySet())
        {
			String opUrl =  caps.getGetServers().get(opName);
			Element opElt = dom.addElement(requestElt, opName);
			dom.setAttributeValue(opElt, "DCPType/HTTP/Get/OnlineResource/@xlink:href", opUrl);
			
			// also add POST url if found
			String postUrl = caps.getPostServers().get(opName);
			if (postUrl != null)
				dom.setAttributeValue(opElt, "DCPType/HTTP/Post/OnlineResource/@xlink:href", postUrl);
		}
		
		// now loop through all PostServers + add only the ones with only POST support
		for (String opName: caps.getPostServers().keySet())
        {
			String opUrl =  caps.getPostServers().get(opName);
			if (caps.getGetServers().get(opName) != null)
				continue;
			
			Element opElt = dom.addElement(requestElt, opName);
			dom.setAttributeValue(opElt, "DCPType/HTTP/Post/OnlineResource/@xlink:href", opUrl);
		}
		
	}
}
