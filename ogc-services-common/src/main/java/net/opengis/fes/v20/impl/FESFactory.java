/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20.impl;

import net.opengis.fes.v20.After;
import net.opengis.fes.v20.And;
import net.opengis.fes.v20.AnyInteracts;
import net.opengis.fes.v20.BBOX;
import net.opengis.fes.v20.Before;
import net.opengis.fes.v20.Begins;
import net.opengis.fes.v20.BegunBy;
import net.opengis.fes.v20.Beyond;
import net.opengis.fes.v20.BinarySpatialOp;
import net.opengis.fes.v20.BinaryTemporalOp;
import net.opengis.fes.v20.ComparisonOperator;
import net.opengis.fes.v20.Contains;
import net.opengis.fes.v20.Crosses;
import net.opengis.fes.v20.DWithin;
import net.opengis.fes.v20.Disjoint;
import net.opengis.fes.v20.DistanceBuffer;
import net.opengis.fes.v20.During;
import net.opengis.fes.v20.EndedBy;
import net.opengis.fes.v20.Ends;
import net.opengis.fes.v20.Equals;
import net.opengis.fes.v20.FilterCapabilities;
import net.opengis.fes.v20.GMLExpression;
import net.opengis.fes.v20.Intersects;
import net.opengis.fes.v20.Literal;
import net.opengis.fes.v20.LowerBoundary;
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
import net.opengis.fes.v20.TContains;
import net.opengis.fes.v20.TEquals;
import net.opengis.fes.v20.TOverlaps;
import net.opengis.fes.v20.TemporalCapabilities;
import net.opengis.fes.v20.TemporalOperator;
import net.opengis.fes.v20.TemporalOperatorName;
import net.opengis.fes.v20.Touches;
import net.opengis.fes.v20.UpperBoundary;
import net.opengis.fes.v20.Factory;
import net.opengis.fes.v20.ValueReference;
import net.opengis.fes.v20.Within;
import net.opengis.gml.v32.AbstractGeometry;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.Envelope;
import net.opengis.ows.v11.Domain;
import net.opengis.ows.v11.impl.DomainImpl;


public class FESFactory implements Factory
{ 
    
