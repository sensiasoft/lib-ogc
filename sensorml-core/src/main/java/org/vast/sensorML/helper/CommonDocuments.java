/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML.helper;

import org.vast.sensorML.SMLMetadataBuilders.CIOnlineResourceBuilder;
import org.isotc211.v2005.gmd.CIOnlineResource;
import org.vast.sensorML.SMLHelper;


/**
 * <p>
 * Helper methods to generate common document types
 * </p>
 *
 * @author Alex Robin
 * @date Jul 16, 2020
 */
public class CommonDocuments
{
    public final static String DATASHEET_ROLE = "http://sensorml.com/ont/swe/doc/Datasheet";
    public final static String DATASHEET_LABEL = "Datasheet";
    
    
    public CIOnlineResource datasheet(String url)
    {
       return createDocument()
           .name(DATASHEET_LABEL)
           .url(url)
           .build();
    }
    
    private CIOnlineResourceBuilder createDocument()
    {
        return new CIOnlineResourceBuilder(SMLHelper.DEFAULT_GMD_FACTORY);
    }
}
