/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import java.time.Instant;
import java.time.OffsetDateTime;
import net.opengis.swe.v20.DataType;


/**
 * <p>
 * Carries an array of boolean primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockBoolean extends AbstractDataBlock
{
	private static final long serialVersionUID = -6524352354000350972L;
    public static final byte TRUE_VAL = 1;
	public static final byte FALSE_VAL = 0;
	protected boolean[] primitiveArray;
	
	
	public DataBlockBoolean()
	{
	}
	
	
	public DataBlockBoolean(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockBoolean copy()
	{
		DataBlockBoolean newBlock = new DataBlockBoolean();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockBoolean renew()
    {
        DataBlockBoolean newBlock = new DataBlockBoolean();
        newBlock.primitiveArray = new boolean[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockBoolean clone()
    {
        DataBlockBoolean newBlock = new DataBlockBoolean();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new boolean[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public boolean[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (boolean[])obj;
        this.atomCount = primitiveArray.length;
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.BOOLEAN;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.BOOLEAN;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new boolean[size];
		this.atomCount = size;
	}


	@Override
    public boolean getBooleanValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public byte getByteValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public short getShortValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public int getIntValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public long getLongValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public float getFloatValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public double getDoubleValue(int index)
	{
		return primitiveArray[startIndex + index] ? TRUE_VAL : FALSE_VAL;
	}


	@Override
    public String getStringValue(int index)
	{
		return Boolean.toString(primitiveArray[startIndex + index]);
	}


    @Override
    public Instant getTimeStamp(int index)
    {
        throw conversionError(getDataType(), DataType.INSTANT);
    }


    @Override
    public OffsetDateTime getDateTime(int index)
    {
        throw conversionError(getDataType(), DataType.DATETIME);
    }


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = (value == 0) ? false : true;
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = (Float.isNaN(value) || Math.abs(value) < Math.ulp(0.0)) ? false : true;
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = (Double.isNaN(value) || Math.abs(value) < Math.ulp(0.0)) ? false : true;
	}


	@Override
    public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Boolean.parseBoolean(value);
	}


    @Override
    public void setTimeStamp(int index, Instant value)
    {
        throw conversionError(DataType.INSTANT, getDataType());
    }


    @Override
    public void setDateTime(int index, OffsetDateTime value)
    {
        throw conversionError(DataType.DATETIME, getDataType());
    }
}
