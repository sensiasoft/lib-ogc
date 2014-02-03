/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import java.util.Map;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.ogc.om.IObservation;
import org.vast.ogc.om.ObservationReaderV20;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;
import org.vast.sweCommon.SweComponentReaderV20;
import org.vast.sweCommon.SweEncodingReaderV20;


/**
 * <p><b>Title:</b><br/>
 * SOS InsertResultTemplate Request Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertResultTemplate
 * request and create a InsertResultTemplateRequest object for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 2, 2014
 * @version 1.0
 */
public class InsertResultTemplateReaderV20 extends SWERequestReader<InsertResultTemplateRequest>
{
    protected SweComponentReaderV20 componentReader = new SweComponentReaderV20();
    protected SweEncodingReaderV20 encodingReader = new SweEncodingReaderV20();
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
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		String val;
		
        // offering
        val = dom.getElementValue(requestElt, "offering");
        request.setOffering(val);        
		
        // observation template
        try
        {
            Element obsElt = dom.getElement(requestElt, "observationTemplate");
            IObservation obs = obsReader.read(dom, obsElt);
            request.setObservationTemplate(obs);
        }
        catch (XMLReaderException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "observationTemplate", "Unable to read O&M observation");
        }
        
        // result structure
        try
        {
            Element resultStructElt = dom.getElement(requestElt, "resultStructure");
            DataComponent component = componentReader.readComponent(dom, resultStructElt);
            request.setResultStructure(component);
        }
        catch (XMLReaderException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "resultStructure", "Unable to read SWE Common data");
        }
        
        // result encoding
        try
        {
            Element resultEncodingElt = dom.getElement(requestElt, "resultEncoding");
            DataEncoding encoding = encodingReader.readEncoding(dom, resultEncodingElt);
            request.setResultEncoding(encoding);
        }
        catch (XMLReaderException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "resultEncoding", "Unable to read SWE Common encoding");
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    /**
     * Checks that GetObservation mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(InsertResultTemplateRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);		
        
        // need offering
        if (request.getOffering() == null)
            report.add(new OWSException(OWSException.missing_param_code, "offering"));
        
        // need observation
        if (request.getObservationTemplate() == null)
            report.add(new OWSException(OWSException.missing_param_code, "observationTemplate"));
        
        // need result structure
        if (request.getResultStructure() == null)
            report.add(new OWSException(OWSException.missing_param_code, "resultStructure"));
        
        // need result encoding
        if (request.getResultEncoding() == null)
            report.add(new OWSException(OWSException.missing_param_code, "resultEncoding"));
        
        report.process();
    }

}