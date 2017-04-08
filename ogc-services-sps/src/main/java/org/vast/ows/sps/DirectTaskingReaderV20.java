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
import net.opengis.swe.v20.DataEncoding;
import org.vast.xml.DOMHelper;
import org.vast.xml.XMLReaderException;
import org.w3c.dom.Element;
import org.vast.ows.OWSException;
import org.vast.ows.OWSExceptionReport;
import org.vast.ows.OWSUtils;
import org.vast.ows.swe.SWERequestReader;
import org.vast.swe.SWEUtils;
import org.vast.util.DateTime;


/**
 * <p>
 * Provides methods to parse a SOAP/XML SPS DirectTasking request
 * </p>
 *
 * @author Alex Robin
 * @date Jan 24, 2017
 * */
public class DirectTaskingReaderV20 extends SWERequestReader<DirectTaskingRequest>
{
    protected SWEUtils sweUtils = new SWEUtils(SWEUtils.V2_0);
    
    
    public DirectTaskingReaderV20()
	{       
	}


    @Override
    public DirectTaskingRequest readURLParameters(Map<String, String> queryParameters) throws OWSException
    {
        throw new SPSException(noKVP + "SPS 2.0 DirectTasking");
    }
    
    
	@Override
	public DirectTaskingRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		DirectTaskingRequest request = new DirectTaskingRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		        
        // procedure
		String sensorID = dom.getElementValue(requestElt, "procedure");
        request.setProcedureID(sensorID);
        
        // time slot
        String timeRange = dom.getElementValue(requestElt, "timeSlot");
        try
        {
            if (timeRange != null)
                request.setTimeSlot(parseTimeArg(timeRange));
        }
        catch (Exception e)
        {
            String code = SPSException.invalid_param_code;
            String locator = "timeSlot";
            throw new SPSException(code, locator, invalidValue + locator + ": " + timeRange, e);
        }
        
        // encoding
        try
        {
            Element resultEncodingElt = dom.getElement(requestElt, "encoding/*");
            DataEncoding encoding = sweUtils.readEncoding(dom, resultEncodingElt);
            request.setEncoding(encoding);
        }
        catch (XMLReaderException e)
        {
            throw new SPSException(OWSException.invalid_param_code, "encoding", e);
        }
        
        // latest response time
        String isoDate = dom.getElementValue(requestElt, "latestResponseTime");
        try
        {
            if (isoDate != null)
            {
                DateTime latestResponseTime = new DateTime(timeFormat.parseIso(isoDate));
                request.setLatestResponseTime(latestResponseTime);
            }
        }
        catch (Exception e)
        {
            String code = SPSException.invalid_param_code;
            String locator = "latestResponseTime";
            throw new SPSException(code, locator, invalidValue + locator + ": " + isoDate, e);
        }
        
        this.checkParameters(request, report);
        return request;
	}
    
    
    protected void checkParameters(DirectTaskingRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.SPS);		
        
        // need procedure
        if (request.getProcedureID() == null)
            report.add(new OWSException(OWSException.missing_param_code, "procedure"));
        
        // need time slot
        if (request.getTimeSlot() == null)
            report.add(new OWSException(OWSException.missing_param_code, "timeSlot"));
        
        // need encoding
        if (request.getEncoding() == null)
            report.add(new OWSException(OWSException.missing_param_code, "encoding"));
        
        report.process();
    }

}