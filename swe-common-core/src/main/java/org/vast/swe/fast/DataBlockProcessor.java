/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2016 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import org.vast.swe.IComponentFilter;
import org.vast.util.Asserts;
import net.opengis.swe.v20.CategoryRange;
import net.opengis.swe.v20.CountRange;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataComponentVisitor;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.QuantityRange;
import net.opengis.swe.v20.RangeComponent;
import net.opengis.swe.v20.TimeRange;
import net.opengis.swe.v20.Vector;


/**
 * <p>
 * Base class for all data block level processors.<br/>
 * The processor tree is pre-configured so that the processing can occur very
 * fast on many data blocks matching the same components structure.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 7, 2016
 */
public abstract class DataBlockProcessor implements DataComponentVisitor
{
    DataComponent dataComponents;
    IComponentFilter filter;
    AtomProcessor rootProcessor;
    ArrayDeque<AtomProcessor> processorStack = new ArrayDeque<>();
    boolean enableSubTree = true;
    boolean processorTreeReady;
    boolean hasVarSizeArray = false;
    
    
    public interface AtomProcessor
    {
        public void setEnabled(boolean enabled);
        public boolean isEnabled();
        public int process(DataBlock data, int index) throws IOException;
    }
    
    
    public interface CompositeProcessor extends AtomProcessor
    {
        public void add(AtomProcessor parser);
    }
    
    
    public abstract static class BaseProcessor implements AtomProcessor
    {
        boolean enabled = true;
        
        @Override
        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }
        
