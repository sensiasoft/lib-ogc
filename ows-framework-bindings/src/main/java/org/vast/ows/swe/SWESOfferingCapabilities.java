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

import java.util.List;
import org.vast.ows.OWSLayerCapabilities;


/**
 * <p>
 * SWES offering capabilities (base class for SOS and SP capabilities) 
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Dec 13, 2014
 */
public abstract class SWESOfferingCapabilities extends OWSLayerCapabilities
{
    public static String FORMAT_OM2 = "http://www.opengis.net/om/2.0";
    public static String FORMAT_SML1 = "http://www.opengis.net/sensorML/1.0.1";
    public static String FORMAT_SML2 = "http://www.opengis.net/sensorml/2.0";

    protected List<String> observableProperties;
    protected List<String> procedures;
    protected List<String> relatedFeatures;
    protected List<String> procedureFormats;


    public SWESOfferingCapabilities()
    {
        super();
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


    public List<String> getRelatedFeatures()
    {
        return relatedFeatures;
    }


    public void setRelatedFeatures(List<String> relatedFeatures)
    {
        this.relatedFeatures = relatedFeatures;
    }


    public List<String> getProcedureFormats()
    {
        return procedureFormats;
    }


    public void setProcedureFormats(List<String> procedureFormats)
    {
        this.procedureFormats = procedureFormats;
    }

}