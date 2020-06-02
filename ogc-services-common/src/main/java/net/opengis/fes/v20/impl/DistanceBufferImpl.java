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

import java.util.Objects;
import net.opengis.fes.v20.DistanceBuffer;
import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.Measure;


public class DistanceBufferImpl implements DistanceBuffer
{
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
        return obj instanceof DistanceBuffer &&
                Objects.equals(operand1, ((DistanceBuffer)obj).getOperand1()) &&
                Objects.equals(operand2, ((DistanceBuffer)obj).getOperand2()) &&
                Objects.equals(distance, ((DistanceBuffer)obj).getDistance());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(operand1,
                            operand2,
                            distance);
    }
}
