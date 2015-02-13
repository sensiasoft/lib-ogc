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

import net.opengis.fes.v20.DistanceBuffer;
import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.Measure;


public class DistanceBufferImpl extends SpatialOpsImpl implements DistanceBuffer
{
    static final long serialVersionUID = 1L;
    protected Expression operand1;
    protected Expression operand2;
    protected Measure distance;
    
    
    public DistanceBufferImpl()
    {
    }
    
    
    @Override
    public Expression getOperand1()
    {
        return operand1;
    }
    
    
    @Override
    public void setOperand1(Expression operand1)
    {
        this.operand1 = operand1;
    }
    
    
    @Override
    public Expression getOperand2()
    {
        return operand2;
    }
    
    
    @Override
    public void setOperand2(Expression operand2)
    {
        this.operand2 = operand2;
    }
    
    
    @Override
    public Measure getDistance()
    {
        return distance;
    }
    
    
    @Override
    public void setDistance(Measure distance)
    {
        this.distance = distance;
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        if (!this.getClass().equals(obj.getClass()))
            return false;
        
        DistanceBuffer other = (DistanceBuffer)obj;
        
        if (!getOperand1().equals(other.getOperand1()))
            return false;
        
        if (!getOperand2().equals(other.getOperand2()))
            return false;
        
        if (getDistance().getValue() != other.getDistance().getValue())
            return false;
        
        return true;
    }
}
