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
import org.vast.swe.SWEBuilders.SweIdentifiableBuilder;
import org.vast.util.BaseBuilder;
import org.vast.util.NestedBuilder;
import net.opengis.sensorml.v20.AbstractMetadataList;
import net.opengis.sensorml.v20.ClassifierList;
import net.opengis.sensorml.v20.DocumentList;
import net.opengis.sensorml.v20.IdentifierList;
import net.opengis.sensorml.v20.Term;


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

    /**
     * @return A builder to create a new DocumentList object
     */
    public static DocumentListBuilder newDocumentList()
    {
        return newDocumentList(SMLBuilders.DEFAULT_SML_FACTORY);
    }


    /**
     * @param fac Factory to use to create SML objects
     * @return A builder to create a new DocumentList object
     */
    public static DocumentListBuilder newDocumentList(SMLFactory fac)
    {
        return new DocumentListBuilder(fac);
    }


    /*
     * Base builder for all unnamed metadata lists
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

        public abstract B addItem(E item);
        public abstract NestedBuilder<B> addItem();
    }


    /*
     * Base builder for all named metadata lists
     */
    public static abstract class NamedMetadataListBuilder<
            B extends NamedMetadataListBuilder<B, T, E>,
            T extends AbstractMetadataList, E>
        extends MetadataListBuilder<B, T, E>
    {
        protected NamedMetadataListBuilder(SMLFactory fac)
        {
            super(fac);
        }

        public abstract B addItem(String name, E item);
        public abstract NestedBuilder<B> addItem(String name);
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
        protected IdentifierListBuilder(SMLFactory fac)
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

        @Override
        public B addItem(Term item)
        {
            instance.addIdentifier(item);
            return (B)this;
        }

        @Override
        public NestedBuilder<B> addItem()
        {
            //TODO
            return null;
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
        protected ClassifierListBuilder(SMLFactory fac)
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

        @Override
        public B addItem(Term item)
        {
            instance.addClassifier(item);
            return (B)this;
        }

        @Override
        public NestedBuilder<B> addItem()
        {
            //TODO
            return null;
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
     * Builder class for DocumentList
     * </p>
     *
     * @author Alex Robin
     * @date Apr 29, 2020
     */
    public static class DocumentListBuilder extends BaseDocumentListBuilder<DocumentListBuilder>
    {
        protected DocumentListBuilder(SMLFactory fac)
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

        @Override
        public B addItem(CIOnlineResource item)
        {
            instance.addDocument(item);
            return (B)this;
        }

        @Override
        public NestedBuilder<B> addItem()
        {
            //TODO
            return null;
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
        protected CIOnlineResourceBuilder(org.isotc211.v2005.gmd.Factory fac)
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
