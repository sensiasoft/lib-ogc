/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.test;

import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataType;
import static org.junit.Assert.*;
import org.junit.Test;
import org.vast.swe.SWEUtils;
import org.vast.swe.helper.RasterHelper;


public class TestRasterHelper
{
    RasterHelper fac = new RasterHelper();
    SWEUtils utils = new SWEUtils(SWEUtils.V2_0);
    
    
    @Test
    public void testCreateRgbImage() throws Exception
    {
        DataArray img = fac.newRgbImage(800, 600, DataType.SHORT);
        utils.writeComponent(System.out, img, false, true);
        
        assertEquals(600, img.getComponentCount());
        assertEquals(800, img.getElementType().getComponentCount());
        assertEquals(3, ((DataArray)img.getElementType()).getElementType().getComponentCount());
    }
    
    
    @Test
    public void testCreateGrayImage() throws Exception
    {
        DataArray img = fac.newGrayscaleImage(320, 200, DataType.BYTE);
        utils.writeComponent(System.out, img, false, true);
        
        assertEquals(200, img.getComponentCount());
        assertEquals(320, img.getElementType().getComponentCount());
        assertTrue(((DataArray)img.getElementType()).getElementType() instanceof Count);        
    }
}
