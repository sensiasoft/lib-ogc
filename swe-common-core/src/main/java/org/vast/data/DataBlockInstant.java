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
import java.time.ZoneOffset;
import net.opengis.swe.v20.DataType;


/**
 * <p>
 * Carries an array of Instant objects.
 * All data is casted to other types when possible.
 * </p>
 *
 * @author Alex Robin
 * @since Mar 7, 2025
 * */
public class DataBlockInstant extends AbstractDataBlock
{
	private static final long serialVersionUID = 4026669977621429786L;
    protected Instant[] primitiveArray;
	
	
	public DataBlockInstant()
	{
	}
	
	
	public DataBlockInstant(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockInstant copy()
	{
		DataBlockInstant newBlock = new DataBlockInstant();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockInstant renew()
    {
        DataBlockInstant newBlock = new DataBlockInstant();
        newBlock.primitiveArray = new Instant[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockInstant clone()
    {
        DataBlockInstant newBlock = new DataBlockInstant();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new Instant[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public Instant[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (Instant[])obj;
        this.atomCount = primitiveArray.length;
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.INSTANT;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.INSTANT;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new Instant[size];
		this.atomCount = size;
	}


	@Override
    public boolean getBooleanValue(int index)
	{
	    return primitiveArray[startIndex + index] != null;
	}


	@Override
    public byte getByteValue(int index)
	{
		throw conversionError(getDataType(), DataType.BYTE);
	}


	@Override
    public short getShortValue(int index)
	{
	    throw conversionError(getDataType(), DataType.SHORT);
	}


	@Override
    public int getIntValue(int index)
	{
	    throw conversionError(getDataType(), DataType.INT);
	}


	@Override
    public long getLongValue(int index)
	{
		return instantToLong(primitiveArray[startIndex + index]);
	}


	@Override
    public float getFloatValue(int index)
	{
	    throw conversionError(getDataType(), DataType.FLOAT);
	}


	@Override
    public double getDoubleValue(int index)
	{
		return instantToDouble(primitiveArray[startIndex + index]);
	}


	@Override
    public String getStringValue(int index)
	{
		return primitiveArray[startIndex + index].toString();
	}


    @Override
    public Instant getTimeStamp(int index)
    {
        return primitiveArray[startIndex + index];
    }


    @Override
    public OffsetDateTime getDateTime(int index)
    {
        var ts = getTimeStamp(index);
        return ts != null ? OffsetDateTime.ofInstant(ts, ZoneOffset.UTC) : null;
    }


	@Override
    public void setBooleanValue(int index, boolean value)
	{
	    throw conversionError(DataType.BOOLEAN, getDataType());
	}


	@Override
    public void setByteValue(int index, byte value)
	{
	    throw conversionError(DataType.BYTE, getDataType());
	}


	@Override
    public void setShortValue(int index, short value)
	{
	    throw conversionError(DataType.SHORT, getDataType());
	}


	@Override
    public void setIntValue(int index, int value)
	{
	    throw conversionError(DataType.INT, getDataType());
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = longToInstant(value);
	}


	@Override
    public void setFloatValue(int index, float value)
	{
	    throw conversionError(DataType.FLOAT, getDataType());
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = doubleToInstant(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
	    try {
	        primitiveArray[startIndex + index] = 
	            value != null ? Instant.parse(value) : null;
        }
        catch (Exception e) {
            throw new UnsupportedOperationException("Cannot convert " + value + " to " + getDataType());
        }
	}


    @Override
    public void setTimeStamp(int index, Instant value)
    {
        primitiveArray[startIndex + index] = value;
    }


    @Override
    public void setDateTime(int index, OffsetDateTime value)
    {
        setTimeStamp(index, value != null ? value.toInstant() : null);
    }
    
    
    public static long instantToLong(Instant ts)
    {
        return (ts != null) ? ts.toEpochMilli() : Long.MIN_VALUE;
    }
    
    
    public static Instant longToInstant(long epochMillis)
    {
        return epochMillis != Long.MIN_VALUE ? Instant.ofEpochMilli(epochMillis) : null;
    }
    
    
    public static double instantToDouble(Instant ts)
    {
        double dval = Double.NaN;
        
        if (ts != null)
        {
            var sec = (double)ts.getEpochSecond();
            var nanos = ts.getNano();
            dval = sec + nanos*1e-9;
        }
        
        return dval;
    }
    
    
    public static Instant doubleToInstant(double epochTimeAsDouble)
    {
        if (Double.isNaN(epochTimeAsDouble))
            return null;
        
        var seconds = Math.floor(epochTimeAsDouble);
        var nanos = epochTimeAsDouble*1e9 - seconds*1e9;
        return Instant.ofEpochSecond((long)seconds, (long)nanos);
    }
}
