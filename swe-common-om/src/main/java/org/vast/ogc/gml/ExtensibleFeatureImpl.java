/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import com.google.common.collect.ImmutableMap;
import net.opengis.gml.v32.impl.AbstractFeatureImpl;


/**
 * <p>
 * Base class for implementing custom features based on application schemas.<br/>
 * Subclasses typically add new fields along with corresponding getters and
 * setters and must override {@link #getProperties()} to expose these
 * properties in a generic way.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 2 2021
 * */
public abstract class ExtensibleFeatureImpl extends AbstractFeatureImpl
{
    private static final long serialVersionUID = -7413618024981503885L;
    
    /* cached properties */
    protected transient Map<QName, Object> properties;


    public ExtensibleFeatureImpl()
    {
        this.properties = new LinkedHashMap<>();
    }
    
    
    @Override
    public Map<QName, Object> getProperties()
    {
        if (properties == null)
        {
            var builder = ImmutableMap.<QName, Object>builder();
            appendProperties(builder);
            properties = builder.build();
        }
        
        return properties;
    }
    
    
    protected void appendProperties(ImmutableMap.Builder<QName, Object> builder)
    {
        // subclasses can append custom properties here
    }
}
