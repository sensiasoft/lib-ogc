/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2016-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p>
 * Provides methods to parse a KVP SPS ConnectTasking request and create
 * a ConnectTaskingRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class ConnectTaskingReaderV20 extends SWERequestReader<ConnectTaskingRequest>
{
	
	public ConnectTaskingReaderV20()
	{
	}
	
	
	@Override
	public ConnectTaskingRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		ConnectTaskingRequest request = new ConnectTaskingRequest();
		readCommonQueryArguments(queryParameters, request);
		Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
		
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
			
            // template argument
            if (argName.equalsIgnoreCase("session"))
                request.setSessionID(argValue);
			
			else
				throw new SPSException(invalidKVP + ": Unknown Argument " + argName);
		}

		checkParameters(request, report);
		return request;
	}
	
	
	@Override
	public ConnectTaskingRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
	    throw new SPSException(noXML + "SPS 2.0 ConnectTasking");
	}
	
	
	/**
     * Checks that DescribeSensor mandatory parameters are present
     * @param request
     * @throws OWSException
     */
	protected void checkParameters(ConnectTaskingRequest request, OWSExceptionReport report) throws OWSException
	{
		// check common params + generate exception
		super.checkParameters(request, report);
		
		// need session ID
        if (request.getSessionID() == null)
            report.add(new OWSException(OWSException.missing_param_code, "session"));
		
		report.process();
	}
}