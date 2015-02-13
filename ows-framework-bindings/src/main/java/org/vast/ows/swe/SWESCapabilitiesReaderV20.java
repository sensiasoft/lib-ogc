/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.*;
import org.vast.xml.DOMHelper;
import org.vast.ogc.gml.GMLEnvelopeReader;
import org.vast.ogc.gml.GMLTimeReader;
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSException;


/**
 * <p>
 * Helper methods to read SWES v2.0 capabilities documents
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Dec 12, 2014
 */
public abstract class SWESCapabilitiesReaderV20 extends OWSCapabilitiesReaderV11
{
    GMLEnvelopeReader gmlEnvReader = new GMLEnvelopeReader();
    GMLTimeReader gmlTimeReader = new GMLTimeReader();
    
    
	public SWESCapabilitiesReaderV20()
	{
	}
	
	
	protected void readCommonOfferingProperties(DOMHelper dom, Element offeringElt, SWESOfferingCapabilities offering,
	        List<String> serviceProcFormats, List<String> serviceObsProperties, List<String> serviceRelFeatures) throws OWSException
    {
	    // identifier
        String id = dom.getElementValue(offeringElt, "identifier");
        offering.setIdentifier(id);
        
        // name
        String name = dom.getElementValue(offeringElt, "name");
        offering.setTitle(name);
        
        // description
        String description = dom.getElementValue(offeringElt, "description");
        offering.setDescription(description);
        
        // procedure
        String procedureID = dom.getElementValue(offeringElt, "procedure");
        offering.getProcedures().add(procedureID);
        
        // for the following items, we combine service level and offering level metadata            
        // procedure description formats
        offering.getProcedureFormats().addAll(serviceProcFormats);
        offering.getProcedureFormats().addAll(readProcedureFormats(dom, offeringElt));
        
        // observable properties
        offering.getObservableProperties().addAll(serviceObsProperties);
        offering.getObservableProperties().addAll(readObservableProperties(dom, offeringElt));
        
        // related features
        offering.getRelatedFeatures().addAll(serviceRelFeatures);
        offering.getRelatedFeatures().addAll(readRelatedFeatures(dom, offeringElt));
    }
	
	
	/*
	 * Reads content from a list of procedureDescriptionFormat elements
	 */
	protected List<String> readProcedureFormats(DOMHelper dom, Element parentElt)
	{
	    return readStringList(dom, parentElt, "procedureDescriptionFormat");
	}
	
	
	/*
     * Reads content from a list of observableProperty elements
     */
    protected List<String> readObservableProperties(DOMHelper dom, Element parentElt)
    {
        return readStringList(dom, parentElt, "observableProperty");
    }
    
    
    /*
     * Reads content from a list of relatedFeature elements
     */
    protected List<String> readRelatedFeatures(DOMHelper dom, Element parentElt)
    {
        List<String> featureList = new ArrayList<String>();
        
        NodeList featRelationshipElts = dom.getElements(parentElt, "relatedFeature/FeatureRelationship/target");
        for (int i=0; i<featRelationshipElts.getLength(); i++)
        {
            String href = dom.getAttributeValue((Element)featRelationshipElts.item(i), "@href");
            featureList.add(href);
        }
        
        return featureList;
    }
    
    
    /*
     * Reads string values from a list of identical elements
     */
    protected List<String> readStringList(DOMHelper dom, Element parentElt, String eltPath)
    {
        List<String> tokenList = new ArrayList<String>();
        NodeList elts = dom.getElements(parentElt, eltPath);
        for (int i=0; i<elts.getLength(); i++)
        {
            String token = dom.getElementValue((Element)elts.item(i));
            tokenList.add(token);
        }
        
        return tokenList;
    }	
}
