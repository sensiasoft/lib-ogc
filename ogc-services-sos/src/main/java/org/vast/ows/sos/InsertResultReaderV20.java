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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSUtils;
import org.vast.ows.SweEncodedMessageProcessor;
import org.vast.ows.swe.SWERequestReader;
import org.vast.swe.DataSourceDOM;
import org.vast.swe.DataSourceURI;
import org.vast.swe.SWEData;


/**
 * <p>
 * Provides methods to parse a KVP or SOAP/XML SOS InsertResult
 * request and create a InsertResultRequest object for version 2.0
 * </p>
 *
 * @author Alex Robin
 * @date Feb 2, 2014
 * */
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
        OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
        InsertResultRequest request = new InsertResultRequest();
        readCommonQueryArguments(queryParameters, request);
        
        // parse other params
        Iterator<Entry<String, String>> it = queryParameters.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, String> item = it.next();
            String argName = item.getKey();
            String argValue = item.getValue();
            
            // template argument
            if (argName.equalsIgnoreCase("template"))
                request.setTemplateId(argValue);
            
            // vendor parameters
            else
            {
                if (argValue == null)
                    argValue = "";
                addKVPExtension(argName, argValue, request);
            }
        }        
        
        request.setResultDataSource(new DataSourceURI("POST"));
        this.checkParameters(request, report);
        return request;
    }
    
    
	@Override
	public InsertResultRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		InsertResultRequest request = new InsertResultRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
        // template
        String templateId = dom.getElementValue(requestElt, "template");
        request.setTemplateId(templateId);
        
        // result values
        try
        {
            Element valuesElt = dom.getElement(requestElt, "resultValues");
            DataSourceDOM domSrc = new DataSourceDOM(dom, valuesElt);            
            
            // if data structure and encoding are set, we can parse now
            if (resultStructure != null && resultEncoding != null)
            {
                SWEData sweData = new SWEData();
                sweData.setElementType(resultStructure);
                sweData.setEncoding(resultEncoding);
                sweData.parseData(domSrc);
                request.setResultData(sweData);
            }
            else
                request.setResultDataSource(domSrc);
        }
        catch (IOException e)
        {
            throw new SOSException(OWSException.invalid_param_code, "resultValues", e);
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
        
        // need template ID
        if (request.getTemplateId() == null)
            report.add(new OWSException(OWSException.missing_param_code, "template"));
        
        // need result values
        if (request.getResultData() == null && request.getResultDataSource() == null)
            report.add(new OWSException(OWSException.missing_param_code, "resultValues"));
        
        report.process();
    }

}