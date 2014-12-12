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
import org.vast.ows.swe.SWESOfferingCapabilities;
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
public class SOSOfferingCapabilities extends SWESOfferingCapabilities
{
    protected List<Bbox> observedAreas;
    protected List<TimeExtent> phenomenonTimes;
    protected List<TimeExtent> resultTimes;    
    protected List<String> responseFormats;
    protected List<String> supportedEncodings;
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
        return (SOSServiceCapabilities) super.getParent();
    }
}
