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

import org.isotc211.v2005.gmd.CIOnlineResource;
import org.vast.swe.SWEBuilders.SimpleComponentBuilder;
import org.vast.swe.SWEBuilders.SweIdentifiableBuilder;
import org.vast.util.BaseBuilder;
import org.vast.util.NestedBuilder;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.sensorml.v20.AbstractMetadataList;
import net.opengis.sensorml.v20.CapabilityList;
import net.opengis.sensorml.v20.CharacteristicList;
import net.opengis.sensorml.v20.ClassifierList;
import net.opengis.sensorml.v20.DocumentList;
import net.opengis.sensorml.v20.IdentifierList;
import net.opengis.sensorml.v20.Term;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.SimpleComponent;


/**
 * <p>
 * Utility class to build SensorML metadata lists with a fluent API
 * </p>
 *
 * @author Alex Robin
 * @date Apr 29, 2020
 */
public class SMLMetadataBuilders
{
    public final static String SYSTEM_CAPS_ROLE = "http://www.w3.org/ns/ssn/systems/SystemCapability";
    public final static String CONDITION_ROLE = "http://www.w3.org/ns/ssn/systems/Condition";
    
    
    /*
     * Base builder for all metadata lists
     */
    @SuppressWarnings("unchecked")
    public static abstract class MetadataListBuilder<
            B extends MetadataListBuilder<B, T, E>,
            T extends AbstractMetadataList, E>
        extends SweIdentifiableBuilder<B, T>
    {
        SMLFactory smlFac;

        protected MetadataListBuilder(SMLFactory fac)
        {
            this.smlFac = fac;
        }

        /**
         * Start from an existing instance
         * @param obj instance to start from.
         * No copy is made, the instance will be mutated by the builder
         * @return This builder for chaining
         */
        public B from(T obj)
        {
            this.instance = obj;
            return (B)this;
        }

        public B definition(String defUri)
        {
            instance.setDefinition(defUri);
            return (B)this;
        }
    }


    /*
     * Base builder for all named property lists
     */
    
    public static abstract class PropertyListBuilder<
        B extends PropertyListBuilder<B, T, E>,
        T extends AbstractMetadataList, E>
        extends MetadataListBuilder<B, T, E>
    {
        protected PropertyListBuilder(SMLFactory fac)
        {
            super(fac);
        }
        
        public abstract B add(String name, E item);
        public abstract B addCondition(String name, SimpleComponent item);
        

        public B add(String name, BaseBuilder<? extends E> builder)
        {
            return add(name, builder.build());
        }
        

        public B addCondition(String name, SimpleComponentBuilder<?,?> builder)
        {
            return addCondition(name, builder.build());
        }
    }
    
    
    /**
     * <p>
     * Builder class for IdentifierList
     * </p>
     *
     * @author Alex Robin
     * @date Apr 29, 2020
     */
    public static class IdentifierListBuilder extends BaseIdentifierListBuilder<IdentifierListBuilder>
    {
        public IdentifierListBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newIdentifierList();
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseIdentifierListBuilder<B extends BaseIdentifierListBuilder<B>>
        extends MetadataListBuilder<B, IdentifierList, Term>
    {
        protected BaseIdentifierListBuilder(SMLFactory fac)
        {
            super(fac);
        }

        public B add(Term item)
        {
            instance.addIdentifier(item);
            return (B)this;
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedIdentifierListBuilder<B> extends BaseIdentifierListBuilder<NestedIdentifierListBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedIdentifierListBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newIdentifierList();
            this.parent = parent;
        }
    }


    /**
     * <p>
     * Builder class for ClassifierList
     * </p>
     *
     * @author Alex Robin
     * @date Apr 29, 2020
     */
    public static class ClassifierListBuilder extends BaseClassifierListBuilder<ClassifierListBuilder>
    {
        public ClassifierListBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newClassifierList();
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseClassifierListBuilder<B extends BaseClassifierListBuilder<B>>
        extends MetadataListBuilder<B, ClassifierList, Term>
    {
        protected BaseClassifierListBuilder(SMLFactory fac)
        {
            super(fac);
        }

        public B add(Term item)
        {
            instance.addClassifier(item);
            return (B)this;
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedClassifierListBuilder<B> extends BaseClassifierListBuilder<NestedClassifierListBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedClassifierListBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newClassifierList();
            this.parent = parent;
        }
    }


