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

package org.vast.ows.sos;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.gml3.bindings.PolygonTypeBinding;
import org.geotools.gml3.bindings.TimeInstantTypeBinding;
import org.geotools.gml3.bindings.TimePeriodTypeBinding;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.TemporalPrimitive;
import org.vast.ows.AbstractRequestReader;
import org.vast.ows.OWSException;
import org.vast.util.Bbox;
import org.vast.util.TimeExtent;


public class FESUtils
{
    protected static Pattern NS_DECL_PATTERN = Pattern.compile("xmlns\\(.*\\)(,xmlns\\(.*\\))*");
    protected static Pattern NS_DECL_SPLIT = Pattern.compile("(\\),)?xmlns\\(");
    
    protected static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
    
    
    /**
     * Reads the KVP argument encoding the temporal filter
     * @param arg arg argument value extracted from query string
     * @param request
     * @throws SOSException
     */
    protected static BinaryTemporalOperator readKVPTemporalFilter(String arg, Map<String, String> namespaceMap) throws OWSException
    {
        int firstComma = arg.indexOf(',');
        String valueRef = arg.substring(0, firstComma);
        String isoTime = arg.substring(firstComma + 1);
        
        // parse iso time string
        TimeExtent time = AbstractRequestReader.parseTimeArg(isoTime);
        BinaryTemporalOperator timeOp = timeInfoToFilter(time);
        
        // set value reference
        // TODO use namespace map
        ((AttributeExpressionImpl)timeOp.getExpression1()).setPropertyName(valueRef);        
        
        return timeOp;
    }
    
    
    /**
     * Reads the KVP argument encoding the spatial filter
     * @param arg argument value extracted from query string
     * @param request
     * @throws SOSException
     */
    protected static BinarySpatialOperator readKVPSpatialFilter(String arg, Map<String, String> namespaceMap) throws OWSException
    {
        int firstComma = arg.indexOf(',');
        String valueRef = arg.substring(0, firstComma);
        String bboxString = arg.substring(firstComma + 1);
        
        // parse bbox string
        Bbox bbox = AbstractRequestReader.parseBboxArg(bboxString);
        BinarySpatialOperator spatialOp = bboxToFilter(bbox);
        
        // set value reference
        // TODO use namespace map
        ((AttributeExpressionImpl)spatialOp.getExpression1()).setPropertyName(valueRef);
        
        return spatialOp;
    }
    
    
    /**
     * Reads the KVP argument containing namespace prefix declarations
     * @return
     */
    protected static Map<String, String> readKVPNamespaces(String argValue) throws SOSException
    {
        //if (!NS_DECL_PATTERN.matcher(argValue).matches())
        //    throw new SOSException(SOSException.invalid_param_code, "namespaces", null, null);
        
        Map<String, String> namespaceMap = new HashMap<String, String>();
        String[] nsList = NS_DECL_SPLIT.split(argValue);
        for (String ns: nsList)
        {
            if (ns.length() > 0)
            {
                String[] nsElts = ns.split(",");
                namespaceMap.put(nsElts[0], nsElts[1]);
            }
        }
        
        return namespaceMap;
    }
    
    
    /**
     * Utility method to convert from a GeoTools temporal operator to a simpler TimeInfo
     * @param temporalFilter
     * @return
     */
    protected static TimeExtent filterToTimeInfo(BinaryTemporalOperator temporalFilter)
    {
        TimeExtent time = new TimeExtent();
        
        if (temporalFilter != null)
        {
            TemporalPrimitive timePrimitive = (TemporalPrimitive) ((Literal)temporalFilter.getExpression2()).getValue();
                    
            if (timePrimitive instanceof Instant)
            {
                time.setBaseTime(((Instant)timePrimitive).getPosition().getDate().getTime()/1000.0);            
            }
            else if (timePrimitive instanceof Period)
            {
                time.setStartTime(((Period)timePrimitive).getBeginning().getPosition().getDate().getTime()/1000.0); 
                time.setStopTime(((Period)timePrimitive).getEnding().getPosition().getDate().getTime()/1000.0); 
            }
        }
        
        return time;
    }
    
    
    /**
     * Utility method to convert a TimeExtent to a GeoTools TEquals or During operator
     * @param time
     * @return
     */
    protected static BinaryTemporalOperator timeInfoToFilter(TimeExtent time)
    {
        Date begin = new Date((long)(time.getStartTime()*1000));
        Date end = new Date((long)(time.getStopTime()*1000));
        BinaryTemporalOperator temporalFilter;
        TemporalPrimitive timePrimitive;
        
        if (time.isTimeInstant())
        {
            timePrimitive = new DefaultInstant(new DefaultPosition(begin));
            temporalFilter = filterFactory.tequals(filterFactory.property("phenomenonTime"),
                                                        filterFactory.literal(timePrimitive));
        }
        else if (time.isEndNow() || time.getStopTime() == Double.POSITIVE_INFINITY)
        {
            timePrimitive = new DefaultInstant(new DefaultPosition(end));
            temporalFilter = filterFactory.after(filterFactory.property("phenomenonTime"),
                                                  filterFactory.literal(timePrimitive));
        }
        else if (time.getStartTime() == Double.NEGATIVE_INFINITY)
        {
            timePrimitive = new DefaultInstant(new DefaultPosition(end));
            temporalFilter = filterFactory.before(filterFactory.property("phenomenonTime"),
                                                  filterFactory.literal(timePrimitive));
        }
        else
        {
            timePrimitive = new DefaultPeriod(new DefaultInstant(new DefaultPosition(begin)),
                                              new DefaultInstant(new DefaultPosition(end)));
            temporalFilter = filterFactory.during(filterFactory.property("phenomenonTime"),
                                                       filterFactory.literal(timePrimitive));
        }
        
        return temporalFilter;
    }
    
    
    /**
     * Utility method to convert from a GeoTools spatial operator to a simpler Bbox
     * @param spatialFilter
     * @return
     */
    protected static Bbox filterToBbox(BinarySpatialOperator spatialFilter)
    {
        Bbox bbox = new Bbox();
        
        if (spatialFilter instanceof BBOX)
        {
            BBOX ogcBbox = (BBOX)spatialFilter;
            bbox.setCrs(ogcBbox.getSRS());
            bbox.setMinX(ogcBbox.getMinX());
            bbox.setMinY(ogcBbox.getMinY());
            bbox.setMaxX(ogcBbox.getMaxX());
            bbox.setMaxY(ogcBbox.getMaxY());
        }
        
        return bbox;
    }
    
    
    /**
     * Utility method to convert a Bbox to a GeoTools BBOX operator
     * @param bbox
     * @return
     */
    protected static BinarySpatialOperator bboxToFilter(Bbox bbox)
    {
        return filterFactory.bbox("featureOfInterest/*/shape", bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), bbox.getCrs());
    }
    
    
    public static void resetIdCounters()
    {
        TimeInstantTypeBinding.resetIdCounter();
        TimePeriodTypeBinding.resetIdCounter();
        PolygonTypeBinding.resetIdCounter();
    }
}
