/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2020 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensorML;

import java.util.function.Consumer;
import org.vast.data.SWEFactory;
import org.vast.sensorML.SMLMetadataBuilders.NestedDocumentListBuilder;
import org.vast.swe.SWEHelper;
import org.vast.swe.SWEBuilders.NestedVectorBuilder;
import org.vast.util.Asserts;
import org.vast.util.BaseBuilder;
import org.vast.util.NestedBuilder;
import com.google.common.base.Strings;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.impl.ReferenceImpl;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.AggregateProcess;
import net.opengis.sensorml.v20.DescribedObject;
import net.opengis.sensorml.v20.DocumentList;
import net.opengis.sensorml.v20.IdentifierList;
import net.opengis.sensorml.v20.PhysicalComponent;
import net.opengis.sensorml.v20.PhysicalSystem;
import net.opengis.sensorml.v20.ProcessMethod;
import net.opengis.sensorml.v20.SimpleProcess;
import net.opengis.sensorml.v20.Term;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Utility class to build SensorML processing components with a fluent API
 * </p>
 *
 * @author Alex Robin
 * @date Apr 17, 2020
 */
public class SMLBuilders
{
    public static final String LONG_NAME_DEF = SWEHelper.getPropertyUri("LongName");
    public static final String LONG_NAME_LABEL = "Long Name";
    public static final String SHORT_NAME_DEF = SWEHelper.getPropertyUri("ShortName");
    public static final String SHORT_NAME_LABEL = "Short Name";
    public static final String SERIAL_NUMBER_DEF = SWEHelper.getPropertyUri("SerialNumber");
    public static final String SERIAL_NUMBER_LABEL = "Serial Number";
    public static final String MODEL_NUMBER_DEF = SWEHelper.getPropertyUri("ModelNumber");
    public static final String MODEL_NUMBER_LABEL = "Model Number";
    public static final String MANUFACTURER_DEF = SWEHelper.getPropertyUri("Manufacturer");
    public static final String MANUFACTURER_LABEL = "Manufacturer Name";

