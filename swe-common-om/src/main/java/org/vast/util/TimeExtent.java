/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2019 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import com.google.common.collect.Range;


/**
 * <p>
 * Immutable class for storing a time instant or time period.
 * </p><p>
 * This class also supports special cases of time instants at 'now', time
 * periods beginning or ending at 'now', and open-ended time periods.
 * </p><p>
 * Note that no time zone information is retained. It usually means begin and
 * end times are UTC unless otherwise specified by the application. See 
 * {@link ZonedTimeExtent} if you need to store a time extent with a time zone.
 * </p>
 *
 * @author Alex Robin
 * @since Apr 12, 2020
 * */
public class TimeExtent
{
    public static final String SPECIAL_VALUE_NOW = "now";
    public static final String SPECIAL_VALUE_UNBOUNDED = "..";
    public static final TimeExtent ALL_TIMES = TimeExtent.period(Instant.MIN, Instant.MAX);
    
    protected Instant begin = null; // null means 'now', Instant.MIN means unbounded
    protected Instant end = null; // null means 'now', Instant.MAX means unbounded
    
    
    /**
     * @return A time extent representing the special value 'now'
     */
    public static TimeExtent now()
    {
        return new TimeExtent();
    }
    
    
    /**
     * @return A time extent representing the current time instant, that is
     * to say the value returned by {@link Instant#now()}
     */
    public static TimeExtent currentTime()
    {
        return TimeExtent.instant(Instant.now());
    }


    /**
     * @param t Time instant
     * @return A time extent representing a time instant
     */
    public static TimeExtent instant(Instant t)
    {
        Asserts.checkNotNull(t, Instant.class);
        TimeExtent time = new TimeExtent();
        time.begin = time.end = Asserts.checkNotNull(t, Instant.class);
        return time;
    }
    
    
    /**
     * @param begin Beginning of time period
     * @param end End of time period
     * @return A time extent representing a time period 
     */
    public static TimeExtent period(Instant begin, Instant end)
    {
        TimeExtent time = new TimeExtent();
        time.begin = Asserts.checkNotNull(begin, "begin");
        time.end = Asserts.checkNotNull(end, "end");
        Asserts.checkArgument(!begin.isAfter(end), "begin cannot be after end");
        return time;
    }
    
    
    /**
     * Build a time extent from a range of instant
     * @param timeRange Range object. Both ends can be bounded or unbounded
     * @return A time extent representing a time period 
     */
    public static TimeExtent period(Range<Instant> timeRange)
    {
        Asserts.checkNotNull(timeRange, Range.class);
        TimeExtent time = new TimeExtent();
        time.begin = timeRange.hasLowerBound() ? timeRange.lowerEndpoint() : Instant.MIN;
        time.end = timeRange.hasUpperBound() ? timeRange.upperEndpoint() : Instant.MAX;
        return time;
    }
    
    
    /**
     * @param begin Begin time instant
     * @return An open-ended time extent starting at the specified time
     */
    public static TimeExtent beginAt(Instant begin)
    {
        return TimeExtent.period(begin, Instant.MAX);
    }
    
    
    /**
     * @param end End time instant
     * @return An open time extent ending at the specified time
     */
    public static TimeExtent endAt(Instant end)
    {
        return TimeExtent.period(Instant.MIN, end);
    }
    
    
    /**
     * @param end End time instant
     * @return A time extent starting 'now' and ending at the specified time
     */
    public static TimeExtent beginNow(Instant end)
    {
        TimeExtent time = new TimeExtent();
        time.begin = null;
        time.end = Asserts.checkNotNull(end, "end");
        return time;
    }
    
    
    /**
     * @param begin Begin time instant
     * @return A time extent starting at the specified time and ending 'now' 
     */
    public static TimeExtent endNow(Instant begin)
    {
        TimeExtent time = new TimeExtent();
        time.begin = Asserts.checkNotNull(begin, "begin");
        time.end = null;
        return time;
    }
    
    
    /**
     * Computes a time extent that exactly contains all provided time extents
     * @param timeExtents One or more time extents
     * @return A time extent that contains all provided time extents, accounting
     * for 'now' edge cases
     */
    public static TimeExtent span(TimeExtent... timeExtents)
    {
        Instant min = null;
        Instant max = null;
        Instant now = Instant.now();
        
        for (var te: timeExtents)
        {
            if (min == null)
            {
                min = te.begin;
                max = te.end;
            }
            else
            {
                if ( (te.begin != null && min != null && te.begin.isBefore(min)) ||
                     (te.begin == null && min != null && now.isBefore(min)) ||
                     (te.begin != null && min == null && te.begin.isBefore(now)) )
                    min = te.begin;
                
                if ( (te.end != null && max != null && te.end.isAfter(max)) ||
                     (te.end == null && max != null && now.isAfter(max)) ||
                     (te.end != null && max == null && te.end.isAfter(now)))
                   max = te.end;
            }
        }
        
        TimeExtent time = new TimeExtent();
        time.begin = min;
        time.end = max;
        return time;
    }
    
    
    /**
     * <p>Parse a time extent from a string that can be either an ISO-8601 time instant or a 
     * time interval with begin and end times in ISO-8601 format, and separated by a '/'.
     * Special values 'now' and '..' (meaning unbounded) are also supported.
     * </p><p>
     * Example values that can be parsed:<br/>
     * <li>Time instant: "2018-03-12T00:00:00Z", "2000-05-25T00:00:00-06:00", "2019-09-25" or "now"</li>
     * <li>Closed interval: "2020-02-12T00:00:00Z/2020-03-18T12:31:12Z" or "2020-01-15Z/2020-02-16Z"</li>
     * <li>Open interval: "2020-02-12T00:00:00Z/.." or "../2020-03-18T12:31:12Z"</li>
     * <li>Interval with indeterminate value: "2020-02-12T00:00:00Z/now", "now/2030-08-01+01:00" or "now/.."</li>
     * </p><p>
     * When only the date portion is provided, the implied time is 00:00:00
     * </p>
     * @param text The text to be parsed
     * @return The TimeExtent representing the same time instant or interval
     */
    public static TimeExtent parse(String text)
    {
        var tokens = text.split("/");
        
        if (tokens.length == 1)
        {
            if (SPECIAL_VALUE_NOW.equalsIgnoreCase(tokens[0]))
            {
                return TimeExtent.now();
            }
            else
            {
                var dateTime = OffsetDateTime.parse(tokens[0], DateTimeFormat.ISO_DATE_OR_TIME_FORMAT);
                return TimeExtent.instant(dateTime.toInstant());
            }
        }
        else if (tokens.length == 2)
        {
            if (SPECIAL_VALUE_NOW.equalsIgnoreCase(tokens[0]))
            {
                var dateTime = parseInstantOrUnbounded(tokens[1], false);
                return TimeExtent.beginNow(dateTime);
            }
            else if (SPECIAL_VALUE_NOW.equalsIgnoreCase(tokens[1]))
            {
                var dateTime = parseInstantOrUnbounded(tokens[0], true);
                return TimeExtent.endNow(dateTime);
            }
            else
            {
                var begin = parseInstantOrUnbounded(tokens[0], true);
                var end = parseInstantOrUnbounded(tokens[1], false);
                return TimeExtent.period(begin, end);
            }
        }
        
        throw new DateTimeParseException("Invalid time extent", text, 0);
    }
    
    
    private static Instant parseInstantOrUnbounded(String token, boolean isBegin)
    {
        if (SPECIAL_VALUE_UNBOUNDED.equalsIgnoreCase(token))
            return isBegin ? Instant.MIN : Instant.MAX;
        else
            return OffsetDateTime.parse(token, DateTimeFormat.ISO_DATE_OR_TIME_FORMAT).toInstant();
    }
    
    
    protected TimeExtent()
    {        
    }    


