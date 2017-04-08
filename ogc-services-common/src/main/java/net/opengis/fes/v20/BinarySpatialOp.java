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
 * POJO class for XML type BinarySpatialOpType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public interface BinarySpatialOp extends SpatialOps
{    
    
    /**
     * Gets the operand1 property
     */
    public Expression getOperand1();
    
    
    /**
     * Sets the operand1 property
     */
    public void setOperand1(Expression operand1);
    
    
    /**
     * Gets the operand2 property
     */
    public Expression getOperand2();
    
    
    /**
     * Sets the operand2 property
     */
    public void setOperand2(Expression operand2);
}
