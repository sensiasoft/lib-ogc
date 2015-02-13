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

package org.vast.ows.sld.functions;


/**
 * <p>
 * Look Up Table implementation of a mapping function.
 * This will interpolate at the 0th order between points.
 * (i.e. the value of the previous point will be used). 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Apr 3, 2006
 * */
public class LookUpTable0D extends AbstractMappingFunction
{
    protected double[][] tableData;
    
    
    public LookUpTable0D(double[][] tableData)
    {
        this.tableData = tableData;
    }
    
    
    @Override
    public double compute(double indexVal)
    {
        if (tableData == null)
            return 0;
        
        int maxi = tableData[0].length - 1;
        
        // 0 order extrapolation if more than max value
        if (indexVal >= tableData[0][maxi])
            return tableData[1][maxi];
        
        // 0 order extrapolation if less than min value
        else if (indexVal <= tableData[0][0])
            return tableData[1][0];
        
        // for all other cases we need to find entry in table
        else
        {
            int nextIndex = 0;
            
            // find first entry higher than index
            double tableVal = tableData[0][nextIndex];
            while (indexVal > tableVal)
            {
                nextIndex++;
                tableVal = tableData[0][nextIndex];     
            }
            
            // if index exactly equal to value in table
            // no interpolation needed
            if (indexVal == tableVal)
            {
                return tableData[1][nextIndex];
            }
            // otherwise, need interpolation
            else
            {
                // compute linear interpolation factor
                int prevIndex = nextIndex - 1;
                double prevInVal = tableData[0][prevIndex];
                double nextInVal = tableData[0][nextIndex];
                double a = (indexVal - prevInVal) / (nextInVal - prevInVal);
                double prevOutVal = tableData[1][prevIndex];
                double nextOutVal = tableData[1][nextIndex];
                
                // return interpolated output
                return (prevOutVal + a*(nextOutVal - prevOutVal));
            }
        }
    }


    public double[][] getTableData()
    {
        return tableData;
    }


    public void setTableData(double[][] tableData)
    {
        this.tableData = tableData;
    }
}
