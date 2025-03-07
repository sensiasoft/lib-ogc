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
 * Carries an array of long primitives.
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockLong extends AbstractDataBlock
{
	private static final long serialVersionUID = -696416660534797635L;
    protected long[] primitiveArray;
	
	
	public DataBlockLong()
	{
	}
	
	
	public DataBlockLong(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockLong copy()
	{
		DataBlockLong newBlock = new DataBlockLong();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockLong renew()
    {
        DataBlockLong newBlock = new DataBlockLong();
        newBlock.primitiveArray = new long[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockLong clone()
    {
        DataBlockLong newBlock = new DataBlockLong();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new long[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public long[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (long[])obj;
        this.atomCount = primitiveArray.length;
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.LONG;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.LONG;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new long[size];
		this.atomCount = size;
	}


	@Override
    public boolean getBooleanValue(int index)
	{
		return (primitiveArray[startIndex + index] == 0) ? false : true;
	}


	@Override
    public byte getByteValue(int index)
	{
		return (byte)primitiveArray[startIndex + index];
	}


	@Override
    public short getShortValue(int index)
	{
		return (short)primitiveArray[startIndex + index];
	}


	@Override
    public int getIntValue(int index)
	{
		return (int)primitiveArray[startIndex + index];
	}


	@Override
    public long getLongValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public float getFloatValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public double getDoubleValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


	@Override
    public String getStringValue(int index)
	{
		return Long.toString(primitiveArray[startIndex + index]);
	}


    @Override
    public Instant getTimeStamp(int index)
    {
        return DataBlockInstant.longToInstant(primitiveArray[startIndex + index]);
    }


    @Override
    public OffsetDateTime getDateTime(int index)
    {
        return DataBlockDateTime.longToDateTime(primitiveArray[startIndex + index]);
    }


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = value ? DataBlockBoolean.TRUE_VAL : DataBlockBoolean.FALSE_VAL;
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = value;
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = Math.round(value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = Math.round(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = Long.parseLong(value);
	}


    @Override
    public void setTimeStamp(int index, Instant value)
    {
        primitiveArray[startIndex + index] = DataBlockInstant.instantToLong(value);
    }


    @Override
    public void setDateTime(int index, OffsetDateTime value)
    {
        primitiveArray[startIndex + index] = DataBlockDateTime.dateTimeToLong(value);
    }
}
