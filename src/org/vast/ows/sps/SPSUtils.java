/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 	Alexandre Robin <alexandre.robin@spotimage.fr>
 	
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.cdm.common.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSUtils;
import org.vast.ows.ParameterizedRequest;
import org.vast.ows.ParameterizedRequestReader;
import org.vast.ows.ParameterizedResponse;
import org.vast.ows.ParameterizedResponseReader;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * SPS Utils
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Utility methods for common stuffs in SPS services
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Alexandre Robin
 * @date Oct 08, 2008
 */
public class SPSUtils extends OWSUtils
{
	
	public ParameterizedRequest readParameterizedRequest(DOMHelper dom, Element requestElt, DataComponent mainParams) throws OWSException
	{
		OWSRequest tempReq = new OWSRequest();
		AbstractRequestReader.readCommonXML(dom, requestElt, tempReq);
		
		ParameterizedRequestReader reader = (ParameterizedRequestReader)OGCRegistry.createReader(OWSUtils.SPS, tempReq.getOperation(), tempReq.getVersion());
		reader.setParamStructure(mainParams);
		ParameterizedRequest request = (ParameterizedRequest)reader.readXMLQuery(dom, dom.getRootElement());
		
		return request;
	}
	
	
	public ParameterizedResponse readParameterizedResponse(DOMHelper dom, Element responseElt, DataComponent mainParams, String version) throws OWSException
	{
		ParameterizedResponseReader reader = (ParameterizedResponseReader)OGCRegistry.createReader(OWSUtils.SPS, responseElt.getLocalName(), version);
		reader.setParamStructure(mainParams);
		ParameterizedResponse response = (ParameterizedResponse)reader.readXMLResponse(dom, dom.getRootElement());
		
		return response;
	}
	
}
