/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20;


/**
 * POJO class for XML type BinaryComparisonOpType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface BinaryComparisonOp extends ComparisonOps
{
    
    
    /**
     * Gets 1st operand property
     */
    public Expression getOperand1();
    
    
    /**
     * Sets 1st operand property
     */
    public void setOperand1(Expression exp);
    
    
    /**
     * Gets 2nd operand property
     */
    public Expression getOperand2();
    
    
    /**
     * Sets 2nd operand property
     */
    public void setOperand2(Expression exp);
    
    
    /**
     * Gets the matchCase property
     */
    public boolean getMatchCase();
    
    
    /**
     * Checks if matchCase is set
     */
    public boolean isSetMatchCase();
    
    
    /**
     * Sets the matchCase property
     */
    public void setMatchCase(boolean matchCase);
    
    
    /**
     * Unsets the matchCase property
     */
    public void unSetMatchCase();
    
    
    /**
     * Gets the matchAction property
     */
    public MatchAction getMatchAction();
    
    
    /**
     * Checks if matchAction is set
     */
    public boolean isSetMatchAction();
    
    
    /**
     * Sets the matchAction property
     */
    public void setMatchAction(MatchAction matchAction);
}
