/***************************************************************
 (c) Copyright 2007, University of Alabama in Huntsville (UAH)
 ALL RIGHTS RESERVED

 This software is the property of UAH.
 It cannot be duplicated, used, or distributed without the
 express written consent of UAH.

 This software developed by the Vis Analysis Systems Technology
 (VAST) within the Earth System Science Lab under the direction
 of Mike Botts (mike.botts@atmos.uah.edu)
 ***************************************************************/

package org.vast.physics;


/**
 * <p><b>Title:</b><br/>
 * Time Extent
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Class for storing the definition of a temporal domain.
 * This can include a base time, time bias (deviation from base time),
 * time step, and lead/lag time deltas.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Tony Cook, Mike Botts, Alexandre Robin
 * @date Nov 15, 2005
 * @version 1.0
 */
public class TimeExtent
{
    public final static double NOW_ACCURACY = 1000;
    public final static double NOW = Double.MIN_VALUE;
    public final static double UNKNOWN = Double.MAX_VALUE;
    
    protected double baseTime = Double.NaN;
    protected double timeBias = 0;
    protected double timeStep = 0;
    protected double leadTimeDelta = 0;
    protected double lagTimeDelta = 0;
    protected boolean baseAtNow = false;  // if true baseTime is associated to machine clock
    protected boolean endNow = false;     // if true stopTime is associated to machine clock
    protected boolean beginNow = false;   // if true startTime is associated to machine clock
    
    
    public TimeExtent()
    {        
    }


    public TimeExtent(double baseJulianTime)
    {
        this.baseTime = baseJulianTime;
    }
    
    
    public TimeExtent copy()
    {
        TimeExtent timeExtent = new TimeExtent();
        
        timeExtent.baseTime = this.getBaseTime();
        timeExtent.timeBias = this.timeBias;
        timeExtent.timeStep = this.timeStep;
        timeExtent.leadTimeDelta = this.leadTimeDelta;
        timeExtent.lagTimeDelta = this.lagTimeDelta;
        timeExtent.baseAtNow = this.baseAtNow;
        timeExtent.endNow = this.endNow;
        timeExtent.beginNow = this.beginNow;
        
        return timeExtent;
    }


    public TimeExtent(double baseJulianTime, double timeBiasSeconds, double timeStepSeconds, double leadTimeDeltaSeconds, double lagTimeDeltaSeconds)
    {

        this.baseTime = baseJulianTime;
        this.timeBias = timeBiasSeconds;
        this.timeStep = timeStepSeconds;
        this.leadTimeDelta = Math.abs(leadTimeDeltaSeconds);
        this.lagTimeDelta = Math.abs(lagTimeDeltaSeconds);
    }


    public void setBaseTime(double baseJulianTime)
    {
        this.baseTime = baseJulianTime;
    }


    public void setTimeBias(double seconds)
    {
        this.timeBias = seconds;
    }


    public void setTimeStep(double seconds)
    {
        this.timeStep = seconds;
    }


    public void setLeadTimeDelta(double seconds)
    {
        this.leadTimeDelta = Math.abs(seconds);
    }


    public void setLagTimeDelta(double seconds)
    {
        this.lagTimeDelta = Math.abs(seconds);
    }


    public void setDeltaTimes(double leadDeltaSeconds, double lagDeltaSeconds)
    {
        this.leadTimeDelta = Math.abs(leadDeltaSeconds);
        this.lagTimeDelta = Math.abs(lagDeltaSeconds);
    }


    /**
     * To get baseTime without bias applied
     * @return
     */
    public double getBaseTime()
    {
        if (baseAtNow)
            return getNow() + timeBias;
        else
            return baseTime;
    }


    /**
     * To get baseTime or absTime with bias applied
     * @return
     */
    public double getAdjustedTime()
    {
        return (getBaseTime() + timeBias);
    }


    public double getTimeBias()
    {
        return timeBias;
    }


    public double getTimeStep()
    {
        return timeStep;
    }


    public double getLeadTimeDelta()
    {
        return leadTimeDelta;
    }


    public double getLagTimeDelta()
    {
        return lagTimeDelta;
    }


    public double getTimeRange()
    {
        return (getAdjustedLeadTime() - getAdjustedLagTime());
    }


    public double getAdjustedLeadTime()
    {
        if (endNow)
            return getNow() + timeBias;
        else
            return (getBaseTime() + timeBias + leadTimeDelta);
    }


    public double getAdjustedLagTime()
    {
        if (beginNow)
            return getNow() + timeBias;
        else
            return (getBaseTime() + timeBias - lagTimeDelta);
    }
    
    
    public boolean isBaseAtNow()
    {
        return baseAtNow;
    }


    public void setBaseAtNow(boolean baseAtNow)
    {
        this.baseAtNow = baseAtNow;
    }


    public boolean isBeginNow()
    {
        return beginNow;
    }


