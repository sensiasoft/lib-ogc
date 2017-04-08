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

import org.vast.ows.OWSException;


/**
 * <p>
 * Exception object to carry WPS error messages.
 * </p>
 *
 * @author Grergoire Berthiau
 * @date Nov 29, 2008
 * */
public class WPSException extends OWSException
{
	static final long serialVersionUID = 0x59DB55493721FA3CL;
		
	
	public WPSException(String message)
	{
		super(message);
	}
	
	
	public WPSException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	public WPSException(String code, String locator, String badValue, String message)
	{
		super(code, locator, badValue, message);
	}
}
