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
import org.vast.util.NumberUtils;
import net.opengis.fes.v20.Measure;


public class MeasureImpl implements Measure
{
    protected String uom;
    protected double value;
    
    
    public MeasureImpl()
    {
    }
    
    
    @Override
    public String getUom()
    {
        return uom;
    }
    
    
    @Override
    public void setUom(String uom)
    {
        this.uom = uom;
    }
    
    
    @Override
    public double getValue()
    {
        return value;
    }
    
    
    @Override
    public void setValue(double value)
    {
        this.value = value;
    }
    
    
    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Measure &&
                Objects.equals(uom, ((Measure)obj).getUom()) &&
                NumberUtils.ulpEquals(value, ((Measure)obj).getValue());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(uom,
                            value);
    }
}
