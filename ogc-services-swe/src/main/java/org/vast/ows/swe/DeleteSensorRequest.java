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
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import org.vast.ows.OWSRequest;


/**
 * <p>
 * Container for SWES DeleteSensor request parameters
 * </p>
 *
 * @author Alex Robin
 * @date Feb 02, 2014
 * */
public class DeleteSensorRequest extends OWSRequest
{
    protected String procedureId;
    
	
    protected DeleteSensorRequest()
    {
        operation = "DeleteSensor";
    }
    
    
	public DeleteSensorRequest(String serviceType)
    {
        this();
	    service = serviceType;
	}


    public String getProcedureId()
    {
        return procedureId;
    }


    public void setProcedureId(String procedureId)
    {
        this.procedureId = procedureId;
    }    
}
