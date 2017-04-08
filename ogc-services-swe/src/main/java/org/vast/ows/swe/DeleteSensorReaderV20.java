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

import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;


/**
 * <p>
 * Provides methods to parse a KVP or XML SWES DeleteSensor
 * request and create a DeleteSensorRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class DeleteSensorReaderV20 extends SWERequestReader<DeleteSensorRequest>
{

    public DeleteSensorReaderV20()
    {        
    }
    
    
    @Override
    public DeleteSensorRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
        DeleteSensorRequest request = new DeleteSensorRequest();
        readCommonQueryArguments(queryParameters, request);
        Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
        
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
            
            // procedure
            if ("procedure".equalsIgnoreCase(argName))
            {
                request.setProcedureId(argValue);
            }
            
            else
                throw new OWSException(invalidKVP + ": Unknown Argument " + argName);
        }

        checkParameters(request, report);
        return request;
    }
    
    
    @Override
    public DeleteSensorRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
        OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
        DeleteSensorRequest request = new DeleteSensorRequest();        
        
        // do common stuffs like version, request name and service type
        readCommonXML(dom, requestElt, request);
        
        // procedure
        String procedure = dom.getElementValue(requestElt, "procedure");
        request.setProcedureId(procedure);
        
        this.checkParameters(request, report); 
        return request;
    }
    
    
    protected void checkParameters(DeleteSensorRequest request, OWSExceptionReport report) throws OWSException
    {
        // check common params + generate exception
        super.checkParameters(request, report);
        
        // need procedure
        if (request.getProcedureId() == null)
            report.add(new OWSException(OWSException.missing_param_code, "procedure"));
        
        report.process();
    }
}