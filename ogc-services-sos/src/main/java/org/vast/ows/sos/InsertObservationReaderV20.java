/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.Map;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.vast.ogc.om.IObservation;
import org.vast.ogc.om.ObservationReaderV20;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertInsertObservation
 * request and create a InsertInsertObservationRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
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
            NodeList obsElts = dom.getElements(requestElt, "observation/*");
            for (int i=0; i<obsElts.getLength(); i++)
            {
                Element obsElt = (Element)obsElts.item(i);
                IObservation obs = reader.read(dom, obsElt);
                request.getObservations().add(obs);
            }
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
        if (request.getObservations() == null || request.getObservations().isEmpty())
            report.add(new OWSException(OWSException.missing_param_code, "observation"));
        
        report.process();
    }

}