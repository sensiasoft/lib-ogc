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
 * Point Symbolizer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * SLD Point Symbolizer object.
 * Allows to specify point geometry and symbols.
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @date Nov 10, 2005
 * @version 1.0
 */
public class VectorSymbolizer extends Symbolizer
{
	public enum CoordsType
    {
        DIFF, POLAR, ABS
    }
    
    protected Graphic graphic;
	protected ScalarParameter dx, dy, dz;
    protected ScalarParameter dth, dphi, dr;
    protected CoordsType coordinateType;
    
    
	public Graphic getGraphic()
	{
		return graphic;
	}


	public void setGraphic(Graphic graphic)
	{
		this.graphic = graphic;
	}


    public CoordsType getCoordinateType()
    {
        return coordinateType;
    }


    public void setCoordinateType(CoordsType coordinateType)
    {
        this.coordinateType = coordinateType;
    }


    public ScalarParameter getDphi()
    {
        return dphi;
    }


    public void setDphi(ScalarParameter dphi)
    {
        this.dphi = dphi;
    }


    public ScalarParameter getDr()
    {
        return dr;
    }


    public void setDr(ScalarParameter dr)
    {
        this.dr = dr;
    }


    public ScalarParameter getDth()
    {
        return dth;
    }


    public void setDth(ScalarParameter dth)
    {
        this.dth = dth;
    }


    public ScalarParameter getDx()
    {
        return dx;
    }


    public void setDx(ScalarParameter dx)
    {
        this.dx = dx;
    }


    public ScalarParameter getDy()
    {
        return dy;
    }


    public void setDy(ScalarParameter dy)
    {
        this.dy = dy;
    }


    public ScalarParameter getDz()
    {
        return dz;
    }


    public void setDz(ScalarParameter dz)
    {
        this.dz = dz;
    }
}
