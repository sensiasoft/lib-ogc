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
 * SLD Point Symbolizer object.
 * Allows to specify point geometry and symbols.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 10, 2005
 * */
public class VectorSymbolizer extends Symbolizer
{
    public enum DirectionType {DIFF, ROT, ABS};
    
    protected Geometry direction;
    protected DirectionType directionType = DirectionType.DIFF;
    protected ScalarParameter length;
    protected Stroke stroke;
    protected ScalarParameter numberOfSteps;
	protected Fill fill;
    protected Graphic graphic;


    public VectorSymbolizer()
    {
        length = new ScalarParameter();
        length.setConstantValue(new Double(1.0));
        numberOfSteps = new ScalarParameter();
        numberOfSteps.setConstantValue(new Float(10.0));
    }
    
    
    public ScalarParameter getNumberOfSteps() {
		return numberOfSteps;
	}

    
	public void setNumberOfSteps(ScalarParameter numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}


    public Geometry getDirection()
    {
        if (direction == null)
            direction = new Geometry();
        
        return direction;
    }


    public void setDirection(Geometry direction)
    {
        this.direction = direction;
    }
    
    
    public DirectionType getDirectionType()
    {
        return directionType;
    }


    public void setDirectionType(DirectionType directionType)
    {
        this.directionType = directionType;
    }


    public ScalarParameter getLength()
    {
        return length;
    }


    public void setLength(ScalarParameter length)
    {
        this.length = length;
    }
    
    
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
    
    
    public Fill getFill()
    {
        if (fill == null)
            fill = new Fill();
        
        return fill;
    }


    public void setFill(Fill fill)
    {
        this.fill = fill;
    }
    
    
    public Graphic getGraphic()
    {
        return graphic;
    }


    public void setGraphic(Graphic graphic)
    {
        this.graphic = graphic;
    }
}
