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

import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.vast.data.DateTimeOrDouble;
import org.vast.data.SWEFactory;
import org.vast.util.Asserts;
import org.vast.util.BaseBuilder;
import org.vast.util.NestedBuilder;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
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
import net.opengis.swe.v20.NilValues;
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
        public B copyFrom(AbstractSWEIdentifiable base)
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
    public static abstract class DataComponentBuilder<
            B extends DataComponentBuilder<B, T>,
            T extends DataComponent>
        extends SweIdentifiableBuilder<B, T>
    {
        SWEFactory fac;

        protected DataComponentBuilder(SWEFactory fac)
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
        public B copyFrom(DataComponent base)
        {
            super.copyFrom((AbstractSWEIdentifiable)base);
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
         * @param uri URI of definition (usually resolvable to the actual
         * definition of observable or concept)
         * @return This builder for chaining
         */
        public B definition(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI                
            instance.setDefinition(uri);
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
        extends DataComponentBuilder<B, T>
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

        public B refFrame(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI            
            instance.setReferenceFrame(uri);
            return (B)this;
        }

        public B refFrame(String uri, String axisId)
        {
            refFrame(uri);
            instance.setAxisID(axisId);
            return (B)this;
        }

        public B axisId(String axisId)
        {
            instance.setAxisID(axisId);
            return (B)this;
        }
        
        public B addQuality(SimpleComponent q)
        {
            return addQuality(null, q);
        }

        public B addQuality(String role, SimpleComponent q)
        {
            Asserts.checkArgument(
                q instanceof Quantity ||
                q instanceof QuantityRange ||
                q instanceof Category ||
                q instanceof Text,
                "Quality must be one of " + Arrays.asList("Quantity", "QuantityRange", "Category", "Text"));
            
            OgcProperty<SimpleComponent> prop = new OgcPropertyImpl<>(q);
            prop.setRole(role);
            instance.getQualityList().add(prop);
            
            return (B)this;
        }
        
        public B addQuality(SimpleComponentBuilder<?,?> builder)
        {
            return addQuality(builder.build());
        }

        public B addQuality(String role, SimpleComponentBuilder<?,?> builder)
        {
            return addQuality(role, builder.build());
        }

        protected NilValues ensureNilValues()
        {
            if (!instance.isSetNilValues())
            {
                var nilValues = fac.newNilValues();
                instance.setNilValues(nilValues);
                return nilValues;
            }

            return instance.getNilValues();
        }
        
        protected B addNilValue(String value, String reasonUri)
        {
            URI.create(reasonUri); // validate URI
            
            var nilValues = ensureNilValues();
            var nilValue = fac.newNilValue();
            nilValue.setValue(value);
            nilValue.setReason(reasonUri);
            nilValues.addNilValue(nilValue);
                        
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
            if (uri != null)
                URI.create(uri); // validate URI
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
            return addAllowedValues(Arrays.asList(values));
        }

        public B addAllowedValues(Iterable<String> values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (String val: values)
                constraint.addValue(val);
            return (B)this;
        }

        public <E extends Enum<E>> B addAllowedValues(Class<E> values)
        {
            return addAllowedValues(EnumSet.allOf(values).stream()
                .map(Enum::name)
                .collect(Collectors.toList()));
        }

        public B addAllowedValues(int... values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (int val: values)
                constraint.addValue(Integer.toString(val));
            return (B)this;
        }
        
        public B addNilValue(String value, String reasonUri)
        {
            return (B)super.addNilValue(value, reasonUri);
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
            for (long val: values)
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
        
        public B addNilValue(int value, String reasonUri)
        {
            return (B)super.addNilValue(Integer.toString(value), reasonUri);
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
            uomUri(SWEConstants.UOM_UNITLESS); // default to unitless
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
            uom.getValue(); // validate UCUM code is correct
            instance.setUom(uom);
            return (B)this;
        }

        /**
         * Sets the unit of measure by URI
         * @param uri Unit URI
         * @return This builder for chaining
         */
        public B uomUri(String uri)
        {
            URI.create(uri); // validate URI
            UnitReference uom = fac.newUnitReference();
            uom.setHref(uri);
            instance.setUom(uom);
            return (B)this;
        }
        
        /**
         * Helper to set the unit of measure by code or URI
         * @param codeOrUri UCUM code or URI for unit
         * @return This builder for chaining
         */
        public B uom(String codeOrUri)
        {
            if (codeOrUri.startsWith("http") || codeOrUri.startsWith("urn"))
                return uomUri(codeOrUri);
            else
                return uomCode(codeOrUri);
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
        
        public B significantFigures(int digits)
        {
            AllowedValues constraint = ensureConstraint();
            constraint.setSignificantFigures(digits);
            return (B)this;
        }
        
        public B addNilValue(double value, String reasonUri)
        {
            return (B)super.addNilValue(Double.toString(value), reasonUri);
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
            uom.getValue(); // validate UCUM code is correct
            instance.setUom(uom);
            return (B)this;
        }

        public B uomUri(String uri)
        {
            URI.create(uri); // validate URI
            UnitReference uom = fac.newUnitReference();
            uom.setHref(uri);
            instance.setUom(uom);
            return (B)this;
        }

        public B timeFrame(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI
            instance.setReferenceFrame(uri);
            return (B)this;
        }

        @Override
		public B refFrame(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI
            instance.setReferenceFrame(uri);
            return (B)this;
        }

        public B refTime(Instant refTime)
        {
            instance.setReferenceTime(refTime.atOffset(ZoneOffset.UTC));
            return (B)this;
        }
        
        public B addNilValue(double value, String reasonUri)
        {
            return (B)super.addNilValue(Double.toString(value), reasonUri);
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
            instance.setValue(new DateTimeOrDouble(value));
            return (B)this;
        }

        public B value(Instant value)
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
        	instance.setValue(new DateTimeOrDouble[] {
            	new DateTimeOrDouble(min),
            	new DateTimeOrDouble(max)
            });
            return (B)this;
        }

        public B value(Instant min, Instant max)
        {
            instance.setValue(new DateTimeOrDouble[] {
            	new DateTimeOrDouble(min.atOffset(ZoneOffset.UTC)),
            	new DateTimeOrDouble(max.atOffset(ZoneOffset.UTC))
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
            return addAllowedValues(Arrays.asList(values));
        }

        public B addAllowedValues(Iterable<String> values)
        {
            AllowedTokens constraint = ensureConstraint();
            for (String val: values)
                constraint.addValue(val);
            return (B)this;
        }

        public <E extends Enum<E>> B addAllowedValues(Class<E> values)
        {
            return addAllowedValues(EnumSet.allOf(values).stream()
                .map(Enum::name)
                .collect(Collectors.toList()));
        }

        public B pattern(String pattern)
        {
        	AllowedTokens constraint = ensureConstraint();
        	constraint.setPattern(pattern);
        	return (B)this;
        }
        
        public B addNilValue(String value, String reasonUri)
        {
            return (B)super.addNilValue(value, reasonUri);
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
    public abstract static class BaseDataRecordBuilder<B extends BaseDataRecordBuilder<B>> extends DataComponentBuilder<B, DataRecord>
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

        public B addField(String name, DataComponentBuilder<?,?> fieldBuilder)
        {
            return addField(name, fieldBuilder.build());
        }

        public B addField(String name, String role, DataComponent field)
        {
            OgcProperty<DataComponent> prop = new OgcPropertyImpl<DataComponent>(name, field);
            prop.setRole(role);
            instance.getFieldList().add(prop);
            return (B)this;
        }

        public B addField(String name, String role, DataComponentBuilder<?,?> fieldBuilder)
        {
            return addField(name, role, fieldBuilder.build());
        }

        public B addSamplingTimeIsoUTC(String name)
        {
            instance.addField(name, new TimeBuilder(fac)
                .asSamplingTimeIsoUTC()
                .build());
            return (B)this;
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
    public abstract static class BaseVectorBuilder<B extends BaseVectorBuilder<B>> extends DataComponentBuilder<B, Vector>
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

        public B refFrame(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI
            instance.setReferenceFrame(uri);
            return (B)this;
        }

        public B localFrame(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI
            instance.setLocalFrame(uri);
            return (B)this;
        }

        public B addCoordinate(String name, Quantity coord)
        {
            instance.addCoordinateAsQuantity(name, coord);
            return (B)this;
        }

        public B addCoordinate(String name, Count coord)
        {
            instance.addCoordinateAsCount(name, coord);
            return (B)this;
        }

        public B addCoordinate(String name, Time coord)
        {
            instance.addCoordinateAsTime(name, coord);
            return (B)this;
        }

        public <NB extends SimpleComponentBuilder<NB, T>, T extends ScalarComponent>
            B addCoordinate(String name, SimpleComponentBuilder<NB, T> coordBuilder)
        {
            var coord = coordBuilder.build();
            
            if (coord instanceof Quantity)
                addCoordinate(name, (Quantity)coord);
            else if (coord instanceof Count)
                addCoordinate(name, (Count)coord);
            else if (coord instanceof Time)
                addCoordinate(name, (Time)coord);
            
            return (B)this;
        }

        public B dataType(DataType dataType)
        {
            this.dataType = dataType;
            return (B)this;
        }
        
        public B values(double[] values)
        {
            var vectorSize = instance.getComponentCount();
            Asserts.checkArgument(values.length == vectorSize,
                "Incorrect number of values. Vector is of size " + vectorSize);
            
            if (!instance.hasData())
                instance.assignNewDataBlock();
            
            for (int i = 0; i < vectorSize; i++)
                instance.getData().setDoubleValue(i, values[i]);
            
            return (B)this;
        }
        
        public B fill(double value)
        {
            if (!instance.hasData())
                instance.assignNewDataBlock();
            
            for (int i = 0; i < instance.getComponentCount(); i++)
                instance.getData().setDoubleValue(i, value);            
            
            return (B)this;
        }

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
    public abstract static class BaseDataChoiceBuilder<B extends BaseDataChoiceBuilder<B>> extends DataComponentBuilder<B, DataChoice>
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

        public B addItem(String name, DataComponentBuilder<?,?> itemBuilder)
        {
            return addItem(name, itemBuilder.build());
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
    public abstract static class BaseDataArrayBuilder<B extends BaseDataArrayBuilder<B>> extends DataComponentBuilder<B, DataArray>
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

        public B withElement(String name, DataComponentBuilder<?,?> eltBuilder)
        {
            return withElement(name, eltBuilder.build());
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
    public abstract static class BaseMatrixBuilder<B extends BaseMatrixBuilder<B>> extends DataComponentBuilder<B, Matrix>
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

        public B refFrame(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI            
            instance.setReferenceFrame(uri);
            return (B)this;
        }

        public B localFrame(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI
            instance.setLocalFrame(uri);
            return (B)this;
        }

        public B size(int numRows, int numCols, boolean rowMajor)
        {
            if (rowMajor)
            this.dimSizes = rowMajor ? new int[] {numRows, numCols} : new int[] {numCols, numRows};
            this.eltNames = rowMajor ? new String[] {"row", "elt"} : new String[] {"col", "elt"};
            return (B)this;
        }

        public B dataType(DataType dataType)
        {
            this.dataType = dataType;
            return (B)this;
        }

        public B withElement(String name, ScalarComponent component)
        {
            this.scalarEltName = Asserts.checkNotNull(name, "name");
            this.eltType = Asserts.checkNotNull(component, ScalarComponent.class);
            return (B)this;
        }

        public <NB extends SimpleComponentBuilder<NB, T>, T extends ScalarComponent>
            B withElement(String name, SimpleComponentBuilder<NB, T> eltBuilder)
        {
            var elt = eltBuilder.build();
            
            if (elt instanceof Quantity)
                withElement(name, (Quantity)elt);
            else if (elt instanceof Count)
                withElement(name, (Count)elt);
            else if (elt instanceof Time)
                withElement(name, (Time)elt);
            
            return (B)this;
        }

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