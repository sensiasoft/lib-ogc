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
import java.util.HashMap;
import java.util.Map;
import org.vast.data.AbstractDataBlock;
import org.vast.data.DataBlockList;
import org.vast.data.DataBlockMixed;
import org.vast.util.ReaderException;
import com.google.gson.stream.JsonReader;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.RangeComponent;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Implementation of JSON data parser for parsing inline DataArray values.
 * The format is a JSON array of elements where each element is itself a flat
 * array of mixed types.
 * </p><p>
 * This particular implementation is based on Gson JsonReader.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 29, 2023
 */
public class JsonArrayDataParserGson extends JsonDataParserGson
{
    
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
            beginRecordIfRoot(this);
            
            fieldProcessors.get(0).process(data, index++);
            fieldProcessors.get(1).process(data, index++);
            
            endRecordIfRoot(this);
            return index;
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

        public RecordReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            beginRecordIfRoot(this);
            
            for (AtomProcessor p: fieldProcessors)
                index = p.process(data, index);
            
            endRecordIfRoot(this);
            return index;
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
        
        public ArrayReader(String eltName)
        {
            this.eltName = eltName;
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            beginRecordIfRoot(this);
            
            // resize array if var size
            int arraySize = getArraySize();
            if (varSizeArray != null)
                updateArraySize(varSizeArray, arraySize);
            
            // case of array with variable size items
            // e.g. item is itself a variable size array or a choice
            if (varSizeArray != null && varSizeArray.getData() instanceof DataBlockList)
            {
                var arrayData = (DataBlockList)varSizeArray.getData();
                var globalIdx = index;
                for (int i = 0; i < arraySize; i++)
                {
                    var itemData = arrayData.get(i);
                    globalIdx += eltProcessor.process(itemData, 0);
                }
                index = globalIdx;
                data.updateAtomCount();
            }
            else
            {
                for (int i = 0; i < arraySize; i++)
                    index = eltProcessor.process(data, index);
            }
            
            endRecordIfRoot(this);
            return index;
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }
    
    
    protected class ImplicitSizeReader extends ImplicitSizeProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            try
            {
                arraySize = reader.nextInt();
                if (arraySize < 0)
                    throw new NumberFormatException();
                return index;
            }
            catch (NumberFormatException e)
            {
                throw new ReaderException(INVALID_ARRAY_SIZE_MSG + arraySize);
            }
        }
    }


    protected class ChoiceReader extends ChoiceProcessor implements JsonAtomReader
    {
        String eltName;
        DataChoice choice;
        Map<String, Integer> itemIndexes = new HashMap<>();

        public ChoiceReader(DataChoice choice)
        {
            this.eltName = choice.getName();
            this.choice = choice;
            
            int i = 0;
            for (DataComponent item: choice.getItemList())
                itemIndexes.put(item.getName(), i++);
        }

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            beginRecordIfRoot(this);
            
            var itemName = reader.nextString();
            var selectedIndex = itemIndexes.get(itemName);
            if (selectedIndex == null)
                throw new IllegalStateException(INVALID_CHOICE_MSG + itemName + " at " + reader.getPath());
            
            // set selected choice index and corresponding datablock
            data.setIntValue(index++, selectedIndex);
            var selectedData = choice.getComponent(selectedIndex).createDataBlock();
            ((DataBlockMixed)data).setBlock(1, (AbstractDataBlock)selectedData);
            
            // delegate to selected item processor
            index = super.process(data, index, selectedIndex);

            endRecordIfRoot(this);
            return index;
        }

        @Override
        public String getEltName()
        {
            return eltName;
        }
    }
    
    
    public JsonArrayDataParserGson()
    {
    }
    
    
    public JsonArrayDataParserGson(JsonReader reader)
    {
        super(reader);
    }
    
    
    protected void beginRecordIfRoot(AtomProcessor processor) throws IOException
    {
        if (processor == rootProcessor)
            reader.beginArray();
    }
    
    
    protected void endRecordIfRoot(AtomProcessor processor) throws IOException
    {
        if (processor == rootProcessor)
            reader.endArray();
    }
    
    
    @Override
    protected AtomProcessor getRangeProcessor(RangeComponent range)
    {
        return new RangeReader(range.getName());
    }


    @Override
    protected RecordProcessor getRecordProcessor(DataRecord record)
    {
        return new RecordReader(record.getName());
    }


    @Override
    protected RecordProcessor getVectorProcessor(Vector vect)
    {
        return new RecordReader(vect.getName());
    }


    @Override
    protected ChoiceProcessor getChoiceProcessor(DataChoice choice)
    {
        return new ChoiceReader(choice);
    }
    
    
    @Override
    protected ArrayProcessor getArrayProcessor(DataArray array)
    {
        return new ArrayReader(array.getName());
    }
    
    
    @Override
    protected ImplicitSizeProcessor getImplicitSizeProcessor(DataArray array)
    {
        return new ImplicitSizeReader();
    }
}
