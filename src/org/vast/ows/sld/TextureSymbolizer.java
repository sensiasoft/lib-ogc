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
 * Texture Mapping Symbolizer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * SLD-X Texture Mapping Symbolizer object.
 * Allows to specify grid geometry + texture data.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 10, 2005
 * @version 1.0
 */
public class TextureSymbolizer extends Symbolizer
{
    protected RasterSymbolizer imagery;
    protected GridSymbolizer grid;


    public GridSymbolizer getGrid()
    {
        return grid;
    }


    public void setGrid(GridSymbolizer grid)
    {
        this.grid = grid;
    }


    public RasterSymbolizer getImagery()
    {
        return imagery;
    }


    public void setImagery(RasterSymbolizer imagery)
    {
        this.imagery = imagery;
    }
    
    
    @Override
    public Geometry getGeometry()
    {
        throw new UnsupportedOperationException();
    }


    @Override
    public void setGeometry(Geometry geometry)
    {
        throw new UnsupportedOperationException();
    }

}
