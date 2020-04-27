/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.function.Consumer;
import org.vast.data.DateTimeOrDouble;
import org.vast.data.SWEFactory;
import org.vast.util.BaseBuilder;
import org.vast.util.NestedBuilder;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Utility class to build SWE Common data components with a fluent API
 * </p>
 *
 * @author Alex Robin
 * @date Apr 13, 2020
 */
public class SWEBuilders
{
    static SWEFactory factory = new SWEFactory();
    
    
    /**
     * @return A builder to create a new Boolean component
     */
    public static BooleanBuilder newBoolean()
    {        
        return newBoolean(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new Boolean component
     */
    public static BooleanBuilder newBoolean(SWEFactory fac)
    {
        return new BooleanBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new Category component
     */
    public static CategoryBuilder newCategory()
    {        
        return newCategory(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new Category component
     */
    public static CategoryBuilder newCategory(SWEFactory fac)
    {
        return new CategoryBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new Count component
     */
    public static CountBuilder newCount()
    {        
        return newCount(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new Count component
     */
    public static CountBuilder newCount(SWEFactory fac)
    {
        return new CountBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new Quantity component
     */
    public static QuantityBuilder newQuantity()
    {        
        return newQuantity(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new Quantity component
     */
    public static QuantityBuilder newQuantity(SWEFactory fac)
    {
        return new QuantityBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new Time component
     */
    public static TimeBuilder newTime()
    {        
        return newTime(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new Time component
     */
    public static TimeBuilder newTime(SWEFactory fac)
    {
        return new TimeBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new Text component
     */
    public static TextBuilder newText()
    {        
        return newText(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new Boolean component
     */
    public static TextBuilder newText(SWEFactory fac)
    {
        return new TextBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new DataRecord component
     */
    public static DataRecordBuilder newRecord()
    {        
        return newRecord(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new DataRecord component
     */
    public static DataRecordBuilder newRecord(SWEFactory fac)
    {
        return new DataRecordBuilder(fac);
    }
    
    
    /*
     * Base builder for all components
     */
    @SuppressWarnings("unchecked")
    protected static abstract class ComponentBuilder<
            B extends ComponentBuilder<B, T>,
            T extends DataComponent>
        extends BaseBuilder<T>
    {
        SWEFactory fac;
        Consumer<T> visitor;
        
        ComponentBuilder(SWEFactory fac)
        {
            this.fac = fac;
        }
                
        /**
         * Copy all info from another component
         * @param base Component to copy from
         * @return This builder for chaining
         */
        public B copyFrom(DataComponent base)
        {
            withID(base.getId());
            withLabel(base.getLabel());
            withDescription(base.getDescription());
            withDefinition(base.getDefinition());
            if (base.isSetOptional())
                setOptional(base.getOptional());
            if (base.isSetUpdatable())
                setUpdatable(base.getUpdatable());
            return (B)this;
        }        
        
        /**
         * Sets the component ID
         * @param id
         * @return This builder for chaining
         */
        public B withID(String id)
        {
            instance.setId(id);
            return (B)this;
        }
        
        /**
         * Sets the component name (usually only at the top level)
         * @param name
         * @return This builder for chaining
         */
        public B withName(String name)
        {
            instance.setName(name);
            return (B)this;
        }
        
        /**
         * Sets the component human-readable label
         * @param label
         * @return This builder for chaining
         */
        public B withLabel(String label)
        {
            instance.setLabel(label);
            return (B)this;
        }        
        
        /**
         * Sets the component human-readable description
         * @param description
         * @return This builder for chaining
         */
        public B withDescription(String description)
        {
            instance.setDescription(description);
            return (B)this;
        }        
        
        /**
         * Sets the component definition URI
         * @param defUri URI of definition (usually resolvable to the actual 
         * definition of observable or concept)
         * @return This builder for chaining
         */
        public B withDefinition(String defUri)
        {
            instance.setDefinition(defUri);
            return (B)this;
        }        
        
        /**
         * Sets the component optional flag
         * @param optional
         * @return  This builder for chaining
         */
        public B setOptional(boolean optional)
        {
            instance.setOptional(optional);
            return (B)this;
        }        
        
        /**
         * Sets the component updatable flag
         * @param updatable
         * @return  This builder for chaining
         */
        public B setUpdatable(boolean updatable)
        {
            instance.setUpdatable(updatable);
            return (B)this;
        }
        
        /**
         * Adds a visitor that will be called with the component after it is built
         * @param visitor Component visitor
         * @return This builder for chaining
         */
        public B visitor(Consumer<T> visitor)
        {
            this.visitor = visitor;
            return (B)this;
        }
        
        @Override
        public T build()
        {
            T c = super.build();
            if (visitor != null)
                visitor.accept(c);
            return c;
        }
    }
    
    
    /*
     * Base builder for all simple components 
     */
    @SuppressWarnings("unchecked")
    protected static abstract class SimpleComponentBuilder<
            B extends SimpleComponentBuilder<B, T>,
            T extends SimpleComponent>
        extends ComponentBuilder<B, T>
    {        
        SimpleComponentBuilder(SWEFactory fac)
        {
            super(fac);
        }

        protected B copyFrom(SimpleComponent base)
        {
            super.copyFrom(base);
            instance.setDataType(base.getDataType());
            instance.setReferenceFrame(base.getReferenceFrame());
            instance.setAxisID(base.getAxisID());
            // TODO copy quality
            instance.setNilValues(base.getNilValues());
            return (B)this;
        }        
        
        public B withDataType(DataType dataType)
        {
            instance.setDataType(dataType);
            return (B)this;
        }        
        
        public B withRefFrame(String refFrame, String axisId)
        {
            instance.setReferenceFrame(refFrame);
            instance.setAxisID(axisId);
            return (B)this;
        }
    }
    
    
    /**
     * <p>
     * Builder class for Boolean components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class BooleanBuilder extends BaseBooleanBuilder<BooleanBuilder>    
    {
        protected BooleanBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseBooleanBuilder<B extends BaseBooleanBuilder<B>> extends SimpleComponentBuilder<B, Boolean>
    {
        protected BaseBooleanBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newBoolean();
        }        
        
        public B copyFrom(Boolean base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }        
        
        public B withValue(boolean value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedBooleanBuilder<B> extends BaseBooleanBuilder<NestedBooleanBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedBooleanBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for Category components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class CategoryBuilder extends BaseCategoryBuilder<CategoryBuilder>    
    {
        protected CategoryBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseCategoryBuilder<B extends BaseCategoryBuilder<B>> extends SimpleComponentBuilder<B, Category>
    {
        protected BaseCategoryBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newCategory();
        }        
        
        public B copyFrom(Category base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }
        
        public B withCodeSpace(String uri)
        {
            instance.setCodeSpace(uri);
            return (B)this;
        }
        
        AllowedTokens ensureConstraint()
        {
            AllowedTokens constraint = instance.getConstraint();
            if (constraint == null)
            {
                constraint = fac.newAllowedTokens();
                instance.setConstraint(constraint);
            }
            
            return constraint;
        }
        
        public B withAllowedValues(String... values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (String val: values)
                constraint.addValue(val);
            return (B)this;
        }
        
        public B withAllowedValues(int... values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (int val: values)
                constraint.addValue(Integer.toString(val));
            return (B)this;
        }
        
        public B withValue(String value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedCategoryBuilder<B> extends BaseCategoryBuilder<NestedCategoryBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedCategoryBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for Count components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class CountBuilder extends BaseCountBuilder<CountBuilder>    
    {
        protected CountBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseCountBuilder<B extends BaseCountBuilder<B>> extends SimpleComponentBuilder<B, Count>
    {        
        protected BaseCountBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newCount();
        }
        
        public B copyFrom(Count base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }
        
        AllowedValues ensureConstraint()
        {
            AllowedValues constraint = instance.getConstraint();
            if (constraint == null)
            {
                constraint = fac.newAllowedValues();
                instance.setConstraint(constraint);
            }
            
            return constraint;
        }
        
        public B withAllowedValues(int... values)
        {
            AllowedValues constraint = ensureConstraint();
            for (int val: values)
                constraint.addValue(val);
            return (B)this;
        }
        
        public B withAllowedIntervals(int[]... intervals)
        {
            AllowedValues constraint = ensureConstraint();
            for (int[] val: intervals)
                constraint.addInterval(new double[] {val[0], val[1]});
            return (B)this;
        }
        
        public B withAllowedInterval(int min, int max)
        {
            AllowedValues constraint = ensureConstraint();
            constraint.addInterval(new double[] {min, max});
            return (B)this;
        }
        
        public B withValue(int value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedCountBuilder<B> extends BaseCountBuilder<NestedCountBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedCountBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for Quantity components
     * </p>
     * 
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class QuantityBuilder extends BaseQuantityBuilder<QuantityBuilder>    
    {
        protected QuantityBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseQuantityBuilder<B extends BaseQuantityBuilder<B>> extends SimpleComponentBuilder<B, Quantity>    
    {
        protected BaseQuantityBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newQuantity();
        }        
        
        public B copyFrom(Quantity base)
        {
            super.copyFrom(base);
            instance.setUom(base.getUom());
            instance.setValue(base.getValue());
            return (B)this;
        }        
        
        public B withUom(UnitReference uom)
        {
            instance.setUom(uom);
            return (B)this;
        }        
        
        public B withUomCode(String code)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setCode(code);
            instance.setUom(uom);
            return (B)this;
        }        
        
        public B withUomUri(String uri)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setHref(uri);
            instance.setUom(uom);
            return (B)this;
        }
        
        AllowedValues ensureConstraint()
        {
            AllowedValues constraint = instance.getConstraint();
            if (constraint == null)
            {
                constraint = fac.newAllowedValues();
                instance.setConstraint(constraint);
            }
            
            return constraint;
        }
        
        public B withAllowedValues(double... values)
        {
            AllowedValues constraint = ensureConstraint();
            for (double val: values)
                constraint.addValue(val);
            return (B)this;
        }
        
        public B withAllowedIntervals(double[]... intervals)
        {
            AllowedValues constraint = ensureConstraint();
            for (double[] val: intervals)
                constraint.addInterval(val);
            return (B)this;
        }
        
        public B withAllowedInterval(double min, double max)
        {
            AllowedValues constraint = ensureConstraint();
            constraint.addInterval(new double[] {min, max});
            return (B)this;
        }      
        
        public B withValue(double value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedQuantityBuilder<B> extends BaseQuantityBuilder<NestedQuantityBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedQuantityBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for Time components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class TimeBuilder extends BaseTimeBuilder<TimeBuilder>    
    {
        protected TimeBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseTimeBuilder<B extends BaseTimeBuilder<B>> extends SimpleComponentBuilder<B, Time>
    {
        protected BaseTimeBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newTime();
        }        
        
        public B copyFrom(Time base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }        
        
        public B withIsoUnit()
        {
            withUomUri(Time.ISO_TIME_UNIT);
            return (B)this;
        }        
        
        public B withUomCode(String code)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setCode(code);
            instance.setUom(uom);
            return (B)this;
        }        
        
        public B withUomUri(String uri)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setHref(uri);
            instance.setUom(uom);
            return (B)this;
        }        
        
        public B withTimeFrame(String defUri)
        {
            instance.setReferenceFrame(defUri);
            return (B)this;
        }        
        
        public B withUtcTimeFrame()
        {
            withTimeFrame(SWEConstants.TIME_REF_UTC);
            return (B)this;
        }        
        
        public B withGpsTimeFrame()
        {
            withTimeFrame(SWEConstants.TIME_REF_GPS);
            return (B)this;
        }        
        
        public B withTaiTimeFrame()
        {
            withTimeFrame(SWEConstants.TIME_REF_TAI);
            return (B)this;
        }
        
        public B withRefTime(Instant refTime)
        {
            instance.setReferenceTime(refTime.atOffset(ZoneOffset.UTC));
            return (B)this;
        }
        
        public B withValue(double value)
        {
            instance.setValue(new DateTimeOrDouble(value));
            return (B)this;
        }        
        
        public B withValue(Instant value)
        {
            instance.setValue(new DateTimeOrDouble(value.atOffset(ZoneOffset.UTC)));
            return (B)this;
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedTimeBuilder<B> extends BaseTimeBuilder<NestedTimeBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedTimeBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for Text components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class TextBuilder extends BaseTextBuilder<TextBuilder>    
    {
        protected TextBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseTextBuilder<B extends BaseTextBuilder<B>> extends SimpleComponentBuilder<B, Text>
    {
        protected BaseTextBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newText();
        }        
        
        public B copyFrom(Text base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }        
        
        public B withValue(String value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedTextBuilder<B> extends BaseTextBuilder<NestedTextBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedTextBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for DataRecord components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class DataRecordBuilder extends BaseDataRecordBuilder<DataRecordBuilder>    
    {
        protected DataRecordBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseDataRecordBuilder<B extends BaseDataRecordBuilder<B>> extends ComponentBuilder<B, DataRecord>
    {        
        protected BaseDataRecordBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newDataRecord();
        }
        
        protected B copyFrom(DataRecord base)
        {
            super.copyFrom(base);
            throw new UnsupportedOperationException("Not implemented yet");
            //return (B)this;
        }
        
        public B withField(String name, DataComponent field)
        {
            instance.addField(name, field);
            return (B)this;
        }
        
        public B withIsoTimeStampUTC(String name)
        {
            instance.addField(name, newTime()
                .withDefinition(SWEConstants.DEF_SAMPLING_TIME)
                .withLabel("Sampling Time")
                .withIsoUnit()
                .withUtcTimeFrame()
                .build());
            return (B)this;
        }
        
        public B withIsoTimeStampGPS(String name)
        {
            instance.addField(name, newTime()
                .withDefinition(SWEConstants.DEF_SAMPLING_TIME)
                .withLabel("Sampling Time")
                .withIsoUnit()
                .withGpsTimeFrame()
                .build());
            return (B)this;
        }
        
        public NestedBooleanBuilder<B> withBooleanField(String name)
        {
            return new NestedBooleanBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedCategoryBuilder<B> withCategoryField(String name)
        {
            return new NestedCategoryBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedCountBuilder<B> withCountField(String name)
        {
            return new NestedCountBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedQuantityBuilder<B> withQuantityField(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedTimeBuilder<B> withTimeField(String name)
        {
            return new NestedTimeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedTextBuilder<B> withTextField(String name)
        {
            return new NestedTextBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedRecordBuilder<B> withNestedRecord(String name)
        {
            return new NestedRecordBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedVectorBuilder<B> withNestedVector(String name)
        {
            return new NestedVectorBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedChoiceBuilder<B> withNestedChoice(String name)
        {
            return new NestedChoiceBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
        
        public NestedArrayBuilder<B> withNestedArray(String name)
        {
            return new NestedArrayBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.withField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedRecordBuilder<B> extends BaseDataRecordBuilder<NestedRecordBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedRecordBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for Vector components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 13, 2020
     */
    public static class VectorBuilder extends BaseVectorBuilder<VectorBuilder>    
    {
        protected VectorBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseVectorBuilder<B extends BaseVectorBuilder<B>> extends ComponentBuilder<B, Vector>
    {        
        protected BaseVectorBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newVector();
        }
        
        public B from(Vector base)
        {
            this.instance = base;
            return (B)this;
        }
        
        public B copyFrom(Vector base)
        {
            super.copyFrom(base);
            throw new UnsupportedOperationException("Not implemented yet");
            //return (B)this;
        }
        
        public B withCoordinate(String name, Quantity coord)
        {
            instance.addCoordinateAsQuantity(name, coord);
            return (B)this;
        }
        
        public B withCoordinate(String name, Count coord)
        {
            instance.addCoordinateAsCount(name, coord);
            return (B)this;
        }
        
        public B withCoordinate(String name, Time coord)
        {
            instance.addCoordinateAsTime(name, coord);
            return (B)this;
        }
        
        public NestedQuantityBuilder<B> withQuantityCoord(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseVectorBuilder.this.withCoordinate(name, build());
                    return (B)BaseVectorBuilder.this;
                }
            };
        }
        
        public NestedCountBuilder<B> withCountCoord(String name)
        {
            return new NestedCountBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseVectorBuilder.this.withCoordinate(name, build());
                    return (B)BaseVectorBuilder.this;
                }
            };
        }
        
        public NestedTimeBuilder<B> withTimeCoord(String name)
        {
            return new NestedTimeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseVectorBuilder.this.withCoordinate(name, build());
                    return (B)BaseVectorBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedVectorBuilder<B> extends BaseVectorBuilder<NestedVectorBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedVectorBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for DataChoice components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 24, 2020
     */
    public static class DataChoiceBuilder extends BaseDataChoiceBuilder<DataChoiceBuilder>    
    {
        protected DataChoiceBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseDataChoiceBuilder<B extends BaseDataChoiceBuilder<B>> extends ComponentBuilder<B, DataChoice>
    {        
        protected BaseDataChoiceBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newDataChoice();
        }
        
        protected B copyFrom(DataChoice base)
        {
            super.copyFrom(base);
            throw new UnsupportedOperationException("Not implemented yet");
            //return (B)this;
        }
        
        public B withItem(String name, DataComponent field)
        {
            instance.addItem(name, field);
            return (B)this;
        }
        
        public NestedBooleanBuilder<B> withBooleanField(String name)
        {
            return new NestedBooleanBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedCategoryBuilder<B> withCategoryField(String name)
        {
            return new NestedCategoryBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedCountBuilder<B> withCountField(String name)
        {
            return new NestedCountBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedQuantityBuilder<B> withQuantityField(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedTimeBuilder<B> withTimeField(String name)
        {
            return new NestedTimeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedTextBuilder<B> withTextField(String name)
        {
            return new NestedTextBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedRecordBuilder<B> withNestedRecord(String name)
        {
            return new NestedRecordBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedVectorBuilder<B> withNestedVector(String name)
        {
            return new NestedVectorBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
        
        public NestedChoiceBuilder<B> withNestedchoice(String name)
        {
            return new NestedChoiceBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.withItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedChoiceBuilder<B> extends BaseDataChoiceBuilder<NestedChoiceBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedChoiceBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for DataArray components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 24, 2020
     */
    public static class DataArrayBuilder extends BaseDataArrayBuilder<DataArrayBuilder>    
    {
        protected DataArrayBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseDataArrayBuilder<B extends BaseDataArrayBuilder<B>> extends ComponentBuilder<B, DataArray>
    {        
        protected BaseDataArrayBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newDataArray();
        }
        
        protected B copyFrom(DataChoice base)
        {
            super.copyFrom(base);
            throw new UnsupportedOperationException("Not implemented yet");
            //return (B)this;
        }
        
        public B withElementType(String name, DataComponent field)
        {
            instance.setElementType(name, field);
            return (B)this;
        }
        
        public B withFixedSize(int numElts)
        {
            instance.setElementCount(newCount()
                .withValue(numElts).build());
            return (B)this;
        }
        
        public B withElementCount(String sizeComponentID)
        {
            instance.getElementCountProperty().setHref("#" + sizeComponentID);
            return (B)this;
        }
        
        public NestedBooleanBuilder<B> withBooleanElement(String name)
        {
            return new NestedBooleanBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedCategoryBuilder<B> withCategoryElement(String name)
        {
            return new NestedCategoryBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedCountBuilder<B> withCountElement(String name)
        {
            return new NestedCountBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedQuantityBuilder<B> withQuantityElement(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedTimeBuilder<B> withTimeElement(String name)
        {
            return new NestedTimeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedTextBuilder<B> withTextElement(String name)
        {
            return new NestedTextBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedRecordBuilder<B> withRecordElement(String name)
        {
            return new NestedRecordBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedVectorBuilder<B> withVectorElement(String name)
        {
            return new NestedVectorBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedChoiceBuilder<B> withChoiceElement(String name)
        {
            return new NestedChoiceBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
        
        public NestedArrayBuilder<B> withNestedArray(String name)
        {
            return new NestedArrayBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataArrayBuilder.this.withElementType(name, build());
                    return (B)BaseDataArrayBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedArrayBuilder<B> extends BaseDataArrayBuilder<NestedArrayBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedArrayBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
}