/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sld;

/**
 * <p><b>Title:</b><br/>
 * Grid Symbolizer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * SLD-X Grid Symbolizer object.
 * Allows to specify grid geometry, fill and stroke.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 10, 2005
 * @version 1.0
 */
public class GridSymbolizer extends Symbolizer
{
	protected Stroke stroke;
	protected Fill fill;
    protected Dimensions dimensions;


	public Fill getFill()
	{
		return fill;
	}


	public void setFill(Fill fill)
	{
		this.fill = fill;
	}


	public Stroke getStroke()
	{
		return stroke;
	}


	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}


    public Dimensions getDimensions()
    {
        return dimensions;
    }


    public void setDimensions(Dimensions dimensions)
    {
        this.dimensions = dimensions;
    }
}
