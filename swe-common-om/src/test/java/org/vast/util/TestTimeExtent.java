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
import java.time.Instant;
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

}
