/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.LowerBoundary;
import net.opengis.fes.v20.PropertyIsBetween;
import net.opengis.fes.v20.UpperBoundary;


public class PropertyIsBetweenImpl implements PropertyIsBetween
{
    protected Expression expression;
    protected LowerBoundary lowerBoundary;
    protected UpperBoundary upperBoundary;
    
    
    public PropertyIsBetweenImpl()
    {
    }
    
    
    @Override
    public Expression getOperand()
    {
        return expression;
    }
    
    
    @Override
    public void setOperand(Expression expression)
    {
        this.expression = expression;
    }
    
    
    @Override
    public LowerBoundary getLowerBoundary()
    {
        return lowerBoundary;
    }
    
    
    @Override
    public void setLowerBoundary(LowerBoundary lowerBoundary)
    {
        this.lowerBoundary = lowerBoundary;
    }
    
    
    @Override
    public UpperBoundary getUpperBoundary()
    {
        return upperBoundary;
    }
    
    
    @Override
    public void setUpperBoundary(UpperBoundary upperBoundary)
    {
        this.upperBoundary = upperBoundary;
    }
}
