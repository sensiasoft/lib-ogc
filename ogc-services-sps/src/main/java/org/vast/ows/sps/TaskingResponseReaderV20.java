/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import org.vast.ows.OWSException;
import org.vast.ows.swe.SWEResponseReader;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p>
 * Used to read a version 2.0 SPS tasking responses into
 * the corresponding object
 * </p>
 *
 * @author Alex Robin
 * @date Feb, 29 2008
 * */
public class TaskingResponseReaderV20 extends SWEResponseReader<TaskingResponse<?>>
{
	protected SPSCommonReaderV20 commonReader = new SPSCommonReaderV20();
	
	
	@Override
	public TaskingResponse<StatusReport> readXMLResponse(DOMHelper dom, Element responseElt) throws OWSException
	{
		TaskingResponse<StatusReport> response = null;			
		String respName = responseElt.getLocalName();
		String className = getClass().getPackage().getName() + "." + respName;
		
		try
        {
		    Class<?> respClass = Class.forName(className);
		    response = (TaskingResponse<StatusReport>)respClass.newInstance();
            response.setVersion("2.0");
            
            // status or feasibility report
            Element reportElt = dom.getElement(responseElt, "result/*");	            
            if (reportElt != null)
            {
                StatusReport report = (StatusReport)commonReader.readReport(dom, reportElt);
                response.setReport(report);
            }
            
            // read extensions
            readXMLExtensions(dom, responseElt, response);
            
            return response;
        }
        catch (ClassNotFoundException e)
        {
            // problem if class is not found in class path
            throw new IllegalStateException("Error while instantiating SPS response Class " + className, e);
        }
		catch (Exception e)
        {
            throw new SPSException(READ_ERROR_MSG + response.getMessageType(), e);
        }
	}	
}
