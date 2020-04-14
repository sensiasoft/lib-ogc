/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20.bind;

import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import net.opengis.AbstractXMLStreamBindings;
import javax.xml.namespace.QName;
import net.opengis.fes.v20.After;
import net.opengis.fes.v20.And;
import net.opengis.fes.v20.AnyInteracts;
import net.opengis.fes.v20.BBOX;
import net.opengis.fes.v20.Before;
import net.opengis.fes.v20.Begins;
import net.opengis.fes.v20.BegunBy;
import net.opengis.fes.v20.Beyond;
import net.opengis.fes.v20.BinaryComparisonOp;
import net.opengis.fes.v20.BinaryLogicOp;
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.BinaryTemporalOp;
import net.opengis.fes.v20.ComparisonOperator;
import net.opengis.fes.v20.ComparisonOperatorName;
import net.opengis.fes.v20.ComparisonOps;
import net.opengis.fes.v20.Contains;
import net.opengis.fes.v20.Crosses;
import net.opengis.fes.v20.DWithin;
import net.opengis.fes.v20.Disjoint;
import net.opengis.fes.v20.DistanceBuffer;
import net.opengis.fes.v20.During;
import net.opengis.fes.v20.EndedBy;
import net.opengis.fes.v20.Ends;
import net.opengis.fes.v20.Equals;
import net.opengis.fes.v20.Expression;
import net.opengis.fes.v20.FilterCapabilities;
import net.opengis.fes.v20.FilterPredicate;
import net.opengis.fes.v20.GMLExpression;
import net.opengis.fes.v20.Intersects;
import net.opengis.fes.v20.Literal;
import net.opengis.fes.v20.LogicOps;
import net.opengis.fes.v20.LowerBoundary;
import net.opengis.fes.v20.MatchAction;
import net.opengis.fes.v20.Measure;
import net.opengis.fes.v20.Meets;
import net.opengis.fes.v20.MetBy;
import net.opengis.fes.v20.Not;
import net.opengis.fes.v20.Or;
import net.opengis.fes.v20.OverlappedBy;
import net.opengis.fes.v20.Overlaps;
import net.opengis.fes.v20.PropertyIsBetween;
import net.opengis.fes.v20.PropertyIsEqualTo;
import net.opengis.fes.v20.PropertyIsGreaterThan;
import net.opengis.fes.v20.PropertyIsGreaterThanOrEqualTo;
import net.opengis.fes.v20.PropertyIsLessThan;
import net.opengis.fes.v20.PropertyIsLessThanOrEqualTo;
import net.opengis.fes.v20.PropertyIsLike;
import net.opengis.fes.v20.PropertyIsNil;
import net.opengis.fes.v20.PropertyIsNotEqualTo;
import net.opengis.fes.v20.PropertyIsNull;
import net.opengis.fes.v20.ScalarCapabilities;
import net.opengis.fes.v20.SpatialCapabilities;
import net.opengis.fes.v20.SpatialOperator;
import net.opengis.fes.v20.SpatialOperatorName;
import net.opengis.fes.v20.SpatialOps;
import net.opengis.fes.v20.TContains;
import net.opengis.fes.v20.TEquals;
import net.opengis.fes.v20.TOverlaps;
import net.opengis.fes.v20.TemporalCapabilities;
import net.opengis.fes.v20.TemporalOperator;
import net.opengis.fes.v20.TemporalOperatorName;
import net.opengis.fes.v20.TemporalOps;
import net.opengis.fes.v20.Touches;
import net.opengis.fes.v20.UnaryLogicOp;
import net.opengis.fes.v20.UpperBoundary;
import net.opengis.fes.v20.ValueReference;
import net.opengis.fes.v20.Within;
import net.opengis.fes.v20.Factory;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.Envelope;
import net.opengis.ows.v11.Domain;
import net.opengis.ows.v11.Range;


@SuppressWarnings("javadoc")
public class XMLStreamBindings extends AbstractXMLStreamBindings
{
    public final static String NS_URI = "http://www.opengis.net/fes/2.0";
    public final static String OWS_NS_URI = "http://www.opengis.net/ows/1.1";
    