        @Override
        public boolean isEnabled()
        {
            return this.enabled;
        }
    }
    
    
    public static class RecordProcessor extends BaseProcessor implements CompositeProcessor
    {
        List<AtomProcessor> fieldProcessors = new ArrayList<>();
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            for (AtomProcessor p: fieldProcessors)
                index = p.process(data, index);
            return index;
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            fieldProcessors.add(processor);
        }
    }
    
    
    public interface ArraySizeSupplier
    {
        int getArraySize();
    }
    
    
    protected class ImplicitSizeProcessor extends BaseProcessor implements ArraySizeSupplier
    {
        DataArray varSizeArray;
        int arraySize;

        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            dataComponents.setData(data);
            arraySize = varSizeArray.getComponentCount();
            return index;
        }

        @Override
        public int getArraySize()
        {
            return arraySize;
        }
    }
    
    
    public static class ArrayProcessor extends BaseProcessor implements CompositeProcessor
    {
        ArraySizeSupplier sizeSupplier;
        AtomProcessor eltProcessor;
        DataArray varSizeArray;
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            for (int i = 0; i < getArraySize(); i++)
                index = eltProcessor.process(data, index);
            return index;
        }
        
        public int getArraySize()
        {
            return sizeSupplier.getArraySize();
        }
        
        public void setArraySizeSupplier(ArraySizeSupplier sizeProvider)
        {
            this.sizeSupplier = sizeProvider;
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            this.eltProcessor = processor;
        }
    }
    
    
    public abstract static class ChoiceProcessor extends BaseProcessor implements CompositeProcessor
    {
        List<AtomProcessor> itemProcessors = new ArrayList<>();
        
        public int process(DataBlock data, int index, int selectedIndex) throws IOException
        {
            return itemProcessors.get(selectedIndex).process(data, index);
        }
        
        @Override
        public void add(AtomProcessor processor)
        {
            itemProcessors.add(processor);
        }
    }
    
    
    protected abstract void init() throws IOException;
    
    
    protected void addToProcessorTree(AtomProcessor processor)
    {
        // add to parent processor or root
        if (!processorStack.isEmpty())
        {
            CompositeProcessor parent = (CompositeProcessor)processorStack.peek();
            parent.add(processor);
        }
        else
            rootProcessor = processor;
        
        // enable/disable processor and parents as needed
        setEnabled(processor);
        
        if (processor instanceof CompositeProcessor)
            processorStack.push(processor);
    }
    
    
    protected void setEnabled(AtomProcessor processor)
    {
        processor.setEnabled(enableSubTree);
        
        // if enabled, also enable all parents
        if (enableSubTree)
        {
            for (AtomProcessor parent: processorStack)
                parent.setEnabled(true);
        }
    }
    
    
    protected void checkEnabled(DataComponent comp)
    {
        // do nothing if we're already enabled
        if (enableSubTree)
            return;
        
        if (filter == null || filter.accept(comp))
            enableSubTree = true;
    }
    
    
    @Override
    public void visit(CountRange range)
    {
        visitRange(range);
    }
    
    
    @Override
    public void visit(QuantityRange range)
    {
        visitRange(range);
    }
    
    
    @Override
    public void visit(TimeRange range)
    {
        visitRange(range);
    }
    
    
    @Override
    public void visit(CategoryRange range)
    {
        visitRange(range);
    }
    
    
    protected void visitRange(RangeComponent range)
    {
        AtomProcessor rangeProcessor = getRangeProcessor(range);
        if (rangeProcessor != null)
            addToProcessorTree(rangeProcessor);
        
        range.getComponent(0).accept(this);
        range.getComponent(1).accept(this);
        
        if (rangeProcessor != null)
            processorStack.pop();
    }
    
    
    protected AtomProcessor getRangeProcessor(RangeComponent range)
    {
        return null;
    }
    
    
    @Override
    public void visit(DataRecord record)
    {
        addToProcessorTree(getRecordProcessor(record));
        
        for (DataComponent field: record.getFieldList())
        {
            boolean saveEnabled = enableSubTree;
            checkEnabled(field);
            field.accept(this);
            enableSubTree = saveEnabled; // reset flag
        }
        
        processorStack.pop();
    }
    
    
    protected RecordProcessor getRecordProcessor(DataRecord record)
    {
        return new RecordProcessor();
    }
    
    
    @Override
    public void visit(Vector vect)
    {
        addToProcessorTree(getVectorProcessor(vect));
        
        for (DataComponent coord: vect.getCoordinateList())
        {
            boolean saveEnabled = enableSubTree;
            checkEnabled(coord);
            coord.accept(this);
            enableSubTree = saveEnabled; // reset flag
        }
        
        processorStack.pop();
    }
    
    
    protected RecordProcessor getVectorProcessor(Vector vect)
    {
        return new RecordProcessor();
    }
    
    
    @Override
    public void visit(DataChoice choice)
    {
        addToProcessorTree(getChoiceProcessor(choice));
        for (DataComponent item: choice.getItemList())
            item.accept(this);
        processorStack.pop();
    }
    
    
    protected abstract ChoiceProcessor getChoiceProcessor(DataChoice choice);
    
    
    @Override
    public void visit(DataArray array)
    {
        ArrayProcessor arrayProcessor = getArrayProcessor(array);

        if (array.isImplicitSize())
        {
            ImplicitSizeProcessor sizeProcessor = getImplicitSizeProcessor(array);
            sizeProcessor.varSizeArray = array;
            addToProcessorTree(sizeProcessor);
            arrayProcessor.setArraySizeSupplier(sizeProcessor);
            arrayProcessor.varSizeArray = array;
            hasVarSizeArray = true;
        }
        else if (array.isVariableSize())
        {
            // look for size writer
            String refId = array.getArraySizeComponent().getId();
            ArraySizeSupplier sizeSupplier = getArraySizeSupplier(refId);
            Asserts.checkState(sizeSupplier != null, "Missing array size supplier");
            arrayProcessor.setArraySizeSupplier(sizeSupplier);
            arrayProcessor.varSizeArray = array;
            hasVarSizeArray = true;
        }
        else
        {
            final int arraySize = array.getComponentCount();
            arrayProcessor.setArraySizeSupplier(() -> arraySize);
        }
        
        addToProcessorTree(arrayProcessor);
        array.getElementType().accept(this);
        processorStack.pop();
    }
    
    
    protected ArrayProcessor getArrayProcessor(DataArray array)
    {
        return new ArrayProcessor();
    }
    
    
    protected abstract ImplicitSizeProcessor getImplicitSizeProcessor(DataArray array);
    
    
    protected abstract ArraySizeSupplier getArraySizeSupplier(String refId);
    
    
    public void setDataComponents(DataComponent components)
    {
        this.dataComponents = components.copy();
        this.processorTreeReady = false;
    }


    public DataComponent getDataComponents()
    {
        return this.dataComponents;
    }
    
    
    public void setDataComponentFilter(IComponentFilter filter)
    {
        this.filter = filter;
        this.enableSubTree = false;
    }
}
