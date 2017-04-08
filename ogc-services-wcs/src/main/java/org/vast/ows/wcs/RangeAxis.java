/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is Spotimage S.A.
 Portions created by the Initial Developer are Copyright (C) 2007
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alexandre.robin@spotimage.fr>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import java.util.ArrayList;
import java.util.List;
import net.opengis.swe.v20.DataType;
import org.vast.ows.OWSIdentification;
import org.vast.unit.Unit;


/**
 * <p>
 * Information on a range axis
 * (ex: name=xs1, uom=W.sr-1.m-2.um, type=short, min=1, max=65500, null=0) 
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date 6 nov. 07
 * */
public class RangeAxis extends OWSIdentification
{
	protected Object nullValue;
	protected DataType dataType;
	protected String definition;
	protected Unit unit;
	protected int min;
	protected int max;
	protected List<String> keys;
	

	public RangeAxis()
	{
		keys = new ArrayList<String>(5);
	}
	
	
	public Object getNullValue()
	{
		return nullValue;
	}


	public void setNullValue(Object nullValue)
	{
		this.nullValue = nullValue;
	}


	public DataType getDataType()
	{
		return dataType;
	}


	public void setDataType(DataType dataType)
	{
		this.dataType = dataType;
	}
	
	
	public Unit getUnit()
	{
		return this.unit;
	}
	
	
	public void setUnit(Unit unit)
	{
		this.unit = unit;
	}


	public int getMin()
	{
		return min;
	}


	public void setMin(int min)
	{
		this.min = min;
	}


	public int getMax()
	{
		return max;
	}


	public void setMax(int max)
	{
		this.max = max;
	}


	public List<String> getKeys()
	{
		return keys;
	}


	public void setKeys(List<String> keys)
	{
		this.keys = keys;
	}


	public String getDefinition()
	{
		return definition;
	}


	public void setDefinition(String definition)
	{
		this.definition = definition;
	}
}
