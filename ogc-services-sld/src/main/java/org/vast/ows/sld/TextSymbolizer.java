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
 * SLD Text Symbolizer object.
 * Allows to specify labels, placement, font, halo, and fill.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 10, 2005
 * */
public class TextSymbolizer extends Symbolizer
{
	protected ScalarParameter label;
	protected Font font;
	protected LabelPlacement placement;
	protected Halo halo;
	protected Fill fill = new Fill();


	public Fill getFill()
	{
		return fill;
	}


	public void setFill(Fill fill)
	{
		this.fill = fill;
	}


	public Font getFont()
	{
		return font;
	}


	public void setFont(Font font)
	{
		this.font = font;
	}


	public Halo getHalo()
	{
		return halo;
	}


	public void setHalo(Halo halo)
	{
		this.halo = halo;
	}


	public ScalarParameter getLabel()
	{
		return label;
	}


	public void setLabel(ScalarParameter label)
	{
		this.label = label;
	}


	public LabelPlacement getPlacement()
	{
		return placement;
	}


	public void setPlacement(LabelPlacement placement)
	{
		this.placement = placement;
	}
}
