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

import java.time.OffsetDateTime;
import java.util.function.Consumer;
import org.isotc211.v2005.gmd.CIOnlineResource;
import org.vast.swe.SWEBuilders.SweIdentifiableBuilder;
import org.vast.util.Asserts;
import org.vast.util.BaseBuilder;
import com.google.common.base.Strings;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.TimeIndeterminateValue;
import net.opengis.gml.v32.impl.ReferenceImpl;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.AggregateProcess;
import net.opengis.sensorml.v20.CapabilityList;
import net.opengis.sensorml.v20.CharacteristicList;
import net.opengis.sensorml.v20.ClassifierList;
import net.opengis.sensorml.v20.DataInterface;
import net.opengis.sensorml.v20.DescribedObject;
import net.opengis.sensorml.v20.DocumentList;
import net.opengis.sensorml.v20.IdentifierList;
import net.opengis.sensorml.v20.ObservableProperty;
import net.opengis.sensorml.v20.PhysicalComponent;
import net.opengis.sensorml.v20.PhysicalSystem;
import net.opengis.sensorml.v20.ProcessMethod;
import net.opengis.sensorml.v20.SimpleProcess;
import net.opengis.sensorml.v20.SpatialFrame;
import net.opengis.sensorml.v20.Term;
import net.opengis.swe.v20.DataComponent;
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

    
    /*
     * Base builder for all described objects
     */
    @SuppressWarnings("unchecked")
    public static abstract class DescribedObjectBuilder<
            B extends DescribedObjectBuilder<B, T>,
            T extends DescribedObject>
        extends BaseBuilder<T>
    {
        net.opengis.sensorml.v20.Factory smlFac;
        net.opengis.gml.v32.Factory gmlFac;
        Consumer<T> visitor;

        protected DescribedObjectBuilder(SMLFactory smlFac)
        {
            this.smlFac = smlFac;
            this.gmlFac = SMLHelper.DEFAULT_GML_FACTORY;
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
        public B id(String id)
        {
            instance.setId(id);
            return (B)this;
        }

        /**
         * Sets the process name
         * @param name
         * @return This builder for chaining
         */
        public B name(String name)
        {
            instance.setName(name);
            return (B)this;
        }

        /**
         * Sets the process description
         * @param description
         * @return This builder for chaining
         */
        public B description(String description)
        {
            instance.setDescription(description);
            return (B)this;
        }

        /**
         * Sets the process unique identifier
         * @param uid
         * @return This builder for chaining
         */
        public B uniqueID(String uid)
        {
            instance.setUniqueIdentifier(uid);
            return (B)this;
        }
        
        public B addIdentifierList(IdentifierList idList)
        {
            instance.addIdentification(idList);
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

            Term term = smlFac.newTerm();
            term.setDefinition(def);
            term.setLabel(label);
            term.setValue(value);
            
            return addIdentifier(term);
        }
        
        /**
         * Adds an identifier to the default list (first one in document)
         * @param term The identifier
         * @return This builder for chaining
         */
        public B addIdentifier(Term term)
        {
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
            
            idList.addIdentifier(term);
            return (B)this;
        }
        
        public B addClassifierList(ClassifierList idList)
        {
            instance.addClassification(idList);
            return (B)this;
        }
        
        /**
         * Adds a classifier to the default list (first one in document)
         * @param term The classifier
         * @return This builder for chaining
         */
        public B addClassifier(Term term)
        {
            // ensure we have a classification section
            OgcPropertyList<ClassifierList> sectionList = instance.getClassificationList();
            ClassifierList termList;
            if (sectionList.isEmpty())
            {
                termList = smlFac.newClassifierList();
                sectionList.add(termList);
            }
            else
                termList = sectionList.get(0);
            
            termList.addClassifier(term);
            return (B)this;
        }
        
        public B validTimePeriod(OffsetDateTime begin, OffsetDateTime end)
        {
            var beginPos = gmlFac.newTimePosition();
            beginPos.setDateTimeValue(begin);            
            var endPos = gmlFac.newTimePosition();
            endPos.setDateTimeValue(end);            
            var p = gmlFac.newTimePeriod();
            p.setBeginPosition(beginPos);
            p.setEndPosition(endPos);            
            instance.addValidTimeAsTimePeriod(p);
            return (B)this;
        }
        
        public B validFrom(OffsetDateTime begin)
        {
            var beginPos = gmlFac.newTimePosition();
            beginPos.setDateTimeValue(begin);            
            var endPos = gmlFac.newTimePosition();
            endPos.setIndeterminatePosition(TimeIndeterminateValue.NOW);            
            var p = gmlFac.newTimePeriod();
            p.setBeginPosition(beginPos);
            p.setEndPosition(endPos);            
            instance.addValidTimeAsTimePeriod(p);
            return (B)this;
        }
        
        public B addCharacteristicList(String name, CharacteristicList propList)
        {
            instance.addCharacteristics(name, propList);
            return (B)this;
        }
        
        public B addCapabilityList(String name, CapabilityList propList)
        {
            instance.addCapabilities(name, propList);
            return (B)this;
        }
        
        public B addDocument(String role, String label, String desc, String url)
        {
            // ensure we have a documentation section
            OgcPropertyList<DocumentList> sectionList = instance.getDocumentationList();
            DocumentList docList;
            if (sectionList.isEmpty())
            {
                docList = smlFac.newDocumentList();
                sectionList.add(docList);
            }
            else
                docList = sectionList.get(0);
            
            CIOnlineResource doc = SMLHelper.DEFAULT_GMD_FACTORY.newCIOnlineResource();
            doc.setName(label);
            doc.setLinkage(url);
            
            OgcProperty<CIOnlineResource> prop = new OgcPropertyImpl<>(doc);
            prop.setRole(role);
            docList.getDocumentList().add(prop);
            
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
        
        public B typeOf(String href)
        {
            instance.setTypeOf(new ReferenceImpl(href));
            return (B)this;
        }
        
        public B typeOf(String uid, String href)
        {
            ReferenceImpl ref = new ReferenceImpl(href);
            ref.setTitle(uid);
            instance.setTypeOf(ref);
            return (B)this;
        }
        
        public B addInput(String name, ObservableProperty obsProp)
        {
            instance.addInput(name, obsProp);
            return (B)this;
        }
        
        public B addInput(String name, DataComponent comp)
        {
            instance.addInput(name, comp);
            return (B)this;
        }
        
        public B addInput(String name, DataInterface di)
        {
            instance.addOutput(name, di);
            return (B)this;
        }
        
        public B addOutput(String name, DataComponent comp)
        {
            instance.addOutput(name, comp);
            return (B)this;
        }
        
        public B addOutput(String name, DataInterface di)
        {
            instance.addOutput(name, di);
            return (B)this;
        }
        
        public B addParameter(String name, DataComponent comp)
        {
            instance.addParameter(name, comp);
            return (B)this;
        }

        /**
         * Validate the process before it is built
         * @return This builder for chaining
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
        public SimpleProcessBuilder(SMLFactory fac)
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

        public B methodURI(String uri)
        {
            instance.getMethodProperty().setHref(uri);
            return (B)this;
        }

        public B method(ProcessMethod method)
        {
            instance.setMethod(method);;
            return (B)this;
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
        public AggregateProcessBuilder(SMLFactory fac)
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
        public PhysicalComponentBuilder(SMLFactory fac)
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

        public B location(Vector v)
        {
            instance.addPositionAsVector(v);
            return (B)this;
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
        public PhysicalSystemBuilder(SMLFactory fac)
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
        
        public B addLocalReferenceFrame(SpatialFrame refFrame)
        {
            instance.addLocalReferenceFrame(refFrame);
            return (B)this;
        }

        public B setLocation(Vector loc)
        {
            // automatically assign reference frame if already defined
            if (!instance.getLocalReferenceFrameList().isEmpty())
            {
                SpatialFrame localFrame = instance.getLocalReferenceFrameList().get(0);
                loc.setLocalFrame("#" + localFrame.getId());                
            }
            
            instance.addPositionAsVector(loc);
            return (B)this;
        }

        public B addComponent(String name, AbstractProcess component)
        {
            instance.addComponent(name, component);
            return (B)this;
        }

        public B addComponentLocation(String id, Vector loc)
        {
            instance.addPositionAsVector(loc);
            return (B)this;
        }
    }    
    
    
    /**
     * <p>
     * Builder class for Term object
     * </p>
     *
     * @author Alex Robin
     * @date June 21, 2020
     */
    public static class TermBuilder extends BaseTermBuilder<TermBuilder>
    {
        public TermBuilder(SMLFactory fac)
        {
            super(fac);
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseTermBuilder<B extends BaseTermBuilder<B>> extends BaseBuilder<Term>
    {
        SMLFactory fac;
        
        protected BaseTermBuilder(SMLFactory fac)
        {
            this.fac = fac;
            this.instance = fac.newTerm();
        }

        public B copyFrom(Term base)
        {
            instance.setDefinition(base.getDefinition());
            instance.setLabel(base.getLabel());
            instance.setCodeSpace(base.getCodeSpace());
            instance.setValue(base.getValue());
            return (B)this;
        }

        /**
         * Sets the term label
         * @param label
         * @return This builder for chaining
         */
        public B label(String label)
        {
            instance.setLabel(label);
            return (B)this;
        }

        /**
         * Sets the term definition URI
         * @param defUri URI of definition
         * @return This builder for chaining
         */
        public B definition(String defUri)
        {
            instance.setDefinition(defUri);
            return (B)this;
        }

        /**
         * Sets the term value
         * @param value
         * @return This builder for chaining
         */
        public B value(String value)
        {
            instance.setValue(value);
            return (B)this;
        }
    }   
    
    
    /**
     * <p>
     * Builder class for ObservableProperty object
     * </p>
     *
     * @author Alex Robin
     * @date June 21, 2020
     */
    public static class ObsPropBuilder extends BaseObsPropBuilder<ObsPropBuilder>
    {
        public ObsPropBuilder(SMLFactory fac)
        {
            super(fac);
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseObsPropBuilder<B extends BaseObsPropBuilder<B>> 
        extends SweIdentifiableBuilder<BaseObsPropBuilder<B>, ObservableProperty>
    {
                
        protected BaseObsPropBuilder(SMLFactory fac)
        {
            this.instance = fac.newObservableProperty();
        }

        public B copyFrom(ObservableProperty base)
        {
            super.copyFrom(base);
            instance.setDefinition(base.getDefinition());
            return (B)this;
        }
        
        /**
         * Sets the observable definition URI
         * @param defUri URI of definition
         * @return This builder for chaining
         */
        public B definition(String defUri)
        {
            instance.setDefinition(defUri);
            return (B)this;
        }
    }
}
