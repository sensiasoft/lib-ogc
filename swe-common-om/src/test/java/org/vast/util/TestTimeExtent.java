/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import static org.junit.Assert.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.Test;


public class TestTimeExtent
{
    
    
    protected void checkConsistent(TimeExtent te)
    {
        var now = Instant.now();
        
        if (te.isNow())
        {
            assertTrue(te.isInstant());
            assertTrue(te.beginsNow());
            assertTrue(te.endsNow());
        }
        
        if (te.beginsNow())
        {
            assertFalse(te.isAllTimes());
            assertTrue(Duration.between(te.begin(), now).getSeconds() < 1);
        }
        
        if (te.endsNow())
        {
            assertFalse(te.isAllTimes());
            assertTrue(Duration.between(te.end(), now).getSeconds() < 1);
        }
        
        if (te.isInstant())
        {
            assertFalse(te.isAllTimes());            
        }
        
        if (te.isAllTimes())
        {
            assertFalse(te.hasBegin());
            assertFalse(te.hasEnd());
            assertFalse(te.isInstant());
            assertFalse(te.isNow());
            assertFalse(te.beginsNow());
            assertFalse(te.endsNow());
        }
        
        if (!te.hasBegin())
        {
            assertEquals(te.begin(), Instant.MIN);
            assertFalse(te.beginsNow());
        }
        
        if (!te.hasEnd())
        {
            assertEquals(te.end(), Instant.MAX);
            assertFalse(te.endsNow());
        }
    }
    
    
    @Test
    public void testSpanOverlapingPeriods()
    {
        var t1_min = Instant.parse("2020-01-01T01:01:00Z");
        var t1_max = Instant.parse("2020-06-10T08:49:00Z");
        var t2_min = Instant.parse("2020-03-16T12:38:00Z");
        var t2_max = Instant.parse("2020-09-21T17:25:00Z");
        
        var te = TimeExtent.span(
            TimeExtent.period(t1_min, t1_max),
            TimeExtent.period(t2_min, t2_max));
            
        assertEquals(t1_min, te.begin());
        assertEquals(t2_max, te.end());
        checkConsistent(te);
    }
    
    
    @Test
    public void testSpanDisjointPeriods()
    {
        var t1_min = Instant.parse("2020-01-01T01:01:00Z");
        var t1_max = Instant.parse("2020-06-10T02:19:00Z");
        var t2_min = Instant.parse("2020-07-16T12:38:00Z");
        var t2_max = Instant.parse("2020-11-21T19:37:00Z");
        
        var te = TimeExtent.span(
            TimeExtent.period(t1_min, t1_max),
            TimeExtent.period(t2_min, t2_max));
            
        assertEquals(t1_min, te.begin());
        assertEquals(t2_max, te.end());
        checkConsistent(te);
    }
    
    
    @Test
    public void testSpanBothPeriodsBeginNow()
    {
        var t1_max = Instant.parse("2020-01-01T01:01:00Z");
        var t2_max = Instant.parse("2020-07-16T12:38:00Z");
        
        var te = TimeExtent.span(
            TimeExtent.beginNow(t1_max),
            TimeExtent.beginNow(t2_max));
            
        assertTrue(te.beginsNow());
        assertEquals(t2_max, te.end());
        checkConsistent(te);
    }
    
    
    @Test
    public void testSpanBothPeriodsEndingNow()
    {
        var t1_min = Instant.parse("2020-01-01T01:01:00Z");
        var t2_min = Instant.parse("2020-07-16T12:38:00Z");
        
        var te = TimeExtent.span(
            TimeExtent.endNow(t1_min),
            TimeExtent.endNow(t2_min));
        
        assertEquals(t1_min, te.begin());
        assertTrue(te.endsNow());
        checkConsistent(te);
    }
    
    
    @Test
    public void testSpanOnePeriodEndingNowOtherBefore()
    {
        var t1_min = Instant.parse("2020-01-01T01:01:00Z");
        var t1_max = Instant.parse("2020-06-10T02:19:00Z");
        var t2_min = Instant.parse("2020-07-16T12:38:00Z");
        
        var te = TimeExtent.span(
            TimeExtent.period(t1_min, t1_max),
            TimeExtent.endNow(t2_min));
        
        assertEquals(t1_min, te.begin());
        assertTrue(te.endsNow());
        checkConsistent(te);
    }
    
    
    @Test
    public void testSpanOnePeriodEndingNowOtherAfter()
    {
        var t1_min = Instant.parse("2020-01-01T01:01:00Z");
        var t1_max = Instant.parse("3021-06-10T02:19:00Z");
        var t2_min = Instant.parse("2020-07-16T12:38:00Z");
        
        var te = TimeExtent.span(
            TimeExtent.period(t1_min, t1_max),
            TimeExtent.endNow(t2_min));
        
        assertEquals(t1_min, te.begin());
        assertEquals(t1_max, te.end());
        checkConsistent(te);
    }
    
    
    @Test
    public void testParseInstant()
    {
        var isoString = "2018-04-26T12:45:58Z";
        var te = TimeExtent.parse(isoString);
        assertEquals(Instant.parse(isoString), te.begin());
        assertEquals(Instant.parse(isoString), te.end());
        checkConsistent(te);
        
        isoString = "2030-12-14T15:23:41+02:00";
        te = TimeExtent.parse(isoString);
        assertEquals(OffsetDateTime.parse(isoString).toInstant(), te.begin());
        assertEquals(OffsetDateTime.parse(isoString).toInstant(), te.end());
        checkConsistent(te);
                
        isoString = "1960-03-31Z";
        te = TimeExtent.parse(isoString);
        assertEquals(LocalDate.parse(isoString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.begin());
        assertEquals(LocalDate.parse(isoString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.end());
        checkConsistent(te);
        
        te = TimeExtent.parse("now");
        assertTrue(te.isNow());
        checkConsistent(te);
    }
    
    
    @Test
    public void testParsePeriod()
    {
        var beginString = "2018-04-26T12:45:58Z";
        var endString = "2028-05-26T12:45:58Z";
        var te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(Instant.parse(beginString), te.begin());
        assertEquals(Instant.parse(endString), te.end());
        checkConsistent(te);
        
        beginString = "2018-04-26T12:45:58-06:00";
        endString = "2028-05-26T12:45:58+08:00";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(OffsetDateTime.parse(beginString).toInstant(), te.begin());
        assertEquals(OffsetDateTime.parse(endString).toInstant(), te.end());
        checkConsistent(te);
                
        beginString = "2200-04-20Z";
        endString = "2300-05-31Z";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(LocalDate.parse(beginString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.begin());
        assertEquals(LocalDate.parse(endString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.end());
        checkConsistent(te);
        
        beginString = "1980-01-01+02:00";
        endString = "now";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(LocalDate.parse(beginString.replace("+02:00", "")).atTime(0,0).atOffset(ZoneOffset.ofHours(2)).toInstant(), te.begin());
        assertTrue(te.endsNow());
        checkConsistent(te);
        
        beginString = "now";
        endString = "2030-09-30T12:00:00Z";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertTrue(te.beginsNow());
        assertEquals(Instant.parse(endString), te.end());
        checkConsistent(te);
        
        beginString = "2024-02-29T03:45:19.333Z";
        endString = TimeExtent.SPECIAL_VALUE_UNBOUNDED;
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(Instant.parse(beginString), te.begin());
        assertFalse(te.hasEnd());
        checkConsistent(te);
        
        beginString = TimeExtent.SPECIAL_VALUE_UNBOUNDED;
        endString = "1980-05-26Z";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertFalse(te.hasBegin());
        assertEquals(LocalDate.parse(endString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.end());
        checkConsistent(te);
        
        te = TimeExtent.parse("now/..");
        assertTrue(te.beginsNow());
        assertFalse(te.hasEnd());
        checkConsistent(te);
        
        te = TimeExtent.parse("../now");
        assertTrue(te.endsNow());
        assertFalse(te.hasBegin());
        checkConsistent(te);
    }
    
    
    @Test
    public void testIntersection() throws Exception
    {
        var t0 = Instant.now();
        TimeExtent te1, te2, xTe;
        
        // 2 finite ranges w/ instant in common
        te1 = TimeExtent.period(t0, t0.plusSeconds(10));
        te2 = TimeExtent.period(t0.minusSeconds(10), t0);
        xTe = TimeExtent.intersection(te1, te2);
        assertTrue(xTe.isInstant());
        assertEquals(te2.end(), xTe.begin());
        checkConsistent(xTe);
        
        // 2 disjoint time ranges
        te1 = TimeExtent.period(t0, t0.plusSeconds(10));
        te2 = TimeExtent.period(t0.minusSeconds(100), t0.minusMillis(1));
        xTe = TimeExtent.intersection(te1, te2);
        assertNull(xTe);
        
        // 2 finite ranges w/ range in common
        te1 = TimeExtent.period(t0, t0.plusSeconds(10));
        te2 = TimeExtent.period(t0.minusSeconds(10), t0.plusSeconds(5));
        xTe = TimeExtent.intersection(te1, te2);        
        assertEquals(te1.begin(), xTe.begin());
        assertEquals(te2.end(), xTe.end());
        checkConsistent(xTe);
        
        // 1 fixed range and 1 range with 'now' bound
        te1 = TimeExtent.beginNow(t0.plusSeconds(10));
        te2 = TimeExtent.period(t0.minusSeconds(1), t0.plusSeconds(1));
        xTe = TimeExtent.intersection(te1, te2);
        assertEquals(te2.end(), xTe.end());
        checkConsistent(xTe);
        
        // 'now' instant and range
        te1 = TimeExtent.now();
        te2 = TimeExtent.period(t0.minusSeconds(3600*1000), t0.plusSeconds(3600));
        xTe = TimeExtent.intersection(te1, te2);        
        assertTrue(xTe.isNow());
        checkConsistent(xTe);
        
        // 'now' instant and range not including now
        te1 = TimeExtent.now();
        te2 = TimeExtent.period(t0.minusSeconds(3600*1000), Instant.now().minusMillis(1));
        xTe = TimeExtent.intersection(te1, te2);        
        assertNull(xTe);
        
        // 'now' instant and range ending 'now'
        te1 = TimeExtent.now();      
        te2 = TimeExtent.endNow(t0.minusSeconds(180));
        xTe = TimeExtent.intersection(te1, te2);        
        assertTrue(xTe.isNow());
        checkConsistent(xTe);
        
        // 2 ranges ending 'now'
        te1 = TimeExtent.endNow(t0.minusSeconds(3600));
        te2 = TimeExtent.endNow(t0.minusSeconds(180));
        xTe = TimeExtent.intersection(te1, te2);
        assertEquals(te2.begin(), xTe.begin());
        assertTrue(xTe.endsNow());
        checkConsistent(xTe);
        
        // 2 ranges beginning 'now'
        te1 = TimeExtent.beginNow(t0.plusSeconds(180));     
        te2 = TimeExtent.beginNow(t0.plusSeconds(181));
        xTe = TimeExtent.intersection(te1, te2);
        assertTrue(xTe.beginsNow());
        assertEquals(te1.end(), xTe.end());        
        checkConsistent(xTe);
    }

}
