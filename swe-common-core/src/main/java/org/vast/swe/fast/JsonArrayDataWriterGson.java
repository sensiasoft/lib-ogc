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
import java.util.ArrayList;
import org.vast.util.WriterException;
import com.google.gson.stream.JsonWriter;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.RangeComponent;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Implementation of JSON data writer for writing array data inline, as a 
 * JSON array of elements where each element is itself a flat array of 
 * mixed types.
 * </p><p>
 * This particular implementation is based on Gson JsonWriter.
 * </p>
 *
 * @author Alex Robin
 * @since Sep 29, 2023
 */
public class JsonArrayDataWriterGson extends JsonDataWriterGson
{

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
                beginRecordIfRoot(this);
                
                fieldProcessors.get(0).process(data, index++);
                fieldProcessors.get(1).process(data, index++);
                
                endRecordIfRoot(this);
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
                beginRecordIfRoot(this);
                
                for (AtomProcessor p: fieldProcessors)
                    index = p.process(data, index);
                
                endRecordIfRoot(this);
                return index;
            }
            catch (IOException e)
            {
                throw new WriterException(JSON_ERROR + " record " + eltName, e);
            }
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
            // skip if disabled
            if (!enabled)
                return super.process(data, index);
            
            try
            {
                beginRecordIfRoot(this);
            
                // write implicit size if needed
                if (varSizeArray != null && varSizeArray.isImplicitSize())
                    writer.value(varSizeArray.getComponentCount());
                
                for (int i = 0; i < getArraySize(); i++)
                    index = eltProcessor.process(data, index);
                
                endRecordIfRoot(this);
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
            index++;
            
            // skip if disabled
            if (!enabled)
                return super.process(data, index, selectedIndex);
            
            try
            {
                beginRecordIfRoot(this);
                
                writer.name(choiceTokens.get(selectedIndex));
                index = super.process(data, index, selectedIndex);
                
                endRecordIfRoot(this);
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
    
    
    public JsonArrayDataWriterGson()
    {
    }
    
    
    public JsonArrayDataWriterGson(JsonWriter writer)
    {
        super(writer);
    }
    
    
    protected void beginRecordIfRoot(AtomProcessor processor) throws IOException
    {
        if (processor == rootProcessor)
        {
            writer.beginArray();
            writeInline(true);
        }
    }
    
    
    protected void endRecordIfRoot(AtomProcessor processor) throws IOException
    {
        if (processor == rootProcessor)
        {
            writer.endArray();
            writeInline(false);
        }
    }
    
    
    @Override
    protected AtomProcessor getRangeProcessor(RangeComponent range)
    {
        return new RangeWriter(range.getName());
    }


    @Override
    protected RecordProcessor getRecordProcessor(DataRecord record)
    {
        return new RecordWriter(record.getName(), false);
    }


    @Override
    protected RecordProcessor getVectorProcessor(Vector vect)
    {
        return new RecordWriter(vect.getName(), true);
    }


    @Override
    protected ChoiceProcessor getChoiceProcessor(DataChoice choice)
    {
        return new ChoiceWriter(choice);
    }
    
    
    @Override
    protected ArrayProcessor getArrayProcessor(DataArray array)
    {
        return new ArrayWriter(array.getName());
    }
    
    
    @Override
    protected ImplicitSizeProcessor getImplicitSizeProcessor(DataArray array)
    {
        return new ImplicitSizeProcessor();
    }
}
