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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.vast.json.JsonInliningWriter;
import org.vast.util.DateTimeFormat;
import org.vast.util.WriterException;
import com.google.gson.stream.JsonWriter;
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
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * New implementation of JSON data writer with better efficiency since the
 * write tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p><p>
 * This particular implementation is based on Gson JsonWriter.
 * </p>
 *
 * @author Alex Robin
 * @since Jan 26, 2021
 */
public class JsonDataWriterGson extends AbstractDataWriter
{
    static final String JSON_ERROR = "Error writing JSON ";
    static final String DEFAULT_INDENT = "  ";

    protected JsonWriter writer;
    protected boolean wrapWithJsonArray;
    Map<String, IntegerWriter> countWriters = new HashMap<>();


    protected interface JsonAtomWriter
    {
        String getEltName();
    }


    protected abstract class ValueWriter extends BaseProcessor implements JsonAtomWriter
    {
        String eltName;

        public abstract void writeValue(DataBlock data, int index) throws IOException;

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                if (enabled)
                    writeValue(data, index);
                return ++index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + " value " + eltName, e);
            }
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class BooleanWriter extends ValueWriter
    {
        public BooleanWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            boolean val = data.getBooleanValue(index);
            writer.value(val);
        }
    }


    protected class IntegerWriter extends ValueWriter
    {
        int val;

        public IntegerWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            val = data.getIntValue(index);
            writer.value(val);
        }
    }


    protected class DoubleWriter extends ValueWriter
    {
        public DoubleWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);

            // need to add quote on special values because they are not valid literal values in JSON
            if (Double.isNaN(val))
                writer.value("NaN");
            else if (val == Double.POSITIVE_INFINITY)
                writer.value("+INF");
            else if (val == Double.NEGATIVE_INFINITY)
                writer.value("-INF");
            else
                writer.value(val);
        }
    }


    protected class FloatWriter extends ValueWriter
    {
        public FloatWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            float val = data.getFloatValue(index);

            // need to add quote on special values because they are not valid literal values in JSON
            if (Float.isNaN(val))
                writer.value("NaN");
            else if (val == Float.POSITIVE_INFINITY)
                writer.value("+INF");
            else if (val == Float.NEGATIVE_INFINITY)
                writer.value("-INF");
            else
                writer.value(val);
        }
    }


    protected class RoundingDecimalWriter extends DoubleWriter
    {
        int scale;

        public RoundingDecimalWriter(String eltName, int numDecimalPlaces)
        {
            super(eltName);
            this.scale = numDecimalPlaces;
        }

        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);

            // need to add quote on special values because they are not valid literal values in JSON
            if (Double.isNaN(val))
                writer.value("NaN");
            else if (val == Double.POSITIVE_INFINITY)
                writer.value("+INF");
            else if (val == Double.NEGATIVE_INFINITY)
                writer.value("-INF");
            else
            {
                BigDecimal bd = BigDecimal.valueOf(val);
                bd = bd.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
                writer.jsonValue(bd.toPlainString());
            }
        }
    }


    protected class IsoDateTimeWriter extends ValueWriter
    {
        DateTimeFormat timeFormat = new DateTimeFormat();

        public IsoDateTimeWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);
            if (Double.isNaN(val))
                writer.value("NaN");
            else if (val == Double.POSITIVE_INFINITY)
                writer.value("+INF");
            else if (val == Double.NEGATIVE_INFINITY)
                writer.value("-INF");
            else
                writer.value(timeFormat.formatIso(val, 0));
        }
    }


    protected class StringWriter extends ValueWriter
    {
        static final int BUF_SIZE_INCREMENT = 64;
        char[] buf = new char[2*BUF_SIZE_INCREMENT];
        int pos;
        
        public StringWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            String val = data.getStringValue(index);
            writer.value(val);
        }
    }


    protected class RangeWriter extends RecordProcessor implements JsonAtomWriter
    {
        String eltName;

        public RangeWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writer.beginArray();
                fieldProcessors.get(0).process(data, index++);
                fieldProcessors.get(1).process(data, index++);
                writer.endArray();
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + " range " + eltName, e);
            }
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class RecordWriter extends RecordProcessor implements JsonAtomWriter
    {
        String eltName;
        boolean isVector;

        public RecordWriter(String eltName, boolean isVector)
        {
            this.eltName = eltName;
            this.isVector = isVector;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            // skip if disabled
            if (!enabled)
                return super.process(data, index);
            
            try
            {
                writer.beginObject();
                
                // no indent if only scalars
                if (isVector)
                    writeInline(true);

                for (AtomProcessor p: fieldProcessors)
                {
                    if (p.isEnabled())
                        writer.name(((JsonAtomWriter)p).getEltName());
                    index = p.process(data, index);
                }

                writer.endObject();

                // restore indent
                writeInline(false);
                
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + " record " + eltName, e);
            }
        }

        /*@Override
        public void add(AtomProcessor processor)
        {
            fieldProcessors.add(processor);
            if (!(processor instanceof ValueWriter))
                onlyScalars = false;
        }*/

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class ArrayWriter extends ArrayProcessor implements JsonAtomWriter
    {
        String eltName;
        boolean onlyScalars = true;
        IntegerWriter sizeWriter;
        
        public ArrayWriter(String eltName)
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
                // retrieve variable array size
                if (sizeWriter != null)
                    arraySize = sizeWriter.val;

                writer.beginArray();
                
                // no indent if only scalars
                if (onlyScalars)
                    writeInline(true);

                for (int i = 0; i < arraySize; i++)
                    index = eltProcessor.process(data, index);
                
                writer.endArray();

                // restore indent
                if (onlyScalars)
                    writeInline(false);
                
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + " array " + eltName, e);
            }
        }

        @Override
        public void add(AtomProcessor processor)
        {
            super.add(processor);
            if (!(processor instanceof ValueWriter))
                onlyScalars = false;
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected class ChoiceWriter extends ChoiceProcessor implements JsonAtomWriter
    {
        String eltName;
        boolean onlyScalars = false;
        ArrayList<String> choiceTokens;

        public ChoiceWriter(DataChoice choice)
        {
            this.eltName = choice.getName();
            choiceTokens = new ArrayList<>(choice.getNumItems());
            for (DataComponent item: choice.getItemList())
                choiceTokens.add(item.getName());
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int selectedIndex = data.getIntValue(index);
            if (selectedIndex < 0 || selectedIndex >= choiceTokens.size())
                throw new WriterException(AbstractDataParser.INVALID_CHOICE_MSG + selectedIndex);

            // skip if disabled
            if (!enabled)
                return super.process(data, index, selectedIndex);
            
            try
            {
                writer.beginObject();
                
                // no indent if only scalars
                if (onlyScalars)
                    writeInline(true);
                
                writer.name(choiceTokens.get(selectedIndex));
                super.process(data, index, selectedIndex);

                writer.endObject();

                // restore indent
                if (onlyScalars)
                    writeInline(false);

                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + " choice " + eltName, e);
            }
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
    
    
    public JsonDataWriterGson()
    {        
    }
    
    
    public JsonDataWriterGson(JsonWriter writer)
    {
        this.writer = writer;
    }


    @Override
    protected void init()
    {
    }


    @Override
    public void setOutput(OutputStream os)
    {
        this.writer = new JsonInliningWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        writer.setIndent(DEFAULT_INDENT);
        writer.setLenient(true);
    }
    
    
    protected void writeInline(boolean writeInline)
    {
        if (writer instanceof JsonInliningWriter)
            ((JsonInliningWriter) writer).writeInline(writeInline);
    }


    @Override
    public void startStream(boolean addWrapper) throws IOException
    {
        this.wrapWithJsonArray = addWrapper;

        // wrap records with array if we're writing multiple ones together
        if (wrapWithJsonArray)
            writer.beginArray();
    }


    @Override
    public void endStream() throws IOException
    {
        if (wrapWithJsonArray)
            writer.endArray();
    }


    @Override
    public void flush() throws IOException
    {
        if (writer != null)
            writer.flush();
    }


    @Override
    public void close() throws IOException
    {
        if (writer != null)
            writer.close();
    }


    @Override
    public void reset()
    {
        super.reset();
    }


    @Override
    public void visit(Boolean comp)
    {
        addToProcessorTree(new BooleanWriter(comp.getName()));
    }


    @Override
    public void visit(Count comp)
    {
        IntegerWriter writer = new IntegerWriter(comp.getName());
        if (comp.isSetId())
            countWriters.put(comp.getId(), writer);
        addToProcessorTree(writer);
    }


    @Override
    public void visit(Quantity comp)
    {
        if (comp.getConstraint() != null && comp.getConstraint().isSetSignificantFigures())
        {
            int sigFigures = comp.getConstraint().getSignificantFigures();
            addToProcessorTree(new RoundingDecimalWriter(comp.getName(), sigFigures));
        }
        else if (comp.getDataType() == DataType.FLOAT)
            addToProcessorTree(new FloatWriter(comp.getName()));
        else
            addToProcessorTree(new DoubleWriter(comp.getName()));
    }


    @Override
    public void visit(Time comp)
    {
        if (!comp.isIsoTime())
        {
            if (comp.getConstraint() != null && comp.getConstraint().isSetSignificantFigures())
            {
                int sigFigures = comp.getConstraint().getSignificantFigures();
                addToProcessorTree(new RoundingDecimalWriter(comp.getName(), sigFigures));
            }
            else
                addToProcessorTree(new DoubleWriter(comp.getName()));
        }
        else
            addToProcessorTree(new IsoDateTimeWriter(comp.getName()));
    }


    @Override
    public void visit(Category comp)
    {
        addToProcessorTree(new StringWriter(comp.getName()));
    }


    @Override
    public void visit(Text comp)
    {
        addToProcessorTree(new StringWriter(comp.getName()));
    }


    @Override
    public void visit(CountRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(QuantityRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(TimeRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(CategoryRange range)
    {
        addToProcessorTree(new RangeWriter(range.getName()));
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(DataRecord rec)
    {
        addToProcessorTree(new RecordWriter(rec.getName(), false));
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
        addToProcessorTree(new RecordWriter(rec.getName(), true));
        for (DataComponent field: rec.getCoordinateList())
            field.accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(DataChoice choice)
    {
        addToProcessorTree(new ChoiceWriter(choice));
        for (DataComponent item: choice.getItemList())
            item.accept(this);
        processorStack.pop();
    }


    @Override
    public void visit(DataArray array)
    {
        ArrayWriter arrayWriter = new ArrayWriter(array.getName());

        if (array.isImplicitSize())
        {
            ArraySizeScanner sizeWriter = new ArraySizeScanner();
            sizeWriter.arrayProcessor = arrayWriter;
        }
        else if (array.isVariableSize())
        {
            // look for size writer
            String refId = array.getArraySizeComponent().getId();
            arrayWriter.sizeWriter = countWriters.get(refId);
        }
        else
            arrayWriter.setArraySize(array.getComponentCount());

        addToProcessorTree(arrayWriter);
        array.getElementType().accept(this);
        processorStack.pop();
    }
}
