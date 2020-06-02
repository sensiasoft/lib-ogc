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
 * @author Alex Robin
 * @since Oct 27, 2005
 * */
public class SOSOfferingCapabilities extends SWESOfferingCapabilities
{
    protected List<Bbox> observedAreas;
    protected TimeExtent phenomenonTime;
    protected TimeExtent resultTime;    
    protected Set<String> responseFormats;
    protected Set<String> supportedEncodings;
    protected Set<String> observationTypes;
    protected Set<String> foiTypes;
    

    public SOSOfferingCapabilities()
    {
        observedAreas = new ArrayList<Bbox>(2);
        responseFormats = new LinkedHashSet<String>(2);
    	observationTypes = new LinkedHashSet<String>(2);
    	foiTypes = new LinkedHashSet<String>(2);
    }


    public List<Bbox> getObservedAreas()
    {
        return observedAreas;
    }


    public void setObservedAreas(List<Bbox> observedAreas)
    {
        this.observedAreas = observedAreas;
    }


    public TimeExtent getPhenomenonTime()
    {
        return phenomenonTime;
    }


    public void setPhenomenonTime(TimeExtent phenomenonTime)
    {
        this.phenomenonTime = phenomenonTime;
    }


    public TimeExtent getResultTime()
    {
        return resultTime;
    }


    public void setResultTime(TimeExtent resultTime)
    {
        this.resultTime = resultTime;
    }


    public Set<String> getResponseFormats()
    {
        return responseFormats;
    }


    public Set<String> getSupportedEncodings()
    {
        return supportedEncodings;
    }


    public Set<String> getObservationTypes()
    {
        return observationTypes;
    }
    
    
    public Set<String> getFoiTypes()
    {
        return foiTypes;
    }
	
    
    @Override
    public SOSServiceCapabilities getParent()
    {
        return (SOSServiceCapabilities) super.getParent();
    }
}
