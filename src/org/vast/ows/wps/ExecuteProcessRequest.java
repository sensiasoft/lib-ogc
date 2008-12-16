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

import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.ows.OWSRequest;



/**
 * <p><b>Title:</b><br/>
 * GetResult Request
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Container for WPS ExecuteProcess request parameters
 * </p>
 *
 * <p>Copyright (c) 2008</p>
 * @author Gregoire Berthiau
 * @date Dec 15, 2008
 * @version 1.0
 */

public class ExecuteProcessRequest extends OWSRequest
{
    protected String offering;
    protected DataComponent inputDataComponent;
	protected DataEncoding inputDataEncoding;
 
	
	public ExecuteProcessRequest()
	{
		service = "WPS";
		operation = "ExecuteProcess";
	}
	
	
	public ExecuteProcessRequest(DataComponent inputDataComponent, DataEncoding inputDataEncoding)
	{
		service = "WPS";
		operation = "ExecuteProcess";
		this.inputDataEncoding = inputDataEncoding;
		this.inputDataComponent = inputDataComponent;
	}


	public DataComponent getInputDataComponent() {
		return inputDataComponent;
	}


	public void setInputDataComponent(DataComponent inputDataComponent) {
		this.inputDataComponent = inputDataComponent;
	}


	public DataEncoding getInputDataEncoding() {
		return inputDataEncoding;
	}


	public void setInputDataEncoding(DataEncoding inputDataEncoding) {
		this.inputDataEncoding = inputDataEncoding;
	}


	
}
