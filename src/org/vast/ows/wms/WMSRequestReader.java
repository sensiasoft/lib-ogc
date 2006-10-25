/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wms;

import java.util.StringTokenizer;

import org.vast.io.xml.DOMReader;
import org.w3c.dom.*;
import org.vast.ows.*;
import org.vast.ows.sos.SOSException;
import org.vast.ows.wcs.WCSException;


/**
 * <p><b>Title:</b><br/>
 * WMS Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST WMS request and
 * create an WMSQuery object
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 4, 2005
 * @version 1.0
 */
public class WMSRequestReader extends OWSRequestReader
{
	
	public WMSRequestReader()
	{	
	}

	
	@Override
	public WMSQuery readGetRequest(String queryString) throws OWSException
	{
		WMSQuery query = new WMSQuery();
		StringTokenizer st = new StringTokenizer(queryString, "&");
        
        while (st.hasMoreTokens())
        {
            String argName = null;
            String argValue = null;
            String nextArg = st.nextToken();

            // separate argument name and value
            try
            {
                int sepIndex = nextArg.indexOf('=');
                argName = nextArg.substring(0, sepIndex);
                argValue = nextArg.substring(sepIndex + 1);
            }
            catch (IndexOutOfBoundsException e)
            {
                throw new WCSException(invalidGet);
            }
            
            // service ID
            if (argName.equalsIgnoreCase("service"))
            {
                query.setService(argValue);
            }
            
            // service version
            else if (argName.equalsIgnoreCase("version"))
            {
                query.setVersion(argValue);
            }

            // request argument
            else if (argName.equalsIgnoreCase("request"))
            {
                query.setRequest(argValue);
            }
            
            // layers argument
            else if (argName.equalsIgnoreCase("layers"))
            {
                String[] layerList = argValue.split(",");
                query.getLayers().clear();                 
                for (int i=0; i<layerList.length; i++)
                    query.getLayers().add(layerList[i]);
            }
            
            // styles argument
            else if (argName.equalsIgnoreCase("styles"))
            {
                String[] styleList = argValue.split(",");
                query.getStyles().clear();                 
                for (int i=0; i<styleList.length; i++)
                    query.getStyles().add(styleList[i]);
            }
            
            // time
            else if (argName.equalsIgnoreCase("time"))
            {
                this.parseTimeArg(query.getTime(), argValue);
            }
            
            // bbox
            else if (argName.equalsIgnoreCase("bbox"))
            {
                this.parseBboxArg(query.getBbox(), argValue);
            }
            
            // width
            else if (argName.equalsIgnoreCase("width"))
            {
                try {query.setWidth(Integer.parseInt(argValue));}
                catch (NumberFormatException e) {throw new WMSException(invalidGet + "Width should be an integer value");}
            }
            
            // height
            else if (argName.equalsIgnoreCase("height"))
            {
                try {query.setHeight(Integer.parseInt(argValue));}
                catch (NumberFormatException e) {throw new WMSException(invalidGet + "Height should be an integer value");}
            }
            
            // format argument
            else if (argName.equalsIgnoreCase("format"))
            {
                query.setFormat(argValue);
            }

            else
                throw new SOSException(invalidGet + ": Unknown Argument " + argName);
        }
        
		return query;
	}
	
	
	@Override
	public WMSQuery readRequestXML(DOMReader domReader, Element requestElt) throws WMSException
	{
		String opName = requestElt.getLocalName();
		WMSQuery query;
		
		if (opName.equalsIgnoreCase("GetCapabilities"))
		{
			query = new WMSQuery();
			readGetCapabilitiesXML(domReader, requestElt, query);
		}
		
		else if (opName.equalsIgnoreCase("GetMap"))
			query = readGetMapXML(domReader, requestElt);
		
		else if (opName.equalsIgnoreCase("GetFeatureInfo"))
			query = readGetFeatureInfoXML(domReader, requestElt);
		
		else throw new WMSException("Operation " + opName + "not supported");
		
		// do common stuffs like version, request name and service type
		readCommonXML(domReader, requestElt, query);		
		
		return query;
	}
	
	
	/**
	 * Reads a GetMap XML request and fill up the WMSQuery accordingly
	 * @param domReader
	 * @param requestElt
	 * @return
	 * @throws WMSException
	 */
	protected WMSQuery readGetMapXML(DOMReader domReader, Element requestElt) throws WMSException
	{
		WMSQuery query = new WMSQuery();
		
		// layer names and styles
		NodeList layerElts = domReader.getElements(requestElt, "StyledLayerDescriptor/NamedLayer");
		for (int i=0; i<layerElts.getLength(); i++)
		{
			Element elt = (Element)layerElts.item(i);
			query.getLayers().add(domReader.getElementValue(elt, "Name"));
			String styleName = domReader.getElementValue(elt, "NamedStyle/Name");
			if (styleName == null) styleName = "";
			query.getStyles().add(styleName);
		}
		
		// bounding box
		parseBbox(query, domReader.getElementValue(requestElt, "BoundingBox/coordinates"));
		query.setSrs(domReader.getAttributeValue(requestElt, "BoundingBox/srsName"));
		
		// output parameters
		query.setFormat(domReader.getElementValue(requestElt, "Output/Format"));
		String transparency = domReader.getElementValue(requestElt, "Output/Transparent");
		query.setTransparent(transparency.equalsIgnoreCase("true") ? true : false);
		query.setWidth(Integer.parseInt(domReader.getElementValue(requestElt, "Output/Size/Width")));
		query.setHeight(Integer.parseInt(domReader.getElementValue(requestElt, "Output/Size/Height")));
		query.setExceptionType(domReader.getElementValue(requestElt, "Exceptions"));
		
		return query;
	}
	
	
	/**
	 * Parses a GetFeatureInfo request and fill up the WMSQuery accordingly
	 * @param domReader
	 * @param requestElt
	 * @return
	 * @throws WMSException
	 */
	protected WMSQuery readGetFeatureInfoXML(DOMReader domReader, Element requestElt) throws WMSException
	{
		WMSQuery query = new WMSQuery();
		
		return query;
	}
	
	
	/**
	 * Parses comma separated BBOX coordinates and set corresponding fields in WMSQuery
	 * @param query
	 * @param coordText
	 */
	protected void parseBbox(WMSQuery query, String coordText)
	{
		String[] coords = coordText.split("[ ,]");
		query.getBbox().setMinX(Double.parseDouble(coords[0]));
		query.getBbox().setMinY(Double.parseDouble(coords[1]));
		query.getBbox().setMaxX(Double.parseDouble(coords[2]));
		query.getBbox().setMaxY(Double.parseDouble(coords[3]));
	}
}