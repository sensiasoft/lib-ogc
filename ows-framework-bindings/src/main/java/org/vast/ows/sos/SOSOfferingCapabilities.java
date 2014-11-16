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

package org.vast.ows.sos;

import java.util.*;
import org.vast.ows.OWSLayerCapabilities;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Contains SOS layer capabilities like available formats,
 * observables, procedures...
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Oct 27, 2005
 * @version 1.0
 */
public class SOSOfferingCapabilities extends OWSLayerCapabilities
{
    public static String FORMAT_OM2 = "http://www.opengis.net/om/2.0";
    public static String FORMAT_SML1 = "http://www.opengis.net/sensorML/1.0.1";
    public static String FORMAT_SML2 = "http://www.opengis.net/sensorml/2.0";
    
    protected List<String> observableProperties;
    protected List<String> procedures;
    protected List<Bbox> observedAreas;
    protected List<TimeExtent> phenomenonTimes;
    protected List<TimeExtent> resultTimes;    
    protected List<String> relatedFeatures;
    
    protected List<String> responseFormats;
    protected List<String> supportedEncodings;
    protected List<String> procedureFormats;    
    protected List<String> observationTypes;
    protected List<String> foiTypes;
    

    public SOSOfferingCapabilities()
    {
        observableProperties = new ArrayList<String>(10);
        procedures = new ArrayList<String>(2);
        observedAreas = new ArrayList<Bbox>(2);
        phenomenonTimes = new ArrayList<TimeExtent>(2);
        resultTimes = new ArrayList<TimeExtent>(2);
        relatedFeatures = new ArrayList<String>(2);        
        responseFormats = new ArrayList<String>(2);
    	procedureFormats = new ArrayList<String>(2);
    	observationTypes = new ArrayList<String>(2);
    	foiTypes = new ArrayList<String>(2);
    }


    public List<String> getObservableProperties()
    {
        return observableProperties;
    }


    public void setObservableProperties(List<String> observableProperties)
    {
        this.observableProperties = observableProperties;
    }


    public List<String> getProcedures()
    {
        return procedures;
    }


    public void setProcedures(List<String> procedures)
    {
        this.procedures = procedures;
    }


    public List<Bbox> getObservedAreas()
    {
        return observedAreas;
    }


    public void setObservedAreas(List<Bbox> observedAreas)
    {
        this.observedAreas = observedAreas;
    }


    public List<TimeExtent> getPhenomenonTimes()
    {
        return phenomenonTimes;
    }


    public void setPhenomenonTimes(List<TimeExtent> phenomenonTimes)
    {
        this.phenomenonTimes = phenomenonTimes;
    }


    public List<TimeExtent> getResultTimes()
    {
        return resultTimes;
    }


    public void setResultTimes(List<TimeExtent> resultTimes)
    {
        this.resultTimes = resultTimes;
    }


    public List<String> getRelatedFeatures()
    {
        return relatedFeatures;
    }


    public void setRelatedFeatures(List<String> relatedFeatures)
    {
        this.relatedFeatures = relatedFeatures;
    }
    
    
    public List<String> getResponseFormats()
    {
        return responseFormats;
    }


    public void setResponseFormats(List<String> responseFormats)
    {
        this.responseFormats = responseFormats;
    }


    public List<String> getSupportedEncodings()
    {
        return supportedEncodings;
    }


    public void setSupportedEncodings(List<String> supportedEncodings)
    {
        this.supportedEncodings = supportedEncodings;
    }


    public List<String> getProcedureFormats()
    {
        return procedureFormats;
    }


    public void setProcedureFormats(List<String> procedureFormats)
    {
        this.procedureFormats = procedureFormats;
    }
    
    
    public List<String> getObservationTypes()
    {
        return observationTypes;
    }


    public void setObservationTypes(List<String> observationTypes)
    {
        this.observationTypes = observationTypes;
    }
    
    
    public List<String> getFoiTypes()
    {
        return foiTypes;
    }


    public void setFoiTypes(List<String> foiTypes)
    {
        this.foiTypes = foiTypes;
    }


    @Override
    public SOSServiceCapabilities getParent()
    {
        return (SOSServiceCapabilities)super.getParent();
    }
	
}
