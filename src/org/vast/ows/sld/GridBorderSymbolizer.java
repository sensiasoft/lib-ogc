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
 * Grid Border Symbolizer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * SLD-X Grid Border Symbolizer object.
 * To render only the border of gridded data.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Aug 02, 2006
 * @version 1.0
 */
public class GridBorderSymbolizer extends GridSymbolizer
{
	protected Stroke stroke;


	public Stroke getStroke()
	{
        if (stroke == null)
            stroke = new Stroke();
        
        return stroke;
	}


	public void setStroke(Stroke stroke)
	{
		this.stroke = stroke;
	}
}
