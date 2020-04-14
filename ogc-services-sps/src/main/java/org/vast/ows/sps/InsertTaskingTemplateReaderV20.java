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

import java.util.List;
import java.util.Map;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEValidator;


/**
 * <p>
 * Provides methods to parse a SOAP/XML SPS InsertTaskingTemplate
 * request and create the corresponding object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class InsertTaskingTemplateReaderV20 extends SWERequestReader<InsertTaskingTemplateRequest>
{
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    
    
    public InsertTaskingTemplateReaderV20()
	{       
	}


    @Override
    public InsertTaskingTemplateRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SPSException(noKVP + "SPS 2.0 InsertTaskingTemplate");
    }
    
    
	@Override
	public InsertTaskingTemplateRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertTaskingTemplateRequest request = new InsertTaskingTemplateRequest();
		SWEValidator validator = new SWEValidator();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		String val;
		Element templateElt = dom.getElement(requestElt, "proposedTemplate/TaskingTemplate");
        
        // offering
        val = dom.getElementValue(templateElt, "procedure");
        request.setProcedureID(val);
        
        // tasking parameters structure
        DataComponent structure = null;
        try
        {
            Element resultStructElt = dom.getElement(templateElt, "taskingParameters/*");
            structure = sweUtils.readComponent(dom, resultStructElt);
            List<Exception> errors = validator.validateComponent(structure, null);
            for (Exception e: errors)
                report.add(new OWSException(OWSException.invalid_param_code, "taskingParameters", "Invalid structure definition: " + e.getMessage()));
            request.setTaskingParameters(structure);
        }
        catch (XMLReaderException e)
        {
            throw new SPSException(SPSException.invalid_param_code, "taskingParameters", e);
        }
        
        // encoding
        try
        {
            Element resultEncodingElt = dom.getElement(templateElt, "encoding/*");
            DataEncoding encoding = sweUtils.readEncoding(dom, resultEncodingElt);
            List<Exception> errors = validator.validateEncoding(encoding, structure, null);
            for (Exception e: errors)
                report.add(new SPSException(SPSException.invalid_param_code, "encoding", "Invalid encoding definition: " + e.getMessage()));
            request.setEncoding(encoding);
        }
        catch (XMLReaderException e)
        {
            throw new SPSException(SPSException.invalid_param_code, "encoding", e);
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    protected void checkParameters(InsertTaskingTemplateRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SPS);		
        
        // need offering
        if (request.getProcedureID() == null)
            report.add(new SPSException(OWSException.missing_param_code, "procedure"));
        
        // need tasking parameters
        if (request.getTaskingParameters() == null)
            report.add(new SPSException(OWSException.missing_param_code, "taskingParameters"));
        
        // need encoding
        if (request.getEncoding() == null)
            report.add(new SPSException(OWSException.missing_param_code, "encoding"));
        
        report.process();
    }

}