    /**
     * <p>
     * Builder class for CharacteristicList
     * </p>
     *
     * @author Alex Robin
     * @date June 29, 2020
     */
    public static class CharacteristicListBuilder extends BaseCharacteristicListBuilder<CharacteristicListBuilder>
    {
        public CharacteristicListBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newCharacteristicList();
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseCharacteristicListBuilder<B extends BaseCharacteristicListBuilder<B>>
        extends PropertyListBuilder<B, CharacteristicList, DataComponent>
    {
        protected BaseCharacteristicListBuilder(SMLFactory fac)
        {
            super(fac);
        }
        
        @Override
        public B addCondition(String name, SimpleComponent item)
        {
            OgcProperty<DataComponent> prop = new OgcPropertyImpl<>(item);
            prop.setName(name);
            prop.setRole(CONDITION_ROLE);
            instance.getCharacteristicList().add(prop);
            return (B)this;
        }

        @Override
        public B add(String name, DataComponent item)
        {
            instance.addCharacteristic(name, item);
            return (B)this;
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedCharacteristicListBuilder<B> extends BaseCharacteristicListBuilder<NestedCharacteristicListBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedCharacteristicListBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newCharacteristicList();
            this.parent = parent;
        }
    }


    /**
     * <p>
     * Builder class for CapabilityList
     * </p>
     *
     * @author Alex Robin
     * @date June 29, 2020
     */
    public static class CapabilityListBuilder extends BaseCapabilityListBuilder<CapabilityListBuilder>
    {
        public CapabilityListBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newCapabilityList();
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseCapabilityListBuilder<B extends BaseCapabilityListBuilder<B>>
        extends PropertyListBuilder<B, CapabilityList, DataComponent>
    {
        protected BaseCapabilityListBuilder(SMLFactory fac)
        {
            super(fac);
        }
        
        @Override
        public B addCondition(String name, SimpleComponent item)
        {
            OgcProperty<DataComponent> prop = new OgcPropertyImpl<>(item);
            prop.setName(name);
            prop.setRole(CONDITION_ROLE);
            instance.getCapabilityList().add(prop);
            return (B)this;
        }

        @Override
        public B add(String name, DataComponent item)
        {
            instance.addCapability(name, item);
            return (B)this;
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedCapabilityListBuilder<B> extends BaseCapabilityListBuilder<NestedCapabilityListBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedCapabilityListBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newCapabilityList();
            this.parent = parent;
        }
    }


    /**
     * <p>
     * Builder class for DocumentList
     * </p>
     *
     * @author Alex Robin
     * @date Apr 29, 2020
     */
    public static class DocumentListBuilder extends BaseDocumentListBuilder<DocumentListBuilder>
    {
        public DocumentListBuilder(SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newDocumentList();
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseDocumentListBuilder<B extends BaseDocumentListBuilder<B>>
        extends MetadataListBuilder<B, DocumentList, CIOnlineResource>
    {
        protected BaseDocumentListBuilder(SMLFactory fac)
        {
            super(fac);
        }

        public B add(CIOnlineResource item)
        {
            instance.addDocument(item);
            return (B)this;
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedDocumentListBuilder<B> extends BaseDocumentListBuilder<NestedDocumentListBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedDocumentListBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newDocumentList();
            this.parent = parent;
        }
    }


    /**
     * <p>
     * Builder class for CIOnlineResource
     * </p>
     *
     * @author Alex Robin
     * @date Apr 29, 2020
     */
    public static class CIOnlineResourceBuilder extends BaseCIOnlineResourceBuilder<CIOnlineResourceBuilder>
    {
        public CIOnlineResourceBuilder(org.isotc211.v2005.gmd.Factory fac)
        {
            super(fac);
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseCIOnlineResourceBuilder<B extends BaseCIOnlineResourceBuilder<B>>
        extends BaseBuilder<CIOnlineResource>
    {
        protected BaseCIOnlineResourceBuilder(org.isotc211.v2005.gmd.Factory fac)
        {
            this.instance = fac.newCIOnlineResource();
        }
        
        public B name(String name)
        {
            instance.setName(name);
            return (B)this;
        }
        
        public B description(String desc)
        {
            instance.setDescription(desc);
            return (B)this;
        }
        
        public B url(String url)
        {
            instance.setLinkage(url);
            return (B)this;
        }
    }

    /* Nested builder for use within another builder */
    public static abstract class NestedCIOnlineResourceBuilder<B> extends BaseDocumentListBuilder<NestedCIOnlineResourceBuilder<B>> implements NestedBuilder<B>
    {
        B parent;

        protected NestedCIOnlineResourceBuilder(B parent, SMLFactory fac)
        {
            super(fac);
            this.instance = fac.newDocumentList();
            this.parent = parent;
        }
    }

}
