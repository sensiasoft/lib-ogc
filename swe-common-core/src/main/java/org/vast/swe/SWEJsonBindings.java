/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2012-2019 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.io.IOException;
import org.vast.data.DataComponentProperty;
import org.vast.data.DateTimeOrDouble;
import org.vast.data.EncodedValuesImpl;
import org.vast.data.JSONEncodingImpl;
import org.vast.data.SWEFactory;
import org.vast.data.TextEncodingImpl;
import org.vast.data.XMLEncodingImpl;
import org.vast.json.JsonInliningWriter;
import org.vast.unit.UnitParserUCUM;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.opengis.AbstractBindings;
import net.opengis.OgcProperty;
import net.opengis.OgcPropertyImpl;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.AbstractSWE;
import net.opengis.swe.v20.AbstractSWEIdentifiable;
import net.opengis.swe.v20.SimpleComponent;
import net.opengis.swe.v20.AllowedTimes;
import net.opengis.swe.v20.AllowedTokens;
import net.opengis.swe.v20.AllowedValues;
import net.opengis.swe.v20.BinaryBlock;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryOrRange;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountOrRange;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataStream;
import net.opengis.swe.v20.EncodedValues;
import net.opengis.swe.v20.Matrix;
import net.opengis.swe.v20.NilValue;
import net.opengis.swe.v20.NilValues;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityOrRange;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeOrRange;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.UnitReference;
import net.opengis.swe.v20.Vector;
import net.opengis.swe.v20.XMLEncoding;
import net.opengis.swe.v20.Factory;
import net.opengis.swe.v20.JSONEncoding;


@SuppressWarnings("javadoc")
public class SWEJsonBindings extends AbstractBindings
{
    protected Factory factory;


    public SWEJsonBindings()
    {
        factory = new SWEFactory();
    }
    
    
    public SWEJsonBindings(Factory factory)
    {
        this.factory = factory;
    }
    
    
    
    /********************
     * Read methods
     *******************/
    
    public DataComponent readDataComponent(JsonReader reader) throws IOException
    {
        return readDataComponent(reader, false);
    }
    
    
    public DataComponent readDataComponentWithName(JsonReader reader) throws IOException
    {
        return readDataComponent(reader, true);
    }
    
    
    public DataComponent readDataComponent(JsonReader reader, boolean nameRequired) throws IOException
    {
        String type = null;
        String name = null;
        DataComponent comp = null;
        
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
                
                if (nameRequired && name == null)
                    throw new JsonParseException("JSON object must contain a 'name' property before the other members @ " + reader.getPath());
            }
            
