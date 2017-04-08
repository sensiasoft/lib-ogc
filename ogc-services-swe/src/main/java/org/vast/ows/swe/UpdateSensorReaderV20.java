/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import net.opengis.sensorml.v20.AbstractProcess;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import java.util.Map;
import org.vast.ogc.OGCRegistry;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;
import org.vast.sensorML.SMLUtils;


/**
 * <p>
 * Provides methods to parse an XML SWES UpdateSensor
 * request and create a UpdateSensorRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class UpdateSensorReaderV20 extends SWERequestReader<UpdateSensorRequest>
{
    SMLUtils smlUtils = new SMLUtils(SMLUtils.V2_0);
    
    
    @Override
    public UpdateSensorRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new OWSException(noKVP + "SWES 2.0 UpdateSensor");
    }
    
    
    @Override
    public UpdateSensorRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
        OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
        UpdateSensorRequest request = new UpdateSensorRequest();
        
        // do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		String val;
		
		// procedure ID
		val = dom.getElementValue(requestElt, "procedure");
		request.setProcedureId(val);
				
        // procedureDescriptionFormat
        val = dom.getElementValue(requestElt, "procedureDescriptionFormat");
        request.setProcedureDescriptionFormat(val);
        if (request.getProcedureDescriptionFormat() == null)
        {
            report.add(new OWSException(OWSException.missing_param_code, "procedureDescriptionFormat"));
            report.process();
        }
        
        // check format is supported
        if (!val.equals(OGCRegistry.getNamespaceURI(SMLUtils.SENSORML, "2.0")))
            throw new OWSException(OWSException.invalid_param_code, "procedureDescription", "Unsupported format: " + val);
            
        // read SensorML description
        try
        {            
            Element procedureElt = dom.getElement(requestElt, "description/SensorDescription/data/*");
            AbstractProcess process = smlUtils.readProcess(dom, procedureElt);
            request.setProcedureDescription(process);
        }
        catch (XMLReaderException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "description", "Unable to read SensorML description:\n" + e.getMessage(), e);
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    protected void checkParameters(InsertSensorRequest request, OWSExceptionReport report) throws OWSException
    {
        // check common params
        super.checkParameters(request, report, OWSUtils.SPS);
        
        // procedure
        if (request.getProcedureDescription() == null)
            report.add(new OWSException(OWSException.missing_param_code, "description"));
        
        report.process();
    }
}