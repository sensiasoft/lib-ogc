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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.vast.ows.OWSCapabilitiesWriterV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.ows.OWSServiceCapabilities;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Helper methods to write SWES v2.0 capabilities documents
 * </p>
 *
 * @author Alex Robin
 * @since Dec 12, 2014
 */
public abstract class SWESCapabilitiesWriterV20 extends OWSCapabilitiesWriterV11
{
    	
	protected void writeCommonContentsProperties(DOMHelper dom, Element contentsElt, OWSServiceCapabilities serviceCaps) throws OWSException
	{
		// SWES properties common to all offerings
		for (String token: getCommonListEntries(serviceCaps, "getProcedureFormats"))
            dom.setElementValue(contentsElt, "+swes:procedureDescriptionFormat", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getObservableProperties"))
            dom.setElementValue(contentsElt, "+swes:observableProperty", token);
        
        for (String token: getCommonListEntries(serviceCaps, "getRelatedFeatures"))
            addRelatedFeature(dom, contentsElt, token);
	}
	
	
	protected void writeCommonOfferingProperties(DOMHelper dom, Element offeringElt, OWSServiceCapabilities serviceCaps, SWESOfferingCapabilities offeringCaps)
	{
	    // description
        String desc = offeringCaps.getDescription();
        if (desc != null)
            dom.setElementValue(offeringElt, "swes:description", desc);
        
        // identifier
        dom.setElementValue(offeringElt, "swes:identifier", offeringCaps.getIdentifier());
        
        // name
        String title = offeringCaps.getTitle();
        if (title != null)
            dom.setElementValue(offeringElt, "+swes:name", title);
        
        // procedure
        if (!offeringCaps.getProcedures().isEmpty())
            dom.setElementValue(offeringElt, "swes:procedure", offeringCaps.getProcedures().iterator().next());
        
        // procedure description formats (inheritance = replace)
        if (!offeringCaps.getProcedureFormats().equals(getCommonListEntries(serviceCaps, "getProcedureFormats")))
        {
            for (String token: offeringCaps.getProcedureFormats())
                dom.setElementValue(offeringElt, "+swes:procedureDescriptionFormat", token);
        }
        
        // observable properties (inheritance = replace)
        if (!offeringCaps.getObservableProperties().equals(getCommonListEntries(serviceCaps, "getObservableProperties")))
        {
            for (String token: offeringCaps.getObservableProperties())
                dom.setElementValue(offeringElt, "+swes:observableProperty", token);
        }
        
        // related features (inheritance = replace)
        if (!offeringCaps.getRelatedFeatures().equals(getCommonListEntries(serviceCaps, "getRelatedFeatures")))
        {
            for (String token: offeringCaps.getRelatedFeatures())
                addRelatedFeature(dom, offeringElt, token);
        }        
	}
	
	
	protected void addRelatedFeature(DOMHelper dom, Element parentElt, String featureURI)
	{
	    dom.setAttributeValue(parentElt, "+swes:relatedFeature/swes:FeatureRelationship/swes:target/@xlink:href", featureURI);
	}
	
	
	/*
	 * Extracts token sets common to all offerings
	 */
	protected Collection<String> getCommonListEntries(OWSServiceCapabilities caps, String getListMethodName)
	{
	    /*
        // collect all offering sets
	    List<Set<String>> offeringSets = new ArrayList<Set<String>>(caps.getLayers().size());
	    for (OWSLayerCapabilities layerCaps: caps.getLayers())
	    {
	        try
            {
                Set<String> tokenSet = (Set<String>)layerCaps.getClass().getMethod(getListMethodName).invoke(layerCaps);
                offeringSets.add(tokenSet);
            }
            catch (Exception e)
            {
            }
	    }
	    
	    // sort sets by size
        Collections.sort(offeringSets, new Comparator<Set<String>>() {
            @Override
            public int compare(Set<String> set1, Set<String> set2)
            {
                return set2.size() - set1.size();
            }
        });
	    
	    // iterate to find largest common set
	    Set<String> bestSet = Collections.emptySet();
	    int bestScore = 0;
	    for (int i = 0; i < offeringSets.size(); i++)
	    {
    	    int score = 0;
    	    Set<String> refSet = offeringSets.get(i);
    	            
    	    for (int j = i; j < offeringSets.size(); j++)
	        {
	            if (offeringSets.get(j).equals(refSet))
	                score += refSet.size();
	        }
    	    
    	    // best set has to be used in at least 2 offerings
    	    if (score > refSet.size() && score > bestScore)
    	    {
    	        bestScore = score;
    	        bestSet = refSet;
    	    }
	    }
	    */
	    
	    // if there is only one offering, don't use the common section
	    if (caps.getLayers().size() <= 1)
	        return Collections.EMPTY_SET;
	    
	    // keep set if common to all offerings
	    Set<String> firstSet = null;
	    for (OWSLayerCapabilities layerCaps: caps.getLayers())
        {
            try
            {
                Set<String> tokenSet = (Set<String>)layerCaps.getClass().getMethod(getListMethodName).invoke(layerCaps);
                if (firstSet != null && !tokenSet.equals(firstSet))
                    return Collections.EMPTY_SET;
                firstSet = tokenSet;
            }
            catch (Exception e)
            {
                throw new IllegalArgumentException("Invalid getList method", e);
            }
        }
	    
	    return firstSet;
	}
}