    /**
     * @return True if begin time is defined, false otherwise
     */
    public boolean hasBegin()
    {
        return begin != Instant.MIN;
    }
    
    
    /**
     * @return The beginning instant of {@link Instant.MIN} if undefined.
     * If {@link #beginsNow()} also returns true, the current system time is returned.
     */
    public Instant begin()
    {
        return begin != null ? begin : Instant.now();
    }


    /**
     * @return True if end time is defined, false otherwise
     */
    public boolean hasEnd()
    {
        return end != Instant.MAX;
    }


    /**
     * @return The end instant of {@link Instant.MAX} if undefined.
     * If {@link #endsNow()} also returns true, the current system time is returned.
     */
    public Instant end()
    {
        return end != null ? end : Instant.now();
    }
    
    
    /**
     * @return True if this time extent represents a time instant,
     * false if it represents a time period
     */
    public boolean isInstant()
    {
        return Objects.equals(begin,  end);
    }
    
    
    /**
     * @return True if this time extent represents the 'now' instant, false otherwise
     */
    public boolean isNow()
    {
        return begin == null && end == null;
    }
    
    
    /**
     * @return True if this time extent begins at 'now', false otherwise
     */
    public boolean beginsNow()
    {
        return begin == null;
    }
    
    
    /**
     * @return True if this time extent ends at 'now', false otherwise
     */
    public boolean endsNow()
    {
        return end == null;
    }
    
    
    /**
     * @return True if this time extent contains all possible time instants (i.e. no begin and end bounds)
     */
    public boolean isAllTimes()
    {
        return !hasBegin() && !hasEnd();
    }


