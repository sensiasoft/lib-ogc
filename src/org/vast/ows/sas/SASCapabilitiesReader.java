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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sas;

import org.vast.xml.DOMHelper;
import org.vast.ows.AbstractCapabilitiesReader;
import org.vast.ows.OWSException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b><br/>
 * SASCapabilitiesReader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Tony Cook
 * @date Nov 21, 2006
 * @version 1.0
 */
public class SASCapabilitiesReader extends AbstractCapabilitiesReader
{
   
    public SASCapabilitiesReader()
    {
    }
    
    
    @Override
    protected String buildQuery() throws OWSException
    {
        String url = null;
        url = this.server + "service=SAS&version=" + version + "&request=GetCapabilities";        
        return url;
    }
    
    
    @Override
    protected void readServers(DOMHelper dom, Element capsElt) throws OWSException
    {
    	super.readServers(dom, capsElt);
    	
    	// put default POST server if none is specified in capabilities doc
		if (serviceCaps.getGetServers().isEmpty() && serviceCaps.getPostServers().isEmpty())
		{
			serviceCaps.getPostServers().put("GetObservation", this.server);
		}
    }

    
    @Override
    protected void readContents(DOMHelper dom, Element capsElt) throws OWSException
    {
        NodeList subList = dom.getElements(capsElt, "Contents/SubscriptionOfferingList/SubscriptionOffering");
        
        for (int i=0; i<subList.getLength(); i++)
        {
            Element subElt = (Element)subList.item(i);
            System.out.println(dom.getAttributeValue(subElt, "@id"));
           // String offeringName = dom.getElementValue(offeringElt, "name");
            
           // if (offeringName == null)
           // 	offeringName = "Offering " + i + 1;
            
//            String id = dom.getAttributeValue(offeringElt, "id");
//            
//            SOSLayerCapabilities layerCaps = new SOSLayerCapabilities();           
//            
//            try
//            {
//	            layerCaps.setName(offeringName);
//	            layerCaps.setId(id);
//	            getFormatList(offeringElt, layerCaps);
//	            getObservableList(offeringElt, layerCaps);
//	            getProcedureList(offeringElt, layerCaps);
//	            getTimeList(offeringElt, layerCaps);
//	            layerCaps.setParent(serviceCaps);
//            }
//            catch (GMLException e)
//            {
//            	String message = parsingError + " in offering " + layerCaps.getId();
//                ExceptionSystem.display(new OWSException(message, e));
//				continue;
//            }
//            
//            serviceCaps.getLayers().add(layerCaps);
        }
    }
    
}
