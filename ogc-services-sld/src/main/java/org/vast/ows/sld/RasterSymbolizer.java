/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.sld;

/**
 * <p>
 * SLD Raster Symbolizer object.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 11, 2005
 * */
public class RasterSymbolizer extends Symbolizer
{
    protected RasterChannel redChannel;
    protected RasterChannel greenChannel;
    protected RasterChannel blueChannel;
    protected RasterChannel alphaChannel;
    protected RasterChannel grayChannel;
    protected ScalarParameter opacity;
    protected Dimensions rasterDimensions;
    protected boolean normalizedColors;
    protected int rasterPoolSize;

    
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


    public Dimensions getRasterDimensions()
    {
        return rasterDimensions;
    }


    public void setRasterDimensions(Dimensions dimensions)
    {
        this.rasterDimensions = dimensions;
    }


    public boolean hasNormalizedColors()
    {
        return normalizedColors;
    }


    public void setNormalizedColors(boolean normalizedColors)
    {
        this.normalizedColors = normalizedColors;
    }


	public void setTexPoolSize(int rasterPoolSize) 
	{
		this.rasterPoolSize = rasterPoolSize; 
	}
	
	
	public int getTexPoolSize() 
	{
		return this.rasterPoolSize; 
	}
	
}
