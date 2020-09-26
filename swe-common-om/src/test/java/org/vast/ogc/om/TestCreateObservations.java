/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.time.Instant;
import org.junit.Test;
import org.vast.ogc.def.DefinitionRef;
import org.vast.swe.SWEConstants;
import org.vast.swe.SWEHelper;
import org.vast.swe.helper.GeoPosHelper;
import org.vast.swe.helper.RasterHelper;
import org.vast.util.TimeExtent;
import net.opengis.swe.v20.DataType;


public class TestCreateObservations
{
    OMUtils omUtils = new OMUtils(OMUtils.V2_0);
    GeoPosHelper geoHelper = new GeoPosHelper();
    RasterHelper imgHelper = new RasterHelper();
    
    
    @Test
    public void testCreateScalarObs() throws Exception
    {
    	ObservationImpl obs = new ObservationImpl();
        obs.setPhenomenonTime(TimeExtent.currentTime());
        obs.setResultTime(Instant.now());
        obs.setProcedure(new ProcedureRef("urn:ogc:sensor:001"));
        obs.setObservedProperty(new DefinitionRef(SWEHelper.getPropertyUri("Temperature")));
        obs.setResult(geoHelper.createQuantity()
                .definition(SWEHelper.getPropertyUri("Temperature"))
                .uomCode("Cel")
                .label("Air Temperature")
                .value(23.5)
                .build());
        
        omUtils.writeObservation(System.out, obs, OMUtils.V2_0);
    }
    
    
    @Test
    public void testCreateComplexObs() throws Exception
    {
    	ObservationImpl obs = new ObservationImpl();
    	obs.setPhenomenonTime(TimeExtent.currentTime());
        obs.setResultTime(Instant.now());
        obs.setProcedure(new ProcedureRef("urn:ogc:sensor:001"));
        obs.setObservedProperty(new DefinitionRef(SWEConstants.DEF_PLATFORM_LOC));
        obs.setResult(geoHelper.newLocationVectorLatLon(SWEConstants.DEF_PLATFORM_LOC));
        obs.getResult().assignNewDataBlock();
        obs.getResult().getData().setDoubleValue(0, 45.6);
        obs.getResult().getData().setDoubleValue(1, 2.3);
        
        omUtils.writeObservation(System.out, obs, OMUtils.V2_0);
    }
    
    
    @Test
    public void testCreateArrayObs() throws Exception
    {
    	ObservationImpl obs = new ObservationImpl();        
    	obs.setPhenomenonTime(TimeExtent.currentTime());
        obs.setResultTime(Instant.now());
        obs.setProcedure(new ProcedureRef("urn:ogc:sensor:001"));
        obs.setObservedProperty(new DefinitionRef(SWEHelper.getPropertyUri("RasterImage")));
        obs.setResult(imgHelper.newGrayscaleImage(10, 10, DataType.BYTE));
        obs.getResult().assignNewDataBlock();
        
        omUtils.writeObservation(System.out, obs, OMUtils.V2_0);
    }

}
