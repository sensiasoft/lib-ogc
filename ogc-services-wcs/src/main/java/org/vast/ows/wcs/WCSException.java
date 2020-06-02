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
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.ows.OWSException;


/**
 * <p>
 * Exception object to carry WCS error messages.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 07 jul. 06
 * */
public class WCSException extends OWSException
{
	static final long serialVersionUID = 0xED126190670F4F6AL;
	public final static String invalid_format_code = "InvalidFormat";
	public final static String invalid_format_text = "Invalid Format: ";
	public final static String invalid_coverage_code = "CoverageNotDefined";
	public final static String invalid_coverage_text = "Invalid Coverage ID: ";
	
	
	public WCSException(String message)
	{
		super(message);
	}
	
	
	public WCSException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	public WCSException(String code, String locator)
	{
		this(code, locator, null, null);
	}
	
	
	public WCSException(String code, String locator, String badValue, String message)
	{
		super(code, locator, badValue, message);
		
		if (code == invalid_coverage_code)
			locator = "Coverage";
		else if (code == invalid_format_code)
			locator = "Format";
	}
	
	
	@Override
	public String getMessage()
	{
		// otherwise build generic message
		if (this.code == invalid_coverage_code)
			return invalid_coverage_text + ((badValue == null) ? "" : badValue);
		else if (this.code == invalid_format_code)
			return invalid_format_text + ((badValue == null) ? "" : badValue);
		else
			return super.getMessage();
	}
}
