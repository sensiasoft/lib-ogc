/***************************** BEGIN COPYRIGHT BLOCK **************************

Copyright (C) 2024 Delta Air Lines, Inc. All Rights Reserved.

Notice: All information contained herein is, and remains the property of
Delta Air Lines, Inc. The intellectual and technical concepts contained herein
are proprietary to Delta Air Lines, Inc. and may be covered by U.S. and Foreign
Patents, patents in process, and are protected by trade secret or copyright law.
Dissemination, reproduction or modification of this material is strictly
forbidden unless prior written permission is obtained from Delta Air Lines, Inc.

******************************* END COPYRIGHT BLOCK ***************************/

package org.vast.swe.test;

import java.time.Instant;
import org.junit.Test;
import org.vast.data.DataBlockProxy;
import org.vast.data.IDataAccessor;
import org.vast.swe.SWEHelper;

/**
 * <p>
 * TODO TestDataAccessor type description
 * </p>
 *
 * @author Alex Robin
 * @since Jan 21, 2025
 */
public class TestDataAccessor
{
    interface RecordAccessor1 extends IDataAccessor
    {
        @SweMapping(path="time")
        public Instant getTimeStamp();
        
        @SweMapping(path="temp")
        public double getTemperature();
        
        @SweMapping(path="press")
        public double getPressure();
    }
    
    
    @Test
    public void testRecordOfScalars()
    {
        var swe = new SWEHelper();
        var rec = swe.createRecord()
            .addField("time", swe.createTime())
            .addField("temp", swe.createQuantity())
            .addField("press", swe.createQuantity())
            .addField("windSpeed", swe.createQuantity())
            .addField("windDir", swe.createQuantity())
            .build();
        
        var accessor = DataBlockProxy.generate(rec, RecordAccessor1.class);
        
        var now = Instant.now().toEpochMilli();
        
        var db = rec.createDataBlock();
        db.setDoubleValue(now/1000.);
        db.setDoubleValue(1, 25.6);
        accessor.wrap(db);
        
        System.out.println(accessor.getTimeStamp());
        System.out.println(accessor.getTemperature());
        
    }

}
