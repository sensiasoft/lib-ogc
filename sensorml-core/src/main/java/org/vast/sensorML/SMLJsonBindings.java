package org.vast.sensorML;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.function.BiConsumer;
import org.isotc211.v2005.gmd.CIContact;
import org.isotc211.v2005.gmd.CIOnlineResource;
import org.isotc211.v2005.gmd.CIResponsibleParty;
import org.isotc211.v2005.gmd.MDLegalConstraints;
import org.isotc211.v2005.gmd.impl.CIOnlineResourceImpl;
import org.vast.data.SWEFactory;
import org.vast.ogc.geopose.GeoPoseJsonBindings;
import org.vast.ogc.geopose.Pose;
import org.vast.ogc.gml.GMLUtils;
import org.vast.ogc.gml.GeoJsonBindings;
import org.vast.swe.SWEJsonBindings;
import org.vast.util.TimeExtent;
import com.google.common.base.Strings;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.OgcPropertyList;
import net.opengis.gml.v32.AbstractFeature;
import net.opengis.gml.v32.Point;
import net.opengis.gml.v32.Reference;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.impl.GMLFactory;
import net.opengis.sensorml.v20.AbstractMetadataList;
import net.opengis.sensorml.v20.AbstractModes;
import net.opengis.sensorml.v20.AbstractPhysicalProcess;
import net.opengis.sensorml.v20.AbstractProcess;
import net.opengis.sensorml.v20.AggregateProcess;
import net.opengis.sensorml.v20.CapabilityList;
import net.opengis.sensorml.v20.CharacteristicList;
import net.opengis.sensorml.v20.ClassifierList;
import net.opengis.sensorml.v20.ConfigSetting;
import net.opengis.sensorml.v20.ConstraintSetting;
import net.opengis.sensorml.v20.ContactList;
import net.opengis.sensorml.v20.Deployment;
import net.opengis.sensorml.v20.DescribedObject;
import net.opengis.sensorml.v20.DocumentList;
import net.opengis.sensorml.v20.Factory;
import net.opengis.sensorml.v20.FeatureList;
import net.opengis.sensorml.v20.IOPropertyList;
import net.opengis.sensorml.v20.IdentifierList;
import net.opengis.sensorml.v20.KeywordList;
import net.opengis.sensorml.v20.Link;
import net.opengis.sensorml.v20.Mode;
import net.opengis.sensorml.v20.ModeChoice;
import net.opengis.sensorml.v20.ObservableProperty;
import net.opengis.sensorml.v20.PhysicalComponent;
import net.opengis.sensorml.v20.PhysicalSystem;
import net.opengis.sensorml.v20.Settings;
import net.opengis.sensorml.v20.SimpleProcess;
import net.opengis.sensorml.v20.SpatialFrame;
import net.opengis.sensorml.v20.Status;
import net.opengis.sensorml.v20.Term;
import net.opengis.swe.v20.AbstractSWEIdentifiable;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.DataComponent;


@SuppressWarnings("javadoc")
public class SMLJsonBindings
{
    static final String SML_JSON_MEDIA_TYPE = "application/sml+json";
    
    protected Factory factory;
    protected net.opengis.gml.v32.Factory gmlFactory;
    protected org.isotc211.v2005.gmd.Factory isoFactory;
    protected SWEJsonBindings sweBindings;
    protected GeoJsonBindings geojsonBindings;
    protected GeoPoseJsonBindings geoposeBindings;
    protected String propName;


    public SMLJsonBindings()
    {
        this(true);
    }
    
    
    public SMLJsonBindings(boolean enforceTypeFirst)
    {
        this(new SMLFactory(), new SWEFactory(), new GMLFactory(true), enforceTypeFirst);
    }
    
    
    public SMLJsonBindings(Factory factory, net.opengis.swe.v20.Factory sweFactory, net.opengis.gml.v32.Factory gmlFactory, boolean enforceTypeFirst)
    {
        this.factory = factory;
        this.gmlFactory = gmlFactory;
        this.isoFactory = new org.isotc211.v2005.gmd.impl.GMDFactory();
        this.sweBindings = new SWEJsonBindings(sweFactory, enforceTypeFirst);
        this.geojsonBindings = new GeoJsonBindings(true, enforceTypeFirst);
        this.geoposeBindings = new GeoPoseJsonBindings();
    }
    
    
    /*
     * Read methods
     */
    
