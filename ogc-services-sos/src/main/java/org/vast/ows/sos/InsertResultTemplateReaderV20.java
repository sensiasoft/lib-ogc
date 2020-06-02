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

import java.util.List;
import java.util.Map;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.vast.ogc.om.IObservation;
import org.vast.ogc.om.ObservationReaderV20;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestReader;
import org.vast.swe.SWEUtils;
import org.vast.swe.SWEValidator;


/**
 * <p>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertResultTemplate
 * request and create a InsertResultTemplateRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
public class InsertResultTemplateReaderV20 extends SWERequestReader<InsertResultTemplateRequest>
{
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    protected ObservationReaderV20 obsReader = new ObservationReaderV20();
    
    
    public InsertResultTemplateReaderV20()
	{
       
	}


    @Override
    public InsertResultTemplateRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SOSException(noKVP + "SOS 2.0 InsertResultTemplate");
    }
    
    
	@Override
	public InsertResultTemplateRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertResultTemplateRequest request = new InsertResultTemplateRequest();
		SWEValidator validator = new SWEValidator();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		String val;
		Element templateElt = dom.getElement(requestElt, "proposedTemplate/ResultTemplate");
		
        // offering
        val = dom.getElementValue(templateElt, "offering");
        request.setOffering(val);        
		
        // observation template
        try
        {
            Element obsElt = dom.getElement(templateElt, "observationTemplate/*");
            if (obsElt != null)
            {
                IObservation obs = obsReader.read(dom, obsElt);
                request.setObservationTemplate(obs);
            }
        }
        catch (XMLReaderException e)
        {
            throw new SOSException(OWSException.invalid_param_code, "observationTemplate", e);
        }
        
        // result structure
        DataComponent structure = null;
        try
        {
            Element resultStructElt = dom.getElement(templateElt, "resultStructure/*");
            structure = sweUtils.readComponent(dom, resultStructElt);
            List<Exception> errors = validator.validateComponent(structure, null);
            for (Exception e: errors)
                report.add(new OWSException(OWSException.invalid_param_code, "resultStructure", "Invalid structure definition: " + e.getMessage()));
            request.setResultStructure(structure);
        }
        catch (XMLReaderException e)
        {
            throw new SOSException(OWSException.invalid_param_code, "resultStructure", e);
        }
        
        // result encoding
        try
        {
            Element resultEncodingElt = dom.getElement(templateElt, "resultEncoding/*");
            DataEncoding encoding = sweUtils.readEncoding(dom, resultEncodingElt);
            List<Exception> errors = validator.validateEncoding(encoding, structure, null);
            for (Exception e: errors)
                report.add(new OWSException(OWSException.invalid_param_code, "resultEncoding", "Invalid encoding definition: " + e.getMessage()));
            request.setResultEncoding(encoding);
        }
        catch (XMLReaderException e)
        {
            throw new SOSException(OWSException.invalid_param_code, "resultEncoding", e);
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    /**
     * Checks that InsertResultTemplate mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(InsertResultTemplateRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);		
        
        // need offering
        if (request.getOffering() == null)
            report.add(new SOSException(OWSException.missing_param_code, "offering"));
        
        // need observation
        //if (request.getObservationTemplate() == null)
        //    report.add(new OWSException(OWSException.missing_param_code, "observationTemplate"));
        
        // need result structure
        if (request.getResultStructure() == null)
            report.add(new SOSException(OWSException.missing_param_code, "resultStructure"));
        
        // need result encoding
        if (request.getResultEncoding() == null)
            report.add(new SOSException(OWSException.missing_param_code, "resultEncoding"));
        
        report.process();
    }

}