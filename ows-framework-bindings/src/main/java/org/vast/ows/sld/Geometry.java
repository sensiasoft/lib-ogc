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
 * SLD Geometry object.
 * Allows to define where to get x,y,z,t coordinates
 * and breaks.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 11, 2005
 * */
public class Geometry
{
	protected String propertyName;
    protected String crs;
	protected ScalarParameter x;
	protected ScalarParameter y;
	protected ScalarParameter z;
	protected ScalarParameter t;
    protected ScalarParameter breaks;
	protected ScalarParameter object;


    public String getPropertyName()
	{
		return propertyName;
	}


    public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}
    
    
    public String getCrs()
    {
        return crs;
    }


    public void setCrs(String crs)
    {
        this.crs = crs;
    }


    public ScalarParameter getT()
	{
		return t;
	}


    public void setT(ScalarParameter t)
	{
		this.t = t;
	}


    public ScalarParameter getX()
	{
		return x;
	}


    public void setX(ScalarParameter x)
	{
		this.x = x;
	}


    public ScalarParameter getY()
	{
        return y;
	}


    public void setY(ScalarParameter y)
	{
		this.y = y;
	}


    public ScalarParameter getZ()
	{
        return z;
	}


    public void setZ(ScalarParameter z)
	{
		this.z = z;
	}
    
    
    public ScalarParameter getBreaks()
    {
        return breaks;
    }


    public void setBreaks(ScalarParameter breaks)
    {
        this.breaks = breaks;
    }
    
    
    public ScalarParameter getObject()
    {
        return object;
    }


    public void setObject(ScalarParameter object)
    {
        this.object = object;
    }
}
