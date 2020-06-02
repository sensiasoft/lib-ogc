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

import java.io.InputStream;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataComponent;
import org.vast.ows.OWSResponse;


/**
 * <p>
 * reader for WPS Describe Process responses with a input/output expressed in SWE Common.
 * </p>
 *
 * @author Gregoire Berthiau
 * @date Dec 15, 2008
 * */
public class ExecuteProcessResponse extends OWSResponse
{
	protected DataEncoding dataEncoding;
	protected DataComponent dataComponent;
	protected InputStream dataStream;

	
	public DataEncoding getDataEncoding() {
		return dataEncoding;
	}


	public void setDataEncoding(DataEncoding dataEncoding) {
		this.dataEncoding = dataEncoding;
	}


	public DataComponent getDataComponent() {
		return dataComponent;
	}


	public void setDataComponent(DataComponent dataComponent) {
		this.dataComponent = dataComponent;
	}
	
	
	public void setDataStream(InputStream dataStream) {
		this.dataStream = dataStream;
	}

	
	public InputStream getDataStream() {
		return dataStream;
	}

}
