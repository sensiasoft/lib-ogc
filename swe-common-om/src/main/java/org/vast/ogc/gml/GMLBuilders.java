/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import org.vast.util.Asserts;
import org.vast.util.BaseBuilder;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.LineString;
import net.opengis.gml.v32.LinearRing;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Polygon;
import net.opengis.gml.v32.impl.GMLFactory;


/**
 * <p>
 * Utility class to build GML objects with a fluent API
 * </p>
 *
 * @author Alex Robin
 * @date Sep 20, 2023
 */
public class GMLBuilders
{
    static final String MIN_COORDINATES_ERROR_MSG = "Must have at least {} coordinates";
    static final String INVALID_NUMBER_COORDINATES_ERROR_MSG = "Invalid number of coordinates. Must be multiple of SRS dimension";
    
    GMLFactory gmlFac;
    
    
    public GMLBuilders()
    {
        this(true);
    }
    
    
    public GMLBuilders(boolean useJTS)
    {
        this.gmlFac = new GMLFactory(useJTS);
    }
    
    
    public GMLBuilders(GMLFactory gmlFac)
    {
        this.gmlFac = gmlFac;
    }
    
    
    public PointBuilder createPoint()
    {
        return new PointBuilder(gmlFac);
    }
    
    
    public LineStringBuilder createLineString()
    {
        return new LineStringBuilder(gmlFac);
    }
    
    
    public PolygonBuilder createPolygon()
    {
        return new PolygonBuilder(gmlFac);
    }
    
    
    /*
     * Base builder for all Geometry objects
     */
    @SuppressWarnings("unchecked")
    public static abstract class GeometryBuilder<
            B extends GeometryBuilder<B, T>,
            T extends AbstractGeometry>
        extends BaseBuilder<T>
    {
        GMLFactory fac;
        
        protected GeometryBuilder(GMLFactory fac)
        {
            this.fac = Asserts.checkNotNull(fac, GMLFactory.class);
        }

        /**
         * Copy all info from another geometry
         * @param base Component to copy from
         * @return This builder for chaining
         */
        public B copyFrom(AbstractGeometry base)
        {
            instance.setId(base.getId());
            instance.setSrsName(base.getSrsName());
            instance.setSrsDimension(base.getSrsDimension());
            return (B)this;
        }

        /**
         * Sets the geometry ID
         * @param id
         * @return This builder for chaining
         */
        public B id(String id)
        {
            instance.setId(id);
            return (B)this;
        }

        /**
         * Sets the URI of the geometry CRS
         * @param srs
         * @return This builder for chaining
         */
        public B srsName(String srs)
        {
            instance.setSrsName(srs);
            return (B)this;
        }

        /**
         * Sets the geometry SRS dimensions
         * @param numDims
         * @return This builder for chaining
         */
        public B srsDims(int numDims)
        {
            instance.setSrsDimension(numDims);
            return (B)this;
        }

        @Override
        public T build()
        {
            T c = super.build();
            return c;
        }
    }

    /**
     * Builder for point geometry
     */
    public static class PointBuilder extends GeometryBuilder<PointBuilder, Point>
    {
        
        public PointBuilder(GMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPoint();
        }

        public PointBuilder from(Point base)
        {
            this.instance = base;
            return (PointBuilder)this;
        }

        /**
         * Copy all info from another point
         * @param base geometry to copy from
         * @return This builder for chaining
         */
        public PointBuilder copyFrom(AbstractGeometry base)
        {
            super.copyFrom(base);
            
            return this;
        }
        
        
        public PointBuilder coordinates(double... coords)
        {
            instance.setSrsDimension(coords.length);
            instance.setPos(coords);
            return this;
        }
        
        @Override
        public Point build()
        {
            Asserts.checkNotNull(instance.getPos(), "pos");
            Asserts.checkState(instance.getPos().length >= 2, MIN_COORDINATES_ERROR_MSG, 2);
            Asserts.checkState(instance.getPos().length == instance.getSrsDimension(), INVALID_NUMBER_COORDINATES_ERROR_MSG);
            return super.build();
        }
    }
    
    
    /**
     * Builder for LineString geometry
     */
    public class LineStringBuilder extends GeometryBuilder<LineStringBuilder, LineString>
    {
        protected LineStringBuilder(GMLFactory fac)
        {
            super(fac);
            this.instance = fac.newLineString();
        }
        
        public LineStringBuilder coordinates(double... posList)
        {
            instance.setPosList(posList);
            return this;
        }
        
        @Override
        public LineString build()
        {
            Asserts.checkNotNull(instance.getPosList(), "posList");
            Asserts.checkState(instance.getPosList().length >= 4, MIN_COORDINATES_ERROR_MSG, 4);
            Asserts.checkState(instance.getPosList().length % instance.getSrsDimension() == 0, INVALID_NUMBER_COORDINATES_ERROR_MSG);
            return super.build();
        }
    }
    
    
    public class PolygonBuilder extends GeometryBuilder<PolygonBuilder, Polygon>
    {       
        protected PolygonBuilder(GMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPolygon();
        }
        
        public PolygonBuilder exterior(double... posList)
        {
            LinearRing exterior = gmlFac.newLinearRing();
            exterior.setPosList(posList);
            instance.setExterior(exterior);
            return this;
        }
        
        public PolygonBuilder interior(double... posList)
        {
            LinearRing hole = gmlFac.newLinearRing();
            hole.setPosList(posList);
            instance.addInterior(hole);
            return this;
        }
        
        @Override
        public Polygon build()
        {
            Asserts.checkState(instance.isSetExterior(), "No exterior set");
            Asserts.checkNotNull(instance.getExterior().getPosList(), "posList");
            Asserts.checkState(instance.getExterior().getPosList().length >= 6, MIN_COORDINATES_ERROR_MSG, 3);
            Asserts.checkState(instance.getExterior().getPosList().length % instance.getSrsDimension() == 0, INVALID_NUMBER_COORDINATES_ERROR_MSG);
            return super.build();
        }
    }
}