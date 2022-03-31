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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataInputExt;
import org.vast.data.AbstractDataBlock;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.DataBlockMixed;
import org.vast.swe.DataInputStreamBI;
import org.vast.swe.DataInputStreamLI;
import org.vast.swe.SWEHelper;
import org.vast.util.Asserts;
import org.vast.util.ReaderException;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.ByteOrder;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.ScalarComponent;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.Time;


/**
 * <p>
 * New implementation of binary data parser with better efficiency since the 
 * write tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p>
 *
 * @author Alex Robin
 * @since Mar 28, 2022
 */
public class BinaryDataParser extends AbstractDataParser
{
    protected DataInputExt dataInput;
    Map<String, ArraySizeSupplier> countParsers = new HashMap<>();
    
    
    protected abstract class ValueParser extends BaseProcessor
    {
        public abstract void readValue(DataBlock data, int index) throws IOException;

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            if (enabled)
                readValue(data, index);
            return ++index;
        }
    }
    
    
    protected class BooleanParser extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            boolean val = dataInput.readBoolean();
            data.setBooleanValue(index, val);
        }
    }
    
    
    protected class ByteParser extends ValueParser implements ArraySizeSupplier
    {
        byte val;
        
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            val = dataInput.readByte();
            data.setByteValue(index, val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class UByteParser extends ValueParser implements ArraySizeSupplier
    {
        short val;
        
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            val = (short)dataInput.readUnsignedByte();
            data.setShortValue(index, val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class ShortParser extends ValueParser implements ArraySizeSupplier
    {
        short val;
        
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            val = dataInput.readShort();
            data.setShortValue(index, val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class UShortParser extends ValueParser implements ArraySizeSupplier
    {
        int val;
        
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            val = dataInput.readUnsignedShort();
            data.setIntValue(index, val);
            
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class IntParser extends ValueParser implements ArraySizeSupplier
    {
        int val;
        
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            val = dataInput.readInt();
            data.setIntValue(index, val);
        }

        @Override
        public int getArraySize()
        {
            return val;
        }
    }
    
    
    protected class UIntParser extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            long val = dataInput.readUnsignedInt();
            data.setLongValue(index, val);
        }
    }
    
    
    protected class LongParser extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            long val = dataInput.readLong();
            data.setLongValue(index, val);
        }
    }
    
    
    protected class ULongParser extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            long val = dataInput.readUnsignedLong();
            data.setLongValue(index, val);
        }
    }
    
    
    protected class DoubleParser extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            double val = dataInput.readDouble();
            data.setDoubleValue(index, val);
        }
    }
    
    
    protected class FloatParser extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            float val = dataInput.readFloat();
            data.setFloatValue(index, val);
        }
    }
    
    
    protected class StringParserASCII extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            String val = dataInput.readASCII();
            data.setStringValue(index, val);
        }
    }
    
    
    protected class StringParserUTF extends ValueParser
    {
        @Override
        public void readValue(DataBlock data, int index) throws IOException
        {
            String val = dataInput.readUTF();
            data.setStringValue(index, val);
        }
    }
    
    
    protected class ArrayParser extends ArrayProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            // resize array if var size
            int arraySize = getArraySize();
            if (varSizeArray != null)
                updateArraySize(varSizeArray, arraySize);
            
            return super.process(data, index);
        }
    }
    
    
    protected class ImplicitSizeParser extends ImplicitSizeProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            super.process(data, index);
            arraySize = dataInput.readInt();
            if (arraySize < 0)
                throw new ReaderException(INVALID_ARRAY_SIZE_MSG + arraySize);
            return index;
        }
    }
    
    
    protected class ChoiceTokenParser extends ChoiceProcessor
    {
        DataChoice choice;
        int maxChoiceIdx;
        
        public ChoiceTokenParser(DataChoice choice)
        {
            this.choice = choice;
            maxChoiceIdx = choice.getItemList().size() - 1;
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            int selectedIndex = dataInput.readInt();
            if (selectedIndex < 0 || selectedIndex >= maxChoiceIdx)
                throw new IOException(AbstractDataParser.INVALID_CHOICE_MSG + selectedIndex);
            
            // set selected choice index and corresponding datablock
            data.setIntValue(index++, selectedIndex);
            var selectedData = choice.getComponent(selectedIndex).createDataBlock();
            ((DataBlockMixed)data).setBlock(1, (AbstractDataBlock)selectedData);
            
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
    public void setInput(InputStream is)
    {
        // create right data output stream
        if (((BinaryEncoding)dataEncoding).getByteOrder() == ByteOrder.LITTLE_ENDIAN)
            dataInput = new DataInputStreamLI(is);
        else
            dataInput = new DataInputStreamBI(is);
    }
    

    @Override
    public void close() throws IOException
    {
        if (dataInput != null)
            ((InputStream)dataInput).close();
    }
    
    
    protected BaseProcessor getAtomParser(ScalarComponent component)
    {
        BinaryMember enc = ((AbstractDataComponentImpl)component).getEncodingInfo();
        DataType dataType = ((BinaryComponentImpl)enc).getCdmDataType();
        
        switch (dataType)
        {
            case BOOLEAN:
                return new BooleanParser();
            
            case BYTE:
                return new ByteParser();
                
            case UBYTE:
                return new UByteParser();
                
            case SHORT:
                return new ShortParser();
                
            case USHORT:
                return new UShortParser();
                
            case INT:
                return new IntParser();
                
            case UINT:
                return new UIntParser();
                
            case LONG:
                return new LongParser();
                
            case ULONG:
                return new ULongParser();
                
            case FLOAT:
                return new FloatParser();
                
            case DOUBLE:
                return new DoubleParser();
                
            case UTF_STRING:
                return new StringParserUTF();
                
            case ASCII_STRING:
                return new StringParserASCII();
                
            default:
                throw new IllegalStateException("Unsupported datatype " + dataType);
        }
    }
    
    
    @Override
    public void visit(Boolean comp)
    {
        addToProcessorTree(getAtomParser(comp));
    }
    
    
    @Override
    public void visit(Count comp)
    {
        BaseProcessor Parser = getAtomParser(comp);
        if (comp.isSetId())
        {
            Asserts.checkState(Parser instanceof ArraySizeSupplier, "Unsupported datatype for array size");
            countParsers.put(comp.getId(), (ArraySizeSupplier)Parser);
        }
        addToProcessorTree(Parser);
    }
    
    
    @Override
    public void visit(Quantity comp)
    {
        addToProcessorTree(getAtomParser(comp));
    }
    
    
    @Override
    public void visit(Time comp)
    {
        addToProcessorTree(getAtomParser(comp));
    }
    
    
    @Override
    public void visit(Category comp)
    {
        addToProcessorTree(getAtomParser(comp));
    }
    
    
    @Override
    public void visit(Text comp)
    {
        addToProcessorTree(getAtomParser(comp));
    }
    
    
    @Override
    protected ChoiceProcessor getChoiceProcessor(DataChoice choice)
    {
        return new ChoiceTokenParser(choice);
    }


    @Override
    protected ArrayProcessor getArrayProcessor(DataArray array)
    {
        return new ArrayParser();
    }
    
    
    @Override
    protected ImplicitSizeProcessor getImplicitSizeProcessor(DataArray array)
    {
        return new ImplicitSizeParser();
    }
    
    
    @Override
    protected ArraySizeSupplier getArraySizeSupplier(String refId)
    {
        return countParsers.get(refId);
    }


    @Override
    protected boolean moreData() throws IOException
    {
        return ((InputStream)dataInput).available() > 0;
    }
}
