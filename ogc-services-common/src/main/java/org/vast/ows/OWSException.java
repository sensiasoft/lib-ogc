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

package org.vast.ows;


/**
 * <p>
 * Base exception class for all OGC Services.
 * Contains the fields necessary to generate an exception report.
 * </p>
 *
 * @author Alex Robin
 * @since 23 oct. 07
 * */
public class OWSException extends Exception
{
	private static final long serialVersionUID = 430944706383046157L;
    
	public final static String version_nego_failed_code = "VersionNegotiationFailed";
	public final static String version_nego_failed_text = "Version Negotiation Failed";
	public final static String invalid_request_code = "InvalidRequest";	
	public final static String invalid_request_text = "Invalid Request";
	public final static String invalid_param_code = "InvalidParameterValue";
	public final static String invalid_param_text = "Invalid Parameter Value: ";
	public final static String missing_param_code = "MissingParameterValue";
	public final static String missing_param_text = "Missing Parameter Value: ";
	public final static String unsupported_op_code = "OperationNotSupported";
	public final static String unsupported_op_text = "Unsupported Operation: ";
	public final static String no_code = "NoApplicableCode";
	public final static String no_code_text = "Internal Error";
	
	public final static String VERSION_10 = "1.0";
	public final static String VERSION_11 = "1.1.0";
	
	protected String code;
	protected String locator;
	protected String badValue;
	protected String version;
	protected String soapVersion;
	
	
	public OWSException(String message)
	{
		super(message);
	}
    
    
    public OWSException(String message, Throwable cause)
    {
        super(message, cause);
    }
	
	
	public OWSException(String code, String locator)
	{
		this.code = code;
		this.locator = locator;
	}
	
	
	public OWSException(String code, String locator, Throwable cause)
    {
        super(cause);
	    this.code = code;
        this.locator = locator;
    }
	
	
	public OWSException(String code, String locator, String message)
	{
		super(message);
		this.code = code;
		this.locator = locator;
	}
    
    
    public OWSException(String code, String locator, String message, Throwable cause)
    {
        super(message, cause);
        this.code = code;
        this.locator = locator;
    }
	
	
	public OWSException(String code, String locator, String badValue, String message)
	{
		this(code, locator, message);
		this.badValue = badValue;
	}
    
    
    public OWSException(String code, String locator, String badValue, String message, Throwable e)
    {
        this(code, locator, message, e);
        this.badValue = badValue;
    }
	
	
	public OWSException(String code, String locator, String message, Exception e)
	{
		super(message, e);
		this.code = code;
		this.locator = locator;
	}


	public String getCode()
	{
		return code;
	}


	public void setCode(String code)
	{
		this.code = code;
	}


	public String getLocator()
	{
		return locator;
	}


	public void setLocator(String locator)
	{
		this.locator = locator;
	}
	
	
	public String getBadValue()
	{
		return badValue;
	}


	public void setBadValue(String badValue)
	{
		this.badValue = badValue;
	}


	public String getVersion()
	{
		return version;
	}


	public void setVersion(String version)
	{
		this.version = version;
	}
	
	
	public String getSoapVersion()
    {
        return soapVersion;
    }


    public void setSoapVersion(String soapVersion)
    {
        this.soapVersion = soapVersion;
    }


    @Override
	public String getMessage()
	{
		String message = super.getMessage();
		
		// first try to use custom message if specified
		if (message != null && !message.equals(""))
			return message;
		
		// otherwise build generic message
		else if (this.code == version_nego_failed_code)
            return version_nego_failed_text;
		else if (this.code == invalid_request_code)
			return invalid_request_text;
		else if (this.code == missing_param_code)
			return missing_param_text + locator;
		else if (this.code == invalid_param_code)
			return invalid_param_text + locator + ((badValue == null) ? "" : "=" + badValue);
		else if (this.code == unsupported_op_code)
			return unsupported_op_text + locator;
		else if (this.code == no_code)
			return no_code_text;
		else
			return null;
	}
}
