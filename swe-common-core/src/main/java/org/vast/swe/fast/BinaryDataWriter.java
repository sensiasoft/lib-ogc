/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2022 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataOutputExt;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.swe.DataOutputStreamBI;
import org.vast.swe.DataOutputStreamLI;
import org.vast.swe.SWEHelper;
import org.vast.util.Asserts;
import org.vast.util.WriterException;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;


/**
 * <p>
 * New implementation of binary data writer with better efficiency since the 
 * write tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p>
 *
 * @author Alex Robin
 * @since Mar 28, 2022
 */
public class BinaryDataWriter extends AbstractDataWriter
{
    protected DataOutputExt dataOutput;
    Map<String, ArraySizeSupplier> countWriters = new HashMap<>();
    
    
    protected abstract class ValueWriter extends BaseProcessor
    {
        public abstract void writeValue(DataBlock data, int index) throws IOException;

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            if (enabled)
                writeValue(data, index);
            return ++index;
        }
    }
    
    
    protected class BooleanWriter extends ValueWriter
    {
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            boolean val = data.getBooleanValue(index);
            dataOutput.writeBoolean(val);
        }
    }
    
    
    protected class ByteWriter extends ValueWriter implements ArraySizeSupplier
    {
        byte val;
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            val = data.getByteValue(index);
            dataOutput.writeByte(val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class UByteWriter extends ValueWriter implements ArraySizeSupplier
    {
        byte val;
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            byte val = data.getByteValue(index);
            dataOutput.writeUnsignedByte(val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class ShortWriter extends ValueWriter implements ArraySizeSupplier
    {
        short val;
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            val = data.getShortValue(index);
            dataOutput.writeShort(val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class UShortWriter extends ValueWriter implements ArraySizeSupplier
    {
        short val;
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            val = data.getShortValue(index);
            dataOutput.writeUnsignedShort(val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class IntWriter extends ValueWriter implements ArraySizeSupplier
    {
        int val;
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            val = data.getIntValue(index);
            dataOutput.writeInt(val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class UIntWriter extends ValueWriter implements ArraySizeSupplier
    {
        int val;
        
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            val = data.getIntValue(index);
            dataOutput.writeUnsignedInt(val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class LongWriter extends ValueWriter
    {
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            long val = data.getLongValue(index);
            dataOutput.writeLong(val);
        }
    }
    
    
    protected class ULongWriter extends ValueWriter
    {
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            long val = data.getLongValue(index);
            dataOutput.writeUnsignedLong(val);
        }
    }
    
    
    protected class DoubleWriter extends ValueWriter
    {
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            double val = data.getDoubleValue(index);
            dataOutput.writeDouble(val);
        }
    }
    
    
    protected class FloatWriter extends ValueWriter
    {
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            float val = data.getFloatValue(index);
            dataOutput.writeFloat(val);
        }
    }
    
    
    protected class StringWriterASCII extends ValueWriter
    {
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            String val = data.getStringValue(index);
            dataOutput.writeASCII(val);
        }
    }
    
    
    protected class StringWriterUTF extends ValueWriter
    {
        @Override
        public void writeValue(DataBlock data, int index) throws IOException
        {
            String val = data.getStringValue(index);
            dataOutput.writeUTF(val);
        }
    }
    
    
    protected class ImplicitSizeWriter extends ImplicitSizeProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            super.process(data, index);
            dataOutput.writeInt(arraySize);
            return index;
        }
    }
    
    
    protected class ChoiceTokenWriter extends ChoiceProcessor
    {
        ArrayList<String> choiceTokens;
        
        public ChoiceTokenWriter(DataChoice choice)
        {
            choiceTokens = new ArrayList<String>(choice.getNumItems());
            for (DataComponent item: choice.getItemList())
                choiceTokens.add(item.getName());
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int selectedIndex = data.getIntValue(index);
            if (selectedIndex < 0 || selectedIndex >= choiceTokens.size())
                throw new WriterException(AbstractDataParser.INVALID_CHOICE_MSG + selectedIndex);
            
            if (enabled)
                dataOutput.writeInt(selectedIndex);
            
            return super.process(data, ++index, selectedIndex);
        }
    }
    
    
    protected void init() throws IOException
    {
        try
        {
            SWEHelper.assignBinaryEncoding(dataComponents, (BinaryEncoding)dataEncoding);
        }
        catch (CDMException e)
        {
            throw new IOException(e.getMessage(), e.getCause());
        }
    }
    
    
    @Override
    public void setOutput(OutputStream os)
    {
        // create right data output stream
        if (((BinaryEncoding)dataEncoding).getByteOrder() == ByteOrder.LITTLE_ENDIAN)
            dataOutput = new DataOutputStreamLI(os);
        else
            dataOutput = new DataOutputStreamBI(os);
    }
    

    @Override
    public void flush() throws IOException
    {
        if (dataOutput != null)
            dataOutput.flush();
    }
    

    @Override
    public void close() throws IOException
    {
        if (dataOutput != null)
            dataOutput.close();
    }
    
    
    protected BaseProcessor getAtomWriter(ScalarComponent component)
    {
        BinaryMember enc = ((AbstractDataComponentImpl)component).getEncodingInfo();
        DataType dataType = ((BinaryComponentImpl)enc).getCdmDataType();
        
        switch (dataType)
        {
            case BOOLEAN:
                return new BooleanWriter();
            
            case BYTE:
                return new ByteWriter();
                
            case UBYTE:
                return new UByteWriter();
                
            case SHORT:
                return new ShortWriter();
                
            case USHORT:
                return new UShortWriter();
                
            case INT:
                return new IntWriter();
                
            case UINT:
                return new UIntWriter();
                
            case LONG:
                return new LongWriter();
                
            case ULONG:
                return new ULongWriter();
                
            case FLOAT:
                return new FloatWriter();
                
            case DOUBLE:
                return new DoubleWriter();
                
            case UTF_STRING:
                return new StringWriterUTF();
                
            case ASCII_STRING:
                return new StringWriterASCII();
                
            default:
                throw new IllegalStateException("Unsupported datatype " + dataType);
        }
    }
    
    
    @Override
    public void visit(Boolean comp)
    {
        addToProcessorTree(getAtomWriter(comp));
    }
    
    
    @Override
    public void visit(Count comp)
    {
        BaseProcessor writer = getAtomWriter(comp);
        if (comp.isSetId())
        {
            Asserts.checkState(writer instanceof ArraySizeSupplier, "Unsupported datatype for array size");
            countWriters.put(comp.getId(), (ArraySizeSupplier)writer);
        }
        addToProcessorTree(writer);
    }
    
    
    @Override
    public void visit(Quantity comp)
    {
        addToProcessorTree(getAtomWriter(comp));
    }
    
    
    @Override
    public void visit(Time comp)
    {
        addToProcessorTree(getAtomWriter(comp));
    }
    
    
    @Override
    public void visit(Category comp)
    {
        addToProcessorTree(getAtomWriter(comp));
    }
    
    
    @Override
    public void visit(Text comp)
    {
        addToProcessorTree(getAtomWriter(comp));
    }
    
    
    @Override
    protected ChoiceProcessor getChoiceProcessor(DataChoice choice)
    {
        return new ChoiceTokenWriter(choice);
    }
    
    
    @Override
    protected ImplicitSizeProcessor getImplicitSizeProcessor(DataArray array)
    {
        return new ImplicitSizeWriter();
    }
    
    
    @Override
    protected ArraySizeSupplier getArraySizeSupplier(String refId)
    {
        return countWriters.get(refId);
    }
}
