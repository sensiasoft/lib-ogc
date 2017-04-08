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
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSUtils;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertSensor
 * request and create a InsertSensorRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
public class InsertSensorReaderV20 extends org.vast.ows.swe.InsertSensorReaderV20<InsertSensorRequest>
{
    
    public InsertSensorReaderV20()
	{       
	}


    @Override
    public InsertSensorRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SOSException(noKVP + "SOS 2.0 InsertSensor");
    }
    
    
	@Override
	public InsertSensorRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertSensorRequest request = new InsertSensorRequest();
		
		// read common swes InsertSensor params
		super.readXMLQuery(dom, requestElt, request, report);
		
		// insertion metadata
		String val;
        Element metadataElt = dom.getElement(requestElt, "metadata/SosInsertionMetadata");
        if (metadataElt != null)
        {
            NodeList obsTypeElts = dom.getElements(metadataElt, "observationType");
            for (int i=0; i<obsTypeElts.getLength(); i++)
            {
                val = dom.getElementValue((Element)obsTypeElts.item(i));
                request.getObservationTypes().add(val);
            }
            
            NodeList foiTypeElts = dom.getElements(metadataElt, "featureOfInterestType");
            for (int i=0; i<foiTypeElts.getLength(); i++)
            {
                val = dom.getElementValue((Element)foiTypeElts.item(i));
                request.getFoiTypes().add(val);
            }
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    protected void checkParameters(InsertSensorRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);
        
        // need observation type
        if (request.getObservationTypes().isEmpty())
            report.add(new OWSException(OWSException.missing_param_code, "observationType"));
        
        // need foi type
        if (request.getFoiTypes().isEmpty())
            report.add(new OWSException(OWSException.missing_param_code, "featureOfInterestType"));
        
        // procedure
        if (request.getProcedureDescription() == null)
            report.add(new OWSException(OWSException.missing_param_code, "procedureDescription"));
        
        report.process();
    }

}