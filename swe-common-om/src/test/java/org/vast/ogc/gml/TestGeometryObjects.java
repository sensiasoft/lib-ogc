/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import org.junit.Test;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;


public class TestGeometryObjects
{
    
    @Test
    public void testBuildPoint2D()
    {
        GMLBuilders gml = new GMLBuilders(false);
        Point point;
        
        point = gml.createPoint()
            .coordinates(33.65888,45.12499)
            .build();
        System.out.println(point);
    }
    
    
    @Test
    public void testBuildPoint3D()
    {
        GMLBuilders gml = new GMLBuilders(false);
        Point point;
        
        point = gml.createPoint()
            .coordinates(-76.817978500001,13.122848800001,356)
            .build();
        System.out.println(point);
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void testBuildPointWrongDimension()
    {
        GMLBuilders gml = new GMLBuilders(false);
        Point point;
        
        point = gml.createPoint()
            .coordinates(0,0, 1,1, 2,3, 2,0, 0.5,0)
            .srsDims(2)
            .build();
        System.out.println(point);
    }
    
    
    @Test
    public void testBuildLineString2D()
    {
        GMLBuilders gml = new GMLBuilders(false);
        LineString line;
        
        line = gml.createLineString()
            .srsDims(2)
            .coordinates(0,0, 1,1, 2,3, 2,0, 0.5,0)
            .build();
        System.out.println(line);
    }
    
    
    @Test
    public void testBuildLineString3D()
    {
        GMLBuilders gml = new GMLBuilders(false);
        LineString line;
        
        line = gml.createLineString()
            .srsDims(3)
            .coordinates(0,0,0, 1,1,1, 2,2,2, 3,3,3)
            .build();
        System.out.println(line);
    }
    
    
    @Test(expected=IllegalStateException.class)
    public void testBuildLineStringWrongDimension()
    {
        GMLBuilders gml = new GMLBuilders(false);
        LineString line;
        
        line = gml.createLineString()
            .srsDims(3)
            .coordinates(0, 0, 1, 1, 2, 1, 2, 0, 0.5, 0)
            .build();
        System.out.println(line);
    }
    
    
    @Test
    public void testBuildPolygon2D()
    {
        GMLBuilders gml = new GMLBuilders(false);
        Polygon poly;
        
        // no holes
        poly = gml.createPolygon()
            .exterior(0, 0, 1, 0, 1, 1, 0, 1, 0, 0)
            .build();
        System.out.println(poly);
        
        // 1 hole
        poly = gml.createPolygon()
            .exterior(0, 0, 1.5, 0, 1.5, 2.5, 0, 1.3, 0, 0)
            .interior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .build();
        System.out.println(poly);
        
        // several holes
        poly = gml.createPolygon()
            .exterior(0, 0, 1.5, 0, 1.5, 2.5, 0, 1.3, 0, 0)
            .interior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .interior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .interior(0.4, 0.4, 0.7, 0.4, 0.7, 0.7, 0.4, 0.7, 0.4, 0.4)
            .build();
        System.out.println(poly);        
    }

}
