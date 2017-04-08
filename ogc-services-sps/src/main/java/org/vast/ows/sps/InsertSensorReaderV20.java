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

import java.util.Map;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ows.*;


/**
 * <p>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertSensor
 * request and create a InsertSensorRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class InsertSensorReaderV20 extends org.vast.ows.swe.InsertSensorReaderV20<InsertSensorRequest>
{
    
    public InsertSensorReaderV20()
	{       
	}


    @Override
    public InsertSensorRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SPSException(noKVP + "SPS 2.0 InsertSensor");
    }
    
    
	@Override
	public InsertSensorRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertSensorRequest request = new InsertSensorRequest();
		
		// read common swes InsertSensor params
		super.readXMLQuery(dom, requestElt, request, report);
		
		/*// insertion metadata
		String val;
        Element metadataElt = dom.getElement(requestElt, "metadata/SpsInsertionMetadata");
        if (metadataElt != null)
        {
            
        }*/
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    protected void checkParameters(InsertSensorRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SPS);
        
        // procedure
        if (request.getProcedureDescription() == null)
            report.add(new OWSException(OWSException.missing_param_code, "procedureDescription"));
        
        report.process();
    }

}