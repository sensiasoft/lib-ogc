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
 * Carries an array of String objects.
 * All data is casted to other types when requested.
 * </p>
 *
 * @author Alex Robin
 * @since Nov 23, 2005
 * */
public class DataBlockString extends AbstractDataBlock
{
	private static final long serialVersionUID = -7268873688598943399L;
    protected String[] primitiveArray;
	
	
	public DataBlockString()
	{
	}
	
	
	public DataBlockString(int size)
	{
		resize(size);
	}
	
	
	@Override
    public DataBlockString copy()
	{
		DataBlockString newBlock = new DataBlockString();
		newBlock.primitiveArray = this.primitiveArray;
		newBlock.startIndex = this.startIndex;
		newBlock.atomCount = this.atomCount;
		return newBlock;
	}
    
    
    @Override
    public DataBlockString renew()
    {
        DataBlockString newBlock = new DataBlockString();
        newBlock.primitiveArray = new String[this.atomCount];
        newBlock.startIndex = this.startIndex;
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public DataBlockString clone()
    {
        // TODO make sure new Strings are created
        DataBlockString newBlock = new DataBlockString();
        //newBlock.primitiveArray = this.primitiveArray.clone();
        newBlock.primitiveArray = new String[this.atomCount];
        System.arraycopy(this.primitiveArray, this.startIndex, newBlock.primitiveArray, 0, this.atomCount);
        newBlock.atomCount = this.atomCount;
        return newBlock;
    }
    
    
    @Override
    public String[] getUnderlyingObject()
    {
        return primitiveArray;
    }
    
    
    @Override
    public void setUnderlyingObject(Object obj)
    {
    	this.primitiveArray = (String[])obj;
        this.atomCount = primitiveArray.length;
    }
	
	
	@Override
    public DataType getDataType()
	{
		return DataType.UTF_STRING;
	}


	@Override
    public DataType getDataType(int index)
	{
		return DataType.UTF_STRING;
	}
	
	
	@Override
    public void resize(int size)
	{
		primitiveArray = new String[size];
		this.atomCount = size;
	}


	@Override
    public boolean getBooleanValue(int index)
	{
		var str = primitiveArray[startIndex + index];
        
        try {
            return Boolean.parseBoolean(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.BOOLEAN);
        }
	}


	@Override
    public byte getByteValue(int index)
	{
		var str = primitiveArray[startIndex + index];
        
        try {
            return Byte.parseByte(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.BYTE);
        }
	}


	@Override
    public short getShortValue(int index)
	{
		var str = primitiveArray[startIndex + index];
        
        try {
            return Short.parseShort(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.SHORT);
        }
	}


	@Override
    public int getIntValue(int index)
	{
		var str = primitiveArray[startIndex + index];
        
        try {
            return Integer.parseInt(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.INT);
        }
	}


	@Override
    public long getLongValue(int index)
	{
		var str = primitiveArray[startIndex + index];
        
        try {
            return Long.parseLong(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.LONG);
        }
	}


	@Override
    public float getFloatValue(int index)
	{
		var str = primitiveArray[startIndex + index];
        
        try {
            return Float.parseFloat(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.FLOAT);
        }
	}


	@Override
    public double getDoubleValue(int index)
	{
		var str = primitiveArray[startIndex + index];
        
        try {
            return Double.parseDouble(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.DOUBLE);
        }
	}


	@Override
    public String getStringValue(int index)
	{
		return primitiveArray[startIndex + index];
	}


    @Override
    public Instant getTimeStamp(int index)
    {
        var str = primitiveArray[startIndex + index];
        
        try {
            return Instant.parse(str);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.INSTANT);
        }
    }


    @Override
    public OffsetDateTime getDateTime(int index)
    {
        var str = primitiveArray[startIndex + index];
        
        try {
            return OffsetDateTime.parse(primitiveArray[startIndex + index]);
        }
        catch (Exception e) {
            throw conversionError(str, DataType.DATETIME);
        }
    }


	@Override
    public void setBooleanValue(int index, boolean value)
	{
		primitiveArray[startIndex + index] = Boolean.toString(value);
	}


	@Override
    public void setByteValue(int index, byte value)
	{
		primitiveArray[startIndex + index] = Byte.toString(value);
	}


	@Override
    public void setShortValue(int index, short value)
	{
		primitiveArray[startIndex + index] = Short.toString(value);
	}


	@Override
    public void setIntValue(int index, int value)
	{
		primitiveArray[startIndex + index] = Integer.toString(value);
	}


	@Override
    public void setLongValue(int index, long value)
	{
		primitiveArray[startIndex + index] = Long.toString(value);
	}


	@Override
    public void setFloatValue(int index, float value)
	{
		primitiveArray[startIndex + index] = Float.toString(value);
	}


	@Override
    public void setDoubleValue(int index, double value)
	{
		primitiveArray[startIndex + index] = Double.toString(value);
	}


	@Override
    public void setStringValue(int index, String value)
	{
		primitiveArray[startIndex + index] = value;
	}


    @Override
    public void setTimeStamp(int index, Instant value)
    {
        primitiveArray[startIndex + index] = value.toString();
    }


    @Override
    public void setDateTime(int index, OffsetDateTime value)
    {
        primitiveArray[startIndex + index] = value.toString();
    }
    
    
    protected UnsupportedOperationException conversionError(String value, DataType destType)
    {
        return new UnsupportedOperationException("Cannot convert " + value + " to " + destType);
    }
}
