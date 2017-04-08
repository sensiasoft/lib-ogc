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
import net.opengis.fes.v20.ComparisonOperator;
import net.opengis.fes.v20.ScalarCapabilities;


public class ScalarCapabilitiesImpl implements ScalarCapabilities
{
    protected boolean logicalOperators;
    protected List<ComparisonOperator> comparisonOperators = new ArrayList<ComparisonOperator>();
    
    
    public ScalarCapabilitiesImpl()
    {
    }
    
    
    @Override
    public boolean getLogicalOperators()
    {
        return logicalOperators;
    }
    
    
    @Override
    public void setLogicalOperators(boolean b)
    {
        this.logicalOperators = b;        
    }
    
    
    @Override
    public List<ComparisonOperator> getComparisonOperators()
    {
        return comparisonOperators;
    }
}
