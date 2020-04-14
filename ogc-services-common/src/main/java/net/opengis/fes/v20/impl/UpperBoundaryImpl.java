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
import net.opengis.fes.v20.UpperBoundary;


/**
 * POJO class for XML type UpperBoundaryType(@http://www.opengis.net/fes/2.0).
 *
 * This is a complex type.
 */
public class UpperBoundaryImpl implements UpperBoundary
{
    protected Expression expression;
    
    
    public UpperBoundaryImpl()
    {
    }
    
    
    /**
     * Gets the expression property
     */
    @Override
    public Expression getExpression()
    {
        return expression;
    }
    
    
    /**
     * Sets the expression property
     */
    @Override
    public void setExpression(Expression expression)
    {
        this.expression = expression;
    }
}
