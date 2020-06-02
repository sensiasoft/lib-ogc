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
 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.swe;

import java.util.ArrayList;
import java.util.List;
import net.opengis.sensorml.v20.AbstractProcess;
import org.vast.ogc.gml.FeatureRef;
import org.vast.ows.OWSRequest;


/**
 * <p>
 * Container for SWES InsertSensor request parameters
 * </p>
 *
 * @author Alex Robin
 * @date Feb 02, 2014
 * */
public class InsertSensorRequest extends OWSRequest
{
    public final static String DEFAULT_FEATURE_ROLE = "http://www.opengis.net/def/nil/OGC/0/unknown";
    
    protected String procedureDescriptionFormat = SWESUtils.DEFAULT_PROCEDURE_FORMAT;
    protected AbstractProcess procedureDescription;
    protected List<String> observableProperties;
    protected List<FeatureRef> relatedFeatures;    
    
	
	public InsertSensorRequest(String serviceType)
    {
        service = serviceType;
		operation = "InsertSensor";
		observableProperties = new ArrayList<String>(5);
		relatedFeatures = new ArrayList<FeatureRef>(2);
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


    public List<String> getObservableProperties()
    {
        return observableProperties;
    }


    public void setObservableProperties(List<String> observableProperties)
    {
        this.observableProperties = observableProperties;
    }


    public List<FeatureRef> getRelatedFeatures()
    {
        return relatedFeatures;
    }


    public void setRelatedFeatures(List<FeatureRef> relatedFeatures)
    {
        this.relatedFeatures = relatedFeatures;
    }
}
