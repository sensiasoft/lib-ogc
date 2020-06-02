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

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.SpatialCapabilities;
import net.opengis.fes.v20.SpatialOperator;


/**
 * POJO class for XML type Spatial_CapabilitiesType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class SpatialCapabilitiesImpl implements SpatialCapabilities
{
    protected List<QName> geometryOperands = new ArrayList<QName>();
    protected List<SpatialOperator> spatialOperators = new ArrayList<SpatialOperator>();
    
    
    public SpatialCapabilitiesImpl()
    {
    }
    
    
    @Override
    public List<QName> getGeometryOperands()
    {
        return geometryOperands;
    }
    
    
    @Override
    public List<SpatialOperator> getSpatialOperators()
    {
        return spatialOperators;
    }
}