    public FESFactory()
    {
    }
    
    
    @Override
    public PropertyIsLike newPropertyIsLike()
    {
        return new PropertyIsLikeImpl();
    }
    
    
    @Override
    public PropertyIsNull newPropertyIsNull()
    {
        return new PropertyIsNullImpl();
    }
    
    
    @Override
    public PropertyIsNil newPropertyIsNil()
    {
        return new PropertyIsNilImpl();
    }
    
    
    @Override
    public PropertyIsBetween newPropertyIsBetween()
    {
        return new PropertyIsBetweenImpl();
    }
    
    
    @Override
    public LowerBoundary newLowerBoundary()
    {
        return new LowerBoundaryImpl();
    }
    
    
    @Override
    public UpperBoundary newUpperBoundary()
    {
        return new UpperBoundaryImpl();
    }
    
    
    @Override
    public BBOX newBBOX()
    {
        return new BBOXImpl();
    }
    
    
    @Override
    public DistanceBuffer newDistanceBuffer()
    {
        return new DistanceBufferImpl();
    }
    
    
    @Override
    public Measure newMeasure()
    {
        return new MeasureImpl();
    }
    
    
    @Override
    public Literal newLiteral()
    {
        return new LiteralImpl();
    }
    
    
    @Override
    public ScalarCapabilities newScalarCapabilities()
    {
        return new ScalarCapabilitiesImpl();
    }
    
    
    @Override
    public ComparisonOperator newComparisonOperator()
    {
        return new ComparisonOperatorImpl();
    }
    
    
    @Override
    public SpatialCapabilities newSpatialCapabilities()
    {
        return new SpatialCapabilitiesImpl();
    }
    
    
    @Override
    public SpatialOperator newSpatialOperator()
    {
        return new SpatialOperatorImpl();
    }
    
    
    @Override
    public TemporalCapabilities newTemporalCapabilities()
    {
        return new TemporalCapabilitiesImpl();
    }
    
    
    @Override
    public TemporalOperator newTemporalOperator()
    {
        return new TemporalOperatorImpl();
    }
    
    
    @Override
    public PropertyIsEqualTo newPropertyIsEqualTo()
    {
        return new PropertyIsEqualToImpl();
    }
    
    
    @Override
    public PropertyIsNotEqualTo newPropertyIsNotEqualTo()
    {
        return new PropertyIsNotEqualToImpl();
    }
    
    
    @Override
    public PropertyIsLessThan newPropertyIsLessThan()
    {
        return new PropertyIsLessThanImpl();
    }
    
    
    @Override
    public PropertyIsGreaterThan newPropertyIsGreaterThan()
    {
        return new PropertyIsGreaterThanImpl();
    }
    
    
    @Override
    public PropertyIsLessThanOrEqualTo newPropertyIsLessThanOrEqualTo()
    {
        return new PropertyIsLessThanOrEqualToImpl();
    }
    
    
    @Override
    public PropertyIsGreaterThanOrEqualTo newPropertyIsGreaterThanOrEqualTo()
    {
        return new PropertyIsGreaterThanOrEqualToImpl();
    }
    
    
    @Override
    public Equals newEquals()
    {
        return new EqualsImpl();
    }
    
    
    @Override
    public Disjoint newDisjoint()
    {
        return new DisjointImpl();
    }
    
    
    @Override
    public Touches newTouches()
    {
        return new TouchesImpl();
    }
    
    
    @Override
    public Within newWithin()
    {
        return new WithinImpl();
    }
    
    
    @Override
    public Overlaps newOverlaps()
    {
        return new OverlapsImpl();
    }
    
    
    @Override
    public Crosses newCrosses()
    {
        return new CrossesImpl();
    }
    
    
    @Override
    public Intersects newIntersects()
    {
        return new IntersectsImpl();
    }
    
    
    @Override
    public Contains newContains()
    {
        return new ContainsImpl();
    }
    
    
    @Override
    public DWithin newDWithin()
    {
        return new DWithinImpl();
    }
    
    
    @Override
    public Beyond newBeyond()
    {
        return new BeyondImpl();
    }
    
    
    @Override
    public After newAfter()
    {
        return new AfterImpl();
    }
    
    
    @Override
    public Before newBefore()
    {
        return new BeforeImpl();
    }
    
    
    @Override
    public Begins newBegins()
    {
        return new BeginsImpl();
    }
    
    
    @Override
    public BegunBy newBegunBy()
    {
        return new BegunByImpl();
    }
    
    
    @Override
    public TContains newTContains()
    {
        return new TContainsImpl();
    }
    
    
    @Override
    public During newDuring()
    {
        return new DuringImpl();
    }
    
    
    @Override
    public EndedBy newEndedBy()
    {
        return new EndedByImpl();
    }
    
    
    @Override
    public Ends newEnds()
    {
        return new EndsImpl();
    }
    
    
    @Override
    public TEquals newTEquals()
    {
        return new TEqualsImpl();
    }
    
    
    @Override
    public Meets newMeets()
    {
        return new MeetsImpl();
    }
    
    
    @Override
    public MetBy newMetBy()
    {
        return new MetByImpl();
    }
    
    
    @Override
    public TOverlaps newTOverlaps()
    {
        return new TOverlapsImpl();
    }
    
    
    @Override
    public OverlappedBy newOverlappedBy()
    {
        return new OverlappedByImpl();
    }
    
    
    @Override
    public AnyInteracts newAnyInteracts()
    {
        return new AnyInteractsImpl();
    }
    
    
    @Override
    public And newAnd()
    {
        return new AndImpl();
    }
    
    
    @Override
    public Or newOr()
    {
        return new OrImpl();
    }
    
    
    @Override
    public Not newNot()
    {
        return new NotImpl();
    }
    
    
    @Override
    public final GMLExpression newGmlExpression(Object gmlObj)
    {
        return new GMLExpressionImpl(gmlObj);
    }


