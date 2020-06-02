/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sld;

/**
 * <p>
 * Object used for one of the channel selections.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 11, 2005
 * */
public class RasterChannel extends ScalarParameter
{
    protected boolean normalize;
    protected boolean histogram;
    protected double gamma = 1.0;


    public RasterChannel(ScalarParameter param)
    {       
        this.constant = param.constant;
        this.constantValue = param.constantValue;
        this.mappingFunction = param.mappingFunction;
        this.propertyName = param.propertyName;
    }


    public double getGamma()
    {
        return gamma;
    }


    public void setGamma(double gamma)
    {
        this.gamma = gamma;
    }


    public boolean isHistogram()
    {
        return histogram;
    }


    public void setHistogram(boolean histogram)
    {
        this.histogram = histogram;
    }


    public boolean isNormalize()
    {
        return normalize;
    }


    public void setNormalize(boolean normalize)
    {
        this.normalize = normalize;
    }
}
