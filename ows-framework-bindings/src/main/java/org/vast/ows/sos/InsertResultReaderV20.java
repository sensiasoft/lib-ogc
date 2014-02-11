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

import java.io.IOException;
import java.util.Map;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.ows.*;
import org.vast.ows.swe.SWERequestReader;
import org.vast.sweCommon.DataSourceDOM;
import org.vast.sweCommon.SWEData;


/**
 * <p><b>Title:</b><br/>
 * SOS InsertResult Request Reader v2.0
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertResult
 * request and create a InsertResultRequest object for version 2.0
 * </p>
 *
 * <p>Copyright (c) 2014</p>
 * @author Alexandre Robin
 * @date Feb 2, 2014
 * @version 1.0
 */
public class InsertResultReaderV20 extends SWERequestReader<InsertResultRequest> implements SweEncodedMessageProcessor
{
    protected DataComponent resultStructure;
    protected DataEncoding resultEncoding;
    
    
    public InsertResultReaderV20()
	{
       
	}
    
    
    @Override
    public void setSweCommonStructure(DataComponent structure, DataEncoding encoding)
    {
        this.resultStructure = structure;
        this.resultEncoding = encoding;
    }


    @Override
    public InsertResultRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SOSException(noKVP + "SOS 2.0 InsertResult");
    }
    
    
	@Override
	public InsertResultRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertResultRequest request = new InsertResultRequest();
		
		if (resultStructure == null || resultEncoding == null)
		    throw new RuntimeException("Result structure and/or encoding are not properly set");
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
        // template
        String templateId = dom.getElementValue(requestElt, "template");
        request.setTemplateId(templateId);		
        
        // result values
        try
        {
            SWEData sweData = new SWEData();
            sweData.setElementType(resultStructure);
            sweData.setEncoding(resultEncoding);
            
            Element valuesElt = dom.getElement(requestElt, "resultValues");
            DataSourceDOM domSrc = new DataSourceDOM(dom, valuesElt);
            sweData.parseData(domSrc);
            
            request.setResultData(sweData);            
        }
        catch (IOException e)
        {
            throw new OWSException(OWSException.invalid_param_code, "resultValues", "Unable to read SWE Common data");
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    /**
     * Checks that InsertResult mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(InsertResultRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SOS);		
        
        // need offering
        if (request.getTemplateId() == null)
            report.add(new OWSException(OWSException.missing_param_code, "template"));
        
        // need result values
        if (request.getResultData() == null || request.getResultData().getComponentCount() == 0)
            report.add(new OWSException(OWSException.missing_param_code, "resultValues"));
        
        report.process();
    }

}