    public void setBeginNow(boolean beginNow)
    {
        this.beginNow = beginNow;
    }


    public boolean isEndNow()
    {
        return endNow;
    }


    public void setEndNow(boolean endNow)
    {
        this.endNow = endNow;
    }


    /**
     * Returns number of full time steps
     * @return
     */
    public int getNumberOfSteps()
    {
        if (timeStep == 0.0)
            return 1;
        else
            return (int) ((getAdjustedLeadTime() - getAdjustedLagTime()) / timeStep);
    }
    
    
    /**
     * Calculates times based on current time settings, always assuring
     * that both endpoints are included even if an uneven time step occurs
     * at the end
     */
    public double[] getTimes()
    {
        double time = getAdjustedLeadTime();
        double lagTime = getAdjustedLagTime();

        // if step is 0 returns two extreme points
        if (timeStep == 0.0)
        {
            return new double[] {time, lagTime};
        }
            
        double timeRange = Math.abs(time - lagTime);
        double remainder = timeRange % timeStep;
        int steps = (int) (timeRange / timeStep) + 1;       

        double[] times;
        if (remainder != 0.0)
        {
            times = new double[steps + 1];
            times[steps] = lagTime;
        }
        else
            times = new double[steps];

        for (int i = 0; i < steps; i++)
            times[i] = time - i * timeStep;
        
        return times;
    }


    public String toString()
    {
        String tString = new String("TimeExtent:");
        tString += "\n  baseTime = " + (baseAtNow ? "now" : baseTime);
        tString += "\n  timeBias = " + timeBias;
        tString += "\n  timeStep = " + timeStep;
        tString += "\n  leadTimeDelta = " + leadTimeDelta;
        tString += "\n  lagTimeDelta = " + lagTimeDelta;
        return tString;
    }


    /**
     * Tests if time ranges are equal
     * @param timeExtent
     * @return
     */
    public boolean compareTimeRange(TimeExtent timeExtent)
    {
        if (this.getAdjustedLagTime() != timeExtent.getAdjustedLagTime())
            return false;
        
        if (this.getAdjustedLeadTime() != timeExtent.getAdjustedLeadTime())
            return false;
        
        return true;
    }
    
    
    /**
     * Checks if this timeExtent contains the given timeExtent
     * @param timeExtent
     * @return
     */
    public boolean contains(TimeExtent timeExtent)
    {
        double thisLag = this.getAdjustedLagTime();
        double thisLead = this.getAdjustedLeadTime();
        double otherLag = timeExtent.getAdjustedLagTime();
        double otherLead = timeExtent.getAdjustedLeadTime();
        
        if (otherLag < thisLag)
            return false;
        
        if  (otherLag > thisLead)
            return false;
        
        if (otherLead < thisLag)
            return false;        
        
        if (otherLead > thisLead)
            return false;
        
        return true;
    }
    
    
    /**
     * Checks if this timeExtent intersects the given timeExtent
     * @param timeExtent
     * @return
     */
    public boolean intersects(TimeExtent timeExtent)
    {
        double thisLag = this.getAdjustedLagTime();
        double thisLead = this.getAdjustedLeadTime();
        double otherLag = timeExtent.getAdjustedLagTime();
        double otherLead = timeExtent.getAdjustedLeadTime();
        
        if (otherLag > thisLag && otherLag < thisLead)
            return true;
        
        if (otherLead > thisLag && otherLead < thisLead)
            return true;
        
        if (otherLag <= thisLag && otherLead >= thisLead)
            return true;
        
        return false;
    }
    
    
    /**
     * Check if time is null (i.e. baseTime is not set)
     * @return
     */
    public boolean isNull()
    {
        return (baseTime == Double.NaN && !baseAtNow);
    }
    
    
    /**
     * Resets all variables so that extent is null
     */
    public void nullify()
    {
        baseTime = Double.NaN;
        timeBias = 0;
        timeStep = 0;
        leadTimeDelta = 0;
        lagTimeDelta = 0;
        baseAtNow = false;
        endNow = false;
        beginNow = false;
    }
    
    
    /**
     * Resizes this extent so that it contains the given time value
     * @param t time value (MUST be in same reference frame as the extent)
     */
    public void resizeToContain(double t)
    {
        if (isNull())
        {
            baseTime = t;
            timeBias = 0;
            return;
        }    
        
        double adjBaseTime = getAdjustedTime();
        if (t > getAdjustedLeadTime())
            leadTimeDelta = t - adjBaseTime;
        else if (t < getAdjustedLagTime())
            lagTimeDelta = adjBaseTime - t; 
    }
    
    
    /**
     * Return latest value for now. This would return a new 'now' value
     * only if previous call was made more than 1 second ago.
     * @return
     */
    private double now = 0;
    private double getNow()
    {
        double exactNow = System.currentTimeMillis()/1000;
        if (exactNow - now > NOW_ACCURACY)
            now = exactNow;
        
        return now;
    }
}
