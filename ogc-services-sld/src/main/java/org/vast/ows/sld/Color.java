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
 * Stores an RGBA color as 4 scalar parameters and provides
 * utility methods to deal with colors.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @date Nov 11, 2005
 * */
public class Color
{
    protected ScalarParameter red;
    protected ScalarParameter green;
    protected ScalarParameter blue;
    protected ScalarParameter alpha;
        
    
    public static String toHexString(float r, float g, float b, boolean bgr)
    {
        String red = Integer.toHexString((int)(r*255.0f));
        String green = Integer.toHexString((int)(g*255.0f));
        String blue = Integer.toHexString((int)(b*255.0f));
        
        // make sure we always have 2 digits!
        if (red.length() == 1)
            red = "0" + red;
        if (green.length() == 1)
            green = "0" + green;
        if (blue.length() == 1)
            blue = "0" + blue;
        
        if (bgr)
            return blue + green + red;
        else
            return red + green + blue;
    }
    
    
    public static String toHexString(float r, float g, float b, float a, boolean abgr)
    {
        String color = toHexString(r, g, b, abgr);
        String alpha = Integer.toHexString((int)(a*255.0f));
        
        // make sure we always have 2 digits!
        if (alpha.length() == 1)
            alpha = "0" + alpha;
        
        if (abgr)
            return alpha + color;
        else
            return color + alpha;
    }


    public Color()
    {
        red = new ScalarParameter();
        red.setConstantValue(new Float(0.0f));
        green = new ScalarParameter();
        green.setConstantValue(new Float(0.0f));
        blue = new ScalarParameter();
        blue.setConstantValue(new Float(0.0f));
        alpha = new ScalarParameter();
        alpha.setConstantValue(new Float(1.0f));
    }


    /**
     * Constructor using float values between 0.0 and 1.0
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public Color(float redVal, float greenVal, float blueVal, float alphaVal)
    {
        this();
        red.setConstantValue(redVal);
        green.setConstantValue(greenVal);
        blue.setConstantValue(blueVal);
        alpha.setConstantValue(alphaVal);
    }


    /**
     * Constructor using integer component values between 0 and 255
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public Color(int redVal, int greenVal, int blueVal, int alphaVal)
    {
        this();
        red.setConstantValue((float)redVal / 255.0f);
        green.setConstantValue((float)greenVal / 255.0f);
        blue.setConstantValue((float)blueVal / 255.0f);
        alpha.setConstantValue((float)alphaVal / 255.0f);
    }


    /**
     * Constructor using an hexadecimal RGB value
     * @param hexValue
     */
    public Color(String hexValue)
    {
        this();
        setFromHexValue(hexValue);
    }
    
    
    public boolean isConstant()
    {
        if (!red.isConstant())
            return false;
        
        if (!green.isConstant())
            return false;
        
        if (!blue.isConstant())
            return false;
        
        if (!alpha.isConstant())
            return false;
        
        return true;
    }
    
    
    public void setFromHexValue(String hexValue)
    {
        // red
        String rVal = hexValue.substring(0, 2);
        red.setConstantValue(((float) Integer.parseInt(rVal, 16)) / 255.0f);

        // green
        String gVal = hexValue.substring(2, 4);
        green.setConstantValue(((float) Integer.parseInt(gVal, 16)) / 255.0f);

        // blue
        String bVal = hexValue.substring(4, 6);
        blue.setConstantValue(((float) Integer.parseInt(bVal, 16)) / 255.0f);

        alpha.setConstantValue(1.0f);
    }


    public ScalarParameter getAlpha()
    {
        return alpha;
    }


    public void setAlpha(ScalarParameter alpha)
    {
        this.alpha = alpha;
    }


    public ScalarParameter getBlue()
    {
        return blue;
    }


    public void setBlue(ScalarParameter blue)
    {
        this.blue = blue;
    }


    public ScalarParameter getGreen()
    {
        return green;
    }


    public void setGreen(ScalarParameter green)
    {
        this.green = green;
    }
    
    
    public ScalarParameter getRed()
    {
        return red;
    }


    public void setRed(ScalarParameter red)
    {
        this.red = red;
    }


    public float getAlphaValue()
    {
        return (Float)alpha.getConstantValue();
    }


    public void setAlphaValue(float alphaVal)
    {
        alpha.setConstantValue(alphaVal);
    }


    public float getBlueValue()
    {
        return (Float)blue.getConstantValue();
    }


    public void setBlueValue(float blueVal)
    {
        blue.setConstantValue(blueVal);
    }


    public float getGreenValue()
    {
        return (Float)green.getConstantValue();
    }


    public void setGreenValue(float greenVal)
    {
        green.setConstantValue(greenVal);
    }
    
    
    public float getRedValue()
    {
        return (Float)red.getConstantValue();
    }


    public void setRedValue(float redVal)
    {
        red.setConstantValue(redVal);
    }
}
