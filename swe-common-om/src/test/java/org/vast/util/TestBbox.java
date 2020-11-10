/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

import static org.junit.Assert.*;
import org.junit.Test;


public class TestBbox
{

    @Test
    public void testContains()
    {
        Bbox bbox = new Bbox(0, 0, 10, 10);
        Bbox bbox_within = new Bbox(1, 2, 5, 5);
        Bbox bbox_touchMinX = new Bbox(0, 2, 5, 5);
        Bbox bbox_touchMaxX = new Bbox(1, 2, 10, 5);
        Bbox bbox_touchCorner = new Bbox(-10, -3, 0, 0);
        Bbox bbox_crossMaxX = new Bbox(1, 2, 10, 15);
        Bbox bbox_null = new Bbox();
        
        assertTrue(bbox.contains(bbox_within));
        assertTrue(bbox.contains(bbox_touchMinX));
        assertTrue(bbox.contains(bbox_touchMaxX));
        assertFalse(bbox.contains(bbox_touchCorner));
        assertFalse(bbox.contains(bbox_crossMaxX));
        assertFalse(bbox.contains(bbox_null));
    }
    
    
    @Test
    public void testIntersects()
    {
        Bbox bbox = new Bbox(0, 0, 10, 10);
        Bbox bbox_within = new Bbox(1, 2, 5, 5);
        Bbox bbox_touchMinX = new Bbox(0, 2, 5, 5);
        Bbox bbox_touchMaxX = new Bbox(1, 2, 10, 5);
        Bbox bbox_touchCorner = new Bbox(-10, -3, 0, 0);
        Bbox bbox_crossMaxX = new Bbox(1, 2, 10, 15);
        Bbox bbox_null = new Bbox();
        
        assertTrue(bbox.intersects(bbox_within));
        assertTrue(bbox.intersects(bbox_touchMinX));
        assertTrue(bbox.intersects(bbox_touchMaxX));
        assertTrue(bbox.intersects(bbox_touchCorner));
        assertTrue(bbox.intersects(bbox_crossMaxX));
        assertFalse(bbox.intersects(bbox_null));
    }
    
    
    @Test
    public void testErrorIncompatibleCRS()
    {
        Bbox bbox1 = new Bbox(0, 0, 10, 10);
        Bbox bbox2 = new Bbox(1, 2, 5, 5);
        
        bbox1.setCrs("crs1");
        bbox2.setCrs("crs2");
        checkIncompatibleCRS(bbox1, bbox2);
        
        bbox1.setCrs(null);
        bbox2.setCrs("crs2");
        checkIncompatibleCRS(bbox1, bbox2);
        
        bbox1.setCrs("crs1");
        bbox2.setCrs(null);
        checkIncompatibleCRS(bbox1, bbox2);
        
        bbox1.setCrs("crs1");
        bbox2.setCrs("crs1");
        bbox1.contains(bbox2);
    }
    
    
    protected void checkIncompatibleCRS(Bbox bbox1, Bbox bbox2)
    {
        assertThrows(IllegalArgumentException.class, () -> {
            bbox1.contains(bbox2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            bbox1.intersects(bbox2);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            bbox1.add(bbox2);
        });
    }

}
