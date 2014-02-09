/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.Map;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ogc.om.IObservation;
import org.vast.ogc.om.ObservationReaderV20;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p><b>Title:</b><br/>
 * SOS InsertInsertObservation Request Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertInsertObservation
 * request and create a InsertInsertObservationRequest object for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 2, 2014
 * @version 1.0
 */
public class InsertObservationReaderV20 extends SWERequestReader<InsertObservationRequest>
{
    protected ObservationReaderV20 reader = new ObservationReaderV20();
    
    
    public InsertObservationReaderV20()
	{
       
	}


    @Override
    public InsertObservationRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SOSException(noKVP + "SOS 2.0 InsertObservation");
    }
    
    
	@Override
	public InsertObservationRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertObservationRequest request = new InsertObservationRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
        // offering
        String offering = dom.getElementValue(requestElt, "offering");
        request.setOffering(offering);		
        
        // observation
        try
        {
            Element obsElt = dom.getElement(requestElt, "observation/*");
            IObservation obs = reader.read(dom, obsElt);
            request.setObservation(obs);
        }
        catch (Exception e)
        {
            report.add(new OWSException(OWSException.invalid_param_code, "observation", "Error while parsing observation content", e));
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    /**
     * Checks that InsertObservation mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(InsertObservationRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);		
        
        // need offering
        if (request.getOffering() == null)
            report.add(new OWSException(OWSException.missing_param_code, "offering"));
        
        // need observation
        if (request.getObservation() == null)
            report.add(new OWSException(OWSException.missing_param_code, "observation"));
        
        report.process();
    }

}