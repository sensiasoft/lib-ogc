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

import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataComponent;
import org.vast.ows.OWSResponse;


/**
 * <p>
 * container for WPS Describe Process responses with a input/output expressed in SWE Common.
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Dec 15, 2008
 * */
public class DescribeProcessResponse extends OWSResponse
{
	protected DataEncoding outputDataEncoding;
	protected DataComponent inputDataComponent;
	protected DataEncoding inputDataEncoding;
	protected DataComponent outputDataComponent;       
 
	
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
