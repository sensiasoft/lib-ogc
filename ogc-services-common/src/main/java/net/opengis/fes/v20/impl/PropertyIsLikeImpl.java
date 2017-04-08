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
import net.opengis.fes.v20.PropertyIsLike;


public class PropertyIsLikeImpl implements PropertyIsLike
{
    protected Expression expression1;
    protected Expression expression2;
    protected String wildCard = "";
    protected String singleChar = "";
    protected String escapeChar = "";
    
    
    public PropertyIsLikeImpl()
    {
    }


    @Override
    public Expression getOperand1()
    {
        return expression1;
    }
    
    
    @Override
    public void setOperand1(Expression exp)
    {
        this.expression1 = exp;
    }
    
    
    @Override
    public Expression getOperand2()
    {
        return expression2;
    }

    
    @Override
    public void setOperand2(Expression exp)
    {
        this.expression2 = exp;
    }
    
    
    @Override
    public String getWildCard()
    {
        return wildCard;
    }
    
    
    @Override
    public void setWildCard(String wildCard)
    {
        this.wildCard = wildCard;
    }
    
    
    @Override
    public String getSingleChar()
    {
        return singleChar;
    }
    
    
    @Override
    public void setSingleChar(String singleChar)
    {
        this.singleChar = singleChar;
    }
    
    
    @Override
    public String getEscapeChar()
    {
        return escapeChar;
    }
    
    
    @Override
    public void setEscapeChar(String escapeChar)
    {
        this.escapeChar = escapeChar;
    }
}
