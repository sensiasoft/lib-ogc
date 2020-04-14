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

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.TemporalOperator;
import net.opengis.fes.v20.TemporalOperatorName;


public class TemporalOperatorImpl implements TemporalOperator
{
    protected List<QName> temporalOperands = new ArrayList<QName>();
    protected TemporalOperatorName name;
    
    
    public TemporalOperatorImpl()
    {
    }
    
    
    @Override
    public List<QName> getTemporalOperands()
    {
        return temporalOperands;
    }
    
    
    @Override
    public TemporalOperatorName getName()
    {
        return name;
    }
    
    
    @Override
    public void setName(TemporalOperatorName name)
    {
        this.name = name;
    }
}
