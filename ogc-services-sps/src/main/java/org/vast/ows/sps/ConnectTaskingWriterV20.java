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

import java.util.LinkedHashMap;
import java.util.Map;
import org.vast.xml.DOMHelper;
import org.vast.ows.AbstractRequestWriter;
import org.vast.ows.OWSException;
import org.w3c.dom.Element;


/**
 * <p>
 * Provides methods to generate a KVP SPS ConnectTasking request based on
 * values contained in a ConnectTaskingRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class ConnectTaskingWriterV20 extends AbstractRequestWriter<ConnectTaskingRequest>
{
        
	public ConnectTaskingWriterV20()
	{
	}

	
	@Override
	public Map<String, String> buildURLParameters(ConnectTaskingRequest request) throws OWSException
	{
	    Map<String, String> urlParams = new LinkedHashMap<String, String>();
        addCommonArgs(urlParams, request);
        
		// procedure
        urlParams.put("session", request.getSessionID());
        
		return urlParams;
	}
	
	
	@Override
	public Element buildXMLQuery(DOMHelper dom, ConnectTaskingRequest request) throws OWSException
	{
	    throw new SPSException(noXML + "SPS 2.0 ConnectTasking");
	}
}