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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wps;

import org.vast.cdm.common.CDMException;
import org.vast.ows.AbstractResponseReader;
import org.vast.ows.OWSException;
import org.vast.sweCommon.SWECommonUtils;
import org.vast.xml.*;
import org.w3c.dom.*;


/**
 * <p><b>Title:</b>
 * Describe Process Response Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * reader for WPS Describe Process responses with a input/output expressed in SWE Common.
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Gregoire Berthiau
 * @date Dec 15, 2008
 * @version 1.0
 */
public class DescribeProcessResponseReader  extends AbstractResponseReader<DescribeProcessResponse>
{

	
	public DescribeProcessResponse readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException 
	{
		
		DescribeProcessResponse response = new DescribeProcessResponse();
		try
		{
			SWECommonUtils utils = new SWECommonUtils();
			
            // read input
            Element inputObjElt = dom.getElement(responseElt, "input/*");
            
            Element inputEncElt = dom.getElement(inputObjElt, "encoding");
            response.inputDataEncoding = utils.readEncodingProperty(dom, inputEncElt);
            
            Element inputDefElt = dom.getElement(inputObjElt, "elementType");
            response.inputDataComponent = utils.readComponentProperty(dom, inputDefElt);

         // read output
            Element outputObjElt = dom.getElement(responseElt, "output/*");
            
            Element outputEncElt = dom.getElement(outputObjElt, "encoding");
            response.outputDataEncoding = utils.readEncodingProperty(dom, outputEncElt);
            
            Element outputDefElt = dom.getElement(outputObjElt, "elementType");
            response.outputDataComponent = utils.readComponentProperty(dom, outputDefElt);
		
		return null;
    		
		}
        catch (IllegalStateException e)
        {
            throw new OWSException("No reader found for SWECommon", e);
        }
		catch (CDMException e)
		{
			throw new OWSException(e.getMessage());
		}
	}
    
}
