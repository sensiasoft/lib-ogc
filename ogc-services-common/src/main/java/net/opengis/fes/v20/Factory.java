/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.fes.v20;

import net.opengis.ows.v11.Domain;


public interface Factory
{
    
    public PropertyIsLike newPropertyIsLike();
    
    
    public PropertyIsNull newPropertyIsNull();
    
    
    public PropertyIsNil newPropertyIsNil();
    
    
    public PropertyIsBetween newPropertyIsBetween();
    
    
    public LowerBoundary newLowerBoundary();
    
    
    public UpperBoundary newUpperBoundary();
    
    
    public BBOX newBBOX();
    
    
    public DistanceBuffer newDistanceBuffer();
    
    
    public Measure newMeasure();
    
    
    public ValueReference newValueReference(String val);
    
    
    public Literal newLiteral();
    
    
    public Literal newLiteral(String val);
    
    
    public GMLExpression newGmlExpression(Object gmlObj);
    
    
    public FilterCapabilities newFilterCapabilities();
    
        
    public Domain newConstraint(String name);
    
    
    public Domain newConstraint(String name, String defaultValue);
    
    
    public ScalarCapabilities newScalarCapabilities();
        
    
    public ComparisonOperator newComparisonOperator();
    
    
    public SpatialCapabilities newSpatialCapabilities();
    
    
    public SpatialOperator newSpatialOperator();
    
    
    public TemporalCapabilities newTemporalCapabilities();
    
    
    public TemporalOperator newTemporalOperator();
    
    
    public PropertyIsEqualTo newPropertyIsEqualTo();
    
    
    public PropertyIsNotEqualTo newPropertyIsNotEqualTo();
    
    
    public PropertyIsLessThan newPropertyIsLessThan();
    
    
    public PropertyIsGreaterThan newPropertyIsGreaterThan();
    
    
    public PropertyIsLessThanOrEqualTo newPropertyIsLessThanOrEqualTo();
    
    
    public PropertyIsGreaterThanOrEqualTo newPropertyIsGreaterThanOrEqualTo();
    
    
    public Equals newEquals();
    
    
    public Disjoint newDisjoint();
    
    
    public Touches newTouches();
    
    
    public Within newWithin();
    
    
    public Overlaps newOverlaps();
    
    
    public Crosses newCrosses();
    
    
    public Intersects newIntersects();
    
    
    public Contains newContains();
    
    
    public DWithin newDWithin();
    
    
    public Beyond newBeyond();
    
    
    public After newAfter();
    
    
    public Before newBefore();
    
    
    public Begins newBegins();
    
    
    public BegunBy newBegunBy();
    
    
    public TContains newTContains();
    
    
    public During newDuring();
    
    
    public EndedBy newEndedBy();
    
    
    public Ends newEnds();
    
    
    public TEquals newTEquals();
    
    
    public Meets newMeets();
    
    
    public MetBy newMetBy();
    
    
    public TOverlaps newTOverlaps();
    
    
    public OverlappedBy newOverlappedBy();
    
    
    public AnyInteracts newAnyInteracts();
    
    
    public And newAnd();
    
    
    public Or newOr();
    
    
    public Not newNot();

}