    /**
     * @return The duration of this time extent
     */
    public Duration duration()
    {
        if (Objects.equals(begin, end))
            return Duration.ZERO;
        return Duration.between(begin(), end());
    }
    
    
    /**
     * @param other Another time extent 
     * @return True if the specified time extent is contained within this
     * time extent, false otherwise
     */
    public boolean contains(TimeExtent other)
    {
        Asserts.checkNotNull(other, TimeExtent.class);
        return asRange().encloses(other.asRange());
    }
    
    
    /**
     * @param t A time instant 
     * @return True if the specified time instant is contained within this
     * time extent, false otherwise
     */
    public boolean contains(Instant t)
    {
        Asserts.checkNotNull(t, Instant.class);
        
        return t.equals(begin()) ||
            t.equals(end()) ||
            (t.isAfter(begin())) && (t.isBefore(end()));
    }
    
    
    /**
     * @param other Another time extent 
     * @return True if the specified time extent intersects this time extent,
     * false otherwise
     */
    public boolean intersects(TimeExtent other)
    {
        Asserts.checkNotNull(other, TimeExtent.class);
        return asRange().isConnected(other.asRange());
    }
    
    
    /**
     * Compute the intersection between 2 time extents accouting for edge cases
     * @param te1 First time extent
     * @param te2 Second time extent
     * @return A time extent representing the intersection or null if none exists
     */
    public static TimeExtent intersection(TimeExtent te1, TimeExtent te2)
    {
        // handle all times special case
        if (te1.isAllTimes())
            return te2;
        else if (te2.isAllTimes())
            return te1;
        
        Instant begin, end;
        var now = Instant.now();
        
        // compute intersection lower bound, handling special cases with 'now'
        if (te1.beginsNow() && te2.beginsNow())
            begin = null;
        else if (te1.beginsNow())
            begin = te2.begin().isAfter(now) ? te2.begin() : null;
        else if (te2.beginsNow())
            begin = te1.begin().isAfter(now) ? te1.begin() : null;
        else
            begin = te1.begin().isAfter(te2.begin()) ? te1.begin() : te2.begin();
                
        // compute intersection upper bound, handling special cases with 'now'
        if (te1.endsNow() && te2.endsNow())
            end = null;
        else if (te1.endsNow())
            end = te2.end().isBefore(now) ? te2.end() : null;
        else if (te2.endsNow())
            end = te1.end().isBefore(now) ? te1.end() : null;
        else
            end = te1.end().isBefore(te2.end()) ? te1.end() : te2.end();
        
        // construct time extent or return null
        // intersection is empty if computed end is before begin
        if (begin == null && end == null)
            return TimeExtent.now();
        else if (begin == null)
            return end.isAfter(now) ? TimeExtent.beginNow(end) : null;
        else if (end == null)
            return begin.isBefore(now) ? TimeExtent.endNow(begin) : null;
        else
            return begin.isAfter(end) ? null : TimeExtent.period(begin, end);
    }

    
    /**
     * @param keepNow Keep the 'now' string if set to true, otherwise use the current time
     * @return The ISO8601 representation of this time extent
     */
    public String isoStringUTC(boolean keepNow)
    {
        if (isInstant())
            return begin.toString();
        
        StringBuilder sb = new StringBuilder();
        sb.append(begin != null ? begin : keepNow ? "now" : begin())
          .append('/')
          .append(end != null ? end : keepNow ? "now" : end());
        return sb.toString();
    }
    
    
    /**
     * @return This time extent as a {@link Range} of two time instants.<br/>
     * Note that calling this method is invalid if this time extent either begins
     * or ends at 'now'
     */
    public Range<Instant> asRange()
    {
        if (beginsNow() || endsNow())
            throw new IllegalStateException("A time extent relative to 'now' cannot be expressed as a range");
        
        if (!hasBegin())
            return Range.atMost(end());
        else if (!hasEnd())
            return Range.atLeast(begin());
        else
            return Range.closed(begin(), end());
    }
    
    
    /**
     * @param timeZone The desired time zone
     * @return A new time extent with the associated time zone
     */
    public ZonedTimeExtent atZone(ZoneId timeZone)
    {
        ZonedTimeExtent te = new ZonedTimeExtent();
        te.begin = this.begin;
        te.end = this.end;
        te.timeZone = Asserts.checkNotNull(timeZone, ZoneId.class);
        return te;
    }


    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        
        if (!(obj instanceof TimeExtent))
            return false;
        
        TimeExtent other = (TimeExtent)obj;
        
        return ( beginsNow() && other.beginsNow() || Objects.equals(begin, other.begin()) ) &&
               ( endsNow() && other.endsNow() || Objects.equals(end, other.end()) );
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(begin, end);
    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append('[')
            .append(begin == null ? "now" : begin == Instant.MIN ? "-∞" : begin)
            .append(' ')
            .append(end == null ? "now" : end == Instant.MAX ? "+∞" : end)
            .append(']');
        return sb.toString();
    }
}
