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

package org.vast.ows.wms;

import org.vast.ows.OWSException;


/**
 * <p>
 * Exception object to carry WMS error messages.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 07 jul. 06
 * */
public class WMSException extends OWSException
{
	static final long serialVersionUID = 0x0D57D045A3588AE34L;
	public final static String invalid_format_code = "InvalidFormat";
	public final static String invalid_format_text = "Invalid Format: ";
	public final static String invalid_srs_code = "InvalidSRS";
	public final static String invalid_srs_text = "Invalid SRS: ";
	public final static String invalid_layer_code = "LayerNotDefined";
	public final static String invalid_layer_text = "Unknown Layer: ";
	public final static String invalid_style_code = "StyleNotDefined";
	public final static String invalid_style_text = "Unknown Style: ";
	
	
	public WMSException(String message)
	{
		super(message);
	}
	
	
	public WMSException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	public WMSException(String code, String locator)
	{
		super(code, locator, (String)null, (String)null);
	}
	
	
	public WMSException(String code, String locator, String badValue, String message)
	{
		super(code, locator, badValue, message);
	}
	
	
	@Override
	public String getMessage()
	{
		String message = super.getMessage();
		
		// first try to use OWS message if specified
		if (message != null && !message.equals(""))
			return message;
		
		// otherwise build generic message
		else if (this.code == invalid_format_code)
			return invalid_format_text + ((badValue == null) ? "" : badValue);
		else if (this.code == invalid_srs_code)
			return invalid_srs_text + ((badValue == null) ? "" : badValue);
		else if (this.code == invalid_layer_code)
			return invalid_layer_text + ((badValue == null) ? "" : badValue);
		else if (this.code == invalid_style_code)
			return invalid_style_text + ((badValue == null) ? "" : badValue);
		else
			return null;
	}
}
