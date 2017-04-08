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

import java.util.*;


/**
 * <p>
 * SLD Graphic object (represents a symbol).
 * Allows to specify graphic opacity, size and rotation.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 11, 2005
 * */
public class Graphic
{
	protected ScalarParameter opacity;
	protected ScalarParameter size;
	protected ScalarParameter rotation;
	protected ScalarParameter spacing; // min distance between edges of icons
	protected List<GraphicSource> glyphs;

	
	public Graphic()
	{
		glyphs = new ArrayList<GraphicSource>(1);
	}
	

	public List<GraphicSource> getGlyphs()
	{
		return glyphs;
	}


	public void setGlyphs(List<GraphicSource> images)
	{
		this.glyphs = images;
	}


	public ScalarParameter getOpacity()
	{
		return opacity;
	}


	public void setOpacity(ScalarParameter opacity)
	{
		this.opacity = opacity;
	}


	public ScalarParameter getRotation()
	{
		return rotation;
	}


	public void setRotation(ScalarParameter rotation)
	{
		this.rotation = rotation;
	}


	public ScalarParameter getSize()
	{
		return size;
	}


	public void setSize(ScalarParameter size)
	{
		this.size = size;
	}


	public ScalarParameter getSpacing()
	{
		return spacing;
	}


	public void setSpacing(ScalarParameter spacing)
	{
		this.spacing = spacing;
	}
}
