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
 Tony Cook <tcook@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.ows.wcs;

import org.vast.ows.sos.SOSResponseSerializerV10;


/**
 * <p><b>Title:</b>
 * WCS Response Serializer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Write SWE XML response in a WCS
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin, Tony Cook
 * @date Jun 21, 2005
 * @version 1.0
 */
public class WCSResponseSerializer extends SOSResponseSerializerV10
{
    int bytesPerPoint; //  used only by BinaryBlock encoding?


    public WCSResponseSerializer()
    {
    }


    public void setBytesPerPoint(int bpp)
    {
        //  set Encoding Dimension
        bytesPerPoint = bpp;
    }

    /* TODO for SRTM support unless we use variable array size...
    public void setGridDimension(int xdim, int ydim)
    {
        this.setGridDimension(xdim, ydim, 1);
    }


    //  Maybe this should only be in derived classes, as it is already being a pain to generalize
    //  between Goes and SRTM
    //  Leaving it now.  SRTMHandler is the only class still calling this.  TC 6/13/06
    public void setGridDimension(int xdim, int ydim, int zdim)
    {
        // set numRows
        Element elt = dom.getElement(obsElt, "resultDefinition/DataBlockDefinition/components/DataArray");
        elt.setAttribute("arraySize", "" + ydim);
        // set numCols
        Element ncElt = dom.getElement("result/Data/definition/DataDefinition/dataComponents/DataArray/" + "component/DataArray");
        ncElt.setAttribute("arraySize", "" + xdim);

        //  TODO:  support zdim
        int dim = xdim * ydim * zdim;
        Element blElt = dom.getElement("result/Data/definition/DataDefinition/encoding/BinaryBlock");
        if (blElt != null)
            blElt.setAttribute("byteLength", "" + (dim * bytesPerPoint));
    }


    //  Just like setGridDim, but gets the second occurence of the dimension elements
    public void setImageDimension(int width, int height)
    {
        // set numRows
        NodeList nl = dom.getElements("result/Data/definition/DataDefinition/dataComponents/" + "DataGroup/component/DataArray");
        Element elt = (Element) nl.item(1);
        elt.setAttribute("arraySize", "" + height);
        // set numCols
        nl = dom.getElements("result/Data/definition/DataDefinition/dataComponents/" + "DataGroup/component/DataArray/component/DataArray");
        elt = (Element) nl.item(1);
        elt.setAttribute("arraySize", "" + width);
    }
    */
}