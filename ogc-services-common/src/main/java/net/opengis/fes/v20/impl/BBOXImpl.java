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
import net.opengis.fes.v20.BBOX;
import net.opengis.fes.v20.Expression;


public class BBOXImpl implements BBOX
{
    protected Expression operand1;
    protected Expression operand2;
    
    
    public BBOXImpl()
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
    public boolean equals(Object obj)
    {
        return obj instanceof BBOX &&
               Objects.equals(operand1, ((BBOX)obj).getOperand1()) &&
               Objects.equals(operand2, ((BBOX)obj).getOperand2());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(operand1, operand2);
    }
}
