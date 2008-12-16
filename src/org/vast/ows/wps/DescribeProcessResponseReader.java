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

import java.io.*;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataHandler;
import org.vast.ogc.OGCException;
import org.vast.ogc.OGCExceptionReader;
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
public class DescribeProcessResponseReader
{
	
	protected DataHandler dataHandler;
	protected DataEncoding outputDataEncoding;
	protected DataComponent inputDataComponent;
	protected DataEncoding inputDataEncoding;
	protected DataComponent outputDataComponent;       
    
    public void parse(InputStream inputStream, DataHandler handler) throws CDMException
    {
		try
		{
		    dataHandler = handler;
			
			// parse xml header using DOMReader
			DOMHelper dom = new DOMHelper(inputStream, false);
			OGCExceptionReader.checkException(dom);
			
			// find first Observation element
			Element rootElement = dom.getRootElement();
			NodeList elts = rootElement.getOwnerDocument().getElementsByTagNameNS("http://www.opengis.net/wps/2.0", "ProcessDescription");
			Element obsElt = (Element)elts.item(0);	
			if (obsElt == null)
				throw new CDMException("XML Response doesn't contain any Process Description");

			SWECommonUtils utils = new SWECommonUtils();
			
            // read input
            Element inputObjElt = dom.getElement(obsElt, "input/*");
            
            Element inputEncElt = dom.getElement(inputObjElt, "encoding");
            this.inputDataEncoding = utils.readEncodingProperty(dom, inputEncElt);
            
            Element inputDefElt = dom.getElement(inputObjElt, "elementType");
            this.inputDataComponent = utils.readComponentProperty(dom, inputDefElt);

         // read output
            Element outputObjElt = dom.getElement(obsElt, "output/*");
            
            Element outputEncElt = dom.getElement(outputObjElt, "encoding");
            this.outputDataEncoding = utils.readEncodingProperty(dom, outputEncElt);
            
            Element outputDefElt = dom.getElement(outputObjElt, "elementType");
            this.outputDataComponent = utils.readComponentProperty(dom, outputDefElt);
                
    		
		}
        catch (IllegalStateException e)
        {
            throw new CDMException("No reader found for SWECommon", e);
        }
		catch (DOMHelperException e)
		{
			throw new CDMException("Error while parsing Observation XML", e);
		}		
		catch (OGCException e)
		{
			throw new CDMException(e.getMessage());
		}
	}

    
	public DataEncoding getOutputDataEncoding() {
		return outputDataEncoding;
	}


	public void setOutputDataEncoding(DataEncoding outputDataEncoding) {
		this.outputDataEncoding = outputDataEncoding;
	}


	public DataComponent getInputDataComponent() {
		return inputDataComponent;
	}


	public void setInputDataComponents(DataComponent inputDataComponent) {
		this.inputDataComponent = inputDataComponent;
	}


	public DataEncoding getInputDataEncoding() {
		return inputDataEncoding;
	}


	public void setInputDataEncoding(DataEncoding inputDataEncoding) {
		this.inputDataEncoding = inputDataEncoding;
	}


	public DataComponent getOutputDataComponent() {
		return outputDataComponent;
	}


	public void setOutputDataComponents(DataComponent outputDataComponent) {
		this.outputDataComponent = outputDataComponent;
	}
    
}
