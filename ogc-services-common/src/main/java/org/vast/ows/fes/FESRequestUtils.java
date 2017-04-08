/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.fes;

import net.opengis.fes.v20.BBOX;
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.BinaryTemporalOp;
import net.opengis.fes.v20.GMLExpression;
import net.opengis.fes.v20.TemporalOperatorName;
import net.opengis.fes.v20.impl.FESFactory;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.Envelope;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimePeriod;
import org.vast.ogc.gml.GMLUtils;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


public class FESRequestUtils
{
    protected static String DEFAULT_TIME_PROPERTY = "time";
        
    static FESFactory filterFactory = new FESFactory();
    static GMLUtils gmlUtils = new GMLUtils(GMLUtils.V3_2);
    
    
    /**
     * Utility method to convert from a FES temporal operator to a simpler TimeExtent.<br/>
     * Note that the assumed temporal operator is TEquals for instants and During for periods.
     * @param temporalFilter
     * @return time extent object
     */
    public static TimeExtent filterToTimeExtent(BinaryTemporalOp temporalFilter)
    {
        if (temporalFilter != null)
        {
            AbstractTimeGeometricPrimitive timePrimitive = (AbstractTimeGeometricPrimitive) ((GMLExpression)temporalFilter.getOperand2()).getGmlObject();
            return gmlUtils.timePrimitiveToTimeExtent(timePrimitive);            
        }
        
        return new TimeExtent();
    }
    
    
    /**
     * Utility method to convert a TimeExtent to a FES TEquals or During operator
     * @param timeExtent
     * @return binary temporal operator corresponding to TimeExtent settings
     */
    public static BinaryTemporalOp timeExtentToFilter(TimeExtent timeExtent)
    {
        BinaryTemporalOp temporalFilter;
        AbstractTimeGeometricPrimitive timePrim = gmlUtils.timeExtentToTimePrimitive(timeExtent, false);
                
        if (timePrim instanceof TimeInstant)
        {
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.T_EQUALS,
                                                         DEFAULT_TIME_PROPERTY,
                                                         (TimeInstant)timePrim);
        }
        else
        {
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.DURING,
                                                         DEFAULT_TIME_PROPERTY,
                                                         (TimePeriod)timePrim);
        }
        
        return temporalFilter;
    }
    
    
    /**
     * Utility method to convert from FES spatial operator to a Bbox object
     * @param spatialFilter
     * @return VAST Bbox instance, null if filter is not an FES BBOX operator
     */
    public static Bbox filterToBbox(BinarySpatialOp spatialFilter)
    {
        if (spatialFilter instanceof BBOX)
        {
            GMLExpression exp = (GMLExpression) ((BBOX)spatialFilter).getOperand2();
            Envelope env = (Envelope)exp.getGmlObject();
            return GMLUtils.envelopeToBbox(env);
        }
                
        return new Bbox();
    }
    
    
    /**
     * Utility method to convert a Bbox object to a FES BBOX operator
     * @param bbox
     * @return FES BBOX operator corresponding to bbox
     */
    public static BinarySpatialOp bboxToFilter(Bbox bbox)
    {
        Envelope gmlEnv = gmlUtils.bboxToEnvelope(bbox);
        return filterFactory.newBBOX("featureOfInterest/*/shape", gmlEnv);
    }
}
