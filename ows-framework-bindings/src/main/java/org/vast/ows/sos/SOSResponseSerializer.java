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

package org.vast.ows.sos;

import java.io.*;
import org.w3c.dom.*;
import org.vast.math.Vector3d;
import org.vast.ogc.OGCRegistry;
import org.vast.ogc.gml.GMLTimeWriter;
import org.vast.ogc.om.OMUtils;
import org.vast.ows.SweResponseSerializer;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.util.TimeExtent;
import org.vast.xml.DOMHelper;


/**
 * <p><b>Title:</b><br/>
 * SOS Response Serializer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This class is a specific serializer used to output XML for
 * SOS Responses (O&M Observation). It provides additional setID, setTime, setFoi, 
 * methods to easily change the corresponding parameters in the XML
 * response and overrides the <swe:result> element serialization by
 * calling the attached SweDataWriter to handle the job. 
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Feb 10, 2006
 * @version 1.0
 */
public class SOSResponseSerializer extends SweResponseSerializer
{
	protected Element obsElt;
    
	
    public SOSResponseSerializer()
	{		
	}
    
    
    @Override
    public void setTemplate(DOMHelper dom)
    {
        super.setTemplate(dom);        
        dom.addUserPrefix("gml", OGCRegistry.getNamespaceURI(OGCRegistry.GML));
        dom.addUserPrefix("om", OGCRegistry.getNamespaceURI(OMUtils.OM, "1.0"));
        dom.addUserPrefix("swe", OGCRegistry.getNamespaceURI(SWECommonUtils.SWE, "1.0"));
        NodeList elts = dom.getDocument().getElementsByTagNameNS("http://www.opengis.net/om/1.0", "Observation");
        obsElt = (Element)elts.item(0);
    }
    
    /**
     * Changes Id attribute for the ObservationCollection element
     * @param id
     */
    public void setRootID(String id){
    	dom.setAttributeValue(dom.getRootElement(), "gml:id", id);
    }
    
    
    /**
     * Changes Id attribute for an individual Observation in the ObservationCollection
     * @param id
     * @todo  support setting ID of any Observation in the ObsCollections
     */
    public void setID(String id)
    {
        dom.setAttributeValue(obsElt, "gml:id", id);
    }
    
    
	/**
	 * Changes eventTime element in the DOM to contain the request times
	 * @param time
	 */
	public void setTime(TimeExtent time, int zone)
	{
        try
        {
            time = time.copy();
            time.setTimeZone(zone);
            time.setBeginNow(false);  //  FIX THIS- should not change these
            time.setEndNow(false);
            time.setBaseAtNow(false);            
            Element obsTimeElt = dom.addElement(obsElt, "om:samplingTime");            
            GMLTimeWriter timeWriter = new GMLTimeWriter();
            Element timeElt = timeWriter.writeTime(dom, time);
            obsTimeElt.appendChild(timeElt);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
	}
    
    
    /**
     * Set Foi parameters name and location
     * @param name
     * @param location
     * @todo verify 1.0 works
     */
    public void setFoi(String name, Vector3d location)
    {
        try
        {
            Element foiElt = dom.addElement(obsElt, "om:featureOfInterest/swe:GeoReferenceableFeature");
            dom.setElementValue(foiElt, "gml:name", name);
            
            Element pointElt = dom.addElement(foiElt, "gml:location/gml:Point");
            dom.setAttributeValue(pointElt, "@srsName", "urn:ogc:def:crs:EPSG:6.1:4329");
            
            dom.setElementValue(pointElt, "gml:coordinates", location.x + " " + location.y + " " + location.z);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
	
	/**
	 * Adds a hook to override serialization of the result element
     * Uses the DataWriter to write the content of the element.
	 */
	protected void serializeElement(Element elt) throws IOException
	{
		if (elt.getLocalName().equals("values"))
		{
			String swePrefix = "swe";
            
            this._format.setIndenting(false);
			this._printer.printText("\n<" + swePrefix + ":values>");
			this._printer.flush();
			
            dataWriter.write();
			
			this._printer.printText("\n</" + swePrefix + ":values>");
			this._printer.flush();
			//  Probably need to turn this off- some parsers have had issues wiht added line breaks
			this._format.setIndenting(true);
		}
		else
		{
			super.serializeElement(elt);
		}
	}	
	
	
}
