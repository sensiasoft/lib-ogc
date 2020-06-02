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

package org.vast.ows.swe;

import java.util.LinkedHashSet;
import java.util.Set;
import org.vast.ows.OWSLayerCapabilities;


/**
 * <p>
 * SWES offering capabilities (base class for SOS and SP capabilities) 
 * </p>
 *
 * @author Alex Robin
 * @since Dec 13, 2014
 */
public abstract class SWESOfferingCapabilities extends OWSLayerCapabilities
{
    public static final String FORMAT_OM2 = "http://www.opengis.net/om/2.0";
    public static final String FORMAT_OM2_JSON = FORMAT_OM2.replaceAll("om", "om-json");
    public static final String FORMAT_SML1 = "http://www.opengis.net/sensorML/1.0.1";
    public static final String FORMAT_SML2 = "http://www.opengis.net/sensorml/2.0";
    public static final String FORMAT_SML2_JSON = FORMAT_SML2.replaceAll("sensorml", "sensorml-json");;

    protected Set<String> observableProperties;
    protected Set<String> procedures;
    protected Set<String> relatedFeatures;
    protected Set<String> procedureFormats;


    public SWESOfferingCapabilities()
    {
        super();
        observableProperties = new LinkedHashSet<>(10);
        procedures = new LinkedHashSet<>(2);
        relatedFeatures = new LinkedHashSet<>(2);        
        procedureFormats = new LinkedHashSet<>(2);        
    }


    public Set<String> getObservableProperties()
    {
        return observableProperties;
    }


    public Set<String> getProcedures()
    {
        return procedures;
    }
    
    
    public String getMainProcedure()
    {
        if (procedures.isEmpty())
            return null;
        
        return procedures.iterator().next();
    }


    public Set<String> getRelatedFeatures()
    {
        return relatedFeatures;
    }


    public Set<String> getProcedureFormats()
    {
        return procedureFormats;
    }

}