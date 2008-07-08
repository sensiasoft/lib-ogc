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

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.vast.util.ExceptionSystem;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLDocument;
import org.vast.ows.AbstractCapabilitiesReader;
import org.vast.ows.OWSCapabilitiesReaderV11;
import org.vast.ows.OWSException;
import org.vast.ows.OWSServiceCapabilities;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;


/**
 * <p><b>Title:</b><br/>
 * SASCapabilitiesReader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Gregoire Berthiau
 * @date Jul 2, 2008
 * @version 1.0
 */
public class SASCapabilitiesReader extends AbstractCapabilitiesReader
{
   
    public SASCapabilitiesReader()
    {
    }
    
    @Override
    protected void readContents(DOMHelper dom, Element capsElt) throws OWSException
    {
        NodeList subList = dom.getElements(capsElt, "Contents/SubscriptionOfferingList/SubscriptionOffering");
        if(subList == null)
    		throw new SASException("Capabilities from an SAS service must have subscription offering");
        
        for (int i=0; i<subList.getLength(); i++)
        {
            Element subElt = (Element)subList.item(i);
            
            String subName = "Subscription " + Integer.toString(i + 1); 
            
            SASLayerCapabilities layerCaps = new SASLayerCapabilities();           
            
            try
            {        
            	String subscriptionID = dom.getElementValue(subElt, "SubscriptionOfferingID");             
            	if(subscriptionID == null)
            		throw new SASException("a subscriptionID must be specified");
            	
            	Element messageStructureElt = dom.getElement(subElt, "messageStructure/DataBlockDefinition/components");  
            	if(messageStructureElt == null)
            		throw new SASException("a message structure in SWEcommon must be specified");
            	
            	String messageStructureName =  dom.getAttributeValue(messageStructureElt, "name");
            	String messageStructure = "";
            	
            	try {
					TransformerFactory tf = TransformerFactory.newInstance();
					Transformer trans = tf.newTransformer();
					StringWriter sw = new StringWriter();
					trans.transform(new DOMSource(messageStructureElt), new StreamResult(sw));
					messageStructure = sw.toString();
					messageStructure.substring(messageStructure.indexOf("?", 5)+1);
				} catch (TransformerConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerFactoryConfigurationError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				int initialIndex = messageStructure.indexOf("?", 5)+2;
				messageStructure = messageStructure.substring(initialIndex);
				
            	String sensorID = dom.getElementValue(subElt, "member/SensorID");
            	if(sensorID == null)
            		throw new SASException("a sensorID must be specified");
            
            	String frequency = null;
            	Element frequencyElt = dom.getElement(subElt, "member/reportingFrequency/Quantity"); 
            	if(frequencyElt!=null){
            	String frequencyValue = dom.getElementValue(frequencyElt, "value");
            	String frequencyUnit = dom.getAttributeValue(frequencyElt, "uom/code");  
            	frequency = frequencyValue + " " + frequencyUnit;
            	}
            	
            // plenty of other element and attribute to be added. message structure, subscription offering and sensor ID are required	
            	layerCaps.setMessageStructureName(messageStructureName);
            	layerCaps.setMessageStructure(messageStructure);
	            layerCaps.setTitle(subName);
	            layerCaps.setSubscriptionOfferingID(subscriptionID);
	            layerCaps.setSensorID(sensorID);
	            layerCaps.setFrequency(frequency);
	            layerCaps.setParent(serviceCaps);
            }
            catch (SASException e)
            {
            	String message = parsingError + " in subscription " + layerCaps.getSubscriptionOfferingID();
                ExceptionSystem.display(new SASException(message, e));
				continue;
            }
            
            serviceCaps.getLayers().add(layerCaps);
        }
    }

	@Override
	public OWSServiceCapabilities readXMLResponse(DOMHelper dom, Element capabilitiesElt) throws OWSException {

		serviceCaps = new OWSServiceCapabilities();
    	
    	// Version
        String version = dom.getAttributeValue(capabilitiesElt, "version");
        serviceCaps.setVersion(version);

        // Contents section
        readContents(dom, capabilitiesElt);
        
        return serviceCaps;
	}
    
}
