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
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wms;

import java.util.StringTokenizer;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;
import org.vast.ows.util.Bbox;


/**
 * <p><b>Title:</b><br/>
 * GetMap Request Reader v1.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or XML GetMap request and
 * create a GetMapRequest object for version 1.0
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 10, 2007
 * @version 1.0
 */
public class GetMapReaderV10 extends AbstractRequestReader<GetMapRequest>
{
	
	public GetMapReaderV10()
	{	
	}

	
	@Override
	public GetMapRequest readURLQuery(String queryString) throws OWSException
	{
		GetMapRequest request = new GetMapRequest();
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
                request.setVersion(argValue);
            }

            // request argument
            else if (argName.equalsIgnoreCase("request"))
            {
                request.setOperation(argValue);
            }
            
            // layers argument
            else if (argName.equalsIgnoreCase("layers"))
            {
                String[] layerList = argValue.split(",");
                request.getLayers().clear();                 
                for (int i=0; i<layerList.length; i++)
                    request.getLayers().add(layerList[i]);
            }
            
            // styles argument
            else if (argName.equalsIgnoreCase("styles"))
            {
                String[] styleList = argValue.split(",");
                request.getStyles().clear();                 
                for (int i=0; i<styleList.length; i++)
                    request.getStyles().add(styleList[i]);
            }
            
            // bbox
            else if (argName.equalsIgnoreCase("bbox"))
            {
            	Bbox bbox = parseBboxArg(argValue);
                request.setBbox(bbox);
            }
            
            // width
            else if (argName.equalsIgnoreCase("width"))
            {
                try {request.setWidth(Integer.parseInt(argValue));}
                catch (NumberFormatException e) {throw new WMSException(invalidKVP + "Width should be an integer value");}
            }
            
            // height
            else if (argName.equalsIgnoreCase("height"))
            {
                try {request.setHeight(Integer.parseInt(argValue));}
                catch (NumberFormatException e) {throw new WMSException(invalidKVP + "Height should be an integer value");}
            }
            
            // transparency
            else if (argName.equalsIgnoreCase("transparent"))
            {
                request.setTransparent(Boolean.parseBoolean(argValue));
            }
            
            // format argument
            else if (argName.equalsIgnoreCase("format"))
            {
                request.setFormat(argValue);
            }

            else
                throw new WMSException(invalidKVP + ": Unknown Argument " + argName);
        }
        
		return request;
	}
	
	
	@Override
	public GetMapRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		throw new WMSException(noXML + "WMS 1.0 GetMap");
	}
	
	
	/**
	 * Parses comma separated BBOX coordinates and set corresponding fields in WMSQuery
	 * @param query
	 * @param coordText
	 */
	protected void parseBbox(GetMapRequest query, String coordText)
	{
		String[] coords = coordText.split("[ ,]");
		query.getBbox().setMinX(Double.parseDouble(coords[0]));
		query.getBbox().setMinY(Double.parseDouble(coords[1]));
		query.getBbox().setMaxX(Double.parseDouble(coords[2]));
		query.getBbox().setMaxY(Double.parseDouble(coords[3]));
	}
}