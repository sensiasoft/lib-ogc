/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the
 University of Alabama in Huntsville (UAH). <http://vast.uah.edu>
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.

 Please Contact Mike Botts <mike.botts@uah.edu>
 or Alexandre Robin <alex.robin@sensiasoftware.com> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
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
        
        if (temporalFilter != null)
        {
            AbstractTimeGeometricPrimitive timePrimitive = (AbstractTimeGeometricPrimitive) ((GMLExpression)temporalFilter.getOperand2()).getGmlObject();
                    
            if (timePrimitive instanceof TimeInstant)
            {
                TimePosition timePos = ((TimeInstant) timePrimitive).getTimePosition();
                if (timePos.getIndeterminatePosition() == TimeIndeterminateValue.NOW)
                    time.setBaseAtNow(true);
                else
                    time.setBaseTime(timePos.getDecimalValue());            
            }
            else if (timePrimitive instanceof TimePeriod)
            {
                time.setStartTime(((TimePeriod)timePrimitive).getBeginPosition().getDecimalValue()); 
                time.setStopTime(((TimePeriod)timePrimitive).getEndPosition().getDecimalValue()); 
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
        AbstractTimeGeometricPrimitive timePrimitive;
        
        if (time.isTimeInstant())
        {
            TimePosition timePosition;
            if (time.isBaseAtNow())
            {
                timePosition = gmlFactory.newTimePosition();
                timePosition.setIndeterminatePosition(TimeIndeterminateValue.NOW);
            }
            else
                timePosition = gmlFactory.newTimePosition(begin);
            
            timePrimitive = gmlFactory.newTimeInstant(timePosition);
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.T_EQUALS, DEFAULT_TIME_PROPERTY, timePrimitive);
        }
        else if (time.isEndNow() || time.getStopTime() == Double.POSITIVE_INFINITY)
        {
            timePrimitive = gmlFactory.newTimeInstant(gmlFactory.newTimePosition(begin));
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.AFTER, DEFAULT_TIME_PROPERTY, timePrimitive);
        }
        else if (time.getStartTime() == Double.NEGATIVE_INFINITY)
        {
            timePrimitive = gmlFactory.newTimeInstant(gmlFactory.newTimePosition(end));
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.BEFORE, DEFAULT_TIME_PROPERTY, timePrimitive);
        }
        else
        {
            timePrimitive = gmlFactory.newTimePeriod(gmlFactory.newTimePosition(begin), gmlFactory.newTimePosition(end));
            temporalFilter = filterFactory.newTemporalOp(TemporalOperatorName.DURING, DEFAULT_TIME_PROPERTY, timePrimitive);
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
