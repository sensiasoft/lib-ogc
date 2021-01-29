/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.

Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.

******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.vast.util.DateTimeFormat;
import org.vast.util.ReaderException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * New implementation of JSON data parser with better efficiency since the
 * read tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p><p>
 * This particular implementation is based on Gson JsonReader.
 * </p>
 *
 * @author Alex Robin
 * @since Jan 26, 2021
 */
public class JsonDataParserGson extends AbstractDataParser
{
    static final String JSON_ERROR = "Error reading JSON";
    static final String DEFAULT_INDENT = "  ";

    protected JsonReader reader;
    protected boolean multipleRecords;
    protected Map<String, IntegerReader> countReaders = new HashMap<>();
    protected boolean hasJsonArrayWrapper = false;


    protected interface JsonAtomReader
    {
        String getEltName();
    }


    protected abstract class ValueReader extends BaseProcessor implements JsonAtomReader
    {
        String eltName;

        public abstract void readValue(DataBlock data, int index) throws IOException;

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                if (enabled)
                    readValue(data, index);
                return ++index;
            }
            catch (IOException | IllegalStateException e)
            {
                throw new ReaderException(JSON_ERROR + " value " + eltName, e);
            }
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class BooleanReader extends ValueReader
    {
        public BooleanReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            var val = reader.nextBoolean();
            data.setBooleanValue(index, val);
        }
    }


    protected class IntegerReader extends ValueReader
    {
        int val;

        public IntegerReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            var val = reader.nextInt();
            data.setIntValue(index, val);
        }
    }


    protected class DoubleReader extends ValueReader
    {
        public DoubleReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            var token = reader.peek();
            double val = 0.0;
            
            switch (token)
            {
                case NUMBER:
                    val = reader.nextDouble();
                    break;
                    
                // handle cases of special values
                case STRING:
                    var str = reader.nextString();
                    if ("NaN".equals(str))
                        val = Double.NaN;
                    else if ("+INF".equals(str) || "INF".equals(str))
                        val = Double.POSITIVE_INFINITY;
                    else if ("-INF".equals(str))
                        val = Double.NEGATIVE_INFINITY;
                    break;
                    
                default:
                    throw new IllegalStateException("Unexpected datatype " + token);
            }
                        
            data.setDoubleValue(index, val);
        }
    }


    protected class IsoDateTimeReader extends ValueReader
    {
        DateTimeFormat timeFormat = new DateTimeFormat();

        public IsoDateTimeReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            var str = reader.nextString();
            double val = 0.0;
            
            if ("NaN".equals(str))
                val = Double.NaN;
            else if ("+INF".equals(str) || "INF".equals(str))
                val = Double.POSITIVE_INFINITY;
            else if ("-INF".equals(str))
                val = Double.NEGATIVE_INFINITY;
            else
            {
                try { val = timeFormat.parseIso(str); }
                catch (ParseException e) { throw new IOException(e.getMessage()); }
            }
            
            data.setDoubleValue(index, val);
        }
    }


    protected class StringReader extends ValueReader
    {
        static final int BUF_SIZE_INCREMENT = 64;
        char[] buf = new char[2*BUF_SIZE_INCREMENT];
        int pos;
        
        public StringReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            var str = reader.nextString();
            data.setStringValue(index, str);
        }
    }


    protected class RangeReader extends RecordProcessor implements JsonAtomReader
    {
        String eltName;

        public RangeReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                reader.beginArray();
                fieldProcessors.get(0).process(data, index++);
                fieldProcessors.get(1).process(data, index++);
                reader.endArray();
                return index;
            }
            catch (IOException e)
            {
                throw new ReaderException(JSON_ERROR + " range " + eltName, e);
            }
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class RecordReader extends RecordProcessor implements JsonAtomReader
    {
        String eltName;
        boolean onlyScalars = true;

        public RecordReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            // skip if disabled
            if (!enabled)
                return super.process(data, index);
            
            try
            {
                reader.beginObject();
                
                for (AtomProcessor p: fieldProcessors)
                {
                    if (p.isEnabled())
                    {
                        var expectedName = ((JsonAtomReader)p).getEltName();
                        var actualName = reader.nextName();
                        if (!actualName.equals(expectedName))
                            throw new IllegalStateException("Expected field " + expectedName + " but was " + actualName);
                    }
                    
                    index = p.process(data, index);
                }

                reader.endObject();
                
                return index;
            }
            catch (IllegalStateException | IOException e)
            {
                throw new ReaderException(JSON_ERROR + " record " + eltName, e);
            }
        }

        @Override
        public void add(AtomProcessor processor)
        {
            fieldProcessors.add(processor);
            if (!(processor instanceof ValueReader))
                onlyScalars = false;
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class ArrayReader extends ArrayProcessor implements JsonAtomReader
    {
        String eltName;
        boolean onlyScalars = true;
        IntegerReader sizeReader;
        
        public ArrayReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            // skip if disabled
            if (!enabled)
                return super.process(data, index);
            
            try
            {
                reader.beginArray();
                
                for (int i = 0; i < arraySize; i++)
                    index = eltProcessor.process(data, index);
                
                reader.endArray();                
                
                return index;
            }
            catch (IOException e)
            {
                throw new ReaderException(JSON_ERROR + " array " + eltName, e);
            }
        }

        @Override
        public void add(AtomProcessor processor)
        {
            super.add(processor);
            if (!(processor instanceof ValueReader))
                onlyScalars = false;
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class ChoiceReader extends ChoiceProcessor implements JsonAtomReader
    {
        String eltName;
        boolean onlyScalars = false;
        Map<String, Integer> itemIndexes = new HashMap<>();

        public ChoiceReader(DataChoice choice)
        {
            this.eltName = choice.getName();
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            // skip if disabled
            if (!enabled)
                return index+1;
            
            try
            {
                reader.beginObject();
                
                var itemName = reader.nextName();
                var selectedIndex = itemIndexes.get(itemName);
                if (selectedIndex == null)
                    throw new IllegalArgumentException("Invalid choice token");
                
                data.setIntValue(index++, selectedIndex);
                // TODO set proper datablock for selected choice item
                super.process(data, index, selectedIndex);

                reader.endObject();

                return index;
            }
            catch (IOException e)
            {
                throw new ReaderException(JSON_ERROR + " choice " + eltName, e);
            }
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            var itemName = ((JsonAtomReader)processor).getEltName();
            itemIndexes.put(itemName, itemProcessors.size());
            super.add(processor);
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class ArraySizeScanner extends BaseProcessor
    {
        ArrayProcessor arrayProcessor;

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int arraySize = data.getIntValue(index);
            arrayProcessor.arraySize = arraySize;
            return ++index;
        }
    }
    
    
    public JsonDataParserGson()
    {
    }
    
    
    public JsonDataParserGson(JsonReader reader)
    {
        this.reader = reader;
    }


    @Override
    protected void init() throws IOException
    {
        if (hasJsonArrayWrapper)
            reader.beginArray();
    }


    @Override
    public void setInput(InputStream is) throws IOException
    {
        this.reader = new JsonReader(new InputStreamReader(is));
    }
    
    
    public void setHasArrayWrapper()
    {
        this.hasJsonArrayWrapper = true;
    }


    @Override
    public void close() throws IOException
    {
        if (reader != null)
            reader.close();
    }


    @Override
    public void reset()
    {
        super.reset();
    }


    @Override
    public void visit(Boolean comp)
    {
        addToProcessorTree(new BooleanReader(comp.getName()));
    }


    @Override
    public void visit(Count comp)
    {
        IntegerReader Reader = new IntegerReader(comp.getName());
        if (comp.isSetId())
            countReaders.put(comp.getId(), Reader);
        addToProcessorTree(Reader);
    }


    @Override
    public void visit(Quantity comp)
    {
        addToProcessorTree(new DoubleReader(comp.getName()));
    }


    @Override
    public void visit(Time comp)
    {
        if (!comp.isIsoTime())
            addToProcessorTree(new DoubleReader(comp.getName()));
        else
            addToProcessorTree(new IsoDateTimeReader(comp.getName()));
    }


    @Override
    public void visit(Category comp)
    {
        addToProcessorTree(new StringReader(comp.getName()));
    }


    @Override
    public void visit(Text comp)
    {
        addToProcessorTree(new StringReader(comp.getName()));
    }


    @Override
    public void visit(CountRange range)
    {
        addToProcessorTree(new RangeReader(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(QuantityRange range)
    {
        addToProcessorTree(new RangeReader(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(TimeRange range)
    {
        addToProcessorTree(new RangeReader(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(CategoryRange range)
    {
        addToProcessorTree(new RangeReader(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(DataRecord rec)
    {
        addToProcessorTree(new RecordReader(rec.getName()));
        for (DataComponent field: rec.getFieldList())
        {
            boolean saveEnabled = enableSubTree;
            checkEnabled(field);
            field.accept(this);
            enableSubTree = saveEnabled; // reset flag
        }
        processorStack.pop();
    }


    @Override
    public void visit(Vector rec)
    {
        addToProcessorTree(new RecordReader(rec.getName()));
        for (DataComponent field: rec.getCoordinateList())
            field.accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(DataChoice choice)
    {
        addToProcessorTree(new ChoiceReader(choice));
        for (DataComponent item: choice.getItemList())
            item.accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(DataArray array)
    {
        ArrayReader arrayReader = new ArrayReader(array.getName());

        if (array.isImplicitSize())
        {
            ArraySizeScanner sizeReader = new ArraySizeScanner();
            sizeReader.arrayProcessor = arrayReader;
            addToProcessorTree(sizeReader);
        }
        else if (array.isVariableSize())
        {
            // look for size Reader
            String refId = array.getArraySizeComponent().getId();
            arrayReader.sizeReader = countReaders.get(refId);
        }
        else
            arrayReader.setArraySize(array.getComponentCount());

        addToProcessorTree(arrayReader);
        array.getElementType().accept(this);
        processorStack.pop();
    }


    @Override
    protected boolean moreData() throws IOException
    {
        return reader.peek() != JsonToken.END_DOCUMENT && reader.hasNext();
    }
}
