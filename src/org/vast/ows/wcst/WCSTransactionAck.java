/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcst;

import org.vast.ows.OWSResponse;
import org.vast.util.DateTime;


/**
 * <p><b>Title:</b><br/>
 * WCS Transaction Acknowledgment
 * </p>
 *
 * <p><b>Description:</b><br/>
 * 
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin <alexandre.robin@spotimage.fr>
 * @date 26 nov. 07
 * @version 1.0
 */
public class WCSTransactionAck extends OWSResponse
{
	protected DateTime timeStamp;
	protected WCSTransactionRequest request;


	public WCSTransactionAck()
	{
		service = "WCS";
        messageType = "TransactionAck";
	}
	
	
	public WCSTransactionAck(WCSTransactionRequest request)
	{
		this();
		this.request = request;
	}
	
	
	public DateTime getTimeStamp()
	{
		return timeStamp;
	}


	public void setTimeStamp(DateTime timeStamp)
	{
		this.timeStamp = timeStamp;
	}


	public WCSTransactionRequest getRequest()
	{
		return request;
	}


	public void setRequest(WCSTransactionRequest request)
	{
		this.request = request;
	}	
}
