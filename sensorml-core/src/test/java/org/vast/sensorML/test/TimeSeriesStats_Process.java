/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML.test;

import java.util.Iterator;
import java.util.TreeSet;
import org.vast.data.*;
import org.vast.process.*;
import org.vast.sensorML.ExecutableProcessImpl;


/**
 * <p>
 * Time series statistics process
 * </p>
 *
 * @author Alexandre Robin
 * @date Aug 20, 2020
 */
public class TimeSeriesStats_Process extends ExecutableProcessImpl
{
    DataValue timeIn;
    DataValue varIn;
    DataValue timeOut;
    DataValue meanOut;
    DataValue stdOut;
    DataValue windowSizeParam;
    DataValue reportPeriodParam;
    TreeSet<Sample> inputSamples = new TreeSet<>();
    double lastReportTime = Double.NEGATIVE_INFINITY;
    
    
    static class Sample implements Comparable<Sample>
    {
        double time;
        double val;
        
        Sample(double time, double val)
        {
            this.time = time;
            this.val = val;
        }
        
        @Override
        public int compareTo(Sample other)
        {
            return Double.compare(time, other.time);
        }
    }


    public TimeSeriesStats_Process()
    {
    }


    /**
     * Initializes the process
     * Gets handles to input/output components
     */
    @Override
    public void init() throws SMLException
    {
        try
        {
            timeIn = (DataValue) inputData.getComponent("sample").getComponent("time");
            varIn = (DataValue) inputData.getComponent("sample").getComponent("val");
            
            timeOut = (DataValue) outputData.getComponent("stats").getComponent("time");
            meanOut = (DataValue) outputData.getComponent("stats").getComponent("mean");
            stdOut = (DataValue) outputData.getComponent("stats").getComponent("stdev");
            
            windowSizeParam = (DataValue) paramData.getComponent("windowSize");            
            reportPeriodParam = (DataValue) paramData.getComponent("reportingPeriod");
        }
        catch (Exception e)
        {
            throw new SMLException(IO_ERROR_MSG, e);
        }
    }


    /**
     * Executes process algorithm on inputs and set output data
     */
    @Override
    public void execute() throws SMLException
    {
        double time = timeIn.getData().getDoubleValue();
        double val = varIn.getData().getDoubleValue();
        inputSamples.add(new Sample(time, val));
        
        // skip if we don't have enough samples yet
        double windowSize = windowSizeParam.getData().getDoubleValue();
        Sample oldest = inputSamples.first();
        Sample latest = inputSamples.last();
        if (latest.time - oldest.time < windowSize)
            return;
        
        // remove oldest samples if needed
        Iterator<Sample> it = inputSamples.iterator();
        while (it.hasNext())
        {
            if (it.next().time < latest.time - windowSize)
                it.remove();
            else
                break;
        }
        
        // skip if reporting delay hasn't been reached
        double reportingPeriod = reportPeriodParam.getData().getDoubleValue();
        if (latest.time - lastReportTime < reportingPeriod)
            return;
        
        // compute mean
        double mean = 0.0;
        for (Sample s: inputSamples)
            mean += s.val;
        mean /= inputSamples.size();
        
        // compute std dev
        double stdev = 0.0;
        for (Sample s: inputSamples)
            stdev += Math.pow(s.val - mean, 2);
        stdev = Math.sqrt(stdev);

        lastReportTime = latest.time;
        timeOut.getData().setDoubleValue(latest.time);
        meanOut.getData().setDoubleValue(mean);
        stdOut.getData().setDoubleValue(stdev);
    }
}