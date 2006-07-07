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
 * Raster Symbolizer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * SLD Raster Symbolizer object.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 11, 2005
 * @version 1.0
 */
public class RasterSymbolizer extends Symbolizer
{
    protected RasterChannel redChannel;
    protected RasterChannel greenChannel;
    protected RasterChannel blueChannel;
    protected RasterChannel alphaChannel;
    protected RasterChannel grayChannel;
    protected ScalarParameter opacity;
    protected Dimensions dimensions;


    public RasterChannel getAlphaChannel()
    {
        return alphaChannel;
    }


    public void setAlphaChannel(RasterChannel alphaChannel)
    {
        this.alphaChannel = alphaChannel;
    }


    public RasterChannel getBlueChannel()
    {
        return blueChannel;
    }


    public void setBlueChannel(RasterChannel blueChannel)
    {
        this.blueChannel = blueChannel;
    }


    public RasterChannel getGrayChannel()
    {
        return grayChannel;
    }


    public void setGrayChannel(RasterChannel grayChannel)
    {
        this.grayChannel = grayChannel;
    }


    public RasterChannel getGreenChannel()
    {
        return greenChannel;
    }


    public void setGreenChannel(RasterChannel greenChannel)
    {
        this.greenChannel = greenChannel;
    }


    public ScalarParameter getOpacity()
    {
        return opacity;
    }


    public void setOpacity(ScalarParameter opacity)
    {
        this.opacity = opacity;
    }


    public RasterChannel getRedChannel()
    {
        return redChannel;
    }


    public void setRedChannel(RasterChannel redChannel)
    {
        this.redChannel = redChannel;
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
