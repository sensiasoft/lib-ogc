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

 Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import org.vast.ows.OWSResponse;
import net.opengis.sensorml.v20.AbstractProcess;


/**
 * <p>
 * Container for SWES DescribeSensor response data
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class DescribeSensorResponse extends OWSResponse
{
    protected String procedureDescriptionFormat = SWESUtils.DEFAULT_PROCEDURE_FORMAT;
    protected AbstractProcess procedureDescription;
    
        
    protected DescribeSensorResponse()
    {
        messageType = "DescribeSensorResponse";
    }


    public String getProcedureDescriptionFormat()
    {
        return procedureDescriptionFormat;
    }


    public void setProcedureDescriptionFormat(String procedureDescriptionFormat)
    {
        this.procedureDescriptionFormat = procedureDescriptionFormat;
    }


    public AbstractProcess getProcedureDescription()
    {
        return procedureDescription;
    }


    public void setProcedureDescription(AbstractProcess procedureDescription)
    {
        this.procedureDescription = procedureDescription;
    }
}
