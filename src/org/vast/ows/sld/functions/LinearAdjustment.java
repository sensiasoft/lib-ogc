/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sld.functions;

import org.vast.ows.sld.MappingFunction;


/**
 * <p><b>Title:</b><br/>
 * Linear Adjustment
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Linear Adjustment function of the form y = a*x + b.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Apr 3, 2006
 * @version 1.0
 */
public class LinearAdjustment implements MappingFunction
{
    protected double gain;
    protected double offset;
    
    
    public LinearAdjustment(double gain, double offset)
    {
        this.gain = gain;
        this.offset = offset;
    }
    
    
    public double compute(double inVal)
    {
        return inVal * gain + offset;
    }


    public double getGain()
    {
        return gain;
    }


    public void setGain(double gain)
    {
        this.gain = gain;
    }


    public double getOffset()
    {
        return offset;
    }


    public void setOffset(double offset)
    {
        this.offset = offset;
    }

}
