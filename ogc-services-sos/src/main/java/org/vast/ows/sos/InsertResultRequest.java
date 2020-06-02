/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sos;

import org.vast.cdm.common.DataSource;
import org.vast.ows.OWSRequest;
import org.vast.swe.SWEData;


/**
 * <p>
 * Container for SOS InsertResult request parameters
 * </p>
 *
 * @author Alex Robin
 * @date Feb 02, 2014
 * */
public class InsertResultRequest extends OWSRequest
{
    protected String templateId;
    protected DataSource resultDataSource;
    protected SWEData resultData;
    
	
	public InsertResultRequest()
	{
		service = "SOS";
		operation = "InsertResult";
	}


    public String getTemplateId()
    {
        return templateId;
    }


    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }


    public DataSource getResultDataSource()
    {
        return resultDataSource;
    }


    public void setResultDataSource(DataSource resultDataSource)
    {
        this.resultDataSource = resultDataSource;
    }


    public SWEData getResultData()
    {
        return resultData;
    }


    public void setResultData(SWEData resultData)
    {
        this.resultData = resultData;
    }    
}
