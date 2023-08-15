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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import org.isotc211.v2005.gco.impl.CodeListValueImpl;
import org.isotc211.v2005.gmd.CIAddress;
import org.isotc211.v2005.gmd.CIContact;
import org.isotc211.v2005.gmd.CIOnlineResource;
import org.isotc211.v2005.gmd.CIResponsibleParty;
import org.isotc211.v2005.gmd.CITelephone;
import org.vast.swe.SWEBuilders.SimpleComponentBuilder;
import org.vast.swe.SWEBuilders.SweIdentifiableBuilder;
import org.vast.util.BaseBuilder;
import net.opengis.sensorml.v20.AbstractMetadataList;
import net.opengis.sensorml.v20.CapabilityList;
import net.opengis.sensorml.v20.CharacteristicList;
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
            instance.addCondition(name, item);
            return (B)this;
        }

        @Override
        public B add(String name, DataComponent item)
        {
            instance.addCharacteristic(name, item);
            return (B)this;
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
            instance.addCondition(name, item);
            return (B)this;
        }

        @Override
        public B add(String name, DataComponent item)
        {
            instance.addCapability(name, item);
            return (B)this;
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
            try {
                if (url != null)
                    URI.create(url).toURL(); // validate URL
            }
            catch (MalformedURLException e) {
                throw new IllegalArgumentException(e);
            }
            instance.setLinkage(url);
            return (B)this;
        }
        
        public B mediaType(String type)
        {
            instance.setApplicationProfile(type);
            return (B)this;
        }
    }


    /**
     * <p>
     * Builder class for CIResponsibleParty
     * </p>
     *
     * @author Alex Robin
     * @date Aug 8, 2023
     */
    public static class CIResponsiblePartyBuilder extends BaseCIResponsiblePartyBuilder<CIResponsiblePartyBuilder>
    {
        public CIResponsiblePartyBuilder(org.isotc211.v2005.gmd.Factory fac)
        {
            super(fac);
        }
    }

    @SuppressWarnings("unchecked")
    public abstract static class BaseCIResponsiblePartyBuilder<B extends BaseCIResponsiblePartyBuilder<B>>
        extends BaseBuilder<CIResponsibleParty>
    {
        org.isotc211.v2005.gmd.Factory fac;
        
        protected BaseCIResponsiblePartyBuilder(org.isotc211.v2005.gmd.Factory fac)
        {
            this.fac = fac;
            this.instance = fac.newCIResponsibleParty();
        }
        
        public B role(String uri)
        {
            if (uri != null)
                URI.create(uri); // validate URI
            var code = new CodeListValueImpl(uri);
            instance.setRole(code);
            return (B)this;
        }
        
        public B organisationName(String name)
        {
            instance.setOrganisationName(name);
            return (B)this;
        }
        
        public B individualName(String name)
        {
            instance.setIndividualName(name);
            return (B)this;
        }
        
        public B positionName(String name)
        {
            instance.setPositionName(name);
            return (B)this;
        }
        
        public B deliveryPoint(String s)
        {
            setAsFirstItem(ensureAddress().getDeliveryPointList(), s);
            return (B)this;
        }
        
        public B city(String s)
        {
            ensureAddress().setCity(s);
            return (B)this;
        }
        
        public B administrativeArea(String s)
        {
            ensureAddress().setAdministrativeArea(s);
            return (B)this;
        }
        
        public B postalCode(String s)
        {
            ensureAddress().setPostalCode(s);
            return (B)this;
        }
        
        public B country(String s)
        {
            ensureAddress().setCountry(s);
            return (B)this;
        }
        
        public B email(String s)
        {
            setAsFirstItem(ensureAddress().getElectronicMailAddressList(), s);
            return (B)this;
        }
        
        public B phone(String s)
        {
            setAsFirstItem(ensurePhone().getVoiceList(), s);
            return (B)this;
        }
        
        public B fax(String s)
        {
            setAsFirstItem(ensurePhone().getFacsimileList(), s);
            return (B)this;
        }
        
        public B website(String url)
        {
            var ref = fac.newCIOnlineResource();
            ref.setLinkage(url);
            ensureContactInfo().setOnlineResource(ref);
            return (B)this;
        }
        
        public B hoursOfService(String s)
        {
            ensureContactInfo().setHoursOfService(s);
            return (B)this;
        }
        
        public B contactInstructions(String s)
        {
            ensureContactInfo().setContactInstructions(s);
            return (B)this;
        }
        
        protected CIContact ensureContactInfo()
        {
            if (!instance.isSetContactInfo())
                instance.setContactInfo(fac.newCIContact());
            return instance.getContactInfo();
        }
        
        protected CITelephone ensurePhone()
        {
            var ci = ensureContactInfo();
            if (!ci.isSetPhone())
                ci.setPhone(fac.newCITelephone());
            return ci.getPhone();
        }
        
        protected CIAddress ensureAddress()
        {
            var ci = ensureContactInfo();
            if (!ci.isSetAddress())
                ci.setAddress(fac.newCIAddress());
            return ci.getAddress();
        }
        
        protected void setAsFirstItem(List<String> list, String s)
        {
            if (list.isEmpty())
                list.add(s);
            else
                list.set(0, s);
        }
    }

}
