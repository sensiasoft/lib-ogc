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
import net.opengis.fes.v20.GMLExpression;


public class GMLExpressionImpl implements GMLExpression
{
    Object gmlObj;
    
    
    public GMLExpressionImpl(Object gmlObj)
    {
        this.gmlObj = gmlObj;
    }
    
    
    @Override
    public Object getGmlObject()
    {
        return gmlObj;
    }


    @Override
    public void setGmlObject(Object gmlObj)
    {
        this.gmlObj = gmlObj;
    }


    @Override
    public String toString()
    {
        return gmlObj.toString();
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof GMLExpression &&
               Objects.equals(gmlObj, ((GMLExpression)obj).getGmlObject());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(gmlObj);
    }
}
