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
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.vast.swe.SWEDataTypeUtils;
import org.vast.util.DateTimeFormat;
import org.vast.util.WriterException;
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
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public class JsonDataWriter extends AbstractDataWriter
{
    static final String JSON_ERROR = "Error writing JSON stream for ";
    static final String INDENT = "  ";
    static final Map<Character, String> ESCAPED_CHARS = new HashMap<>();
    
    static {
        ESCAPED_CHARS.put('\n', "\\n");
        ESCAPED_CHARS.put('\t', "\\t");
        ESCAPED_CHARS.put('\"', "\\\"");
        ESCAPED_CHARS.put('\\', "\\\\");
    }

    protected Writer writer;
    protected NullWriter nullWriter = new NullWriter();
    protected int depth;
    boolean wrapWithJsonArray;
    boolean firstBlock = true;
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
                writeValue(data, index);
                return ++index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " value", e);
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
            writer.write(java.lang.Boolean.toString(val));
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
            writer.write(Integer.toString(val));
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
                writer.write("\"NaN\"");
            else if (val == Double.POSITIVE_INFINITY)
                writer.write("\"+INF\"");
            else if (val == Double.NEGATIVE_INFINITY)
                writer.write("\"-INF\"");
            else
                writer.write(Double.toString(val));
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
                writer.write("\"NaN\"");
            else if (val == Float.POSITIVE_INFINITY)
                writer.write("\"+INF\"");
            else if (val == Float.NEGATIVE_INFINITY)
                writer.write("\"-INF\"");
            else
                writer.write(Float.toString(val));
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
                writer.write("\"NaN\"");
            else if (val == Double.POSITIVE_INFINITY)
                writer.write("\"+INF\"");
            else if (val == Double.NEGATIVE_INFINITY)
                writer.write("\"-INF\"");
            else
            {
                BigDecimal bd = BigDecimal.valueOf(val);
                bd = bd.setScale(scale, RoundingMode.HALF_UP).stripTrailingZeros();
                writer.write(bd.toPlainString());
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
            writer.write('"');
            if (Double.isNaN(val) || Double.isInfinite(val))
                writer.write(SWEDataTypeUtils.getDoubleOrInfAsString(val));
            else
                writer.write(timeFormat.formatIso(val, 0));
            writer.write('"');
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

            if (val != null)
            {
                writer.write('"');
                writer.write(escape(val), 0, pos);
                writer.write('"');
            }
            else
                writer.write("null");
        }
        
        protected char[] escape(String val)
        {
            pos = 0;
            if (buf.length < 2*val.length())
                buf = Arrays.copyOf(buf, (2*val.length()/BUF_SIZE_INCREMENT+1)*BUF_SIZE_INCREMENT);
            
            for (int i = 0; i < val.length(); i++)
            {
                char c = val.charAt(i);
                String escaped = ESCAPED_CHARS.get(c);
                
                if (escaped != null)
                {
                    for (int j = 0; j < escaped.length(); j++)
                        buf[pos++] = escaped.charAt(j);
                }
                else
                    buf[pos++] = c;
            }
            
            return buf;
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
                writer.write('[');
                fieldProcessors.get(0).process(data, index++);
                writer.write(", ");
                fieldProcessors.get(1).process(data, index++);
                writer.write(']');
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " range", e);
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
        boolean onlyScalars = true;

        public RecordWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                writer.write('{');

                depth++;
                int i = 0;
                for (AtomProcessor p: fieldProcessors)
                {
                    // switch to null writer if this field should be skipped
                    Writer oldWriter = writer;
                    if (!p.isEnabled())
                    {
                        writer = nullWriter;
                        i--;
                    }
                    
                    // insert separator
                    if (i > 0)
                        writer.write(',');

                    // indent only if child is complex
                    if (!onlyScalars)
                    {
                        writer.write('\n');
                        indent();
                    }
                    else if (i > 0)
                        writer.write(' ');

                    writer.append('"').append(((JsonAtomWriter)p).getEltName()).append('"');
                    writer.write(": ");
                    index = p.process(data, index);
                    i++;

                    writer = oldWriter; // restore old writer
                }

                depth--;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }
                writer.write('}');

                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " record", e);
            }
        }

        @Override
        public void add(AtomProcessor processor)
        {
            fieldProcessors.add(processor);
            if (!(processor instanceof ValueWriter))
                onlyScalars = false;
        }

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

        public ArrayWriter(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                // retrieve variable array size
                int arraySize = sizeSupplier.getArraySize();
                
                writer.write('[');

                depth++;
                for (int i = 0; i < arraySize; i++)
                {
                    // insert separator
                    if (i > 0)
                        writer.write(',');

                    // indent only if child is complex
                    if (!onlyScalars)
                    {
                        writer.write('\n');
                        indent();
                    }
                    else if (i > 0)
                        writer.write(' ');

                    index = eltProcessor.process(data, index);
                }

                depth--;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }
                writer.write(']');

                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " array", e);
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

            try
            {
                writer.write('{');

                depth++;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }

                writer.append('"').append(choiceTokens.get(selectedIndex)).append('"');
                writer.write(": ");
                index = super.process(data, ++index, selectedIndex);

                depth--;
                if (!onlyScalars)
                {
                    writer.write('\n');
                    indent();
                }
                writer.write('}');

                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + eltName + " choice", e);
            }
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }


    protected void indent() throws IOException
    {
        for (int i = 0; i < depth; i++)
            writer.write(INDENT);
    }


    @Override
    protected void init()
    {
    }


    @Override
    public void setOutput(OutputStream os)
    {
        this.writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
    }


    @Override
    public void write(DataBlock data) throws IOException
    {
        if (!firstBlock)
            writer.write(",\n");

        indent();
        super.write(data);

        if (wrapWithJsonArray)
            firstBlock = false;
    }


    @Override
    public void startStream(boolean addWrapper) throws IOException
    {
        this.wrapWithJsonArray = addWrapper;

        // wrap records with array if we're writing multiple ones together
        if (wrapWithJsonArray)
        {
            writer.write("[\n");
            depth++;
        }
    }


    @Override
    public void endStream() throws IOException
    {
        if (wrapWithJsonArray)
        {
            writer.write("\n]");
            flush();
        }
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
        firstBlock = true;
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
        addToProcessorTree(new RecordWriter(rec.getName()));
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
        addToProcessorTree(new RecordWriter(rec.getName()));
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
            ArraySizeScanner sizeScanner = new ArraySizeScanner();
            addToProcessorTree(sizeScanner);
            arrayWriter.setArraySizeSupplier(sizeScanner);
        }
        else if (array.isVariableSize())
        {
            // look for size writer
            String refId = array.getArraySizeComponent().getId();
            IntegerWriter sizeWriter = countWriters.get(refId);
            arrayWriter.setArraySizeSupplier(() -> sizeWriter.val);
        }
        else
        {
            final int arraySize = array.getComponentCount();
            arrayWriter.setArraySizeSupplier(() -> arraySize);
        }
        
        addToProcessorTree(arrayWriter);
        array.getElementType().accept(this);
        processorStack.pop();
    }
}
