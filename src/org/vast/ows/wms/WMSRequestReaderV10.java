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
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * WMS POST/GET Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a GET or POST WMS request and
 * create a WMSQuery object for versions 1.0
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 4, 2005
 * @version 1.0
 */
public class WMSRequestReaderV10 extends WMSRequestReader
{
	
	public WMSRequestReaderV10()
	{	
	}

	
	@Override
	public WMSQuery readURLQuery(String queryString) throws OWSException
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
                throw new WMSException(invalidKVP);
            }
            
            // service version
            if (argName.equalsIgnoreCase("wmtver"))
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
            
            // bbox
            else if (argName.equalsIgnoreCase("bbox"))
            {
                this.parseBboxArg(query.getBbox(), argValue);
            }
            
            // width
            else if (argName.equalsIgnoreCase("width"))
            {
                try {query.setWidth(Integer.parseInt(argValue));}
                catch (NumberFormatException e) {throw new WMSException(invalidKVP + "Width should be an integer value");}
            }
            
            // height
            else if (argName.equalsIgnoreCase("height"))
            {
                try {query.setHeight(Integer.parseInt(argValue));}
                catch (NumberFormatException e) {throw new WMSException(invalidKVP + "Height should be an integer value");}
            }
            
            // transparency
            else if (argName.equalsIgnoreCase("transparent"))
            {
                query.setTransparent(Boolean.parseBoolean(argValue));
            }
            
            // format argument
            else if (argName.equalsIgnoreCase("format"))
            {
                query.setFormat(argValue);
            }

            else
                throw new WMSException(invalidKVP + ": Unknown Argument " + argName);
        }
        
		return query;
	}
	
	
	@Override
	public WMSQuery readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		throw new WMSException("XML request not supported in WMS 1.0");
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