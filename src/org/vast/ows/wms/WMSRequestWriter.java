/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wms;

import org.vast.ows.AbstractRequestWriter;


/**
 * <p><b>Title:</b>
 * WMS Request Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Abstract WMS Request Writer containing common code
 * for all versions
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Jan 16, 2007
 * @version 1.0
 */
public abstract class WMSRequestWriter extends AbstractRequestWriter<WMSQuery>
{
    
    /**
     * Create comma separated layer list for GET request
     * @param query
     * @return
     */
    protected String createLayerList(WMSQuery query)
    {
        StringBuffer buff = new StringBuffer();
        
        int layerCount = query.getLayers().size();
        for (int i=0; i<layerCount; i++)
        {
            buff.append(query.getLayers().get(i));
            
            if (i != layerCount-1)
                buff.append(',');               
        }
        
        return buff.toString();
    }
    
    
    /**
     * Create comma separated style list for GET request
     * @param query
     * @return
     */
    protected String createStyleList(WMSQuery query)
    {
        StringBuffer buff = new StringBuffer();
        
        int styleCount = query.getStyles().size();
        for (int i=0; i<styleCount; i++)
        {
            buff.append(query.getStyles().get(i));
            
            if (i != styleCount-1)
                buff.append(',');               
        }
        
        return buff.toString();
    }
}
