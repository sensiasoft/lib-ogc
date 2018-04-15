/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.xml.namespace.QName;
import com.google.common.collect.Range;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.impl.GMLFactory;


public class GenericTemporalFeatureImpl extends GenericFeatureImpl implements TemporalFeature
{
    private static final long serialVersionUID = 2901698262626551458L;
    public static final QName PROP_VALID_TIME = new QName(GMLStaxBindings.NS_URI, "validTime", GMLStaxBindings.NS_PREFIX_GML);
    

    public GenericTemporalFeatureImpl(QName qname)
    {
        super(qname);
    }
    

    @Override
    public Range<Instant> getValidTime()
    {
        AbstractTimeGeometricPrimitive validTime = (AbstractTimeGeometricPrimitive)properties.get(PROP_VALID_TIME);
        if (validTime == null)
            return null;
        
        if (validTime instanceof TimeInstant)
        {
            OffsetDateTime dateTime = ((TimeInstant)validTime).getTimePosition().getDateTimeValue();
            Instant instant = dateTime.withOffsetSameInstant(ZoneOffset.UTC).toInstant();
            return Range.singleton(instant);
        }
        else if (validTime instanceof TimePeriod)
        {
            OffsetDateTime beginTime = ((TimePeriod)validTime).getBeginPosition().getDateTimeValue();
            Instant begin = beginTime.withOffsetSameInstant(ZoneOffset.UTC).toInstant();
            OffsetDateTime endTime = ((TimePeriod)validTime).getEndPosition().getDateTimeValue();
            Instant end = endTime.withOffsetSameInstant(ZoneOffset.UTC).toInstant();
            return Range.closed(begin, end);
        }
        
        return null;
    }
    
    
    public void setValidTimeInstant(Instant timeInstant)
    {
        GMLFactory gmlFac = new GMLFactory();
        TimePosition time = gmlFac.newTimePosition(timeInstant.toEpochMilli()/1000.);
        TimeInstant instant = gmlFac.newTimeInstant(time);
        setProperty(PROP_VALID_TIME, instant);
    }
    
    
    public void setValidTimePeriod(Instant beginTime, Instant endTime)
    {
        GMLFactory gmlFac = new GMLFactory();
        TimePosition begin = gmlFac.newTimePosition(beginTime.toEpochMilli()/1000.);
        TimePosition end = gmlFac.newTimePosition(endTime.toEpochMilli()/1000.);
        TimePeriod period = gmlFac.newTimePeriod(begin, end);
        setProperty(PROP_VALID_TIME, period);
    }
}
