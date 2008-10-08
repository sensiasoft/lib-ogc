/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.util.Stack;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.cdm.common.DataHandler;
import org.vast.cdm.common.DataType;
import org.vast.cdm.common.ErrorHandler;
import org.vast.cdm.common.RawDataHandler;
import org.vast.sweCommon.SweConstants;


public abstract class DataIterator
{
	protected DataHandler dataHandler;
	protected RawDataHandler rawHandler;
	protected ErrorHandler errorHandler;
    protected DataArray parentArray;
    protected int parentArrayIndex;
	protected DataComponent dataComponents;
	protected DataEncoding dataEncoding;
	protected Stack<Record> componentStack;
	protected Record currentRecord;
    protected boolean newBlock = true;
	protected boolean endOfArray = false;
	protected boolean parsing = true;
	protected DataValue selectedValue = new DataValue(SweConstants.SELECTED_ITEM_NAME, DataType.INT); // for holding choice selection index
	protected DataValue sizeValue = new DataValue(SweConstants.ELT_COUNT_NAME, DataType.INT); // for holding implicit array size
	
    
	protected class Record
    {
        public DataComponent component;
        public int index;
        public int count;

        public Record(DataComponent component)
        {
            this.component = component;
            this.count = component.getComponentCount();
            this.index = 0;
        }
    }
    
    
	public DataIterator(boolean parsing)
	{
		this.parsing = parsing;
		this.componentStack = new Stack<Record>();
	}
	
	
	protected abstract void processAtom(DataValue scalarInfo) throws CDMException;
	
	
	/**
	 * Process an aggregate component
	 * @param scalarInfo
	 * @return true if children should be processed, false otherwise
	 * @throws CDMException
	 */
	protected abstract boolean processBlock(DataComponent blockInfo) throws CDMException;
	
	
	/**
	 * TODO nextInfo method description
	 * @return
	 */
	protected void processNextElement() throws CDMException
	{
        // reset iterator if new block is starting
		if (newBlock)
        {
            this.reset();
            newBlock = false;
        }
        
        // send beginning of data event
    	if (dataHandler != null && componentStack.isEmpty() && currentRecord.index == 0)
    		dataHandler.startData(dataComponents);
    	
        // retrieve current component
    	DataComponent next = currentRecord.component;
    	
        // if child is not a DataValue, go in !!
        if (!(next instanceof DataValue))
        {
        	// send start block event
        	if (dataHandler != null)
        		dataHandler.startDataBlock(currentRecord.component);
        	
        	// some reader/writer may write the whole block at once
        	// and will then return false here to skip children
        	boolean processChildren = processBlock(next);
        	
        	if (processChildren)
        	{
        		// push this aggregate record to the stack
	    		componentStack.push(currentRecord);
	    		
        		// case of variable array size
	    		if ((next instanceof DataArray) && ((DataArray)next).isVariableSize())
	    		{
	    			if (((DataArray)next).getSizeComponent().getParent() == null)
	    			{
	    				// read implicit array size (when parsing)
	    				if (parsing)
	        			{
	    					processAtom(sizeValue);
	            			int newSize = sizeValue.getData().getIntValue();
	            			
	            			// resize array according to size read!
	            			((DataArray)next).updateSize(newSize);
	            			currentRecord.count = newSize;
	        			}
	        		
	        			// write array size
	        			else
	        			{
	        				sizeValue.getData().setIntValue(((DataArray)next).getComponentCount());
	        				processAtom(sizeValue);
	        			}
	    			}        		
	    			else if (parsing)
	    				((DataArray)next).updateSize();
	    		}
	    	
	    		// case of choice
	    		else if (next instanceof DataChoice)
	    		{
	    			// read implicit choice index (when parsing)
	    			if (parsing)
	    			{
	    				processAtom(selectedValue);
	    				((DataChoice)next).setSelected(selectedValue.getData().getIntValue());
	    			}
	    		
	    			// write choice index
	    			else
	    			{
	    				selectedValue.getData().setIntValue(((DataChoice)next).getSelected());
	    				processAtom(selectedValue);
	    			}
	    		}
	    		
	    		// select first child of aggregate
	    		if (next instanceof DataChoice)
	    			next = ((DataChoice)next).getSelectedComponent();
	        	else
	        		next = next.getComponent(0);
	        		        	
	    		// create new record for first child an process it right away
	    		currentRecord = new Record(next);
	    		processNextElement();
	    		return;
        	}
        }
        
        // otherwise parse one atom element
        else
        {
            if (dataHandler != null)
                dataHandler.beginDataAtom(next);
            
            processAtom((DataValue)next);
            
            if (dataHandler != null)
                dataHandler.endDataAtom(next, next.getData());
        }
        
        // take care of parent record in stack
        if (!componentStack.isEmpty())
        {
        	// recursively send 'end' event and pop from stack if parent is finished
        	Record parentRecord = null;
        	boolean stop = false;
    		while (!stop)
    		{
    			// catch end of data
    			if (componentStack.isEmpty())
    			{
    				if (dataHandler != null)
    	                dataHandler.endData(dataComponents, dataComponents.getData());
    				
    	            // signal that a new block is starting
    	            newBlock = true;
    	            currentRecord = null;
    	            
    	            if (parentArray != null && parentArrayIndex == parentArray.getComponentCount())
    	                endOfArray = true;
    	            
    				return;
    			}
    			
    			parentRecord = componentStack.pop();
            	
            	// increment index of parent component
        		if (parentRecord.component instanceof DataChoice)
            		parentRecord.index = parentRecord.count;
            	else
            		parentRecord.index++;
        		
        		if (parentRecord.index >= parentRecord.count)
        		{
        			if (dataHandler != null)
        				dataHandler.endDataBlock(parentRecord.component, parentRecord.component.getData());
        		}
        		else
        			stop = true;
        	}
    		
    		// create next component record
    		componentStack.push(parentRecord);
    		next = parentRecord.component.getComponent(parentRecord.index);
    		currentRecord = new Record(next);
        }
	}
		
	
	/**
	 * Reset the parser before parsing a new tuple
	 */
	public void reset() throws CDMException
	{
		// prepare next array element
		if (parentArray != null)
        {
            dataComponents = parentArray.getComponent(parentArrayIndex);
            parentArrayIndex++;
        }
		
		// generate new data block
        else
            dataComponents.renewDataBlock();
        
		// reset component stack
        componentStack.clear();
        currentRecord = new Record(dataComponents);
	}

	
	/////////////////////
	// Get/Set Methods //
	/////////////////////
	public DataHandler getDataHandler()
	{
		return this.dataHandler;
	}
	
	
	public RawDataHandler getRawDataHandler()
	{
		return this.rawHandler;
	}


	public ErrorHandler getErrorHandler()
	{
		return this.errorHandler;
	}
	
	
	public DataComponent getDataComponents()
	{
		return this.dataComponents;
	}


	public DataEncoding getDataEncoding()
	{
		return this.dataEncoding;
	}


	public void setDataHandler(DataHandler handler)
	{
		this.dataHandler = handler;
	}
	
	
	public void setRawDataHandler(RawDataHandler handler)
	{
		this.rawHandler = handler;		
	}


	public void setErrorHandler(ErrorHandler handler)
	{
		this.errorHandler = handler;
	}
	
	
	public void setDataComponents(DataComponent dataInfo)
	{
		this.dataComponents = (AbstractDataComponent)dataInfo;
	}


	public void setDataEncoding(DataEncoding dataEncoding)
	{
		this.dataEncoding = dataEncoding;
	}
    
    
    public void setParentArray(DataArray parentArray)
    {
        this.parentArray = parentArray;
        parentArray.renewDataBlock();
        parentArrayIndex = 0;
    }
}