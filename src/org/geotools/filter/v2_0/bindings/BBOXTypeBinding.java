/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.filter.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v1_0.OGC;
import org.geotools.filter.v1_0.OGCBBOXTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.GML;
import org.geotools.referencing.CRS;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * Binding object for the type http://www.opengis.net/ogc:BBOXType.
 *
 * <p>
 *        <pre>
 *       <code>
 *  &lt;xsd:complexType name="BBOXType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="fes:SpatialOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element minOccurs="0" ref="fes:expression"/&gt;
 *                  &lt;xsd:any namespace="##other"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt; 
 *              
 *        </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class BBOXTypeBinding extends OGCBBOXTypeBinding {
    private FilterFactory factory;
    
    public BBOXTypeBinding() {
        //(JD) TODO: fix this. The reason we dont use constructor injection to get 
        // the factory is that pico does not do both setter + constructor injection
        // And since we support setter injection of a crs we just fall back on 
        // common factory finder... since there is actually only one filter factory
        // impl not a huge deal, but it woul dbe nice to be consistent
        factory = CommonFactoryFinder.getFilterFactory(null);
    }
    
    public QName getTarget() {
        return FES.BBOXType;
    }
    
    @Override
    public Object getProperty(Object object, QName name)
        throws Exception {
        BBOX box = (BBOX) object;
    
        //&lt;xsd:element ref="ogc:PropertyName"/&gt;
        if (FES.expression.equals(name)) {
            return factory.property(box.getPropertyName());
        }
    
        return null;
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        List<Object> literalValues = new ArrayList<Object>(1);
        Literal literal = (Literal) ((BBOX)object).getExpression2();
        literalValues.add(new Object[] {org.geotools.gml3.v3_2.GML.Envelope, ((Geometry)((Literal)literal).getValue()).getEnvelopeInternal()});
        return literalValues;
    }
}
