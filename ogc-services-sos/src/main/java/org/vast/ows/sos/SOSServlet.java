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

import org.vast.ows.GetCapabilitiesRequest;
import org.vast.ows.OWSRequest;
import org.vast.ows.server.OWSServlet;
import org.vast.ows.swe.DeleteSensorRequest;
import org.vast.ows.swe.DescribeSensorRequest;
import org.vast.ows.swe.UpdateSensorRequest;


/**
 * <p>
 * Base abstract class for implementing SOS servlets
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 24, 2012
 * */
@SuppressWarnings("serial")
public abstract class SOSServlet extends OWSServlet
{
    protected static final String DEFAULT_VERSION = "2.0.0";
    protected static final String TEXT_MIME_TYPE = "text/plain";
    protected static final String BINARY_MIME_TYPE = "application/octet-stream";
    protected static final String UNSUPPORTED_MSG = " operation is not supported on this server";
        
    protected static final String SOS_PREFIX = "sos";
    protected static final String SWES_PREFIX = "swe";
    protected static final String SOAP_PREFIX = "soap";
    
    
	@Override
    public void handleRequest(OWSRequest request) throws Exception
    {
	    // core operations
	    if (request instanceof GetCapabilitiesRequest)
            handleRequest((GetCapabilitiesRequest)request);
        else if (request instanceof DescribeSensorRequest)
            handleRequest((DescribeSensorRequest)request);
        else if (request instanceof GetFeatureOfInterestRequest)
            handleRequest((GetFeatureOfInterestRequest)request);
	    else if (request instanceof GetObservationRequest)
            handleRequest((GetObservationRequest)request);
	    
	    // result retrieval
        else if (request instanceof GetResultRequest)
            handleRequest((GetResultRequest)request);
        else if (request instanceof GetResultTemplateRequest)
            handleRequest((GetResultTemplateRequest)request);
	    
	    // transactional methods
        else if (request instanceof InsertSensorRequest)
            handleRequest((InsertSensorRequest)request);
        else if (request instanceof UpdateSensorRequest)
            handleRequest((UpdateSensorRequest)request);
        else if (request instanceof DeleteSensorRequest)
            handleRequest((DeleteSensorRequest)request);
        else if (request instanceof InsertObservationRequest)
            handleRequest((InsertObservationRequest)request);
        else if (request instanceof InsertResultRequest)
            handleRequest((InsertResultRequest)request);
        else if (request instanceof InsertResultTemplateRequest)
            handleRequest((InsertResultTemplateRequest)request);
    }


    protected abstract void handleRequest(GetCapabilitiesRequest query) throws Exception;
	    
	
    protected abstract void handleRequest(DescribeSensorRequest request) throws Exception;
	
	
	protected abstract void handleRequest(GetObservationRequest request) throws Exception;
	
	
	protected void handleRequest(GetResultTemplateRequest request) throws Exception
	{
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
	}
	
	
	protected void handleRequest(GetResultRequest request) throws Exception
	{
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
	}
    
	
	protected void handleRequest(GetFeatureOfInterestRequest request) throws Exception
	{
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
	}
	
	
	protected void handleRequest(InsertSensorRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(UpdateSensorRequest request) throws Exception
    {
        throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(DeleteSensorRequest request) throws Exception
    {
        throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(InsertObservationRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(InsertResultTemplateRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
	protected void handleRequest(InsertResultRequest request) throws Exception
    {
	    throw new UnsupportedOperationException(request.getOperation() + UNSUPPORTED_MSG);
    }
	
	
    @Override
    protected String getServiceType()
    {
        return SOSUtils.SOS;
    }
}
