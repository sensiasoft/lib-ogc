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
import net.opengis.fes.v20.BinaryComparisonOp;
import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.MatchAction;


public abstract class BinaryComparisonOpImpl implements BinaryComparisonOp
{
    protected Expression operand1;
    protected Expression operand2;
    protected Boolean matchCase;
    protected MatchAction matchAction;
    
    
    public BinaryComparisonOpImpl()
    {
    }
    
    
    @Override
    public Expression getOperand1()
    {
        return operand1;
    }
    
    
    @Override
    public void setOperand1(Expression exp)
    {
        this.operand1 = exp;
    }
    
    
    @Override
    public Expression getOperand2()
    {
        return operand2;
    }

    
    @Override
    public void setOperand2(Expression exp)
    {
        this.operand2 = exp;
    }
    
    
    @Override
    public boolean getMatchCase()
    {
        return matchCase;
    }
    
    
    @Override
    public boolean isSetMatchCase()
    {
        return (matchCase != null);
    }
    
    
    @Override
    public void setMatchCase(boolean matchCase)
    {
        this.matchCase = matchCase;
    }
    
    
    @Override
    public void unSetMatchCase()
    {
        this.matchCase = null;
    }
    
    
    @Override
    public MatchAction getMatchAction()
    {
        return matchAction;
    }
    
    
    @Override
    public boolean isSetMatchAction()
    {
        return (matchAction != null);
    }
    
    
    @Override
    public void setMatchAction(MatchAction matchAction)
    {
        this.matchAction = matchAction;
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
        return obj instanceof BinaryComparisonOp &&
               Objects.equals(operand1, ((BinaryComparisonOp)obj).getOperand1()) &&
               Objects.equals(operand2, ((BinaryComparisonOp)obj).getOperand2()) &&
               Objects.equals(matchAction, ((BinaryComparisonOp)obj).getMatchAction()) &&
               matchCase == ((BinaryComparisonOp)obj).getMatchCase();
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(operand1,
                            operand2,
                            matchCase,
                            matchAction);
    }
}
