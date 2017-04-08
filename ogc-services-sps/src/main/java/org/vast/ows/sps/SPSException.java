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
    Johannes Echterhoff <echterhoff@uni-muenster.de>
 	Alexandre Robin <alexandre.robin@spotimage.fr>
 	
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.ows.OWSException;


/**
 */
public class SPSException extends OWSException
{
	private static final long serialVersionUID = 8834638750689652806L;


	public SPSException(String message)
	{
		super(message);
	}
	
	
	public SPSException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	public SPSException(String code, String locator)
	{
		super(code, locator);
	}
    
    
    public SPSException(String code, String locator, Throwable cause)
    {
        super(code, locator, cause);
    }
	
	
	public SPSException(String code, String locator, String badValue)
    {
        super(code, locator, badValue, (String)null);
    }
	
	
	public SPSException(String code, String locator, String badValue, String message)
	{
		super(code, locator, badValue, message);
	}
	
	
	public SPSException(String code, String locator, String message, Exception e)
    {
        super(code, locator, message, e);
    }
}
