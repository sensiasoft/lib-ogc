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

package org.vast.ows.swe;

import org.vast.ows.OWSException;
import org.vast.sensorML.SMLUtils;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import net.opengis.sensorml.v20.AbstractProcess;


/**
 * <p>
 * Reader for XML DescribeSensor response for SWES v2.0 
 * </p>
 *
 * @author Alex Robin
 * @date Dec, 14 2016
 * */
public class DescribeSensorResponseReaderV20 extends SWEResponseReader<DescribeSensorResponse>
{
    SMLUtils smlUtils = new SMLUtils(SMLUtils.V2_0);
    
    
    @Override
    public DescribeSensorResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
	    DescribeSensorResponse response = new DescribeSensorResponse();
	    response.setVersion("2.0");		
		String val;
		
		// read extensions
		readXMLExtensions(dom, responseElt, response);
		
		// procedureDescriptionFormat
        val = dom.getElementValue(responseElt, "procedureDescriptionFormat");
        response.setProcedureDescriptionFormat(val);
        
        // read SensorML procedure description
        try
        {            
            Element procedureElt = dom.getElement(responseElt, "description/SensorDescription/data/*");
            AbstractProcess process = smlUtils.readProcess(dom, procedureElt);
            response.setProcedureDescription(process);
        }
        catch (XMLReaderException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "procedureDescription", "Unable to read SensorML description:\n" + e.getMessage(), e);
        }
        
		return response;
	}	
}