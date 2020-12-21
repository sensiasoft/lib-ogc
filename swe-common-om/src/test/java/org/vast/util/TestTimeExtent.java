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
    }
    
    
    @Test
    public void testSpanOnePeriodEndingNowOtherAfter()
    {
        var t1_min = Instant.parse("2020-01-01T01:01:00Z");
        var t1_max = Instant.parse("2021-06-10T02:19:00Z");
        var t2_min = Instant.parse("2020-07-16T12:38:00Z");
        
        var te = TimeExtent.span(
            TimeExtent.period(t1_min, t1_max),
            TimeExtent.endNow(t2_min));
        
        assertEquals(t1_min, te.begin());
        assertEquals(t1_max, te.end());
    }
    
    
    @Test
    public void testParseInstant()
    {
        var isoString = "2018-04-26T12:45:58Z";
        var te = TimeExtent.parse(isoString);
        assertEquals(Instant.parse(isoString), te.begin());
        assertEquals(Instant.parse(isoString), te.end());
        
        isoString = "2030-12-14T15:23:41+02:00";
        te = TimeExtent.parse(isoString);
        assertEquals(OffsetDateTime.parse(isoString).toInstant(), te.begin());
        assertEquals(OffsetDateTime.parse(isoString).toInstant(), te.end());
                
        isoString = "1960-03-31Z";
        te = TimeExtent.parse(isoString);
        assertEquals(LocalDate.parse(isoString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.begin());
        assertEquals(LocalDate.parse(isoString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.end());
        
        te = TimeExtent.parse("now");
        assertTrue(te.isNow());
        assertTrue(te.beginsNow());
        assertTrue(te.endsNow());
        assertTrue(Duration.between(te.begin(), te.end()).getSeconds() < 1);
    }
    
    
    @Test
    public void testParsePeriod()
    {
        var beginString = "2018-04-26T12:45:58Z";
        var endString = "2028-05-26T12:45:58Z";
        var te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(Instant.parse(beginString), te.begin());
        assertEquals(Instant.parse(endString), te.end());
        
        beginString = "2018-04-26T12:45:58-06:00";
        endString = "2028-05-26T12:45:58+08:00";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(OffsetDateTime.parse(beginString).toInstant(), te.begin());
        assertEquals(OffsetDateTime.parse(endString).toInstant(), te.end());
                
        beginString = "2200-04-20Z";
        endString = "2300-05-31Z";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(LocalDate.parse(beginString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.begin());
        assertEquals(LocalDate.parse(endString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.end());
        
        beginString = "1980-01-01+02:00";
        endString = "now";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(LocalDate.parse(beginString.replace("+02:00", "")).atTime(0,0).atOffset(ZoneOffset.ofHours(2)).toInstant(), te.begin());
        assertTrue(te.endsNow());
        
        beginString = "now";
        endString = "2030-09-30T12:00:00Z";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertTrue(te.beginsNow());
        assertEquals(Instant.parse(endString), te.end());
        
        beginString = "2024-02-29T03:45:19.333Z";
        endString = TimeExtent.SPECIAL_VALUE_UNBOUNDED;
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(Instant.parse(beginString), te.begin());
        assertEquals(Instant.MAX, te.end());
        assertFalse(te.hasEnd());
        
        beginString = TimeExtent.SPECIAL_VALUE_UNBOUNDED;
        endString = "1980-05-26Z";
        te = TimeExtent.parse(beginString + "/" + endString);
        assertEquals(Instant.MIN, te.begin());
        assertEquals(LocalDate.parse(endString.replace("Z", "")).atTime(0,0).atOffset(ZoneOffset.UTC).toInstant(), te.end());
        assertFalse(te.hasBegin());
        
        te = TimeExtent.parse("now/..");
        assertTrue(te.beginsNow());
        assertFalse(te.endsNow());
        assertFalse(te.hasEnd());
        assertTrue(Duration.between(Instant.now(), te.begin()).getSeconds() < 1);
        
        te = TimeExtent.parse("../now");
        assertFalse(te.beginsNow());
        assertTrue(te.endsNow());
        assertFalse(te.hasBegin());
        assertTrue(Duration.between(Instant.now(), te.end()).getSeconds() < 1);
    }

}
