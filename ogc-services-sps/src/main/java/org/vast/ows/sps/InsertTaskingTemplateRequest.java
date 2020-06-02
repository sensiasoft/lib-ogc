/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2016-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sps;

import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.DataComponent;
import org.vast.ows.OWSRequest;


/**
 * <p>
 * Container for SPS InsertTaskingTemplate request parameters
 * </p>
 *
 * @author Alex Robin
 * @date Dec 14, 2016
 * */
public class InsertTaskingTemplateRequest extends OWSRequest
{
    protected String procedureID;
    protected DataComponent taskingParameters;
    protected DataEncoding encoding;
    
	
	public InsertTaskingTemplateRequest()
	{
		service = SPSUtils.SPS;
		operation = "InsertTaskingTemplate";
	}


    public String getProcedureID()
    {
        return procedureID;
    }


    public void setProcedureID(String offering)
    {
        this.procedureID = offering;
    }


    public DataComponent getTaskingParameters()
    {
        return taskingParameters;
    }


    public void setTaskingParameters(DataComponent resultStructure)
    {
        this.taskingParameters = resultStructure;
    }


    public DataEncoding getEncoding()
    {
        return encoding;
    }


    public void setEncoding(DataEncoding encoding)
    {
        this.encoding = encoding;
    }
    
}
