/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Conformance;
import net.opengis.fes.v20.FilterCapabilities;
import net.opengis.fes.v20.ScalarCapabilities;
import net.opengis.fes.v20.SpatialCapabilities;
import net.opengis.fes.v20.TemporalCapabilities;


public class FilterCapabilitiesImpl implements FilterCapabilities
{
    protected Conformance conformance = new ConformanceImpl();
    protected ScalarCapabilities scalarCapabilities;
    protected SpatialCapabilities spatialCapabilities;
    protected TemporalCapabilities temporalCapabilities;


    public FilterCapabilitiesImpl()
    {
    }


    @Override
    public Conformance getConformance()
    {
        return conformance;
    }


    @Override
    public void setConformance(Conformance conformance)
    {
        this.conformance = conformance;        
    }


    @Override
    public ScalarCapabilities getScalarCapabilities()
    {
        return scalarCapabilities;
    }


    @Override
    public void setScalarCapabilities(ScalarCapabilities scalarCapabilities)
    {
        this.scalarCapabilities = scalarCapabilities;
    }


    @Override
    public SpatialCapabilities getSpatialCapabilities()
    {
        return spatialCapabilities;
    }


    @Override
    public void setSpatialCapabilities(SpatialCapabilities spatialCapabilities)
    {
        this.spatialCapabilities = spatialCapabilities;
    }


    @Override
    public TemporalCapabilities getTemporalCapabilities()
    {
        return temporalCapabilities;
    }


    @Override
    public void setTemporalCapabilities(TemporalCapabilities temporalCapabilities)
    {
        this.temporalCapabilities = temporalCapabilities;
    }
}
