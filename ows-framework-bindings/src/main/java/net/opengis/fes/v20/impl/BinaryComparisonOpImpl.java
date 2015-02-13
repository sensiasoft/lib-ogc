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

import net.opengis.fes.v20.BinaryComparisonOp;
import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.MatchAction;


public abstract class BinaryComparisonOpImpl extends ComparisonOpsImpl implements BinaryComparisonOp
{
    static final long serialVersionUID = 1L;
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
        if (!this.getClass().equals(obj.getClass()))
            return false;
        
        BinaryComparisonOp other = (BinaryComparisonOp)obj;
        
        if (!getOperand1().equals(other.getOperand1()))
            return false;
        
        if (!getOperand2().equals(other.getOperand2()))
            return false;
        
        if (getMatchCase() != other.getMatchCase())
            return false;
        
        if (!getMatchAction().equals(other.getMatchAction()))
            return false;
        
        return true;
    }
}
