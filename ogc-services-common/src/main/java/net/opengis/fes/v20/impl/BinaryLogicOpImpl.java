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
import net.opengis.fes.v20.BinaryLogicOp;
import net.opengis.fes.v20.FilterPredicate;


public abstract class BinaryLogicOpImpl extends LogicOpsImpl implements BinaryLogicOp
{
    protected FilterPredicate operand1;
    protected FilterPredicate operand2;
    
    
    public BinaryLogicOpImpl()
    {
    }
    
    
    @Override
    public FilterPredicate getOperand1()
    {
        return operand1;
    }
    
    
    @Override
    public void setOperand1(FilterPredicate operand1)
    {
        this.operand1 = operand1;
    }
    
    
    @Override
    public FilterPredicate getOperand2()
    {
        return operand2;
    }
    
    
    @Override
    public void setOperand2(FilterPredicate operand2)
    {
        this.operand2 = operand2;
    }
    
    
    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append(getClass().getSimpleName());
        buf.append(" (");
        buf.append(operand1.toString());
        buf.append(',');
        buf.append(operand2.toString());
        buf.append(')');
        return buf.toString();
    }


    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof BinaryLogicOp &&
               Objects.equals(operand1, ((BinaryLogicOp)obj).getOperand1()) &&
               Objects.equals(operand2, ((BinaryLogicOp)obj).getOperand2());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(operand1,
                            operand2);
    }
}
