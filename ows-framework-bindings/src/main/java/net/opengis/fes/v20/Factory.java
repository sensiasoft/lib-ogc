package net.opengis.fes.v20;


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
