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
import net.opengis.gml.v32.TimeIndeterminateValue;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.impl.GMLFactory;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


public class FESRequestUtils
{
    protected static String DEFAULT_TIME_PROPERTY = "time";
        
    static FESFactory filterFactory = new FESFactory();
    static GMLFactory gmlFactory = new GMLFactory(true);
    
    /**
     * Utility method to convert from a FES temporal operator to a simpler TimeExtent.<br/>
     * Note that the assumed temporal operator is TEquals for instants and During for periods.
     * @param temporalFilter
     * @return time extent object
     */
    public static TimeExtent filterToTimeInfo(BinaryTemporalOp temporalFilter)
    {
        TimeExtent time = new TimeExtent();
        TimePosition timePos;
        
        if (temporalFilter != null)
        {
            AbstractTimeGeometricPrimitive timePrimitive = (AbstractTimeGeometricPrimitive) ((GMLExpression)temporalFilter.getOperand2()).getGmlObject();
                    
            if (timePrimitive instanceof TimeInstant)
            {
                timePos = ((TimeInstant) timePrimitive).getTimePosition();
                if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                    time.setBaseAtNow(true);
                else
                    time.setBaseTime(timePos.getDecimalValue());            
            }
            else if (timePrimitive instanceof TimePeriod)
            {
                timePos = ((TimePeriod) timePrimitive).getBeginPosition();
                if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                    time.setBeginNow(true);
                else
                    time.setStartTime(timePos.getDecimalValue());
                
                timePos = ((TimePeriod) timePrimitive).getEndPosition();
                if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                    time.setEndNow(true);
                else
                    time.setStopTime(timePos.getDecimalValue());
            }
        }
        
        return time;
    }
    
    
    /**
     * Utility method to convert a TimeExtent to a FES TEquals or During operator
     * @param time
     * @return binary temporal operator corresponding to TimeExtent settings
     */
    public static BinaryTemporalOp timeInfoToFilter(TimeExtent time)
    {
        double begin = time.getStartTime();
        double end = time.getStopTime();
        BinaryTemporalOp temporalFilter;
        
        if (time.isTimeInstant())
        {
            TimePosition timePosition = gmlFactory.newTimePosition();
            if (time.isBaseAtNow())
                timePosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
            else
                timePosition.setDecimalValue(begin);
            
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.T_EQUALS, DEFAULT_TIME_PROPERTY,
                                                         gmlFactory.newTimeInstant(timePosition));
        }
        else
        {
            TimePosition beginPosition = gmlFactory.newTimePosition();
            if (time.isBeginNow())
                beginPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
            else
                beginPosition.setDecimalValue(begin);
                
            TimePosition endPosition = gmlFactory.newTimePosition();
            if (time.isEndNow())
                endPosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
            else
                endPosition.setDecimalValue(end);
            
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.DURING, DEFAULT_TIME_PROPERTY,
                                                         gmlFactory.newTimePeriod(beginPosition, endPosition));
        }
        
        return temporalFilter;
    }
    
    
    /**
     * Utility method to convert from a GeoTools spatial operator to a simpler Bbox
     * @param spatialFilter
     * @return VAST Bbox instance, null if filter is not an FES BBOX operator
     */
    public static Bbox filterToBbox(BinarySpatialOp spatialFilter)
    {
        Bbox bbox = new Bbox();
        
        if (spatialFilter instanceof BBOX)
        {
            GMLExpression exp = (GMLExpression) ((BBOX)spatialFilter).getOperand2();
            Envelope ogcBbox = (Envelope)exp.getGmlObject();
            bbox.setCrs(ogcBbox.getSrsName());
            bbox.setMinX(ogcBbox.getLowerCorner()[0]);
            bbox.setMinY(ogcBbox.getLowerCorner()[1]);
            bbox.setMaxX(ogcBbox.getUpperCorner()[0]);
            bbox.setMaxY(ogcBbox.getUpperCorner()[1]);
        }
                
        return bbox;
    }
    
    
    /**
     * Utility method to convert a Bbox to a GeoTools BBOX operator
     * @param bbox
     * @return FES BBOX operator corresponding to bbox
     */
    public static BinarySpatialOp bboxToFilter(Bbox bbox)
    {
        Envelope gmlEnv = gmlFactory.newEnvelope(bbox.getCrs(), bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY());
        return filterFactory.newBBOX("featureOfInterest/*/shape", gmlEnv);
    }
}
