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

import org.vast.io.xml.DOMReader;
import org.vast.util.MessageSystem;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * <p><b>Title:</b><br/>
 * SLD Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * This class parses an SLD XML document and creates appropriate
 * SLD java objects like Symbolizers
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 11, 2005
 * @version 1.0
 */
public class SLDReader
{
	private ParameterReader cssReader;
    	
	
	public SLDReader()
	{
		cssReader = new ParameterReader();        
	}
	
	
	public Symbolizer readSymbolizer(DOMReader dom, Element symElt)
	{
		Symbolizer sym = null;
		
		if (symElt.getLocalName().equals("PointSymbolizer"))
			sym = readPoint(dom, symElt);
		else if (symElt.getLocalName().equals("LineSymbolizer"))
			sym = readLine(dom, symElt);
		else if (symElt.getLocalName().equals("PolygonSymbolizer"))
			sym = readPolygon(dom, symElt);
        else if (symElt.getLocalName().equals("TextSymbolizer"))
            sym = readText(dom, symElt);
        else if (symElt.getLocalName().equals("GridMeshSymbolizer"))
            sym = readGridMesh(dom, symElt);
        else if (symElt.getLocalName().equals("GridFillSymbolizer"))
            sym = readGridFill(dom, symElt);
        else if (symElt.getLocalName().equals("GridBorderSymbolizer"))
            sym = readGridBorder(dom, symElt);
		else if (symElt.getLocalName().equals("RasterSymbolizer"))
			sym = readRaster(dom, symElt);
        else if (symElt.getLocalName().equals("TextureSymbolizer"))
            sym = readTexture(dom, symElt);
        else
            MessageSystem.display("Unknown Symbolizer: " + symElt.getLocalName(), true);
        
		return sym;
	}
	
	
	public PointSymbolizer readPoint(DOMReader dom, Element symElt)
	{
		PointSymbolizer pointSym = new PointSymbolizer();		
		readGeometryElt(pointSym, dom, symElt);
		
		// read graphic
		Element graphicElt = dom.getElement(symElt, "Graphic");
		Graphic graphic = readGraphic(dom, graphicElt);
		pointSym.setGraphic(graphic);
		
		return pointSym;
	}
	
	
	public LineSymbolizer readLine(DOMReader dom, Element symElt)
	{
		LineSymbolizer lineSym = new LineSymbolizer();
		readGeometryElt(lineSym, dom, symElt);
		
		// read stroke
		Element strokeElt = dom.getElement(symElt, "Stroke");
		Stroke stroke = readStroke(dom, strokeElt);
		lineSym.setStroke(stroke);
		
		return lineSym;
	}
	
	
	public PolygonSymbolizer readPolygon(DOMReader dom, Element symElt)
	{
		PolygonSymbolizer polygonSym = new PolygonSymbolizer();
		readGeometryElt(polygonSym, dom, symElt);
		
		// read stroke
		Element strokeElt = dom.getElement(symElt, "Stroke");
		Stroke stroke = readStroke(dom, strokeElt);
		polygonSym.setStroke(stroke);
		
		// read fill
		Element fillElt = dom.getElement(symElt, "Fill");
		Fill fill = readFill(dom, fillElt);
		polygonSym.setFill(fill);
		
		return polygonSym;
	}
	
	
	public TextSymbolizer readText(DOMReader dom, Element symElt)
	{
		TextSymbolizer textSym = new TextSymbolizer();
        readGeometryElt(textSym, dom, symElt);
        
        // read label text
        Element labelElt = dom.getElement(symElt, "Label"); 
        ScalarParameter labelParam = cssReader.readCssParameter(dom, labelElt);
        textSym.setLabel(labelParam);
        
        // read Font
        Element fontElt = dom.getElement(symElt, "Font");
        Font font = readFont(dom, fontElt);
        textSym.setFont(font);
        
        // read label placement
        //TODO read label placement
        
        // read fill
        Element fillElt = dom.getElement(symElt, "Fill");
        Fill fill = readFill(dom, fillElt);
        textSym.setFill(fill);
		
		return textSym;
	}
    
    
    public GridFillSymbolizer readGridFill(DOMReader dom, Element symElt)
    {
        GridFillSymbolizer gridSym = new GridFillSymbolizer();
        readGeometryElt(gridSym, dom, symElt);
        
        // read dimensions
        Element dimElt = dom.getElement(symElt, "Dimensions");
        Dimensions dims = readDimensions(dom, dimElt);
        gridSym.setDimensions(dims);
               
        // read fill
        Element fillElt = dom.getElement(symElt, "Fill");
        Fill fill = readFill(dom, fillElt);
        gridSym.setFill(fill);
        
        return gridSym;
    }
    
    
    public GridMeshSymbolizer readGridMesh(DOMReader dom, Element symElt)
    {
        GridMeshSymbolizer gridSym = new GridMeshSymbolizer();
        readGeometryElt(gridSym, dom, symElt);
        
        // read dimensions
        Element dimElt = dom.getElement(symElt, "Dimensions");
        Dimensions dims = readDimensions(dom, dimElt);
        gridSym.setDimensions(dims);
               
        // read stroke
        Element strokeElt = dom.getElement(symElt, "Stroke");
        Stroke stroke = readStroke(dom, strokeElt);
        gridSym.setStroke(stroke);
        
        return gridSym;
    }
    
    
    public GridBorderSymbolizer readGridBorder(DOMReader dom, Element symElt)
    {
        GridBorderSymbolizer gridSym = new GridBorderSymbolizer();
        readGeometryElt(gridSym, dom, symElt);
        
        // read dimensions
        Element dimElt = dom.getElement(symElt, "Dimensions");
        Dimensions dims = readDimensions(dom, dimElt);
        gridSym.setDimensions(dims);
        
        // read stroke
        Element strokeElt = dom.getElement(symElt, "Stroke");
        Stroke stroke = readStroke(dom, strokeElt);
        gridSym.setStroke(stroke);
        
        return gridSym;
    }    
	
	
	public RasterSymbolizer readRaster(DOMReader dom, Element symElt)
	{
        RasterSymbolizer rasterSym = new RasterSymbolizer();
        readRasterParameters(rasterSym, dom, symElt);
        return rasterSym;
    }
    
    
    public void readRasterParameters(RasterSymbolizer rasterSym, DOMReader dom, Element symElt)
    {
		// read red channel
        Element redElt = dom.getElement(symElt, "ChannelSelection/RedChannel");
        rasterSym.setRedChannel(readRasterChannel(dom, redElt));
        
        // read green channel
        Element greenElt = dom.getElement(symElt, "ChannelSelection/GreenChannel");
        rasterSym.setGreenChannel(readRasterChannel(dom, greenElt));
        
        // read blue channel
        Element blueElt = dom.getElement(symElt, "ChannelSelection/BlueChannel");
        rasterSym.setBlueChannel(readRasterChannel(dom, blueElt));
        
        // read alpha channel
        Element alphaElt = dom.getElement(symElt, "ChannelSelection/AlphaChannel");
        rasterSym.setAlphaChannel(readRasterChannel(dom, alphaElt));
        
        // read gray channel
        Element grayElt = dom.getElement(symElt, "ChannelSelection/GrayChannel");
        rasterSym.setGrayChannel(readRasterChannel(dom, grayElt));        
        
        // read raster global opacity
        Element opacityElt = dom.getElement(symElt, "Opacity");
        ScalarParameter opacity = cssReader.readCssParameter(dom, opacityElt);
        rasterSym.setOpacity(opacity);
        
        // read raster dimensions
        Element dimElt = dom.getElement(symElt, "RasterDimensions");
        Dimensions dim = readDimensions(dom, dimElt);
        rasterSym.setRasterDimensions(dim);
	}
    
    
    public RasterChannel readRasterChannel(DOMReader dom, Element channelElt)
    {
        RasterChannel channel = null;
        
        if (channelElt == null)
            return null;
                
        // read source channel name
        if (dom.existElement(channelElt, "SourceChannelName"))
        {
            Element channelNameElt = dom.getElement(channelElt, "SourceChannelName");
            ScalarParameter channelName = cssReader.readPropertyName(dom, channelNameElt);
            channel = new RasterChannel(channelName);
        }
        else
        {
            ScalarParameter channelParam = cssReader.readCssParameter(dom, channelElt);
            channel = new RasterChannel(channelParam);
        }
        
        // read normalize and histogram
        if (dom.existElement(channelElt, "ContrastEhancement/Normalize"))
            channel.setNormalize(true);
        else if (dom.existElement(channelElt, "ContrastEhancement/Histogram"))
            channel.setHistogram(true);
        
        // read gamma correction value
        String gammaText = dom.getElementValue(channelElt, "ContrastEhancement/GammaValue");
        if (gammaText != null)
        {
            channel.setGamma(Double.parseDouble(gammaText));
        }
        
        return channel;
    }
    
    
    public TextureSymbolizer readTexture(DOMReader dom, Element symElt)
    {
        TextureSymbolizer texSym = new TextureSymbolizer();
        readGeometryElt(texSym, dom, symElt);
        readRasterParameters(texSym, dom, symElt);
        
        // read grid dimensions
        Element dimElt = dom.getElement(symElt, "GridDimensions");
        Dimensions dim = readDimensions(dom, dimElt);
        texSym.setGridDimensions(dim);
        
        return texSym;
    }
	
	
	protected void readGeometryElt(Symbolizer symbolizer, DOMReader dom, Element symElt)
	{
		// read geometry
		Element geomElt = dom.getElement(symElt, "Geometry");
		Geometry geometry = readGeometry(dom, geomElt);
		symbolizer.setGeometry(geometry);
	}
	
	
	/**
	 * Reads a Geometry Element and fill up the corresponding object
	 * This supports STT extensions
	 * @param dom
	 * @param geomElt
	 * @return
	 */
	public Geometry readGeometry(DOMReader dom, Element geomElt)
	{
		Geometry geometry = new Geometry();
		
		if (dom.existElement(geomElt, "PropertyName"))
		{
			String propName = dom.getElementValue(geomElt, "PropertyName");
			geometry.setPropertyName(propName);
		}
		else
		{
			// read all css parameters
			NodeList cssElts = dom.getElements(geomElt, "CssParameter");
			for (int i=0; i<cssElts.getLength(); i++)
			{
				Element cssElt = (Element)cssElts.item(i);
				String paramName = dom.getAttributeValue(cssElt, "name");
				
                if (paramName.equalsIgnoreCase("geometry-object"))
                {
                    ScalarParameter objectData = cssReader.readCssParameter(dom, cssElt);
                    geometry.setObject(objectData);
                }
                else if (paramName.equalsIgnoreCase("geometry-breaks"))
                {
                    ScalarParameter breakData = cssReader.readCssParameter(dom, cssElt);
                    geometry.setBreaks(breakData);
                }
                else if (paramName.equalsIgnoreCase("geometry-x"))
				{
					ScalarParameter xData = cssReader.readCssParameter(dom, cssElt);
					geometry.setX(xData);
				}
				else if (paramName.equalsIgnoreCase("geometry-y"))
				{
					ScalarParameter yData = cssReader.readCssParameter(dom, cssElt);
					geometry.setY(yData);
				}
				else if (paramName.equalsIgnoreCase("geometry-z"))
				{
					ScalarParameter zData = cssReader.readCssParameter(dom, cssElt);
					geometry.setZ(zData);
				}
				else if (paramName.equalsIgnoreCase("geometry-t"))
				{
					ScalarParameter tData = cssReader.readCssParameter(dom, cssElt);
					geometry.setT(tData);
				}
			}
		}
		
		return geometry;
	}
    
    
    /**
     * Reads a sldx:Dimensions element and fill up the corresponding object
     * @param dom
     * @param symElt
     * @return
     */
    public Dimensions readDimensions(DOMReader dom, Element dimElt)
    {
        Dimensions dim = new Dimensions();
        
        // read all css parameters
        NodeList cssElts = dom.getElements(dimElt, "CssParameter");
        for (int i=0; i<cssElts.getLength(); i++)
        {
            Element cssElt = (Element)cssElts.item(i);
            String paramName = dom.getAttributeValue(cssElt, "name");
            
            if (paramName.equalsIgnoreCase("width"))
            {
                ScalarParameter widthData = cssReader.readCssParameter(dom, cssElt);
                dim.setWidth(widthData);
            }
            else if (paramName.equalsIgnoreCase("length"))
            {
                ScalarParameter lengthData = cssReader.readCssParameter(dom, cssElt);
                dim.setLength(lengthData);
            }
            else if (paramName.equalsIgnoreCase("depth"))
            {
                ScalarParameter depthData = cssReader.readCssParameter(dom, cssElt);
                dim.setDepth(depthData);
            }
            else if (paramName.equalsIgnoreCase("time"))
            {
                ScalarParameter timeData = cssReader.readCssParameter(dom, cssElt);
                dim.setTime(timeData);
            }
        }
        
        return dim;
    }
	
	
	/**
	 * Reads a Graphic Element and fill up the corresponding object
	 * @param dom
	 * @param graphicElt
	 * @return
	 */
	public Graphic readGraphic(DOMReader dom, Element graphicElt)
	{
		Graphic graphic = new Graphic();
		if (graphicElt == null)
			return graphic;
		
		// read mark
		if (dom.existElement(graphicElt, "Mark"))
		{
			GraphicMark mark = new GraphicMark();
			
			// well known name (= shape)
			Element nameElt = dom.getElement(graphicElt, "Mark/WellKnownName");
			ScalarParameter shape = cssReader.readCssParameter(dom, nameElt);
			mark.setShape(shape);
			
			// stroke
			Element strokeElt = dom.getElement(graphicElt, "Mark/Stroke");
			Stroke stroke = readStroke(dom, strokeElt);
			mark.setStroke(stroke);
			
			// fill
			Element fillElt = dom.getElement(graphicElt, "Mark/Fill");
			Fill fill = readFill(dom, fillElt);
			mark.setFill(fill);
			
			graphic.getGlyphs().add(mark);
		}
		
		// read external graphic
		else if (dom.existElement(graphicElt, "ExternalGraphic"))
		{
			GraphicImage img = new GraphicImage();
			
			img.setFormat(dom.getAttributeValue(graphicElt, "ExternalGraphic/Format"));
			//img.setUrl();
			
			graphic.getGlyphs().add(img);
		}
		
		// read opacity
		Element opacityElt = dom.getElement(graphicElt, "Opacity");
		ScalarParameter opacity = cssReader.readCssParameter(dom, opacityElt);
		graphic.setOpacity(opacity);
		
		// read size
		Element sizeElt = dom.getElement(graphicElt, "Size");
		ScalarParameter size = cssReader.readCssParameter(dom, sizeElt);
		graphic.setSize(size);
		
		// read rotation
		Element rotationElt = dom.getElement(graphicElt, "Rotation");
		ScalarParameter rotation = cssReader.readCssParameter(dom, rotationElt);
		graphic.setRotation(rotation);
		
		return graphic;
	}
	
	
	/**
	 * Reads a Fill Element and fill up the corresponding object
	 * @param dom
	 * @param fillElt
	 * @return
	 */
	public Fill readFill(DOMReader dom, Element fillElt)
	{
		Fill fill = new Fill();
        fill.setColor(new Color());
		if (fillElt == null)
			return fill;
		
		// read graphic fill if present
		Element gFillElt = dom.getElement(fillElt, "GraphicFill");
		if (gFillElt != null)
			fill.setGraphicFill(readGraphic(dom, gFillElt));
		
		// read all css parameters
		NodeList cssElts = dom.getElements(fillElt, "CssParameter");
		for (int i=0; i<cssElts.getLength(); i++)
		{
			Element cssElt = (Element)cssElts.item(i);
			String paramName = dom.getAttributeValue(cssElt, "name");
			
			if (paramName.equalsIgnoreCase("fill"))
			{
                String colorVal = dom.getElementValue(cssElt, "");
				fill.setColor(new Color(colorVal.substring(1)));
			}
            else if (paramName.equalsIgnoreCase("fill-red"))
            {
                ScalarParameter red = cssReader.readCssParameter(dom, cssElt);
                fill.getColor().setRed(red);
            }
            else if (paramName.equalsIgnoreCase("fill-green"))
            {
                ScalarParameter green = cssReader.readCssParameter(dom, cssElt);
                fill.getColor().setGreen(green);
            }
            else if (paramName.equalsIgnoreCase("fill-blue"))
            {
                ScalarParameter blue = cssReader.readCssParameter(dom, cssElt);
                fill.getColor().setBlue(blue);
            }
            else if (paramName.equalsIgnoreCase("fill-opacity"))
            {
                ScalarParameter opacity = cssReader.readCssParameter(dom, cssElt);
                fill.setOpacity(opacity);
                fill.getColor().setAlpha(opacity);
            }        
		}
		
		return fill;
	}
	
	
	/**
	 * Reads a Stroke Element and fill up the corresponding object
	 * @param dom
	 * @param strokeElt
	 * @return
	 */
	public Stroke readStroke(DOMReader dom, Element strokeElt)
	{
		Stroke stroke = new Stroke();
        stroke.setColor(new Color());
        
		if (strokeElt == null)
			return stroke;
		
		// read graphic fill if present
		Element gFillElt = dom.getElement(strokeElt, "GraphicFill");
		if (gFillElt != null)
			stroke.setGraphicFill(readGraphic(dom, gFillElt));
		
		// read graphic stroke if present
		Element gStrokeElt = dom.getElement(strokeElt, "GraphicStroke");
		if (gStrokeElt != null)
			stroke.setGraphicStroke(readGraphic(dom, gStrokeElt));
		
		// read all css parameters
		NodeList cssElts = dom.getElements(strokeElt, "CssParameter");
		for (int i=0; i<cssElts.getLength(); i++)
		{
			Element cssElt = (Element)cssElts.item(i);
			String paramName = dom.getAttributeValue(cssElt, "name");
			
			if (paramName.equalsIgnoreCase("stroke"))
			{
                String colorVal = dom.getElementValue(cssElt, "");
                stroke.setColor(new Color(colorVal.substring(1)));
			}
            else if (paramName.equalsIgnoreCase("stroke-red"))
            {
                ScalarParameter red = cssReader.readCssParameter(dom, cssElt);
                stroke.getColor().setRed(red);
            }
            else if (paramName.equalsIgnoreCase("stroke-green"))
            {
                ScalarParameter green = cssReader.readCssParameter(dom, cssElt);
                stroke.getColor().setGreen(green);
            }
            else if (paramName.equalsIgnoreCase("stroke-blue"))
            {
                ScalarParameter blue = cssReader.readCssParameter(dom, cssElt);
                stroke.getColor().setBlue(blue);
            }
			else if (paramName.equalsIgnoreCase("stroke-opacity"))
			{
				ScalarParameter opacity = cssReader.readCssParameter(dom, cssElt);
				stroke.setOpacity(opacity);
                stroke.getColor().setAlpha(opacity);
			}            
			else if (paramName.equalsIgnoreCase("stroke-width"))
			{
				ScalarParameter width = cssReader.readCssParameter(dom, cssElt);
				stroke.setWidth(width);
			}
			else if (paramName.equalsIgnoreCase("stroke-linejoin"))
			{
				ScalarParameter linejoin = cssReader.readCssParameter(dom, cssElt);
				stroke.setLinejoin(linejoin);
			}
			else if (paramName.equalsIgnoreCase("stroke-linecap"))
			{
				ScalarParameter linecap = cssReader.readCssParameter(dom, cssElt);
				stroke.setLinecap(linecap);
			}
			else if (paramName.equalsIgnoreCase("stroke-dasharray"))
			{
				ScalarParameter dasharray = cssReader.readCssParameter(dom, cssElt);
				stroke.setDasharray(dasharray);
			}
			else if (paramName.equalsIgnoreCase("stroke-dashoffset"))
			{
				ScalarParameter dashoffset = cssReader.readCssParameter(dom, cssElt);
				stroke.setDashoffset(dashoffset);
			}
		}
		
		return stroke;
	}
	
	
	/**
	 * Reads a Font Element and fill up the corresponding object
	 * @param dom
	 * @param fontElt
	 * @return
	 */
	public Font readFont(DOMReader dom, Element fontElt)
	{
		Font font = new Font();
		if (fontElt == null)
			return font;
		
		// read all css parameters
		NodeList cssElts = dom.getElements(fontElt, "CssParameter");
		for (int i=0; i<cssElts.getLength(); i++)
		{
			Element cssElt = (Element)cssElts.item(i);
			String paramName = dom.getAttributeValue(cssElt, "name");
			
			if (paramName.equalsIgnoreCase("font-family"))
			{
				ScalarParameter family = cssReader.readCssParameter(dom, cssElt);
				font.setFamily(family);
			}
			else if (paramName.equalsIgnoreCase("font-style"))
			{
				ScalarParameter style = cssReader.readCssParameter(dom, cssElt);
				font.setStyle(style);
			}
			else if (paramName.equalsIgnoreCase("font-weight"))
			{
				ScalarParameter weight = cssReader.readCssParameter(dom, cssElt);
				font.setWeight(weight);
			}
			else if (paramName.equalsIgnoreCase("font-size"))
			{
				ScalarParameter size = cssReader.readCssParameter(dom, cssElt);
				font.setSize(size);
			}
		}
		
		return font;
	}
}
