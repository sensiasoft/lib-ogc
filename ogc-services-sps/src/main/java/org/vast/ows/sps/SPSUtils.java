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
import javax.xml.namespace.QName;
import net.opengis.swe.v20.DataComponent;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.AbstractRequestReader;
import org.vast.ows.OWSBindingProvider;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSRequest;
import org.vast.ows.OWSRequestReader;
import org.vast.ows.OWSUtils;
import org.vast.ows.SweEncodedMessageProcessor;
import org.vast.ows.swe.SWESUtils;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Utility methods for common stuffs in SPS services
 * </p>
 *
 * @author Alex Robin
 * @date Oct 08, 2008
 */
public class SPSUtils extends OWSUtils implements OWSBindingProvider
{
	public static final String EVENT_TASK_EXPIRED = "TaskingRequestExpired";
	public static final String EVENT_TASK_SUBMITTED = "TaskSubmitted";
	public static final String EVENT_TASK_COMPLETED = "TaskCompleted";
	public static final String EVENT_DATA_PUBLISHED = "DataPublished";
	public static final String EVENT_TASK_FAILED = "TaskFailed";
	public static final String EVENT_TASK_CANCELLED = "TaskCancelled";
	public static final String EVENT_TASK_UPDATED = "TaskUpdated";
	public static final String EVENT_TASK_RESERVED = "TaskReserved";
	public static final String EVENT_TASK_CONFIRMED = "TaskConfirmed";
	public static final String EVENT_RESERVATION_EXPIRED = "ReservationExpired";
	
	
	@Override
    public void loadBindings()
    {
        String mapFileUrl = getClass().getResource("SPSRegistry.xml").toString();
        OGCRegistry.loadMaps(mapFileUrl, false);
    }
    
    
    public OWSRequest readSpsRequest(DOMHelper dom, Element requestElt, DataComponent sweParams) throws OWSException
    {
	    //requestElt = skipSoapEnvelope(dom, requestElt);
	    
	    // read common params and check that they're present
        OWSRequest request = new OWSRequest();
        AbstractRequestReader.readCommonXML(dom, requestElt, request);
        OWSExceptionReport report = new OWSExceptionReport();
        AbstractRequestReader.checkParameters(request, report, SPSUtils.SPS);
        report.process();
		
		// parse request with appropriate reader
        try
        {
            OWSRequestReader<?> reader = (OWSRequestReader<?>)OGCRegistry.createReader(SPSUtils.SPS, request.getOperation(), request.getVersion());
            if (reader instanceof SweEncodedMessageProcessor)
                ((SweEncodedMessageProcessor)reader).setSweCommonStructure(sweParams, null);
    		return reader.readXMLQuery(dom, requestElt);
        }
        catch (IllegalStateException e)
        {
            String spec = SPSUtils.SPS + " " + request.getOperation() + " v" + request.getVersion();
            throw new OWSException(UNSUPPORTED_SPEC_MSG + spec, e);
        }
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
