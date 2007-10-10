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

package org.vast.ows.wcs;

import java.util.StringTokenizer;
import org.vast.xml.DOMHelper;
import org.w3c.dom.*;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * DescribeCoverage Request Reader v1.1
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or XML DescribeCoverage request and
 * create a DescribeCoverage object for version 1.1.0 and 1.1.1
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Oct 08, 2007
 * @version 1.0
 */
public class DescribeCoverageReaderV11 extends AbstractRequestReader<DescribeCoverageRequest>
{
	
	@Override
	public DescribeCoverageRequest readURLQuery(String queryString) throws OWSException
	{
		DescribeCoverageRequest query = new DescribeCoverageRequest();
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
                throw new WCSException(invalidKVP);
            }
            
            // SERVICE
            if (argName.equalsIgnoreCase("SERVICE"))
            {
                query.setService(argValue);
            }
            
            // VERSION
            else if (argName.equalsIgnoreCase("VERSION"))
            {
                query.setVersion(argValue);
            }

            // REQUEST
            else if (argName.equalsIgnoreCase("REQUEST"))
            {
                query.setRequest(argValue);
            }
            
            // IDENTIFIER list
            else if (argName.equalsIgnoreCase("IDENTIFIERS"))
            {
                String[] coverageList = argValue.split(",");
                for (int i=0; i<coverageList.length; i++)
                	query.getCoverages().add(coverageList[i]);
            }
        }

        return query;
	}
	
	
	@Override
	public DescribeCoverageRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		DescribeCoverageRequest query = new DescribeCoverageRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, query);
		
		// get all Coverage ids
		NodeList idElts = dom.getElements(requestElt, "Identifier");
		for (int i=0; i<idElts.getLength(); i++)
		{
			String val = dom.getElementValue((Element)idElts.item(i));
			query.getCoverages().add(val);
		}
		
		return query;
	}
}