            if (type != null && (!nameRequired || name != null))
                break;
        }
        
        comp = readDataComponentByType(reader, type);
        if (comp == null)
            throw new JsonParseException("Invalid component type: " + type + " @ " + reader.getPath());
        
        if (name != null)
            comp.setName(name);
        
        return comp;
    }
    
    
    public DataComponent readDataComponentByType(JsonReader reader, String type) throws IOException
    {
        if ("DataRecord".equals(type))
            return readDataRecord(reader);
        else if ("Vector".equals(type))
            return readVector(reader);
        else if ("DataArray".equals(type))
            return readDataArray(reader);
        else if ("Matrix".equals(type))
            return readMatrix(reader);
        else if ("DataChoice".equals(type))
            return readDataChoice(reader);
        else if ("Count".equals(type))
            return readCount(reader);
        else if ("CategoryRange".equals(type))
            return readCategoryRange(reader);
        else if ("QuantityRange".equals(type))
            return readQuantityRange(reader);
        else if ("Time".equals(type))
            return readTime(reader);
        else if ("TimeRange".equals(type))
            return readTimeRange(reader);
        else if ("Boolean".equals(type))
            return readBoolean(reader);
        else if ("Text".equals(type))
            return readText(reader);
        else if ("Category".equals(type))
            return readCategory(reader);
        else if ("Quantity".equals(type))
            return readQuantity(reader);
        else if ("CountRange".equals(type))
            return readCountRange(reader);
        else
            return null;
    }
    
    
    public boolean readAbstractSWEProperties(JsonReader reader, AbstractSWE bean, String name) throws IOException
    {
        // id
        if ("id".equals(name))
            bean.setId(trimStringValue(reader.nextString()));
        
        else
            return false;
        
        return true;
    }
    
    
    public boolean readAbstractSWEIdentifiableProperties(JsonReader reader, AbstractSWEIdentifiable bean, String name) throws IOException
    {
        // identifier
        if ("identifier".equals(name))
            bean.setIdentifier(trimStringValue(reader.nextString()));

        // label
        else if ("label".equals(name))
            bean.setLabel(trimStringValue(reader.nextString()));

        // description
        else if ("description".equals(name))
            bean.setDescription(trimStringValue(reader.nextString()));
        
        else
            return readAbstractSWEProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readAbstractDataComponentProperties(JsonReader reader, DataComponent bean, String name) throws IOException
    {
        // definition
        if ("definition".equals(name))
            bean.setDefinition(reader.nextString());
        
        // name
        else if ("name".equals(name))
            bean.setName(reader.nextString());
        
        // updatable
        else if ("updatable".equals(name))
            bean.setUpdatable(reader.nextBoolean());

        // optional
        else if ("optional".equals(name))
            bean.setOptional(reader.nextBoolean());

        else
            return readAbstractSWEIdentifiableProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readAbstractSimpleComponentProperties(JsonReader reader, SimpleComponent bean, String name) throws IOException
    {
        // referenceframe
        if ("referenceFrame".equals(name))
            bean.setReferenceFrame(reader.nextString());

        // axisid
        else if ("axisID".equals(name))
            bean.setAxisID(reader.nextString());

        // quality
        else if ("quality".equals(name))
        {
            reader.beginArray();
            
            while (reader.hasNext())
            {
                OgcProperty<SimpleComponent> qualityProp = new OgcPropertyImpl<SimpleComponent>();
                qualityProp.setValue((SimpleComponent)readDataComponentWithName(reader));
                bean.getQualityList().add(qualityProp);
            }
            
            reader.endArray();
        }

        // nilValues
        else if ("nilValues".equals(name))
        {
            OgcProperty<NilValues> nilValuesProp = bean.getNilValuesProperty();
            nilValuesProp.setValue(readNilValues(reader));
        }
        
        else
            return readAbstractDataComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public DataRecord readDataRecord(JsonReader reader) throws IOException
    {
        DataRecord bean = factory.newDataRecord();
        
        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readDataRecordProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    public boolean readDataRecordProperties(final JsonReader reader, DataRecord bean, String name) throws IOException
    {
        if ("fields".equals(name))
        {
            reader.beginArray();
            
            while (reader.hasNext())
            {
                var fieldProp = new DataComponentProperty<DataComponent>();
                var comp = readDataComponentWithName(reader);
                fieldProp.setName(comp.getName());
                fieldProp.setValue(comp);
                
                try {
                    bean.getFieldList().add(fieldProp);
                }
                catch (IllegalArgumentException e) {
                    throw new JsonParseException(reader.getPath() + ": " + e.getMessage());
                }
            }
            
            reader.endArray();
        }
        
        else
            return readAbstractDataComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Vector readVector(JsonReader reader) throws IOException
    {
        Vector bean = factory.newVector();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readVectorProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readVectorProperties(JsonReader reader, Vector bean, String name) throws IOException
    {
        // referenceframe
        if ("referenceFrame".equals(name))
            bean.setReferenceFrame(reader.nextString());

        // localframe
        else if ("localFrame".equals(name))
            bean.setLocalFrame(reader.nextString());
        
        // coordinates
        else if ("coordinates".equals(name))
        {
            reader.beginArray();
            
            while (reader.hasNext())
            {
                var coordProp = new DataComponentProperty<ScalarComponent>();
                var comp = readDataComponentWithName(reader);
                if (!(comp instanceof Count || comp instanceof Quantity || comp instanceof Time))
                    throw new IOException("Invalid vector coordinate type: " + comp.getClass().getSimpleName());
                coordProp.setName(comp.getName());
                coordProp.setValue((ScalarComponent)comp);
                bean.getCoordinateList().add(coordProp);
            }
            
            reader.endArray();
        }
        
        else
            return readAbstractDataComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public DataChoice readDataChoice(JsonReader reader) throws IOException
    {
        DataChoice bean = factory.newDataChoice();
        
        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readDataChoiceProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readDataChoiceProperties(JsonReader reader, DataChoice bean, String name) throws IOException
    {
        // choiceValue
        if ("choiceValue".equals(name))
        {
            bean.setChoiceValue(readCategory(reader));
        }

        // item
        else if ("items".equals(name))
        {
            reader.beginArray();
            
            while (reader.hasNext())
            {
                var itemProp = new DataComponentProperty<DataComponent>();
                var comp = readDataComponentWithName(reader);
                itemProp.setName(comp.getName());
                itemProp.setValue(comp);
                bean.getItemList().add(itemProp);
            }
            
            reader.endArray();
        }
        
        else
            return readAbstractDataComponentProperties(reader, bean, name);
            
        return true;
    }
    
    
    public DataArray readDataArray(JsonReader reader) throws IOException
    {
        DataArray bean = factory.newDataArray();
        
        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readDataArrayProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readDataArrayProperties(JsonReader reader, DataArray bean, String name) throws IOException
    {
        // elementCount
        if ("elementCount".equals(name))
        {
            OgcProperty<Count> elementCountProp = bean.getElementCountProperty();
            elementCountProp.setValue(readCount(reader));
        }

        // elementType
        if ("elementType".equals(name))
        {
            OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
            elementTypeProp.setValue(readDataComponentWithName(reader));
        }

        // encoding
        if ("encoding".equals(name))
        {
            bean.setEncoding(readEncoding(reader));
        }

        // values
        if ("values".equals(name))
        {
            EncodedValues values = readEncodedValuesProperty(reader, bean, bean.getEncoding());
            bean.setValues(values);
        }
        
        else
            return readAbstractDataComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Matrix readMatrix(JsonReader reader) throws IOException
    {
        Matrix bean = factory.newMatrix();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readMatrixProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readMatrixProperties(JsonReader reader, Matrix bean, String name) throws IOException
    {
        // referenceframe
        if ("referenceFrame".equals(name))
            bean.setReferenceFrame(reader.nextString());

        // localframe
        if ("localFrame".equals(name))
            bean.setLocalFrame(reader.nextString());
        
        else
            return readDataArrayProperties(reader, bean, name);
        
        return true;
    }
    
    
    public DataStream readDataStream(JsonReader reader) throws IOException
    {
        DataStream bean = factory.newDataStream();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readDataStreamProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readDataStreamProperties(JsonReader reader, DataStream bean, String name) throws IOException
    {
        // elementCount
        if ("elementCount".equals(name))
        {
            OgcProperty<Count> elementCountProp = bean.getElementCountProperty();
            elementCountProp.setValue(readCount(reader));
        }

        // elementType
        if ("elementType".equals(name))
        {
            OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
            elementTypeProp.setValue(readDataComponentWithName(reader));
        }

        // encoding
        if ("encoding".equals(name))
        {
            bean.setEncoding(readEncoding(reader));
        }

        // values
        if ("values".equals(name))
        {
            EncodedValues values = readEncodedValuesProperty(reader, bean, bean.getEncoding());
            bean.setValues(values);
        }
        
        else
            return readAbstractSWEIdentifiableProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Boolean readBoolean(JsonReader reader) throws IOException
    {
        Boolean bean = factory.newBoolean();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readBooleanProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readBooleanProperties(JsonReader reader, Boolean bean, String name) throws IOException
    {
        if ("value".equals(name))
            bean.setValue(reader.nextBoolean());
        
        else
            return readAbstractSimpleComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Text readText(JsonReader reader) throws IOException
    {
        Text bean = factory.newText();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readTextProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readTextProperties(JsonReader reader, Text bean, String name) throws IOException
    {
        // constraint
        if ("constraint".equals(name))
        {
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            constraintProp.setValue(readAllowedTokens(reader));
        }

        // value
        if ("value".equals(name))
            bean.setValue(trimStringValue(reader.nextString()));
        
        else
            return readAbstractSimpleComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Count readCount(JsonReader reader) throws IOException
    {
        Count bean = factory.newCount();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readCountProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();
        
        return bean;
    }
    
    
    protected boolean readCountBaseProperties(JsonReader reader, CountOrRange bean, String name) throws IOException
    {
        // constraint
        if ("constraint".equals(name))
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            constraintProp.setValue(readAllowedValues(reader));
        }
        
        else
            return readAbstractSimpleComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readCountProperties(JsonReader reader, Count bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
            bean.setValue(reader.nextInt());
        
        else
            return readCountBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public CountRange readCountRange(JsonReader reader) throws IOException
    {
        CountRange bean = factory.newCountRange();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readCountRangeProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readCountRangeProperties(JsonReader reader, CountRange bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
        {
            reader.beginArray();
            int min = reader.nextInt();
            int max = reader.nextInt();
            bean.setValue(new int[] {min, max});
            reader.endArray();
        }
        
        else
            return readCountBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Category readCategory(JsonReader reader) throws IOException
    {
        Category bean = factory.newCategory();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readCategoryProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    protected boolean readCategoryBaseProperties(JsonReader reader, CategoryOrRange bean, String name) throws IOException
    {
        // codeSpace
        if ("codeSpace".equals(name))
            bean.setCodeSpace(reader.nextString());
        
        // constraint
        else if ("constraint".equals(name))
        {
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            constraintProp.setValue(readAllowedTokens(reader));
        }
        
        else
            return readAbstractSimpleComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readCategoryProperties(JsonReader reader, Category bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
            bean.setValue(reader.nextString());
        
        else
            return readCategoryBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public CategoryRange readCategoryRange(JsonReader reader) throws IOException
    {
        CategoryRange bean = factory.newCategoryRange();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readCategoryRangeProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readCategoryRangeProperties(JsonReader reader, CategoryRange bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
        {
            reader.beginArray();
            String min = reader.nextString();
            String max = reader.nextString();
            bean.setValue(new String[] {min, max});
            reader.endArray();
        }
        
        else
            return readCategoryBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Quantity readQuantity(JsonReader reader) throws IOException
    {
        Quantity bean = factory.newQuantity();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readQuantityProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    protected boolean readQuantityBaseProperties(JsonReader reader, QuantityOrRange bean, String name) throws IOException
    {
        // uom
        if ("uom".equals(name))
        {
            bean.setUom(readUnitReference(reader));
        }
        
        // constraint
        else if ("constraint".equals(name))
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            constraintProp.setValue(readAllowedValues(reader));
        }
        
        else
            return readAbstractSimpleComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    public boolean readQuantityProperties(JsonReader reader, Quantity bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
            bean.setValue(reader.nextDouble());
        
        else
            return readQuantityBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public QuantityRange readQuantityRange(JsonReader reader) throws IOException
    {
        QuantityRange bean = factory.newQuantityRange();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readQuantityRangeProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readQuantityRangeProperties(JsonReader reader, QuantityRange bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
        {
            reader.beginArray();
            double min = reader.nextDouble();
            double max = reader.nextDouble();
            bean.setValue(new double[] {min, max});
            reader.endArray();
        }
        
        else
            return readQuantityBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public Time readTime(JsonReader reader) throws IOException
    {
        Time bean = factory.newTime();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readTimeProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    protected boolean readTimeBaseProperties(JsonReader reader, TimeOrRange bean, String name) throws IOException
    {
        // referencetime
        if ("referenceTime".equals(name))
            bean.setReferenceTime(getDateTimeFromString(reader.nextString()));

        // localframe
        else if ("localFrame".equals(name))
            bean.setLocalFrame(reader.nextString());

        // uom
        else if ("uom".equals(name))
            bean.setUom(readUnitReference(reader));
        
        // constraint
        else if ("constraint".equals(name))
        {
            OgcProperty<AllowedTimes> constraintProp = bean.getConstraintProperty();
            constraintProp.setValue(readAllowedTimes(reader, bean.isIsoTime()));
        }
        
        else
            return readAbstractSimpleComponentProperties(reader, bean, name);
        
        return true;
    }
    
    
    protected DateTimeOrDouble readTimeVal(JsonReader reader, boolean isIso) throws IOException
    {
        return isIso ?
            new DateTimeOrDouble(getDateTimeFromString(reader.nextString())) :
            new DateTimeOrDouble(reader.nextDouble());
    }
    
    
    public boolean readTimeProperties(JsonReader reader, Time bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
            bean.setValue(readTimeVal(reader, bean.isIsoTime()));
        
        else
            return readTimeBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public TimeRange readTimeRange(JsonReader reader) throws IOException
    {
        TimeRange bean = factory.newTimeRange();

        // start reading object unless already started
        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readTimeRangeProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readTimeRangeProperties(JsonReader reader, TimeRange bean, String name) throws IOException
    {
        // value
        if ("value".equals(name))
        {
            reader.beginArray();
            var min = readTimeVal(reader, bean.isIsoTime());
            var max = readTimeVal(reader, bean.isIsoTime());
            bean.setValue(new DateTimeOrDouble[] {min, max});
            reader.endArray();
        }
        
        else
            return readTimeBaseProperties(reader, bean, name);
        
        return true;
    }
    
    
    public NilValues readNilValues(JsonReader reader) throws IOException
    {
        NilValues bean = factory.newNilValues();

        reader.beginArray();
        while (reader.hasNext())
            bean.addNilValue(readNilValue(reader));
        reader.endArray();

        return bean;
    }
    
    
    public NilValue readNilValue(JsonReader reader) throws IOException
    {
        NilValue bean = factory.newNilValue();

        reader.beginObject();
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readNilValueProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readNilValueProperties(JsonReader reader, NilValue bean, String name) throws IOException
    {
        if ("reason".equals(name))
            bean.setReason(reader.nextString());
        
        else if ("value".equals(name))
        {
            if (reader.peek() == JsonToken.NUMBER)
                bean.setValue(getStringValue(reader.nextDouble()));
            else
                bean.setValue(reader.nextString());
        }
        
        return true;
    }
    
    
    public AllowedTokens readAllowedTokens(JsonReader reader) throws IOException
    {
        AllowedTokens bean = factory.newAllowedTokens();

        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readAllowedTokensProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readAllowedTokensProperties(JsonReader reader, AllowedTokens bean, String name) throws IOException
    {
        // values
        if ("values".equals(name))
        {
            reader.beginArray();
            while (reader.hasNext())
                bean.addValue(trimStringValue(reader.nextString()));
            reader.endArray();
        }
        
        // pattern
        else if ("pattern".equals(name))
        {
            bean.setPattern(trimStringValue(reader.nextString()));
        }
        
        else
            return readAbstractSWEProperties(reader, bean, name);
        
        return true;
    }
    
    
    public AllowedValues readAllowedValues(JsonReader reader) throws IOException
    {
        AllowedValues bean = factory.newAllowedValues();

        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readAllowedValuesProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readAllowedValuesProperties(JsonReader reader, AllowedValues bean, String name) throws IOException
    {
        // values
        if ("values".equals(name))
        {
            reader.beginArray();
            while (reader.hasNext())
                bean.addValue(reader.nextDouble());
            reader.endArray();
        }
        
        // intervals
        else if ("intervals".equals(name))
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                reader.beginArray();
                var min = reader.nextDouble();
                var max = reader.nextDouble();
                bean.addInterval(new double[] {min, max});
                reader.endArray();
            }
            reader.endArray();
        }
        
        // significantFigures
        else if ("significantFigures".equals(name))
        {
            bean.setSignificantFigures(reader.nextInt());
        }
        
        else
            return readAbstractSWEProperties(reader, bean, name);
        
        return true;
    }
    
    
    public AllowedTimes readAllowedTimes(JsonReader reader, boolean isIso) throws IOException
    {
        AllowedTimes bean = factory.newAllowedTimes();

        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readAllowedTimesProperties(reader, bean, isIso, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readAllowedTimesProperties(JsonReader reader, AllowedTimes bean, boolean isIso, String name) throws IOException
    {
        // values
        if ("values".equals(name))
        {
            reader.beginArray();
            while (reader.hasNext())
                bean.addValue(readTimeVal(reader, isIso));
            reader.endArray();
        }
        
        // intervals
        else if ("intervals".equals(name))
        {
            reader.beginArray();
            while (reader.hasNext())
            {
                reader.beginArray();
                var min = readTimeVal(reader, isIso);
                var max = readTimeVal(reader, isIso);
                bean.addInterval(new DateTimeOrDouble[] {min, max});
                reader.endArray();
            }
            reader.endArray();
        }
        
        // significantFigures
        else if ("significantFigures".equals(name))
        {
            bean.setSignificantFigures(reader.nextInt());
        }
        
        else
            return readAbstractSWEProperties(reader, bean, name);
        
        return true;
    }
    
    
    public UnitReference readUnitReference(JsonReader reader) throws IOException
    {
        UnitReference bean = factory.newUnitReference();

        reader.beginObject();
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if (!readUnitReferenceProperties(reader, bean, name))
                reader.skipValue();
        }
        reader.endObject();

        return bean;
    }
    
    
    public boolean readUnitReferenceProperties(JsonReader reader, UnitReference bean, String name) throws IOException
    {
        // code 
        if ("code".equals(name))
        {
            var ucumCode = reader.nextString();
            if (!UnitParserUCUM.isValidUnit(ucumCode))
                throw new IOException("Invalid UCUM code '" + ucumCode + "'");
            bean.setCode(ucumCode);
        }
        
        else if ("href".equals(name))
        {
            bean.setHref(reader.nextString());
        }
        
        else
            return false;
        
        return true;
    }
    
    
    public EncodedValues readEncodedValuesProperty(JsonReader reader, AbstractSWEIdentifiable blockComponent, DataEncoding encoding) throws IOException
    {
        /*EncodedValues bean = factory.newEncodedValuesProperty();

        JsonReader reader = collectProperties(reader);
        readPropertyProperties(attrMap, bean);

        String text = reader.getElementText();
        if (text != null && text.trim().length() > 0)
        {
            if (blockComponent instanceof DataArray)
                bean.setAsText((DataArray)blockComponent, encoding, text);
            else if (blockComponent instanceof DataStream)
                bean.setAsText((DataStream)blockComponent, encoding, text);
            else if (blockComponent == null)
                bean.setAsText((DataArray)null, encoding, text);
        }
        else if (!bean.hasHref())
            return null;

        return bean;*/
        reader.skipValue();
        return null;
    }
    
    
    public DataEncoding readEncoding(JsonReader reader) throws IOException
    {
        reader.beginObject();
        String type = readObjectType(reader);
        return readEncodingByType(reader, type);
    }
    
    
    public DataEncoding readEncodingByType(JsonReader reader, String type) throws IOException
    {
        if ("TextEncoding".equals(type))
            return readTextEncoding(reader);
        else if ("JSONEncoding".equals(type))
            return readJsonEncoding(reader);
        else if ("XMLEncoding".equals(type))
            return readXmlEncoding(reader);
        else if ("BinaryEncoding".equals(type))
            return readBinaryEncoding(reader);
        else
            throw new JsonParseException("Invalid component type: " + type + " @ " + reader.getPath());
    }
    
    
    public TextEncoding readTextEncoding(JsonReader reader) throws IOException
    {
        var bean = factory.newTextEncoding();
        
        while (reader.hasNext())
        {
            var name = reader.nextName();
            
            if ("collapseWhiteSpaces".equals(name))
                bean.setCollapseWhiteSpaces(reader.nextBoolean());
            else if ("decimalSeparator".equals(name))
                bean.setDecimalSeparator(reader.nextString());
            else if ("tokenSeparator".equals(name))
                bean.setTokenSeparator(reader.nextString());
            else if ("blockSeparator".equals(name))
                bean.setBlockSeparator(reader.nextString());
            else
                reader.skipValue();
        }
        
        reader.endObject();
        return bean;
    }
    
    
    public JSONEncoding readJsonEncoding(JsonReader reader) throws IOException
    {
        return new JSONEncodingImpl();
    }
    
    
    public XMLEncoding readXmlEncoding(JsonReader reader) throws IOException
    {
        return new XMLEncodingImpl();
    }
    
    
    public BinaryEncoding readBinaryEncoding(JsonReader reader) throws IOException
    {
        var bean = factory.newBinaryEncoding();
        
        while (reader.hasNext())
        {
            var name = reader.nextName();
            
            if ("byteOrder".equals(name))
            {
                var val = reader.nextString();
                try {
                    bean.setByteOrder(ByteOrder.valueOf(val));
                } catch (IllegalArgumentException e){
                    throw new JsonParseException("Invalid byte order value: " + val + " @ " + reader.getPath());
                }
            }
            else if ("byteEncoding".equals(name))
            {
                var val = reader.nextString();
                try {
                    bean.setByteEncoding(ByteEncoding.valueOf(val));
                } catch (IllegalArgumentException e){
                    throw new JsonParseException("Invalid byte encoding: " + val + " @ " + reader.getPath());
                }
            }
            else if ("byteLength".equals(name))
            {
                bean.setByteLength(reader.nextInt());
            }
            else if ("members".equals(name))
            {
                reader.beginArray();
                while (reader.hasNext())
                {
                    var type = readObjectType(reader);
                    if ("Component".equals(type))
                        bean.addMemberAsComponent(readBinaryComponent(reader));
                    else if ("Block".equals(type))
                        bean.addMemberAsBlock(readBinaryBlock(reader));
                    else
                        throw new JsonParseException("Invalid binary component type: " + type + " @ " + reader.getPath());
                }
                reader.endArray();
            }
            else
                reader.skipValue();
        }
        
        reader.endObject();
        return bean;
    }
    
    
    protected BinaryComponent readBinaryComponent(JsonReader reader) throws IOException
    {
        var bean = factory.newBinaryComponent();

        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if ("ref".equals(name))
                bean.setRef(reader.nextString());
            else if ("dataType".equals(name))
                bean.setDataType(reader.nextString());
            else if ("bitLength".equals(name))
                bean.setBitLength(reader.nextInt());
            else if ("byteLength".equals(name))
                bean.setByteLength(reader.nextInt());
            else if ("significantBits".equals(name))
                bean.setSignificantBits(reader.nextInt());
            else if ("encryption".equals(name))
                bean.setEncryption(reader.nextString());
            else
                reader.skipValue();
        }
        reader.endObject();
        return bean;
    }
    
    
    protected BinaryBlock readBinaryBlock(JsonReader reader) throws IOException
    {
        var bean = factory.newBinaryBlock();

        if (reader.peek() == JsonToken.BEGIN_OBJECT)
            reader.beginObject();
        
        while (reader.hasNext())
        {
            String name = reader.nextName();
            if ("ref".equals(name))
                bean.setRef(reader.nextString());
            else if ("byteLength".equals(name))
                bean.setByteLength(reader.nextInt());
            else if ("paddingBytes-before".equals(name))
                bean.setPaddingBytesBefore(reader.nextInt());
            else if ("paddingBytes-after".equals(name))
                bean.setPaddingBytesAfter(reader.nextInt());
            else if ("compression".equals(name))
                bean.setCompression(reader.nextString());
            else if ("encryption".equals(name))
                bean.setEncryption(reader.nextString());
            else
                reader.skipValue();
        }
        reader.endObject();
        return bean;
    }
    
    
    public String readObjectType(JsonReader reader) throws IOException
    {
        if (!"type".equals(reader.nextName()))
            throw new JsonParseException("JSON object must contain a 'type' property as its first member @ " + reader.getPath());
        return reader.nextString();
    }
    
    
    public void checkObjectType(JsonReader reader, String expectedType) throws IOException
    {
        var type = readObjectType(reader);
        if (!expectedType.equals(type))
            throw new JsonParseException(expectedType + " object expected but was " + type + " @ " + reader.getPath());
    }


    /********************
     * Write methods
     *******************/
    
    public void writeAbstractSWEProperties(JsonWriter writer, AbstractSWE bean) throws IOException
    {
        // id
        if (bean.isSetId())
            writer.name("id").value(bean.getId());
    
        // extensions
        boolean extObjAdded = false;
        int numItems = bean.getExtensionList().size();
        for (int i = 0; i < numItems; i++)
        {
            Object item = bean.getExtensionList().get(i);
            if (canWriteExtension(item))
            {
                if (!extObjAdded)
                {
                    writer.name("extensions").beginObject();
                    extObjAdded = true;
                }
                
                writeExtension(writer, item);
            }
        }
        if (extObjAdded)
            writer.endObject();
    }
    
    
    public void writeAbstractSWEIdentifiableProperties(JsonWriter writer, AbstractSWEIdentifiable bean) throws IOException
    {
        writeAbstractSWEProperties(writer, bean);

        // identifier
        if (bean.isSetIdentifier())
            writer.name("identifier").value(bean.getIdentifier());

        // label
        if (bean.isSetLabel())
            writer.name("label").value(bean.getLabel());

        // description
        if (bean.isSetDescription())
            writer.name("description").value(bean.getDescription());
    }
    
    
    public void writeAbstractDataComponentProperties(JsonWriter writer, DataComponent bean) throws IOException
    {
        // definition
        if (bean.isSetDefinition())
            writer.name("definition").value(bean.getDefinition());
        
        writeAbstractSWEIdentifiableProperties(writer, bean);
        
        // updatable
        if (bean.isSetUpdatable())
            writer.name("updatable").value(bean.getUpdatable());

        // optional
        if (bean.isSetOptional())
            writer.name("optional").value(bean.getOptional());
    }
    
    
    public void writeAbstractSimpleComponentProperties(JsonWriter writer, SimpleComponent bean) throws IOException
    {
        writeAbstractDataComponentProperties(writer, bean);

        // referenceFrame
        if (bean.isSetReferenceFrame())
            writer.name("referenceFrame").value(bean.getReferenceFrame());

        // axisID
        if (bean.isSetAxisID())
            writer.name("axisID").value(bean.getAxisID());
        
        // quality
        if (!bean.getQualityList().isEmpty())
        {
            writer.name("quality").beginArray();
            
            for (OgcProperty<SimpleComponent> item: bean.getQualityList().getProperties())
            {
                if (item.hasValue() && !item.hasHref())
                    writeDataComponent(writer, item.getValue(), true);
                else if (item.hasHref())
                    writeLink(writer, item);
            }
            
            writer.endArray();
        }

        // nilValues
        if (bean.isSetNilValues())
        {
            writer.name("nilValues");
            OgcProperty<NilValues> nilValuesProp = bean.getNilValuesProperty();
            if (nilValuesProp.hasValue() && !nilValuesProp.hasHref())
                writeNilValues(writer, bean.getNilValues());
            else if (nilValuesProp.hasHref())
                writeLink(writer, nilValuesProp);
        }
    }
    
    
    public void writeDataRecordProperties(JsonWriter writer, DataRecord bean, boolean writeInlineValues) throws IOException
    {
        writeAbstractDataComponentProperties(writer, bean);
        
        // fields
        if (!bean.getFieldList().isEmpty())
        {
            writer.name("fields").beginArray();
            
            for (OgcProperty<DataComponent> item: bean.getFieldList().getProperties())
            {
                if (item.hasValue() && !item.hasHref())
                    writeDataComponent(writer, item.getValue(), writeInlineValues, item.getName());
                else if (item.hasHref())
                    writeLink(writer, item);
            }
            
            writer.endArray();
        }
    }
    
    
    public void writeVectorProperties(JsonWriter writer, Vector bean, boolean writeInlineValues) throws IOException
    {
        writeAbstractDataComponentProperties(writer, bean);

        // referenceFrame
        if (bean.isSetReferenceFrame())
            writer.name("referenceFrame").value(bean.getReferenceFrame());

        // localFrame
        if (bean.isSetLocalFrame())
            writer.name("localFrame").value(bean.getLocalFrame());

        // coordinates
        if (!bean.getCoordinateList().isEmpty())
        {
            writer.name("coordinates").beginArray();
            
            for (OgcProperty<ScalarComponent> item: bean.getCoordinateList().getProperties())
            {
                if (item.hasValue() && !item.hasHref())
                    writeDataComponent(writer, item.getValue(), writeInlineValues, item.getName());
                else if (item.hasHref())
                    writeLink(writer, item);
            }
            
            writer.endArray();
        }
    }
    
    
    public void writeDataChoiceProperties(JsonWriter writer, DataChoice bean, boolean writeInlineValues) throws IOException
    {
        writeAbstractDataComponentProperties(writer, bean);

        // choiceValue
        if (bean.isSetChoiceValue())
        {
            writer.name("choiceValue");
            writeDataComponent(writer, bean.getChoiceValue(), false);
        }

        // items
        if (!bean.getItemList().isEmpty())
        {
            writer.name("items").beginArray();
            
            for (OgcProperty<DataComponent> item: bean.getItemList().getProperties())
            {
                if (item.hasValue() && !item.hasHref())
                    writeDataComponent(writer, item.getValue(), writeInlineValues, item.getName());
                else if (item.hasHref())
                    writeLink(writer, item);
            }
            
            writer.endArray();
        }
    }
    
    
    public void writeDataArrayProperties(JsonWriter writer, DataArray bean, boolean writeInlineValues) throws IOException
    {
        writeAbstractDataComponentProperties(writer, bean);

        // elementCount
        writer.name("elementCount");
        OgcProperty<Count> elementCountProp = bean.getElementCountProperty();
        if (elementCountProp.hasValue() && !elementCountProp.hasHref())
            writeDataComponent(writer, bean.getElementCount(), true);
        else if (elementCountProp.hasHref())
            writeLink(writer, elementCountProp);

        // elementType
        writer.name("elementType");
        OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
        if (elementTypeProp.hasValue() && !elementTypeProp.hasHref())
            writeDataComponent(writer, bean.getElementType(), false, elementTypeProp.getName());
        else if (elementTypeProp.hasHref())
            writeLink(writer, elementTypeProp);
        
        if (writeInlineValues && bean.hasData())
        {
            // if not set, use text encoding by default
            if (!bean.isSetEncoding())
            {
                if (bean.getElementType() instanceof ScalarComponent)
                    bean.setEncoding(new TextEncodingImpl(",", " "));
                else
                    bean.setEncoding(new TextEncodingImpl());
            }
            
            // encoding
            writer.name("encoding");
            writeAbstractEncoding(writer, bean.getEncoding());
            
            // add encoded values object if needed
            if (!bean.isSetValues())
                bean.setValues(new EncodedValuesImpl());
            
            writer.name("values");
            writeEncodedValuesProperty(writer, bean, bean.getEncoding(), bean.getValues());
        }
    }
    
    
    public void writeMatrix(JsonWriter writer, Matrix bean, boolean writeInlineValues) throws IOException
    {
        writeMatrixProperties(writer, bean, writeInlineValues);
    }
    
    
    public void writeMatrixProperties(JsonWriter writer, Matrix bean, boolean writeInlineValues) throws IOException
    {
        writeDataArrayProperties(writer, bean, writeInlineValues);

        // referenceFrame
        if (bean.isSetReferenceFrame())
            writer.name("referenceFrame").value(bean.getReferenceFrame());

        // localFrame
        if (bean.isSetLocalFrame())
            writer.name("localFrame").value(bean.getLocalFrame());
    }
    
    
    public void writeDataStream(JsonWriter writer, DataStream bean) throws IOException
    {
        writeDataStreamProperties(writer, bean);
    }
    
    
    public void writeDataStreamProperties(JsonWriter writer, DataStream bean) throws IOException
    {
        writeAbstractSWEIdentifiableProperties(writer, bean);

        // elementCount
        if (bean.isSetElementCount())
        {
            writer.name("elementCount");
            writeDataComponent(writer, bean.getElementCount(), true);
        }

        // elementType
        writer.name("elementType");
        OgcProperty<DataComponent> elementTypeProp = bean.getElementTypeProperty();
        if (elementTypeProp.hasValue() && !elementTypeProp.hasHref())
            writeDataComponent(writer, bean.getElementType(), false, elementTypeProp.getName());
        else if (elementTypeProp.hasHref())
            writeLink(writer, elementTypeProp);

        // encoding
        writer.name("encoding");
        writeAbstractEncoding(writer, bean.getEncoding());

        // values
        writer.name("values");
        if (bean.isSetValues())
            writeEncodedValuesProperty(writer, bean, bean.getEncoding(), bean.getValues());
    }
    
    
    public void writeBooleanProperties(JsonWriter writer, Boolean bean, boolean writeInlineValues) throws IOException
    {
        writeAbstractSimpleComponentProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
            writer.name("value").value(bean.getValue());
    }

    
    public void writeTextProperties(JsonWriter writer, Text bean, boolean writeInlineValues) throws IOException
    {
        writeAbstractSimpleComponentProperties(writer, bean);

        // constraint
        if (bean.isSetConstraint())
        {
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            if (constraintProp.hasValue())
            {
                writer.name("constraint");
                writeAllowedTokens(writer, constraintProp.getValue());
            }
        }

        // value
        if (bean.isSetValue() && writeInlineValues)
            writer.name("value").value(bean.getValue());
    }
    
    
    protected void writeCountBaseProperties(JsonWriter writer, CountOrRange bean) throws IOException
    {
        writeAbstractSimpleComponentProperties(writer, bean);

        // constraint
        if (bean.isSetConstraint())
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            if (constraintProp.hasValue())
            {
                writer.name("constraint");
                writeAllowedValues(writer, constraintProp.getValue(), true);
            }
        }
    }
    
    
    public void writeCountProperties(JsonWriter writer, Count bean, boolean writeInlineValues) throws IOException
    {
        writeCountBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
            writer.name("value").value(bean.getValue());
    }
    
    
    public void writeCountRangeProperties(JsonWriter writer, CountRange bean, boolean writeInlineValues) throws IOException
    {
        writeCountBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.name("value").beginArray();
            writeInline(writer, true);
            int[] val = bean.getValue();
            writer.value(val[0]);
            writer.value(val[1]);
            writer.endArray();
            writeInline(writer, false);
        }
    }
    
    
    protected void writeCategoryBaseProperties(JsonWriter writer, CategoryOrRange bean) throws IOException
    {
        writeAbstractSimpleComponentProperties(writer, bean);

        // codeSpace
        if (bean.isSetCodeSpace())
        {
            writer.name("codeSpace").value(bean.getCodeSpace());
        }
        
        // constraint
        if (bean.isSetConstraint())
        {
            OgcProperty<AllowedTokens> constraintProp = bean.getConstraintProperty();
            if (constraintProp.hasValue())
            {
                writer.name("constraint");
                writeAllowedTokens(writer, constraintProp.getValue());
            }
        }
    }
    
    
    public void writeCategoryProperties(JsonWriter writer, Category bean, boolean writeInlineValues) throws IOException
    {
        writeCategoryBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
            writer.name("value").value(bean.getValue());
    }
    
    
    public void writeCategoryRangeProperties(JsonWriter writer, CategoryRange bean, boolean writeInlineValues) throws IOException
    {
        writeCategoryBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.name("value").beginArray();
            writeInline(writer, true);
            String[] val = bean.getValue();
            writer.value(val[0]);
            writer.value(val[1]);
            writer.endArray();
            writeInline(writer, false);
        }
    }
    
    
    protected void writeQuantityBaseProperties(JsonWriter writer, QuantityOrRange bean) throws IOException
    {
        writeAbstractSimpleComponentProperties(writer, bean);
        
        // uom
        writer.name("uom");
        writeUnitReference(writer, bean.getUom());

        // constraint
        if (bean.isSetConstraint())
        {
            OgcProperty<AllowedValues> constraintProp = bean.getConstraintProperty();
            if (constraintProp.hasValue())
            {
                writer.name("constraint");
                writeAllowedValues(writer, constraintProp.getValue(), false);
            }
        }
    }
    
    
    public void writeQuantityProperties(JsonWriter writer, Quantity bean, boolean writeInlineValues) throws IOException
    {
        writeQuantityBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
            writer.name("value").value(bean.getValue());
    }
    
    
    public void writeQuantityRangeProperties(JsonWriter writer, QuantityRange bean, boolean writeInlineValues) throws IOException
    {
        writeQuantityBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.name("value").beginArray();
            writeInline(writer, true);
            double[] val = bean.getValue();
            writer.value(val[0]);
            writer.value(val[1]);
            writer.endArray();
            writeInline(writer, false);
        }
    }
    
    
    protected void writeTimeBaseProperties(JsonWriter writer, TimeOrRange bean) throws IOException
    {
        writeAbstractSimpleComponentProperties(writer, bean);
        
        // referenceTime
        if (bean.isSetReferenceTime())
            writer.name("referenceTime").value(getStringValue(bean.getReferenceTime()));

        // localFrame
        if (bean.isSetLocalFrame())
            writer.name("localFrame").value(bean.getLocalFrame());
        
        // uom
        writer.name("uom");
        writeUnitReference(writer, bean.getUom());

        // constraint
        if (bean.isSetConstraint())
        {
            OgcProperty<AllowedTimes> constraintProp = bean.getConstraintProperty();
            if (constraintProp.hasValue())
            {
                writer.name("constraint");
                writeAllowedTimes(writer, constraintProp.getValue());
            }
        }
    }
    
    
    public void writeTimeProperties(JsonWriter writer, Time bean, boolean writeInlineValues) throws IOException
    {
        writeTimeBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.name("value");
            writeTimeVal(writer, bean.getValue());
        }
    }
    
    
    protected void writeTimeVal(JsonWriter writer, DateTimeOrDouble val) throws IOException
    {
        if (val.isDateTime())
            writer.value(getStringValue(val.getDateTime()));
        else
            writer.value(val.getDecimalTime());
    }
    
    
    public void writeTimeRangeProperties(JsonWriter writer, TimeRange bean, boolean writeInlineValues) throws IOException
    {
        writeTimeBaseProperties(writer, bean);

        // value
        if (bean.isSetValue() && writeInlineValues)
        {
            writer.name("value").beginArray();
            var range = bean.getValue();
            writeTimeVal(writer, range[0]);
            writeTimeVal(writer, range[1]);
            writer.endArray();
        }
    }
    
    
    public void writeNilValues(JsonWriter writer, NilValues bean) throws IOException
    {
        writer.beginArray();
        for (var nilVal: bean.getNilValueList())
            writeNilValue(writer, nilVal);
        writer.endArray();
    }
    
    
    public void writeAllowedTokens(JsonWriter writer, AllowedTokens bean) throws IOException
    {
        writer.beginObject();
        writeAllowedTokensProperties(writer, bean);
        writer.endObject();
    }
    
    
    public void writeAllowedTokensProperties(JsonWriter writer, AllowedTokens bean) throws IOException
    {
        writeAbstractSWEProperties(writer, bean);

        // values
        if (bean.getNumValues() > 0)
        {
            writer.name("values").beginArray();
            for (String val: bean.getValueList())
                writer.value(val);
            writer.endArray();
        }

        // pattern
        if (bean.isSetPattern())
            writer.name("pattern").value(bean.getPattern());
    }
    
    
    public void writeAllowedValues(JsonWriter writer, AllowedValues bean, boolean writeIntegers) throws IOException
    {
        writer.beginObject();
        writeAllowedValuesProperties(writer, bean, writeIntegers);
        writer.endObject();
    }
    
    
    public void writeAllowedValuesProperties(JsonWriter writer, AllowedValues bean, boolean writeIntegers) throws IOException
    {
        writeAbstractSWEProperties(writer, bean);

        // values
        if (bean.getNumValues() > 0)
        {
            writer.name("values").beginArray();
            writeInline(writer, true);
            for (double val: bean.getValueList())
                writer.value(val);
            writer.endArray();
            writeInline(writer, false);
        }

        // intervals
        if (bean.getNumIntervals() > 0)
        {
            writer.name("intervals").beginArray();
            for (double[] range: bean.getIntervalList())
            {
                writer.beginArray();
                writeInline(writer, true);
                writer.value(range[0]);
                writer.value(range[1]);
                writer.endArray();
                writeInline(writer, false);
            }
            writer.endArray();
        }

        // significantFigures
        if (bean.isSetSignificantFigures())
            writer.name("significantFigures").value(bean.getSignificantFigures());
    }
    
    
    public void writeAllowedTimes(JsonWriter writer, AllowedTimes bean) throws IOException
    {
        writer.beginObject();
        writeAllowedTimesProperties(writer, bean);
        writer.endObject();
    }
    
    
    public void writeAllowedTimesProperties(JsonWriter writer, AllowedTimes bean) throws IOException
    {
        writeAbstractSWEProperties(writer, bean);

        // values
        if (bean.getNumValues() > 0)
        {
            writer.name("values").beginArray();
            for (var val: bean.getValueList())
                writeTimeVal(writer, val);
            writer.endArray();
        }

        // intervals
        if (bean.getNumIntervals() > 0)
        {
            writer.name("intervals").beginArray();
            for (var range: bean.getIntervalList())
            {
                writer.beginArray();
                writeTimeVal(writer, range[0]);
                writeTimeVal(writer, range[1]);
                writer.endArray();
            }
            writer.endArray();
        }

        // significantFigures
        if (bean.isSetSignificantFigures())
            writer.name("significantFigures").value(bean.getSignificantFigures());
    }
    
    
    public void writeUnitReference(JsonWriter writer, UnitReference bean) throws IOException
    {
        writer.beginObject();
        writeUnitReferenceProperties(writer, bean);
        writer.endObject();
    }
    
    
    public void writeUnitReferenceProperties(JsonWriter writer, UnitReference bean) throws IOException
    {
        // code
        if (bean.isSetCode())
            writer.name("code").value(bean.getCode());
        
        else if (bean.hasHref())
            writer.name("href").value(bean.getHref());
    }
    
    
    public void writeNilValue(JsonWriter writer, NilValue bean) throws IOException
    {
        writer.beginObject();
        writer.name("reason").value(bean.getReason());
        writer.name("value").value(bean.getValue());
        writer.endObject();
    }
    
    
    public void writeEncodedValuesProperty(JsonWriter writer, AbstractSWEIdentifiable blockComponent, DataEncoding encoding, EncodedValues bean) throws IOException
    {
        /*writePropertyProperties(writer, bean);

        if (!bean.hasHref())
        {
            String text = null;
            if (blockComponent instanceof DataArray)
                text = bean.getAsText((DataArray)blockComponent, encoding);
            else if (blockComponent instanceof DataStream)
                text = bean.getAsText((DataStream)blockComponent, encoding);

            if (text != null)
                writer.writeCharacters(text);
        }*/
        writer.beginArray();
        writer.endArray();
    }


    /**
     * Dispatcher method for writing classes derived from AbstractDataComponent
     */
    public void writeDataComponent(JsonWriter writer, DataComponent bean, boolean writeInlineValues) throws IOException
    {
        writeDataComponent(writer, bean, writeInlineValues, null);
    }
    
    
    public void writeDataComponent(JsonWriter writer, DataComponent bean, boolean writeInlineValues, String name) throws IOException
    {
        writer.beginObject();
        
        if (name != null)
            writer.name("name").value(name);
        
        if (bean instanceof DataRecord)
        {
            writer.name("type").value("DataRecord");
            writeDataRecordProperties(writer, (DataRecord)bean, writeInlineValues);
        }
        else if (bean instanceof Vector)
        {
            writer.name("type").value("Vector");
            writeVectorProperties(writer, (Vector)bean, writeInlineValues);
        }
        else if (bean instanceof Matrix)
        {
            writer.name("type").value("Matrix");
            writeMatrixProperties(writer, (Matrix)bean, writeInlineValues);
        }
        else if (bean instanceof DataArray)
        {
            writer.name("type").value("DataArray");
            writeDataArrayProperties(writer, (DataArray)bean, writeInlineValues);
        }
        else if (bean instanceof DataChoice)
        {
            writer.name("type").value("DataChoice");
            writeDataChoiceProperties(writer, (DataChoice)bean, writeInlineValues);
        }
        else if (bean instanceof Count)
        {
            writer.name("type").value("Count");
            writeCountProperties(writer, (Count)bean, writeInlineValues);
        }
        else if (bean instanceof CategoryRange)
        {
            writer.name("type").value("CategoryRange");
            writeCategoryRangeProperties(writer, (CategoryRange)bean, writeInlineValues);
        }
        else if (bean instanceof QuantityRange)
        {
            writer.name("type").value("QuantityRange");
            writeQuantityRangeProperties(writer, (QuantityRange)bean, writeInlineValues);
        }
        else if (bean instanceof Time)
        {
            writer.name("type").value("Time");
            writeTimeProperties(writer, (Time)bean, writeInlineValues);
        }
        else if (bean instanceof TimeRange)
        {
            writer.name("type").value("TimeRange");
            writeTimeRangeProperties(writer, (TimeRange)bean, writeInlineValues);
        }
        else if (bean instanceof Boolean)
        {
            writer.name("type").value("Boolean");
            writeBooleanProperties(writer, (Boolean)bean, writeInlineValues);
        }
        else if (bean instanceof Text)
        {
            writer.name("type").value("Text");
            writeTextProperties(writer, (Text)bean, writeInlineValues);
        }
        else if (bean instanceof Category)
        {
            writer.name("type").value("Category");
            writeCategoryProperties(writer, (Category)bean, writeInlineValues);
        }
        else if (bean instanceof Quantity)
        {
            writer.name("type").value("Quantity");
            writeQuantityProperties(writer, (Quantity)bean, writeInlineValues);
        }
        else if (bean instanceof CountRange)
        {
            writer.name("type").value("CountRange");
            writeCountRangeProperties(writer, (CountRange)bean, writeInlineValues);
        }
        
        writer.endObject();
    }


    /**
     * Dispatcher method for writing classes derived from AbstractEncoding
     */
    public void writeAbstractEncoding(JsonWriter writer, DataEncoding bean) throws IOException
    {
        writer.beginObject();
        
        if (bean instanceof TextEncoding)
        {
            writer.name("type").value("TextEncoding");
            writeTextEncodingProperties(writer, (TextEncoding)bean);
        }
        else if (bean instanceof JSONEncoding)
        {
            writer.name("type").value("JSONEncoding");
            writeJsonEncodingProperties(writer, (JSONEncoding)bean);
        }
        else if (bean instanceof XMLEncoding)
        {
            writer.name("type").value("XMLEncoding");
            writeXmlEncodingProperties(writer, (XMLEncoding)bean);
        }
        else if (bean instanceof BinaryEncoding)
        {
            writer.name("type").value("BinaryEncoding");
            writeBinaryEncodingProperties(writer, (BinaryEncoding)bean);
        }
        
        writer.endObject();
    }
    
    
    public void writeTextEncodingProperties(JsonWriter writer, TextEncoding bean) throws IOException
    {
        writer.name("collapseWhiteSpaces").value(bean.getCollapseWhiteSpaces());
        writer.name("decimalSeparator").value(bean.getDecimalSeparator());
        writer.name("tokenSeparator").value(bean.getTokenSeparator());
        writer.name("blockSeparator").value(bean.getBlockSeparator());
    }
    
    
    public void writeJsonEncodingProperties(JsonWriter writer, JSONEncoding bean) throws IOException
    {
        
    }
    
    
    public void writeXmlEncodingProperties(JsonWriter writer, XMLEncoding bean) throws IOException
    {
        
    }
    
    
    public void writeBinaryEncodingProperties(JsonWriter writer, BinaryEncoding bean) throws IOException
    {
        writer.name("byteOrder").value(bean.getByteOrder().toString());
        writer.name("byteEncoding").value(bean.getByteEncoding().toString());
        
        if (bean.getNumMembers() > 0)
        {
            writer.name("members").beginArray();
            for (var member: bean.getMemberList())
            {
                writer.beginObject();
                if (member instanceof BinaryComponent)
                {
                    writer.name("type").value("Component");
                    writer.name("ref").value(member.getRef());
                    
                    var comp = (BinaryComponent)member;
                    writer.name("dataType").value(comp.getDataType());

                    if (comp.isSetBitLength())
                        writer.name("bitLength").value(comp.getBitLength());

                    if (comp.isSetByteLength())
                        writer.name("byteLength").value(comp.getByteLength());

                    if (comp.isSetSignificantBits())
                        writer.name("significantBits").value(comp.getSignificantBits());

                    if (comp.isSetEncryption())
                        writer.name("encryption").value(comp.getEncryption());
                }
                else if (member instanceof BinaryBlock)
                {
                    writer.name("type").value("Block");
                    writer.name("ref").value(member.getRef());
                    
                    var block = (BinaryBlock)member;
                    
                    if (block.isSetByteLength())
                        writer.name("byteLength").value(block.getByteLength());
                    
                    if (block.isSetPaddingBytesBefore())
                        writer.name("paddingBytes-before").value(block.getPaddingBytesBefore());
                    
                    if (block.isSetPaddingBytesAfter())
                        writer.name("paddingBytes-after").value(block.getPaddingBytesAfter());

                    if (block.isSetEncryption())
                        writer.name("encryption").value(block.getEncryption());

                    if (block.isSetCompression())
                        writer.name("compression").value(block.getCompression());
                }
                writer.endObject();
            }
            writer.endArray();
        }
    }
    
    
    public void writeExtension(JsonWriter writer, Object obj) throws IOException
    {
        // we do nothing by default
        // sub-classes can override to implement some extensions
    }
    
    
    protected void writeLink(JsonWriter writer, OgcProperty<?> prop) throws IOException
    {
        writer.beginObject();
        String val;
        
        val = prop.getName();
        if (val != null)
            writer.name("name").value(val);
        
        val = prop.getHref();
        if (val != null)
            writer.name("href").value(val);
        
        val = prop.getTitle();
        if (val != null)
            writer.name("title").value(val);
        
        val = prop.getRole();
        if (val != null)
            writer.name("role").value(val);
        
        val = prop.getArcRole();
        if (val != null)
            writer.name("arcrole").value(val);
            
            writer.endObject();
    }
    
    
    protected void writeInline(JsonWriter writer, boolean inline)
    {
        if (writer instanceof JsonInliningWriter)
            ((JsonInliningWriter)writer).writeInline(inline);
    }


    public Factory getFactory()
    {
        return factory;
    }
}