    @Override
    public final FilterCapabilities newFilterCapabilities()
    {
        return new FilterCapabilitiesImpl();
    }
    
    
    @Override
    public final Domain newConstraint(String name)
    {
        Domain constraint = new DomainImpl();
        constraint.setName(name);
        return constraint;
    }
    
    
    @Override
    public final Domain newConstraint(String name, String defaultValue)
    {
        Domain constraint = new DomainImpl();
        constraint.setName(name);
        constraint.setDefaultValue(defaultValue);
        return constraint;
    }


    @Override
    public final ValueReference newValueReference(String val)
    {
        ValueReference valRef = new ValueReferenceImpl();
        valRef.setValue(val);
        return valRef;
    }


    @Override
    public final Literal newLiteral(String val)
    {
        Literal lt = new LiteralImpl();
        lt.setValue(val);
        return lt;
    }
    
    
    public BinaryTemporalOp newTemporalOp(TemporalOperatorName opName, String propRef, AbstractTimeGeometricPrimitive timePrim)
    {
        BinaryTemporalOp binaryOp;
        
        switch (opName)
        {
            case AFTER:
                binaryOp = newAfter();
                break;
                
            case BEFORE:
                binaryOp = newBefore();
                break;
                
            case BEGINS:
                binaryOp = newBegins();
                break;
                
            case BEGUN_BY:
                binaryOp = newBegunBy();
                break;
                
            case DURING:
                binaryOp = newDuring();
                break;
                
            case ENDED_BY:
                binaryOp = newEndedBy();
                break;
                
            case ENDS:
                binaryOp = newEnds();
                break;
                
            case MEETS:
                binaryOp = newMeets();
                break;
                
            case MET_BY:
                binaryOp = newMetBy();
                break;
                
            case OVERLAPPED_BY:
                binaryOp = newOverlappedBy();
                break;
                
            case T_CONTAINS:
                binaryOp = newTContains();
                break;
                
            case T_EQUALS:
                binaryOp = newTEquals();
                break;
                
            case T_OVERLAPS:
                binaryOp = newTOverlaps();
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported temporal operator " + opName);
        }
        
        binaryOp.setOperand1(newValueReference(propRef));
        binaryOp.setOperand2(newGmlExpression(timePrim));
        return binaryOp;
    }
    
    
    public BinarySpatialOp newSpatialOp(SpatialOperatorName opName, String propRef, AbstractGeometry geom)
    {
        BinarySpatialOp binaryOp;
        
        switch (opName)
        {                
            case CONTAINS:
                binaryOp = newContains();
                break;
                
            case CROSSES:
                binaryOp = newCrosses();
                break;
                
            case DISJOINT:
                binaryOp = newDisjoint();
                break;
                
            case EQUALS:
                binaryOp = newEquals();
                break;
                
            case INTERSECTS:
                binaryOp = newIntersects();
                break;
                
            case OVERLAPS:
                binaryOp = newOverlaps();
                break;
                
            case TOUCHES:
                binaryOp = newTouches();
                break;
                
            case WITHIN:
                binaryOp = newWithin();
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported spatial operator " + opName); 
        }
        
        binaryOp.setOperand1(newValueReference(propRef));
        binaryOp.setOperand2(newGmlExpression(geom));
        return binaryOp;
    }
    
    
    public DistanceBuffer newSpatialBufferOp(SpatialOperatorName opName, String propRef, AbstractGeometry geom, double distance)
    {
        DistanceBuffer bufferOp;
        
        switch (opName)
        {
            case BEYOND:
                bufferOp = newBeyond();
                break;
                
            case D_WITHIN:
                bufferOp = newDWithin();
                break;
                
            default:
                throw new IllegalArgumentException("Unsupported spatial operator " + opName); 
        }
        
        bufferOp.setOperand1(newValueReference(propRef));
        bufferOp.setOperand2(newGmlExpression(geom));
        return bufferOp;
    }
    
    
    public BBOX newBBOX(String propRef, Envelope env)
    {
        BBOX bbox = newBBOX();
        bbox.setOperand1(newValueReference(propRef));
        bbox.setOperand2(newGmlExpression(env));
        return bbox;
    }
}
