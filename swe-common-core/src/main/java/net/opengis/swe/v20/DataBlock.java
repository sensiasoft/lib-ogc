/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;

/**
 * <p>
 * Implementations of this class should be able to carry data
 * for any part of the data cluster defined in the DataDefinition
 * part of the SWE Common Data Model. It should provide access to any
 * data atom through primitive specific methods. Data should be
 * casted from one primitive to another when needed and possible.
 * In order to improve performance, no check needs to be enforced on
 * the index argument (i.e 0 < index < blockSize - 1), thus all
 * getXXXValue(int index) and setXXXValue(int index, ...) do not
 * need to guarantee their bahavior when an invalid index is used.
 * IndexOutOfBoundException can be thrown in some cases.  
 * </p>
 *
 * @author Alex Robin
 * @since Aug 14, 2005
 * */
public interface DataBlock extends Serializable
{
	public DataType getDataType();
	
	
	public DataType getDataType(int index);
	
	
	public int getAtomCount();
	
	
	public boolean getBooleanValue(int index);
	
	
	public byte getByteValue(int index);


	public short getShortValue(int index);


	public int getIntValue(int index);


	public long getLongValue(int index);


	public float getFloatValue(int index);


	public double getDoubleValue(int index);
	
	
	public String getStringValue(int index);
    
    
    public Instant getTimeStamp(int index);
    
    
    public OffsetDateTime getDateTime(int index);


	public default boolean getBooleanValue()
	{
	    return getBooleanValue(0);
	}
	
	
	public default byte getByteValue()
    {
        return getByteValue(0);
    }


	public default short getShortValue()
    {
        return getShortValue(0);
    }


	public default int getIntValue()
    {
        return getIntValue(0);
    }


	public default long getLongValue()
    {
        return getLongValue(0);
    }


	public default float getFloatValue()
    {
        return getFloatValue(0);
    }


	public default double getDoubleValue()
    {
        return getDoubleValue(0);
    }
	
	
	public default String getStringValue()
    {
        return getStringValue(0);
    }
	
	
	public default Instant getTimeStamp()
    {
        return getTimeStamp(0);
    }
    
    
    public default OffsetDateTime getDateTime()
    {
        return getDateTime(0);
    }


	public void setBooleanValue(int index, boolean value);
			
			
	public void setByteValue(int index, byte value);
	
	
	public void setShortValue(int index, short value);


	public void setIntValue(int index, int value);


	public void setLongValue(int index, long value);


	public void setFloatValue(int index, float value);


	public void setDoubleValue(int index, double value);
	
	
	public void setStringValue(int index, String value);
    
    
    public void setTimeStamp(int index, Instant value);
    
    
    public void setDateTime(int index, OffsetDateTime value);


	public default void setBooleanValue(boolean value)
	{
	    setBooleanValue(0, value);
	}
	
	
	public default void setByteValue(byte value)
    {
	    setByteValue(0, value);
    }


	public default void setShortValue(short value)
    {
	    setShortValue(0, value);
    }


	public default void setIntValue(int value)
    {
	    setIntValue(0, value);
    }


	public default void setLongValue(long value)
    {
	    setLongValue(0, value);
    }


	public default void setFloatValue(float value)
    {
	    setFloatValue(0, value);
    }


	public default void setDoubleValue(double value)
    {
	    setDoubleValue(0, value);
    }
	
	
	public default void setStringValue(String value)
    {
	    setStringValue(0, value);
    }
    
    
    public default void setTimeStamp(Instant value)
    {
        setTimeStamp(0, value);
    }
    
    
    public default void setDateTime(OffsetDateTime value)
    {
        setDateTime(0, value);
    }
	
	
	public void resize(int size);
    
    
    public DataBlock copy();
    
    
    public DataBlock clone();
    
    
    public DataBlock renew();
    
    
    public Object getUnderlyingObject();
    
    
    public void setUnderlyingObject(Object obj);
    
    
    public void updateAtomCount();
}
