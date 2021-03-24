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
import java.util.Arrays;
import java.util.function.Consumer;
import org.vast.data.SWEFactory;
import org.vast.util.Asserts;
import org.vast.util.BaseBuilder;
import org.vast.util.NestedBuilder;
import net.opengis.DateTimeDouble;
import net.opengis.IDateTime;
import net.opengis.swe.v20.AbstractSWEIdentifiable;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryOrRange;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountOrRange;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityOrRange;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeOrRange;
import net.opengis.swe.v20.TimeRange;
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

    /*
     * Base builder for all SWEIdentifiable objects
     */
    @SuppressWarnings("unchecked")
    public static abstract class SweIdentifiableBuilder<
            B extends SweIdentifiableBuilder<B, T>,
            T extends AbstractSWEIdentifiable>
        extends BaseBuilder<T>
    {
        Consumer<T> visitor;

        protected SweIdentifiableBuilder()
        {
        }

        /**
         * Copy all info from another component
         * @param base Component to copy from
         * @return This builder for chaining
         */
        public B copyFrom(DataComponent base)
        {
            instance.setId(base.getId());
            instance.setLabel(base.getLabel());
            instance.setDescription(base.getDescription());
            return (B)this;
        }

        /**
         * Sets the component ID
         * @param id
         * @return This builder for chaining
         */
        public B id(String id)
        {
            instance.setId(id);
            return (B)this;
        }

        /**
         * Sets the component human-readable label
         * @param label
         * @return This builder for chaining
         */
        public B label(String label)
        {
            instance.setLabel(label);
            return (B)this;
        }

        /**
         * Sets the component human-readable description
         * @param description
         * @return This builder for chaining
         */
        public B description(String description)
        {
            instance.setDescription(description);
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
     * Base builder for all components
     */
    @SuppressWarnings("unchecked")
    public static abstract class ComponentBuilder<
            B extends ComponentBuilder<B, T>,
            T extends DataComponent>
        extends SweIdentifiableBuilder<B, T>
    {
        SWEFactory fac;

        protected ComponentBuilder(SWEFactory fac)
        {
            this.fac = fac;
        }

        public B from(T base)
        {
            this.instance = base;
            return (B)this;
        }

        /**
         * Copy all info from another component
         * @param base Component to copy from
         * @return This builder for chaining
         */
        @Override
        public B copyFrom(DataComponent base)
        {
            super.copyFrom(base);
            instance.setDefinition(base.getDefinition());
            if (base.isSetOptional())
                instance.setOptional(base.getOptional());
            if (base.isSetUpdatable())
                instance.setUpdatable(base.getUpdatable());
            return (B)this;
        }

        /**
         * Sets the component name (usually only at the top level)
         * @param name
         * @return This builder for chaining
         */
        public B name(String name)
        {
            instance.setName(name);
            return (B)this;
        }

        /**
         * Sets the component definition URI
         * @param defUri URI of definition (usually resolvable to the actual
         * definition of observable or concept)
         * @return This builder for chaining
         */
        public B definition(String defUri)
        {
            instance.setDefinition(defUri);
            return (B)this;
        }

        /**
         * Sets the component optional flag
         * @param optional
         * @return  This builder for chaining
         */
        public B optional(boolean optional)
        {
            instance.setOptional(optional);
            return (B)this;
        }

        /**
         * Sets the component updatable flag
         * @param updatable
         * @return  This builder for chaining
         */
        public B updatable(boolean updatable)
        {
            instance.setUpdatable(updatable);
            return (B)this;
        }
    }


    /*
     * Base builder for all simple components
     */
    @SuppressWarnings("unchecked")
    public static abstract class SimpleComponentBuilder<
            B extends SimpleComponentBuilder<B, T>,
            T extends SimpleComponent>
        extends ComponentBuilder<B, T>
    {
        protected SimpleComponentBuilder(SWEFactory fac)
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

        public B dataType(DataType dataType)
        {
            instance.setDataType(dataType);
            return (B)this;
        }

        public B refFrame(String refFrameUri)
        {
            instance.setReferenceFrame(refFrameUri);
            return (B)this;
        }

        public B refFrame(String refFrameUri, String axisId)
        {
            instance.setReferenceFrame(refFrameUri);
            instance.setAxisID(axisId);
            return (B)this;
        }

        public B axisId(String axisId)
        {
            instance.setAxisID(axisId);
            return (B)this;
        }

        public B addQuality(Quantity q)
        {
            instance.addQuality(q);
            return (B)this;
        }

        public B addQuality(QuantityBuilder builder)
        {
            return addQuality(builder.build());
        }

        public B addQuality(QuantityRange q)
        {
            instance.addQuality(q);
            return (B)this;
        }

        public B addQuality(QuantityRangeBuilder builder)
        {
            return addQuality(builder.build());
        }

        public B addQuality(Category q)
        {
            instance.addQuality(q);
            return (B)this;
        }

        public B addQuality(CategoryBuilder builder)
        {
            return addQuality(builder.build());
        }

        public B addQuality(Text q)
        {
            instance.addQuality(q);
            return (B)this;
        }

        public B addQuality(TextBuilder builder)
        {
            return addQuality(builder.build());
        }

        /*public NestedQuantityBuilder<B> addQualityAsQuantity()
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    SimpleComponentBuilder.this.addQuality(build());
                    return (B)SimpleComponentBuilder.this;
                }
            };
        }

        public NestedQuantityRangeBuilder<B> addQualityAsQuantityRange()
        {
            return new NestedQuantityRangeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    SimpleComponentBuilder.this.addQuality(build());
                    return (B)SimpleComponentBuilder.this;
                }
            };
        }

        public NestedCategoryBuilder<B> addQualityAsCategory()
        {
            return new NestedCategoryBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    SimpleComponentBuilder.this.addQuality(build());
                    return (B)SimpleComponentBuilder.this;
                }
            };
        }

        public NestedTextBuilder<B> addQualityAsText()
        {
            return new NestedTextBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    SimpleComponentBuilder.this.addQuality(build());
                    return (B)SimpleComponentBuilder.this;
                }
            };
        }*/
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
    public abstract static class BaseBooleanBuilder<B extends BaseBooleanBuilder<B>> extends SimpleComponentBuilder<B, Boolean>
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

        public B value(boolean value)
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


    @SuppressWarnings("unchecked")
    public abstract static class CategoryOrRangeBuilder<B extends CategoryOrRangeBuilder<B, T>, T extends CategoryOrRange> extends SimpleComponentBuilder<B, T>
    {
        protected CategoryOrRangeBuilder(SWEFactory fac, T instance)
        {
            super(fac);
            this.instance = instance;
        }

        public B copyFrom(T base)
        {
            super.copyFrom(base);
            instance.setCodeSpace(base.getCodeSpace());
            instance.setConstraint(base.getConstraint());
            return (B)this;
        }

        public B codeSpace(String uri)
        {
            instance.setCodeSpace(uri);
            return (B)this;
        }

        protected AllowedTokens ensureConstraint()
        {
            AllowedTokens constraint = instance.getConstraint();
            if (constraint == null)
            {
                constraint = fac.newAllowedTokens();
                instance.setConstraint(constraint);
            }

            return constraint;
        }

        public B addAllowedValues(String... values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (String val: values)
                constraint.addValue(val);
            return (B)this;
        }

        public B addAllowedValues(int... values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (int val: values)
                constraint.addValue(Integer.toString(val));
            return (B)this;
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

    @SuppressWarnings("unchecked")
    public abstract static class BaseCategoryBuilder<B extends BaseCategoryBuilder<B>> extends CategoryOrRangeBuilder<B, Category>
    {
        protected BaseCategoryBuilder(SWEFactory fac)
        {
            super(fac, fac.newCategory());
        }

        @Override
		public B copyFrom(Category base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(String value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }


    /**
     * <p>
     * Builder class for CategoryRange components
     * </p>
     *
     * @author Alex Robin
     * @date May 15, 2020
     */
    public static class CategoryRangeBuilder extends BaseCategoryRangeBuilder<CategoryRangeBuilder>
    {
        protected CategoryRangeBuilder(SWEFactory fac)
        {
            super(fac);
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedCategoryRangeBuilder<B> extends BaseCategoryRangeBuilder<NestedCategoryRangeBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedCategoryRangeBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseCategoryRangeBuilder<B extends BaseCategoryRangeBuilder<B>> extends CategoryOrRangeBuilder<B, CategoryRange>
    {
        protected BaseCategoryRangeBuilder(SWEFactory fac)
        {
            super(fac, fac.newCategoryRange());
        }

        @Override
		public B copyFrom(CategoryRange base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(String min, String max)
        {
            instance.setValue(new String[] {min, max});
            return (B)this;
        }
    }


    /*
     * Base builder for Count and CountRange components
     */
    @SuppressWarnings("unchecked")
    public abstract static class CountOrRangeBuilder<B extends CountOrRangeBuilder<B, T>, T extends CountOrRange> extends SimpleComponentBuilder<B, T>
    {
        protected CountOrRangeBuilder(SWEFactory fac, T instance)
        {
            super(fac);
            this.instance = instance;
        }

        public B copyFrom(T base)
        {
            super.copyFrom(base);
            instance.setConstraint(base.getConstraint());
            return (B)this;
        }

        protected AllowedValues ensureConstraint()
        {
            AllowedValues constraint = instance.getConstraint();
            if (constraint == null)
            {
                constraint = fac.newAllowedValues();
                instance.setConstraint(constraint);
            }

            return constraint;
        }

        public B addAllowedValues(int... values)
        {
            AllowedValues constraint = ensureConstraint();
            for (int val: values)
                constraint.addValue(val);
            return (B)this;
        }

        public B addAllowedIntervals(int[]... intervals)
        {
            AllowedValues constraint = ensureConstraint();
            for (int[] val: intervals)
                constraint.addInterval(new double[] {val[0], val[1]});
            return (B)this;
        }

        public B addAllowedInterval(int min, int max)
        {
            AllowedValues constraint = ensureConstraint();
            constraint.addInterval(new double[] {min, max});
            return (B)this;
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

    @SuppressWarnings("unchecked")
    public abstract static class BaseCountBuilder<B extends BaseCountBuilder<B>> extends CountOrRangeBuilder<B, Count>
    {
        protected BaseCountBuilder(SWEFactory fac)
        {
            super(fac, fac.newCount());
        }

        @Override
		public B copyFrom(Count base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(int value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }


    /**
     * <p>
     * Builder class for CountRange components
     * </p>
     *
     * @author Alex Robin
     * @date May 15, 2020
     */
    public static class CountRangeBuilder extends BaseCountRangeBuilder<CountRangeBuilder>
    {
        protected CountRangeBuilder(SWEFactory fac)
        {
            super(fac);
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedCountRangeBuilder<B> extends BaseCountRangeBuilder<NestedCountRangeBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedCountRangeBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseCountRangeBuilder<B extends BaseCountRangeBuilder<B>> extends CountOrRangeBuilder<B, CountRange>
    {
        protected BaseCountRangeBuilder(SWEFactory fac)
        {
            super(fac, fac.newCountRange());
        }

        @Override
		public B copyFrom(CountRange base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(int min, int max)
        {
            instance.setValue(new int[] {min, max});
            return (B)this;
        }
    }


    /*
     * Base builder for Quantity and QuantityRange components
     */
    @SuppressWarnings("unchecked")
    public abstract static class QuantityOrRangeBuilder<B extends QuantityOrRangeBuilder<B, T>, T extends QuantityOrRange> extends SimpleComponentBuilder<B, T>
    {
        protected QuantityOrRangeBuilder(SWEFactory fac, T instance)
        {
            super(fac);
            Asserts.checkArgument(instance instanceof Quantity || instance instanceof QuantityRange,
            		"Instance must be one of " + Arrays.asList(Quantity.class.getSimpleName(), Arrays.asList(QuantityRange.class.getSimpleName())));

            this.instance = instance;
            uomUri(SWEConstants.UOM_ANY);
        }

        public B copyFrom(T base)
        {
            super.copyFrom(base);
            instance.setUom(base.getUom());
            return (B)this;
        }

        /**
         * Sets the unit of measure by code
         * @param code UCUM code
         * @return This builder for chaining
         */
        public B uomCode(String code)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setCode(code);
            instance.setUom(uom);
            return (B)this;
        }

        public B uomUri(String uri)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setHref(uri);
            instance.setUom(uom);
            return (B)this;
        }

        protected AllowedValues ensureConstraint()
        {
            AllowedValues constraint = instance.getConstraint();
            if (constraint == null)
            {
                constraint = fac.newAllowedValues();
                instance.setConstraint(constraint);
            }

            return constraint;
        }

        public B addAllowedValues(double... values)
        {
            AllowedValues constraint = ensureConstraint();
            for (double val: values)
                constraint.addValue(val);
            return (B)this;
        }

        public B addAllowedIntervals(double[]... intervals)
        {
            AllowedValues constraint = ensureConstraint();
            for (double[] val: intervals)
                constraint.addInterval(val);
            return (B)this;
        }

        public B addAllowedInterval(double min, double max)
        {
            AllowedValues constraint = ensureConstraint();
            constraint.addInterval(new double[] {min, max});
            return (B)this;
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

    @SuppressWarnings("unchecked")
    public abstract static class BaseQuantityBuilder<B extends BaseQuantityBuilder<B>> extends QuantityOrRangeBuilder<B, Quantity>
    {
        protected BaseQuantityBuilder(SWEFactory fac)
        {
            super(fac, fac.newQuantity());
        }

        @Override
		public B copyFrom(Quantity base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(double value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }


    /**
     * <p>
     * Builder class for QuantityRange components
     * </p>
     *
     * @author Alex Robin
     * @date May 15, 2020
     */
    public static class QuantityRangeBuilder extends BaseQuantityRangeBuilder<QuantityRangeBuilder>
    {
        protected QuantityRangeBuilder(SWEFactory fac)
        {
            super(fac);
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedQuantityRangeBuilder<B> extends BaseQuantityRangeBuilder<NestedQuantityRangeBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedQuantityRangeBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseQuantityRangeBuilder<B extends BaseQuantityRangeBuilder<B>> extends QuantityOrRangeBuilder<B, QuantityRange>
    {
        protected BaseQuantityRangeBuilder(SWEFactory fac)
        {
            super(fac, fac.newQuantityRange());
        }

        @Override
		public B copyFrom(QuantityRange base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(double min, double max)
        {
            instance.setValue(new double[] {min, max});
            return (B)this;
        }
    }


    /*
     * Base builder for Time or TimeRange
     */
    @SuppressWarnings("unchecked")
    public abstract static class TimeOrRangeBuilder<B extends TimeOrRangeBuilder<B, T>, T extends TimeOrRange> extends SimpleComponentBuilder<B, T>
    {
        protected TimeOrRangeBuilder(SWEFactory fac, T instance)
        {
            super(fac);
            this.instance = instance;
        }

        public B copyFrom(T base)
        {
            super.copyFrom(base);
            instance.setUom(base.getUom());
            instance.setConstraint(base.getConstraint());
            return (B)this;
        }

        public B uomCode(String code)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setCode(code);
            instance.setUom(uom);
            return (B)this;
        }

        public B uomUri(String uri)
        {
            UnitReference uom = fac.newUnitReference();
            uom.setHref(uri);
            instance.setUom(uom);
            return (B)this;
        }

        public B timeFrame(String defUri)
        {
            instance.setReferenceFrame(defUri);
            return (B)this;
        }

        @Override
		public B refFrame(String refFrameUri)
        {
        	instance.setReferenceFrame(refFrameUri);
            return (B)this;
        }

        public B refTime(Instant refTime)
        {
            double t = refTime.getEpochSecond() / 1000.;
            instance.setReferenceTime(new DateTimeDouble(t));
            return (B)this;
        }

        /* More helper methods */

        public B withIso8601Format()
        {
            uomUri(Time.ISO_TIME_UNIT);
            return (B)this;
        }

        public B withUtcTimeFrame()
        {
            timeFrame(SWEConstants.TIME_REF_UTC);
            return (B)this;
        }

        public B withGpsTimeFrame()
        {
            timeFrame(SWEConstants.TIME_REF_GPS);
            return (B)this;
        }

        public B withTaiTimeFrame()
        {
            timeFrame(SWEConstants.TIME_REF_TAI);
            return (B)this;
        }

        /**
         * Helper to create a sampling time stamp with UTC time frame and ISO format
         * @return this builder for chaining
         */
        public B asSamplingTimeIsoUTC()
        {
            definition(SWEConstants.DEF_SAMPLING_TIME);
            label("Sampling Time");
            withIso8601Format();
            withUtcTimeFrame();
            return (B)this;
        }

        public B asSamplingTimeIsoGPS()
        {
            definition(SWEConstants.DEF_SAMPLING_TIME);
            label("Sampling Time");
            withIso8601Format();
            withGpsTimeFrame();
            return (B)this;
        }

        public B asPhenomenonTimeIsoUTC()
        {
            definition(SWEConstants.DEF_PHENOMENON_TIME);
            label("Phenomenon Time");
            withIso8601Format();
            withUtcTimeFrame();
            return (B)this;
        }

        public B asForecastTimeIsoUTC()
        {
            definition(SWEConstants.DEF_FORECAST_TIME);
            label("Forecast Time");
            withIso8601Format();
            withUtcTimeFrame();
            return (B)this;
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
    public abstract static class BaseTimeBuilder<B extends BaseTimeBuilder<B>> extends TimeOrRangeBuilder<B, Time>
    {
        protected BaseTimeBuilder(SWEFactory fac)
        {
            super(fac, fac.newTime());
        }

        @Override
		public B copyFrom(Time base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(double value)
        {
            instance.setValue(new DateTimeDouble(value));
            return (B)this;
        }

        public B value(Instant value)
        {
            instance.setValue(new DateTimeDouble(value.getEpochSecond()/1000.));
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
     * Builder class for TimeRange components
     * </p>
     *
     * @author Alex Robin
     * @date May 15, 2020
     */
    public static class TimeRangeBuilder extends BaseTimeRangeBuilder<TimeRangeBuilder>
    {
        protected TimeRangeBuilder(SWEFactory fac)
        {
            super(fac);
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseTimeRangeBuilder<B extends BaseTimeRangeBuilder<B>> extends TimeOrRangeBuilder<B, TimeRange>
    {
        protected BaseTimeRangeBuilder(SWEFactory fac)
        {
            super(fac, fac.newTimeRange());
        }

        @Override
		public B copyFrom(TimeRange base)
        {
            super.copyFrom(base);
            instance.setValue(base.getValue());
            return (B)this;
        }

        public B value(double min, double max)
        {
        	instance.setValue(new IDateTime[] {
            	new DateTimeDouble(min),
            	new DateTimeDouble(max)
            });
            return (B)this;
        }

        public B value(Instant min, Instant max)
        {
            instance.setValue(new IDateTime[] {
            	new DateTimeDouble(min.getEpochSecond()/1000.),
            	new DateTimeDouble(max.getEpochSecond()/1000.)
            });
            return (B)this;
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedTimeRangeBuilder<B> extends BaseTimeBuilder<NestedTimeRangeBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedTimeRangeBuilder(B parent, SWEFactory fac)
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
    public abstract static class BaseTextBuilder<B extends BaseTextBuilder<B>> extends SimpleComponentBuilder<B, Text>
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

        protected AllowedTokens ensureConstraint()
        {
            AllowedTokens constraint = instance.getConstraint();
            if (constraint == null)
            {
                constraint = fac.newAllowedTokens();
                instance.setConstraint(constraint);
            }

            return constraint;
        }

        public B addAllowedValues(String... values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (String val: values)
                constraint.addValue(val);
            return (B)this;
        }

        public B pattern(String pattern)
        {
        	AllowedTokens constraint = ensureConstraint();
        	constraint.setPattern(pattern);
        	return (B)this;
        }

        public B value(String value)
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
    public abstract static class BaseDataRecordBuilder<B extends BaseDataRecordBuilder<B>> extends ComponentBuilder<B, DataRecord>
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

        public B addField(String name, DataComponent field)
        {
            instance.addField(name, field);
            return (B)this;
        }

        public B addField(String name, ComponentBuilder<?,?> fieldBuilder)
        {
            return addField(name, fieldBuilder.build());
        }

        public B addSamplingTimeIsoUTC(String name)
        {
            instance.addField(name, new TimeBuilder(fac)
                .asSamplingTimeIsoUTC()
                .build());
            return (B)this;
        }

        /*public NestedBooleanBuilder<B> addBooleanField(String name)
        {
            return new NestedBooleanBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedCategoryBuilder<B> addCategoryField(String name)
        {
            return new NestedCategoryBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedCategoryRangeBuilder<B> addCategoryRangeField(String name)
        {
            return new NestedCategoryRangeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedCountBuilder<B> addCountField(String name)
        {
            return new NestedCountBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedCountRangeBuilder<B> addCountRangeField(String name)
        {
            return new NestedCountRangeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedQuantityBuilder<B> addQuantityField(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedQuantityRangeBuilder<B> addQuantityRangeField(String name)
        {
            return new NestedQuantityRangeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedTimeBuilder<B> addTimeField(String name)
        {
            return new NestedTimeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedTimeRangeBuilder<B> addTimeRangeField(String name)
        {
            return new NestedTimeRangeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedTextBuilder<B> addTextField(String name)
        {
            return new NestedTextBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedVectorBuilder<B> addVectorField(String name)
        {
            return new NestedVectorBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedArrayBuilder<B> addArrayField(String name)
        {
            return new NestedArrayBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedMatrixBuilder<B> addMatrixField(String name)
        {
            return new NestedMatrixBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedChoiceBuilder<B> addChoiceField(String name)
        {
            return new NestedChoiceBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }

        public NestedRecordBuilder<B> addRecordField(String name)
        {
            return new NestedRecordBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataRecordBuilder.this.addField(name, build());
                    return (B)BaseDataRecordBuilder.this;
                }
            };
        }*/
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
    public abstract static class BaseVectorBuilder<B extends BaseVectorBuilder<B>> extends ComponentBuilder<B, Vector>
    {
        DataType dataType = null;

        protected BaseVectorBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newVector();
        }

        public B copyFrom(Vector base)
        {
            super.copyFrom(base);
            throw new UnsupportedOperationException("Not implemented yet");
            //return (B)this;
        }

        public B refFrame(String refFrameUri)
        {
            instance.setReferenceFrame(refFrameUri);
            return (B)this;
        }

        public B localFrame(String localFrameUri)
        {
            instance.setLocalFrame(localFrameUri);
            return (B)this;
        }

        public B addCoordinate(String name, Quantity coord)
        {
            instance.addCoordinateAsQuantity(name, coord);
            return (B)this;
        }

        public B addCoordinate(String name, QuantityBuilder coord)
        {
            instance.addCoordinateAsQuantity(name, coord.build());
            return (B)this;
        }

        public B addCoordinate(String name, Count coord)
        {
            instance.addCoordinateAsCount(name, coord);
            return (B)this;
        }

        public B addCoordinate(String name, CountBuilder coord)
        {
            instance.addCoordinateAsCount(name, coord.build());
            return (B)this;
        }

        public B addCoordinate(String name, Time coord)
        {
            instance.addCoordinateAsTime(name, coord);
            return (B)this;
        }

        public B addCoordinate(String name, TimeBuilder coord)
        {
            instance.addCoordinateAsTime(name, coord.build());
            return (B)this;
        }

        public B dataType(DataType dataType)
        {
            this.dataType = dataType;
            return (B)this;
        }

        /*public NestedQuantityBuilder<B> addQuantityCoord(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseVectorBuilder.this.addCoordinate(name, build());
                    return (B)BaseVectorBuilder.this;
                }
            };
        }

        public NestedCountBuilder<B> addCountCoord(String name)
        {
            return new NestedCountBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseVectorBuilder.this.addCoordinate(name, build());
                    return (B)BaseVectorBuilder.this;
                }
            };
        }

        public NestedTimeBuilder<B> addTimeCoord(String name)
        {
            return new NestedTimeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseVectorBuilder.this.addCoordinate(name, build());
                    return (B)BaseVectorBuilder.this;
                }
            };
        }*/

        @Override
        public Vector build()
        {
            Vector v = super.build();
            if (dataType != null)
                v.setDataType(dataType);
            return v;
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
    public abstract static class BaseDataChoiceBuilder<B extends BaseDataChoiceBuilder<B>> extends ComponentBuilder<B, DataChoice>
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

        public B addItem(String name, DataComponent field)
        {
            instance.addItem(name, field);
            return (B)this;
        }

        public B addItem(String name, ComponentBuilder<?,?> itemBuilder)
        {
            return addItem(name, itemBuilder.build());
        }

        /*public NestedBooleanBuilder<B> addBooleanItem(String name)
        {
            return new NestedBooleanBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedCategoryBuilder<B> addCategoryItem(String name)
        {
            return new NestedCategoryBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedCountBuilder<B> addCountItem(String name)
        {
            return new NestedCountBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedQuantityBuilder<B> addQuantityItem(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedTimeBuilder<B> addTimeItem(String name)
        {
            return new NestedTimeBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedTextBuilder<B> addTextItem(String name)
        {
            return new NestedTextBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedRecordBuilder<B> addRecordItem(String name)
        {
            return new NestedRecordBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedVectorBuilder<B> addVectorItem(String name)
        {
            return new NestedVectorBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedMatrixBuilder<B> addMatrixItem(String name)
        {
            return new NestedMatrixBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedArrayBuilder<B> addArrayItem(String name)
        {
            return new NestedArrayBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }

        public NestedChoiceBuilder<B> addChoiceItem(String name)
        {
            return new NestedChoiceBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseDataChoiceBuilder.this.addItem(name, build());
                    return (B)BaseDataChoiceBuilder.this;
                }
            };
        }*/
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
    public abstract static class BaseDataArrayBuilder<B extends BaseDataArrayBuilder<B>> extends ComponentBuilder<B, DataArray>
    {
        protected BaseDataArrayBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newDataArray();
        }

        protected B copyFrom(DataArray base)
        {
            super.copyFrom(base);
            throw new UnsupportedOperationException("Not implemented yet");
            //return (B)this;
        }

        public B withFixedSize(int numElts)
        {
            instance.setElementCount(new CountBuilder(fac)
                .value(numElts).build());
            return (B)this;
        }

        public B withVariableSize(String sizeComponentID)
        {
            instance.getElementCountProperty().setHref("#" + sizeComponentID);
            return (B)this;
        }

        public B withSizeComponent(Count sizeComponent)
        {
            instance.setElementCount(sizeComponent);
            return (B)this;
        }

        public B withElement(String name, DataComponent field)
        {
            instance.setElementType(name, field);
            return (B)this;
        }

        public B withElement(String name, ComponentBuilder<?,?> eltBuilder)
        {
            return withElement(name, eltBuilder.build());
        }

        /*public NestedBooleanBuilder<B> withBooleanElement(String name)
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

        public NestedArrayBuilder<B> withArrayElement(String name)
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
        }*/
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


    /**
     * <p>
     * Builder class for Matrix components
     * </p>
     *
     * @author Alex Robin
     * @date Apr 24, 2020
     */
    public static class MatrixBuilder extends BaseMatrixBuilder<MatrixBuilder>
    {
        protected MatrixBuilder(SWEFactory fac)
        {
            super(fac);
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseMatrixBuilder<B extends BaseMatrixBuilder<B>> extends ComponentBuilder<B, Matrix>
    {
        int dimSizes[];
        String[] eltNames;
        ScalarComponent eltType;
        String scalarEltName;
        DataType dataType = null;

        protected BaseMatrixBuilder(SWEFactory fac)
        {
            super(fac);
            this.instance = fac.newMatrix();
        }

        protected B copyFrom(Matrix base)
        {
            super.copyFrom(base);
            throw new UnsupportedOperationException("Not implemented yet");
            //return (B)this;
        }

        public B refFrame(String refFrameUri)
        {
            instance.setReferenceFrame(refFrameUri);
            return (B)this;
        }

        public B localFrame(String localFrameUri)
        {
            instance.setLocalFrame(localFrameUri);
            return (B)this;
        }

        public B size(int numRows, int numCols, boolean rowMajor)
        {
            if (rowMajor)
            this.dimSizes = rowMajor ? new int[] {numRows, numCols} : new int[] {numCols, numRows};
            this.eltNames = rowMajor ? new String[] {"row", "elt"} : new String[] {"col", "elt"};
            return (B)this;
        }

        public B withElement(String name, ScalarComponent component)
        {
            this.scalarEltName = Asserts.checkNotNull(name, "name");
            this.eltType = Asserts.checkNotNull(component, ScalarComponent.class);
            return (B)this;
        }

        public B withElement(String name, QuantityBuilder coord)
        {
            return withElement(name, coord.build());
        }

        public B withElement(String name, CountBuilder coord)
        {
            return withElement(name, coord.build());
        }

        public B withElement(String name, TimeBuilder coord)
        {
            return withElement(name, coord.build());
        }

        public B withElement(String name, BooleanBuilder coord)
        {
            return withElement(name, coord.build());
        }

        public B withElement(String name, TextBuilder coord)
        {
            return withElement(name, coord.build());
        }

        public B dataType(DataType dataType)
        {
            this.dataType = dataType;
            return (B)this;
        }

        /*public NestedQuantityBuilder<B> withQuantityElement(String name)
        {
            return new NestedQuantityBuilder<B>((B)this, fac)
            {
                @Override
                public B done()
                {
                    BaseMatrixBuilder.this.withElementType(name, build());
                    return (B)BaseMatrixBuilder.this;
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
                    BaseMatrixBuilder.this.withElementType(name, build());
                    return (B)BaseMatrixBuilder.this;
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
                    BaseMatrixBuilder.this.withElementType(name, build());
                    return (B)BaseMatrixBuilder.this;
                }
            };
        }*/

        @Override
        public Matrix build()
        {
            Matrix parent = instance;

            for (int i = 0; i < dimSizes.length; i++)
            {
                parent.setElementCount(new CountBuilder(fac)
                    .value(dimSizes[i]).build());

                if (i < dimSizes.length-1)
                {
                    Matrix nestedMatrix = fac.newMatrix();
                    parent.setElementType(eltNames[i], nestedMatrix);
                    parent = nestedMatrix;
                }
                else
                {
                    parent.setElementType(scalarEltName, eltType);
                    if (dataType != null)
                        eltType.setDataType(dataType);
                }
            }

            return super.build();
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedMatrixBuilder<B> extends BaseMatrixBuilder<NestedMatrixBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedMatrixBuilder(B parent, SWEFactory fac)
        {
            super(fac);
            this.parent = parent;
        }
    }
}