/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows;

import java.io.InputStream;

import org.vast.io.xml.*;


public class OWSExceptionReader
{
	
	public static void checkException(DOMReader dom) throws OWSException
	{
		String exceptionText = null;

		if (exceptionText == null)
            exceptionText = dom.getElementValue("Exception");
		
        if (exceptionText == null)
            exceptionText = dom.getElementValue("ServiceException");
		
		if (exceptionText == null)
            exceptionText = dom.getElementValue("ServiceException/Locator");

        if (exceptionText == null)
            exceptionText = dom.getAttributeValue("ServiceException/code");
		
        if (exceptionText != null)
        	throw new OWSException("ServiceException: " + exceptionText);
	}
	
	
	public void parseException(InputStream in) throws OWSException
	{
		try
        {
            DOMReader dom = new DOMReader(in, false);
            checkException(dom);
        }
        catch (DOMReaderException e)
        {
            e.printStackTrace();
        }
        
		return;
	}
}
