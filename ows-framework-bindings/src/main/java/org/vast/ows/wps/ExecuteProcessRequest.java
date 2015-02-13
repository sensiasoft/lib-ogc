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

import javax.xml.soap.AttachmentPart;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataComponent;
import org.vast.ows.OWSRequest;


/**
 * <p>
 * Container for WPS ExecuteProcess request parameters
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Dec 15, 2008
 * */

public class ExecuteProcessRequest extends OWSRequest
{
    protected String offering, requestFormat;
    protected DataComponent inputDataComponent;
	protected DataEncoding inputDataEncoding;
	protected AttachmentPart attachmentPart;
	
	
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

	
    public String getOffering() {
		return offering;
	}


	public void setOffering(String offering) {
		this.offering = offering;
	}


	public AttachmentPart getAttachmentPart() {
		return attachmentPart;
	}


	public void setAttachmentPart(AttachmentPart attachmentPart) {
		this.attachmentPart = attachmentPart;
	}


	public String getRequestFormat() {
		return requestFormat;
	}


	public void setRequestFormat(String requestFormat) {
		this.requestFormat = requestFormat;
	}

}