    public static SMLFactory DEFAULT_SML_FACTORY = new SMLFactory();
    public static SWEFactory DEFAULT_SWE_FACTORY = new SWEFactory();
    public static org.isotc211.v2005.gmd.Factory DEFAULT_GMD_FACTORY = new org.isotc211.v2005.gmd.impl.GMDFactory();
    public static org.isotc211.v2005.gco.Factory DEFAULT_GCO_FACTORY = new org.isotc211.v2005.gco.impl.GCOFactory();
    
    
    /**
     * Helper method to edit a SensorML process in-place using a builder
     * @param <B> Type of expected builder
     * @param process process instance to start from
     * @return A builder corresponding to the provided provided process type
     */
    @SuppressWarnings("unchecked")
    public static <B extends AbstractProcessBuilder<?,?>> B edit(AbstractProcess process)
    {
        Asserts.checkNotNull(process, AbstractProcess.class);
        
        if (process instanceof PhysicalSystem)
            return (B)createPhysicalSystem();
        else if (process instanceof PhysicalComponent)
            return (B)createPhysicalComponent();
        else if (process instanceof AggregateProcess)
            return (B)createAggregateProcess();
        else if (process instanceof SimpleProcess)
            return (B)createSimpleProcess();
        else
            throw new IllegalArgumentException("Unsupported process type: " + process.getClass().getCanonicalName());
    }
    
    
    /**
     * @return A builder to create a new SimpleProcess object
     */
    public static SimpleProcessBuilder createSimpleProcess()
    {        
        return createSimpleProcess(DEFAULT_SML_FACTORY);
    }
    
    
    /**
     * @param fac Factory to use to create SML objects
     * @return A builder to create a new SimpleProcess object
     */
    public static SimpleProcessBuilder createSimpleProcess(SMLFactory fac)
    {
        return new SimpleProcessBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new AggregateProcess object
     */
    public static AggregateProcessBuilder createAggregateProcess()
    {        
        return createAggregateProcess(DEFAULT_SML_FACTORY);
    }
    
    
    /**
     * @param fac Factory to use to create SML objects
     * @return A builder to create a new AggregateProcess object
     */
    public static AggregateProcessBuilder createAggregateProcess(SMLFactory fac)
    {
        return new AggregateProcessBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new PhysicalComponent object
     */
    public static PhysicalComponentBuilder createPhysicalComponent()
    {        
        return createPhysicalComponent(DEFAULT_SML_FACTORY);
    }
    
    
    /**
     * @param fac Factory to use to create SML objects
     * @return A builder to create a new PhysicalComponent object
     */
    public static PhysicalComponentBuilder createPhysicalComponent(SMLFactory fac)
    {
        return new PhysicalComponentBuilder(fac);
    }
    
    
    /**
     * @return A builder to create a new PhysicalSystem object
     */
    public static PhysicalSystemBuilder createPhysicalSystem()
    {        
        return createPhysicalSystem(DEFAULT_SML_FACTORY);
    }
    
    
    /**
     * @param fac Factory to use to create SML objects
     * @return A builder to create a new PhysicalSystem object
     */
    public static PhysicalSystemBuilder createPhysicalSystem(SMLFactory fac)
    {
        return new PhysicalSystemBuilder(fac);
    }
    
    
    /*
     * Base builder for all described objects
     */
    @SuppressWarnings("unchecked")
    public static abstract class DescribedObjectBuilder<
            B extends DescribedObjectBuilder<B, T>,
            T extends DescribedObject>
        extends BaseBuilder<T>
    {
        SMLFactory smlFac;
        SWEFactory sweFac;
        Consumer<T> visitor;
        
        protected DescribedObjectBuilder(SMLFactory fac)
        {
            this.smlFac = fac;
            this.sweFac = DEFAULT_SWE_FACTORY;
        }
        
        /**
         * Use a custom factory to create SWE data components
         * @param fac
         * @return This builder for chaining
         */
        public B usingSweFactory(SWEFactory fac)
        {
            this.sweFac = fac;
            return (B)this;
        }
        
        /**
         * Start from an existing instance
         * @param obj instance of DescribedObject to start from.
         * No copy is made, the instance will be mutated by the builder
         * @return This builder for chaining
         */
        public B from(T obj)
        {
            this.instance = obj;
            return (B)this;
        }
        
        /**
         * Sets the process ID
         * @param id
         * @return This builder for chaining
         */
        public B setID(String id)
        {
            instance.setId(id);
            return (B)this;
        }
        
        /**
         * Sets the process name
         * @param name
         * @return This builder for chaining
         */
        public B setName(String name)
        {
            instance.setName(name);
            return (B)this;
        }
        
        /**
         * Sets the process description
         * @param description
         * @return This builder for chaining
         */
        public B setDescription(String description)
        {
            instance.setDescription(description);
            return (B)this;
        }
        
        /**
         * Sets the process unique identifier
         * @param uid
         * @return This builder for chaining
         */
        public B setUniqueIdentifier(String uid)
        {
            Asserts.checkNotNull(uid, "uid");
            return (B)this;
        }
        
        /**
         * Adds a custom identifier
         * @param label Label of identifier (e.g. "Serial Number")
         * @param def Definition URI of identifier (link to online definition)
         * @param value Identifier value
         * @return This builder for chaining
         */
        public B addIdentifier(String label, String def, String value)
        {
            Asserts.checkNotNull(label, "label");
            Asserts.checkNotNull(def, "definition");
            Asserts.checkNotNull(value, "value");
            
            // ensure we have an identification section
            OgcPropertyList<IdentifierList> sectionList = instance.getIdentificationList();
            IdentifierList idList;
            if (sectionList.isEmpty())
            {
                idList = smlFac.newIdentifierList();
                sectionList.add(idList);
            }
            else
                idList = sectionList.get(0);
            
            Term term = smlFac.newTerm();
            term.setDefinition(def);
            term.setLabel(label);
            term.setValue(value);
            idList.addIdentifier(term);
            return (B)this;
        }
        
        public B addDocumentList(DocumentList docList)
        {
            instance.addDocumentation(docList);
            return (B)this;
        }
        
        public NestedDocumentListBuilder<B> addDocumentList()
        {
            return new NestedDocumentListBuilder<B>((B)this, smlFac)
            {
                @Override
                public B done()
                {
                    DescribedObjectBuilder.this.addDocumentList(build());
                    return (B)DescribedObjectBuilder.this;
                }
            };
        }
        
        /**
         * Adds a "Short Name" identifier
         * @param value
         * @return This builder for chaining
         */
        public B setShortName(String value)
        {
            addIdentifier(SHORT_NAME_LABEL, SHORT_NAME_DEF, value);
            return (B)this;
        }
        
        /**
         * Adds a "Long Name" identifier
         * @param value
         * @return This builder for chaining
         */
        public B setLongName(String value)
        {
            addIdentifier(LONG_NAME_LABEL, LONG_NAME_DEF, value);
            return (B)this;
        }
    }
    
    
    /*
     * Base builder for all process types
     */
    @SuppressWarnings("unchecked")
    public static abstract class AbstractProcessBuilder<
            B extends AbstractProcessBuilder<B, T>,
            T extends AbstractProcess>
        extends DescribedObjectBuilder<B, T>
    {
                
        protected AbstractProcessBuilder(SMLFactory fac)
        {
            super(fac);
        }
        
        /**
         * Adds a "Serial Number" identifier
         * @param value
         * @return This builder for chaining
         */
        public B setSerialNumber(String value)
        {
            addIdentifier(SERIAL_NUMBER_LABEL, SERIAL_NUMBER_DEF, value);
            return (B)this;
        }
        
        /**
         * Adds a "Model Number" identifier
         * @param value
         * @return This builder for chaining
         */
        public B setModelNumber(String value)
        {
            addIdentifier(MODEL_NUMBER_LABEL, MODEL_NUMBER_DEF, value);
            return (B)this;
        }
        
        /**
         * Adds a "Manufacturer" identifier
         * @param value
         * @return This builder for chaining
         */
        public B setManufacturerName(String value)
        {
            addIdentifier(MANUFACTURER_LABEL, MANUFACTURER_DEF, value);
            return (B)this;
        }
        
        /**
         * Validate the process before it is built
         * @throws SMLException
         */
        public B validate() throws SMLException
        {
            if (Strings.isNullOrEmpty(instance.getUniqueIdentifier()))
                throw new SMLException("Missing unique identifier");
            return (B)this;
        }
    }
    
    
    /**
     * <p>
     * Builder class for SimpleProcess
     * </p>
     *
     * @author Alex Robin
     * @date Apr 18, 2020
     */
    public static class SimpleProcessBuilder extends BaseSimpleProcessBuilder<SimpleProcessBuilder, SimpleProcess>    
    {
        protected SimpleProcessBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newSimpleProcess();
        }        
    }
    
    @SuppressWarnings("unchecked")
    public abstract static class BaseSimpleProcessBuilder<
            B extends BaseSimpleProcessBuilder<B, T>,
            T extends SimpleProcess>
        extends AbstractProcessBuilder<B, T>
    {
        protected BaseSimpleProcessBuilder(SMLFactory fac)
        {
            super(fac);            
        }
        
        public B setMethodURI(String uri)
        {
            instance.getMethodProperty().setHref(uri);
            return (B)this;
        }
        
        public B setMethod(ProcessMethod method)
        {
            instance.setMethod(method);;
            return (B)this;
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedSimpleProcessBuilder<B> extends BaseSimpleProcessBuilder<NestedSimpleProcessBuilder<B>, SimpleProcess> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedSimpleProcessBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newSimpleProcess();
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for AggregateProcess
     * </p>
     *
     * @author Alex Robin
     * @date Apr 28, 2020
     */
    public static class AggregateProcessBuilder extends BaseAggregateProcessBuilder<AggregateProcessBuilder, AggregateProcess>    
    {
        protected AggregateProcessBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newAggregateProcess();
        }        
    }
    
    @SuppressWarnings("unchecked")
    public abstract static class BaseAggregateProcessBuilder<
            B extends BaseAggregateProcessBuilder<B, T>,
            T extends AggregateProcess>
        extends AbstractProcessBuilder<B, T>
    {
        protected BaseAggregateProcessBuilder(SMLFactory fac)
        {
            super(fac);            
        }
        
        public B addComponent(String name, AbstractProcess p)
        {
            instance.addComponent(name, p);
            return (B)this;
        }
        
        public NestedSimpleProcessBuilder<B> addSimpleProcess(String name)
        {
            return new NestedSimpleProcessBuilder<B>((B)this, smlFac)
            {
                @Override
                public B done()
                {
                    BaseAggregateProcessBuilder.this.addComponent(name, build());
                    return (B)BaseAggregateProcessBuilder.this;
                }
            };
        }
        
        public NestedAggregateProcessBuilder<B> addNestedAggregateProcess(String name)
        {
            return new NestedAggregateProcessBuilder<B>((B)this, smlFac)
            {
                @Override
                public B done()
                {
                    BaseAggregateProcessBuilder.this.addComponent(name, build());
                    return (B)BaseAggregateProcessBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedAggregateProcessBuilder<B> extends BaseAggregateProcessBuilder<NestedAggregateProcessBuilder<B>, AggregateProcess> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedAggregateProcessBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newAggregateProcess();
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for PhysicalComponent
     * </p>
     *
     * @author Alex Robin
     * @date Apr 18, 2020
     */
    public static class PhysicalComponentBuilder extends BasePhysicalComponentBuilder<PhysicalComponentBuilder>    
    {
        protected PhysicalComponentBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPhysicalComponent();
        }        
    }    
    
    @SuppressWarnings("unchecked")
    public abstract static class BasePhysicalComponentBuilder<B extends BasePhysicalComponentBuilder<B>> extends BaseSimpleProcessBuilder<B, PhysicalComponent>
    {
        protected BasePhysicalComponentBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPhysicalComponent();
        }
        
        public B attachedTo(String platformUri)
        {
            instance.setAttachedTo(new ReferenceImpl(platformUri));
            return (B)this;
        }
        
        public B setLocation(Vector v)
        {
            instance.addPositionAsVector(v);
            return (B)this;
        }
        
        public NestedVectorBuilder<B> addLocation()
        {
            return new NestedVectorBuilder<B>((B)this, sweFac)
            {
                @Override
                public B done()
                {
                    BasePhysicalComponentBuilder.this.setLocation(build());
                    return (B)BasePhysicalComponentBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedPhysicalComponentBuilder<B> extends BasePhysicalComponentBuilder<NestedPhysicalComponentBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedPhysicalComponentBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPhysicalComponent();
            this.parent = parent;
        }
    }
    
    
    /**
     * <p>
     * Builder class for PhysicalSystem
     * </p>
     *
     * @author Alex Robin
     * @date Apr 18, 2020
     */
    public static class PhysicalSystemBuilder extends BasePhysicalSystemBuilder<PhysicalSystemBuilder>    
    {
        protected PhysicalSystemBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPhysicalSystem();
        }        
    }    
    
    @SuppressWarnings("unchecked")
    public abstract static class BasePhysicalSystemBuilder<B extends BasePhysicalSystemBuilder<B>> extends BaseAggregateProcessBuilder<B, PhysicalSystem>
    {
        protected BasePhysicalSystemBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPhysicalSystem();
        }        
        
        public B attachedTo(String platformUri)
        {
            instance.setAttachedTo(new ReferenceImpl(platformUri));
            return (B)this;
        }
        
        public B setLocation(Vector v)
        {
            instance.addPositionAsVector(v);
            return (B)this;
        }
        
        public NestedVectorBuilder<B> addLocation()
        {
            return new NestedVectorBuilder<B>((B)this, sweFac)
            {
                @Override
                public B done()
                {
                    BasePhysicalSystemBuilder.this.setLocation(build());
                    return (B)BasePhysicalSystemBuilder.this;
                }
            };
        }
        
        public NestedPhysicalComponentBuilder<B> withPhysicalComponent(String name)
        {
            return new NestedPhysicalComponentBuilder<B>((B)this, smlFac)
            {
                @Override
                public B done()
                {
                    BasePhysicalSystemBuilder.this.addComponent(name, build());
                    return (B)BasePhysicalSystemBuilder.this;
                }
            };
        }
        
        public NestedPhysicalSystemBuilder<B> withNestedPhysicalsystem(String name)
        {
            return new NestedPhysicalSystemBuilder<B>((B)this, smlFac)
            {
                @Override
                public B done()
                {
                    BasePhysicalSystemBuilder.this.addComponent(name, build());
                    return (B)BasePhysicalSystemBuilder.this;
                }
            };
        }
    }
    
    /* Nested builder for use within another builder */
    public static abstract class NestedPhysicalSystemBuilder<B> extends BasePhysicalSystemBuilder<NestedPhysicalSystemBuilder<B>> implements NestedBuilder<B>
    {
        B parent;
        
        protected NestedPhysicalSystemBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newPhysicalSystem();
            this.parent = parent;
        }
    }
}
