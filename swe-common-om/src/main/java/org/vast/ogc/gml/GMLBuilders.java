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
import net.opengis.gml.v32.Point;
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
         * Sets the geometry CRS
         * @param srs
         * @return This builder for chaining
         */
        public B srsName(String srs)
        {
            instance.setSrsName(srs);
            return (B)this;
        }

        @Override
        public T build()
        {
            T c = super.build();
            return c;
        }
    }

    
    public static PointBuilder createPoint(boolean useJTS)
    {
        return new PointBuilder(new GMLFactory(useJTS));
    }

    /*
     * Base builder for point geometry
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
    }
}