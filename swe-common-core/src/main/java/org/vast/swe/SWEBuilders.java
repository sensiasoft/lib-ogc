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
import org.vast.data.UnitReferenceImpl;
import org.vast.util.BaseBuilder;
import org.vast.util.NestedBuilder;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.UnitReference;


/**
 * <p>
 * Utility class to build SWE Common data components with a fluid syntax
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
    public static RecordBuilder newRecord()
    {        
        return newRecord(factory);
    }
    
    
    /**
     * @param fac Factory to use to create the component objects
     * @return A builder to create a new DataRecord component
     */
    public static RecordBuilder newRecord(SWEFactory fac)
    {
        return new RecordBuilder(fac);
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
            setOptional(base.getOptional());
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
            var constraint = ensureConstraint();
            for (int val: values)
                constraint.addValue(val);
            return (B)this;
        }
        
        public B withAllowedIntervals(int[]... intervals)
        {
            var constraint = ensureConstraint();
            for (int[] val: intervals)
                constraint.addInterval(new double[] {val[0], val[1]});
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
            var uom = new UnitReferenceImpl();
            uom.setCode(code);
            instance.setUom(uom);
            return (B)this;
        }        
        
        public B withUomUri(String uri)
        {
            var uom = new UnitReferenceImpl();
            uom.setHref(uri);
            instance.setUom(uom);
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
            var uom = new UnitReferenceImpl();
            uom.setCode(code);
            instance.setUom(uom);
            return (B)this;
        }        
        
        public B withUomUri(String uri)
        {
            var uom = new UnitReferenceImpl();
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
    public static class RecordBuilder extends BaseRecordBuilder<RecordBuilder>    
    {
        protected RecordBuilder(SWEFactory fac)
        {
            super(fac);
        }        
    }    
    
    @SuppressWarnings("unchecked")
    protected abstract static class BaseRecordBuilder<B extends BaseRecordBuilder<B>> extends ComponentBuilder<B, DataRecord>
    {        
        protected BaseRecordBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newDataRecord();
        }
        
        protected B copyFrom(DataRecord base)
        {
            super.copyFrom(base);
            // TODO copy fields
            return (B)this;
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
            return new NestedBooleanBuilder<>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseRecordBuilder.this.instance.addField(name, build());
                    return (B)BaseRecordBuilder.this;
                }
            };
        }
        
        public NestedCategoryBuilder<B> withCategoryField(String name)
        {
            return new NestedCategoryBuilder<>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseRecordBuilder.this.instance.addField(name, build());
                    return (B)BaseRecordBuilder.this;
                }
            };
        }
        
        public NestedCountBuilder<B> withCountField(String name)
        {
            return new NestedCountBuilder<>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseRecordBuilder.this.instance.addField(name, build());
                    return (B)BaseRecordBuilder.this;
                }
            };
        }
        
        public NestedQuantityBuilder<B> withQuantityField(String name)
        {
            return new NestedQuantityBuilder<>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseRecordBuilder.this.instance.addField(name, build());
                    return (B)BaseRecordBuilder.this;
                }
            };
        }
        
        public NestedTimeBuilder<B> withTimeField(String name)
        {
            return new NestedTimeBuilder<>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseRecordBuilder.this.instance.addField(name, build());
                    return (B)BaseRecordBuilder.this;
                }
            };
        }
        
        public NestedTextBuilder<B> withTextField(String name)
        {
            return new NestedTextBuilder<>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseRecordBuilder.this.instance.addField(name, build());
                    return (B)BaseRecordBuilder.this;
                }
            };
        }
        
        public NestedRecordBuilder<B> withNestedRecord(String name)
        {
            return new NestedRecordBuilder<>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseRecordBuilder.this.instance.addField(name, build());
                    return (B)BaseRecordBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedRecordBuilder<B> extends BaseRecordBuilder<NestedRecordBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedRecordBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
}