    public DescribedObject readDescribedObject(JsonReader reader) throws IOException
    {
        reader = beginObjectWithType(reader);
        var type = reader.nextString();
        
        if ("SimpleProcess".equals(type))
            return readSimpleProcess(reader);
        else if ("AggregateProcess".equals(type))
            return readAggregateProcess(reader);
        else if ("PhysicalComponent".equals(type))
            return readPhysicalComponent(reader);
        else if ("PhysicalSystem".equals(type))
            return readPhysicalSystem(reader);
        else if ("Deployment".equals(type))
            return readDeployment(reader);
        else if ("Mode".equals(type))
            return readMode(reader);
        else
            throw new JsonParseException("Unknown object type: " + type + " @ " + reader.getPath());
    }
    
    
    public AbstractProcess readAbstractProcess(JsonReader reader) throws IOException
    {
        reader = beginObjectWithType(reader);
        var type = reader.nextString();
        
        return readAbstractProcess(reader, type);
    }
    
    
    protected AbstractProcess readAbstractProcess(JsonReader reader, String type) throws IOException
    {
        if ("SimpleProcess".equals(type))
            return readSimpleProcess(reader);
        else if ("AggregateProcess".equals(type))
            return readAggregateProcess(reader);
        else if ("PhysicalComponent".equals(type))
            return readPhysicalComponent(reader);
        else if ("PhysicalSystem".equals(type))
            return readPhysicalSystem(reader);
        else
            throw new JsonParseException("Unknown object type: " + type + " @ " + reader.getPath());
    }
    
    
    public SimpleProcess readSimpleProcess(JsonReader reader) throws IOException
    {
        var bean = factory.newSimpleProcess();
        
        // start reading object and check type if not already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
        {
            reader.beginObject();
            checkObjectType(reader, "SimpleProcess");
        }
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readSimpleProcessProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    public AggregateProcess readAggregateProcess(JsonReader reader) throws IOException
    {
        var bean = factory.newAggregateProcess();
        
        // start reading object and check type if not already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
        {
            reader.beginObject();
            checkObjectType(reader, "AggregateProcess");
        }
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readAggregateProcessProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    public PhysicalComponent readPhysicalComponent(JsonReader reader) throws IOException
    {
        var bean = factory.newPhysicalComponent();
        
        // start reading object and check type if not already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
        {
            reader.beginObject();
            checkObjectType(reader, "PhysicalComponent");
        }
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readPhysicalComponentProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    public PhysicalSystem readPhysicalSystem(JsonReader reader) throws IOException
    {
        var bean = factory.newPhysicalSystem();
        
        // start reading object and check type if not already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
        {
            reader.beginObject();
            checkObjectType(reader, "PhysicalSystem");
        }
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readPhysicalSystemProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    public Deployment readDeployment(JsonReader reader) throws IOException
    {
        var bean = factory.newDeployment();
        
        // start reading object and check type if not already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
        {
            reader.beginObject();
            checkObjectType(reader, "Deployment");
        }
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readDeploymentProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    public Mode readMode(JsonReader reader) throws IOException
    {
        var bean = factory.newMode();
        
        // start reading object and check type if not already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
        {
            reader.beginObject();
            checkObjectType(reader, "Mode");
        }
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readModeProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    public boolean readDescribedObjectProperties(JsonReader reader, DescribedObject bean, String name) throws IOException
    {
        if ("name".equals(name))
        {
            this.propName = reader.nextString();
        }
        
        else if ("id".equals(name))
        {
            bean.setId(reader.nextString());
        }
        
        else if ("uniqueId".equals(name))
        {
            bean.setUniqueIdentifier(reader.nextString());
        }
        
        else if ("lang".equals(name))
        {
            bean.setLang(reader.nextString());
        }
        
        else if ("label".equals(name))
        {
            bean.setName(reader.nextString());
        }
        
        else if ("description".equals(name))
        {
            bean.setDescription(reader.nextString());
        }
        
        else if ("keywords".equals(name))
        {
            var list = factory.newKeywordList();
            
            reader.beginArray();
            while (reader.hasNext())
            {
                var w = reader.nextString();
                list.addKeyword(w);
            }
            reader.endArray();
            
            if (list.getNumKeywords() > 0)
                bean.addKeywords(list);
        }
        
        else if ("identifiers".equals(name))
        {
            readIdentifierList(reader, bean);
        }
        
        else if ("classifiers".equals(name))
        {
            readClassifierList(reader, bean);
        }
        
        else if ("validTime".equals(name))
        {
            var te = geojsonBindings.readTimeExtent(reader);
            var tp = (TimePeriod)GMLUtils.timeExtentToTimePrimitive(te, true);
            bean.addValidTimeAsTimePeriod(tp);
        }
        
        else if ("securityConstraints".equals(name))
        {
            // TODO read security constraints
            reader.skipValue();
        }
        
        else if ("legalConstraints".equals(name))
        {
         // TODO read legal constraints
            reader.skipValue();
        }
        
        else if ("characteristics".equals(name))
        {
            readCharacteristicList(reader, bean);
        }
        
        else if ("capabilities".equals(name))
        {
            readCapabilitiesList(reader, bean);
        }
        
        else if ("contacts".equals(name))
        {
            readContactList(reader, bean);
        }
        
        else if ("documents".equals(name))
        {
            readDocumentList(reader, bean);
        }
        
        else
            return false;
        
        return true;
    }
    
    
    protected void readIdentifierList(JsonReader reader, DescribedObject bean) throws IOException
    {
        var list = factory.newIdentifierList();
        
        reader.beginArray();
        while (reader.hasNext())
            list.addIdentifier(readTerm(reader));
        reader.endArray();
        
        bean.getIdentificationList().add(list);
    }
    
    
    protected void readClassifierList(JsonReader reader, DescribedObject bean) throws IOException
    {
        var list = factory.newClassifierList();
        
        reader.beginArray();
        while (reader.hasNext())
            list.addClassifier(readTerm(reader));
        reader.endArray();
        
        bean.getClassificationList().add(list);
    }
    
    
    protected Term readTerm(JsonReader reader) throws IOException
    {
        var bean = factory.newTerm();
        
        reader.beginObject();
        while (reader.hasNext())
        {
            var name = reader.nextName();
            if ("definition".equals(name))
            {
                bean.setDefinition(reader.nextString());
            }
            else if ("label".equals(name))
            {
                bean.setLabel(reader.nextString());
            }
            else if ("codeSpace".equals(name))
            {
                bean.setCodeSpace(reader.nextString());
            }
            else if ("value".equals(name))
            {
                bean.setValue(reader.nextString());
            }
            else
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    protected void readContactList(JsonReader reader, DescribedObject bean) throws IOException
    {
        var list = factory.newContactList();
        
        reader.beginArray();
        while (reader.hasNext())
        {
            var prop = readContact(reader);
            list.getContactList().add(prop);
        }
        reader.endArray();
        
        bean.getContactsList().add(list);
    }
    
    
    protected OgcProperty<CIResponsibleParty> readContact(JsonReader reader) throws IOException
    {
        var prop = new OgcPropertyImpl<CIResponsibleParty>();
        var bean = isoFactory.newCIResponsibleParty();
        boolean isInline = false;
        
        reader.beginObject();
        while (reader.hasNext())
        {
            var name = reader.nextName();
            if ("role".equals(name))
            {
                var roleUri = reader.nextString();
                prop.setRole(roleUri);
            }
            else if ("individualName".equals(name))
            {
                bean.setIndividualName(reader.nextString());
                isInline = true;
            }
            else if ("organisationName".equals(name))
            {
                bean.setOrganisationName(reader.nextString());
                isInline = true;
            }
            else if ("positionName".equals(name))
            {
                bean.setPositionName(reader.nextString());
                isInline = true;
            }
            else if ("contactInfo".equals(name))
            {
                bean.setContactInfo(readContactInfo(reader));
                isInline = true;
            }
            else if ("link".equals(name))
            {
                readLink(reader, prop);
            }
            else
                reader.skipValue();
        }
        reader.endObject();
        
        if (isInline)
            prop.setValue(bean);
        
        return prop;
    }
    
    
    protected CIContact readContactInfo(JsonReader reader) throws IOException
    {
        var bean = isoFactory.newCIContact();
        
        reader.beginObject();
        while (reader.hasNext())
        {
            var name = reader.nextName();
            if ("phone".equals(name))
            {
                var phones = isoFactory.newCITelephone();
                
                reader.beginObject();
                while (reader.hasNext())
                {
                    name = reader.nextName();
                    if ("voice".equals(name))
                    {
                        phones.addVoice(reader.nextString());
                    }
                    else if ("facsimile".equals(name))
                    {
                        phones.addFacsimile(reader.nextString());
                    }
                    else
                        reader.skipValue();
                }
                reader.endObject();
                bean.setPhone(phones);
            }
            else if ("address".equals(name))
            {
                var address = isoFactory.newCIAddress();
                
                reader.beginObject();
                while (reader.hasNext())
                {
                    name = reader.nextName();
                    if ("deliveryPoint".equals(name))
                    {
                        address.addDeliveryPoint(reader.nextString());
                    }
                    else if ("city".equals(name))
                    {
                        address.setCity(reader.nextString());
                    }
                    else if ("administrativeArea".equals(name))
                    {
                        address.setAdministrativeArea(reader.nextString());
                    }
                    else if ("postalCode".equals(name))
                    {
                        address.setPostalCode(reader.nextString());
                    }
                    else if ("country".equals(name))
                    {
                        address.setCountry(reader.nextString());
                    }
                    else if ("electronicMailAddress".equals(name))
                    {
                        address.addElectronicMailAddress(reader.nextString());
                    }
                    else
                        reader.skipValue();
                }
                reader.endObject();
                bean.setAddress(address);
            }
            else if ("website".equals(name))
            {
                var uri = reader.nextString();
                var onlineResource = new CIOnlineResourceImpl();
                onlineResource.setLinkage(uri);
                bean.setOnlineResource(onlineResource);
            }
            else if ("hoursOfService".equals(name))
            {
                bean.setHoursOfService(reader.nextString());
            }
            else if ("contactInstructions".equals(name))
            {
                bean.setContactInstructions(reader.nextString());
            }
            else
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    protected void readDocumentList(JsonReader reader, DescribedObject bean) throws IOException
    {
        var list = factory.newDocumentList();
        
        reader.beginArray();
        while (reader.hasNext())
        {
            var prop = readDocument(reader);
            list.getDocumentList().add(prop);
        }
        reader.endArray();
        
        bean.getDocumentationList().add(list);
    }
    
    
    protected OgcProperty<CIOnlineResource> readDocument(JsonReader reader) throws IOException
    {
        var prop = new OgcPropertyImpl<CIOnlineResource>();
        var bean = isoFactory.newCIOnlineResource();
        
        reader.beginObject();
        while (reader.hasNext())
        {
            var name = reader.nextName();
            if ("role".equals(name))
            {
                var roleUri = reader.nextString();
                prop.setRole(roleUri);
            }
            else if ("name".equals(name))
            {
                bean.setName(reader.nextString());
            }
            else if ("description".equals(name))
            {
                bean.setDescription(reader.nextString());
            }
            else if ("link".equals(name))
            {
                var link = gmlFactory.newReference();
                reader.beginObject();
                while (reader.hasNext())
                {
                    name = reader.nextName();
                    if (!readLinkProperties(reader, link, name))
                        reader.skipValue();
                }
                reader.endObject();
                
                bean.setLinkage(link.getHref());
                bean.setApplicationProfile(link.getRemoteSchema());
            }
            else
                reader.skipValue();
        }
        reader.endObject();
        
        prop.setValue(bean);
        return prop;
    }
    
    
    protected void readCharacteristicList(JsonReader reader, DescribedObject bean) throws IOException
    {
        reader.beginArray();
        while (reader.hasNext())
        {
            var list = factory.newCharacteristicList();
            
            reader.beginObject();
            while (reader.hasNext())
            {
                var name = reader.nextName();
                if (readListMetadataProperties(reader, list, name))
                {
                    continue;
                }
                else if ("conditions".equals(name))
                {
                    readDataComponentList(reader, list::addCondition);
                }
                else if ("characteristics".equals(name))
                {
                    readDataComponentList(reader, list::addCharacteristic);
                }
                else
                    reader.skipValue();
            }
            reader.endObject();
            bean.getCharacteristicsList().add(list);
        }
        reader.endArray();
    }
    
    
    protected void readCapabilitiesList(JsonReader reader, DescribedObject bean) throws IOException
    {
        reader.beginArray();
        while (reader.hasNext())
        {
            var list = factory.newCapabilityList();
            
            reader.beginObject();
            while (reader.hasNext())
            {
                var name = reader.nextName();
                if (readListMetadataProperties(reader, list, name))
                {
                    continue;
                }
                else if ("conditions".equals(name))
                {
                    readDataComponentList(reader, list::addCondition);
                }
                else if ("capabilities".equals(name))
                {
                    readDataComponentList(reader, list::addCapability);
                }
                else
                    reader.skipValue();
            }
            reader.endObject();
            bean.getCapabilitiesList().add(list);
        }
        reader.endArray();
    }
    
    
    protected boolean readListMetadataProperties(JsonReader reader, AbstractMetadataList list, String name) throws IOException
    {
        if ("definition".equals(name))
        {
            list.setDefinition(reader.nextString());
        }
        else if ("label".equals(name))
        {
            list.setLabel(reader.nextString());
        }
        else if ("description".equals(name))
        {
            list.setDescription(reader.nextString());
        }
        else
            return false;
        
        return true;
    }
    
    
    protected void readDataComponentList(JsonReader reader, BiConsumer<String, DataComponent> addFunc) throws IOException
    {
        reader.beginArray();
        while (reader.hasNext())
        {
            var comp = sweBindings.readDataComponent(reader);
            try {
                addFunc.accept(comp.getName(), comp);
            }
            catch (Exception e) {
                throw new JsonParseException(e.getMessage() + " @ " + reader.getPath());
            }
        }
        reader.endArray();
    }
    
    
    public boolean readProcessProperties(JsonReader reader, AbstractProcess bean, String name) throws IOException
    {
        if ("definition".equals(name))
        {
            bean.setDefinition(reader.nextString());
        }
        
        else if ("inputs".equals(name))
        {
            readIOList(reader, bean.getInputList());
        }
        
        else if ("outputs".equals(name))
        {
            readIOList(reader, bean.getOutputList());
        }
        
        else if ("parameters".equals(name))
        {
            readIOList(reader, bean.getParameterList());
        }
        
        else if ("typeOf".equals(name))
        {
            var ref = gmlFactory.newReference();
            readLink(reader, ref);
            bean.setTypeOf(ref);
        }
        
        else if ("configuration".equals(name))
        {
            var config = readSettings(reader);
            bean.setConfiguration(config);
        }
        
        else if ("modes".equals(name))
        {
            var modeList = factory.newModeChoice();
            
            reader.beginArray();
            while (reader.hasNext())
            {
                var mode = readMode(reader);
                modeList.addMode(mode);
            }
            reader.endArray();
            
            if (modeList.getNumModes() > 0)
                bean.getModesList().add(modeList);
        }
        
        else if ("featuresOfInterest".equals(name))
        {
            var list = factory.newFeatureList();
            
            reader.beginArray();
            while (reader.hasNext())
            {
                OgcProperty<AbstractFeature> link = readLink(reader);
                list.getFeatureList().add(link);
            }
            reader.endArray();
        }
        
        else
            return readDescribedObjectProperties(reader, bean, name);
        
        return true;
    }
    
    
    protected void readIOList(JsonReader reader, IOPropertyList ioList) throws IOException
    {
        reader.beginArray();
        while (reader.hasNext())
            readIOObject(reader, ioList);
        reader.endArray();
    }
    
    
    protected void readIOObject(JsonReader reader, IOPropertyList ioList) throws IOException
    {
        String type = null;
        String name = null;
        
        reader.beginObject();
        while (reader.hasNext())
        {
            String propName = reader.nextName();
            
            if ("type".equals(propName))
            {
                type = reader.nextString();
            }
            else if ("name".equals(propName))
            {
                name = reader.nextString();
            }
            else
            {
                if (type == null)
                    throw new JsonParseException("JSON object must contain a 'type' property before the other members @ " + reader.getPath());
                
                if (name == null)
                    throw new JsonParseException("JSON object must contain a 'name' property before the other members @ " + reader.getPath());
            }
            
            if (type != null && name != null)
                break;
        }
        
        var sweObj = readIOComponentByType(reader, type);
        var prop = new OgcPropertyImpl<AbstractSWEIdentifiable>();
        prop.setName(name);
        prop.setValue(sweObj);
        ioList.add(prop);
    }
    
    
    protected AbstractSWEIdentifiable readIOComponentByType(JsonReader reader, String type) throws IOException
    {
        // first try to read SWE data component
        var comp = sweBindings.readDataComponentByType(reader, type);
        if (comp != null)
            return comp;

        else if ("DataStream".equals(type))
        {
            return sweBindings.readDataStream(reader);
        }
        
        else if ("ObservableProperty".equals(type))
        {
            var obsProp = factory.newObservableProperty();
            while (reader.hasNext())
            {
                String propName = reader.nextName();
                
                if ("definition".equals(propName))
                {
                    obsProp.setDefinition(reader.nextString());
                }
                else if (!sweBindings.readAbstractSWEIdentifiableProperties(reader, obsProp, propName))
                    reader.skipValue();
            }
            reader.endObject();
            return obsProp;
        }
        
        else if ("DataInterface".equals(type))
        {
            while (reader.hasNext())
            {
                reader.nextName();
                reader.skipValue();
            }
            reader.endObject();
            return null;
        }
        
        else
            throw new JsonParseException("Invalid component type: " + type + " @ " + reader.getPath());
    }
    
    
    protected Settings readSettings(JsonReader reader) throws IOException
    {
        var bean = factory.newSettings();
        
        reader.beginObject();
        while (reader.hasNext())
        {
            String name = reader.nextName();
            
            if ("setValues".equals(name))
            {
                var list = bean.getSetValueList();
                
                reader.beginArray();
                while (reader.hasNext())
                {
                    var setting = factory.newValueSetting();
                    reader.beginObject();
                    while (reader.hasNext())
                    {
                        name = reader.nextName();
                        if ("ref".equals(name))
                            setting.setRef(reader.nextString());
                        else if ("value".equals(name))
                            setting.setValue(reader.nextString());
                        else
                            reader.skipValue();
                    }
                    reader.endObject();
                    list.add(setting);
                }
                reader.endArray();
            }
            else if ("setArrayValues".equals(name))
            {
                var list = bean.getSetArrayValuesList();
                
                reader.beginArray();
                while (reader.hasNext())
                {
                    var setting = factory.newArraySetting();
                    reader.beginObject();
                    while (reader.hasNext())
                    {
                        name = reader.nextName();
                        if ("ref".equals(name))
                            setting.setRef(reader.nextString());
                        else if ("value".equals(name))
                            setting.setValue(sweBindings.readEncodedValuesProperty(reader, null, null));
                        else
                            reader.skipValue();
                    }
                    reader.endObject();
                    list.add(setting);
                }
                reader.endArray();
            }
            else if ("setModes".equals(name))
            {
                var list = bean.getSetModeList();
                
                reader.beginArray();
                while (reader.hasNext())
                {
                    var setting = factory.newModeSetting();
                    reader.beginObject();
                    while (reader.hasNext())
                    {
                        name = reader.nextName();
                        if ("ref".equals(name))
                            setting.setRef(reader.nextString());
                        else if ("value".equals(name))
                            setting.setValue(reader.nextString());
                        else
                            reader.skipValue();
                    }
                    reader.endObject();
                    list.add(setting);
                }
                reader.endArray();
            }
            else if ("setConstraints".equals(name))
            {
                var list = bean.getSetConstraintList();
                
                reader.beginArray();
                while (reader.hasNext())
                {
                    var setting = readConstraintSetting(reader);
                    list.add(setting);
                }
                reader.endArray();
            }
            else if ("setStatus".equals(name))
            {
                var list = bean.getSetStatusList();
                
                reader.beginArray();
                while (reader.hasNext())
                {
                    var setting = factory.newStatusSetting();
                    reader.beginObject();
                    while (reader.hasNext())
                    {
                        name = reader.nextName();
                        if ("ref".equals(name))
                            setting.setRef(reader.nextString());
                        else if ("value".equals(name))
                        {
                            var status = reader.nextString();
                            var statusCode = Status.fromString(status);
                            setting.setValue(statusCode);
                        }
                        else
                            reader.skipValue();
                    }
                    reader.endObject();
                    list.add(setting);
                }
                reader.endArray();
            }
        }
        reader.endObject();
        
        return bean;
    }
    
    
    protected ConstraintSetting readConstraintSetting(JsonReader reader) throws IOException
    {
        var setting = factory.newConstraintSetting();
        
        reader = beginObjectWithType(reader);
        var type = reader.nextString();
        
        if ("AllowedTokens".equals(type))
        {
            var bean = sweBindings.getFactory().newAllowedTokens();
            while (reader.hasNext())
            {
                String name = reader.nextName();
                if (!sweBindings.readAllowedTokensProperties(reader, bean, name))
                    readCommonSettingProperties(reader, setting, name);
            }
            setting.setValue(bean);
        }
        else if ("AllowedValues".equals(type))
        {
            var bean = sweBindings.getFactory().newAllowedValues();
            while (reader.hasNext())
            {
                String name = reader.nextName();
                if (!sweBindings.readAllowedValuesProperties(reader, bean, name))
                    readCommonSettingProperties(reader, setting, name);
            }
            setting.setValue(bean);
        }
        else if ("AllowedTimes".equals(type))
        {
            var bean = sweBindings.getFactory().newAllowedTimes();
            while (reader.hasNext())
            {
                String name = reader.nextName();
                if (!sweBindings.readAllowedTimesProperties(reader, bean, true, name))
                    readCommonSettingProperties(reader, setting, name);
            }
            setting.setValue(bean);
        }
        else
            throw new JsonParseException("Unknown object type: " + type + " @ " + reader.getPath());
        
        reader.endObject();
        return setting;
    }
    
    
    protected boolean readCommonSettingProperties(JsonReader reader, ConfigSetting<?> setting, String name) throws IOException
    {
        if ("ref".equals(name))
            setting.setRef(reader.nextString());
        else
        {
            reader.skipValue();
            return false;
        }
        
        return true;
    }
    
    
    public boolean readPhysicalProcessProperties(JsonReader reader, AbstractPhysicalProcess bean, String name) throws IOException
    {
        if ("attachedTo".equals(name))
        {
            var ref = gmlFactory.newReference();
            readLink(reader, ref);
            bean.setAttachedTo(ref);
        }
        
        else if ("localReferenceFrames".equals(name))
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                var refFrame = readSpatialFrame(reader);
                bean.addLocalReferenceFrame(refFrame);
            }
            reader.endArray();
        }
        
        else if ("position".equals(name))
        {
            readPosition(reader, bean);
        }
        
        else if ("localTimeFrames".equals(name))
        {
            reader.skipValue();
        }
        
        else if ("timePosition".equals(name))
        {
            reader.skipValue();
        }
        
        else
            return readProcessProperties(reader, bean, name);
        
        return true;
    }
    
    
    protected SpatialFrame readSpatialFrame(JsonReader reader) throws IOException
    {
        var bean = factory.newSpatialFrame();
        
        reader.beginObject();
        while (reader.hasNext())
        {
            String name = reader.nextName();
            
            if ("origin".equals(name))
            {
                bean.setOrigin(reader.nextString());
            }
            else if ("axes".equals(name))
            {
                reader.beginArray();
                while (reader.hasNext())
                {
                    reader.beginObject();
                    String axisName = null, axisDescription = null;
                    while (reader.hasNext())
                    {
                        var axisProp = reader.nextName();
                        if ("name".equals(axisProp))
                            axisName = reader.nextString();
                        else if ("description".equals(axisProp))
                            axisDescription = reader.nextString();
                        else
                            reader.skipValue();
                    }
                    bean.addAxis(axisName, axisDescription);
                    reader.endObject();
                }
                reader.endArray();
            }
            else if (!sweBindings.readAbstractSWEIdentifiableProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    protected void readPosition(JsonReader reader, AbstractPhysicalProcess bean) throws IOException
    {
        reader = beginObjectWithType(reader);
        var type = reader.nextString();
        
        if ("Pose".equals(type))
        {
            var pose = geoposeBindings.readPose(reader);
            bean.addPositionAsPose(pose);
        }
        else if ("Point".equals(type))
        {
            var p = (Point)geojsonBindings.readGeometry(reader, type);
            bean.addPositionAsPoint(p);
        }
        else
            throw new JsonParseException("Unsupported position type " + type + " @ " + reader.getPath());
    }
    
    
    public boolean readSimpleProcessProperties(JsonReader reader, SimpleProcess bean, String name) throws IOException
    {
        if ("method".equals(name))
        {
            // only by-ref method supported in JSON for now
            if (reader.peek() == JsonToken.STRING)
            {
                var href = reader.nextString();
                bean.getMethodProperty().setHref(href);
            }
            else
                reader.skipValue();
        }
        
        else
            return readProcessProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readAggregateProcessProperties(JsonReader reader, AggregateProcess bean, String name) throws IOException
    {
        if ("components".equals(name))
            readComponents(reader, bean);
        else if ("connections".equals(name))
            readConnections(reader, bean);
        else
            return readProcessProperties(reader, bean, name);
        
        return true;
    }
    
    
    protected void readComponents(JsonReader reader, AggregateProcess bean) throws IOException
    {
        reader.beginArray();
        while (reader.hasNext())
        {
            var prop = readComponent(reader, true);
            
            try {
                bean.getComponentList().add(prop);
            }
            catch (IllegalArgumentException e) {
                throw new JsonParseException(e.getMessage() + " @ " + reader.getPath());
            }
        }
        reader.endArray();
    }
    
    
    protected OgcProperty<AbstractProcess> readComponent(JsonReader reader, boolean nameRequired) throws IOException
    {
        OgcProperty<AbstractProcess> prop = null;
        reader = beginObjectWithType(reader);
        var type = reader.nextString();
        
        if ("Link".equals(type))
        {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            var link = (OgcProperty<AbstractProcess>)(OgcProperty)readLink(reader);
            prop = link;
        }
        else
        {
            prop = new OgcPropertyImpl<AbstractProcess>();
            var comp = readAbstractProcess(reader, type);
            prop.setValue(comp);
            prop.setName(propName);
            propName = null; // reset
        }
        
        return prop;
    }
    
    
    protected void readConnections(JsonReader reader, AggregateProcess bean) throws IOException
    {
        reader.beginArray();
        while (reader.hasNext())
        {
            var conn = factory.newLink();
            
            reader.beginObject();
            while (reader.hasNext())
            {
                var name = reader.nextName();
                if ("source".equals(name))
                    conn.setSource(reader.nextString());
                else if ("destination".equals(name))
                    conn.setDestination(reader.nextString());
                else
                    reader.skipValue();
            }
            reader.endObject();
            
            bean.getConnectionList().add(conn);
        }
        reader.endArray();
    }
    
    
    public boolean readPhysicalSystemProperties(JsonReader reader, PhysicalSystem bean, String name) throws IOException
    {
        if ("components".equals(name))
            readComponents(reader, bean);
        else if ("connections".equals(name))
            readConnections(reader, bean);
        else
            return readPhysicalProcessProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readPhysicalComponentProperties(JsonReader reader, PhysicalComponent bean, String name) throws IOException
    {
        if ("method".equals(name))
        {
            // only by-ref method supported in JSON for now
            if (reader.peek() == JsonToken.STRING)
            {
                var href = reader.nextString();
                bean.getMethodProperty().setHref(href);
            }
            else
                reader.skipValue();
        }
        
        else
            return readPhysicalProcessProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readModeProperties(JsonReader reader, Mode bean, String name) throws IOException
    {
        if ("configuration".equals(name))
        {
            var config = readSettings(reader);
            bean.setConfiguration(config);
        }
        
        else
            return readDescribedObjectProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readDeploymentProperties(JsonReader reader, Deployment bean, String name) throws IOException
    {
        if ("definition".equals(name))
        {
            bean.setDefinition(reader.nextString());
        }
        
        else if ("location".equals(name))
        {
            var geom = geojsonBindings.readGeometry(reader);
            bean.setGeometry(geom);
        }
        
        else if ("members".equals(name))
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                var prop = readComponent(reader, false);
                bean.getComponentList().add(prop);
            }
            reader.endArray();
        }
        
        else
            return readDescribedObjectProperties(reader, bean, name);
        
        return true;
    }
    
    
    protected <T> OgcProperty<T> readLink(JsonReader reader) throws IOException
    {
        @SuppressWarnings("unchecked")
        var bean = (OgcProperty<T>)new OgcPropertyImpl<>();
        readLink(reader, bean);
        return bean;
    }
    
    
    protected void readLink(JsonReader reader, OgcProperty<?> bean) throws IOException
    {
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            var name = reader.nextName();
            
            if (!readLinkProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
    }
    
    
    protected boolean readLinkProperties(JsonReader reader, OgcProperty<?> bean, String name) throws IOException
    {
        if ("href".equals(name))
        {
            bean.setHref(reader.nextString());
        }
        else if ("name".equals(name))
        {
            bean.setName(reader.nextString());
        }
        else if ("title".equals(name))
        {
            bean.setTitle(reader.nextString());
        }
        else if ("type".equals(name))
        {
            // store media type as remote schema if external reference
            var type = reader.nextString();
            if (bean instanceof Reference)
                ((Reference)bean).setRemoteSchema(type);
        }
        else if ("uid".equals(name))
        {
            // store uid on role property
            bean.setRole(reader.nextString());
        }
        else
            return false;
        
        return true;
    }
    
    
    protected JsonReader beginObjectWithType(JsonReader reader) throws IOException
    {
        return sweBindings.beginObjectWithType(reader);
    }
    
    
    protected void checkObjectType(JsonReader reader, String expectedType) throws IOException
    {
        sweBindings.checkObjectType(reader, expectedType);
    }
    
    
    protected void writeTypeAndName(JsonWriter writer, String type, String name) throws IOException
    {
        sweBindings.writeTypeAndName(writer, type, name);
    }
    
    
    
    /********************
     * Write methods
     *******************/
    
    public void writeDescribedObject(JsonWriter writer, DescribedObject bean) throws IOException
    {
        writeDescribedObject(writer, bean, null);
    }
    
    
    public void writeDescribedObject(JsonWriter writer, DescribedObject bean, String name) throws IOException
    {
        writer.beginObject();
        
        if (bean instanceof PhysicalSystem)
        {
            writeTypeAndName(writer, "PhysicalSystem", name);
            writePhysicalSystemProperties(writer, (PhysicalSystem)bean);
        }
        else if (bean instanceof PhysicalComponent)
        {
            writeTypeAndName(writer, "PhysicalComponent", name);
            writePhysicalComponentProperties(writer, (PhysicalComponent)bean);
        }
        else if (bean instanceof SimpleProcess)
        {
            writeTypeAndName(writer, "SimpleProcess", name);
            writeSimpleProcessProperties(writer, (SimpleProcess)bean);
        }
        else if (bean instanceof AggregateProcess)
        {
            writeTypeAndName(writer, "AggregateProcess", name);
            writeAggregateProcessProperties(writer, (AggregateProcess)bean);
        }
        else if (bean instanceof Mode)
        {
            writeTypeAndName(writer, "Mode", name);
            writeModeProperties(writer, (Mode)bean);
        }
        else if (bean instanceof Deployment)
        {
            writeTypeAndName(writer, "Deployment", name);
            writeDeploymentProperties(writer, (Deployment)bean);
        }
        
        writer.endObject();
    }
    
    
    public void writeSimpleProcessProperties(JsonWriter writer, SimpleProcess bean) throws IOException
    {
        writeDescribedObjectProperties(writer, bean);
        writeProcessProperties(writer, bean);
        
        // on by-ref method supported in JSON for now
        if (bean.getMethodProperty().hasHref())
            writer.name("method").value(bean.getMethodProperty().getHref());
    }
    
    
    public void writeAggregateProcessProperties(JsonWriter writer, AggregateProcess bean) throws IOException
    {
        writeDescribedObjectProperties(writer, bean);
        writeProcessProperties(writer, bean);
        writeComponents(writer, bean.getComponentList());
        writeConnections(writer, bean.getConnectionList());
    }
    
    
    public void writePhysicalSystemProperties(JsonWriter writer, PhysicalSystem bean) throws IOException
    {
        writeDescribedObjectProperties(writer, bean);
        writePhysicalProcessProperties(writer, bean);
        writeProcessProperties(writer, bean);
        writeComponents(writer, bean.getComponentList());
        writeConnections(writer, bean.getConnectionList());
    }
    
    
    public void writePhysicalComponentProperties(JsonWriter writer, PhysicalComponent bean) throws IOException
    {
        writeDescribedObjectProperties(writer, bean);
        writePhysicalProcessProperties(writer, bean);
        writeProcessProperties(writer, bean);
        
        // on by-ref method supported in JSON for now
        if (bean.getMethodProperty().hasHref())
            writer.name("method").value(bean.getMethodProperty().getHref());
    }
    
    
    public void writeModeProperties(JsonWriter writer, Mode bean) throws IOException
    {
        writeDescribedObjectProperties(writer, bean);
        
        if (bean.getConfiguration() != null)
        {
            writer.name("configuration");
            writeSettings(writer, bean.getConfiguration());
        }
    }
    
    
    public void writeDeploymentProperties(JsonWriter writer, Deployment bean) throws IOException
    {
        writeDescribedObjectProperties(writer, bean);
        
        if (bean.getGeometry() != null)
        {
            writer.name("location");
            geojsonBindings.writeGeometry(writer, bean.getGeometry());
        }
        
        if (bean.getNumComponents() > 0)
        {
            writer.name("members").beginArray();
            for (var item: bean.getComponentList().getProperties())
            {
                if (item.hasValue())
                    writeDescribedObject(writer, item.getValue(), item.getName());
                else if (item.hasHref())
                    writeLink(writer, item, "uid", null, true);
            }
            writer.endArray();
        }
    }
    
    
    protected void writeDescribedObjectProperties(JsonWriter writer, DescribedObject bean) throws IOException
    {
        if (!Strings.isNullOrEmpty(bean.getId()))
            writer.name("id").value(bean.getId());
        
        if (bean.getIdentifier() != null)
            writer.name("uniqueId").value(bean.getIdentifier().getValue());
        
        if (bean instanceof AbstractProcess && ((AbstractProcess)bean).isSetDefinition())
            writer.name("definition").value(((AbstractProcess)bean).getDefinition());
        
        if (bean.isSetLang())
            writer.name("lang").value(bean.getLang());
        
        if (bean.getName() != null)
            writer.name("label").value(bean.getName());
        
        if (bean.getDescription() != null)
            writer.name("description").value(bean.getDescription());
        
        if (bean instanceof AbstractProcess)
            writeTypeOf(writer, (AbstractProcess)bean);
        
        writeKeywords(writer, bean.getKeywordsList());
        writeIdentifiers(writer, bean.getIdentificationList());
        writeClassifiers(writer, bean.getClassificationList());
        
        writeValidTime(writer, bean.getValidTime());
        
        writeSecurityConstraints(writer, bean.getSecurityConstraintsList());
        writeLegalConstraints(writer, bean.getLegalConstraintsList());
        
        writeCharacteristics(writer, bean.getCharacteristicsList());
        writeCapabilities(writer, bean.getCapabilitiesList());
        
        writeContacts(writer, bean.getContactsList());
        writeDocumentation(writer, bean.getDocumentationList());
    }
    
    
    protected void writeKeywords(JsonWriter writer, OgcPropertyList<KeywordList> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("keywords").beginArray();
        for (var list: bean)
        {
            for (var kw: list.getKeywordList())
                writer.value(kw);
        }
        writer.endArray();
    }
    
    
    protected void writeIdentifiers(JsonWriter writer, OgcPropertyList<IdentifierList> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("identifiers").beginArray();
        for (var list: bean)
        {
            for (var term: list.getIdentifierList())
                writeTerm(writer, term);
        }
        writer.endArray();
    }
    
    
    protected void writeClassifiers(JsonWriter writer, OgcPropertyList<ClassifierList> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("classifiers").beginArray();
        for (var list: bean)
        {
            for (var term: list.getClassifierList())
                writeTerm(writer, term);
        }
        writer.endArray();
    }
    
    
    protected void writeTerm(JsonWriter writer, Term bean) throws IOException
    {
        writer.beginObject();
        
        if (bean.isSetDefinition())
            writer.name("definition").value(bean.getDefinition());
        
        if (bean.isSetCodeSpace())
            writer.name("codeSpace").value(bean.getCodeSpace());
        
        if (bean.getLabel() != null)
            writer.name("label").value(bean.getLabel());
        
        if (bean.getValue() != null)
            writer.name("value").value(bean.getValue());
        
        writer.endObject();
    }
    
    
    protected void writeSecurityConstraints(JsonWriter writer, List<Object> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("securityConstraints").beginArray();
        for (var list: bean)
        {
            writer.beginObject();
            // TODO write security constraints
            writer.endObject();
        }
        writer.endArray();
    }
    
    
    protected void writeLegalConstraints(JsonWriter writer, OgcPropertyList<MDLegalConstraints> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("legalConstraints").beginArray();
        for (var list: bean)
        {
            writer.beginObject();
         // TODO write legal constraints
            writer.endObject();
        }
        writer.endArray();
    }
    
    
    protected void writeCharacteristics(JsonWriter writer, OgcPropertyList<CharacteristicList> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("characteristics").beginArray();
        for (var list: bean)
        {
            writer.beginObject();
            
            if (list.isSetDefinition())
                writer.name("definition").value(list.getDefinition());
            
            sweBindings.writeAbstractSWEIdentifiableProperties(writer, list);
            
            writer.name("characteristics").beginArray();
            for (var item: list.getCharacteristicList().getProperties())
            {
                sweBindings.writeDataComponent(writer, item.getValue(), true, item.getName());
            }
            writer.endArray();
            writer.endObject();
        }
        writer.endArray();
    }
    
    
    protected void writeCapabilities(JsonWriter writer, OgcPropertyList<CapabilityList> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("capabilities").beginArray();
        for (var list: bean)
        {
            writer.beginObject();
            
            if (list.isSetDefinition())
                writer.name("definition").value(list.getDefinition());
            
            if (list.isSetRelatedProperty())
                writer.name("forProperty").value(list.getRelatedProperty());
            
            sweBindings.writeAbstractSWEIdentifiableProperties(writer, list);
            
            if (list.getNumConditions() > 0)
            {
                writer.name("conditions").beginArray();
                for (var item: list.getConditionList().getProperties())
                {
                    sweBindings.writeDataComponent(writer, item.getValue(), true, item.getName());
                }
                writer.endArray();
            }
            
            writer.name("capabilities").beginArray();
            for (var item: list.getCapabilityList().getProperties())
            {
                sweBindings.writeDataComponent(writer, item.getValue(), true, item.getName());
            }
            writer.endArray();
            
            writer.endObject();
        }
        writer.endArray();
    }
    
    
    protected void writeContacts(JsonWriter writer, OgcPropertyList<ContactList> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("contacts").beginArray();
        for (var list: bean)
        {
            for (var prop: list.getContactList().getProperties())
                writeContact(writer, prop);
        }
        writer.endArray();
    }
    
    
    protected void writeContact(JsonWriter writer, OgcProperty<CIResponsibleParty> prop) throws IOException
    {
        writer.beginObject();
        
        if (prop.getRole() != null)
            writer.name("role").value(prop.getRole());
        
        if (prop.hasHref())
        {
            writer.name("link");
            writeLink(writer, prop, null, null, false);
        }
        else if (prop.hasValue())
        {
            var bean = prop.getValue();
            
            if (bean.isSetIndividualName())
                writer.name("individualName").value(bean.getIndividualName());
            
            if (bean.isSetOrganisationName())
                writer.name("organisationName").value(bean.getOrganisationName());
            
            if (bean.isSetContactInfo())
            {
                writer.name("contactInfo").beginObject();
                
                var ci = bean.getContactInfo();
                
                if (ci.isSetOnlineResource())
                    writer.name("website").value(ci.getOnlineResource().getLinkage());
                
                if (ci.isSetPhone())
                {
                    writer.name("phone").beginObject();
                    var ph = ci.getPhone();
                    
                    if (ph.getNumVoices() > 0)
                        writer.name("voice").value(ph.getVoiceList().get(0));
                    
                    if (ph.getNumFacsimiles() > 0)
                        writer.name("facsimile").value(ph.getFacsimileList().get(0));
                    
                    writer.endObject();
                }
                
                if (ci.isSetAddress())
                {
                    writer.name("address").beginObject();
                    var ad = ci.getAddress();
                    
                    if (ad.getNumDeliveryPoints() > 0)
                        writer.name("deliveryPoint").value(ad.getDeliveryPointList().get(0));
                    
                    if (ad.isSetCity())
                        writer.name("city").value(ad.getCity());
                    
                    if (ad.isSetPostalCode())
                        writer.name("postalCode").value(ad.getPostalCode());
                    
                    if (ad.isSetAdministrativeArea())
                        writer.name("administrativeArea").value(ad.getAdministrativeArea());
                    
                    if (ad.isSetCountry())
                        writer.name("country").value(ad.getCountry());
                    
                    if (ad.getNumElectronicMailAddress() > 0)
                        writer.name("electronicMailAddress").value(ad.getElectronicMailAddressList().get(0));
                    
                    writer.endObject();
                }
                
                writer.endObject();
            }
        }
        
        writer.endObject();
    }
    
    
    protected void writeValidTime(JsonWriter writer, TimeExtent bean) throws IOException
    {
        if (bean == null)
            return;
        
        writer.name("validTime");
        geojsonBindings.writeTimeExtent(writer, bean);
    }
    
    
    protected void writeDocumentation(JsonWriter writer, OgcPropertyList<DocumentList> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("documents").beginArray();
        for (var list: bean)
        {
            for (var item: list.getDocumentList().getProperties())
                writeOnlineResource(writer, item.getValue(), item.getRole());
        }
        writer.endArray();
    }
    
    
    protected void writeOnlineResource(JsonWriter writer, CIOnlineResource bean, String role) throws IOException
    {
        writer.beginObject();
        
        if (role != null)
            writer.name("role").value(role);
        
        if (bean.isSetName())
            writer.name("name").value(bean.getName());
        
        if (bean.isSetDescription())
            writer.name("description").value(bean.getDescription());
        
        if (bean.getLinkage() != null)
        {
            writer.name("link").beginObject();
            writer.name("href").value(bean.getLinkage());
            if (bean.isSetApplicationProfile())
                writer.name("type").value(bean.getApplicationProfile());
            writer.endObject();
        }
        
        writer.endObject();
    }
    
    
    protected void writeLink(JsonWriter writer, OgcProperty<?> bean, String rolePropName, String mediaType, boolean includeType) throws IOException
    {
        writer.beginObject();
        
        if (includeType)
            writer.name("type").value("Link");
        
        if (bean.getName() != null)
            writer.name("name").value(bean.getName());
        
        if (bean.hasHref())
            writer.name("href").value(bean.getHref());
        
        if (bean.getTitle() != null)
            writer.name("title").value(bean.getTitle());
        
        if (bean.getRole() != null && rolePropName != null)
            writer.name(rolePropName).value(bean.getRole());
        
        if (mediaType != null)
            writer.name("type").value(mediaType);
        
        writer.endObject();
    }
    
    
    protected void writeEvents(JsonWriter writer, DescribedObject bean) throws IOException
    {
        
    }
    
    
    protected void writeTypeOf(JsonWriter writer, AbstractProcess bean) throws IOException
    {
        if (bean.isSetTypeOf())
        {
            writer.name("typeOf");
            writeLink(writer, bean.getTypeOf(), "uid", SML_JSON_MEDIA_TYPE, false);
        }
    }
    
    
    protected void writeProcessProperties(JsonWriter writer, AbstractProcess bean) throws IOException
    {
        // typeOf is written in writeDescribedObjectProperties()
        // so it appears at the top of the document
        
        if (bean.isSetConfiguration())
        {
            writer.name("configuration");
            writeSettings(writer, bean.getConfiguration());
        }

        writeFeaturesOfInterest(writer, bean.getFeaturesOfInterest());
        
        writeInputs(writer, bean.getInputList());
        writeOutputs(writer, bean.getOutputList());
        writeParameters(writer, bean.getParameterList());
        
        writeModes(writer, bean.getModesList());
    }
    
    
    protected void writeSettings(JsonWriter writer, Settings bean) throws IOException
    {
        writer.beginObject();
        
        if (bean.getNumSetValues() > 0)
        {
            writer.name("setValues").beginArray();
            for (var setting: bean.getSetValueList())
            {
                writer.beginObject();
                writer.name("ref").value(setting.getRef());
                writer.name("value").value(setting.getValue());
                writer.endObject();
            }
            writer.endArray();
        }
        
        if (bean.getNumSetArrayValues() > 0)
        {
            writer.name("setArrayValues").beginArray();
            for (var setting: bean.getSetArrayValuesList())
            {
                writer.beginObject();
                writer.name("ref").value(setting.getRef());
                writer.name("value");
                sweBindings.writeEncodedValuesProperty(writer, null, null, setting.getValue());
                writer.endObject();
            }
            writer.endArray();
        }
        
        if (bean.getNumSetModes() > 0)
        {
            writer.name("setModes").beginArray();
            for (var setting: bean.getSetModeList())
            {
                writer.beginObject();
                writer.name("ref").value(setting.getRef());
                writer.name("value").value(setting.getValue());
                writer.endObject();
            }
            writer.endArray();
        }
        
        if (bean.getNumSetConstraints() > 0)
        {
            writer.name("setConstraints").beginArray();
            for (var setting: bean.getSetConstraintList())
            {
                writer.beginObject();
                var c = setting.getValue();
                if (c instanceof AllowedValues)
                {
                    writer.name("type").value("AllowedValues");
                    writer.name("ref").value(setting.getRef());
                    sweBindings.writeAllowedValuesProperties(writer, (AllowedValues)setting.getValue(), false);
                }
                else if (c instanceof AllowedTokens)
                {
                    writer.name("type").value("AllowedTokens");
                    writer.name("ref").value(setting.getRef());
                    sweBindings.writeAllowedTokensProperties(writer, (AllowedTokens)setting.getValue());
                }
                else if (c instanceof AllowedTimes)
                {
                    writer.name("type").value("AllowedTimes");
                    writer.name("ref").value(setting.getRef());
                    sweBindings.writeAllowedTimesProperties(writer, (AllowedTimes)setting.getValue());
                }
                writer.endObject();
            }
            writer.endArray();
        }
        
        if (bean.getNumSetStatus() > 0)
        {
            writer.name("setStatus").beginArray();
            for (var setting: bean.getSetStatusList())
            {
                writer.beginObject();
                writer.name("ref").value(setting.getRef());
                writer.name("value").value(setting.getValue() != null ?
                    setting.getValue().toString().toLowerCase() : null);
                writer.endObject();
            }
            writer.endArray();
        }
        
        writer.endObject();
    }
    
    
    protected void writeFeaturesOfInterest(JsonWriter writer, FeatureList bean) throws IOException
    {
        if (bean.getNumFeatures() == 0)
            return;
        
        writer.name("featuresOfInterest").beginArray();
        for (var item: bean.getFeatureList().getProperties())
        {
            if (item.hasHref())
                writeLink(writer, item, null, null, false);
        }
        writer.endArray();
    }
    
    
    protected void writeInputs(JsonWriter writer, IOPropertyList bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("inputs").beginArray();
        for (var item: bean.getProperties())
        {
            var val = item.getValue();
            
            if (val instanceof DataComponent)
                sweBindings.writeDataComponent(writer, (DataComponent)val, false, item.getName());
            else if (val instanceof ObservableProperty)
                writeObservableProperty(writer, (ObservableProperty)val, item.getName());
            
            // TODO write observable property, datastreams and interfaces
        }
        writer.endArray();
    }
    
    
    protected void writeOutputs(JsonWriter writer, IOPropertyList bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("outputs").beginArray();
        for (var item: bean.getProperties())
        {
            var val = item.getValue();
            
            if (val instanceof DataComponent)
                sweBindings.writeDataComponent(writer, (DataComponent)val, false, item.getName());
            else if (val instanceof ObservableProperty)
                writeObservableProperty(writer, (ObservableProperty)val, item.getName());
            
            // TODO write observable property, datastreams and interfaces
        }
        writer.endArray();
    }
    
    
    protected void writeParameters(JsonWriter writer, IOPropertyList bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("parameters").beginArray();
        for (var item: bean.getProperties())
        {
            var val = item.getValue();
            
            if (val instanceof DataComponent)
                sweBindings.writeDataComponent(writer, (DataComponent)val, true, item.getName());
            else if (val instanceof ObservableProperty)
                writeObservableProperty(writer, (ObservableProperty)val, item.getName());
            
            // TODO write observable property, datastreams and interfaces
        }
        writer.endArray();
    }
    
    
    protected void writeObservableProperty(JsonWriter writer, ObservableProperty bean, String name) throws IOException
    {
        writer.beginObject();
        
        if (name != null)
            writer.name("name").value(name);
        
        writer.name("type").value("ObservableProperty");
        
        // definition
        if (bean.getDefinition() != null)
            writer.name("definition").value(bean.getDefinition());
        
        sweBindings.writeAbstractSWEIdentifiableProperties(writer, bean);
        
        writer.endObject();
    }
    
    
    protected void writeModes(JsonWriter writer, List<AbstractModes> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("modes").beginArray();
        for (var item: bean)
        {
            var modeChoice = (ModeChoice)item;
            for (var mode: modeChoice.getModeList())
                writeDescribedObject(writer, mode);
        }
        writer.endArray();
    }
    
    
    protected void writePhysicalProcessProperties(JsonWriter writer, AbstractPhysicalProcess bean) throws IOException
    {
        writeAttachedTo(writer, bean);
        writeLocalFrames(writer, bean.getLocalReferenceFrameList());
        writePosition(writer, bean.getPositionList());
    }
    
    
    protected void writeAttachedTo(JsonWriter writer, AbstractPhysicalProcess bean) throws IOException
    {
        if (bean.isSetAttachedTo())
        {
            writer.name("attachedTo");
            writeLink(writer, bean.getAttachedTo(), "uid", SML_JSON_MEDIA_TYPE, false);
        }
    }
    
    
    protected void writeLocalFrames(JsonWriter writer, List<SpatialFrame> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("localReferenceFrames").beginArray();
        
        for (var selfFrame: bean)
        {
            writer.beginObject();
            
            sweBindings.writeAbstractSWEIdentifiableProperties(writer, selfFrame);
            writer.name("origin").value(selfFrame.getOrigin());
            
            writer.name("axes").beginArray();
            for (var axis: selfFrame.getAxisList().getProperties())
            {
                writer.beginObject();
                writer.name("name").value(axis.getName());
                writer.name("description").value(axis.getValue());
                writer.endObject();
            }
            writer.endArray();
            
            writer.endObject();
        }
        
        writer.endArray();
    }
    
    
    protected void writePosition(JsonWriter writer, OgcPropertyList<Serializable> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("position");
        var selfPos = bean.get(0);
        
        if (selfPos instanceof Point)
        {
            geojsonBindings.writePoint(writer, (Point)selfPos);
        }
        else if (selfPos instanceof Pose)
        {
            geoposeBindings.writePose(writer, (Pose)selfPos, "Pose");
        }
    }
    
    
    protected void writeComponents(JsonWriter writer, OgcPropertyList<AbstractProcess> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("components").beginArray();
        for (var item: bean.getProperties())
        {
            if (item.hasValue())
                writeDescribedObject(writer, item.getValue(), item.getName());
            else if (item.hasHref())
                writeLink(writer, item, "uid", null, true);
        }
        writer.endArray();
    }
    
    
    protected void writeConnections(JsonWriter writer, List<Link> bean) throws IOException
    {
        if (bean.isEmpty())
            return;
        
        writer.name("connections").beginArray();
        for (var item: bean)
        {
            writer.beginObject();
            writer.name("source").value(item.getSource());
            writer.name("destination").value(item.getDestination());
            writer.endObject();
        }
        writer.endArray();
    }
}
