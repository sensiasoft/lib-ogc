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
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
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
        dom.setElementValue(offeringElt, "swes:procedure", offeringCaps.getProcedures().get(0));
        
        // procedure description formats
        for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getProcedureFormats"))
            dom.setElementValue(offeringElt, "+swes:procedureDescriptionFormat", token);
        
        // observable properties
        for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getObservableProperties"))
            dom.setElementValue(offeringElt, "+swes:observableProperty", token);
        
        // related features
        for (String token: getNonCommonListEntries(offeringCaps, serviceCaps, "getRelatedFeatures"))
            addRelatedFeature(dom, offeringElt, token);
        
        
	}
	
	
	protected void addRelatedFeature(DOMHelper dom, Element parentElt, String featureURI)
	{
	    dom.setAttributeValue(parentElt, "+swes:relatedFeature/swes:FeatureRelationship/swes:target/@xlink:href", featureURI);
	}
	
	
	/*
	 * Extracts entries that are common to all offerings in order to build the top level inherited section
	 */
	protected List<String> getCommonListEntries(OWSServiceCapabilities caps, String getListMethodName)
	{
	    ArrayList<String> intersectList = new ArrayList<String>();
	    boolean first = true;
	    
	    for (OWSLayerCapabilities layerCaps: caps.getLayers())
	    {
	        try
            {
                List<String> tokenListFromOffering = (List<String>)layerCaps.getClass().getMethod(getListMethodName).invoke(layerCaps);
                if (first)
                {
                    intersectList.addAll(tokenListFromOffering);
                    first = false;
                }
                else
                    intersectList.retainAll(tokenListFromOffering);                
            }
            catch (Exception e)
            {                
            }
	    }
	    
	    return intersectList;
	}
	
	
	/*
     * Extracts entries that are not common to all offerings
     */
    protected List<String> getNonCommonListEntries(OWSLayerCapabilities layerCaps, OWSServiceCapabilities serviceCaps, String getListMethodName)
    {
        ArrayList<String> nonIntersectList = new ArrayList<String>();        
        List<String> intersectList = getCommonListEntries(serviceCaps, getListMethodName);
        
        try
        {
            List<String> tokenListFromOffering = (List<String>)layerCaps.getClass().getMethod(getListMethodName).invoke(layerCaps);
            nonIntersectList.addAll(tokenListFromOffering);
            nonIntersectList.removeAll(intersectList);            
        }
        catch (Exception e)
        {                
        }
        
        return nonIntersectList;
    }
}
