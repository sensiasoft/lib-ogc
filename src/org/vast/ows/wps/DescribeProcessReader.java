/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import java.util.StringTokenizer;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.vast.ows.*;


/**
 * <p><b>Title:</b><br/>
 * WPS Describe Request Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Provides methods to parse a KVP or XML WPS DescribeProcess
 * request and create a DescribeProcessRequest
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Gregoire Berthiau
 * @date Dec 2, 2008
 * @version 1.0
 */
public class DescribeProcessReader extends AbstractRequestReader<DescribeProcessRequest>
{
    
    public DescribeProcessReader()
	{ 
	}

	@Override
	public DescribeProcessRequest readURLQuery(String queryString) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		DescribeProcessRequest request = new DescribeProcessRequest();
		StringTokenizer st = new StringTokenizer(queryString, "&");
		
		while (st.hasMoreTokens())
		{
			String argName = null;
			String argValue = null;
			String nextArg = st.nextToken();

			// separate argument name and value
			try
			{
				int sepIndex = nextArg.indexOf('=');
				argName = nextArg.substring(0, sepIndex);
				argValue = nextArg.substring(sepIndex + 1);
			}
			catch (IndexOutOfBoundsException e)
			{
				throw new WPSException(invalidKVP);
			}
			
			// service ID
			if (argName.equalsIgnoreCase("service"))
			{
				request.setService(argValue);
			}
			
			// service version
			else if (argName.equalsIgnoreCase("version"))
			{
				request.setVersion(argValue);
			}

			// request argument
			else if (argName.equalsIgnoreCase("request"))
			{
				request.setOperation(argValue);
			}

			// offering argument
			else if (argName.equalsIgnoreCase("offering"))
			{
				request.setOffering(argValue);
			}
			
			// requestFormat argument
			else if (argName.equalsIgnoreCase("requestFormat"))
			{
				request.setRequestFormat(argValue);
			}
			
			else
				throw new WPSException(invalidKVP + ": Unknown Argument " + argName);
		}

		checkParameters(request, report);
		return request;
	}
	
	
	@Override
	public DescribeProcessRequest readXMLQuery(DOMHelper dom, Element requestElt) throws OWSException
	{
		OWSExceptionReport report = new OWSExceptionReport(OWSException.VERSION_11);
		DescribeProcessRequest request = new DescribeProcessRequest();
		
		// do common stuffs like version, request name and service type
		readCommonXML(dom, requestElt, request);
		
		// offering
		String offering = dom.getElementValue(requestElt, "offering");
		request.setOffering(offering);

		// offering
		String requestFormat = dom.getElementValue(requestElt, "requestFormat");
		request.setRequestFormat(requestFormat);
		
        checkParameters(request, report);
        return request;
	}
	
	
    /**
     * Checks that GetResult mandatory parameters are present
     * @param request
     * @throws OWSException
     */
    protected void checkParameters(DescribeProcessRequest request, OWSExceptionReport report) throws OWSException
    {
    	// check common params
		super.checkParameters(request, report, OWSUtils.WPS);
    	
    	// need offering
		if (request.getOffering() == null)
			report.add(new OWSException(OWSException.missing_param_code, "OFFERING"));

		report.process();
    }
}