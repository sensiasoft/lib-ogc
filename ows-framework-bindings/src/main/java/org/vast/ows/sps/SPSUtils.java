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

import java.util.Map;
import org.vast.cdm.common.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestReader;
import org.vast.ows.OWSException;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSRequestReader;
import org.vast.ows.OWSUtils;
import org.vast.ows.ParameterizedRequest;
import org.vast.ows.ParameterizedRequestReader;
import org.vast.ows.swe.SWESUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.QName;
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
	public static String EVENT_TASK_EXPIRED = "TaskingRequestExpired";
	public static String EVENT_TASK_SUBMITTED = "TaskSubmitted";
	public static String EVENT_TASK_COMPLETED = "TaskCompleted";
	public static String EVENT_DATA_PUBLISHED = "DataPublished";
	public static String EVENT_TASK_FAILED = "TaskFailed";
	public static String EVENT_TASK_CANCELLED = "TaskCancelled";
	public static String EVENT_TASK_UPDATED = "TaskUpdated";	
	public static String EVENT_TASK_RESERVED = "TaskReserved";
	public static String EVENT_TASK_CONFIRMED = "TaskConfirmed";
	public static String EVENT_RESERVATION_EXPIRED = "ReservationExpired";
	
	
	public ParameterizedRequest readParameterizedRequest(DOMHelper dom, Element requestElt, DataComponent mainParams) throws OWSException
	{
		OWSRequest tempReq = new OWSRequest();
		AbstractRequestReader.readCommonXML(dom, requestElt, tempReq);
		
		OWSRequestReader<?> reader = (OWSRequestReader<?>)OGCRegistry.createReader(SPSUtils.SPS, tempReq.getOperation(), tempReq.getVersion());
		((ParameterizedRequestReader)reader).setParamStructure(mainParams);
		ParameterizedRequest request = (ParameterizedRequest)reader.readXMLQuery(dom, requestElt);
		
		return request;
	}
	
	
	public DescribeTaskingResponse readDescribeTaskingResponse(DOMHelper dom, Element respElt) throws OWSException
	{
		DescribeTaskingResponse resp = (DescribeTaskingResponse)readXMLResponse(dom, respElt, SPSUtils.SPS, "DescribeTaskingResponse");
		return resp;
	}
	
	
	public DescribeTaskingResponse readDescribeTaskingResponse(Element respElt) throws OWSException
	{
		DOMHelper dom = new DOMHelper(respElt.getOwnerDocument());
		return readDescribeTaskingResponse(dom, respElt);
	}
	
	
	public TaskingResponse<?> readTaskingResponse(DOMHelper dom, Element respElt, String version) throws OWSException
	{
		return (TaskingResponse<?>)super.readXMLResponse(dom, respElt, SPSUtils.SPS, respElt.getLocalName(), version);
	}
	
	
	public GetStatusResponse readStatusResponse(DOMHelper dom, Element respElt, String version) throws OWSException
	{
		return (GetStatusResponse)super.readXMLResponse(dom, respElt, SPSUtils.SPS, respElt.getLocalName(), version);
	}
	
	
	public static void writeXMLExtensions(DOMHelper dom, Element parentElt, String version, Map<QName, Object> extObjs)
	{
		String spsUri = OGCRegistry.getNamespaceURI(OWSUtils.SPS, version);
		SWESUtils.writeXMLExtensions(dom, parentElt, "sps", spsUri, extObjs);
	}
	
}