    protected Factory factory;
    protected net.opengis.gml.v32.bind.XMLStreamBindings gmlBindings;
        
    
    public XMLStreamBindings(Factory factory, net.opengis.gml.v32.bind.XMLStreamBindings gmlBindings)
    {
        this.factory = factory;
        this.gmlBindings = gmlBindings;
    }
    
    
    /**
     * Reads the BinaryComparisonOpType complex type
     */
    public final void readBinaryComparisonOpType(XMLStreamReader reader, BinaryComparisonOp bean) throws XMLStreamException
    {
        reader.nextTag();
        this.readBinaryComparisonOpTypeElements(reader, bean);
    }
    
    
    /**
     * Reads attributes of BinaryComparisonOpType complex type
     */
    public final void readBinaryComparisonOpTypeAttributes(Map<String, String> attrMap, BinaryComparisonOp bean) throws XMLStreamException
    {
        String val;
        
        // matchcase
        val = attrMap.get("matchCase");
        if (val != null)
            bean.setMatchCase(getBooleanFromString(val));
        
        // matchaction
        val = attrMap.get("matchAction");
        if (val != null)
            bean.setMatchAction(MatchAction.fromString(val));
    }
    
    
    /**
     * Reads elements of BinaryComparisonOpType complex type
     */
    public final void readBinaryComparisonOpTypeElements(XMLStreamReader reader, BinaryComparisonOp bean) throws XMLStreamException
    {
        // operand1
        bean.setOperand1(this.readExpression(reader));
        reader.nextTag();
        
        // operand2
        bean.setOperand2(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for BinaryComparisonOpType complex type
     */
    public final void writeBinaryComparisonOpType(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        this.writeBinaryComparisonOpTypeAttributes(writer, bean);
        this.writeBinaryComparisonOpTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of BinaryComparisonOpType complex type
     */
    public final void writeBinaryComparisonOpTypeAttributes(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {        
        // matchCase
        if (bean.isSetMatchCase())
            writer.writeAttribute("matchCase", getStringValue(bean.getMatchCase()));
        
        // matchAction
        if (bean.isSetMatchAction())
            writer.writeAttribute("matchAction", getStringValue(bean.getMatchAction()));
    }
    
    
    /**
     * Writes elements of BinaryComparisonOpType complex type
     */
    public final void writeBinaryComparisonOpTypeElements(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        // operand1
        this.writeExpression(writer, bean.getOperand1());
        
        // operand2
        this.writeExpression(writer, bean.getOperand2());
    }
    
    
    /**
     * Read method for PropertyIsLikeType complex type
     */
    public final PropertyIsLike readPropertyIsLikeType(XMLStreamReader reader) throws XMLStreamException
    {
        PropertyIsLike bean = factory.newPropertyIsLike();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readPropertyIsLikeTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readPropertyIsLikeTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of PropertyIsLikeType complex type
     */
    public final void readPropertyIsLikeTypeAttributes(Map<String, String> attrMap, PropertyIsLike bean) throws XMLStreamException
    {
        String val;
        
        // wildcard
        val = attrMap.get("wildCard");
        if (val != null)
            bean.setWildCard(val);
        
        // singlechar
        val = attrMap.get("singleChar");
        if (val != null)
            bean.setSingleChar(val);
        
        // escapechar
        val = attrMap.get("escapeChar");
        if (val != null)
            bean.setEscapeChar(val);
    }
    
    
    /**
     * Reads elements of PropertyIsLikeType complex type
     */
    public final void readPropertyIsLikeTypeElements(XMLStreamReader reader, PropertyIsLike bean) throws XMLStreamException
    {
        // operand1
        bean.setOperand1(this.readExpression(reader));
        reader.nextTag();
        
        // operand2
        bean.setOperand2(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for PropertyIsLikeType complex type
     */
    public final void writePropertyIsLikeType(XMLStreamWriter writer, PropertyIsLike bean) throws XMLStreamException
    {
        this.writePropertyIsLikeTypeAttributes(writer, bean);
        this.writePropertyIsLikeTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of PropertyIsLikeType complex type
     */
    public final void writePropertyIsLikeTypeAttributes(XMLStreamWriter writer, PropertyIsLike bean) throws XMLStreamException
    {        
        // wildCard
        writer.writeAttribute("wildCard", getStringValue(bean.getWildCard()));
        
        // singleChar
        writer.writeAttribute("singleChar", getStringValue(bean.getSingleChar()));
        
        // escapeChar
        writer.writeAttribute("escapeChar", getStringValue(bean.getEscapeChar()));
    }
    
    
    /**
     * Writes elements of PropertyIsLikeType complex type
     */
    public final void writePropertyIsLikeTypeElements(XMLStreamWriter writer, PropertyIsLike bean) throws XMLStreamException
    {
        // operand1
        this.writeExpression(writer, bean.getOperand1());
        
        // operand2
        this.writeExpression(writer, bean.getOperand2());
    }
    
    
    /**
     * Read method for PropertyIsNullType complex type
     */
    public final PropertyIsNull readPropertyIsNullType(XMLStreamReader reader) throws XMLStreamException
    {
        PropertyIsNull bean = factory.newPropertyIsNull();
        
        reader.nextTag();
        this.readPropertyIsNullTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of PropertyIsNullType complex type
     */
    public final void readPropertyIsNullTypeElements(XMLStreamReader reader, PropertyIsNull bean) throws XMLStreamException
    {
        // operand
        bean.setOperand(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for PropertyIsNullType complex type
     */
    public final void writePropertyIsNullType(XMLStreamWriter writer, PropertyIsNull bean) throws XMLStreamException
    {
        this.writePropertyIsNullTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of PropertyIsNullType complex type
     */
    public final void writePropertyIsNullTypeElements(XMLStreamWriter writer, PropertyIsNull bean) throws XMLStreamException
    {        
        // operand
        this.writeExpression(writer, bean.getOperand());
    }
    
    
    /**
     * Read method for PropertyIsNilType complex type
     */
    public final PropertyIsNil readPropertyIsNilType(XMLStreamReader reader) throws XMLStreamException
    {
        PropertyIsNil bean = factory.newPropertyIsNil();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readPropertyIsNilTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readPropertyIsNilTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of PropertyIsNilType complex type
     */
    public final void readPropertyIsNilTypeAttributes(Map<String, String> attrMap, PropertyIsNil bean) throws XMLStreamException
    {
        String val;
        
        // nilreason
        val = attrMap.get("nilReason");
        if (val != null)
            bean.setNilReason(val);
    }
    
    
    /**
     * Reads elements of PropertyIsNilType complex type
     */
    public final void readPropertyIsNilTypeElements(XMLStreamReader reader, PropertyIsNil bean) throws XMLStreamException
    {
        // operand
        bean.setOperand(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for PropertyIsNilType complex type
     */
    public final void writePropertyIsNilType(XMLStreamWriter writer, PropertyIsNil bean) throws XMLStreamException
    {
        this.writePropertyIsNilTypeAttributes(writer, bean);
        this.writePropertyIsNilTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of PropertyIsNilType complex type
     */
    public final void writePropertyIsNilTypeAttributes(XMLStreamWriter writer, PropertyIsNil bean) throws XMLStreamException
    {        
        // nilReason
        if (bean.isSetNilReason())
            writer.writeAttribute("nilReason", getStringValue(bean.getNilReason()));
    }
    
    
    /**
     * Writes elements of PropertyIsNilType complex type
     */
    public final void writePropertyIsNilTypeElements(XMLStreamWriter writer, PropertyIsNil bean) throws XMLStreamException
    {
        // operand
        this.writeExpression(writer, bean.getOperand());
    }
    
    
    /**
     * Read method for PropertyIsBetweenType complex type
     */
    public final PropertyIsBetween readPropertyIsBetweenType(XMLStreamReader reader) throws XMLStreamException
    {
        PropertyIsBetween bean = factory.newPropertyIsBetween();
        
        reader.nextTag();
        this.readPropertyIsBetweenTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of PropertyIsBetweenType complex type
     */
    public final void readPropertyIsBetweenTypeElements(XMLStreamReader reader, PropertyIsBetween bean) throws XMLStreamException
    {
        boolean found;
        
        // expression
        bean.setOperand(this.readExpression(reader));
        reader.nextTag();
                
        // LowerBoundary
        found = checkElementName(reader, "LowerBoundary");
        if (found)
        {
            bean.setLowerBoundary(this.readLowerBoundaryType(reader));
            reader.nextTag();
        }
        
        // UpperBoundary
        found = checkElementName(reader, "UpperBoundary");
        if (found)
        {
            bean.setUpperBoundary(this.readUpperBoundaryType(reader));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for PropertyIsBetweenType complex type
     */
    public final void writePropertyIsBetweenType(XMLStreamWriter writer, PropertyIsBetween bean) throws XMLStreamException
    {
        this.writePropertyIsBetweenTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of PropertyIsBetweenType complex type
     */
    public final void writePropertyIsBetweenTypeElements(XMLStreamWriter writer, PropertyIsBetween bean) throws XMLStreamException
    {
        // expression
        this.writeExpression(writer, bean.getOperand());
        
        // LowerBoundary
        writer.writeStartElement(NS_URI, "LowerBoundary");
        this.writeLowerBoundaryType(writer, bean.getLowerBoundary());
        writer.writeEndElement();
        
        // UpperBoundary
        writer.writeStartElement(NS_URI, "UpperBoundary");
        this.writeUpperBoundaryType(writer, bean.getUpperBoundary());
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for LowerBoundaryType complex type
     */
    public final LowerBoundary readLowerBoundaryType(XMLStreamReader reader) throws XMLStreamException
    {
        LowerBoundary bean = factory.newLowerBoundary();
        
        reader.nextTag();
        this.readLowerBoundaryTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of LowerBoundaryType complex type
     */
    public final void readLowerBoundaryTypeElements(XMLStreamReader reader, LowerBoundary bean) throws XMLStreamException
    {
        // expression
        bean.setExpression(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for LowerBoundaryType complex type
     */
    public final void writeLowerBoundaryType(XMLStreamWriter writer, LowerBoundary bean) throws XMLStreamException
    {
        this.writeLowerBoundaryTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of LowerBoundaryType complex type
     */
    public final void writeLowerBoundaryTypeElements(XMLStreamWriter writer, LowerBoundary bean) throws XMLStreamException
    {
        this.writeExpression(writer, bean.getExpression());
    }


    /**
     * Read method for UpperBoundaryType complex type
     */
    public final UpperBoundary readUpperBoundaryType(XMLStreamReader reader) throws XMLStreamException
    {
        UpperBoundary bean = factory.newUpperBoundary();
        
        reader.nextTag();
        this.readUpperBoundaryTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of UpperBoundaryType complex type
     */
    public final void readUpperBoundaryTypeElements(XMLStreamReader reader, UpperBoundary bean) throws XMLStreamException
    {
        boolean found;
        
        // expression
        found = checkElementName(reader, "expression");
        if (found)
        {
            bean.setExpression(this.readExpression(reader));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for UpperBoundaryType complex type
     */
    public final void writeUpperBoundaryType(XMLStreamWriter writer, UpperBoundary bean) throws XMLStreamException
    {
        this.writeUpperBoundaryTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of UpperBoundaryType complex type
     */
    public final void writeUpperBoundaryTypeElements(XMLStreamWriter writer, UpperBoundary bean) throws XMLStreamException
    {        
        // expression
        writer.writeStartElement(NS_URI, "expression");
        this.writeExpression(writer, bean.getExpression());
        writer.writeEndElement();
    }
    
    
    /**
     * Reads the BinarySpatialOpType complex type
     */
    public final void readBinarySpatialOpType(XMLStreamReader reader, BinarySpatialOp bean) throws XMLStreamException
    {
        reader.nextTag();
        this.readBinarySpatialOpTypeElements(reader, bean);
    }
    
    
    /**
     * Reads elements of BinarySpatialOpType complex type
     */
    public final void readBinarySpatialOpTypeElements(XMLStreamReader reader, BinarySpatialOp bean) throws XMLStreamException
    {
        // operand1
        bean.setOperand1(this.readExpression(reader));
        reader.nextTag();
        
        // operand2
        bean.setOperand2(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for BinarySpatialOpType complex type
     */
    public final void writeBinarySpatialOpType(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        this.writeBinarySpatialOpTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of BinarySpatialOpType complex type
     */
    public final void writeBinarySpatialOpTypeElements(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        // operand1
        this.writeExpression(writer, bean.getOperand1());
        
        // operand2
        this.writeExpression(writer, bean.getOperand2());
    }
    
    
    /**
     * Reads the BinaryTemporalOpType complex type
     */
    public final void readBinaryTemporalOpType(XMLStreamReader reader, BinaryTemporalOp bean) throws XMLStreamException
    {
        reader.nextTag();
        this.readBinaryTemporalOpTypeElements(reader, bean);
    }
    
    
    /**
     * Reads elements of BinaryTemporalOpType complex type
     */
    public final void readBinaryTemporalOpTypeElements(XMLStreamReader reader, BinaryTemporalOp bean) throws XMLStreamException
    {
        // operand1
        bean.setOperand1(this.readExpression(reader));
        reader.nextTag();
        
        // operand2
        bean.setOperand2(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for BinaryTemporalOpType complex type
     */
    public final void writeBinaryTemporalOpType(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        this.writeBinaryTemporalOpTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of BinaryTemporalOpType complex type
     */
    public final void writeBinaryTemporalOpTypeElements(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {        
        // operand1
        this.writeExpression(writer, bean.getOperand1());
        
        // operand2
        this.writeExpression(writer, bean.getOperand2());
    }
    
    
    /**
     * Read method for BBOXType complex type
     */
    public final BBOX readBBOXType(XMLStreamReader reader) throws XMLStreamException
    {
        BBOX bean = factory.newBBOX();
        
        reader.nextTag();
        this.readBBOXTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of BBOXType complex type
     */
    public final void readBBOXTypeElements(XMLStreamReader reader, BBOX bean) throws XMLStreamException
    {
        // operand1
        bean.setOperand1(this.readExpression(reader));
        reader.nextTag();
        
        // operand2
        bean.setOperand2(this.readExpression(reader));
        reader.nextTag();
    }
    
    
    /**
     * Write method for BBOXType complex type
     */
    public final void writeBBOXType(XMLStreamWriter writer, BBOX bean) throws XMLStreamException
    {
        this.writeBBOXTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of BBOXType complex type
     */
    public final void writeBBOXTypeElements(XMLStreamWriter writer, BBOX bean) throws XMLStreamException
    {
        // operand1
        this.writeExpression(writer, bean.getOperand1());
        
        // operand2
        this.writeExpression(writer, bean.getOperand2());
    }
    
    
    /**
     * Read method for DistanceBufferType complex type
     */
    public final DistanceBuffer readDistanceBufferType(XMLStreamReader reader) throws XMLStreamException
    {
        DistanceBuffer bean = factory.newDistanceBuffer();
        
        reader.nextTag();
        this.readDistanceBufferTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of DistanceBufferType complex type
     */
    public final void readDistanceBufferTypeElements(XMLStreamReader reader, DistanceBuffer bean) throws XMLStreamException
    {
        boolean found;
        
        // operand1
        bean.setOperand1(this.readExpression(reader));
        reader.nextTag();
        
        // operand2
        bean.setOperand2(this.readExpression(reader));
        reader.nextTag();
        
        // Distance
        found = checkElementName(reader, "Distance");
        if (found)
        {
            bean.setDistance(this.readMeasureType(reader));
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for DistanceBufferType complex type
     */
    public final void writeDistanceBufferType(XMLStreamWriter writer, DistanceBuffer bean) throws XMLStreamException
    {
        this.writeDistanceBufferTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of DistanceBufferType complex type
     */
    public final void writeDistanceBufferTypeElements(XMLStreamWriter writer, DistanceBuffer bean) throws XMLStreamException
    {        
        // operand1
        this.writeExpression(writer, bean.getOperand1());
                
        // operand2
        this.writeExpression(writer, bean.getOperand2());
        
        // Distance
        writer.writeStartElement(NS_URI, "Distance");
        this.writeMeasureType(writer, bean.getDistance());
        writer.writeEndElement();
    }
    
    
    /**
     * Reads the BinaryLogicOpType complex type
     */
    public final void readBinaryLogicOpType(XMLStreamReader reader, BinaryLogicOp bean) throws XMLStreamException
    {
        reader.nextTag();
        this.readBinaryLogicOpTypeElements(reader, bean);
    }
    
    
    /**
     * Reads elements of BinaryLogicOpType complex type
     */
    public final void readBinaryLogicOpTypeElements(XMLStreamReader reader, BinaryLogicOp bean) throws XMLStreamException
    {
        FilterPredicate predicate;
        
        // operand1
        predicate = this.readFilterPredicate(reader);
        bean.setOperand1(predicate);
        reader.nextTag();
        
        // operand2
        predicate = this.readFilterPredicate(reader);
        bean.setOperand2(predicate);
        reader.nextTag();
    }
    
    
    /**
     * Write method for BinaryLogicOpType complex type
     */
    public final void writeBinaryLogicOpType(XMLStreamWriter writer, BinaryLogicOp bean) throws XMLStreamException
    {
        this.writeBinaryLogicOpTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of BinaryLogicOpType complex type
     */
    public final void writeBinaryLogicOpTypeElements(XMLStreamWriter writer, BinaryLogicOp bean) throws XMLStreamException
    {        
        // operand1
        this.writeFilterPredicate(writer, bean.getOperand1());
        
        // operand2
        this.writeFilterPredicate(writer, bean.getOperand2());
    }
    
    
    /**
     * Read method for UnaryLogicOpType complex type
     */
    public final void readUnaryLogicOpType(XMLStreamReader reader, UnaryLogicOp bean) throws XMLStreamException
    {
        reader.nextTag();
        this.readUnaryLogicOpTypeElements(reader, bean);
    }
    
    
    /**
     * Reads elements of UnaryLogicOpType complex type
     */
    public final void readUnaryLogicOpTypeElements(XMLStreamReader reader, UnaryLogicOp bean) throws XMLStreamException
    {
        // operand
        FilterPredicate predicate = this.readFilterPredicate(reader);
        bean.setOperand(predicate);
        reader.nextTag();
    }
    
    
    /**
     * Write method for UnaryLogicOpType complex type
     */
    public final void writeUnaryLogicOpType(XMLStreamWriter writer, UnaryLogicOp bean) throws XMLStreamException
    {
        this.writeUnaryLogicOpTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of UnaryLogicOpType complex type
     */
    public final void writeUnaryLogicOpTypeElements(XMLStreamWriter writer, UnaryLogicOp bean) throws XMLStreamException
    {
        // operand
        this.writeFilterPredicate(writer, bean.getOperand());
    }
    
    
    /**
     * Read method for MeasureType complex type
     */
    public final Measure readMeasureType(XMLStreamReader reader) throws XMLStreamException
    {
        Measure bean = factory.newMeasure();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readMeasureTypeAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null)
            bean.setValue(getDoubleFromString(val));
        
        return bean;
    }
    
    
    /**
     * Reads attributes of MeasureType complex type
     */
    public final void readMeasureTypeAttributes(Map<String, String> attrMap, Measure bean) throws XMLStreamException
    {
        String val;
        
        // uom
        val = attrMap.get("uom");
        if (val != null)
            bean.setUom(val);
    }
    
    
    /**
     * Write method for MeasureType complex type
     */
    public final void writeMeasureType(XMLStreamWriter writer, Measure bean) throws XMLStreamException
    {
        this.writeMeasureTypeAttributes(writer, bean);
        
        writer.writeCharacters(getStringValue(bean.getValue()));
    }
    
    
    /**
     * Writes attributes of MeasureType complex type
     */
    public final void writeMeasureTypeAttributes(XMLStreamWriter writer, Measure bean) throws XMLStreamException
    {
        
        // uom
        writer.writeAttribute("uom", getStringValue(bean.getUom()));
    }
    
    
    /**
     * Read method for LiteralType complex type
     */
    public final Literal readLiteralType(XMLStreamReader reader) throws XMLStreamException
    {
        Literal bean = factory.newLiteral();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readLiteralTypeAttributes(attrMap, bean);
        
        String val = reader.getElementText();
        if (val != null)
            bean.setValue(trimStringValue(val));
        
        return bean;
    }
    
    
    /**
     * Reads attributes of LiteralType complex type
     */
    public final void readLiteralTypeAttributes(Map<String, String> attrMap, Literal bean) throws XMLStreamException
    {
        String val;
        
        // type
        val = attrMap.get("type");
        if (val != null)
            bean.setType(new QName(val));
    }
    
    
    /**
     * Write method for LiteralType complex type
     */
    public final void writeLiteralType(XMLStreamWriter writer, Literal bean) throws XMLStreamException
    {
        this.writeLiteralTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes attributes of LiteralType complex type
     */
    public final void writeLiteralTypeAttributes(XMLStreamWriter writer, Literal bean) throws XMLStreamException
    {        
        // type
        if (bean.isSetType())
            writer.writeAttribute("type", getStringValue(bean.getType()));
        
        // value
        writer.writeCharacters(bean.getValue());
    }
    
    
    /**
     * Read method for Scalar_CapabilitiesType complex type
     */
    public final ScalarCapabilities readScalarCapabilitiesType(XMLStreamReader reader) throws XMLStreamException
    {
        ScalarCapabilities bean = factory.newScalarCapabilities();
        
        reader.nextTag();
        this.readScalarCapabilitiesTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of Scalar_CapabilitiesType complex type
     */
    public final void readScalarCapabilitiesTypeElements(XMLStreamReader reader, ScalarCapabilities bean) throws XMLStreamException
    {
        boolean found;
        
        // LogicalOperators
        found = checkElementName(reader, "LogicalOperators");
        if (found)
        {
            bean.setLogicalOperators(true);
            reader.nextTag();
            reader.nextTag();
        }
        
        // ComparisonOperators
        found = checkElementName(reader, "ComparisonOperators");
        if (found)
        {
            reader.nextTag();
            
            do
            {
                found = checkElementName(reader, "ComparisonOperator");
                if (found)
                {
                    ComparisonOperator op = this.readComparisonOperatorType(reader);
                    bean.getComparisonOperators().add(op);                    
                    reader.nextTag();
                }
            }
            while (found);
        }
    }
    
    
    /**
     * Write method for Scalar_CapabilitiesType complex type
     */
    public final void writeScalarCapabilitiesType(XMLStreamWriter writer, ScalarCapabilities bean) throws XMLStreamException
    {
        this.writeScalarCapabilitiesTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of Scalar_CapabilitiesType complex type
     */
    public final void writeScalarCapabilitiesTypeElements(XMLStreamWriter writer, ScalarCapabilities bean) throws XMLStreamException
    {
        // LogicalOperators
        if (bean.getLogicalOperators())
            writer.writeEmptyElement(NS_URI, "LogicalOperators");
        
        // ComparisonOperators
        writer.writeStartElement(NS_URI, "ComparisonOperators");
        for (ComparisonOperator operator: bean.getComparisonOperators())
        {
            writer.writeStartElement(NS_URI, "ComparisonOperator");
            this.writeComparisonOperatorType(writer, operator);
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for ComparisonOperatorType complex type
     */
    public final ComparisonOperator readComparisonOperatorType(XMLStreamReader reader) throws XMLStreamException
    {
        ComparisonOperator bean = factory.newComparisonOperator();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readComparisonOperatorTypeAttributes(attrMap, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of ComparisonOperatorType complex type
     */
    public final void readComparisonOperatorTypeAttributes(Map<String, String> attrMap, ComparisonOperator bean) throws XMLStreamException
    {
        String val;
        
        // name
        val = attrMap.get("name");
        if (val != null)
            bean.setName(ComparisonOperatorName.fromString(val));
    }
    
    
    /**
     * Write method for ComparisonOperatorType complex type
     */
    public final void writeComparisonOperatorType(XMLStreamWriter writer, ComparisonOperator bean) throws XMLStreamException
    {
        this.writeComparisonOperatorTypeAttributes(writer, bean);
    }
    
    
    /**
     * Writes attributes of ComparisonOperatorType complex type
     */
    public final void writeComparisonOperatorTypeAttributes(XMLStreamWriter writer, ComparisonOperator bean) throws XMLStreamException
    {
        
        // name
        writer.writeAttribute("name", getStringValue(bean.getName()));
    }
    
    
    /**
     * Read method for Spatial_CapabilitiesType complex type
     */
    public final SpatialCapabilities readSpatialCapabilitiesType(XMLStreamReader reader) throws XMLStreamException
    {
        SpatialCapabilities bean = factory.newSpatialCapabilities();
        
        reader.nextTag();
        this.readSpatialCapabilitiesTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of Spatial_CapabilitiesType complex type
     */
    public final void readSpatialCapabilitiesTypeElements(XMLStreamReader reader, SpatialCapabilities bean) throws XMLStreamException
    {
        boolean found;
        
        // GeometryOperands
        this.readGeometryOperands(reader, bean.getGeometryOperands());
        
        // SpatialOperators
        found = checkElementName(reader, "SpatialOperators");
        if (found)
        {
            reader.nextTag();
            
            do
            {
                found = checkElementName(reader, "SpatialOperator");
                if (found)
                {
                    SpatialOperator op = this.readSpatialOperatorType(reader);
                    bean.getSpatialOperators().add(op);                    
                    reader.nextTag();
                }
            }
            while (found);
            
            reader.nextTag();
        }
    }
    
    
    private void readGeometryOperands(XMLStreamReader reader, List<QName> operandList) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "GeometryOperands");
        if (found)
        {
            reader.nextTag();
            
            do
            {
                found = checkElementName(reader, "GeometryOperand");
                if (found)
                {
                    operandList.add(getQNameFromString(reader.getAttributeValue(null, "name")));                    
                    reader.nextTag();
                    reader.nextTag();
                }
            }
            while (found);
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for Spatial_CapabilitiesType complex type
     */
    public final void writeSpatialCapabilitiesType(XMLStreamWriter writer, SpatialCapabilities bean) throws XMLStreamException
    {
        this.writeSpatialCapabilitiesTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of Spatial_CapabilitiesType complex type
     */
    public final void writeSpatialCapabilitiesTypeElements(XMLStreamWriter writer, SpatialCapabilities bean) throws XMLStreamException
    {        
        // GeometryOperands
        this.writeGeometryOperands(writer, bean.getGeometryOperands());        
        
        // SpatialOperators
        writer.writeStartElement(NS_URI, "SpatialOperators");
        for (SpatialOperator operator: bean.getSpatialOperators())
        {
            writer.writeStartElement(NS_URI, "SpatialOperator");
            this.writeSpatialOperatorType(writer, operator);
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
    
    
    private void writeGeometryOperands(XMLStreamWriter writer, List<QName> operandList) throws XMLStreamException
    {
        if (operandList.isEmpty())
            return;
        
        writer.writeStartElement(NS_URI, "GeometryOperands");
        
        for (QName opName: operandList)
        {
            writer.writeStartElement(NS_URI, "GeometryOperand");        
            writer.writeAttribute("name", getStringValue(opName));
            writer.writeEndElement();
        }
        
        writer.writeEndElement();
    }


    /**
     * Read method for SpatialOperatorType complex type
     */
    public final SpatialOperator readSpatialOperatorType(XMLStreamReader reader) throws XMLStreamException
    {
        SpatialOperator bean = factory.newSpatialOperator();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readSpatialOperatorTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readSpatialOperatorTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of SpatialOperatorType complex type
     */
    public final void readSpatialOperatorTypeAttributes(Map<String, String> attrMap, SpatialOperator bean) throws XMLStreamException
    {
        String val;
        
        // name
        val = attrMap.get("name");
        if (val != null)
            bean.setName(SpatialOperatorName.fromString(val));
    }
    
    
    /**
     * Reads elements of SpatialOperatorType complex type
     */
    public final void readSpatialOperatorTypeElements(XMLStreamReader reader, SpatialOperator bean) throws XMLStreamException
    {
        // GeometryOperands
        this.readGeometryOperands(reader, bean.getGeometryOperands());
    }
    
    
    /**
     * Write method for SpatialOperatorType complex type
     */
    public final void writeSpatialOperatorType(XMLStreamWriter writer, SpatialOperator bean) throws XMLStreamException
    {
        this.writeSpatialOperatorTypeAttributes(writer, bean);
        this.writeSpatialOperatorTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of SpatialOperatorType complex type
     */
    public final void writeSpatialOperatorTypeAttributes(XMLStreamWriter writer, SpatialOperator bean) throws XMLStreamException
    {        
        // name
        if (bean.isSetName())
            writer.writeAttribute("name", getStringValue(bean.getName()));
    }
    
    
    /**
     * Writes elements of SpatialOperatorType complex type
     */
    public final void writeSpatialOperatorTypeElements(XMLStreamWriter writer, SpatialOperator bean) throws XMLStreamException
    {
        // GeometryOperands
        this.writeGeometryOperands(writer, bean.getGeometryOperands());
    }
    
    
    /**
     * Read method for Temporal_CapabilitiesType complex type
     */
    public final TemporalCapabilities readTemporalCapabilitiesType(XMLStreamReader reader) throws XMLStreamException
    {
        TemporalCapabilities bean = factory.newTemporalCapabilities();
        
        reader.nextTag();
        this.readTemporalCapabilitiesTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads elements of Temporal_CapabilitiesType complex type
     */
    public final void readTemporalCapabilitiesTypeElements(XMLStreamReader reader, TemporalCapabilities bean) throws XMLStreamException
    {
        boolean found;
        
        // TemporalOperands
        this.readTemporalOperands(reader, bean.getTemporalOperands());
        
        // TemporalOperators
        found = checkElementName(reader, "TemporalOperators");
        if (found)
        {
            reader.nextTag();
            
            do
            {
                found = checkElementName(reader, "TemporalOperator");
                if (found)
                {
                    TemporalOperator op = this.readTemporalOperatorType(reader);
                    bean.getTemporalOperators().add(op);                    
                    reader.nextTag();
                }
            }
            while (found);
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Reads list of temporal operands
     */
    private void readTemporalOperands(XMLStreamReader reader, List<QName> operandList) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TemporalOperands");
        if (found)
        {
            reader.nextTag();
            
            do
            {
                found = checkElementName(reader, "TemporalOperand");
                if (found)
                {
                    operandList.add(getQNameFromString(reader.getAttributeValue(null, "name")));                    
                    reader.nextTag();
                    reader.nextTag();
                }
            }
            while (found);
            
            reader.nextTag();
        }
    }
    
    
    /**
     * Write method for Temporal_CapabilitiesType complex type
     */
    public final void writeTemporalCapabilitiesType(XMLStreamWriter writer, TemporalCapabilities bean) throws XMLStreamException
    {
        this.writeTemporalCapabilitiesTypeElements(writer, bean);
    }
    
    
    /**
     * Writes elements of Temporal_CapabilitiesType complex type
     */
    public final void writeTemporalCapabilitiesTypeElements(XMLStreamWriter writer, TemporalCapabilities bean) throws XMLStreamException
    {        
        // TemporalOperands
        this.writeTemporalOperands(writer, bean.getTemporalOperands());        
        
        // TemporalOperators
        writer.writeStartElement(NS_URI, "TemporalOperators");
        for (TemporalOperator operator: bean.getTemporalOperators())
        {
            writer.writeStartElement(NS_URI, "TemporalOperator");
            this.writeTemporalOperatorType(writer, operator);
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
    
    
    private void writeTemporalOperands(XMLStreamWriter writer, List<QName> operandList) throws XMLStreamException
    {
        if (operandList.isEmpty())
            return;
        
        writer.writeStartElement(NS_URI, "TemporalOperands");
        
        for (QName opName: operandList)
        {
            writer.writeStartElement(NS_URI, "TemporalOperand");        
            writer.writeAttribute("name", getStringValue(opName));
            writer.writeEndElement();
        }
        
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TemporalOperatorType complex type
     */
    public final TemporalOperator readTemporalOperatorType(XMLStreamReader reader) throws XMLStreamException
    {
        TemporalOperator bean = factory.newTemporalOperator();
        
        Map<String, String> attrMap = collectAttributes(reader);
        this.readTemporalOperatorTypeAttributes(attrMap, bean);
        
        reader.nextTag();
        this.readTemporalOperatorTypeElements(reader, bean);
        
        return bean;
    }
    
    
    /**
     * Reads attributes of TemporalOperatorType complex type
     */
    public final void readTemporalOperatorTypeAttributes(Map<String, String> attrMap, TemporalOperator bean) throws XMLStreamException
    {
        String val;
        
        // name
        val = attrMap.get("name");
        if (val != null)
            bean.setName(TemporalOperatorName.fromString(val));
    }
    
    
    /**
     * Reads elements of TemporalOperatorType complex type
     */
    public final void readTemporalOperatorTypeElements(XMLStreamReader reader, TemporalOperator bean) throws XMLStreamException
    {
        // TemporalOperands
        this.readTemporalOperands(reader, bean.getTemporalOperands());
    }
    
    
    /**
     * Write method for TemporalOperatorType complex type
     */
    public final void writeTemporalOperatorType(XMLStreamWriter writer, TemporalOperator bean) throws XMLStreamException
    {
        this.writeTemporalOperatorTypeAttributes(writer, bean);
        this.writeTemporalOperatorTypeElements(writer, bean);
    }
    
    
    /**
     * Writes attributes of TemporalOperatorType complex type
     */
    public final void writeTemporalOperatorTypeAttributes(XMLStreamWriter writer, TemporalOperator bean) throws XMLStreamException
    {        
        // name
        writer.writeAttribute("name", getStringValue(bean.getName()));
    }
    
    
    /**
     * Writes elements of TemporalOperatorType complex type
     */
    public final void writeTemporalOperatorTypeElements(XMLStreamWriter writer, TemporalOperator bean) throws XMLStreamException
    {
        // TemporalOperands
        this.writeTemporalOperands(writer, bean.getTemporalOperands());
    }
    
    
    /**
     * Dispatcher method for reading elements derived from comparisonOps
     */
    public final ComparisonOps readComparisonOps(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("PropertyIsEqualTo"))
            return readPropertyIsEqualTo(reader);
        else if (localName.equals("PropertyIsNotEqualTo"))
            return readPropertyIsNotEqualTo(reader);
        else if (localName.equals("PropertyIsLessThan"))
            return readPropertyIsLessThan(reader);
        else if (localName.equals("PropertyIsGreaterThan"))
            return readPropertyIsGreaterThan(reader);
        else if (localName.equals("PropertyIsLessThanOrEqualTo"))
            return readPropertyIsLessThanOrEqualTo(reader);
        else if (localName.equals("PropertyIsGreaterThanOrEqualTo"))
            return readPropertyIsGreaterThanOrEqualTo(reader);
        else if (localName.equals("PropertyIsLike"))
            return readPropertyIsLike(reader);
        else if (localName.equals("PropertyIsNull"))
            return readPropertyIsNull(reader);
        else if (localName.equals("PropertyIsNil"))
            return readPropertyIsNil(reader);
        else if (localName.equals("PropertyIsBetween"))
            return readPropertyIsBetween(reader);
        
        return null;
    }
    
    
    /**
     * Dispatcher method for writing classes derived from comparisonOps
     */
    public final void writeComparisonOps(XMLStreamWriter writer, ComparisonOps bean) throws XMLStreamException
    {
        if (bean instanceof PropertyIsEqualTo)
            writePropertyIsEqualTo(writer, (PropertyIsEqualTo)bean);
        else if (bean instanceof PropertyIsNotEqualTo)
            writePropertyIsNotEqualTo(writer, (PropertyIsNotEqualTo)bean);
        else if (bean instanceof PropertyIsLessThan)
            writePropertyIsLessThan(writer, (PropertyIsLessThan)bean);
        else if (bean instanceof PropertyIsGreaterThan)
            writePropertyIsGreaterThan(writer, (PropertyIsGreaterThan)bean);
        else if (bean instanceof PropertyIsLessThanOrEqualTo)
            writePropertyIsLessThanOrEqualTo(writer, (PropertyIsLessThanOrEqualTo)bean);
        else if (bean instanceof PropertyIsGreaterThanOrEqualTo)
            writePropertyIsGreaterThanOrEqualTo(writer, (PropertyIsGreaterThanOrEqualTo)bean);
        else if (bean instanceof PropertyIsLike)
            writePropertyIsLike(writer, (PropertyIsLike)bean);
        else if (bean instanceof PropertyIsNull)
            writePropertyIsNull(writer, (PropertyIsNull)bean);
        else if (bean instanceof PropertyIsNil)
            writePropertyIsNil(writer, (PropertyIsNil)bean);
        else if (bean instanceof PropertyIsBetween)
            writePropertyIsBetween(writer, (PropertyIsBetween)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    /**
     * Read method for PropertyIsEqualTo elements
     */
    public final PropertyIsEqualTo readPropertyIsEqualTo(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsEqualTo");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        PropertyIsEqualTo bean = factory.newPropertyIsEqualTo();        
        this.readBinaryComparisonOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for PropertyIsEqualTo element
     */
    public final void writePropertyIsEqualTo(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsEqualTo");
        this.writeNamespaces(writer);
        this.writeBinaryComparisonOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsNotEqualTo elements
     */
    public final PropertyIsNotEqualTo readPropertyIsNotEqualTo(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsNotEqualTo");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        PropertyIsNotEqualTo bean = factory.newPropertyIsNotEqualTo();        
        this.readBinaryComparisonOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for PropertyIsNotEqualTo element
     */
    public final void writePropertyIsNotEqualTo(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsNotEqualTo");
        this.writeNamespaces(writer);
        this.writeBinaryComparisonOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsLessThan elements
     */
    public final PropertyIsLessThan readPropertyIsLessThan(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsLessThan");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        PropertyIsLessThan bean = factory.newPropertyIsLessThan();        
        this.readBinaryComparisonOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for PropertyIsLessThan element
     */
    public final void writePropertyIsLessThan(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsLessThan");
        this.writeNamespaces(writer);
        this.writeBinaryComparisonOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsGreaterThan elements
     */
    public final PropertyIsGreaterThan readPropertyIsGreaterThan(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsGreaterThan");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        PropertyIsGreaterThan bean = factory.newPropertyIsGreaterThan();        
        this.readBinaryComparisonOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for PropertyIsGreaterThan element
     */
    public final void writePropertyIsGreaterThan(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsGreaterThan");
        this.writeNamespaces(writer);
        this.writeBinaryComparisonOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsLessThanOrEqualTo elements
     */
    public final PropertyIsLessThanOrEqualTo readPropertyIsLessThanOrEqualTo(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsLessThanOrEqualTo");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        PropertyIsLessThanOrEqualTo bean = factory.newPropertyIsLessThanOrEqualTo();        
        this.readBinaryComparisonOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for PropertyIsLessThanOrEqualTo element
     */
    public final void writePropertyIsLessThanOrEqualTo(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsLessThanOrEqualTo");
        this.writeNamespaces(writer);
        this.writeBinaryComparisonOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsGreaterThanOrEqualTo elements
     */
    public final PropertyIsGreaterThanOrEqualTo readPropertyIsGreaterThanOrEqualTo(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsGreaterThanOrEqualTo");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        PropertyIsGreaterThanOrEqualTo bean = factory.newPropertyIsGreaterThanOrEqualTo();        
        this.readBinaryComparisonOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for PropertyIsGreaterThanOrEqualTo element
     */
    public final void writePropertyIsGreaterThanOrEqualTo(XMLStreamWriter writer, BinaryComparisonOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsGreaterThanOrEqualTo");
        this.writeNamespaces(writer);
        this.writeBinaryComparisonOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsLike elements
     */
    public final PropertyIsLike readPropertyIsLike(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsLike");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readPropertyIsLikeType(reader);
    }
    
    
    /**
     * Write method for PropertyIsLike element
     */
    public final void writePropertyIsLike(XMLStreamWriter writer, PropertyIsLike bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsLike");
        this.writeNamespaces(writer);
        this.writePropertyIsLikeType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsNull elements
     */
    public final PropertyIsNull readPropertyIsNull(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsNull");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readPropertyIsNullType(reader);
    }
    
    
    /**
     * Write method for PropertyIsNull element
     */
    public final void writePropertyIsNull(XMLStreamWriter writer, PropertyIsNull bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsNull");
        this.writeNamespaces(writer);
        this.writePropertyIsNullType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsNil elements
     */
    public final PropertyIsNil readPropertyIsNil(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsNil");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readPropertyIsNilType(reader);
    }
    
    
    /**
     * Write method for PropertyIsNil element
     */
    public final void writePropertyIsNil(XMLStreamWriter writer, PropertyIsNil bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsNil");
        this.writeNamespaces(writer);
        this.writePropertyIsNilType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for PropertyIsBetween elements
     */
    public final PropertyIsBetween readPropertyIsBetween(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "PropertyIsBetween");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readPropertyIsBetweenType(reader);
    }
    
    
    /**
     * Write method for PropertyIsBetween element
     */
    public final void writePropertyIsBetween(XMLStreamWriter writer, PropertyIsBetween bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "PropertyIsBetween");
        this.writeNamespaces(writer);
        this.writePropertyIsBetweenType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from spatialOps
     */
    public final SpatialOps readSpatialOps(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("Equals"))
            return readEquals(reader);
        else if (localName.equals("Disjoint"))
            return readDisjoint(reader);
        else if (localName.equals("Touches"))
            return readTouches(reader);
        else if (localName.equals("Within"))
            return readWithin(reader);
        else if (localName.equals("Overlaps"))
            return readOverlaps(reader);
        else if (localName.equals("Crosses"))
            return readCrosses(reader);
        else if (localName.equals("Intersects"))
            return readIntersects(reader);
        else if (localName.equals("Contains"))
            return readContains(reader);
        else if (localName.equals("DWithin"))
            return readDWithin(reader);
        else if (localName.equals("Beyond"))
            return readBeyond(reader);
        else if (localName.equals("BBOX"))
            return readBBOX(reader);
        
        return null;
    }
    
    
    /**
     * Dispatcher method for writing classes derived from spatialOps
     */
    public final void writeSpatialOps(XMLStreamWriter writer, SpatialOps bean) throws XMLStreamException
    {
        if (bean instanceof Equals)
            writeEquals(writer, (Equals)bean);
        else if (bean instanceof Disjoint)
            writeDisjoint(writer, (Disjoint)bean);
        else if (bean instanceof Touches)
            writeTouches(writer, (Touches)bean);
        else if (bean instanceof Within)
            writeWithin(writer, (Within)bean);
        else if (bean instanceof Overlaps)
            writeOverlaps(writer, (Overlaps)bean);
        else if (bean instanceof Crosses)
            writeCrosses(writer, (Crosses)bean);
        else if (bean instanceof Intersects)
            writeIntersects(writer, (Intersects)bean);
        else if (bean instanceof Contains)
            writeContains(writer, (Contains)bean);
        else if (bean instanceof DWithin)
            writeDWithin(writer, (DWithin)bean);
        else if (bean instanceof Beyond)
            writeBeyond(writer, (Beyond)bean);
        else if (bean instanceof BBOX)
            writeBBOX(writer, (BBOX)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    /**
     * Read method for Equals elements
     */
    public final Equals readEquals(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Equals");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Equals bean = factory.newEquals();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Equals element
     */
    public final void writeEquals(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Equals");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Disjoint elements
     */
    public final Disjoint readDisjoint(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Disjoint");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Disjoint bean = factory.newDisjoint();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Disjoint element
     */
    public final void writeDisjoint(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Disjoint");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Touches elements
     */
    public final Touches readTouches(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Touches");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Touches bean = factory.newTouches();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Touches element
     */
    public final void writeTouches(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Touches");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Within elements
     */
    public final Within readWithin(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Within");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Within bean = factory.newWithin();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Within element
     */
    public final void writeWithin(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Within");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Overlaps elements
     */
    public final Overlaps readOverlaps(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Overlaps");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Overlaps bean = factory.newOverlaps();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Overlaps element
     */
    public final void writeOverlaps(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Overlaps");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Crosses elements
     */
    public final Crosses readCrosses(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Crosses");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Crosses bean = factory.newCrosses();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Crosses element
     */
    public final void writeCrosses(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Crosses");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Intersects elements
     */
    public final Intersects readIntersects(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Intersects");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Intersects bean = factory.newIntersects();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Intersects element
     */
    public final void writeIntersects(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Intersects");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Contains elements
     */
    public final Contains readContains(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Contains");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Contains bean = factory.newContains();        
        this.readBinarySpatialOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Contains element
     */
    public final void writeContains(XMLStreamWriter writer, BinarySpatialOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Contains");
        this.writeNamespaces(writer);
        this.writeBinarySpatialOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for DWithin elements
     */
    public final DistanceBuffer readDWithin(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "DWithin");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readDistanceBufferType(reader);
    }
    
    
    /**
     * Write method for DWithin element
     */
    public final void writeDWithin(XMLStreamWriter writer, DistanceBuffer bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "DWithin");
        this.writeNamespaces(writer);
        this.writeDistanceBufferType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Beyond elements
     */
    public final DistanceBuffer readBeyond(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Beyond");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readDistanceBufferType(reader);
    }
    
    
    /**
     * Write method for Beyond element
     */
    public final void writeBeyond(XMLStreamWriter writer, DistanceBuffer bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Beyond");
        this.writeNamespaces(writer);
        this.writeDistanceBufferType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for BBOX elements
     */
    public final BBOX readBBOX(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "BBOX");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readBBOXType(reader);
    }
    
    
    /**
     * Write method for BBOX element
     */
    public final void writeBBOX(XMLStreamWriter writer, BBOX bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "BBOX");
        this.writeNamespaces(writer);
        this.writeBBOXType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from temporalOps
     */
    public final TemporalOps readTemporalOps(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("After"))
            return readAfter(reader);
        else if (localName.equals("Before"))
            return readBefore(reader);
        else if (localName.equals("Begins"))
            return readBegins(reader);
        else if (localName.equals("BegunBy"))
            return readBegunBy(reader);
        else if (localName.equals("TContains"))
            return readTContains(reader);
        else if (localName.equals("During"))
            return readDuring(reader);
        else if (localName.equals("EndedBy"))
            return readEndedBy(reader);
        else if (localName.equals("Ends"))
            return readEnds(reader);
        else if (localName.equals("TEquals"))
            return readTEquals(reader);
        else if (localName.equals("Meets"))
            return readMeets(reader);
        else if (localName.equals("MetBy"))
            return readMetBy(reader);
        else if (localName.equals("TOverlaps"))
            return readTOverlaps(reader);
        else if (localName.equals("OverlappedBy"))
            return readOverlappedBy(reader);
        else if (localName.equals("AnyInteracts"))
            return readAnyInteracts(reader);
        
        return null;
    }
    
    
    /**
     * Dispatcher method for writing classes derived from temporalOps
     */
    public final void writeTemporalOps(XMLStreamWriter writer, TemporalOps bean) throws XMLStreamException
    {
        if (bean instanceof After)
            writeAfter(writer, (After)bean);
        else if (bean instanceof Before)
            writeBefore(writer, (Before)bean);
        else if (bean instanceof Begins)
            writeBegins(writer, (Begins)bean);
        else if (bean instanceof BegunBy)
            writeBegunBy(writer, (BegunBy)bean);
        else if (bean instanceof TContains)
            writeTContains(writer, (TContains)bean);
        else if (bean instanceof During)
            writeDuring(writer, (During)bean);
        else if (bean instanceof EndedBy)
            writeEndedBy(writer, (EndedBy)bean);
        else if (bean instanceof Ends)
            writeEnds(writer, (Ends)bean);
        else if (bean instanceof TEquals)
            writeTEquals(writer, (TEquals)bean);
        else if (bean instanceof Meets)
            writeMeets(writer, (Meets)bean);
        else if (bean instanceof MetBy)
            writeMetBy(writer, (MetBy)bean);
        else if (bean instanceof TOverlaps)
            writeTOverlaps(writer, (TOverlaps)bean);
        else if (bean instanceof OverlappedBy)
            writeOverlappedBy(writer, (OverlappedBy)bean);
        else if (bean instanceof AnyInteracts)
            writeAnyInteracts(writer, (AnyInteracts)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    /**
     * Read method for After elements
     */
    public final After readAfter(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "After");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        After bean = factory.newAfter();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for After element
     */
    public final void writeAfter(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "After");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Before elements
     */
    public final Before readBefore(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Before");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Before bean = factory.newBefore();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Before element
     */
    public final void writeBefore(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Before");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Begins elements
     */
    public final Begins readBegins(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Begins");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Begins bean = factory.newBegins();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Begins element
     */
    public final void writeBegins(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Begins");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for BegunBy elements
     */
    public final BegunBy readBegunBy(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "BegunBy");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        BegunBy bean = factory.newBegunBy();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for BegunBy element
     */
    public final void writeBegunBy(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "BegunBy");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TContains elements
     */
    public final TContains readTContains(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TContains");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        TContains bean = factory.newTContains();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for TContains element
     */
    public final void writeTContains(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TContains");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for During elements
     */
    public final During readDuring(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "During");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        During bean = factory.newDuring();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for During element
     */
    public final void writeDuring(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "During");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for EndedBy elements
     */
    public final EndedBy readEndedBy(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "EndedBy");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        EndedBy bean = factory.newEndedBy();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for EndedBy element
     */
    public final void writeEndedBy(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "EndedBy");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Ends elements
     */
    public final Ends readEnds(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Ends");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Ends bean = factory.newEnds();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Ends element
     */
    public final void writeEnds(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Ends");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TEquals elements
     */
    public final TEquals readTEquals(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TEquals");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        TEquals bean = factory.newTEquals();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for TEquals element
     */
    public final void writeTEquals(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TEquals");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Meets elements
     */
    public final Meets readMeets(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Meets");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Meets bean = factory.newMeets();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Meets element
     */
    public final void writeMeets(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Meets");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for MetBy elements
     */
    public final MetBy readMetBy(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "MetBy");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        MetBy bean = factory.newMetBy();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for MetBy element
     */
    public final void writeMetBy(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "MetBy");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for TOverlaps elements
     */
    public final TOverlaps readTOverlaps(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "TOverlaps");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        TOverlaps bean = factory.newTOverlaps();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for TOverlaps element
     */
    public final void writeTOverlaps(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "TOverlaps");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for OverlappedBy elements
     */
    public final OverlappedBy readOverlappedBy(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "OverlappedBy");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        OverlappedBy bean = factory.newOverlappedBy();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for OverlappedBy element
     */
    public final void writeOverlappedBy(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "OverlappedBy");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for AnyInteracts elements
     */
    public final AnyInteracts readAnyInteracts(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "AnyInteracts");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        AnyInteracts bean = factory.newAnyInteracts();        
        this.readBinaryTemporalOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for AnyInteracts element
     */
    public final void writeAnyInteracts(XMLStreamWriter writer, BinaryTemporalOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "AnyInteracts");
        this.writeNamespaces(writer);
        this.writeBinaryTemporalOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Dispatcher method for reading elements derived from logicOps
     */
    public final LogicOps readLogicOps(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("And"))
            return readAnd(reader);
        else if (localName.equals("Or"))
            return readOr(reader);
        else if (localName.equals("Not"))
            return readNot(reader);
        
        return null;
    }
    
    
    /**
     * Dispatcher method for writing classes derived from logicOps
     */
    public final void writeLogicOps(XMLStreamWriter writer, LogicOps bean) throws XMLStreamException
    {
        if (bean instanceof And)
            writeAnd(writer, (And)bean);
        else if (bean instanceof Or)
            writeOr(writer, (Or)bean);
        else if (bean instanceof Not)
            writeNot(writer, (Not)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
    }
    
    
    /**
     * Read method for And elements
     */
    public final And readAnd(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "And");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        And bean = factory.newAnd();        
        this.readBinaryLogicOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for And element
     */
    public final void writeAnd(XMLStreamWriter writer, BinaryLogicOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "And");
        this.writeNamespaces(writer);
        this.writeBinaryLogicOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Or elements
     */
    public final Or readOr(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Or");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Or bean = factory.newOr();        
        this.readBinaryLogicOpType(reader, bean);        
        return bean;
    }
    
    
    /**
     * Write method for Or element
     */
    public final void writeOr(XMLStreamWriter writer, BinaryLogicOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Or");
        this.writeNamespaces(writer);
        this.writeBinaryLogicOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Not elements
     */
    public final Not readNot(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Not");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        Not bean = factory.newNot();
        this.readUnaryLogicOpType(reader, bean);
        return bean;
    }
    
    
    /**
     * Write method for Not element
     */
    public final void writeNot(XMLStreamWriter writer, UnaryLogicOp bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Not");
        this.writeNamespaces(writer);
        this.writeUnaryLogicOpType(writer, bean);
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for ValueReference elements
     */
    private final ValueReference readValueReference(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "ValueReference");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return factory.newValueReference(reader.getElementText());
    }
    
    
    /**
     * Write method for Literal element
     */
    private final void writeValueReference(XMLStreamWriter writer, ValueReference bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "ValueReference");
        writer.writeCharacters(bean.getValue());
        writer.writeEndElement();
    }
    
    
    /**
     * Read method for Literal elements
     */
    private final Literal readLiteral(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Literal");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        return this.readLiteralType(reader);
    }
    
    
    /**
     * Write method for Literal element
     */
    private final void writeLiteral(XMLStreamWriter writer, Literal bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Literal");
        this.writeNamespaces(writer);
        this.writeLiteralType(writer, bean);
        writer.writeEndElement();
    }
    
    
    public final Expression readExpression(XMLStreamReader reader) throws XMLStreamException
    {
        String localName = reader.getName().getLocalPart();
        
        if (localName.equals("ValueReference"))
            return readValueReference(reader);
        else if (localName.equals("Literal"))
            return readLiteral(reader);
        
        // case of GML object
        else if (reader.getName().getNamespaceURI().equals(net.opengis.gml.v32.bind.XMLStreamBindings.NS_URI))
        {
            if (localName.equals("Envelope"))
                return factory.newGmlExpression(gmlBindings.readEnvelope(reader));
            else if (localName.contains("Time"))
                return factory.newGmlExpression(gmlBindings.readAbstractTimeGeometricPrimitive(reader));
            else
                return factory.newGmlExpression(gmlBindings.readAbstractGeometry(reader));
        }
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    public void writeExpression(XMLStreamWriter writer, Expression bean) throws XMLStreamException
    {
        if (bean instanceof ValueReference)
            writeValueReference(writer, (ValueReference)bean);
        else if (bean instanceof Literal)
            writeLiteral(writer, (Literal)bean);
        
        // case of GML object
        else if (bean instanceof GMLExpression)
        {
            Object gmlObj = ((GMLExpression)bean).getGmlObject();
            if (gmlObj instanceof AbstractTimeGeometricPrimitive)
                gmlBindings.writeAbstractTimeGeometricPrimitive(writer, (AbstractTimeGeometricPrimitive)gmlObj);
            else if (gmlObj instanceof AbstractGeometry)
                gmlBindings.writeAbstractGeometry(writer, (AbstractGeometry)gmlObj);
            else if (gmlObj instanceof Envelope)
                gmlBindings.writeEnvelope(writer, (Envelope)gmlObj);
            else
                throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + gmlObj.getClass().getCanonicalName()); 
        }
        
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());        
    }
    
    
    public final FilterPredicate readFilterPredicate(XMLStreamReader reader) throws XMLStreamException
    {
        FilterPredicate predicate;
        
        predicate = this.readComparisonOps(reader);
        if (predicate != null)
            return predicate;
        
        predicate = this.readSpatialOps(reader);
        if (predicate != null)
            return predicate;
        
        predicate = this.readTemporalOps(reader);
        if (predicate != null)
            return predicate;
        
        predicate = this.readLogicOps(reader);
        if (predicate != null)
            return predicate;
        
        throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
    }
    
    
    public void writeFilterPredicate(XMLStreamWriter writer, FilterPredicate bean) throws XMLStreamException
    {
        if (bean instanceof ComparisonOps)
            writeComparisonOps(writer, (ComparisonOps)bean);
        else if (bean instanceof SpatialOps)
            writeSpatialOps(writer, (SpatialOps)bean);
        else if (bean instanceof TemporalOps)
            writeTemporalOps(writer, (TemporalOps)bean);
        else if (bean instanceof LogicOps)
            writeLogicOps(writer, (LogicOps)bean);
        else
            throw new XMLStreamException(ERROR_UNSUPPORTED_TYPE + bean.getClass().getCanonicalName());
        
    }
    
    
    public FilterCapabilities readFilterCapabilities(XMLStreamReader reader) throws XMLStreamException
    {
        boolean found = checkElementName(reader, "Filter_Capabilities");
        if (!found)
            throw new XMLStreamException(ERROR_INVALID_ELT + reader.getName() + errorLocationString(reader));
        
        FilterCapabilities bean = factory.newFilterCapabilities();
        reader.nextTag();
        
        // TODO Conformance
        found = checkElementName(reader, "Conformance");
        if (found)
            this.skipElementAndAllChildren(reader);
                
        // TODO Id_Capabilities
        found = checkElementName(reader, "Id_Capabilities");
        if (found)
            this.skipElementAndAllChildren(reader);
        
        // Scalar_Capabilities
        found = checkElementName(reader, "Scalar_Capabilities");
        if (found)
        {
            bean.setScalarCapabilities(this.readScalarCapabilitiesType(reader));
            reader.nextTag();
        }
        
        // Spatial_Capabilities
        found = checkElementName(reader, "Spatial_Capabilities");
        if (found)
        {
            bean.setSpatialCapabilities(this.readSpatialCapabilitiesType(reader));
            reader.nextTag();
        }
        
        // Temporal_Capabilities
        found = checkElementName(reader, "Temporal_Capabilities");
        if (found)
        {
            bean.setTemporalCapabilities(this.readTemporalCapabilitiesType(reader));
            reader.nextTag();
        }
        
        // TODO Functions
        found = checkElementName(reader, "Functions");
        if (found)
            this.skipElementAndAllChildren(reader);
        
        // TODO Extended_Capabilities
        found = checkElementName(reader, "Extended_Capabilities");
        if (found)
            this.skipElementAndAllChildren(reader);
        
        return bean;
    }
    
    
    public void writeFilterCapabilities(XMLStreamWriter writer, FilterCapabilities bean) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Filter_Capabilities");
        
        // Conformance
        if (bean.getConformance() != null)
        {
            writer.writeStartElement(NS_URI, "Conformance");
            for (Domain constraint: bean.getConformance().getConstraintList())
                writeConstraint(writer, constraint);
            writer.writeEndElement();
        }
        
        // TODO IdCapabilities
        
        // Scalar_Capabilities
        if (bean.getScalarCapabilities() != null)
        {
            writer.writeStartElement(NS_URI, "Scalar_Capabilities");
            this.writeScalarCapabilitiesType(writer, bean.getScalarCapabilities());
            writer.writeEndElement();
        }
        
        // Spatial_Capabilities
        if (bean.getSpatialCapabilities() != null)
        {
            writer.writeStartElement(NS_URI, "Spatial_Capabilities");
            this.writeSpatialCapabilitiesType(writer, bean.getSpatialCapabilities());
            writer.writeEndElement();
        }
        
        // Temporal_Capabilities
        if (bean.getTemporalCapabilities() != null)
        {
            writer.writeStartElement(NS_URI, "Temporal_Capabilities");
            this.writeTemporalCapabilitiesType(writer, bean.getTemporalCapabilities());
            writer.writeEndElement();
        }
        
        // TODO Functions
        
        // TODO Extended_Capabilities
        
        writer.writeEndElement();
    }
   
    
    private void writeConstraint(XMLStreamWriter writer, Domain constraint) throws XMLStreamException
    {
        writer.writeStartElement(NS_URI, "Constraint");
        
        // for now we embed OWS stuff here directly
        writer.writeAttribute("name", constraint.getName());
        
        // possible values
        if (constraint.isSetNoValues())
            writer.writeEmptyElement(OWS_NS_URI, "NoValues");
        else if (constraint.isSetAnyValue())
            writer.writeEmptyElement(OWS_NS_URI, "NoValues");
        else
        {
            writer.writeStartElement(OWS_NS_URI, "AllowedValues");
            for (Object allowedVal: constraint.getAllowedValues())
            {
                if (allowedVal instanceof String)
                {
                    writer.writeStartElement(OWS_NS_URI, "Value");
                    writer.writeCharacters(constraint.getDefaultValue());
                    writer.writeEndElement();
                }
                else if (allowedVal instanceof Range)
                {
                    writer.writeStartElement(OWS_NS_URI, "Range");
                    writer.writeCharacters(constraint.getDefaultValue());
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        }
        // default value
        if (constraint.isSetDefaultValue())
        {
            writer.writeStartElement(OWS_NS_URI, "DefaultValue");
            writer.writeCharacters(constraint.getDefaultValue());
            writer.writeEndElement();
        }
        
        writer.writeEndElement();
    }
}
